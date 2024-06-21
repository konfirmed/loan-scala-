import java.io.{BufferedWriter, FileWriter}

class Logging(logFilePath: String) {
  def log(message: String): Unit = {
    val writer = new BufferedWriter(new FileWriter(logFilePath, true))
    writer.write(s"$message\n")
    writer.close()
  }
}
