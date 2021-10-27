package dungeonmania.model.entities.movings;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Item;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends MovingEntity {
    final static int MAX_CHARACTER_HEALTH = 100;
    final static int CHARACTER_ATTACK_DMG = 10;

    public Player(String entityId, Position position) {
        super(entityId, position, MAX_CHARACTER_HEALTH, CHARACTER_ATTACK_DMG);
    }

    public Player(String entityId, Position position, int health, int attackDamage) {
        super(entityId, position, health, attackDamage);
    }

    private List<Item> inventory = new ArrayList<>();

    /**
     * A battle takes place when the character and the enemy are in the same cell, within a single tick.
     * A round of a battle occurs as follows:
     *      Character Health = Character Health - ((Enemy Health * Enemy Attack Damage) / 10)
     *      Enemy Health = Enemy Health - ((Character Health * Character Attack Damage) / 5)
     * @param opponent entity the character is fighting
     */
    public void battle(MovingEntity opponent) {
        setHealth(
            getHealth() - opponent.getHealth() * opponent.getAttackDamage() / 10 
        );

        opponent.setHealth(
            opponent.getHealth() - getHealth() * getAttackDamage() / 5
        );
    }

    @Override
    public void move(Direction direction) {
        this.setPosition(this.getPosition().translateBy(direction));
    }

    @Override
    public void moveTo(Position position) {
        this.setPosition(position);
    }

    /**
     * Collects a Collectable entity and put it in the player's inventory if exists 
     * on the current player position
     * @param dungeon dungeon that player is in
     */
    public void collect(Dungeon dungeon) {
        // currently not possible as dungeon not implemented
    }

    /**
     * Given an entity id, returns the item if it exists in the player's inventory
     * @param entityId unique identifier of an entity
     * @return Item if found, else null
     */
    public Item getItem(String entityId) {
        for(Item i: inventory) {
            if(i.getId() == entityId) {
                return i;
            }
        }

        return null;
    }

}
