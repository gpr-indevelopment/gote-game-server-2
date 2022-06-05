import React, { useState, useCallback, useEffect } from "react";
import { Button, Divider, Space, Spin, notification } from "antd";
import { config } from "./constants";
import StatsExporter from "./StatsExporter";
import StatsDumper from "./StatsDumper";

export default function WebRTCVideo(props) {
  const [localPeer, setLocalPeer] = useState();
  const [startButtonDisabled, setStartButtonDisabled] = useState(true);
  const [stopButtonDisabled, setStopButtonDisabled] = useState(true);
  const [connectDisabled, setConnectDisabled] = useState(false);
  const [ws, setWs] = useState();
  const [loading, setLoading] = useState(false);

  let instantiateRtcPeer = () => {
    let rtcConfiguration = {
      iceServers: [
        { urls: "stun:stun.services.mozilla.com" },
        { urls: "stun:stun.l.google.com:19302" },
        {
          url: 'turn:numb.viagenie.ca',
          credential: 'muazkh',
          username: 'webrtc@live.com'
        },
      ]
    };
    return new RTCPeerConnection(rtcConfiguration);
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

  // Function to reset the frontend app to the initial state
  let resetState = useCallback(() => {
    setConnectDisabled(false);
    setStartButtonDisabled(true);
    setStopButtonDisabled(true);
    setLocalPeer(instantiateRtcPeer());
    setWs(null);
    sendWsMessage("STOP");
  }, [sendWsMessage]);

  // Global error handling function
  let errorHandler = useCallback(
    (error) => {
      console.log("Error found: " + error);
      notification.error({
        message: "An error was found on WebRTC",
        description: error,
      });
      resetState();
    },
    [resetState]
  );

  // WebRTC callbacks
  let gotLocalDescription = useCallback(
    (description) => {
      console.log("Local description:", description);
      localPeer.setLocalDescription(description).catch(errorHandler);
      sendWsMessage("SDP_ANSWER", description);
    },
    [localPeer, sendWsMessage, errorHandler]
  );

  let gotRemoteTrack = (event) => {
    let video = document.getElementById("stream");
    console.log("Incoming stream. Streams length: " + event.streams.length);
    video.srcObject = event.streams[0];
    setStopButtonDisabled(false);
    setLoading(false);
  };

  let gotLocalIceCandidate = (event) => {
    if (event.candidate) {
      console.log("Found local candidate:", event.candidate);
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
            localPeer.setRemoteDescription(data.payload).catch(errorHandler);
            localPeer
              .createAnswer(gotLocalDescription, errorHandler)
              .catch(errorHandler);
            break;
          case "ICE_CANDIDATE":
            console.log("Received ICE candidate. Will ignore: ", data.payload);
            // What to do if remote ICE candidates are not ignored.
            var ice = new RTCIceCandidate(data.payload);
            localPeer.addIceCandidate(ice).catch(errorHandler);
            break;
          case "FINISH_STATS_DUMP":
            console.log(data.payload);
            let timestampSeconds = data.payload.deltaTimestamp / 1000;
            notification.success({
              message: "CSV stats dump successful!",
              description: `Total records: ${data.payload.totalRecords}. Delta timestamp: ${timestampSeconds}s. SessionID: ${data.payload.sessionId}`,
              duration: 0,
            });
            break;
          default:
            console.log("Unknown messageType. Will ignore: ", data.messageType);
            break;
        }
      } else {
        console.log("Not typed message received: ", data);
      }
    },
    [localPeer, gotLocalDescription, errorHandler]
  );

  // Button callbacks
  let onStartClick = () => {
    setLoading(true);
    setStartButtonDisabled(true);
    sendWsMessage("START");
    localPeer.onicecandidate = gotLocalIceCandidate;
    localPeer.ontrack = gotRemoteTrack;
  };

  let onStopClick = useCallback(() => {
    setLocalPeer(instantiateRtcPeer());
    sendWsMessage("STOP");
    setStartButtonDisabled(false);
    setStopButtonDisabled(true);
  }, [sendWsMessage]);

  let onConnectClick = () => {
    setLoading(true);
    setWs(new WebSocket(config.ws));
    setLocalPeer(instantiateRtcPeer());
    setConnectDisabled(true);
    setLoading(false);
  };

  useEffect(() => {
    if (ws) {
      ws.onopen = onWebSocketOpen;
      ws.onmessage = onWebSocketMessage;
      ws.onclose = () => errorHandler("Connection with WebSocket closed");
    }
  }, [ws, onWebSocketOpen, onWebSocketMessage, errorHandler]);

  return (
    <Spin spinning={loading}>
      <div className="container">
        <video id="stream" autoPlay playsInline>
          Your browser doesn't support video
        </video>
        <Divider />
        <Space>
          <Button
            type="primary"
            disabled={connectDisabled}
            onClick={onConnectClick}
            size="large"
            style={{
              width: 120,
            }}
          >
            Connect
          </Button>
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
      <Divider />
      <div className="container">
        <Space>
          <StatsExporter peerConnection={localPeer} webSocket={ws} />
          <StatsDumper webSocket={ws} />
        </Space>
      </div>
    </Spin>
  );
}
