package scripts.MudRuneMaker.Tasks;

import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import scripts.MudRuneMaker.Antiban.PersistantABCUtil;
import scripts.MudRuneMaker.framework.Priority;
import scripts.MudRuneMaker.framework.Task;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.RunescapeBank;


public class HandleNavigatingToBank implements Task {

    public String[] pureEssence = {"Pure essence"};
    public transient int run_at = PersistantABCUtil.getRunPercentage();


    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public String toString() {
        return "Navigating to Bank " + " Status: " + getTaskState().name();
    }

    @Override
    public boolean validate() {
        return Inventory.find(pureEssence).length == 0;
    }

    @Override
    public void execute() {

        switch (getTaskState()) {

            case WALK_TO_BANK:

                DaxWalker.walkToBank(RunescapeBank.VARROCK_EAST);
                if (run_at > Game.getRunEnergy()) {
                    if (!Game.isRunOn()) {
                        PersistantABCUtil.activateRun();
                        General.println("Turning on run.");
                        PersistantABCUtil.generateRunPercentage();
                        General.println("Generating new run percentage.");
                    }
                }
                General.println("Returning to Varrock East Bank");
                PersistantABCUtil.handleIdleActions();
        }
    }

    private enum TaskState {

        WALK_TO_BANK
    }

    private TaskState getTaskState() {

        return TaskState.WALK_TO_BANK;
    }
}
