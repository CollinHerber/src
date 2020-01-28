package scripts.wastedbro.api;

import org.tribot.api2007.Banking;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import scripts.wastedbro.api.rsitem_services.GrandExchange;

import java.awt.*;

@ScriptManifest(authors = "Wastedbro", category = "Tools", name = "ProfitCalcExample")
public class MyScript extends Script implements InventoryListener, Painting {

    private int profitGained = 0;

    @Override
    public void run() {
        InventoryObserver inventoryObserver = new InventoryObserver(() -> !Banking.isBankScreenOpen());
        inventoryObserver.addListener(this);
        inventoryObserver.start();
    }

    @Override
    public void inventoryItemGained(int id, int count) {
        profitGained += GrandExchange.tryGetPrice(id).orElse(0) * count;
    }

    @Override
    public void inventoryItemLost(int id, int count) {
        profitGained -= GrandExchange.tryGetPrice(id).orElse(0) * count;
    }

    @Override
    public void onPaint(Graphics graphics) {
        graphics.drawString("Profit: " + profitGained, 10, 10);
    }
}
