sealed trait ServerEvent

case class ServerUp(region: String) extends ServerEvent
case class ServerDown(region: String, errorCode: Int) extends ServerEvent
case class HighLoad(region: String, cpuUsage: Double) extends ServerEvent
