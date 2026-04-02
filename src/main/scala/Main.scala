// Live demo entry point — run this to show pattern matching in action.

@main def run(): Unit =
  val events: List[ServerEvent] = List(
    ServerUp("eu-west-1"),
    ServerDown("us-east-1", 500),
    ServerDown("ap-south-1", 403),
    HighLoad("eu-west-1", 95.3),
    HighLoad("us-east-1", 72.0)
  )

  println("=== Resilient Event Handling in Scala ===")
  println()

  for event <- events do
    println(s"Event:  $event")
    println(s"Result: ${EventProcessor.processEvent(event)}")
    println()
