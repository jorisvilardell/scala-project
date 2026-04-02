import cats.effect.IO
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.client.Client

object ReverseProxy:

  def routes(client: Client[IO], rules: List[RouteRule]): HttpRoutes[IO] =
    HttpRoutes.of[IO] { req =>
      val start = System.currentTimeMillis()
      matchRule(req, rules) match
        case Some(rule) if rule.target == "DENY" =>
          ProxyLogger.deny(req.method, req.uri.path) *>
            Forbidden("Access Denied: internal routing only.")
        case Some(rule) =>
          val targetUri = resolveUri(req, rule)
          ProxyLogger.proxy(req.method, req.uri.path, targetUri) *>
            client.run(req.withUri(targetUri)).use { resp =>
              val duration = System.currentTimeMillis() - start
              ProxyLogger.response(req.method, req.uri.path, resp.status, duration) *>
                IO.pure(resp)
            }
        case None =>
          ProxyLogger.noMatch(req.method, req.uri.path) *>
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
