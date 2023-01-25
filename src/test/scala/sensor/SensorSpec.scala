package sensor
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Sensor.{fetchFiles, readCsv, SensorData, readAll}
import SensorStatistics.{folder}
import java.nio.file.Path
import java.nio.file.Paths
import scala.collection.mutable.ArrayBuffer
import scala.util.Try

class SensorSpec extends AnyFlatSpec with Matchers {
  val folder = "src/test/scala/sensor/data"

  "The fetchFiles method" should "return a Try[List[Path]]" in {
    fetchFiles(folder) shouldBe an[Try[List[Path]]]
  }

  "The readCsv method" should "return an ArrayBuffer of SensorData" in {
    val file = "src/test/scala/sensor/data/leader-1.csv"
    val filePath = Paths.get(file)
    readCsv(filePath) shouldBe an[ArrayBuffer[SensorData]]
  }

  "The readAll method" should "return a List[ArrayBuffer[SensorData]]" in {
    readAll(folder) shouldBe an[List[ArrayBuffer[SensorData]]]
  }
}
