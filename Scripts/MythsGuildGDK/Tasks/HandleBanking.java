package scripts.MythsGuildGDK.Tasks;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.MythsGuildGDK.framework.Priority;
import scripts.MythsGuildGDK.framework.Task;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ItemEntity;
import scripts.entityselector.finders.prefabs.ObjectEntity;

public class HandleBanking implements Task {

    private String[] food = {"Lobster"};
    private String[] dragonLoot = {"Green dragonhide", "Dragon bones", "Nature rune", "Grimy ranarr weed", "Shield left half", "Dragon spear", "Ensouled dragon head"};
    private String[] mythicalCape = {"Mythical cape"};

    private static RSTile bottomOfStairs = new RSTile(2457, 2839, 0);
    private static RSTile bankTile = new RSTile(2465, 2848, 1);

    private static final RSArea mythsTeleportArea = new RSArea(new RSTile[]{new RSTile(2456, 2852, 0), new RSTile(2458, 2852, 0), new RSTile(2459, 2851, 0), new RSTile(2460, 2851, 0), new RSTile(2461, 2850, 0), new RSTile(2461, 2849, 0), new RSTile(2461, 2848, 0), new RSTile(2461, 2847, 0), new RSTile(2461, 2846, 0), new RSTile(2461, 2845, 0), new RSTile(2461, 2844, 0), new RSTile(2459, 2843, 0), new RSTile(2458, 2842, 0), new RSTile(2456, 2842, 0), new RSTile(2455, 2843, 0), new RSTile(2454, 2843, 0), new RSTile(2453, 2844, 0), new RSTile(2453, 2845, 0), new RSTile(2453, 2846, 0), new RSTile(2453, 2847, 0), new RSTile(2453, 2848, 0), new RSTile(2453, 2849, 0), new RSTile(2453, 2850, 0), new RSTile(2454, 2851, 0)});
    private static final RSArea topOfStairs = new RSArea(new RSTile[]{new RSTile(2454, 2839, 1), new RSTile(2459, 2839, 1), new RSTile(2459, 2841, 1), new RSTile(2454, 2841, 1)});
    public static final RSArea dragonArea = new RSArea(new RSTile [] { new RSTile(1937, 9000, 1),new RSTile(1944, 9006, 1),new RSTile(1954, 8999, 1),new RSTile(1951, 8992, 1),new RSTile(1947, 8986, 1),new RSTile(1940, 8986, 1),new RSTile(1937, 8982, 1),new RSTile(1935, 8979, 1),new RSTile(1927, 8978, 1),new RSTile(1924, 8999, 1) });
    private static final RSArea bankArea = new RSArea(new RSTile[]{new RSTile(2466, 2849, 1), new RSTile(2466, 2845, 1), new RSTile(2465, 2843, 1), new RSTile(2464, 2842, 1), new RSTile(2463, 2841, 1), new RSTile(2462, 2840, 1), new RSTile(2461, 2839, 1), new RSTile(2458, 2839, 1), new RSTile(2455, 2839, 1), new RSTile(2454, 2839, 1), new RSTile(2454, 2842, 1), new RSTile(2457, 2842, 1), new RSTile(2459, 2842, 1), new RSTile(2461, 2844, 1), new RSTile(2462, 2845, 1), new RSTile(2463, 2846, 1), new RSTile(2463, 2848, 1)});

    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public String toString() {
        return "Banking " + " Status: " + getTaskState().name();
    }

    @Override
    public boolean validate() {
        return (Inventory.isFull() && Inventory.find(food).length == 0) || (!dragonArea.contains(Player.getPosition()) && !(Inventory.find(food).length == 5)) || (!dragonArea.contains(Player.getPosition()) && Inventory.find(food).length > 0 && Inventory.find(dragonLoot.length).length > 0);
    }

