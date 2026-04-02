import scala.io.Source

case class RouteRule(method: String, pathSegments: List[String], target: String)

object RouteConfig:

  def load(path: String): List[RouteRule] =
    val source = Source.fromFile(path)
    try
      source.getLines()
        .map(_.trim)
        .filter(line => line.nonEmpty && !line.startsWith("#"))
        .map(parseLine)
        .toList
    finally source.close()

  private def parseLine(line: String): RouteRule =
    line.split("\\s+") match
      case Array(method, path, target) =>
        RouteRule(method.toUpperCase, path.stripPrefix("/").split("/").toList, target)
      case _ =>
        throw IllegalArgumentException(s"Invalid route: $line")
