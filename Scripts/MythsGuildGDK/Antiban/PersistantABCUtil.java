package scripts.MythsGuildGDK.Antiban;

import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.util.abc.ABCProperties;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api.util.abc.preferences.OpenBankPreference;
import org.tribot.api.util.abc.preferences.TabSwitchPreference;
import org.tribot.api.util.abc.preferences.WalkingPreference;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;

public class PersistantABCUtil extends ABCUtil {

    private static PersistantABCUtil _instance = null;

    private int runPercentage = this.generateRunActivation();
    private int eatPercentage = this.generateEatAtHP();
    private int resourcesWon = 0;
    private int resourcesLost = 0;
    private long resourceSwitchCheckTime = Timing.currentTimeMillis() + General.random(20000, 30000);
    public long lastCombatTime = 0;

    private static Positionable nextTarget;
    private static Positionable nextTargetClosest;
    private long waitingSince;

    boolean shouldHover;
    boolean shouldOpenMenu;
    
    //new additions below
	
	private int totalAttemptCount;
	private long totalGatheringTime;
	private static final long ESTIMATED_GATHERING_TIME = 5000;

    private PersistantABCUtil() {
        // Prevent instantiation
    }

    /**
     * Returns the instance of this ABCOld.
     * Note that an instance should be used per character, if your script switches to a different character,
     * you should call ABCOld.get().close() to generate a new one.
     *
     * @return the antiban (ABCUtil) instance
     */
    public static PersistantABCUtil get() {
        return _instance = _instance != null ? _instance : new PersistantABCUtil();
    }

    public void setHoverAndMenuOpenBooleans() {
        this.shouldHover = _instance.shouldHover();
        this.shouldOpenMenu= _instance.shouldOpenMenu();
    }

    /**
     * Gets the health percentage when the player should eat.
     *
     * @return
     */
    public static int getEatPercentage() {
        return get().eatPercentage;
    }

    /**
     * Generates a new percentage to eat at. This should be used right after a successful eat.
     */
    public static void generateEatPercentage() {
        get().eatPercentage = get().generateEatAtHP();
    }

    /**
     * Gets the energy percentage when the player should activate run.
     *
     * @return
     */
    public static int getRunPercentage() {
        return get().runPercentage;
    }

    /**
     * Generates a new energy percentage to run at. This should be used right after run has been toggled on.
     */
    public static void generateRunPercentage() {
        get().runPercentage = get().generateRunActivation();
    }

    /**
     * Returns if we should move to the next anticipated spawn location.
     * This should be checked ONCE when a resource is depleted and no immediate new ones are available.
     *
     * @return true if we should move to the next anticipated location, false otherwise.
     */
    public static boolean shouldMoveAnticipated() {
        return get().shouldMoveToAnticipated();
    }

    /**
     * Returns how many times we have won a resource.
     *
     * @return
     */
    public static int getResourcesWon() {
        return get().resourcesWon;
    }

    /**
     * Returns how many times we have lost a resource.
     *
     * @return
     */
    public static int getResourcesLost() {
        return get().resourcesLost;
    }

    /**
     * Returns if we should hover over the next resource, note that this should only be called once when we start interacting with each new resource.
     *
     * @return true if we should hover the next resource, false otherwise.
     */
    public static boolean shouldHoverNext() {
        return Mouse.isInBounds() && get().shouldHover();
    }

    /**
     * Returns if we should right click the next resource while hovering, note that this should only be called if we are already hovering.
     *
     * @return true if we should open the menu of the next resource, false otherwise.
     */
    public static boolean shouldOpenMenuNext() {
        return Mouse.isInBounds() && get().shouldOpenMenu();
    }

    /**
     * Generates the preferences of how a player should open the bank (by booth/banker etc).
     * NOTE: You DO NOT need to use this if you use Banking#openBank().
     *
     * @return the preference
     */
    public static OpenBankPreference getBankPreference() {
        return get().generateOpenBankPreference();
    }

