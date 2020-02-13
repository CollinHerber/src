package scripts.fairyRingHelper;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSVarBit;

public class FairyRingHelper {

    // Created by Elliott 12/02/2020

    private FairyRingHelper() {}
    private static FairyRingHelper fairyRingHelper = new FairyRingHelper();
    public static FairyRingHelper get() {
        return fairyRingHelper;
    }
    public RSInterface fairyRingInterfaceIsValid() {
        return Interfaces.get(398);
    }

    public RSInterfaceChild firstFairyRingLetterClockwise() {
        return Interfaces.get(398, 19);
    }
    public RSInterfaceChild firstFairyRingLetterAntiClockwise() {
        return Interfaces.get(398, 20);
    }
    public RSInterfaceChild secondFairyRingLetterClockwise() {
        return Interfaces.get(398, 21);
    }
    public RSInterfaceChild secondFairyRingLetterAntiClockwise() {
        return Interfaces.get(398, 22);
    }
    public RSInterfaceChild thirdFairyRingLetterClockwise() {
        return Interfaces.get(398, 23);
    }
    public RSInterfaceChild thirdFairyRingLetterAntiClockwise() {
        return Interfaces.get(398, 24);
    }
    public RSInterfaceChild confirmFairyRingTeleportButton() {
        return Interfaces.get(398, 26);
    } 

    public RSVarBit firstFairyRingLetterVarbit = RSVarBit.get(3985); // 0 = a 1 = d 2 = c 3 = b
    public RSVarBit secondFairyRingLetterVarbit = RSVarBit.get(3986); // 0 = i 1 = l 2 = k 3 = j
    public RSVarBit thirdFairyRingLetterVarbit = RSVarBit.get(3987); // 0 = p 1 = s 2 = r 3 = q

    /** Call this to check that the fairy ring interface is open before selecting letters. **/
    public  boolean fairyRingInterfaceIsOpen(){
        return Interfaces.get(398) != null;
    }
    
    /** sets the first fairy ring letter to A **/
    public boolean setFirstFairyRingLetterA() {
            if (firstFairyRingLetterVarbit.getValue() != 0) {
                if (firstFairyRingLetterVarbit.getValue() == 1) {
                    final RSInterfaceChild firstFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                    firstFairyRingLetterClockwise.click();
                    General.sleep(General.randomSD(750, 75));
                }
                if (firstFairyRingLetterVarbit.getValue() == 2) {
                    final RSInterfaceChild firstFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                    firstFairyRingLetterClockwise.click();
                    General.sleep(General.randomSD(750, 75));
                    firstFairyRingLetterClockwise.click();
                    General.sleep(General.randomSD(750, 75));
                } else if (firstFairyRingLetterVarbit.getValue() == 3) {
                    final RSInterfaceChild firstFairyRingLetterAntiClockwise = firstFairyRingLetterAntiClockwise();
                    firstFairyRingLetterAntiClockwise.click();
                    General.sleep(General.randomSD(750, 75));
                }
            }
            General.println("[FAIRY RING NAVIGATION]: First letter 'A' is set");
            return firstFairyRingLetterVarbit.getValue() == 0;
    }

