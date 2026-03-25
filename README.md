# Orbital Wolf Cannon (Fabric)

A Minecraft Fabric mod that adds the **Orbital Wolf Rod**. Using the rod summons a circular strike pack of wolves around the player.

## Features

- Spawns exactly 48 wolves in an even circle (radius 6)
- Wolves spawn at player Y-level (ground-level summon, not sky drop)
- Wolves are tamed to the activating player (multiplayer-safe ownership)
- Wolves are set active (not sitting), equipped with wolf armor, and buffed with Strength II for 5 minutes
- Nearby hostile mobs are auto-targeted
- Activation particle ring + spawn bursts + powerful summon sound
- 60-second item cooldown with action bar warning when unavailable
- Summoned wolves auto-despawn after 5 minutes
- Tooltip, recipe book unlock, and crafting recipe included
- Rod appears in creative inventory search and shows enchanted glint

## Requirements

- Java 21
- Minecraft 1.21.11
- Fabric Loader + Fabric API

## Quick Start

```powershell
.\gradlew.bat build
.\gradlew.bat runServer
```

Then give yourself the rod:

```mcfunction
give @p orbital_wolf_cannon:orbital_wolf_rod
```

Recipe/advancement verification commands:

```mcfunction
/recipe give @p orbital_wolf_cannon:orbital_wolf_rod
/advancement grant @p only orbital_wolf_cannon:orbital_dog
```

## Test Harness

A small JUnit test validates circle generation math:

- `src/test/java/com/slimeeystudios/orbitalwolfcannon/util/CirclePositionUtilTest.java`

Run tests:

```powershell
.\gradlew.bat test
```

## Project Structure

- `src/main/java/com/slimeeystudios/orbitalwolfcannon/` - mod code
- `src/main/resources/assets/orbital_wolf_cannon/` - lang + model
- `src/main/resources/data/orbital_wolf_cannon/` - recipes

