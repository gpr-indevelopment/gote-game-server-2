import React, { useState, useEffect, useCallback } from "react";
import { Button, Divider, Space, Spin } from "antd";

export default function WebRTCVideo(props) {
  const [localPeer, setLocalPeer] = useState(new RTCPeerConnection());
  const [startButtonDisabled, setStartButtonDisabled] = useState(true);
  const [stopButtonDisabled, setStopButtonDisabled] = useState(true);
  const [ws] = useState(new WebSocket("ws://localhost:8080/server"));
  const [loading, setLoading] = useState(false);

  // Global error handling function
  let errorHandler = (error) => {
    console.log("Found Error: " + error);
  };

  // Standard message format to backend
  let sendWsMessage = useCallback(
    (messageType, payload) => {
      ws.send(JSON.stringify(buildWebSocketPayload(messageType, payload)));
    },
    [ws]
  );

  let buildWebSocketPayload = (messageType, payload) => {
    return {
      messageType,
      payload,
    };
  };

  // WebRTC callbacks
  let gotLocalDescription = useCallback(
    (description) => {
      localPeer.setLocalDescription(description);
      sendWsMessage("SDP_ANSWER", description);
    },
    [localPeer, sendWsMessage]
  );

  let gotRemoteTrack = (event) => {
    let video = document.getElementById("stream");
    if (video.srcObject !== event.streams[0]) {
      console.log("Incoming stream. Streams length: " + event.streams.length);
      video.srcObject = event.streams[0];
      setStopButtonDisabled(false);
      setLoading(false);
    } else {
        errorHandler("Video source object is already filled!");
    }
  };

  let gotLocalIceCandidate = (event) => {
    if (event.candidate) {
      sendWsMessage("ICE_CANDIDATE", event.candidate);
    } else {
      console.log("All ICE candidates have been sent.");
    }
  };

  // WebSocket callbacks
  let onWebSocketOpen = useCallback(() => {
    setStartButtonDisabled(false);
  }, []);

  let onWebSocketMessage = useCallback(
    (message) => {
      let data = JSON.parse(message.data);
      if (data.messageType) {
        switch (data.messageType) {
          case "SDP_OFFER":
            console.log("Received SDP: ", data.payload);
            localPeer.setRemoteDescription(data.payload);
            localPeer.createAnswer(gotLocalDescription, errorHandler);
            break;
          case "ICE_CANDIDATE":
            console.log("Received ICE candidate: ", data.payload);
            var ice = new RTCIceCandidate(data.payload);
            localPeer.addIceCandidate(ice);
            break;
          default:
            console.log("Unknown messageType. Will ignore: ", data.messageType);
            break;
        }
      } else {
        console.log("Not typed message received: ", data);
      }
    },
    [localPeer, gotLocalDescription]
  );

  let onStartClick = () => {
      setLoading(true);
      setStartButtonDisabled(true);
    sendWsMessage("START");
    localPeer.onicecandidate = gotLocalIceCandidate;
    localPeer.ontrack = gotRemoteTrack;
  };

  let onStopClick = () => {
    setLocalPeer(new RTCPeerConnection());
    sendWsMessage("STOP");
    setStartButtonDisabled(false);
    setStopButtonDisabled(true);
  }

  useEffect(() => {
    ws.onopen = onWebSocketOpen;
    ws.onmessage = onWebSocketMessage;
  }, [ws, onWebSocketOpen, onWebSocketMessage]);

  return (
    <Spin spinning={loading}>
      <div className="container">
        <video id="stream" autoPlay playsInline>
          Your browser doesn't support video
        </video>
        <Divider />
        <Space>
          <Button
            id="start"
            type="primary"
            disabled={startButtonDisabled}
            onClick={onStartClick}
            size="large"
            style={{
              width: 120,
            }}
          >
            Start GOTE!
          </Button>
          <Button
            type="danger"
            size="large"
            style={{
              width: 120,
            }}
            disabled={stopButtonDisabled}
            onClick={onStopClick}
          >
            Stop
          </Button>
        </Space>
      </div>
    </Spin>
  );
}
