package sensor
import Sensor.{fetchFiles}
import SensorStatistics.{folder, data, failed, formatted}
import scala.util.Failure
import scala.util.Success

object SensorProgram {
  def main(args: Array[String]): Unit = {
    val asciiText: String = """
   _____                              _____ _        _   _     _   _          
  / ____|                            / ____| |      | | (_)   | | (_)         
 | (___   ___ _ __  ___  ___  _ __  | (___ | |_ __ _| |_ _ ___| |_ _  ___ ___ 
  \___ \ / _ | '_ \/ __|/ _ \| '__|  \___ \| __/ _` | __| / __| __| |/ __/ __|
  ____) |  __| | | \__ | (_) | |     ____) | || (_| | |_| \__ | |_| | (__\__ \
 |_____/ \___|_| |_|___/\___/|_|    |_____/ \__\__,_|\__|_|___/\__|_|\___|___/
                                                                                                                                                        
    """ // Generate from https://patorjk.com/

    fetchFiles(folder) match {
      case Success(_) =>
        println(asciiText)
        println(
          "Number of processed files: " + fetchFiles(folder).toOption
            .getOrElse(List.empty)
            .size
        )
        println("Number of processed measurements: " + data.size)
        println("Number of failed measurements: " + failed)
        println("")
        println("")
        println("Sensors with the highiest avg humidity:")
        println("")
        println("")
        println("sensor-id, min, ave, max")
        formatted.foreach(println)
      case Failure(exception) =>
        println(asciiText)
        println(s"An error occured: $exception")

    }
  }
}
