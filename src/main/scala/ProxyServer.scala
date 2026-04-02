import cats.effect.{IO, IOApp}
import com.comcast.ip4s.*
import org.http4s.*
import org.http4s.ember.client.EmberClient
import org.http4s.ember.server.EmberServerBuilder

object ProxyServer extends IOApp.Simple:

  // Target service to proxy requests to (e.g. jsonplaceholder for demo)
  val target: Uri = uri"https://jsonplaceholder.typicode.com"

  val run: IO[Unit] =
    EmberClient.default[IO].use { client =>
      EmberServerBuilder
        .default[IO]
        .withHost(host"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(ReverseProxy.routes(client, target).orNotFound)
        .build
        .useForever
    }
