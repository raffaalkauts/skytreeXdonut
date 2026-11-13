package com.wiredid.skytree.listener;

import com.wiredid.skytree.SkytreePlugin;
import com.wiredid.skytree.util.ComponentUtil;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adds a "Back to Menu" button to every top-level GUI that was opened from the
 * Main Menu, so players can always return without retyping /menu.
 * The button is only injected into an empty/glass-pane corner slot, and never into
 * GUIs that already have their own Back button or that accept item deposits.
 */
public class MenuNavListener implements Listener {

    /** How long after a Main Menu click the next GUI open counts as "from the menu". */
    private static final long WINDOW_MS = 2500;

    private static final Map<UUID, Long> FROM_MENU = new ConcurrentHashMap<>();

    private final SkytreePlugin plugin;
    private final NamespacedKey navKey;

    public MenuNavListener(SkytreePlugin plugin) {
        this.plugin = plugin;
        this.navKey = new NamespacedKey(plugin, "nav_back_menu");
    }

    public static void markFromMenu(Player player) {
        FROM_MENU.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        FROM_MENU.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player player))
            return;
        Long marked = FROM_MENU.get(player.getUniqueId());
        if (marked == null)
            return;
        if (System.currentTimeMillis() - marked > WINDOW_MS) {
            FROM_MENU.remove(player.getUniqueId());
            return;
        }

        var view = event.getView();
        if (!GuiGuardListener.isPluginGui(view) || GuiGuardListener.isInsertable(view))
            return;

        String title = PlainTextComponentSerializer.plainText().serialize(view.title());
        String lower = title.toLowerCase();
        if (lower.contains("main menu") || lower.contains("confirm") || lower.contains("opening"))
            return;

        // Consume: only the first GUI after the menu click gets the button
        FROM_MENU.remove(player.getUniqueId());

        Inventory top = event.getInventory();
        if (top.getSize() < 18 || hasOwnBack(top))
            return;

        int[] candidates = { top.getSize() - 9, top.getSize() - 1 };
        for (int slot : candidates) {
            ItemStack existing = top.getItem(slot);
            boolean free = existing == null || existing.getType() == Material.AIR
                    || existing.getType().name().endsWith("_STAINED_GLASS_PANE");
            if (free) {
                top.setItem(slot, createBackButton());
                return;
            }
        }
    }

    private boolean hasOwnBack(Inventory inv) {
        for (ItemStack item : inv.getContents()) {
            if (item == null || !item.hasItemMeta() || item.getItemMeta().displayName() == null)
                continue;
            String name = ComponentUtil.stripColor(item.getItemMeta().displayName()).toLowerCase();
            if (name.contains("back") || name.contains("main menu"))
                return true;
        }
        return false;
    }

    private ItemStack createBackButton() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ComponentUtil.parse("\u00a7c\u00a7lBack to Menu"));
        meta.lore(List.of(ComponentUtil.parse("\u00a77Return to the main menu")));
        meta.getPersistentDataContainer().set(navKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta())
            return;
        if (!clicked.getItemMeta().getPersistentDataContainer().has(navKey, PersistentDataType.BYTE))
            return;
        if (!(event.getWhoClicked() instanceof Player player))
            return;

        event.setCancelled(true);
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (plugin.getMainMenuGUI() != null) {
                plugin.getMainMenuGUI().open(player);
            } else {
                player.closeInventory();
            }
        });
    }
}

