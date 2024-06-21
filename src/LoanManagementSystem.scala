object LoanManagementSystem extends App {
  val userAuth = new UserAuthentication("data/users.txt")
  val loanManager = new LoanManagement("data/loans.txt")
  val repaymentManager = new RepaymentManagement("data/repayments.txt")
  val reportGenerator = new ReportGeneration("data/loans.txt", "data/repayments.txt")
  val interestCalculator = new InterestCalculation("data/loans.txt")
  val logger = new Logging("data/logfile.txt")

  if (userAuth.authenticate()) {
    var choice = 0
    do {
      println(
        """
          |========================================
          |        Loan Management System         
          |========================================
          |1. Add Loan
          |2. Update Loan
          |3. Record Repayment
          |4. Generate Report
          |5. Calculate Interest
          |6. Exit
          |========================================
          |Please enter your choice (1-6):
        """.stripMargin)
      choice = scala.io.StdIn.readInt()
      choice match {
        case 1 => loanManager.addLoan()
        case 2 => loanManager.updateLoan()
        case 3 => repaymentManager.recordRepayment()
        case 4 => reportGenerator.generateReport()
        case 5 => interestCalculator.calculateInterest()
        case 6 => println("Exiting system...")
        case _ => println("Invalid choice, please try again.")
      }
    } while (choice != 6)
  } else {
    println("Authentication failed. Exiting system.")
  }
}
