const prod = {
  ws: "ws://" + window.location.host + "/server",
  rest: ""
};
const dev = {
  ws: "ws://localhost:8080/server",
  rest: "http://localhost:8080"
};
export const config = process.env.NODE_ENV === "development" ? dev : prod;
