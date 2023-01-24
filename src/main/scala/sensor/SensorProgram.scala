package sensor
import Sensor.{fetchFiles}
import SensorStatistics.{folder, data, failed, formatted}

object SensorProgram {
  def main(args: Array[String]): Unit = {
    println("Number of processed files: " + fetchFiles(folder).size)
    println("Number of processed measurements: " + data.size)
    println("Number of failed measurements: " + failed)
    println("")
    println("")
    println("Sensors with the highiest avg humidity:")
    println("")
    println("")
    println("sensor-id, min, ave, max")
    formatted.foreach(println)
  }
}
