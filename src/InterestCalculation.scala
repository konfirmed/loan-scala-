import scala.io.Source
import java.io.{BufferedWriter, FileWriter}

class InterestCalculation(loanFilePath: String) {
  def calculateInterest(): Unit = {
    val loans = loadLoans()
    val updatedLoans = loans.map { loan =>
      val interest = loan.outstandingBalance * (loan.interestRate / 100)
      loan.copy(outstandingBalance = loan.outstandingBalance + interest, lastInterestCalcDate = java.time.LocalDate.now.toString)
    }
    saveLoans(updatedLoans)
    println("Interest calculation completed.")
  }

  private def loadLoans(): Seq[Loan] = {
    Source.fromFile(loanFilePath).getLines().map { line =>
      val Array(loanId, borrowerName, loanAmount, interestRate, loanTerm, outstandingBalance, lastInterestCalcDate) = line.split("\\s+")
      Loan(loanId.toInt, borrowerName, loanAmount.toDouble, interestRate.toDouble, loanTerm.toInt, outstandingBalance.toDouble, lastInterestCalcDate)
    }.toSeq
  }

  private def saveLoans(loans: Seq[Loan]): Unit = {
    val writer = new BufferedWriter(new FileWriter(loanFilePath))
    loans.foreach(loan => writer.write(s"${loan.loanId} ${loan.borrowerName} ${loan.loanAmount} ${loan.interestRate} ${loan.loanTerm} ${loan.outstandingBalance} ${loan.lastInterestCalcDate}\n"))
    writer.close()
  }
}
