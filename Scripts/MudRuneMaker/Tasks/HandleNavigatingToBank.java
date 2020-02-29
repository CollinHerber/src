package scripts.MudRuneMaker.Tasks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;

import scripts.MudRuneMaker.Antiban.Antiban;
import scripts.MudRuneMaker.Antiban.PersistantABCUtil;
import scripts.MudRuneMaker.framework.Priority;
import scripts.MudRuneMaker.framework.Task;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.RunescapeBank;


public class HandleNavigatingToBank implements Task {

    public String[] pureEssence = {"Pure essence"};
    public transient int run_at = PersistantABCUtil.getRunPercentage();
    public static int varrockTabUsed;


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
                if (Game.getRunEnergy() >= run_at) {
                    if (!Game.isRunOn()) {
                        Timing.waitCondition(() -> {
                            General.sleep(General.randomSD(600, 25));
                            return PersistantABCUtil.activateRun();
                        }, General.random(2000, 3000));
                        General.println("Turning on run.");
                        PersistantABCUtil.generateRunPercentage();
                        General.println("Generating new run percentage.");
                    }
                }
                varrockTabUsed += 1;
                General.println("Returning to Varrock East Bank");
                PersistantABCUtil.handleIdleActions();
                Antiban.moveCamera();
        }
    }

    private enum TaskState {

        WALK_TO_BANK
    }

    private TaskState getTaskState() {

        return TaskState.WALK_TO_BANK;
    }
}
