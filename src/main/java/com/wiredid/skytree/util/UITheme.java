package com.wiredid.skytree.util;

import org.bukkit.Material;

/**
 * Central UI palette so every GUI shares one consistent look.
 * Rule of thumb: one accent per menu, neutral gray text, no rainbow buttons.
 */
public final class UITheme {

    private UITheme() {
    }

    // Text colors (legacy codes)
    public static final String TITLE = "§f§l";
    public static final String TEXT = "§7";
    public static final String MUTED = "§8";
    public static final String VALUE = "§f";
    public static final String SUCCESS = "§a";
    public static final String ERROR = "§c";
    public static final String ACTION = "§e";

    // Category accents
    public static final String ISLAND = "§a";
    public static final String ECONOMY = "§6";
    public static final String FEATURE = "§b";
    public static final String SYSTEM = "§7";
    public static final String DANGER = "§c";

    // Standard panes
    public static final Material BACKGROUND = Material.BLACK_STAINED_GLASS_PANE;
    public static final Material BORDER = Material.GRAY_STAINED_GLASS_PANE;

    /** Button name: accent color + bold label. */
    public static String button(String accent, String label) {
        return accent + "§l" + label;
    }

    /** Standard call-to-action lore line. */
    public static String clickLine() {
        return MUTED + "» " + ACTION + "Click to open";
    }
}

