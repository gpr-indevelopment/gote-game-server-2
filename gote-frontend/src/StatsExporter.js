import React, { useState } from "react";
import { Button } from "antd";
import { VideoCameraOutlined } from "@ant-design/icons";

export default function StatsExporter(props) {
  const [loading, setLoading] = useState(false);
  let peerConnection = props.peerConnection;
  let ws = props.webSocket;
  const STATS_PUBLISH_INTERVAL = 1000;

  let publishStats = () => {
    peerConnection.getStats().then((stats) => {
      stats.forEach((report) => {
        if (report.type === "inbound-rtp" && report.kind === "video") {
          ws.send(
            JSON.stringify({
              messageType: "STATS_RECORD",
              payload: report,
            })
          );
        }
      });
    });
  };

  let handleStartStatsExport = () => {
    if (!peerConnection) {
      console.log("PeerConnection is null. Will ignore stats export.");
      return;
    }
    setLoading(true);
    let interval = window.setInterval(() => {
      if (peerConnection.connectionState !== "connected") {
        console.log(
          "Clearing stats window interval because PeerConnection is not connected. Interval ID: ",
          interval
        );
        clearInterval(interval);
        setLoading(false);
        return;
      }
      publishStats();
    }, STATS_PUBLISH_INTERVAL);
  };

  return (
    <Button
      danger
      size="large"
      onClick={handleStartStatsExport}
      icon={<VideoCameraOutlined />}
      loading={loading}
    >
      Record Stats
    </Button>
  );
}
