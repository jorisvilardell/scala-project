import cats.effect.IO
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.client.Client

object ReverseProxy:

  def routes(client: Client[IO], target: Uri): HttpRoutes[IO] = HttpRoutes.of[IO] {

    // Pattern match on method + path, forward to target
    case req @ GET -> Root / "api" / "users" =>
      val proxied = req.withUri(target / "users")
      client.run(proxied).use(resp => IO.pure(resp))

    case req @ GET -> Root / "api" / "users" / id =>
      val proxied = req.withUri(target / "users" / id)
      client.run(proxied).use(resp => IO.pure(resp))

    // Block admin routes
    case _ -> Root / "admin" =>
      Forbidden("Access Denied: internal routing only.")
  }