    @Override
    public void execute() {
        switch (getTaskState()) {

            case TELEPORT_TO_MYTHS_GUILD:

                ItemEntity mythsCape = Entities.find(ItemEntity::new)
                        .nameEquals("Mythical cape")
                        .actionsEquals("Teleport");

                if (mythsCape != null) {
                    RSItem[] Cape = Inventory.find(mythicalCape);
                    if (Cape != null && Cape.length > 0 && dragonArea.contains(Player.getPosition())) {
                        if (Cape[0].click("Teleport")) {
                            Timing.waitCondition(() -> {
                                General.sleep(General.randomSD(3000, 30));
                                return mythsTeleportArea.contains(Player.getPosition());
                            }, General.random(300, 400));
                        }
                        General.sleep(General.randomSD(450, 45));
                    }
                    System.out.println("Teleporting to the Myths Guild. Moving to bank.");
                }
                break;

            case CLIMBING_STAIRS:

                if (mythsTeleportArea.contains(Player.getPosition())) {
                    Camera.setCameraAngle(100);

                    ObjectEntity stairs = Entities.find(ObjectEntity::new)
                            .nameEquals("Stairs")
                            .actionsEquals("Climb-up");

                    if (stairs != null) {
                        RSObject[] stairCase = Objects.findNearest(25, "Stairs");
                        if (stairCase != null) {
                            if (stairCase[0].click("Climb-up")) {
                                Timing.waitCondition(() -> {
                                    General.sleep(General.randomSD(750, 90));
                                    return !Player.isMoving();
                                }, General.random(400, 1200));
                                System.out.println("Climbing stairs.");
                            } else {
                                if (!stairCase[0].click("Climb-up")) {
                                    if (!bottomOfStairs.isClickable() && bottomOfStairs.adjustCameraTo()) {
                                        System.out.println("Rotating camera so stairs are visible...");
                                        General.sleep(General.randomSD(450, 40));
                                    }
                                }
                            }
                        }
                    }
                }
                break;

            case CLICKING_ON_BANK:

                RSObject[] bankChest = Objects.findNearest(20, "Bank chest");

                if (bankArea.contains(Player.getPosition()) && !Banking.isBankScreenOpen()) {
                    if (!bankTile.isClickable() && bankTile.adjustCameraTo()) {
                        System.out.println("Banking: Rotating camera towards bank tile.");
                        General.sleep(General.randomSD(450, 34));
                    }
                }
                    if (bankTile.isClickable()) {
                        Banking.openBank();
                        System.out.print("Banking: Bank chest is on screen, clicking on bank.");
                        Timing.waitCondition(() -> {
                            General.sleep(General.randomSD(500, 120));
                            return Banking.isBankScreenOpen();
                        }, General.random(3000, 4000));
                    }

                break;

            case BANKING:

                if (Banking.isBankLoaded()) {
                    if (Inventory.find(dragonLoot).length > 0) {
                        System.out.println("Bank is open, attempting to deposit all...");
                        Banking.depositAllExcept(mythicalCape);
                        Timing.waitCondition(() -> {
                                    General.sleep(General.randomSD(300, 120));
                                    return !Inventory.isFull();
                                },
                                General.random(1000, 1500));
                    }
                    if (!Inventory.isFull()) {
                        System.out.println("We have successfully deposited all loot.");
                        Banking.withdraw(5, food);
                        Timing.waitCondition(() -> {
                            General.sleep(General.randomSD(500,45));
                            return Inventory.find(food).length > 0;
                                },
                                General.random(450, 650));
                        System.out.println("Attempting to withdraw food...");
                        General.sleep(General.randomSD(700, 70));

                    }
                    if (Inventory.find(food).length == 5 && Inventory.find(dragonLoot).length == 0) {
                            System.out.println("We have successfully withdrawn 5 food. Attempting to close bank...");
                            Banking.close();
                            if (!Banking.isBankScreenOpen()) {
                                System.out.println("Bank closed successfully.");
                                //suggestion Improve by using the Esc keys to close bank as that is more human-like
                            }
                        }
                    }
                break;

            case BANKING_FAILSAFE:

                if (Inventory.find(food).length == 5 && Inventory.find(dragonLoot).length == 0 && Banking.isBankScreenOpen()) {
                    System.out.println("[BANKING_FAILSAFE]: Bank is still open, attempting to close...");
                    Banking.close();
                    Timing.waitCondition(() -> {
                        General.sleep(600, 750);
                        return !Banking.isBankScreenOpen();
                    }, General.random(700, 1300));
                    if (!Banking.isBankScreenOpen()) {
                        System.out.println("[BANKING_FAILSAFE]: Bank successfully closed.");
                    }

                } else {
                    if ((!(Inventory.find(food).length == 5)) && !Banking.isBankScreenOpen() && Player.getPosition().equals(bankTile)) {
                        System.out.println("[BANKING_FAILSAFE]: Player is standing at bank, but is not equipped properly. Attempting to open bank...");
                        Banking.openBank();
                        Timing.waitCondition(() -> {
                            General.sleep(300, 500);
                            return Banking.isBankScreenOpen() && Banking.isBankLoaded();
                        }, General.random(300, 400));
                        System.out.println(("[BANKING_FAILSAFE]: Bank successfully opened. Attempting to deposit items..."));
                        if (Banking.isBankScreenOpen() && !(Inventory.find(food).length == 5) && Player.getPosition().equals(bankTile)) {
                            Banking.depositAllExcept(mythicalCape);
                            if (Inventory.getAll().length == 1) {
                                System.out.println("[BANKING_FAILSAFE]: Successfully deposited all items. Attempting to withdraw food...");
                                Banking.withdraw(5, food);
                                Timing.waitCondition(() -> {
                                            General.sleep(General.randomSD(500,45));
                                            return Inventory.find(food).length > 0;
                                        },
                                        General.random(450, 650));
                                if (Inventory.find(food).length == 5) {
                                    System.out.println("[BANKING_FAILSAFE]: Successfully withdrawn food...");
                                } else {
                                    General.sleep(400, 600);
                                }
                            } else {
                                General.sleep(400, 800); // could add failsafe for inventory only containing one item but that item isn't a mythical cape
                            }

                        }
                    }
                }
                if (Banking.isBankLoaded() && Inventory.find(food).length > 0 && Inventory.find(dragonLoot).length > 0) {
                    Banking.depositAllExcept(mythicalCape);
                    System.out.println("[BANKING_FAILSAFE]: Incorrect inventory setup. Attempting to deposit items...");
                }
                if (Banking.isBankLoaded() && Inventory.find(food).length > 5) {
                    Banking.depositAllExcept(mythicalCape);
                    System.out.println("[BANKING_FAILSAFE]: Withdrew too much food, reattempting banking.");
                }

                break;

        }
    }
    private enum TaskState {

