# Reverse Proxy in Scala

A lightweight reverse proxy built with **http4s** and **Ember**, demonstrating Scala's pattern matching applied to real HTTP routing.

## Concepts covered

- **Pattern Matching on HTTP requests:** method + path deconstruction via http4s DSL
- **Resource-safe networking:** managed client connections with `cats-effect`
- **Route-level access control:** blocking patterns via pattern matching

## Project structure

```
src/main/scala/
├── ReverseProxy.scala   # Route definitions (pattern matching on requests)
└── ProxyServer.scala    # Ember HTTP server entry point
```

## Run

```bash
sbt "runMain ProxyServer"
```

## Demo

```bash
curl localhost:8080/api/users      # proxied to target service
curl localhost:8080/api/users/1    # proxied with path parameter
curl localhost:8080/admin          # 403 Forbidden
```
