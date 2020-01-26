package scripts.MythsGuildGDK;

import java.awt.*;
import java.util.ArrayList;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.MythsGuildGDK.Antiban.PersistantABCUtil;
import scripts.MythsGuildGDK.Tasks.HandleBanking;
import scripts.MythsGuildGDK.Tasks.HandleCombat;
import scripts.MythsGuildGDK.Tasks.HandleLooting;
import scripts.MythsGuildGDK.Tasks.HandleReturningToCombat;
import scripts.MythsGuildGDK.data.Vars;
import scripts.MythsGuildGDK.framework.Task;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.wastedbro.api.InventoryListener;
import scripts.wastedbro.api.InventoryObserver;
import scripts.wastedbro.api.rsitem_services.GrandExchange;


@ScriptManifest(category = "Combat", name = "MythsGuildGDK", authors = "Elliott")

public class Main extends Script implements InventoryListener, Painting {

    private long startTime;
    private ArrayList<Task> tasks = new ArrayList<>();

    private long startAttackXP = Skills.getXP(Skills.SKILLS.ATTACK);
    private long startHPXP = Skills.getXP(Skills.SKILLS.HITPOINTS);
    private long startStrengthXP = Skills.getXP(Skills.SKILLS.STRENGTH);
    private long startDefenceXP = Skills.getXP(Skills.SKILLS.DEFENCE);

    private int profitGained = 0;


    @Override
    public void run() {

        sleep(500);
        General.useAntiBanCompliance(true);
        Antiban.create();
        Antiban.setPrintDebug(true);
        
        startTime = Timing.currentTimeMillis();
        PersistantABCUtil.generateRunPercentage();

        System.out.println("Welcome to Elliott's Myths Guild GDK Beta.");
        System.out.println("If you find any bugs please let me know. Happy botting!");

        DaxWalker.setCredentials(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("PRIVATE-KEY", "PUBLIC-KEY");
            }
        });

        if (Login.getLoginState().equals(Login.STATE.LOGINSCREEN)) {
            Login.login();
            System.out.println("[LOGIN]: Logging in...");
            if (Login.getLoginState().equals(Login.STATE.INGAME)) {
                System.out.println("[LOGIN]: Successfully logged in.");
            }
        }
        PersistantABCUtil.activateRun();

        addTasks();

        while (Vars.get().shouldRun){

            handleTasks();

            InventoryObserver inventoryObserver = new InventoryObserver(() -> !Banking.isBankScreenOpen());
             inventoryObserver.addListener(this);
            inventoryObserver.start();

        }

    }
    @Override
    public void inventoryItemGained(int id, int count) {
        General.println("Gained item. Item price");
        profitGained += GrandExchange.tryGetPrice(id).orElse(0) * count;
        General.println("Gained items and they got this price init" + profitGained);
    }

    @Override
    public void inventoryItemLost(int id, int count) {
       profitGained -= GrandExchange.tryGetPrice(id).orElse(0) * count;
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
        Antiban.destroy();

    }

    @Override
    public void onPaint(Graphics g) {
        long timeRan = System.currentTimeMillis() - startTime;

        long hpXPGained = Skills.getXP(Skills.SKILLS.HITPOINTS) - startHPXP;
        long attackXPGained = Skills.getXP(Skills.SKILLS.ATTACK) - startAttackXP;
        long strengthXPGained = Skills.getXP(Skills.SKILLS.STRENGTH) - startStrengthXP;
        long defenceXPGained = Skills.getXP(Skills.SKILLS.DEFENCE) - startDefenceXP;

        long hpXPPerHour = hpXPGained * (3600000 / timeRan);
        long attackXPPerHour = attackXPGained * (3600000 / timeRan);
        long strengthXPPerHour = strengthXPGained * (3600000 / timeRan);
        long defenceXPPerHour = defenceXPGained * (3600000 / timeRan);
        long profitPerHour = profitGained * (3600000 / timeRan);

        g.setColor(Color.CYAN);
        g.drawString("Elliott's Myths Guild GDK", 10, 250);
        g.drawString("Version: BETA 1.0" , 10, 265);
        g.drawString("Task: " + Vars.get().status, 10, 280);
        g.drawString("Runtime: " + Timing.msToString(this.getRunningTime()), 10, 295 );
        g.drawString("Profit: " + profitGained + " (" + profitPerHour + ")", 10, 310);
        g.drawString("Hitpoints XP: " + (hpXPGained) + " (" + hpXPPerHour + ")", 10, 325);
        g.drawString("Attack XP: " + (attackXPGained) + " (" + attackXPPerHour + ")", 10, 340);
        g.drawString("Strength XP: " + (strengthXPGained) + " (" + strengthXPPerHour + ")", 10, 355);
        g.drawString("Defence XP: " + (defenceXPGained) + " (" + defenceXPPerHour + ")", 10, 370);

    }
}
