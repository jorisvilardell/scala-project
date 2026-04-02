import cats.effect.IO
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.client.Client

object ReverseProxy:

  def routes(client: Client[IO], rules: List[RouteRule]): HttpRoutes[IO] =
    HttpRoutes.of[IO] { req =>
      matchRule(req, rules) match
        case Some(rule) if rule.target == "DENY" =>
          Forbidden("Access Denied: internal routing only.")
        case Some(rule) =>
          val targetUri = resolveUri(req, rule)
          client.run(req.withUri(targetUri)).use(resp => IO.pure(resp))
        case None =>
          NotFound(s"No route for ${req.method} ${req.uri.path}")
    }

  private def matchRule(req: Request[IO], rules: List[RouteRule]): Option[RouteRule] =
    val reqSegments = req.uri.path.segments.map(_.decoded()).toList
    rules.find { rule =>
      matchMethod(req.method, rule.method) && matchPath(reqSegments, rule.pathSegments)
    }

  private def matchMethod(reqMethod: Method, ruleMethod: String): Boolean =
    ruleMethod == "*" || reqMethod.name.equalsIgnoreCase(ruleMethod)

  private def matchPath(reqSegments: List[String], ruleSegments: List[String]): Boolean =
    reqSegments.length == ruleSegments.length &&
      reqSegments.zip(ruleSegments).forall { (req, rule) =>
        rule.startsWith(":") || req == rule
      }

  private def resolveUri(req: Request[IO], rule: RouteRule): Uri =
    val reqSegments = req.uri.path.segments.map(_.decoded()).toList
    val params = rule.pathSegments.zip(reqSegments).collect {
      case (param, value) if param.startsWith(":") => param -> value
    }.toMap

    var resolved = rule.target
    params.foreach { (param, value) => resolved = resolved.replace(param, value) }
    Uri.unsafeFromString(resolved)
