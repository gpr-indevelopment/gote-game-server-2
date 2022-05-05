import React from "react";
import { Button } from "antd";

export default function StatsDumper(props) {

  let ws = props.webSocket;
  let handleDumpStats = () => {
    ws.send(
      JSON.stringify({
        messageType: "START_STATS_DUMP",
      })
    );
  };

  return (
    <Button primary size="large" onClick={handleDumpStats}>
      Dump Stats
    </Button>
  );
}
