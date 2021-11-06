package dungeonmania.model.entities.movings;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.buildables.Buildable;
import dungeonmania.model.entities.collectables.Bomb;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.equipment.Sword;
import dungeonmania.model.entities.collectables.potion.Potion;
import dungeonmania.model.entities.statics.Consumable;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class Player extends MovingEntity implements SubjectPlayer {

    public static final int MAX_CHARACTER_HEALTH = 100;
    public static final int CHARACTER_ATTACK_DMG = 10;

    private PlayerState state;
    private boolean inBattle;
    private Inventory inventory = new Inventory();
    private List<MovingEntity> allies = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();

    public Player(Position position) {
        super("player", position, MAX_CHARACTER_HEALTH, CHARACTER_ATTACK_DMG, false);
        this.state = new PlayerDefaultState(this);
        this.inBattle = false;
    }

    /********************************
     *  Getters and Setters         *
     ********************************/

    /**
     * @return PlayerState
     */
    public PlayerState getState() {
        return state;
    }

    /**
     * Sets the player state.
     * @param state
     */
    public void setState(PlayerState state) {
        this.state = state;
    }

    /**
     * @return boolean
     */
    public boolean getInBattle() {
        return inBattle;
    }

    /**
     * Sets the player battle status.
     * @param inBattle
     */
    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }

    /**
     * Get a list of all allies that the player has.
     *
     * @return List<MovingEntity>
     */
    public List<MovingEntity> getAllies() {
        return this.allies;
    }

    /********************************
     *  Ally Methods                *
     ********************************/

    /**
     * Add an ally to the player.
     *
     * @param ally
     */
    public void addAlly(MovingEntity ally) {
        for (MovingEntity m : allies) {
            // Entity is already ally
            if (m.getId().equals(ally.getId())) return;
        }
        allies.add(ally);
        ally.setEnemy(false);
    }

    /********************************
     *  Inventory Methods           *
     ********************************/

    /**
     * Given an entity id, returns the item if it exists in the player's inventory
     *
     * @param itemId unique identifier of an entity
     * @return Item if found, else null
     */
    public Item getInventoryItem(String itemId) {
        return inventory.getItem(itemId);
    }

    /**
     * Finds and return the item that has the prefix from the inventory
     *
     * @param prefix
     * @return Item
     */
    public Item findInventoryItem(String prefix) {
        return inventory.findItem(prefix);
    }

    /**
     * Add an Item object to the inventory.
     *
     * @param item
     */
    public void addInventoryItem(Item item) {
        inventory.addItem(item);
    }

    /**
     * Remove an item with the itemId from the inventory
     *
     * @param itemId
     */
    public void removeInventoryItem(String itemId) {
        inventory.removeItem(itemId);
    }

    /**
     * Get a list of all equipments from the inventory.
     *
     * @return List<Equipment>
     */
    public List<Equipment> getEquipmentList() {
        return inventory.getEquipmentList();
    }

    /**
     * Get a list of all attackable equipments from the inventory.
     *
     * @return List<Equipment>
     */
    public List<Equipment> getAttackEquipmentList() {
        return this.getEquipmentList()
            .stream()
            .filter(equipment -> equipment instanceof AttackEquipment)
            .collect(Collectors.toList());
    }

    /**
     * Get a list of all defensable eqipments from the inventory.
     *
     * @return List<Equipment>
     */
    public List<Equipment> getDefenceEquipmentList() {
        return this.getEquipmentList()
            .stream()
            .filter(equipment -> equipment instanceof DefenceEquipment)
            .collect(Collectors.toList());
    }

    /**
     * @return boolean
     */
    public boolean hasKey() {
        return this.getKey() != null;
    }

    /**
     * @return Key
     */
    public Key getKey() {
        return (Key) inventory.findItem("key");
    }

    /**
     * @return boolean
     */
    public boolean hasWeapon() {
        return !this.getAttackEquipmentList().isEmpty();
    }

    /**
     * @return Equipment
     */
    public Equipment getWeapon() {
        Item weapon = inventory.findItem("sword");
        if (weapon == null) weapon = inventory.findItem("bow");
        return weapon instanceof AttackEquipment ? (Equipment) weapon : null;
    }

    /**
     * Checks if the inventory has the specified quantity of the item.
     *
     * @param prefix
     * @param quantity
     * @return
     */
    public boolean hasItemQuantity(String prefix, int quantity) {
        return inventory.hasItemQuantity(prefix, quantity);
    }

    /**
     * Returns a list of ItemResponse objects that contains information on the
     * player's inventory.
     *
     * @return List<ItemResponse>
     */
    public List<ItemResponse> getInventoryResponses() {
        return inventory.getInventoryResponses();
    }

    /********************************
     *  Action Methods              *
     ********************************/

    /**
     * Conduct any required tasks for a player after it has moved into its new position
     *
     * @param game
     */
    @Override
    public void tick(Game game) {
        List<Entity> entities = game.getEntities(this.getPosition());
        for (Entity e : entities) {
            if (e instanceof MovingEntity) {
                MovingEntity opponent = (MovingEntity) e;
                if (opponent.isEnemy()) this.battle(game, opponent);
            }
        }
        this.state.updateState(this);
    }

    /**
     * Interacts with any entity that is on the tile the character is about to move into.
     * Upon movement, any observers are notified. If an entity blocks the player, then the
     * player does not move at all.
     * @param game
     * @param direction
     */
    public void move(Game game, Direction direction) {
        this.move(game, direction, "");
    }

    /**
     * Interacts with any entity that is on the tile the character is about to move into.
     * Upon movement, any observers are notified. If an entity blocks the player, then the
     * player does not move at all.
     * @param game
     * @param direction
     */
    public void move(Game game, Direction direction, String itemId)
        throws IllegalArgumentException {
        if (itemId != null && itemId.length() > 0) {
            // check if itemId is not it player inventory
            if (game.getEntity(itemId) != null) throw new InvalidActionException(
                "At Player move method - itemUsed is not in the player's inventory"
            );
            // check if itemUsed can be consumed
            Item item = getInventoryItem(itemId);
            if (item != null && !(item instanceof Bomb || item instanceof Potion)) throw new IllegalArgumentException(
                "At Player move method - itemUsed is not a bomb, health_potion, invincibility_potion, or an invisibility_potion, or null"
            );

            // consume item
            if (item != null && item instanceof Consumable) ((Consumable) item).consume(game, this);
        }

        this.setDirection(direction);

        // interact with all entities in that direction
        List<Entity> entities = game.getEntities(this.getPosition().translateBy(direction));
        entities.forEach(
            entity -> {
                // cannot interact with moving entities when moving
                if (!(entity instanceof MovingEntity)) entity.interact(game, this);
            }
        );

        List<Entity> updatedEntities = game.getEntities(this.getPosition().translateBy(direction));
        boolean canMove = updatedEntities.stream().allMatch(e -> !this.collision(e));

        if (canMove) {
            this.setPosition(this.getPosition().translateBy(direction));
            this.tick(game);
            this.notifyObservers();
        }
    }

    /**
     * A battle takes place when the character and the enemy are in the same cell, within a single tick.
     * A round of a battle occurs as follows:
     *      Character Health = Character Health - ((Enemy Health * Enemy Attack Damage) / 10)
     *      Enemy Health = Enemy Health - ((Character Health * Character Attack Damage) / 5)
     *
     * @param opponent entity the character is fighting
     */
    public void battle(Game game, MovingEntity opponent) {
        state.battle(game, opponent);

        // Notify the observers that the player is in battle
        this.setInBattle(true);
        this.notifyObservers();

        if (this.getHealth() <= 0) {
            Item item = this.findInventoryItem("one_ring");
            if (item != null && (item instanceof Consumable)) {
                // Use one ring if it is in inventory
                ((Consumable) item).consume(game, this);
            } else {
                // Entity is dead, remove it
                game.removeEntity(this);
            }
        }

        // If either entity is dead, remove it
        if (opponent.getHealth() <= 0) {
            game.removeEntity(opponent);
        }

        // Notify the observers that the player is no longer in battle
        this.setInBattle(false);
    }

    /**
     * Given an item, places it in the player's inventory
     *
     * @param item that is able to placed in the player's inventory
     */
    public void collect(Item item) {
        this.addInventoryItem(item);
    }

    /**
     * Given a buildableItem, builds it if it is craftable
     *
     * @param equipment
     */
    public void craft(Buildable equipment) {
        if (equipment.isBuildable(inventory)) equipment.craft(inventory);
    }

    /**
     * Check if the equipment is buildable
     *
     * @param equipment
     * @return boolean
     */
    public boolean checkBuildable(Buildable item) {
        return item.isBuildable(this.inventory);
    }

    /********************************
     *  Battling Methods            *
     ********************************/

    /**
     * Returns the total attack damage a player is able to inflict upon an opponent.
     * This includes any attack damage provided by equipment e.g. sword
     *
     * @return int a positive integer indicating the amount of attack
     */
    public int getTotalAttackDamage(MovingEntity opponent) {
        // Normal damage inflicted by player
        int damageToOpponent = this.getBaseAttackDamage();

        // Any extra attack damage provided by equipment
        for (Equipment e : getAttackEquipmentList()) {
            if (e instanceof AttackEquipment) damageToOpponent +=
                e.getMultiplier() * ((AttackEquipment) e).getAttackDamage(opponent);
        }

        // Any extra attack damage provided by allies
        for (MovingEntity a : this.getAllies()) {
            damageToOpponent += a.getBaseAttackDamage();
        }

        return damageToOpponent;
    }

    /**
     * Given an attack damage inflicted to the player by an opponent,
     * reduce the attack by applying defensive multiplier.
     *
     * @param opponentAttackDamage positive integer indicating attack amount to player
     * @return int reduced opponentAttackDamage corresponding to defence amount
     */
    public int applyDefenceToOpponentAttack(int opponentAttackDamage) {
        int finalAttackDamage = opponentAttackDamage;
        for (Equipment e : this.getDefenceEquipmentList()) {
            finalAttackDamage = (int) (finalAttackDamage * e.getMultiplier());
        }
        return finalAttackDamage;
    }

    /********************************
     *  Observer/Subject Methods    *
     ********************************/

    /**
     * Attach an observer to the player.
     *
     * @param observer
     */
    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put(state.getClass().getSimpleName(), state.ticksLeft());
        info.put("inventory", inventory.toJSON());
        return info;
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers of the player.
     */
    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }
}
