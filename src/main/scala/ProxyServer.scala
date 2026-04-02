import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.comcast.ip4s.*
import org.http4s.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder

@main def main(): Unit =
  val rules = RouteConfig.load("routes.conf")
  println(s"Loaded ${rules.size} routing rules")
  rules.foreach(r => println(s"  ${r.method} /${r.pathSegments.mkString("/")} -> ${r.target}"))

  EmberClientBuilder.default[IO].build.use { client =>
    EmberServerBuilder
      .default[IO]
      .withHost(host"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(ReverseProxy.routes(client, rules).orNotFound)
      .build
      .useForever
  }.unsafeRunSync()
