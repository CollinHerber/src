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

import scripts.MudRuneMaker.api.rsitem_services.GrandExchange;

import scripts.MudRuneMaker.data.MyDaxCredentials;
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
    private int startRunecraftingXP = Skills.getXP(Skills.SKILLS.RUNECRAFTING);

    private static int costOfRune = 0;
    private static int mudRuneID = 4698;
    private static int costOfTalisman = 0;
    private static int talismanID = 1444;
    private static int costOfEssence = 0;
    private static int essenceID = 7936;
    private static int costOfStaminaPotion = 0;
    private static int staminaPotionID = 12625;
    private static int costOfVarrockTab = 0;
    private static int varrockTabID = 8007;
    private static int costOfBindingNecklace = 0;
    private static int bindingNecklaceInt = 5521;

    private double versionNumber = 1.16;

    @Override
    public void run() {

        sleep(500);
        General.useAntiBanCompliance(true);

        Antiban.create();
        PersistantABCUtil.get();
        Antiban.setPrintDebug(true);
        Mouse.setSpeed(speed);
        costOfRune = GrandExchange.getPrice(mudRuneID);
        General.println("The price of a Mud rune is: " + costOfRune);
        costOfTalisman = GrandExchange.getPrice(talismanID);
        General.println("The price of a Water talisman is: " + costOfTalisman);
        costOfEssence = GrandExchange.getPrice(essenceID);
        General.println("The price of a Pure essence is: " + costOfEssence);
        costOfStaminaPotion = GrandExchange.getPrice(staminaPotionID);
        General.println("The price of a Stamina potion(4) is: " + costOfStaminaPotion);
        costOfVarrockTab = GrandExchange.getPrice(varrockTabID);
        General.println("The price of a Varrock teleport tablet is: " + costOfVarrockTab);
        costOfBindingNecklace = GrandExchange.getPrice(bindingNecklaceInt);
        General.println("The price of a Binding necklace is: " + costOfBindingNecklace);

        startTime = Timing.currentTimeMillis();

        PersistantABCUtil.generateRunPercentage();
        PersistantABCUtil.activateRun();

        General.println("Welcome to Elliott's Mud Rune Maker.");
        General.println("If you find any bugs please let me know. Happy botting!");
        General.println("Version " + versionNumber);
        General.println("LATEST UPDATE: Implemented accurate profit tracking and better functionality.");
        General.println("Profit is based on a Mud rune value of " + costOfRune + ".");

        DaxWalker.setCredentials(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials(MyDaxCredentials.myDaxCredentials, "PUBLIC-KEY");
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
        }
    }

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
        double timeRan = System.currentTimeMillis() - startTime;
        int runecraftingXPGained = Skills.getXP(Skills.SKILLS.RUNECRAFTING) - startRunecraftingXP;
        int runecraftingXPPerHour = (int) (runecraftingXPGained * (3600000 / timeRan));
        int runecraftingLevel = Skills.getCurrentLevel(Skills.SKILLS.RUNECRAFTING);
        int percentToNextLevel = Skills.getPercentToNextLevel(Skills.SKILLS.RUNECRAFTING);

        int mudRunesMade = HandleMakingRunes.mudRunesMade;
        int mudRunesMadePerHour = (int) (mudRunesMade * (3600000 / timeRan));
        int totalCostOfMudRunes = mudRunesMade * costOfRune;

        int staminaPotionsDrank = HandleBanking.staminaPotionDrank;
        int totalCostOfStamina = (staminaPotionsDrank * costOfStaminaPotion) / 4;

        int varrockTabUsed = HandleNavigatingToBank.varrockTabUsed;
        int totalCostOfVarrockTab = varrockTabUsed * costOfVarrockTab;

        int pureEssenceUsed = HandleMakingRunes.pureEssUsed;
        int totalCostOfEssence = pureEssenceUsed * costOfEssence;

        int waterTalismanUsed = HandleMakingRunes.waterTalismanUsed;
        int totalCostOfTalisman = waterTalismanUsed * costOfTalisman;

        int bindingNecklaceUsed = HandleBanking.bindingNecklaceUsed;
        int totalCostOfBindingNecklace = bindingNecklaceUsed * costOfBindingNecklace;

        int totalCosts = totalCostOfStamina + totalCostOfVarrockTab + totalCostOfEssence + totalCostOfTalisman + totalCostOfBindingNecklace;
        int profit = totalCostOfMudRunes - totalCosts;
        int profitPerHour = (int) (profit * (3600000 / timeRan));

        g.setColor(Color.WHITE);
        g.drawString("Elliott's Mud Rune Maker", 10, 235);
        g.drawString("Version: " + versionNumber , 10, 250);
        g.drawString("Task: " + Vars.get().status, 10, 265);
        g.drawString("Runtime: " + Timing.msToString(this.getRunningTime()), 10, 280);
        g.drawString("Runecrafting XP: " + (runecraftingXPGained) + " (" + runecraftingXPPerHour + ")", 10, 295);
        g.drawString("Runecrafting level: " + runecraftingLevel + " Percent to next level: " + percentToNextLevel, 10, 310);
        g.drawString("Mud Runes Made: " + mudRunesMade + " (" + mudRunesMadePerHour + ")", 10, 325);
        g.drawString("Profit Made: " + profit + " (" + profitPerHour + ")", 10, 340);

        Point mP = Mouse.getPos();
        g.drawLine(mP.x, 0, mP.x, 500);
        g.drawLine(0, mP.y, 800, mP.y);

    }
}