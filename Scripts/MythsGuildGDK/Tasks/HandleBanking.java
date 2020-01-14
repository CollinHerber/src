package scripts.MythsGuildGDK.Tasks;

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

    private static final RSArea mythsTeleportArea = new RSArea(new RSTile [] { new RSTile(2456, 2852, 0),new RSTile(2458, 2852, 0),new RSTile(2459, 2851, 0),new RSTile(2460, 2851, 0),new RSTile(2461, 2850, 0),new RSTile(2461, 2849, 0),new RSTile(2461, 2848, 0),new RSTile(2461, 2847, 0),new RSTile(2461, 2846, 0),new RSTile(2461, 2845, 0),new RSTile(2461, 2844, 0),new RSTile(2459, 2843, 0),new RSTile(2458, 2842, 0),new RSTile(2456, 2842, 0),new RSTile(2455, 2843, 0),new RSTile(2454, 2843, 0),new RSTile(2453, 2844, 0),new RSTile(2453, 2845, 0),new RSTile(2453, 2846, 0),new RSTile(2453, 2847, 0),new RSTile(2453, 2848, 0),new RSTile(2453, 2849, 0),new RSTile(2453, 2850, 0),new RSTile(2454, 2851, 0) });
    private static final RSArea topOfStairs = new RSArea(new RSTile [] { new RSTile(2454, 2839, 1),new RSTile(2459, 2839, 1),new RSTile(2459, 2841, 1),new RSTile(2454, 2841, 1) });
    private static final RSArea dragonArea = new RSArea(new RSTile[]{new RSTile(1947, 8987, 1), new RSTile(1936, 8987, 1), new RSTile(1936, 8994, 1), new RSTile(1939, 8994, 1), new RSTile(1941, 8995, 1), new RSTile(1943, 8997, 1), new RSTile(1944, 8999, 1), new RSTile(1945, 9000, 1), new RSTile(1946, 9000, 1), new RSTile(1947, 9000, 1), new RSTile(1948, 9000, 1), new RSTile(1949, 8999, 1), new RSTile(1950, 8998, 1), new RSTile(1950, 8997, 1), new RSTile(1949, 8995, 1), new RSTile(1948, 8994, 1), new RSTile(1947, 8994, 1), new RSTile(1946, 8993, 1)});

    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public String toString() {
        return "Banking " + "Status: " + getTaskState().name();
    }

    @Override
    public boolean validate() {
        return (Inventory.find(food).length == 0 && Inventory.isFull()) || (Inventory.find(food).length == 15 && Player.getPosition().equals(bankTile) && Banking.isBankScreenOpen()) ;
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
                                General.sleep(General.randomSD(2000, 30));
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
                                }
                                else {
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

                if (topOfStairs.contains(Player.getPosition())) {
                    ObjectEntity bank = Entities.find(ObjectEntity::new)
                            .nameEquals("Bank chest")
                            .actionsEquals("Use");

                    if (bank != null) {
                        RSObject[] bankChest = Objects.findNearest(20, "Bank chest");
                        if (bankChest != null) {
                            if (bankChest[0].click("Use")) {
                                System.out.println("Clicked on bank, attempting to open...");
                                Timing.waitCondition(() -> {
                                    General.sleep(General.randomSD(500, 120));
                                    return Banking.isBankScreenOpen();
                                }, General.random(3000, 4000));
                            }
                        }
                    }
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
                        Banking.withdraw(15, food);
                        System.out.println("Attempting to withdraw food...");
                        General.sleep(General.randomSD(700, 70));

                    } else {

                        if (!Banking.isBankScreenOpen()) {
                            if (Player.isMoving()) {
                                Timing.waitCondition(() -> {
                                    General.sleep(General.randomSD(400, 25));
                                    System.out.println("Sleeping while waiting for bank to open.");
                                    return Banking.isBankScreenOpen();
                                }, General.random(1000, 3000));
                            }
                            else {
                                if (Player.getPosition().equals(bankTile) && !Banking.isBankScreenOpen() && (!(Inventory.find(food).length == 15))) {
                                    Banking.openBank();
                                    System.out.println("Player is idle at bank, opening bank...");
                                    General.sleep(2000, 3000);
                                }
                            }
                        }
                    }
                    if (Inventory.find(food).length == 15 && Inventory.find(dragonLoot).length == 0) {
                        System.out.println("We have successfully withdrawn 15 food. Attempting to close bank...");
                        Banking.close();
                        if (!Banking.isBankScreenOpen()) {
                            System.out.println("Bank closed successfully.");

                            }
                            //suggestion Improve by using the Esc keys to close bank as that is more human-like
                        }
                }
                break;

            case BANKING_FAILSAFE:


                if (Inventory.find(food).length == 15 && Inventory.find(dragonLoot).length == 0 && Banking.isBankScreenOpen()) {
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
                    if ((!(Inventory.find(food).length == 15)) && !Banking.isBankScreenOpen() && Player.getPosition().equals(bankTile)) {
                        System.out.println("[BANKING_FAILSAFE]: Player is standing at bank, but is not equipped properly. Attempting to open bank...");
                        Banking.openBank();
                        Timing.waitCondition(() -> {
                            General.sleep(300, 500);
                            return Banking.isBankScreenOpen() && Banking.isBankLoaded();
                        }, General.random(300, 400));
                        System.out.println(("[BANKING_FAILSAFE]: Bank successfully opened. Attempting to deposit items..."));
                        if (Banking.isBankScreenOpen() && !(Inventory.find(food).length == 15) && Player.getPosition().equals(bankTile)) {
                            Banking.depositAllExcept(mythicalCape);
                            if (Inventory.getAll().length == 1) {
                                System.out.println("[BANKING_FAILSAFE]: Successfully deposited all items. Attempting to withdraw food...");
                                Banking.withdraw(15, food);
                                if (Inventory.find(food).length == 15) {
                                    System.out.println("[BANKING_FAILSAFE]: Successfully withdrawn food...");
                                } else {
                                    General.sleep(400, 1200);
                                }
                            } else {
                                General.sleep(400, 1200);
                            }

                        }
                    }
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

        private TaskState getTaskState () {

            if (dragonArea.contains(Player.getPosition()) && Inventory.isFull() && Inventory.find(food).length == 0) {
                return TaskState.TELEPORT_TO_MYTHS_GUILD;
            }
            if (mythsTeleportArea.contains(Player.getPosition()) && Inventory.isFull() && Inventory.find(food).length == 0) {
                return TaskState.CLIMBING_STAIRS;
            }
            if (topOfStairs.contains(Player.getPosition()) && Inventory.isFull() && Inventory.find(food).length == 0) {
                return TaskState.CLICKING_ON_BANK;
            }
            if (Player.getPosition().equals(bankTile) && Inventory.isFull() && Inventory.find(food).length == 0) {
                return TaskState.BANKING;
            }
            return TaskState.BANKING_FAILSAFE;
        }
}
