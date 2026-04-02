object EventProcessor:

  def processEvent(event: ServerEvent): String = event match
    case ServerUp(region) =>
      s"Traffic routed safely to $region."

    case ServerDown(region, 500) =>
      s"Critical failure in $region! Triggering automated failover."

    case ServerDown(region, code) =>
      s"Minor issue in $region (Code: $code). Logging event."

    case HighLoad(region, cpu) if cpu > 90.0 =>
      s"CPU at $cpu% in $region. Provisioning new instances."

    case HighLoad(_, _) =>
      "Load is high but under critical threshold. Monitoring."
