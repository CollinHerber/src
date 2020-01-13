package scripts.MythsGuildGDK.Tasks;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.MythsGuildGDK.framework.Priority;
import scripts.MythsGuildGDK.framework.Task;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ItemEntity;
import scripts.entityselector.finders.prefabs.NpcEntity;
import scripts.framework.abc.PersistantABCUtil;

public class HandleReturningToCombat implements Task {

    private String[] food = {"Lobster"};
    private String[] mythicalCape = {"Mythical cape"};
    private String[] dragonLoot = {"Green dragonhide", "Dragon bones", "Nature rune", "Grimy ranarr weed", "Shield left half", "Dragon spear", "Ensouled dragon head"};

    private static RSTile bottomOfLadder = new RSTile(1936, 9009, 1);

    private static final RSArea dungeon = new RSArea(new RSTile [] { new RSTile(1937, 9014, 1),new RSTile(1921, 9014, 1),new RSTile(1921, 8995, 1),new RSTile(1931, 8987, 1),new RSTile(1937, 8982, 1),new RSTile(1941, 8986, 1),new RSTile(1943, 8987, 1),new RSTile(1946, 8989, 1),new RSTile(1951, 8996, 1),new RSTile(1950, 9003, 1) });
    private static final RSArea dragonArea = new RSArea(new RSTile[]{new RSTile(1947, 8987, 1), new RSTile(1936, 8987, 1), new RSTile(1936, 8994, 1), new RSTile(1939, 8994, 1), new RSTile(1941, 8995, 1), new RSTile(1943, 8997, 1), new RSTile(1944, 8999, 1), new RSTile(1945, 9000, 1), new RSTile(1946, 9000, 1), new RSTile(1947, 9000, 1), new RSTile(1948, 9000, 1), new RSTile(1949, 8999, 1), new RSTile(1950, 8998, 1), new RSTile(1950, 8997, 1), new RSTile(1949, 8995, 1), new RSTile(1948, 8994, 1), new RSTile(1947, 8994, 1), new RSTile(1946, 8993, 1)});
    private static final RSArea mythsTeleportArea = new RSArea(new RSTile [] { new RSTile(2456, 2852, 0),new RSTile(2458, 2852, 0),new RSTile(2459, 2851, 0),new RSTile(2460, 2851, 0),new RSTile(2461, 2850, 0),new RSTile(2461, 2849, 0),new RSTile(2461, 2848, 0),new RSTile(2461, 2847, 0),new RSTile(2461, 2846, 0),new RSTile(2461, 2845, 0),new RSTile(2461, 2844, 0),new RSTile(2459, 2843, 0),new RSTile(2458, 2842, 0),new RSTile(2456, 2842, 0),new RSTile(2455, 2843, 0),new RSTile(2454, 2843, 0),new RSTile(2453, 2844, 0),new RSTile(2453, 2845, 0),new RSTile(2453, 2846, 0),new RSTile(2453, 2847, 0),new RSTile(2453, 2848, 0),new RSTile(2453, 2849, 0),new RSTile(2453, 2850, 0),new RSTile(2454, 2851, 0) });

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public String toString() {
        return "Returning to Combat " + "Status: " + getTaskState().name();
    }

    @Override
    public boolean validate() {
        return !Banking.isBankScreenOpen() && !dragonArea.contains(Player.getPosition()) && Inventory.getCount(food) == 15 && Inventory.find(dragonLoot).length == 0;
    }

    @Override
    public void execute() {

        switch (getTaskState()) {

            case RETURNING_TO_COMBAT:

                ItemEntity mythsCapeReturn = Entities.find(ItemEntity::new)
                        .nameEquals("Mythical cape")
                        .actionsEquals("Teleport");

                if (mythsCapeReturn != null && !mythsTeleportArea.contains(Player.getPosition()) && !dungeon.contains(Player.getPosition())) {
                    RSItem[] capeReturn = Inventory.find(mythicalCape);
                    if (capeReturn != null && capeReturn.length > 0) {
                        if (capeReturn[0].click("Teleport")) {
                            Timing.waitCondition(() -> {
                                General.sleep(General.randomSD(1500, 30));
                                return mythsTeleportArea.contains(Player.getPosition());
                            }, General.random(300, 400));
                        }
                        System.out.println("Teleporting to the Myths Guild. Returning to combat.");
                    }
                }
                RSObject[] mythsStatue = Objects.findNearest(20, "Mythic Statue");

                if (mythsStatue != null && !dungeon.contains(Player.getPosition())) {
                    Clicking.click("Enter", mythsStatue);
                    Timing.waitCondition(() -> {
                        System.out.println("Attempting to enter the Myth's dungeon...");
                        General.sleep(General.randomSD(700, 60));
                        return Player.getPosition().equals(bottomOfLadder);
                            }, General.random(3000, 4000));
                }
                while (Player.isMoving()) {
                    General.sleep(1200, 2000);
                }
                if (Player.getPosition().equals(bottomOfLadder)) {
                    System.out.println("Successfully entered the Myth's dungeon. Returning to dragons.");
                    Walking.walkTo(dragonArea.getRandomTile());
                    while (Player.isMoving()) {
                        PersistantABCUtil.handleIdleActions();
                    }

                }
                break;

            case COMPLETED_RETURNING_TO_COMBAT:

                RSNPC dragon = Entities.find(NpcEntity::new).nameEquals("Green dragon").actionsEquals("Attack")
                        .sortByDistance()
                        .getFirstResult();

                if (Player.isMoving() && dragon.isOnScreen() && dungeon.contains(Player.getPosition())) {
                   System.out.println("Successfully returned to Green dragons.");
                   General.sleep(3000, 5000);
                }
        }


}
private enum TaskState {

    RETURNING_TO_COMBAT,
    COMPLETED_RETURNING_TO_COMBAT

}

    private TaskState getTaskState () {

        if (!dragonArea.contains(Player.getPosition()) && Inventory.getCount(food) == 15 && Inventory.find(dragonLoot).length == 0 && !Banking.isBankScreenOpen()) {
            return TaskState.RETURNING_TO_COMBAT;
        }
        return TaskState.COMPLETED_RETURNING_TO_COMBAT;
    }
}

