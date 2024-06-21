import java.io.{BufferedWriter, FileWriter}
import scala.io.Source

case class Repayment(repaymentId: Int, loanId: Int, amountPaid: Double, paymentDate: String)

class RepaymentManagement(repaymentFilePath: String) {
  def recordRepayment(): Unit = {
    val repayment = getRepaymentInput()
    saveRepayment(repayment)
    updateLoanBalance(repayment)
    println("Repayment recorded successfully.")
  }

  private def getRepaymentInput(): Repayment = {
    println("Enter Repayment ID: ")
    val repaymentId = scala.io.StdIn.readInt()
    println("Enter Loan ID: ")
    val loanId = scala.io.StdIn.readInt()
    println("Enter Amount Paid: ")
    val amountPaid = scala.io.StdIn.readDouble()
    println("Enter Payment Date (YYYY-MM-DD): ")
    val paymentDate = scala.io.StdIn.readLine()

    Repayment(repaymentId, loanId, amountPaid, paymentDate)
  }

  private def saveRepayment(repayment: Repayment): Unit = {
    val writer = new BufferedWriter(new FileWriter(repaymentFilePath, true))
    writer.write(s"${repayment.repaymentId} ${repayment.loanId} ${repayment.amountPaid} ${repayment.paymentDate}\n")
    writer.close()
  }

  private def updateLoanBalance(repayment: Repayment): Unit = {
    val loanManager = new LoanManagement("data/loans.txt")
    val loans = loanManager.loadLoans()

    loans.find(_.loanId == repayment.loanId) match {
      case Some(existingLoan) =>
        val updatedLoan = existingLoan.copy(outstandingBalance = existingLoan.outstandingBalance - repayment.amountPaid)
        loanManager.saveLoans(loans.map(loan => if (loan.loanId == repayment.loanId) updatedLoan else loan))
      case None =>
        println("Loan not found.")
    }
  }
}
