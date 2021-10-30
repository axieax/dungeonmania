package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.potion.Potion;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.List;

public class Player extends MovingEntity implements Character, SubjectPlayer {
    final static int MAX_CHARACTER_HEALTH = 100;
    final static int CHARACTER_ATTACK_DMG = 10;

    private PlayerState defaultState;
    private PlayerState invisibleState;
    private PlayerState invincibleState;
    private PlayerState state;

    private List<Item> inventory = new ArrayList<>();
    boolean inBattle = false;
    List<MovingEntity> allies = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();

    public Player(String entityId, Position position, int health, int attackDamage) {
        super(entityId, position, health, attackDamage, health *  attackDamage / 5);

        defaultState = new DefaultState(this);
        invisibleState = new InvisibleState(this);
        invincibleState = new InvincibleState(this);

        state = defaultState;
    }

    public Player(String entityId, Position position) {
        this(entityId, position, MAX_CHARACTER_HEALTH, CHARACTER_ATTACK_DMG);
    }

    /**
     * Conduct any required tasks for a player after it has moved into its new position
     */
    @Override
    public void tick(Dungeon dungeon) {
        List<Entity> entities = dungeon.getEntitiesAtPosition(this.getPosition());
        for(Entity e: entities) {
            if(!(e instanceof MovingEntity)) {
               continue;
            }

            MovingEntity opponent = (MovingEntity) e;
            this.battle(dungeon, opponent);
        }
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
     * Given an item, places it in the player's inventory
     * @param item that is able to placed in the player's inventory
     */
    @Override
    public void collect(Item item) {
        this.inventory.add(item);
    }

    @Override
    public void build(String itemId) {

    }

    /**
     * Apply any effects of any consumable item 
     * @param item that is consumable
     */
    @Override
    public void consume(Item item) {
        if(item instanceof Consumable) {
            Consumable consumableItem = (Consumable) item;
            consumableItem.consume(this);
        }
    }

    /**
     * Given an entity id, returns the item if it exists in the player's inventory
     * @param itemId unique identifier of an entity
     * @return Item if found, else null
     */
    public Item getItem(String entityId) {
        return inventory
                .stream()
                .filter(i -> i.getId() == entityId)
                .findFirst()
                .orElse(null);
    }

    public Equipment getWeapon() {
        return inventory
                .stream()
                .filter(i -> i instanceof Equipment)
                .map(i -> (Equipment) i)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Equipment> getEquipment() {
        ArrayList<Equipment> equipment = new ArrayList<>();
        
        for(Item i: this.inventory) {
            if(i instanceof Equipment) {
                equipment.add((Equipment) i);
            }
        }

        return equipment;
    }

    @Override
    public List<AttackEquipment> getAttackEquipment() {
        ArrayList<AttackEquipment> attackEquip = new AttackEquipment();

        for(Equipment e: getEquipment()) {
            if(e instanceof AttackEquipment) {
                attackEquip.add(e);
            }
        }
        return attackEquip;
    }

    @Override
    public List<DefenceEquipment> getDefenceEquipment() {
        ArrayList<DefenceEquipment> defenceEquip = new DefenceEquipment();

        for(Equipment e: getEquipment()) {
            if(e instanceof DefenceEquipment) {
                defenceEquip.add(e);
            }
        }
        return defenceEquip;
    }

      /**
     * Returns the total attack damage a player is able to inflict upon an opponent .
     * This includes any attack damage provided by equipment e.g. sword
     * @return a positive integer indicating the amount of attack
     */
    @Override
    public int getCurrentAttackDamage() {
        // Normal damange inflicted by player
        int damageToOpponent = this.getDefaultBattleDamange();

        // any extra attack damage provided by equipment
        for(AttackEquipment e: getAttackEquipment()) {
            damageToOpponent = e.setAttackMultiplier(damageToOpponent);
        }

        // any extra attack damage provided by allies
        for(MovingEntity a: this.getAllies()) {
            damageToOpponent += a.getDefaultBattleDamange();
        }
    }

    /**
     * Given an attack damage inflicted to the player by an opponent,
     * reduce the attack by applying defensive tactics
     * @param opponentAttackDamage positive integer indicating attack amount to player
     * @return reduced opponentAttackDamage corressponding to defence amount
     */
    @Override
    public int applyDefenceToOpponentAttack(int opponentAttackDamage) {
        int newOpponentAttackDamage = opponentAttackDamage;
        
        // any extra defence provided by equipment
        for(DefenceEquipment e: getDefenceEquipment()) {
            newOpponentAttackDamage = e.setDefenceMultiplier(newOpponentAttackDamage);
        }

        return newOpponentAttackDamage;
    }
    
    @Override
    public void addAlly(MovingEntity ally) {
        for(MovingEntity m: allies) {
            if(m.getId() == ally.getId()) {
                // entity is already ally
                return;
            }
        }

        allies.add(ally);
    }
    
    @Override
    public List<MovingEntity> getAllies() {
        return this.allies;
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
        List<Entity> entities = dungeon.getEntitiesAtPosition(newPlayerPos);

        if(entities == null) { // no entities at new position
            this.setPosition(newPlayerPos);
            this.notifyObservers();
            return;
        } else { 
            // interact with any non-moving entities and determine if player can move onto this tile
            boolean canMove = true;
            for(Entity e: entities) {
                if(e instanceof MovingEntity) {
                    continue;
                }

                e.interact(dungeon, this);
                if(!e.isPassable()) {
                    canMove = false;
                }
            }
    
            // battle with any moving entities
            if(canMove) {
                this.setPosition(newPlayerPos);
                this.tick(dungeon);
                this.notifyObservers();
            }
        }

    }

    @Override
    public void interact(Dungeon dungeon, MovingEntityBehaviour character) {
        // TODO Auto-generated method stub
        
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
    public PlayerState getState() {
        return state;
    }
    
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
        return this.getWeapon() != null;
    }
}
