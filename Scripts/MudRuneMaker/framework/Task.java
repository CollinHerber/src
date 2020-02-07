package scripts.MudRuneMaker.framework;

public interface Task {

   public abstract Priority priority();
   
   public abstract String toString();

    public abstract boolean validate();

    public abstract void execute();

}
