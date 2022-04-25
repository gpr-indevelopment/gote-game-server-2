// Setup HTML components and global objects
var startButton = document.getElementById("start");
var video = document.getElementById("stream");
var ws = new WebSocket("ws://localhost:8080/server");
var localPeer = new RTCPeerConnection();

// Start button is disabled until we can start the session
startButton.disabled = true;

// Global error handling function
var errorHandler = (error) => {
  console.log("Found Error: " + error);
};

// WebSocket callbacks
ws.onopen = () => {
  startButton.disabled = false;
};

ws.onmessage = (message) => {
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
    }
  } else {
    console.log("Not typed message received: ", data);
  }
};

// Start button click listener
startButton.onclick = () => {
  sendWsMessage("START");
  localPeer.onicecandidate = gotLocalIceCandidate;
  localPeer.ontrack = gotRemoteTrack;
};

// WebRTC callbacks
let gotLocalDescription = (description) => {
  localPeer.setLocalDescription(description);
  sendWsMessage("SDP_ANSWER", description);
};

let gotRemoteTrack = (event) => {
  if (video.srcObject !== event.streams[0]) {
    console.log("Incoming stream. Streams length: " + event.streams.length);
    video.srcObject = event.streams[0];
  }
};

let gotLocalIceCandidate = (event) => {
  if (event.candidate) {
    sendWsMessage("ICE_CANDIDATE", event.candidate);
  } else {
    console.log("All ICE candidates have been sent.");
  }
};

// Standard message format to backend
let sendWsMessage = (messageType, payload) => {
  ws.send(JSON.stringify(buildWebSocketPayload(messageType, payload)));
};

let buildWebSocketPayload = (messageType, payload) => {
  return {
    messageType,
    payload,
  };
};
