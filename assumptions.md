# Assumptions
## Dungeon/Game Mechanics
* There can only be one exit in a given dungeon.
* Goals have a quantity next to them which states how many conditions remain for the condition to be satisfied, e.g. :enemies(3) means there are still three more enemies remaining.
* When a game is saved, the name String provided should not be empty.
* In Peaceful Mode and Standard Mode, players begin with 100 health. In Hard Mode, players begin with 80 health.

## Consumable Mechanics
### Potions
* Potions are consumed once. However, the effect lasts for a certain amount of in game ticks (depending for the type of potion). In our implementation,
  - InvincibilityPotion lasts 3 in game ticks (including the tick where the player consumes the potion)
  - InvisibilityPotion lasts for 6 in game ticks
  - HealthPotion are just simply consumed to regain to full health
* If a player is invisible, they cannot attack another entity. Rather they can just pass through the enemy undetected.
* All enemies will run away if a player is invincible. For spiders, if a player is no longer invincible, it will reset it's circular movement on its last position.
* If the player is invisible, the mercenary will not follow them.

### SunStone
* If the player has a `SunStone` and the `Key` to open that door, `SunStone` takes priority to open the door. i.e Any `Key` are not consumed by the player if they have a `SunStone`.
* `SunStone` takes priority when building an item with `Treasure`.
* `SunStone` is consumed if it is only used as a material in building an item (retained in player inventory if used for bribing).
* `SunStone` cannot be interchanged with `Treasure` for completing the Goals

## Buildables Mechanics
* For `Shield`, `Treasure` takes priority over `Key` if the user have enough materials.
### Drop Rates
* `TheOneRing` has a drop rate of 10% after a battle has ended
* `Armour` have a drop rate of 20% after a battle has ended
## Enemy Mechanics
* Spiders can move out of the map if they spawn on the edge and their ‘circular' path causes them to go beyond the map.
* Zombies cannot move a boulder as this action is only conducted by a player.
* Zombies will only randomly pick a free tile to move to.
* Spider starts with an initial direction of clockwise when spawned.
* Zombie/Mercenaries will the amour effect when equipped. It will halve damage inflicted by the player.
* Mercenaries can only move twice if mercenary is aiming to attack the player.
## Entity Mechanics
### Portal
* Portals teleport all moving entities (except ZombieToast). Moving entities that teleport will still follow their original moving pattern. i.e. A spider will resume moving in a circular motion after it has been teleported.
* Entities can only use portals if a) there is a free tile in the direction of the entity's movement when passing the portal and b) if the tile is occupied by an entity, it must be a collectable item or an entity that can be passed through. Otherwise, it will stay on the same position for the tick.
* Assume that there exist a corresponding portal. If there is no corresponding portal to teleport, the entity stays on the same position.

### FloorSwitch, Boulder, Bomb
* If a bomb explodes by a switch, and there are other bombs in the vicinity of the explosion, those bombs can also explode forming a chain reaction.
* The bomb explosion has a radius of one tile i.e. entities in adjacent tiles of the bomb are exploded.
* Boulders can only be moved past a switch or an empty tile.
* Bombs can only explode once it has been placed by the user - meaning that bombs spawned next to a switch cannot explode initially.
* Assume that bomb explosions will also destroy floor switches.

## Weapon Mechanics
* Durability of any weapon is not reduced if a player is invincible.
* There's no notion of holding an item vs having it in your inventory, so if a player has multiple instances of an item e.g. 3 swords, it will attack the enemy three times.
