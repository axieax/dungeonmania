package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.List;

public class Player extends MovingEntity implements Character, SubjectCharacer {
    final static int MAX_CHARACTER_HEALTH = 100;
    final static int CHARACTER_ATTACK_DMG = 10;

    private PlayerState defaultState = new DefaultState(this);
    private PlayerState invisibleState = new InvisibleState(this);
    private PlayerState invincibleState = new InvincibleState(this);
    private PlayerState armouredState = new DefensiveState(this);
    private PlayerState state;

    private List<Item> inventory = new ArrayList<>();
    boolean inBattle = false;
    private List<Observer> observers = new ArrayList<>();

    public Player(String entityId, Position position, int health, int attackDamage) {
        super(entityId, position, health, attackDamage);

        defaultState = new DefaultState(this);
        invisibleState = new InvisibleState(this);
        invincibleState = new InvincibleState(this);
        armouredState = new DefensiveState(this);

        state = defaultState;
    }

    public Player(String entityId, Position position) {
        this(entityId, position, MAX_CHARACTER_HEALTH, CHARACTER_ATTACK_DMG);
    }

    @Override
    public void tick(Dungeon dungeon) {
        
    }

    /**
     * A battle takes place when the character and the enemy are in the same cell, within a single tick.
     * A round of a battle occurs as follows:
     *      Character Health = Character Health - ((Enemy Health * Enemy Attack Damage) / 10)
     *      Enemy Health = Enemy Health - ((Character Health * Character Attack Damage) / 5)
     * @param opponent entity the character is fighting
     */
    @Override
    public void battle(Dungeon dungeon, MovingEntity opponent) {
        
        state.battle(opponent);

        // if either character or entity is dead, remove it
        if(this.getHealth() <= 0) {
            dungeon.hide(this);
        }

        if(opponent.getHealth() <= 0) {
            dungeon.hide(opponent);
            this.inBattle = false;
        }
    }

    /**
     * Collects a Collectable entity and put it in the player's inventory if exists 
     * on the current player position
     */
    @Override
    public void collect() {
        // currently not possible as dungeon not implemented
        // use the dungeon class to see what item player is standing on (if any)
        // and call 'interact' on that item
    }

    @Override
    public void build(String itemId) {

    }

    // TODO: state precedence can take place here?
    //       e.g. if a player is invincible potion and drinks an invisible potion
    //            it stays invincible
    @Override
    public void consume(Item item) {
        // allow each potion to change the state of a player? through item.consume(this)?
        // (pass player object to item)
        // that way we don't need if/else statements in this function
        // this way we can also add precedence for states (e.g. we can look at the current
        // state in the invisbility consume function and decide if it needs to be changed
        // - i.e. don't change if player in invincible state).
    }

    /**
     * Given an entity id, returns the item if it exists in the player's inventory
     * @param itemId unique identifier of an entity
     * @return Item if found, else null
     */
    @Override
    public Item getInventoryItem(String itemId) {
        for(Item i: inventory) {
            if(i.getId() == entityId) {
                return i;
            }
        }

        return null;
    }
    
    /**
     * Interacts with any entity that is on the tile the character is about to move into.
     * If it cannot move onto that tile, it does not move at all.
     * @param dungeon
     * @param direction
     */
    @Override
    public void move(Dungeon dungeon, Direction direction) {
        Position newPlayerPos = this.getPosition().translateBy(direction);
        
        // determine entity that exists in tile that the player will (possibly) move into
        List<Entity> entities = dungeon.getEntitiesAtPosition();
        if(entities == null) { 
            this.setPosition(newPlayerPos);
            return;
        }

        boolean canMove = true;
        for(Entity e: entities) {
            e.interact(dungeon, this);
            if(!e.isPassable()) {
                canMove = false;
            }
        }

        if(canMove) {
            this.setPosition(newPlayerPos);
        }
    }


    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer o: observers) {
            o.update(this);
        }
    }

    /**
     * Determines if the player has armour.
     * Note that the armour must be used although it is stored in the inventory.
     * @return true if player is wearing armour, otherwise false
     */
    public boolean hasArmour() {
        Item armour = getInventoryItem("armour");
        return armour == null ? false : true;
    }

    public void reduceArmourDurability() {
        
    }

    public int getInvincibilityPotionUses() {
        return getInventoryItem("invincibility_potion").getUsesLeft();
    }

    public void reduceInvincibilityPotionUses(Item potion) {

    }

    public int getInvisibilityPotionUses() {
        return getInventoryItem("invisibility_potion").getUsesLeft();
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

    public boolean hasKey() {
        return false;
    }

    public Direction getDirection() {
        return null;
    }

    public Key getKey() {
        return null;
    }

    public void consume(String itemId) {
        return;
    }

    public boolean hasWeapon() {
        return false;
    }

    @Override
    public void interact(Dungeon dungeon, MovingEntityBehaviour character) {
        // TODO Auto-generated method stub
        
    }
}
