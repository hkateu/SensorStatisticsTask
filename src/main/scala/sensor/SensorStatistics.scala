package sensor
import Sensor.{fetchFiles, readAll, SensorData}

object SensorStatistics {

  /** folder is the relative path to the directory with sensor data
    */
  val folder = "src/main/scala/sensor/data"

  /** sensorData holds the fetched data stored as SensorData
    */
  val data = readAll(folder).flatMap(_.toList).map(_.asInstanceOf[SensorData])

  /** failed holds the number of Nones in the sequence which represent 'NaN'
    * values
    */
  val failed = data.filter(_.humidity == None).size

  /** The data is then grouped by sensor-id for easy statitical calculations
    */
  val groupedData = data
    .map { case SensorData(id, value) =>
      (id -> value)
    }
    .groupBy(_._1)
    .map { case (k -> v) =>
      (k -> v.map(_._2))
    }

  /** sumsByKey sums humidity data by sensor-id and stores values in a Map
    */
  val sumsByKey = groupedData.map { case (k -> v) =>
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
  val sizeByKey = groupedData.map { case (k -> v) =>
    (k -> v.filterNot(_ == None).size.toDouble)
  }

  /** sumsByKey and sumsByKey are used to calculate the average humidity grouped
    * by sensor-id
    */
  val average = sumsByKey.map { case (k -> v) =>
    if (sizeByKey(k) == 0) {
      (k -> None)
    } else {
      (k -> Some(v.get / sizeByKey(k)))
    }
  }

  /** min calculates min value
    */
  def min(x: Option[Double], y: Option[Double]) = {
    (x, y) match {
      case (Some(x), Some(y)) => if (x < y) Some(x) else Some(y)
    }
  }

  /** max calculates a max value
    */
  def max(x: Option[Double], y: Option[Double]) = {
    (x, y) match {
      case (Some(x), Some(y)) => if (x > y) Some(x) else Some(y)
    }
  }

  /** Compare processes different option combinations
    */
  def compare(
      x: Option[Double],
      y: Option[Double],
      f: (Option[Double], Option[Double]) => Option[Double]
  ) = {
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
  val minAveMax = groupedData
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
  val formatted = minAveMax
    .map { case (v, x, y, z) =>
      s"$v, ${x.getOrElse("NaN")}, ${y.getOrElse("NaN")}, ${z.getOrElse("NaN")}"
    }

}
