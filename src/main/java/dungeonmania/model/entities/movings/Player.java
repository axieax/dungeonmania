package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.Game;
import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.buildables.Bow;
import dungeonmania.model.entities.buildables.Shield;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Player extends MovingEntity implements Character, SubjectPlayer {

    public static final int MAX_CHARACTER_HEALTH = 100;
    public static final int CHARACTER_ATTACK_DMG = 10;
    private Inventory inventory = new Inventory();

    private PlayerState state;

    boolean inBattle = false;
    List<MovingEntity> allies = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();

    public Player(Position position, int health, int attackDamage) {
        super("player", position, health, attackDamage, health * attackDamage / 5);
        this.state = new PlayerDefaultState(this);
    }

    public Player(Position position) {
        this(position, MAX_CHARACTER_HEALTH, CHARACTER_ATTACK_DMG);
    }

    /**
     * Conduct any required tasks for a player after it has moved into its new position
     */
    @Override
    public void tick(Game game) {
        List<Entity> entities = game.getEntitiesAtPosition(this.getPosition());
        for (Entity e : entities) {
            if (!(e instanceof MovingEntity)) {
                continue;
            }

            MovingEntity opponent = (MovingEntity) e;
            this.battle(game, opponent);
        }
        this.state.updateState(this);
    }

    /**
     * A battle takes place when the character and the enemy are in the same cell, within a single tick.
     * A round of a battle occurs as follows:
     *      Character Health = Character Health - ((Enemy Health * Enemy Attack Damage) / 10)
     *      Enemy Health = Enemy Health - ((Character Health * Character Attack Damage) / 5)
     * @param opponent entity the character is fighting
     */
    @Override
    public void battle(Game game, MovingEntity opponent) {
        state.battle(opponent);

        // if either character or entity is dead, remove it
        if (this.getHealth() <= 0) {
            game.removeEntity(this);
        }

        if (opponent.getHealth() <= 0) {
            game.removeEntity(opponent);
            this.inBattle = false;
        }
    }

    /**
     * Given an item, places it in the player's inventory
     * @param item that is able to placed in the player's inventory
     */
    @Override
    public void collect(Item item) {
        this.addInventoryItem(item);
    }

    @Override
    public void build(String itemId) {}

    /**
     * Given an entity id, returns the item if it exists in the player's inventory
     * @param itemId unique identifier of an entity
     * @return Item if found, else null
     */
    public Item getInventoryItem(String itemId) {
        return inventory.getItem(itemId);
    }

    public Item findInventoryItem(String className) {
        return inventory.findItem(className);
    }

    public void addInventoryItem(Item item) {
        inventory.addItem(item);
    }

    public void removeInventoryItem(String itemId) {
        inventory.removeItem(itemId);
    }

    @Override
    public List<Equipment> getEquipmentList() {
        return inventory.getEquipmentList();
    }

    public List<AttackEquipment> getAttackEquipmentList() {
        return this.getEquipmentList()
            .stream()
            .filter(equipment -> equipment instanceof AttackEquipment)
            .map(equipment -> (AttackEquipment) equipment)
            .collect(Collectors.toList());
    }

    public List<DefenceEquipment> getDefenceEquipmentList() {
        return this.getEquipmentList()
            .stream()
            .filter(equipment -> equipment instanceof AttackEquipment)
            .map(equipment -> (DefenceEquipment) equipment)
            .collect(Collectors.toList());
    }

    public boolean canCraft(String className) {
        if (className.equals(Bow.class.getSimpleName())) {
            return Bow.isBuildable(inventory);
        } else if (className.equals(Shield.class.getSimpleName())) {
            return Shield.isBuildable(inventory);
        }
        return false;
    }

    @Override
    public List<ItemResponse> getInventoryResponses() {
        // TODO Auto-generated method stub
        return null;
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
        for (AttackEquipment e : getAttackEquipmentList()) {
            damageToOpponent = e.setAttackMultiplier(damageToOpponent);
        }

        // any extra attack damage provided by allies
        for (MovingEntity a : this.getAllies()) {
            damageToOpponent += a.getDefaultBattleDamange();
        }

        return damageToOpponent;
    }

    /**
     * Given an attack damage inflicted to the player by an opponent,
     * reduce the attack by applying defensive tactics
     * @param opponentAttackDamage positive integer indicating attack amount to player
     * @return reduced opponentAttackDamage corresponding to defence amount
     */
    @Override
    public int applyDefenceToOpponentAttack(int opponentAttackDamage) {
        int newOpponentAttackDamage = opponentAttackDamage;

        // any extra defence provided by equipment
        for (DefenceEquipment e : getDefenceEquipmentList()) {
            newOpponentAttackDamage = e.setDefenceMultiplier(newOpponentAttackDamage);
        }

        return newOpponentAttackDamage;
    }

    @Override
    public void addAlly(MovingEntity ally) {
        for (MovingEntity m : allies) {
            if (m.getId() == ally.getId()) {
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
     * Upon movement, any observers are notified. If an entity blocks the player, then the
     * player does not move at all.
     * @param game
     * @param direction
     */
    @Override
    public void move(Game game, Direction direction) {
        Position newPlayerPos = this.getPosition().translateBy(direction);
        List<Entity> entities = game.getEntitiesAtPosition(newPlayerPos);

        if (entities == null) { // no entities at new position
            this.setPosition(newPlayerPos);
        } else {
            // interact with any non-moving entities and determine if player can move onto this tile
            boolean canMove = true;
            for (Entity e : entities) {
                if (e instanceof MovingEntity) {
                    continue;
                }

                e.interact(game, this);
                if (!e.isPassable()) {
                    canMove = false;
                }
            }

            // battle with any moving entities
            if (canMove) {
                this.setPosition(newPlayerPos);
                this.tick(game);
                this.notifyObservers();
            }
        }
        this.notifyObservers();
    }

<<<<<<< HEAD
    @Override
    public void interact(Game game, MovingEntityBehaviour character) {
        // TODO Auto-generated method stub
=======
    public void interact(Game game, MovingEntity character) {
>>>>>>> master

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
        for (Observer o : observers) {
            o.update(this);
        }
    }

    /**
     * Determines if the player has armour.
     * Note that the armour must be used although it is stored in the inventory.
     * @return true if player is wearing armour, otherwise false
     */
    public boolean hasArmour() {
        Item armour = findInventoryItem("Armour");
        return armour == null ? false : true;
    }

    public void reduceArmourDurability() {}

    ////////////////////////////////////////////////////////////////////////////////

    public void setState(PlayerState state) {
        this.state = state;
    }

    public PlayerState getState() {
        return state;
    }

    public boolean hasKey() {
        return false;
    }

    public Key getKey() {
        return null;
    }

    public boolean hasWeapon() {
        return this.getAttackEquipmentList().size() != 0;
    }

    public Equipment getWeapon() {
        Item weapon = inventory.findItem("Sword");
        if (weapon == null) weapon = inventory.findItem("Bow");
        return weapon instanceof AttackEquipment ? (Equipment) weapon : null;
    }

    public void craft(Game game, String className) {
        if (className.equals(Bow.class.getSimpleName())) {
            Bow.craft(inventory);
        } else if (className.equals(Shield.class.getSimpleName())) {
            Shield.craft(inventory);
        }
    }

    public Direction getDirection() {
        return null;
    }

    @Override
    public boolean isCollidable(Entity entity) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void moveTo(Position position) {
        // TODO Auto-generated method stub
        
    }
}
