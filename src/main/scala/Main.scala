package reproducer

import cats.effect.std.Random
import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s._
import org.asynchttpclient.DefaultAsyncHttpClientConfig
import org.http4s.HttpRoutes
import org.http4s.asynchttpclient.client.AsyncHttpClient
import org.http4s.dsl.Http4sDsl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.GZip

object Main extends IOApp with Http4sDsl[IO] {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req => IO.println(req.headers) >> Ok("Foo")
  }

  override def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder.default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(GZip(routes).orNotFound)
      .build
      .use { _ =>
        AsyncHttpClient.resource[IO](new DefaultAsyncHttpClientConfig.Builder().setCompressionEnforced(true).build()).use { client =>
          client.expect[String]("http://localhost:8080/").flatMap(IO.println)
        }.as(ExitCode.Success)
      }
}
