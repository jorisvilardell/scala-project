import cats.effect.IO
import org.http4s.*

object ProxyLogger:

  def proxy(method: Method, path: Uri.Path, target: Uri): IO[Unit] =
    IO.println(s"[PROXY]    $method $path -> $target")

  def response(method: Method, path: Uri.Path, status: Status, durationMs: Long): IO[Unit] =
    IO.println(s"[RESP]     $method $path <- $status (${durationMs}ms)")

  def deny(method: Method, path: Uri.Path): IO[Unit] =
    IO.println(s"[DENY]     $method $path -> blocked")

  def noMatch(method: Method, path: Uri.Path): IO[Unit] =
    IO.println(s"[NO MATCH] $method $path -> no route found")
