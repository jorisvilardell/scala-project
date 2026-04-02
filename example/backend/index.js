const express = require("express");
const app = express();
const port = 3000;

const users = [
  { id: 1, name: "Alice", email: "alice@example.com", role: "admin" },
  { id: 2, name: "Bob", email: "bob@example.com", role: "user" },
  { id: 3, name: "Charlie", email: "charlie@example.com", role: "user" },
];

const posts = [
  { id: 1, userId: 1, title: "Getting started with Scala", body: "Pattern matching is powerful." },
  { id: 2, userId: 2, title: "http4s reverse proxy", body: "Building proxies the functional way." },
];

app.get("/users", (req, res) => {
  console.log("-> GET /users");
  res.json(users);
});

app.get("/users/:id", (req, res) => {
  console.log(`-> GET /users/${req.params.id}`);
  const user = users.find((u) => u.id === parseInt(req.params.id));
  if (user) res.json(user);
  else res.status(404).json({ error: "User not found" });
});

app.get("/posts", (req, res) => {
  console.log("-> GET /posts");
  res.json(posts);
});

app.get("/posts/:id", (req, res) => {
  console.log(`-> GET /posts/${req.params.id}`);
  const post = posts.find((p) => p.id === parseInt(req.params.id));
  if (post) res.json(post);
  else res.status(404).json({ error: "Post not found" });
});

app.listen(port, () => {
  console.log(`Mock backend running on http://localhost:${port}`);
});
