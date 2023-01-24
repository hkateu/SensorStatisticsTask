package sensor
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Sensor.{SensorData, readAll, readCsv, fetchFiles}
import SensorStatistics.{
  data,
  failed,
  groupedData,
  sumsByKey,
  sizeByKey,
  min,
  max,
  compare,
  minAveMax,
  formatted
}

class SensorStatisticsSpec extends AnyFlatSpec with Matchers {
  "data" should "be a list of SensorData" in {
    data shouldBe an[List[SensorData]]
  }

  "val failed" should "be of type Int" in {
    failed shouldBe an[Int]
  }

  "groupedData" should "be a Map[String, List[..]]" in {
    groupedData shouldBe an[Map[String, List[Option[Double]]]]
  }

  "sumsByKey" should "be a Map of String and Option[Double]" in {
    sumsByKey shouldBe an[Map[String, Option[Double]]]
  }

  "sizeByKey" should "be a Map of String and Double" in {
    sizeByKey shouldBe an[Map[String, Double]]
  }

  "min(Some(2),Some(3))" should "return Some(2)" in {
    min(Some(2), Some(3)) should equal(Some(2))
  }

  "max(Some(2),Some(3))" should "return Some(3)" in {
    max(Some(2), Some(3)) should equal(Some(3))
  }

  "The compare method" should "hold for the following cases" in {
    compare(None, None, min) should equal(None)
    compare(None, Some(2), min) should equal(Some(2))
    compare(Some(3), None, max) should equal(Some(3))
    compare(Some(2), Some(3), max) should equal(max(Some(2), Some(3)))
  }

  "minAveMax" should "be an Array of one String, and 4 Option[Double]'s" in {
    minAveMax shouldBe an[Array[
      (String, Option[Double], Option[Double], Option[Double])
    ]]
  }

  "formatted" should "be an Array of String" in {
    formatted shouldBe an[Array[String]]
  }
}
