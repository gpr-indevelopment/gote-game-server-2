const prod = {
  url: "ws://" + window.location.host + "/server",
};
const dev = {
  url: "ws://localhost:8080/server",
};
export const config = process.env.NODE_ENV === "development" ? dev : prod;
