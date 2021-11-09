package dungeonmania.model.entities.movings.player;

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
import dungeonmania.model.entities.collectables.equipment.Anduril;
import dungeonmania.model.entities.collectables.potion.Potion;
import dungeonmania.model.entities.movings.BribableEnemy;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Observer;
import dungeonmania.model.entities.movings.SubjectPlayer;
import dungeonmania.model.entities.statics.Consumable;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class Player extends MovingEntity implements SubjectPlayer {

    public static final int MAX_CHARACTER_HEALTH = 100;
    public static final int CHARACTER_ATTACK_DMG = 10;

    private PlayerState state;
    private boolean inBattle;
    private MovingEntity currentBattleOpponent;
    private Inventory inventory = new Inventory();
    private List<BribableEnemy> allies = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();

    public Player(Position position) {
        super("player", position, MAX_CHARACTER_HEALTH, CHARACTER_ATTACK_DMG);
        this.state = new PlayerDefaultState(this);
        this.inBattle = false;
        this.currentBattleOpponent = null;
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
     * @return true if in battle, false otherwise
     */
    public MovingEntity getCurrentBattleOpponent() {
        return currentBattleOpponent;
    }
    
    /**
     * Sets the current opponent that the player is fighting against.
     * @return
     */
    public void setCurrentBattleOpponent(MovingEntity opponent) {
        this.currentBattleOpponent = opponent;
    }

    /**
     * Get a list of all allies that the player has.
     *
     * @return List<Enemy>
     */
    public List<BribableEnemy> getAllies() {
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
    public void addAlly(BribableEnemy ally) {
        for (BribableEnemy m : allies) {
            // Entity is already ally
            if (m.getId().equals(ally.getId())) return;
        }
        allies.add(ally);
        ally.setBribed(true);
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
     * @return List<AttackEquipment>
     */
    public List<AttackEquipment> getAttackEquipmentList() {
        return this.getEquipmentList()
            .stream()
            .filter(equipment -> equipment instanceof AttackEquipment)
            .map(equipment -> (AttackEquipment) equipment)
            .collect(Collectors.toList());
    }

    /**
     * Get a list of all defensable eqipments from the inventory.
     *
     * @return List<DefenceEquipment>
     */
    public List<DefenceEquipment> getDefenceEquipmentList() {
        return this.getEquipmentList()
            .stream()
            .filter(equipment -> equipment instanceof DefenceEquipment)
            .map(equipment -> (DefenceEquipment) equipment)
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

    @Override
    public AnimationQueue getAnimation() {
        double health = (double) getHealth() / MAX_CHARACTER_HEALTH;
        return new AnimationQueue(
            "PostTick",
            getId(),
            Arrays.asList("healthbar set " + health, "healthbar tint 0xff0000, over 0.5s"),
            false,
            10
        );
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
            if (e instanceof Enemy) {
                Enemy opponent = (Enemy) e;

                // Ignore if the opponent is an ally
                if (opponent instanceof BribableEnemy) {
                    BribableEnemy bribableEnemy = (BribableEnemy) opponent;
                    if (bribableEnemy.isBribed()) {
                        continue;
                    }
                }

                this.battle(game, opponent);
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
        throws IllegalArgumentException, InvalidActionException {
        if (itemId != null && itemId.length() > 0) {
            Item item = getInventoryItem(itemId);

            // Item is not null, and it's not a bomb or any potion
            if (item != null && !(item instanceof Bomb || item instanceof Potion)) {
                throw new IllegalArgumentException("Not a valid item to use");
            }

            // Item is in the game but not in the player's inventory, or doesn't exist in either
            if (item == null) {
                throw new InvalidActionException("Item not found in player inventory");
            }

            // Consume item
            if (item instanceof Consumable) {
                ((Consumable) item).consume(game, this);
            }
        }

        this.setDirection(direction);

        // Interact with all entities in that direction
        List<Entity> entities = game.getEntities(this.getPosition().translateBy(direction));
        entities.forEach(entity -> {
            // Cannot interact with moving entities when moving
            if (!(entity instanceof MovingEntity)) entity.interact(game, this);
        });

        // Gets the updated entities after the interaction
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
        // Notify the observers that the player is in battle
        this.setInBattle(true);
        this.setCurrentBattleOpponent(opponent);
        this.notifyObservers();

        state.battle(game, opponent);

        this.setInBattle(false);
        this.setCurrentBattleOpponent(null);
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
     * Given a buildableItem, builds it if it is buildable
     *
     * @param equipment
     */
    public void craft(Buildable equipment) throws InvalidActionException {
        if (equipment.isBuildable(inventory)) {
            equipment.craft(inventory);
        } else {
            throw new InvalidActionException(
                "You don't have enough resources to build this equipment"
            );
        }
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
        for (AttackEquipment e : getAttackEquipmentList()) {
            damageToOpponent += e.getHitRate() * e.getAttackDamage();
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
        for (DefenceEquipment e : this.getDefenceEquipmentList()) {
            finalAttackDamage = (int) (finalAttackDamage * e.getDefenceMultiplier());
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
