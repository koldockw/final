package moneypilot;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.io.IOException;
public final class CoreWorkflowTest {
    private static int checks;
    private static void check(boolean value,String name){if(!value)throw new AssertionError(name);checks++;}
    private static Transaction tx(Transaction.Type type,String amount,LocalDate date) {
        return new Transaction(UUID.randomUUID(),date,type,"Food",new BigDecimal(amount),"Core test");
    }
    public static void main(String[] args)throws Exception {
        Path dir=Files.createTempDirectory("moneypilot-core-");
        try {
            Path file=dir.resolve("data.properties");
            MoneyPilotFacade f=new MoneyPilotFacade(new FileRepository(file));
            LocalDate date=LocalDate.of(2026,11,15);YearMonth month=YearMonth.from(date);
            Transaction income=tx(Transaction.Type.INCOME,"1000",date),a=tx(Transaction.Type.EXPENSE,"0.10",date),b=tx(Transaction.Type.EXPENSE,"0.20",date);
            f.execute(new TransactionCommand.Add(income));f.execute(new TransactionCommand.Add(a));f.execute(new TransactionCommand.Add(b));
            check(f.summary(month).expense().compareTo(new BigDecimal("0.30"))==0,"exact decimal totals");
            check(f.summary(month).balance().compareTo(new BigDecimal("999.70"))==0,"monthly net");
            check(f.summary(month.minusMonths(1)).expense().signum()==0,"month isolation");
            f.execute(new TransactionCommand.Delete(a.id()));f.undo();check(f.all().get(1).id().equals(a.id()),"undo delete restores identity and order");
            Transaction edited=new Transaction(a.id(),date,a.type(),a.category(),new BigDecimal("5"),a.note());
            f.execute(new TransactionCommand.Edit(edited));f.undo();check(f.all().get(1).amount().compareTo(new BigDecimal("0.10"))==0,"undo edit");
            f.settings(new BigDecimal("100"),"Cautious");check(f.strategy().warningLimit(f.budget()).compareTo(new BigDecimal("80"))==0,"strategy switch");
            MoneyPilotFacade restored=new MoneyPilotFacade(new FileRepository(file));
            check(restored.all().size()==3 && restored.strategy().name().equals("Cautious"),"persistence");
            check(!restored.history().canUndo(),"session history");
            Repository broken=new Repository(){public State load(){return new State(List.of(),new BigDecimal("100"),"Balanced");}public void save(State s)throws IOException{throw new IOException("Disk full");}};
            MoneyPilotFacade blocked=new MoneyPilotFacade(broken);boolean failed=false;
            try{blocked.execute(new TransactionCommand.Add(a));}catch(IOException e){failed=true;}
            check(failed && blocked.all().isEmpty()&&!blocked.history().canUndo(),"save failure keeps previous state");
            System.out.println("PASS: "+checks+" core workflow checks");
        } finally {try(var files=Files.list(dir)){for(Path path:files.toList())Files.delete(path);}Files.delete(dir);}
    }
}