    /**
     * Generates the preferences of how a player should switch game tabs (with mouse/f keys etc).
     * NOTE: You DO NOT need to use this if you use GameTab#open().
     *
     * @return the preference
     */
    public static TabSwitchPreference getTabSwitchPreference() {
        return get().generateTabSwitchPreference();
    }

    /**
     * Generates the preferences of how a player should walk (with minimap/screen etc).
     * NOTE: You DO NOT need to use this if you solely use WebWalking.
     *
     * @param distance
     * @return
     */
    public static WalkingPreference getWalkingPreference(final int distance) {
        return get().generateWalkingPreference(distance);
    }

    /**
     * Sets the current time as when we last killed an NPC.
     */
    public static void setLastCombatTime() {
        get().lastCombatTime = Timing.currentTimeMillis();
    }

    /**
     * Sets the current time as when we last did something.
     */
    public static void setWaitingSince() {
        get().waitingSince = Timing.currentTimeMillis();
    }

    /**
     * Travels to the next anticipated resource if allowed.
     *
     * @param resource - the next resource / location to run to.
     * @return true if travelled, false otherwise.
     */
    public static boolean moveToAnticipated(Positionable resource) {

        if (resource != null && shouldMoveAnticipated()) {
            RSArea position = new RSArea(resource.getPosition(), 4);
            return WebWalking.walkTo(position.getRandomTile());
        }
        return true;
    }

    public void updateTrackers() {

        updateTrackers(false);

    }

    public void updateTrackers(final boolean fixed) {

        final boolean recentlyInCombat = (Combat.isUnderAttack() || (Timing.currentTimeMillis() - lastCombatTime < General.random(2000, 6000)));
        //final boolean isHovering = Hovering.isHovering();
//        final boolean shouldOpenMenu = Hovering.getShouldOpenMenu();

        final ABCProperties props = get().getProperties();

        props.setWaitingTime(getWaitingTime());
        //props.setHovering(isHovering);
        //props.setMenuOpen(Hovering.isMenuOpen());
        props.setUnderAttack(recentlyInCombat);
        props.setWaitingFixed(fixed);

        get().generateTrackers();
    }


    /**
     * Performs a wait for a calculated time. Based on real human playing data.
     */
    public void performReactionTimeWait() {

        updateTrackers();

        final int reactionTime = get().generateReactionTime();


        try {
            get().sleep(reactionTime);
        } catch (InterruptedException e) {
            General.println(e);
        }
    }

    public void hoverNextResource(RSObject target) {
        if (Mouse.isInBounds() && _instance.shouldHover()) {
            Clicking.hover(target);
            if (_instance.shouldOpenMenu())
                if (!ChooseOption.isOpen())
                    DynamicClicking.clickRSObject(target, 3);
        }
    }

    /**
     * Generates reaction time using bit flags.
     *
     * @param waitingTime - the amount of time the script was waiting for (e.g. amount of time spent cutting down a tree)
     * @return the generated reaction time in ms
     */
    private int generateReactionTime(int waitingTime) {
        boolean menuOpen = _instance.shouldOpenMenu() && _instance.shouldHover();
        boolean hovering = _instance.shouldHover();
        long menuOpenOption = menuOpen ? ABCUtil.OPTION_MENU_OPEN : 0;
        long hoverOption = hovering ? ABCUtil.OPTION_HOVERING : 0;

        return _instance.generateReactionTime(_instance.generateBitFlags(waitingTime, menuOpenOption, hoverOption));
    }

    /**
     * Generates supporting tracking information using bit flags.
     * This method should be called right after clicking something that will require the player to wait for a while.
     *
     * @param estimatedTime for the action to complete (e.g. amount of time spent cutting down a tree)
     */
    public void generateSupportingTrackerInfo(int estimatedTime) {
        _instance.generateTrackers(estimatedTime);
    }

