# Reverse Proxy in Scala

A lightweight reverse proxy built with **http4s** and **Ember**, with file-based routing configuration.

## Project structure

```
├── routes.conf                    # Routing rules configuration
└── src/main/scala/
    ├── RouteConfig.scala          # Config file parser
    ├── ReverseProxy.scala         # Dynamic route matching & proxying
    └── ProxyServer.scala          # Entry point
```

## Configuration

Define routing rules in `routes.conf`:

```conf
# Format: METHOD PATH TARGET_URL
# Use * as method to match all HTTP methods
# Use :param for path parameters

GET   /api/users        https://jsonplaceholder.typicode.com/users
GET   /api/users/:id    https://jsonplaceholder.typicode.com/users/:id

# Blocked routes (target = DENY)
*     /admin            DENY
```

## Run

```bash
sbt run
```

## Demo

```bash
curl localhost:8080/api/users      # proxied to target service
curl localhost:8080/api/users/1    # proxied with path parameter
curl localhost:8080/admin          # 403 Forbidden
```
