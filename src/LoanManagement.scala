import java.io.{BufferedWriter, FileWriter}
import scala.io.Source
import scala.util.control.Breaks._

case class Loan(loanId: Int, borrowerName: String, loanAmount: Double, interestRate: Double, loanTerm: Int, outstandingBalance: Double, lastInterestCalcDate: String)

class LoanManagement(loanFilePath: String) {
  def addLoan(): Unit = {
    val loan = getLoanInput()
    saveLoan(loan)
    println("Loan added successfully.")
  }

  def updateLoan(): Unit = {
    println("Enter Loan ID to update: ")
    val loanId = scala.io.StdIn.readInt()
    val loans = loadLoans()

    loans.find(_.loanId == loanId) match {
      case Some(existingLoan) =>
        val updatedLoan = getLoanInput(existingLoan.loanId)
        saveLoans(loans.map(loan => if (loan.loanId == loanId) updatedLoan else loan))
        println("Loan updated successfully.")
      case None =>
        println("Loan not found.")
    }
  }

  private def getLoanInput(existingLoanId: Int = 0): Loan = {
    val loanId = if (existingLoanId != 0) existingLoanId else {
      println("Enter Loan ID: ")
      scala.io.StdIn.readInt()
    }

    println("Enter Borrower Name: ")
    val borrowerName = scala.io.StdIn.readLine()
    println("Enter Loan Amount: ")
    val loanAmount = scala.io.StdIn.readDouble()
    println("Enter Interest Rate (e.g., 5.25): ")
    val interestRate = scala.io.StdIn.readDouble()
    println("Enter Loan Term (in months): ")
    val loanTerm = scala.io.StdIn.readInt()
    val outstandingBalance = loanAmount
    val lastInterestCalcDate = java.time.LocalDate.now.toString

    Loan(loanId, borrowerName, loanAmount, interestRate, loanTerm, outstandingBalance, lastInterestCalcDate)
  }

  private def saveLoan(loan: Loan): Unit = {
    val writer = new BufferedWriter(new FileWriter(loanFilePath, true))
    writer.write(s"${loan.loanId} ${loan.borrowerName} ${loan.loanAmount} ${loan.interestRate} ${loan.loanTerm} ${loan.outstandingBalance} ${loan.lastInterestCalcDate}\n")
    writer.close()
  }

  private def saveLoans(loans: Seq[Loan]): Unit = {
    val writer = new BufferedWriter(new FileWriter(loanFilePath))
    loans.foreach(loan => writer.write(s"${loan.loanId} ${loan.borrowerName} ${loan.loanAmount} ${loan.interestRate} ${loan.loanTerm} ${loan.outstandingBalance} ${loan.lastInterestCalcDate}\n"))
    writer.close()
  }

  private def loadLoans(): Seq[Loan] = {
    Source.fromFile(loanFilePath).getLines().map { line =>
      val Array(loanId, borrowerName, loanAmount, interestRate, loanTerm, outstandingBalance, lastInterestCalcDate) = line.split("\\s+")
      Loan(loanId.toInt, borrowerName, loanAmount.toDouble, interestRate.toDouble, loanTerm.toInt, outstandingBalance.toDouble, lastInterestCalcDate)
    }.toSeq
  }
}
