package sensor
import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.Files
import java.nio.charset.Charset
import scala.jdk.CollectionConverters._
import scala.util.Try

object Sensor {
  case class SensorData(id: String, humidity: Option[Double])
  def fetchFiles(source: String) = {
    val filePath = Paths.get(source)
    val charSet = Charset.forName("UTF-8")
    val dirStream = Files.newDirectoryStream(filePath).asScala.toList
    dirStream
  }

  def readCsv(source: Path) = {
    val charSet = Charset.forName("UTF-8")
    val lines = Files.readAllLines(source, charSet)
    val processedLines = lines.asScala
      .map { x =>
        x.split(",") match {
          case Array(x, y) =>
            SensorData(
              x,
              (Try { y.toInt }.toOption) match {
                case Some(x) => Some(x.toDouble)
                case None    => None
              }
            )
          case _ => s"An error occured" // use illegalArgumentException
        }
      }
      .drop(1)
    processedLines
  }

  def readAll(sourceFolder: String) = {
    fetchFiles(sourceFolder).map(readCsv)
  }
}
