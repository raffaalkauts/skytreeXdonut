package com.wiredid.skytree.listener;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.concurrent.ThreadLocalRandom;

/**
 * SkyFactory-style sneak growing: spamming sneak near saplings
 * has a chance to bonemeal every sapling within range.
 */
public class SneakGrowListener implements Listener {

    private static final int RADIUS = 3;
    private static final int VERTICAL = 2;
    /** Chance (0-1) that each sapling in range grows per sneak press. */
    private static final double CHANCE = 0.25;

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        // Only trigger when starting to sneak (one trigger per press)
        if (!event.isSneaking())
            return;

        Player player = event.getPlayer();
        Location loc = player.getLocation();
        int px = loc.getBlockX();
        int py = loc.getBlockY();
        int pz = loc.getBlockZ();
        ThreadLocalRandom rng = ThreadLocalRandom.current();

        for (int x = px - RADIUS; x <= px + RADIUS; x++) {
            for (int y = py - VERTICAL; y <= py + VERTICAL; y++) {
                for (int z = pz - RADIUS; z <= pz + RADIUS; z++) {
                    Block block = loc.getWorld().getBlockAt(x, y, z);
                    if (!Tag.SAPLINGS.isTagged(block.getType()))
                        continue;
                    if (rng.nextDouble() > CHANCE)
                        continue;

                    block.applyBoneMeal(BlockFace.UP);
                    block.getWorld().spawnParticle(Particle.HAPPY_VILLAGER,
                            block.getLocation().add(0.5, 0.5, 0.5), 4, 0.3, 0.3, 0.3, 0);
                }
            }
        }
    }
}

