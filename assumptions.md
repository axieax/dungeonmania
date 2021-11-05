* Potions are consumed once. However, the effect lasts for a certain amount of in game ticks (depending for the type of potion). In our implementation, InvincibilityPotion lasts 2 in game ticks, InvisibilityPotion lasts for 6 in game ticks and HealthPotion are just simply consumed to regain to full health.
* If a player is invisible, they cannot attack another entity. Rather they can just pass through the enemy undetected.
* Spiders are unable to run away if a player is invincible. Only Zombie and Mercenary will run away.
* Spiders can move out of the map if they spawn on the edge and their ‘circular' path causes them to go beyond the map.
* Zombies cannot move a boulder as this action is only conducted by a player.
* Durability of any weapon is not reduced if a player is invincible.
* If the player is invisible, the mercenary will not follow them.
* There's no notion of holding an item vs having it in your inventory, so if a player has multiple instances of an item e.g. 3 swords, it will attack the enemy three times.
* Portals can also teleport enemies as well.
* Entities can only use portals if a) there is a free tile in the direction of the entity's movement when passing the portal and b) if the tile is occupied by an entity, it must be a collectable item or an entity that can be passed through.
* If a bomb explodes by a switch, and there are other bombs in the vicinity of the explosion, those bombs can also explode forming a chain reaction.
* There can only be one exit in a given dungeon.
* The bomb explosion has a radius of one tile i.e. entities in adjacent tiles of the bomb are exploded.
* Boulders can only be moved past a switch or an empty tile.
* Spider starts with an initial direction of clockwise when spawned.
* Zombie/Mercenaries will not have the amour effect when equipped. Only if the player wears the armour that it halves enemy damage.
* TheOneRing has a dropped rate of 10% after a battle has ended
* The weapon (sword/bow) that is used upon interaction with an entity is whichever one that comes first in the inventory (the one that was created/ built first).
* Goals have a quantity next to them which states how many conditions remain for the condition to be satisfied, e.g. :enemies(3) means there are still three more enemies remaining.
* In Peaceful Mode and Standard Mode, players begin with 100 health. In Hard Mode, players begin with 80 health.
* When a game is saved, the name String provided should not be empty.
* Assume that there exist a corresponding portal. If there is no corresponding portal to teleport, the entity stays on the same position.