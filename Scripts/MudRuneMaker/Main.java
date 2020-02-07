package scripts.MudRuneMaker;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.MudRuneMaker.Antiban.Antiban;
import scripts.MudRuneMaker.Antiban.PersistantABCUtil;
import scripts.MudRuneMaker.Tasks.HandleBanking;
import scripts.MudRuneMaker.Tasks.HandleMakingRunes;
import scripts.MudRuneMaker.Tasks.HandleNavigatingToAltar;
import scripts.MudRuneMaker.Tasks.HandleNavigatingToBank;
import scripts.MudRuneMaker.framework.Task;
import scripts.MudRuneMaker.data.Vars;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;

import java.awt.*;
import java.util.ArrayList;


@ScriptManifest(authors = "Elliott", name = "Mud Rune Maker", category = "Runecrafting")

public class Main extends Script implements Painting {

    private long startTime;
    private ArrayList<Task> tasks = new ArrayList<>();
    public int speed = General.random(136, 143);
    public double xpPerRune= 9.3;
    public long longXPPerRune = Math.round(xpPerRune);
    private long startRunecraftingXP = Skills.getXP(Skills.SKILLS.RUNECRAFTING);

    @Override
    public void run() {

        sleep(500);
        General.useAntiBanCompliance(true);

        Antiban.create();
        Antiban.setPrintDebug(true);
        Mouse.setSpeed(speed);

        startTime = Timing.currentTimeMillis();

        PersistantABCUtil.generateRunPercentage();
        PersistantABCUtil.activateRun();

        General.println("Welcome to Elliott's Mud Rune Maker.");
        General.println("If you find any bugs please let me know. Happy botting!");
        General.println("Profit is based on a Mud rune value of 350.");

        DaxWalker.setCredentials(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("PRIVATE-KEY", "PUBLIC-KEY");
            }
        });

        if (Login.getLoginState().equals(Login.STATE.LOGINSCREEN)) {
            Login.login();
            General.println("[LOGIN]: Logging in...");
            if (Login.getLoginState().equals(Login.STATE.INGAME)) {
                General.println("[LOGIN]: Successfully logged in.");
            }
        }

        addTasks();

        while (Vars.get().shouldRun){

            handleTasks();

            //       InventoryObserver inventoryObserver = new InventoryObserver(() -> !Banking.isBankScreenOpen());
            //        inventoryObserver.addListener(this);
            //       inventoryObserver.start();

        }

    }
    //  @Override
    //  public void inventoryItemGained(int id, int count) {
    //      General.println("Gained item. Item price");
    //      profitGained += GrandExchange.tryGetPrice(id).orElse(0) * count;
    //      General.println("Gained items and they got this price init" + profitGained);
    //  }

    //  @Override
    //  public void inventoryItemLost(int id, int count) {
    //     profitGained -= GrandExchange.tryGetPrice(id).orElse(0) * count;
    //  }

    private void addTasks(){

        tasks.add(new HandleBanking());
        tasks.add(new HandleMakingRunes());
        tasks.add(new HandleNavigatingToBank());
        tasks.add(new HandleNavigatingToAltar());

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
        Antiban.destroy();
    }

    @Override
    public void onPaint(Graphics g) {
        long timeRan = System.currentTimeMillis() - startTime;
        long runecraftingXPGained = Skills.getXP(Skills.SKILLS.RUNECRAFTING) - startRunecraftingXP;
        long runecraftingXPPerHour = runecraftingXPGained * (3600000 / timeRan);
        long runecraftingLevel = Skills.getCurrentLevel(Skills.SKILLS.RUNECRAFTING);
        long percentToNextLevel = Skills.getPercentToNextLevel(Skills.SKILLS.RUNECRAFTING);


        long runesMade = runecraftingXPGained / longXPPerRune;
        long runesMadePerHouse = runesMade * (3600000 / timeRan);
        long profit = runesMade * 350;
        long profitPerHour = profit * (3600000 / timeRan);


        g.setColor(Color.WHITE);
        g.drawString("Elliott's Mud Rune Maker", 10, 235);
        g.drawString("Version: 1.12_1" , 10, 250);
        g.drawString("Task: " + Vars.get().status, 10, 265);
        g.drawString("Runtime: " + Timing.msToString(this.getRunningTime()), 10, 280);
        g.drawString("Runecrafting XP: " + (runecraftingXPGained) + " (" + runecraftingXPPerHour + ")", 10, 295);
        g.drawString("Runecrafting level: " + runecraftingLevel + " Percent to next level: " + percentToNextLevel, 10, 310);
        g.drawString("Mud Runes Made: " + runesMade + " (" + runesMadePerHouse + ")", 10, 325);
        g.drawString("Profit Made: " + profit + " (" + profitPerHour + ")", 10, 340);


        Point mP = Mouse.getPos();
        g.drawLine(mP.x, 0, mP.x, 500);
        g.drawLine(0, mP.y, 800, mP.y);

    }
}