    /** sets the first fairy ring letter to D **/
    public boolean setFirstFairyRingLetterD() {

        if (firstFairyRingLetterVarbit.getValue() != 1) {
            if (firstFairyRingLetterVarbit.getValue() == 0) {
                final RSInterfaceChild firstFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                firstFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            if (firstFairyRingLetterVarbit.getValue() == 3) {
                final RSInterfaceChild firstFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                firstFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
                firstFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
           else if (firstFairyRingLetterVarbit.getValue() == 2 ) {
                final RSInterfaceChild firstFairyRingLetterAntiClockwise = firstFairyRingLetterAntiClockwise();
                firstFairyRingLetterAntiClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
        }
        General.println("[FAIRY RING NAVIGATION]: First letter 'D' is set");
        return firstFairyRingLetterVarbit.getValue() == 1;
    }

    /** sets the first fairy ring letter to C **/
    public boolean setFirstFairyRingLetterC() {

        if (firstFairyRingLetterVarbit.getValue() != 2) {
            if (firstFairyRingLetterVarbit.getValue() == 0) {
                final RSInterfaceChild firstFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                firstFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
                firstFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            if (firstFairyRingLetterVarbit.getValue() == 1) {
                final RSInterfaceChild firstFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                firstFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            else if (firstFairyRingLetterVarbit.getValue() == 3 ) {
                final RSInterfaceChild firstFairyRingLetterAntiClockwise = firstFairyRingLetterAntiClockwise();
                firstFairyRingLetterAntiClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
        }
        General.println("[FAIRY RING NAVIGATION]: First letter 'C' is set");
        return firstFairyRingLetterVarbit.getValue() == 2;
    }

    /** sets the first fairy ring letter to B **/
    public boolean setFirstFairyRingLetterB() {

        if (firstFairyRingLetterVarbit.getValue() != 3) {

            if (firstFairyRingLetterVarbit.getValue() == 1) {
                final RSInterfaceChild firstFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                firstFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
                firstFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            if (firstFairyRingLetterVarbit.getValue() == 2) {
                final RSInterfaceChild firstFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                firstFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            else if (firstFairyRingLetterVarbit.getValue() == 0 ) {
                final RSInterfaceChild firstFairyRingLetterAntiClockwise = firstFairyRingLetterAntiClockwise();
                firstFairyRingLetterAntiClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
        }
        General.println("[FAIRY RING NAVIGATION]: First letter 'B' is set");
        return firstFairyRingLetterVarbit.getValue() == 3;
    }

    /** sets the second fairy ring letter to I **/
    public boolean setSecondFairyRingLetterI() {

        if (secondFairyRingLetterVarbit.getValue() != 0) {
            if (secondFairyRingLetterVarbit.getValue() == 1) {
                final RSInterfaceChild secondFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                secondFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            if (secondFairyRingLetterVarbit.getValue() == 2) {
                final RSInterfaceChild secondFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                secondFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
                secondFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            else if (secondFairyRingLetterVarbit.getValue() == 3) {
                final RSInterfaceChild secondFairyRingLetterAntiClockwise = firstFairyRingLetterClockwise();
                secondFairyRingLetterAntiClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
        }
        General.println("[FAIRY RING NAVIGATION]: Second letter 'I' is set");
        return secondFairyRingLetterVarbit.getValue() == 0;
    }

    /** sets the second fairy ring letter to L **/
    public boolean setSecondFairyRingLetterL() {

        if (secondFairyRingLetterVarbit.getValue() != 1) {
            if (secondFairyRingLetterVarbit.getValue() == 0) {
                final RSInterfaceChild secondFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                secondFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            if (secondFairyRingLetterVarbit.getValue() == 3) {
                final RSInterfaceChild secondFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                secondFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
                secondFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            else if (secondFairyRingLetterVarbit.getValue() == 2) {
                final RSInterfaceChild secondFairyRingLetterAntiClockwise = firstFairyRingLetterClockwise();
                secondFairyRingLetterAntiClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
        }
        General.println("[FAIRY RING NAVIGATION]: Second letter 'L' is set");
        return secondFairyRingLetterVarbit.getValue() == 1;
    }

    /** sets the second fairy ring letter to K **/
    public boolean setSecondFairyRingLetterK() {

        if (secondFairyRingLetterVarbit.getValue() != 2) {
            if (secondFairyRingLetterVarbit.getValue() == 1) {
                final RSInterfaceChild secondFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                secondFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            if (secondFairyRingLetterVarbit.getValue() == 0) {
                final RSInterfaceChild secondFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                secondFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
                secondFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            else if (secondFairyRingLetterVarbit.getValue() == 3) {
                final RSInterfaceChild secondFairyRingLetterAntiClockwise = firstFairyRingLetterClockwise();
                secondFairyRingLetterAntiClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
        }
        General.println("[FAIRY RING NAVIGATION]: Second letter 'K' is set");
        return secondFairyRingLetterVarbit.getValue() == 2;
    }

    /** sets the second fairy ring letter to J **/
    public boolean setSecondFairyRingLetterJ() {

        if (secondFairyRingLetterVarbit.getValue() != 3) {
            if (secondFairyRingLetterVarbit.getValue() == 2) {
                final RSInterfaceChild secondFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                secondFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            if (secondFairyRingLetterVarbit.getValue() == 1) {
                final RSInterfaceChild secondFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                secondFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
                secondFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            else if (secondFairyRingLetterVarbit.getValue() == 0) {
                final RSInterfaceChild secondFairyRingLetterAntiClockwise = firstFairyRingLetterClockwise();
                secondFairyRingLetterAntiClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
        }
        General.println("[FAIRY RING NAVIGATION]: Second letter 'J' is set");
        return secondFairyRingLetterVarbit.getValue() == 3;
    }

    /** sets the second fairy ring letter to P **/
    public boolean setThirdFairyRingLetterP() {

        if (thirdFairyRingLetterVarbit.getValue() != 0) {
            if (thirdFairyRingLetterVarbit.getValue() == 1) {
                final RSInterfaceChild thirdFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                thirdFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            if (thirdFairyRingLetterVarbit.getValue() == 2) {
                final RSInterfaceChild thirdFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                thirdFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
                thirdFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            else if (thirdFairyRingLetterVarbit.getValue() == 3) {
                final RSInterfaceChild thirdFairyRingLetterAntiClockwise = firstFairyRingLetterClockwise();
                thirdFairyRingLetterAntiClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
        }
        General.println("[FAIRY RING NAVIGATION]: Third letter 'P' is set");
        return thirdFairyRingLetterVarbit.getValue() == 0;
    }

    /** sets the second fairy ring letter to S **/
    public boolean setThirdFairyRingLetterS() {

        if (thirdFairyRingLetterVarbit.getValue() != 1) {
            if (thirdFairyRingLetterVarbit.getValue() == 0) {
                final RSInterfaceChild thirdFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                thirdFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            if (thirdFairyRingLetterVarbit.getValue() == 3) {
                final RSInterfaceChild thirdFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                thirdFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
                thirdFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            else if (thirdFairyRingLetterVarbit.getValue() == 2) {
                final RSInterfaceChild thirdFairyRingLetterAntiClockwise = firstFairyRingLetterClockwise();
                thirdFairyRingLetterAntiClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
        }
        General.println("[FAIRY RING NAVIGATION]: Third letter 'S' is set");
        return thirdFairyRingLetterVarbit.getValue() == 1;
    }

    /** sets the second fairy ring letter to R **/
    public boolean setThirdFairyRingLetterR() {

        if (thirdFairyRingLetterVarbit.getValue() != 2) {
            if (thirdFairyRingLetterVarbit.getValue() == 1) {
                final RSInterfaceChild thirdFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                thirdFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            if (thirdFairyRingLetterVarbit.getValue() == 0) {
                final RSInterfaceChild thirdFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                thirdFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
                thirdFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            else if (thirdFairyRingLetterVarbit.getValue() == 3) {
                final RSInterfaceChild thirdFairyRingLetterAntiClockwise = firstFairyRingLetterClockwise();
                thirdFairyRingLetterAntiClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
        }
        General.println("[FAIRY RING NAVIGATION]: Third letter 'R' is set");
        return thirdFairyRingLetterVarbit.getValue() == 2;
    }

    /** sets the second fairy ring letter to Q **/
    public boolean setThirdFairyRingLetterQ() {

        if (thirdFairyRingLetterVarbit.getValue() != 3) {
            if (thirdFairyRingLetterVarbit.getValue() == 2) {
                final RSInterfaceChild thirdFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                thirdFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            if (thirdFairyRingLetterVarbit.getValue() == 1) {
                final RSInterfaceChild thirdFairyRingLetterClockwise = firstFairyRingLetterClockwise();
                thirdFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
                thirdFairyRingLetterClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
            else if (thirdFairyRingLetterVarbit.getValue() == 0) {
                final RSInterfaceChild thirdFairyRingLetterAntiClockwise = firstFairyRingLetterClockwise();
                thirdFairyRingLetterAntiClockwise.click();
                General.sleep(General.randomSD(750, 75));
            }
        }
        General.println("[FAIRY RING NAVIGATION]: Third letter 'Q' is set");
        return thirdFairyRingLetterVarbit.getValue() == 3;
    }

    /** confirms fairy rings and initiates the teleport **/
    public boolean confirmFairyRingTeleport() {

        if (fairyRingInterfaceIsOpen()) {
            final RSInterfaceChild confirmFairyRingTeleport = confirmFairyRingTeleportButton();
            confirmFairyRingTeleport.click();
            General.sleep(General.randomSD(2000, 75));
            General.println("[FAIRY RING NAVIGATION]: Confirming fairy ring location.");
            Timing.waitCondition(() -> {
                while (Player.getAnimation() == 3265 || Player.getAnimation() == 3266) {
                    General.println("[FAIRY RING NAVIGATION]: Player is interacting with fairy rings, sleeping until finished.");
                    General.sleep(General.randomSD(1700, 100)); }
                return Player.getAnimation() == -1;
                }, General.randomSD(1500, 100));
        }
        return !fairyRingInterfaceIsOpen();
    }

}
