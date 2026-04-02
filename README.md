# Resilient Event Handling in Scala

Technical presentation demonstrating how Scala's **Pattern Matching** and **Case Classes** enable concise, type-safe event processing — without the verbosity of traditional approaches.

## Concepts covered

- **Algebraic Data Types (ADTs):** `sealed trait` + `case class` to model a closed set of events
- **Pattern Matching:** deconstruction, literal matching, guards, and exhaustivity checking
- **Immutability by default:** case classes produce immutable data structures out of the box

## Project structure

```
src/main/scala/
├── ServerEvent.scala      # ADT definition (sealed trait + case classes)
├── EventProcessor.scala   # Pattern matching logic with guards
└── Main.scala             # Demo entry point
```

## Run

```bash
sbt run
```

## Example output

```
=== Resilient Event Handling in Scala ===

Event:  ServerUp(eu-west-1)
Result: Traffic routed safely to eu-west-1.

Event:  ServerDown(us-east-1,500)
Result: Critical failure in us-east-1! Triggering automated failover.

Event:  ServerDown(ap-south-1,403)
Result: Minor issue in ap-south-1 (Code: 403). Logging event.

Event:  HighLoad(eu-west-1,95.3)
Result: CPU at 95.3% in eu-west-1. Provisioning new instances.

Event:  HighLoad(us-east-1,72.0)
Result: Load is high but under critical threshold. Monitoring.
```
