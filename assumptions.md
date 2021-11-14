# Assumptions
## Dungeon/Game Mechanics
* There can only be one exit in a given dungeon.
* Goals have a quantity next to them which states how many conditions remain for the condition to be satisfied, e.g. :enemies(3) means there are still three more enemies remaining.
* When a game is saved, the name String provided should not be empty.
* In Peaceful Mode and Standard Mode, players begin with 100 health. In Hard Mode, players begin with 80 health.

## Consumable Mechanics
### Potions
* `Potions` are consumed once. However, the effect lasts for a certain amount of in game ticks (depending for the type of potion). In our implementation,
  - `InvincibilityPotion` lasts for 3 in game ticks after it has been consumed (including the tick where the player consumes the potion)
  - `InvisibilityPotion` lasts for 6 in game ticks (including the tick where the player consumes the potion)
  - `HealthPotion` are just simply consumed to regain to full health
* If a player is invisible, they cannot attack another entity. Rather they can just pass through the enemy undetected.
* If the player is invisible, the mercenary will not follow them.

### SunStone
* If the player has a `SunStone` and the `Key` to open that door, `SunStone` takes priority to open the door. i.e Any `Key` are not consumed by the player if they have a `SunStone`.
* `SunStone` takes priority when building an item with `Treasure`.
* `SunStone` is consumed if it is only used as a material in building an item (retained in player inventory if used for bribing).
* `SunStone` cannot be interchanged with `Treasure` for completing the Goals

## Buildables Mechanics
* For `Shield`, `Treasure` takes priority over `Key` if the user has enough materials.
### Drop Rates
* `TheOneRing` has a drop rate of 10% after a battle has ended
* `Armour` have a drop rate of 20% after a battle has ended
## Enemy Mechanics
* `Spiders` can move out of the map if they spawn on the edge and their ‘circular' path causes them to go beyond the map.
* When a `Spider` is performing their initial movement (i.e. moving UP), if there is an entity blocking their movement, then it stays on its spawning position since it cannot perform its initial movement position.
* `Zombie` cannot move a boulder as this action is only conducted by a player.
* `Zombies` will only randomly pick a free tile to move to.
* `Spider` starts with an initial direction of clockwise when spawned.
* `Mercenary` can only move twice if mercenary is aiming to attack the player.
* `Mercenary` spawn at the initial player location after 40 ticks and `Assassin` has a 30% chance of spawning instead - but there must be at least one enemy in the dungeon, and the player cannot be in its initial spawn location
* Apart from the base attack damage and health, as well as the mind control feature; `Assassin` have the same functionality as `Mercenary` (e.g. same armour drop rate).
* There is priority to mind control an `Assassin`, before attempting to bribe it.
* When a `Hydra` regrows its head, and as a result its health increases, the total health can exceed the original maximum health.
* The Hydra entity spawns at the entry location - similar to the mercenary.
## Entity Mechanics
### Portal
* `Portals` teleport all moving entities (except ZombieToast). Moving entities that teleport will still follow their original moving pattern. i.e. A `Spider` will resume moving in a circular motion after it has been teleported.
* Entities can only use `Portals` if a) there is a free tile in the direction of the entity's movement when passing the `Portal` and b) if the tile is occupied by an entity, it must be a collectable item or an entity that can be passed through. Otherwise, it will stay on the same position for the tick.
* Assume that there exist a corresponding `Portal`. If there is no corresponding `Portal` to teleport, the entity stays on the same position.

### FloorSwitch, Boulder, Bomb
* If a `Bomb` explodes by a switch, and there are other `Bombs` in the vicinity of the explosion, those `Bombs` can also explode forming a chain reaction.
* The `Bomb` explosion has a radius of one tile i.e. entities in adjacent tiles of the `Bomb` are exploded.
* `Boulders` can only be moved past a switch or an empty tile.
* `Bombs` can only explode once it has been placed by the user - meaning that `Bombs` spawned next to a switch cannot explode initially.
* Assume that `Bomb` explosions will not destroy portals (but will destroy floor switches).

## Weapon Mechanics
* Durability of any weapon is not reduced if a player is invincible.
* There's no notion of holding an item vs having it in your inventory, so if a player has multiple instances of an item e.g. 3 `Swords`, it will attack the enemy three times.