    /**
     * Calls generate() and passes waitingTime as an argument.
     * Sleeps for the generated amount of time.
     *
     * @param waitingTime the amount of time the script was waiting for (e.g. amount of time spent cutting down a tree)
     */
    public void generateAndSleep(int waitingTime) {
        try {
            double time = generateReactionTime(waitingTime) * 0.65;
            General.println("[ABCOld] Sleeping");
            _instance.sleep((int) time);
        } catch (InterruptedException e) {

        }
    }


    /**
     * Use this method to track how many times you won or lost a resource.
     * Based on this data, we will switch resources if we are losing to much.
     *
     * @param won true if we won a resource, false if not.
     */
    public static void setResourceCounter(boolean won) {

//        log.info("Resource %s", won ? "Won" : "Lost");

        if (won)
            get().resourcesWon++;
        else
            get().resourcesLost++;
    }

    /**
     * Returns if we should change resources because we are losing a lot of resources due to other players in the area.
     * Note this method should be called continuously, internally it will only check every 20-30 seconds.
     *
     * @param competitionCount the amount of players who we are competing with.
     * @return true if we should switch, false otherwise.
     */
    public static boolean switchResources(int competitionCount) {

        double win_percent = ((double) (getResourcesWon() + getResourcesLost()) / (double) getResourcesWon());

        if (50.0 > win_percent && Timing.currentTimeMillis() >= get().resourceSwitchCheckTime) {

            if (get().shouldSwitchResources(competitionCount)) {
                return true;
            }

            get().resourceSwitchCheckTime = Timing.currentTimeMillis() + General.random(20000, 30000);
        }

        return false;
    }


    /**
     *
     * @return
     */
    public static int getWaitingTime() {

        int result = (int) (Timing.currentTimeMillis() - get().waitingSince);

        return result;
    }

    /**
     * Checks if run isn't on and we are allowed to activate it.
     * This handles the generation of new percentages as well.
     *
     * @return true if run was toggled, false if not.
     */
    public static boolean activateRun() {

        if (!Game.isRunOn() && Game.getRunEnergy() >= getRunPercentage()) {

            if (Options.setRunOn(true)) {
                generateRunPercentage();

                return true;
            }
        }

        return false;
    }

    /**
     * Do all the antiban actions we are supposed to do while idling.
     */
    public static boolean handleIdleActions() {

        if (get().shouldCheckTabs()) {
            General.println("[ABC2] : Checking Tabs. ");
            get().checkTabs();
            return true;
        }

        if (get().shouldCheckXP()) {
            General.println("[ABC2] : Hovering Xp. ");
            get().checkXP();
            return true;
        }

        if (get().shouldExamineEntity()) {
            General.println("[ABC2] : Examining Random Entity. ");
            get().examineEntity();
            return true;
        }

        if (get().shouldMoveMouse()) {
            General.println("[ABC2] : Moving Mouse. ");
            get().moveMouse();
            return true;
        }

        if (get().shouldPickupMouse()) {
            General.println("[ABC2] : Picking Up Mouse. ");
            get().pickupMouse();
            return true;
        }

        if (get().shouldRightClick()) {
            General.println("[ABC2] : Right Clicked.");
            get().rightClick();
            return true;
        }

        if (get().shouldRotateCamera()) {
            General.println("[ABC2] : Rotating Camera. ");
            get().rotateCamera();
            return true;
        }

        if (get().shouldLeaveGame()) {
            General.println("[ABC2] : Moving Mouse Off Screen. ");
            get().leaveGame();
            return true;
        }
        return false;
    }

    public RSObject getNextTarget(Positionable[]targets) {
        return (RSObject) _instance.selectNextTarget(targets);
    }
	public long getEstimatedWait()
	{
		long est = totalAttemptCount > 0 ? (int)Math.round(((double)totalGatheringTime / totalAttemptCount)) : ESTIMATED_GATHERING_TIME;
		
		return est < 0 ? ESTIMATED_GATHERING_TIME : est;
	}

	public void generateAndSleep(long waitingTime) {
        try {
            double time = generateReactionTime(waitingTime) * 0.65;
            General.println("[ABCOld] Sleeping");
            _instance.sleep((int) time);
        } catch (InterruptedException e) {

        }
    
	}

}
