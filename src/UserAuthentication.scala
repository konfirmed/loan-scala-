import scala.io.Source
import scala.util.control.Breaks._

class UserAuthentication(userFilePath: String) {
  private val users = loadUsers()

  def authenticate(): Boolean = {
    println("Enter User ID: ")
    val userId = scala.io.StdIn.readLine()
    println("Enter Password: ")
    val password = scala.io.StdIn.readLine()

    users.exists { case (id, pass) => id == userId && pass == password }
  }

  private def loadUsers(): Map[String, String] = {
    Source.fromFile(userFilePath).getLines()
      .map(line => {
        val Array(userId, password) = line.split("\\s+")
        userId -> password
      }).toMap
  }
}
