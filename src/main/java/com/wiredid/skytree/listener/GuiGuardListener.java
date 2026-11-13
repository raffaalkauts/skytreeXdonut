package com.wiredid.skytree.listener;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Entity;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

import java.util.List;
import java.util.Locale;

/**
 * Global guard: stops items from being shift-clicked, double-click collected,
 * or dragged into plugin GUIs that are not meant to accept items.
 * GUIs that legitimately accept items are whitelisted by (plain-text) title.
 */
public class GuiGuardListener implements Listener {

    /** Lower-case title fragments of GUIs that accept deposited items. */
    private static final List<String> INSERTABLE_TITLES = List.of(
            "trash disposal",
            "drop items to sell",
            "vault",
            "rod storage",
            "shipment",
            "bait bag",
            "fish storage",
            "place items");

    static boolean isPluginGui(InventoryView view) {
        Inventory top = view.getTopInventory();
        if (top.getType() != InventoryType.CHEST)
            return false;
        InventoryHolder holder = top.getHolder();
        // Real containers / entities are not ours to guard
        return !(holder instanceof BlockState) && !(holder instanceof Entity)
                && !(holder instanceof org.bukkit.block.DoubleChest);
    }

    static boolean isInsertable(InventoryView view) {
        String title = PlainTextComponentSerializer.plainText().serialize(view.title()).toLowerCase(Locale.ROOT);
        for (String fragment : INSERTABLE_TITLES) {
            if (title.contains(fragment))
                return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!isPluginGui(view) || isInsertable(view))
            return;

        boolean fromBottom = event.getClickedInventory() != null
                && event.getClickedInventory() != view.getTopInventory();

        boolean shift = event.isShiftClick() || event.getClick() == ClickType.SHIFT_LEFT
                || event.getClick() == ClickType.SHIFT_RIGHT;
        InventoryAction action = event.getAction();

        if ((fromBottom && (shift || action == InventoryAction.MOVE_TO_OTHER_INVENTORY))
                || action == InventoryAction.COLLECT_TO_CURSOR
                || event.getClick() == ClickType.DOUBLE_CLICK) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrag(InventoryDragEvent event) {
        InventoryView view = event.getView();
        if (!isPluginGui(view) || isInsertable(view))
            return;
        int topSize = view.getTopInventory().getSize();
        for (int slot : event.getRawSlots()) {
            if (slot < topSize) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