        TELEPORT_TO_MYTHS_GUILD,
        CLIMBING_STAIRS,
        CLICKING_ON_BANK,
        BANKING,
        RETURNING_TO_COMBAT,
        BANKING_FAILSAFE

    }

    private HandleBanking.TaskState getTaskState () {

        if (dragonArea.contains(Player.getPosition()) && Inventory.isFull() && Inventory.find(food).length == 0) {
            return HandleBanking.TaskState.TELEPORT_TO_MYTHS_GUILD;
        }
        if (mythsTeleportArea.contains(Player.getPosition()) && Inventory.isFull() && Inventory.find(food).length == 0) {
            return HandleBanking.TaskState.CLIMBING_STAIRS;
        }
        if ((topOfStairs.contains(Player.getPosition()) && Inventory.isFull() && Inventory.find(food).length == 0) || (bankArea.contains(Player.getPosition()) && !Banking.isBankScreenOpen())) {
            return HandleBanking.TaskState.CLICKING_ON_BANK;
        }
        if ((Player.getPosition().equals(bankTile) && Inventory.isFull() && Inventory.find(food).length == 0) || (Banking.isBankLoaded())) {
            return HandleBanking.TaskState.BANKING;
        }
        return HandleBanking.TaskState.BANKING_FAILSAFE;
    }
}


