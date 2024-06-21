import scala.io.Source

class ReportGeneration(loanFilePath: String, repaymentFilePath: String) {
  def generateReport(): Unit = {
    println(
      """
        |Select Report Type:
        |1. All Loans
        |2. Specific Borrower
        |3. Overdue Loans
      """.stripMargin)
    val choice = scala.io.StdIn.readInt()

    choice match {
      case 1 => reportAllLoans()
      case 2 => reportSpecificBorrower()
      case 3 => reportOverdueLoans()
      case _ => println("Invalid choice, please try again.")
    }
  }

  private def reportAllLoans(): Unit = {
    val loans = loadLoans()
    loans.foreach(loan => println(s"Loan ID: ${loan.loanId}, Borrower Name: ${loan.borrowerName}, Outstanding Balance: ${loan.outstandingBalance}"))
  }

  private def reportSpecificBorrower(): Unit = {
    println("Enter Borrower Name: ")
    val borrowerName = scala.io.StdIn.readLine()
    val loans = loadLoans()
    loans.filter(_.borrowerName == borrowerName).foreach(loan => println(s"Loan ID: ${loan.loanId}, Outstanding Balance: ${loan.outstandingBalance}"))
  }

  private def reportOverdueLoans(): Unit = {
    val loans = loadLoans()
    val today = java.time.LocalDate.now.toString
    loans.filter(_.lastInterestCalcDate < today).foreach(loan => println(s"Loan ID: ${loan.loanId}, Borrower Name: ${loan.borrowerName}, Outstanding Balance: ${loan.outstandingBalance}"))
  }

  private def loadLoans(): Seq[Loan] = {
    Source.fromFile(loanFilePath).getLines().map { line =>
      val Array(loanId, borrowerName, loanAmount, interestRate, loanTerm, outstandingBalance, lastInterestCalcDate) = line.split("\\s+")
      Loan(loanId.toInt, borrowerName, loanAmount.toDouble, interestRate.toDouble, loanTerm.toInt, outstandingBalance.toDouble, lastInterestCalcDate)
    }.toSeq
  }
}
