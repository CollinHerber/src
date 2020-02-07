package scripts.MudRuneMaker.Tasks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.types.RSVarBit;
import scripts.MudRuneMaker.Antiban.Antiban;
import scripts.MudRuneMaker.Antiban.PersistantABCUtil;
import scripts.MudRuneMaker.framework.Priority;
import scripts.MudRuneMaker.framework.Task;
import scripts.MudRuneMaker.entityselector.Entities;
import scripts.MudRuneMaker.entityselector.finders.prefabs.ItemEntity;

public class HandleBanking implements Task {

    public static final RSArea VARROCK_TELEPORT_AREA = new RSArea(new RSTile[]{new RSTile(3206, 3421, 0), new RSTile(3220, 3421, 0), new RSTile(3221, 3438, 0), new RSTile(3205, 3438, 0)});

    public String[] bindingNecklace = {"Binding necklace"};
    public String[] inventoryItems = {"Varrock teleport", "Water rune"};
    public String[] pureEssence = {"Pure essence"};
    public String[] staminaPotion = {"Stamina potion(4)", "Stamina potion(3)", "Stamina potion(2)", "Stamina potion(1)"};
    public String[] watertalisman = {"Water talisman"};
    public String[] earthRunes = {"Earth rune"};

    public RSVarBit staminaPotionVarBit = RSVarBit.get(25);

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public String toString() {
        return "BANKING " + " Status: " + getTaskState().name();
    }

    @Override
    public boolean validate() {
        return Banking.isInBank() && Inventory.find("Pure essence").length == 0;
    }

    @Override
    public void execute() {

        switch (getTaskState()) {

            case OPENING_BANK:
                Banking.openBank();
                Antiban.leaveGame();
                General.sleep(1300, 2400);
                General.println("Opening bank");
                break;

            case DEPOSITING_ITEMS:
                Banking.depositAllExcept(inventoryItems);
                General.println("Deposited all items except Varrock teleport tabs and Water runes.");
                if (Inventory.find(earthRunes).length > 0) {
                    Banking.depositAllExcept(inventoryItems);
                    Antiban.leaveGame();
                }
                General.sleep(764, 1450);
                break;

            case REQUIPPING_BINDING_NECKLACE:

                if (Equipment.getItem(Equipment.SLOTS.AMULET) == null) {
                    Banking.withdraw(1, bindingNecklace);
                    General.println("Withdrawing binding necklace and equipping");
                    General.sleep(1000, 1546);

                    ItemEntity bindingNecklace = Entities.find(ItemEntity::new)
                            .nameEquals("Binding necklace")
                            .actionsEquals("Wear");

                    if (bindingNecklace != null) {
                        RSItem[] bindingNecklaceInventory = Inventory.find("Binding necklace");
                        if (bindingNecklaceInventory != null && bindingNecklaceInventory.length > 0) {
                            if (bindingNecklaceInventory[0].click("Wear")) {
                                Antiban.waitItemInteractionDelay();
                                Timing.waitCondition(() -> {
                                    General.sleep(General.randomSD(3000, 30));
                                    return Inventory.find("Binding necklace").length == 0;
                                }, General.random(782, 1345));
                            }
                            General.println("BANKING: Equipped binding necklace.");
                        }
                    }
                    if (Equipment.getItem(Equipment.SLOTS.AMULET) != null && Inventory.find("Binding necklace").length > 0) {
                        Banking.depositAllExcept(inventoryItems);
                        General.sleep(980, 1500);
                        General.println("BANKING: You are already wearing a binding necklace, banking spare necklace.");
                    }
                }

                break;

            case REDRINKING_STAMINA:

                if (staminaPotionVarBit.getValue() != 1) {
                    Banking.withdraw(1, staminaPotion);
                    General.sleep(1000, 1500);

                    ItemEntity staminaPotion = Entities.find(ItemEntity::new)
                            .nameContains("Stamina potion(")
                            .actionsEquals("Drink");

                    if (staminaPotion != null) {

                        RSItem[] staminaPotionInventory4 = Inventory.find("Stamina potion(4)");
                        RSItem[] staminaPotionInventory3 = Inventory.find("Stamina potion(3)");
                        RSItem[] staminaPotionInventory2 = Inventory.find("Stamina potion(2)");
                        RSItem[] staminaPotionInventory = Inventory.find("Stamina potion(1)");

                        if (staminaPotionInventory4 != null && staminaPotionInventory4.length > 0) {
                                if (staminaPotionInventory4[0].click("Drink")) {
                                    Timing.waitCondition(() -> {
                                        General.sleep(General.randomSD(1350, 30));
                                        return staminaPotionVarBit.getValue() == 1;
                                    }, General.random(776, 1034));
                                }
                                General.println("Drank one dose of Stamina potion (4).");
                            }
                            if (staminaPotionInventory3 != null && staminaPotionInventory3.length > 0) {
                                if (staminaPotionInventory3[0].click("Drink")) {
                                    Timing.waitCondition(() -> {
                                        General.sleep(General.randomSD(1350, 30));
                                        return staminaPotionVarBit.getValue() == 1;
                                    }, General.random(776, 1034));
                                }
                                General.println("Drank one dose of Stamina potion (3).");
                            }
                            if (staminaPotionInventory2 != null && staminaPotionInventory2.length > 0) {
                                if (staminaPotionInventory2[0].click("Drink")) {
                                    Timing.waitCondition(() -> {
                                        General.sleep(General.randomSD(1350, 30));
                                        return staminaPotionVarBit.getValue() == 1;
                                    }, General.random(776, 1034));
                                }
                                General.println("Drank one dose of Stamina potion (2).");
                            }
                            if (staminaPotionInventory != null && staminaPotionInventory.length > 0) {
                                if (staminaPotionInventory[0].click("Drink")) {
                                    Timing.waitCondition(() -> {
                                        General.sleep(General.randomSD(1350, 30));
                                        return staminaPotionVarBit.getValue() == 1;
                                }, General.random(776, 1034));
                            }
                                General.println("Drank one dose of Stamina potion (1).");
                        }
                    }
                    Antiban.getReactionTime();
                    Antiban.sleepReactionTime();
                }
                if (staminaPotionVarBit.getValue() == 1 && Inventory.find(staminaPotion).length > 0) {
                    Banking.depositAllExcept(inventoryItems);
                    General.println("BANKING: Deposited stamina potion in bank");
                    General.sleep(745, 1213);
                }
                if (Inventory.find("Vial").length > 0) {
                    Banking.deposit(1, "Vial");
                    General.println("BANKING: Deposited vial");
                    General.sleep(745, 1213);
                }
                break;

            case WITHDRAWING_ITEMS:

               if (Inventory.find(pureEssence).length == 0) {
                   Banking.withdraw(25, pureEssence);
                   General.sleep(679, 1123);
            }
               if (Inventory.find(watertalisman).length == 0) {
                   Banking.withdraw(1, watertalisman);
                   General.sleep(679, 1123);
               }
                General.println("BANKING: Withdrawn the required items");
               Antiban.randomMovement();
               Antiban.leaveGame();

                break;

            case BANKING_FAILSAFE:

                Banking.depositAllExcept(inventoryItems);
                General.sleep(850,1230);

            case CLOSING_BANK:
                Banking.close();
                General.println("BANKING: Bank closed.");

                break;

        }
    }

