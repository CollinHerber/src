package scripts.MudRuneMaker.Tasks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.MudRuneMaker.Antiban.Antiban;
import scripts.MudRuneMaker.Antiban.PersistantABCUtil;
import scripts.MudRuneMaker.framework.Priority;
import scripts.MudRuneMaker.framework.Task;

import scripts.dax_api.api_lib.DaxWalker;

public class HandleNavigatingToAltar implements Task {

    public String[] pureEssence = {"Pure essence"};
    public String[] watertalisman = {"Water talisman"};

    public static final RSArea earthAltarOutsideArea = new RSArea(new RSTile [] { new RSTile(3303, 3475, 0),new RSTile(3298, 3470, 0),new RSTile(3299, 3467, 0),new RSTile(3306, 3466, 0),new RSTile(3309, 3472, 0),new RSTile(3306, 3473, 0) });
    public static final RSTile earthAltarEntrance = new RSTile( 3305, 3472, 0);
    public static final RSArea insideEarthAltar = new RSArea(new RSTile[]{new RSTile(2650, 4845, 0), new RSTile(2664, 4844, 0), new RSTile(2664, 4831, 0), new RSTile(2661, 4826, 0), new RSTile(2653, 4826, 0), new RSTile(2651, 4832, 0)});


    public transient int run_at = PersistantABCUtil.getRunPercentage();

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public String toString() {
        return "Navigating to Altar " + " Status: " + getTaskState().name();
    }

    @Override
    public boolean validate() {
        return Inventory.find(pureEssence).length == 25 && Inventory.find(watertalisman).length == 1;
    }

    @Override
    public void execute() {

        switch (getTaskState()) {

            case WALKING_TO_ALTAR:

                DaxWalker.walkTo(earthAltarEntrance);
                General.println("Walking to Earth altar entrance");

                if (Game.getRunEnergy() >= run_at) {
                    if (!Game.isRunOn()) {
                        PersistantABCUtil.activateRun();
                        General.println("Turning on run.");
                        PersistantABCUtil.generateRunPercentage();
                        General.println("Generating new run percentage.");
                    }
                }
                PersistantABCUtil.handleIdleActions();
                Antiban.moveCamera();
                break;

            case ENTERING_ALTAR:

                RSObject[] altar = Objects.find(15, "Mysterious ruins");

                if (earthAltarOutsideArea.contains(Player.getPosition())) {
                    if (altar != null && altar[0].isClickable()) {
                        if (altar[0].click("Enter")) {
                            Timing.waitCondition(() -> {
                                General.sleep(General.randomSD(750, 75));
                                return insideEarthAltar.contains(Player.getPosition());
                            }, General.random(2000, 3000));
                        }
                        General.println("Clicking on altar");
                    }
                }
                break;

            case PRINT_OUT:

                General.println("You have entered the altar");
        }

    }

    private enum TaskState {
        WALKING_TO_ALTAR,
        ENTERING_ALTAR,
        PRINT_OUT

    }

    private TaskState getTaskState() {

        if (Inventory.find(pureEssence).length == 25 && Inventory.find(watertalisman).length == 1 && !earthAltarOutsideArea.contains(Player.getPosition())) {
            return TaskState.WALKING_TO_ALTAR;
        }
        if (earthAltarOutsideArea.contains(Player.getPosition())) {
            return TaskState.ENTERING_ALTAR;
        }
        return TaskState.PRINT_OUT;
        }
}

