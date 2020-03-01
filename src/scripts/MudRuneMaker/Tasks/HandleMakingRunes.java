package scripts.MudRuneMaker.Tasks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.MudRuneMaker.Antiban.Antiban;
import scripts.MudRuneMaker.framework.Priority;
import scripts.MudRuneMaker.framework.Task;

import scripts.dax_api.api_lib.DaxWalker;

public class HandleMakingRunes implements Task {

    public static final RSArea insideEarthAltar = new RSArea(new RSTile[]{new RSTile(2650, 4845, 0), new RSTile(2664, 4844, 0), new RSTile(2664, 4831, 0), new RSTile(2661, 4826, 0), new RSTile(2653, 4826, 0), new RSTile(2651, 4832, 0)});
    public static final RSTile stuckPositionEast = new RSTile( 2659, 4836, 0);
    public static final RSTile stuckPositionWest = new RSTile( 2657, 4836, 0);
    public static final RSTile fixStuckPosition = new RSTile( 2658, 4839, 0);

    public static int mudRunesMade;
    public static int pureEssUsed;
    public static int waterTalismanUsed;

    public String[] watertalisman = {"Water talisman"};
    public String[] pureEssence = {"Pure essence"};

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public String toString() {
        return "Making Runes " + " Status: " + getTaskState().name();
    }

    @Override
    public boolean validate() {
        return insideEarthAltar.contains(Player.getPosition()) && Inventory.find(watertalisman).length > 0 && Inventory.find(pureEssence).length > 0;
    }

    @Override
    public void execute() {

        switch (getTaskState()) {

            case FAILSAFE:

                    DaxWalker.walkTo(fixStuckPosition);
                    General.sleep(898, 1034);

            case MAKING_RUNES:

                RSItem[] waterTalisman = Inventory.find(watertalisman);

                if (waterTalisman != null) {
                    if (waterTalisman[0].click("Use")) {
                        Antiban.waitItemInteractionDelay();
                        if (Game.getUptext().contains("Use Water talisman")) {
                            RSObject[] earthAltar = Objects.find(30, "Altar");
                            if (earthAltar != null) {
                                if (!earthAltar[0].isClickable() && earthAltar[0].adjustCameraTo()){
                                    Timing.waitCondition(() -> {
                                        General.sleep(General.randomSD(750, 75));
                                        return earthAltar[0].isClickable();
                                    }, General.random(2000, 3000));
                                }
                                     if (earthAltar[0].click("Use Water talisman -> " + earthAltar[0].getDefinition().getName())) {
                                         Timing.waitCondition(() -> {
                                             General.sleep(General.randomSD(3700, 50));
                                             return Inventory.find("Mud rune").length > 0;
                                         }, General.random(5000, 6000));
                                         mudRunesMade += 25;
                                         pureEssUsed += 25;
                                         waterTalismanUsed +=1;

                                         Antiban.timedActions();
                                         General.println("Making Mud runes.");
                                }
                            }
                        }
                    }

                }
        }
    }

    private enum TaskState {

        FAILSAFE,
        MAKING_RUNES
    }

    private TaskState getTaskState() {

        if ((Player.getPosition().equals(stuckPositionEast) || Player.getPosition().equals(stuckPositionWest)) && !Player.isMoving()) {
            return TaskState.FAILSAFE;
        }
            return TaskState.MAKING_RUNES;
    }
}
