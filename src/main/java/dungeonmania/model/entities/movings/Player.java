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

    private PlayerState defaultState = new DefaultState(this);
    private PlayerState invisibleState = new InvisibleState(this);
    private PlayerState invincibleState = new InvincibleState(this);
    private PlayerState armouredState = new ArmouredState(this);

    private PlayerState state;
    
    private List<Item> inventory = new ArrayList<>();

    public Player(String entityId, Position position, int health, int attackDamage) {
        super(entityId, position, health, attackDamage);

        defaultState = new DefaultState(this);
        invisibleState = new InvisibleState(this);
        invincibleState = new InvincibleState(this);
        armouredState = new ArmouredState(this);

        state = defaultState;
    }

    public Player(String entityId, Position position) {
        this(entityId, position, MAX_CHARACTER_HEALTH, CHARACTER_ATTACK_DMG);
    }

    /**
     * A battle takes place when the character and the enemy are in the same cell, within a single tick.
     * A round of a battle occurs as follows:
     *      Character Health = Character Health - ((Enemy Health * Enemy Attack Damage) / 10)
     *      Enemy Health = Enemy Health - ((Character Health * Character Attack Damage) / 5)
     * @param opponent entity the character is fighting
     */
    public void battle(Dungeon dungeon, MovingEntity opponent) {
        state.battle(opponent);

        // if either character or entity is dead, remove it
        if(this.getHealth() <= 0) {
            dungeon.hide(this);
        }

        if(opponent.getHealth() <= 0) {
            dungeon.hide(opponent);
        }
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
        // use the dungeon class to see what item player is standing on (if any)
        // and call 'interact' on that item
    }

    // TODO: state precedence can take place here?
    //       e.g. if a player is invincible potion and drinks an invisible potion
    //            it stays invincible
    public void consume(Item item) {
        
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

    /**
     * Determines if the player has armour.
     * Note that the armour must be used although it is stored in the inventory.
     * @return true if player is wearing armour, otherwise false
     */
    public boolean hasArmour() {
        Item armour = getItem("armour");
        return armour == null ? false : true;
    }

    public void reduceArmourDurability() {
        
    }

    public int getInvincibilityPotionUses() {
        return getItem("invincibility_potion").getUsesLeft();
    }

    public void reduceInvincibilityPotionUses(Item potion) {

    }

    public int getInvisibilityPotionUses() {
        return getItem("invisibility_potion").getUsesLeft();
    }

    public void reduceInvisibilityPotionUses(Item potion) {

    }

    
    ////////////////////////////////////////////////////////////////////////////////
    public void setState(PlayerState state) {
        this.state = state;
    }
    
    public PlayerState getDefaultState() {
        return defaultState;
    }
    public PlayerState getInvisibleState() {
        return invisibleState;
    }
    public PlayerState getInvincibleState() {
        return invincibleState;
    }
    public PlayerState getArmouredState() {
        return armouredState;
    }
}
