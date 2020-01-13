package scripts.MythsGuildGDK;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.MythsGuildGDK.Tasks.HandleBanking;
import scripts.MythsGuildGDK.Tasks.HandleCombat;
import scripts.MythsGuildGDK.Tasks.HandleLooting;
import scripts.MythsGuildGDK.Tasks.HandleReturningToCombat;
import scripts.MythsGuildGDK.data.Vars;
import scripts.MythsGuildGDK.framework.Task;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;


@ScriptManifest(category = "Combat", name = "MythsGuildGDK", authors = "Elliott")


public class Main extends Script implements Painting{

    private long startTime;

    private ArrayList<Task> tasks = new ArrayList<>();

    @Override
    public void run() {

        startTime = System.currentTimeMillis();
        System.out.println("Welcome to Elliott's Myths Guild GDK Beta.");
        System.out.println("If you find any bugs please let me know. Happy botting!");

        DaxWalker.setCredentials(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
            }
        });

        if (Login.getLoginState().equals(Login.STATE.LOGINSCREEN)) {
            Login.login();
            System.out.println("Logging in...");
        }
        addTasks();

        while (Vars.get().shouldRun){

            handleTasks();

        }

    }

    private void addTasks(){

        tasks.add(new HandleBanking());
        tasks.add(new HandleReturningToCombat());
        tasks.add(new HandleLooting());
        tasks.add(new HandleCombat());

    }

    private void handleTasks(){
        for (Task t : tasks){
            if (t.validate()){
                Vars.get().status = t.toString();
                t.execute();
                General.sleep(100, 250);
                break;
            }
        }
    }

    public static void stopScript(String reason){
        General.println("Script stopped : " + reason);
        Vars.get().shouldRun = false;
    }

    @Override
    public void onPaint(Graphics g) {
        g.setColor(Color.WHITE);

        g.drawString("Elliott's Myths Guild GDK", 10, 250);
        g.drawString("Version: BETA 0.7" , 10, 265);
        g.drawString("Task: " + Vars.get().status, 10, 280);
        g.drawString("Runtime: " + Timing.msToString(this.getRunningTime()), 10, 295 );
        g.drawString("Profit: ", 10, 310); // need to work out how to calculate this
    }
}
