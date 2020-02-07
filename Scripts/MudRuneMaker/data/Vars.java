package scripts.MudRuneMaker.data;

import java.util.HashMap;


public class Vars {

    private Vars() {
    }

    private static final Vars VARS = new Vars();

    public static Vars get() {
        return VARS;
    }

    public boolean shouldRun = true;

    public String status;

    public HashMap<String, Integer> bankCache = new HashMap<>();

}
