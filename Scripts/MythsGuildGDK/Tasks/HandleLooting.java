package scripts.MythsGuildGDK.Tasks;

import org.tribot.api.General;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import scripts.MythsGuildGDK.Antiban.Antiban;
import scripts.entityselector.*;
import scripts.entityselector.finders.prefabs.GroundItemEntity;
import scripts.MythsGuildGDK.framework.Priority;
import scripts.MythsGuildGDK.framework.Task;
import scripts.entityselector.finders.prefabs.ItemEntity;
import scripts.framework.abc.PersistantABCUtil;

public class HandleLooting implements Task {

    // if anything fucks up it's because I changed these to private

    private RSGroundItem[] loot = Entities.find(GroundItemEntity::new).nameEquals("Green dragonhide", "Dragon bones", "Nature rune",
            "Grimy ranarr weed", "Shield left half", "Dragon spear", "Ensouled dragon head")
            .sortByDistance()
            .actionsEquals("Take")
            .getResults();

    private String[] dragonLoot = {"Green dragonhide", "Dragon bones", "Nature rune",
            "Grimy ranarr weed", "Shield left half", "Dragon spear", "Ensouled dragon head"};
    private static String[] food = {"Lobster"};
    private String[] dragon = {"Green dragon"};


    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public String toString() {
        return "Looting " + "Status: " + getTaskState().name();
    }

    @Override
    public boolean validate() {
        return GroundItems.find(dragonLoot).length > 0 && (!Inventory.isFull() || Inventory.find(food).length > 0); // removed Inventory.isfull()

    }

    @Override
    public void execute() {

        switch (getTaskState()) {


            case MAKING_SPACE_FOR_LOOT: //improve this by making it eat the required amount to make space for food before looting

                ItemEntity isEdible = Entities.find(ItemEntity::new)
                        .actionsEquals("Eat");
                if (isEdible != null) {
                    RSItem[] edible = Inventory.find(food);
                    if (edible != null && edible.length > 0) {
                        if (edible[0].click("Eat")) {
                            General.sleep(General.randomSD(750, 45));
                        }
                        General.println("HandleLooting: Eating food to create inventory space, new health is now:" + Combat.getHP());
                    }
                }
                    break;

            case LOOTING:
                if (!Inventory.isFull()) { // removed this, if this stops working correctly place back... && Inventory.find(food).length >= 0) {
                    if (loot != null) {
                        RSGroundItem[] groundLoot = GroundItems.find(dragonLoot);
                        if (groundLoot != null & groundLoot.length > 0) {
                            if (groundLoot[0].isClickable()) {
                                if (groundLoot[0].click("Take " + groundLoot[0].getDefinition().getName())) {
                                    General.sleep(General.randomSD(430, 80));
                                    General.println("HandleLooting: Looting items...");
                                }
                            } else {
                                if (!groundLoot[0].isClickable() && groundLoot[0].adjustCameraTo()) {
                                    General.println("HandleLooting: Rotating camera so that loot is visible.");
                                }
                            }
                        }
                    }

                    break;
                }


            case IDLING: //need to change to something less pointless

                if (dragonLoot.length == 0 && NPCs.find(dragon) == null) {
                    General.sleep(1200, 2000);
                    General.println("HandleLooting: There is no loot, and no dragons available... Idling.");
                }
                break;
        }
    }

    private enum TaskState {

        MAKING_SPACE_FOR_LOOT,
        LOOTING,
        IDLING

    }

    private TaskState getTaskState() {

        if (GroundItems.find(dragonLoot).length > 0 && Inventory.isFull() && Inventory.find(food).length > 0){
            return TaskState.MAKING_SPACE_FOR_LOOT;
        }
        if (GroundItems.find(dragonLoot).length > 0 && !Inventory.isFull()) {
            return TaskState.LOOTING;
        }
        return TaskState.IDLING;

    }
}
