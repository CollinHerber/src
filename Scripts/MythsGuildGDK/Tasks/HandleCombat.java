package scripts.MythsGuildGDK.Tasks;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;

import scripts.MythsGuildGDK.Antiban.PersistantABCUtil;
import scripts.entityselector.*;
import scripts.entityselector.finders.prefabs.ItemEntity;
import scripts.entityselector.finders.prefabs.NpcEntity;
import scripts.MythsGuildGDK.framework.*;

public class HandleCombat implements Task {

    private static int eatAtHP = PersistantABCUtil.getEatPercentage();
    private static String[] food = {"Lobster"};
    private static final RSArea greenDragonArea = new RSArea(new RSTile [] { new RSTile(1946, 8989, 1),new RSTile(1941, 8989, 1),new RSTile(1941, 8996, 1),new RSTile(1943, 8998, 1),new RSTile(1944, 8998, 1),new RSTile(1944, 8999, 1),new RSTile(1944, 9000, 1),new RSTile(1945, 9001, 1),new RSTile(1946, 9001, 1),new RSTile(1947, 9000, 1),new RSTile(1948, 8999, 1),new RSTile(1949, 8999, 1),new RSTile(1950, 8998, 1),new RSTile(1950, 8997, 1),new RSTile(1949, 8996, 1),new RSTile(1947, 8996, 1),new RSTile(1946, 8995, 1),new RSTile(1946, 8994, 1),new RSTile(1945, 8993, 1),new RSTile(1945, 8991, 1),new RSTile(1946, 8991, 1) });
    public static final RSArea dragonArea = new RSArea(new RSTile [] { new RSTile(1937, 9000, 1),new RSTile(1944, 9006, 1),new RSTile(1954, 8999, 1),new RSTile(1951, 8992, 1),new RSTile(1947, 8986, 1),new RSTile(1940, 8986, 1),new RSTile(1937, 8982, 1),new RSTile(1935, 8979, 1),new RSTile(1927, 8978, 1),new RSTile(1924, 8999, 1) });

    private RSNPC greenDragon = Entities.find(NpcEntity::new).nameEquals("Green dragon").actionsEquals("Attack")
            .sortByDistance()
            .getFirstResult();
        // dragon death animations I have found are 28, 92, 4642

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public String toString() {
        return "Combat " + " Status: " + getTaskState().name();
    }

    @Override
    public boolean validate() {
        return Inventory.find(food).length > 0 && (dragonArea.contains(Player.getPosition()) || (greenDragon.isOnScreen() && !dragonArea.contains(Player.getPosition())) || greenDragon.isOnScreen() && !(Combat.getTargetEntity() == greenDragon));
    }

    @Override
    public void execute() {

        switch (getTaskState()) {

            case ATTACKING_DRAGON:

                RSNPC dragon = Entities.find(NpcEntity::new).nameEquals("Green dragon").actionsEquals("Attack")
                        .sortByDistance()
                        .getFirstResult();
                if (dragon != null) {

                    if (!dragon.isClickable() && dragon.adjustCameraTo()) { //this is short circuiting. If the first isn't reached the other will be performed.
                        General.sleep(General.randomSD(325, 45));
                    }
                    if (!dragon.isInCombat()) {//can add here the death animation check
                        if (DynamicClicking.clickRSNPC(dragon, "Attack")) {
                            System.out.println("Attacking dragon...");
                            Timing.waitCondition(() -> {
                                General.sleep(General.randomSD(750, 90));
                                return Combat.isUnderAttack();
                            }, General.random(3000, 4000));
                        }
                        General.sleep(General.randomSD(600, 70));
                    }
                } else {
                    if (dragon == null) {
                        System.out.println("HandleCombat: Idle while waiting for dragons to spawn");
                        PersistantABCUtil.handleIdleActions();
                        General.sleep(General.randomSD(1500, 45));
                    }
                }

                break;

            case EATING_FOOD:

                ItemEntity isEdible = Entities.find(ItemEntity::new)
                            .actionsEquals("Eat");

                    if (isEdible != null) {
                        RSItem[] edible = Inventory.find(food);
                        if (edible != null && edible.length > 0) {
                            if (edible[0].click("Eat")) {
                                General.sleep(General.randomSD(750, 45));
                            }
                            System.out.println("HandleCombat: Eating food, new health is now:" + Combat.getHP());
                            PersistantABCUtil.generateEatPercentage();

                        }

                }
                    break;

            case RETURN_TO_COMBAT_ZONE:

                Walking.walkTo(greenDragonArea.getRandomTile());
                System.out.println("HandleCombat: Player has left combat area, returning to combat area...");
                while (Player.isMoving()) {
                    PersistantABCUtil.handleIdleActions();
                    General.sleep(1200, 1800);
                }

                break;

                case IDLE_IN_COMBAT:

                        if (Combat.isUnderAttack() && Combat.getMaxHP() > eatAtHP) {
                            System.out.println("HandleCombat: Under attack. Idling and performing ABC actions...");
                                PersistantABCUtil.handleIdleActions();
                                General.sleep(General.randomSD(2450, 450));
                            }
                    break;
        }
    }


        private enum TaskState {

            ATTACKING_DRAGON,
            EATING_FOOD,
            RETURN_TO_COMBAT_ZONE,
            IDLE_IN_COMBAT

        }

        private TaskState getTaskState () {

            if (!Combat.isUnderAttack() && Inventory.find(food).length >= 1) {
                return TaskState.ATTACKING_DRAGON;
            }
            if (Combat.getHP() < eatAtHP) { //this was embarrassing, I had these the wrong way round for way too long not understanding why it was always eating
                return TaskState.EATING_FOOD;
            }
            if (Inventory.find(food).length > 0 && !dragonArea.contains(Player.getPosition())){
                return TaskState.RETURN_TO_COMBAT_ZONE;
            }
            return TaskState.IDLE_IN_COMBAT;

        }

}
