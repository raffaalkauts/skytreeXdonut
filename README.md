# Skytree

**Version:** 4.0.0  
**Target:** PaperMC 26.3 / Java 25  
**Type:** SkyFactory-like SkyBlock plugin (150+ items, achievements, quests, energy system)

## Overview

Skytree is a self-contained Paper plugin that generates a void SkyBlock world and provides a
progression-oriented SkyFactory experience: islands, custom items, sieves, barrels, crucibles,
economy, shops, auctions, banking, fishing, minions, crates, quests, and more. No external
plugins are required for core gameplay.

## Requirements

- Java 25 (Temurin or equivalent)
- Paper 26.3 server
- Maven 3.9+ (for building from source)

## Building

```bash
mvn clean package
```

The compiled jar is written to `target/Skytree-v4.0.0.jar`.

## Installation

1. Build or download the jar.
2. Place `Skytree-v4.0.0.jar` into the server `plugins/` folder.
3. Start the server once to generate `plugins/Skytree/` and default configs.
4. Join and run `/is create` (or `/is c`) to create your island.

## Quick Start

1. `/is create` - create your island.
2. Break leaves by hand to obtain saplings and basic resources.
3. Use `/shop` to buy materials or `/sg` to sell items.
4. Progress through crooks, hammers, meshes, sieves, barrels, and crucibles.

## Core Systems

- Void world generation with SkyFactory 3 style island template
- One island per player with protection and member permissions
- USDT economy with `/bal`, `/btc`, `/money`, `/pay`
- 150+ custom items: crooks, hammers, meshes, pebbles, dusts, ore pieces, silkworms, machines
- Iridium-style category shop (`/shop`)
- Sieve, barrel, and crucible mechanics
- Fishing, enchanting, crates, minions, banking, auctions, bounties
- Quests, achievements, daily rewards, leaderboards
- Shards, tags, kits, warps, trust levels

## Main Commands

| Command | Description |
| --- | --- |
| `/is create` (`c`) | Create an island |
| `/is home` (`h`) | Teleport home |
| `/is delete` (`del`) | Delete island |
| `/is stats` (`s`) | View island stats |
| `/shop` | Open global shop |
| `/bal` | Check balance |
| `/pay <player> <amount>` | Send money |
| `/sg` / `/sell` | Open sell GUI |
| `/vault` | Open personal storage |
| `/menu` | Open main menu |
| `/guide` | Open in-game guide |

See `src/main/resources/plugin.yml` for the full command and permission list.

## Permissions

- `skytree.use` - basic usage (default: true)
- `skytree.admin` - admin commands (default: op)
- `skytree.bypass` - bypass protections (default: op)
- `skytree.give` - give custom items (default: op)

## Configuration

All configuration and data files live under `plugins/Skytree/` after first run.
Key files: `config.yml`, `items.yml`, `shop.yml`, `quests.yml`, `worth.yml`.
Edit these to customize world name, island spacing, starting balance, machine rates, etc.

## Development

- Source of truth is `src/main/java` and `src/main/resources`.
- Plugin bootstrap: `com.wiredid.skytree.SkytreePlugin`.
- Architecture notes: `architecture.md`.
- Product requirements: `prd.md`.

## Status

Active development. Some advanced systems (energy networks, island upgrades, biome changing)
are still being expanded. Core SkyFactory progression is functional.