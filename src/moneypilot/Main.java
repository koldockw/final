package moneypilot;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.*;
import java.util.*;

// Первый этап: консоль позволяет проверить логику до подключения окна.
public final class Main {
    public static void main(String[] args) throws Exception {
        Path path=args.length>0?Path.of(args[0]):Path.of(System.getProperty("user.home"),".moneypilot","data.properties");
        MoneyPilotFacade finance=new MoneyPilotFacade(new FileRepository(path));
        try(Scanner input=new Scanner(System.in)) {
            System.out.println("MoneyPilot / SE-2509 / core console");
            while(true) {
                System.out.println("1 Add income | 2 Add expense | 3 Monthly report | 4 Undo | 5 Budget | 0 Exit");
                if(!input.hasNextLine()) return;
                String action=input.nextLine().strip();
                try {
                    switch(action) {
                        case "0" -> { return; }
                        case "1", "2" -> {
                            System.out.println("Date (YYYY-MM-DD):");LocalDate date=LocalDate.parse(input.nextLine().strip());
                            System.out.println("Category "+Transaction.CATEGORIES+":");String category=input.nextLine().strip();
                            System.out.println("Positive amount in KZT:");BigDecimal amount=new BigDecimal(input.nextLine().strip());
                            System.out.println("Note:");String note=input.nextLine();
                            Transaction.Type type=action.equals("1")?Transaction.Type.INCOME:Transaction.Type.EXPENSE;
                            Transaction t=new Transaction(UUID.randomUUID(),date,type,category,amount,note);
                            finance.execute(new TransactionCommand.Add(t));System.out.println("Saved");
                        }
                        case "3" -> {
                            System.out.println("Month (YYYY-MM):");YearMonth month=YearMonth.parse(input.nextLine().strip());
                            MoneyPilotFacade.Summary s=finance.summary(month);
                            System.out.println("Income: "+s.income()+" | Expenses: "+s.expense()+" | Net: "+s.balance()+" KZT");
                            System.out.println("Warning threshold ("+finance.strategy().name()+"): "+finance.strategy().warningLimit(finance.budget())+" KZT");
                            for(Transaction t:finance.month(month))System.out.println(t.date()+" "+t.type()+" "+t.category()+" "+t.amount()+" "+t.note());
                        }
                        case "4" -> { finance.undo();System.out.println("Last transaction action undone"); }
                        case "5" -> {
                            System.out.println("Monthly budget:");BigDecimal budget=new BigDecimal(input.nextLine().strip());
                            System.out.println("Policy (Balanced or Cautious):");String policy=input.nextLine().strip();
                            finance.settings(budget,policy);System.out.println("Budget saved");
                        }
                        default -> System.out.println("Choose a menu number");
                    }
                } catch(Exception e) { System.out.println("Error: "+e.getMessage()); }
            }
        }
    }
}
