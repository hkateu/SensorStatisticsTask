package sensor
import Sensor.{fetchFiles, readAll, SensorData}
import scala.util.Failure
import scala.util.Success

object SensorStatistics {

  /** folder is the relative path to the directory with sensor data
    */
  lazy val folder: String = "src/main/scala/sensor/data"

  /** data holds the fetched data stored as type SensorData
    */

  lazy val data: List[SensorData] = readAll(folder)
    .flatMap(_.toList)
    .map(_.asInstanceOf[SensorData])

  /** failed holds the number of Nones in the sequence which represent 'NaN'
    * values
    */
  lazy val failed: Int = data.filter(_.humidity == None).size

  /** The data is then grouped by sensor-id for easy statitical calculations
    */
  lazy val groupedData: Map[String, List[Option[Double]]] = data
    .map { case SensorData(id, value) =>
      (id -> value)
    }
    .groupBy(_._1)
    .map { case (k -> v) =>
      (k -> v.map(_._2))
    }

  /** sumsByKey sums humidity data by sensor-id and stores values in a Map
    */
  lazy val sumsByKey: Map[String, Option[Double]] = groupedData.map {
    case (k -> v) =>
      (
        k -> v.filterNot(_ == None).foldRight(Option(0.0)) { (n1, n2) =>
          for {
            r <- n1
            s <- n2
          } yield (r + s)
        }
      )
  }

  /** sizeByKey stores the number of rows of data per sensor-id
    */
  lazy val sizeByKey: Map[String, Double] = groupedData.map { case (k -> v) =>
    (k -> v.filterNot(_ == None).size.toDouble)
  }

  /** sumsByKey and sumsByKey are used to calculate the average humidity grouped
    * by sensor-id
    */
  lazy val average: Map[String, Option[Double]] = sumsByKey.map {
    case (k -> v) =>
      if (sizeByKey(k) == 0) {
        (k -> None)
      } else {
        (k -> Some(v.get / sizeByKey(k)))
      }
  }

  /** min calculates min value
    */
  def min(x: Option[Double], y: Option[Double]): Option[Double] = {
    if (x.get < y.get) x else y
  }

  /** max calculates a max value
    */
  def max(x: Option[Double], y: Option[Double]): Option[Double] = {
    if (x.get > y.get) x else y
  }

  /** Compare processes different option combinations
    */
  def compare(
      x: Option[Double],
      y: Option[Double],
      f: (Option[Double], Option[Double]) => Option[Double]
  ): Option[Double] = {
    (x, y) match {
      case (None, None)       => None
      case (None, Some(x))    => Some(x)
      case (Some(x), None)    => Some(x)
      case (Some(x), Some(y)) => f(Some(x), Some(y))
    }
  }

  /** minAveMax holds a sorted Array of Tuples containing sensor-id, min, ave,
    * and max
    */
  lazy val minAveMax
      : Array[(String, Option[Double], Option[Double], Option[Double])] =
    groupedData
      .map { case (k -> v) =>
        (
          k,
          v.reduce((x, y) => compare(x, y, min)),
          average(k),
          v.reduce((x, y) => compare(x, y, max))
        )
      }
      .toArray
      .sortWith((d1, d2) => Ordering[Option[Double]].gt(d1._3, d2._3))

  /** formating minAveMax for printing to console
    */
  lazy val formatted: Array[String] = minAveMax
    .map { case (v, x, y, z) =>
      s"$v, ${x.getOrElse("NaN")}, ${y.getOrElse("NaN")}, ${z.getOrElse("NaN")}"
    }

}
