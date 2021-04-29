package net.stettix.essentialeffects

import cats.effect._
import debug._

import scala.io.Source

object EarlyRelease extends IOApp.Simple {

  override def run: IO[Unit] =
    dbConnectionResource.use { conn =>
      conn.runQuery("SELECT * FROM foo WHERE bar = 42").debug.void
    }

  val dbConnectionResource: Resource[IO, DbConnection] =
    for {
      config <- Resource.liftK(config)
      dbConnection <- DbConnection.make(config.connectUrl)
    } yield dbConnection

  lazy val config: IO[Config] = sourceResource.use(Config.fromSource)

  lazy val sourceResource: Resource[IO, Source] =
    Resource.make(IO(s"> Opening Source to config").debug *> IO(Source.fromString(configContent)))(
      source => IO(s"< Closing Source to config").debug *> IO(source.close())
    )

  val configContent = "exampleConnectUrl"
}

case class Config(connectUrl: String)

object Config {

  def fromSource(source: Source): IO[Config] =
    for {
      config <- IO(Config(source.getLines().next()))
      _ <- IO(s"Read $config").debug
    } yield config
}

trait DbConnection {

  def runQuery(query: String): IO[String]

}

object DbConnection {

  def make(connectUrl: String): Resource[IO, DbConnection] =
    Resource.make(IO(s"> Opening connection to $connectUrl").debug *> createConnection)(
      _ => IO(s"< Closing connection to $connectUrl")
    )

  private val createConnection = IO(new DbConnection {
    override def runQuery(query: String) = IO(s"Results for query '$query'")
  })

}