    private enum TaskState {

        OPENING_BANK,
        DEPOSITING_ITEMS,
        REQUIPPING_BINDING_NECKLACE,
        REDRINKING_STAMINA,
        WITHDRAWING_ITEMS,
        BANKING_FAILSAFE,
        CLOSING_BANK

    }

    private TaskState getTaskState() {

        if (Banking.isInBank() && !Banking.isBankScreenOpen()) {
            return TaskState.OPENING_BANK;
        }
        if (Banking.isBankScreenOpen() && Inventory.find("Mud rune").length > 0) {
            return TaskState.DEPOSITING_ITEMS;
        }
        if (Equipment.getItem(Equipment.SLOTS.AMULET) == null && Banking.isBankScreenOpen()) {
            return TaskState.REQUIPPING_BINDING_NECKLACE;
        }
        if (staminaPotionVarBit.getValue() != 1 && Banking.isBankScreenOpen()) {
            return TaskState.REDRINKING_STAMINA;
        }
        if ((Inventory.find(pureEssence).length == 0 || Inventory.find(watertalisman).length == 0) && Banking.isBankScreenOpen()) {
            return TaskState.WITHDRAWING_ITEMS;
        }
        if ((Inventory.find(watertalisman).length > 1) || (Inventory.find(staminaPotion).length > 0 && Inventory.find(pureEssence).length > 0) || (Inventory.find(bindingNecklace).length > 0 && Inventory.find(pureEssence).length > 0) || ((!(Inventory.find(pureEssence).length == 25)) && (Inventory.find(staminaPotion).length > 0 || Inventory.find(bindingNecklace).length > 0))) {
            return TaskState.BANKING_FAILSAFE;
        }
        return TaskState.CLOSING_BANK;
    }
}


