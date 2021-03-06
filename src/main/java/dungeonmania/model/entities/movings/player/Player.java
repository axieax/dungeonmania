package dungeonmania.model.entities.movings.player;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.exceptions.PlayerDeadException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.buildables.Buildable;
import dungeonmania.model.entities.collectables.Bomb;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.potion.Potion;
import dungeonmania.model.entities.movings.BribableEnemy;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Observer;
import dungeonmania.model.entities.movings.SubjectPlayer;
import dungeonmania.model.entities.movings.olderPlayer.OlderPlayer;
import dungeonmania.model.entities.statics.Consumable;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
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

    public static final int CHARACTER_ATTACK_DMG = 10;
    private final int maxCharacterHealth;

    private PlayerState state;
    private boolean inBattle;
    private MovingEntity currentBattleOpponent;
    private Inventory inventory = new Inventory();
    private List<BribableEnemy> allies = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();

    public Player(Position position, int health) {
        super("player", position, health, CHARACTER_ATTACK_DMG);
        this.state = new PlayerDefaultState(this);
        this.inBattle = false;
        this.currentBattleOpponent = null;
        maxCharacterHealth = health;
    }

    /********************************
     *  Getters and Setters         *
     ********************************/

    /**
     * Get the player state
     *
     * @return PlayerState
     */
    public PlayerState getState() {
        return state;
    }

    /**
     * Sets the player state.
     *
     * @param state
     */
    public void setState(PlayerState state) {
        this.state = state;
        this.notifyObservers();
    }

    /**
     * Checks if a player is in battle
     *
     * @return true if the player is currently in battle, false otherwise
     */
    public boolean getInBattle() {
        return inBattle;
    }

    /**
     * Set the player battle status.
     *
     * @param inBattle
     */
    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }

    /**
     * Get the current battle opponent
     *
     * @return true if in battle, false otherwise
     */
    public MovingEntity getCurrentBattleOpponent() {
        return currentBattleOpponent;
    }

    /**
     * Set the current opponent that the player is fighting against.
     *
     * @param opponent
     */
    public void setCurrentBattleOpponent(MovingEntity opponent) {
        this.currentBattleOpponent = opponent;
    }

    /**
     * Get maxCharacterHealth attribute
     *
     * @return maximum character health
     */
    public int getMaxCharacterHealth() {
        return maxCharacterHealth;
    }

    /********************************
     *  Ally Methods                *
     ********************************/

    /**
     * Get a list of all allies that the player has.
     *
     * @return list of allies
     */
    public List<BribableEnemy> getAllies() {
        return this.allies;
    }

    /**
     * Add an ally (becomes bribed) to the player.
     *
     * @param ally
     */
    public void addAlly(BribableEnemy ally) {
        // Check if the bribable enemy is already an ally
        if (allies.stream().anyMatch(m -> m.getId().equals(ally.getId()))) {
            return;
        }

        allies.add(ally);
        ally.setBribed(true);
    }

    /**
     * Remove an ally (no longer bribed) from the player.
     *
     * @param ally
     */
    public void removeAlly(MovingEntity ally) {
        if (ally instanceof BribableEnemy) {
            ((BribableEnemy) ally).setBribed(false);
        }

        allies.remove(ally);
    }

    /**
     * Removes all allies.
     *
     * @param ally
     */
    public void removeAllies() {
        allies = new ArrayList<>();
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
     * @param prefix of item
     * @return Item if found, else null
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
     * @return list of equipments
     */
    public List<Equipment> getEquipmentList() {
        return inventory.getEquipmentList();
    }

    /**
     * Get a list of all attackable equipments from the inventory.
     *
     * @return list of attack equipments
     */
    public List<AttackEquipment> getAttackEquipmentList() {
        return this.getEquipmentList()
            .stream()
            .filter(equipment -> equipment instanceof AttackEquipment)
            .map(equipment -> (AttackEquipment) equipment)
            .collect(Collectors.toList());
    }

    /**
     * Get a list of all defendable equipments from the inventory.
     *
     * @return list of defence equipments
     */
    public List<DefenceEquipment> getDefenceEquipmentList() {
        return this.getEquipmentList()
            .stream()
            .filter(equipment -> equipment instanceof DefenceEquipment)
            .map(equipment -> (DefenceEquipment) equipment)
            .collect(Collectors.toList());
    }

    /**
     * Checks if the player has a key
     *
     * @return true if player has a key, false otherwise
     */
    public boolean hasKey() {
        return this.getKey() != null;
    }

    /**
     * Get the key from a player's inventory
     *
     * @return Key
     */
    public Key getKey() {
        return (Key) inventory.findItem("key");
    }

    /**
     * Check if a player has a weapon
     * @return true if the player has a weapon, false otherwise
     */
    public boolean hasWeapon() {
        return !this.getAttackEquipmentList().isEmpty();
    }

    /**
     * Get the first weapon in the player's inventory
     * @return weapon
     */
    public Equipment getWeapon() {
        return (AttackEquipment) inventory.findWeapon();
    }

    /**
     * Checks if the inventory has the specified quantity of the item.
     *
     * @param prefix
     * @param quantity
     * @return boolean true if there's the specified quantity in the player inventory
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

    @Override
    public void interact(Game game, Entity character) {
        if (character instanceof Enemy) this.battle(game, (Enemy) character);
    }

    /**
     * Conduct any required tasks for a player after it has moved into its new position
     *
     * @param game
     */
    @Override
    public void tick(Game game) throws PlayerDeadException {
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
    }

    /**
     * Interacts with any entity that is on the tile the character is about to move into.
     * Upon movement, any observers are notified. If an entity blocks the player, then the
     * player does not move at all.
     *
     * @param game
     * @param direction
     */
    public void move(Game game, Direction direction) throws PlayerDeadException {
        this.move(game, direction, null);
    }

    /**
     * Interacts with any entity that is on the tile the character is about to move into.
     * Upon movement, any observers are notified. If an entity blocks the player, then the
     * player does not move at all.
     *
     * @param game
     * @param direction
     */
    public void move(Game game, Direction direction, String itemId)
        throws IllegalArgumentException, InvalidActionException {
        if (itemId != null && itemId.length() > 0) {
            Item item = getInventoryItem(itemId);

            // Check if item is in the player's inventory
            if (item == null) throw new InvalidActionException(
                "At Player move method - itemUsed is not in the player's inventory"
            );

            // Item is not null, and it's not a bomb or any potion
            if (!(item instanceof Bomb || item instanceof Potion)) {
                throw new IllegalArgumentException("Not a valid item to use");
            }

            // Consume item
            if (item instanceof Consumable) ((Consumable) item).consume(game, this);
        }

        this.setDirection(direction);

        // Interact with all entities in that direction
        List<Entity> entities = game.getEntities(this.getPosition().translateBy(direction));
        entities.forEach(entity -> {
            // Cannot interact with moving entities or spawners when moving
            if (
                !(entity instanceof MovingEntity || entity instanceof ZombieToastSpawner)
            ) entity.interact(game, this);
        });

        // Gets the updated entities after the interaction
        List<Entity> updatedEntities = game.getEntities(this.getPosition().translateBy(direction));
        boolean canMove = updatedEntities.stream().allMatch(e -> !this.collision(e));

        if (canMove) {
            this.setPosition(this.getPosition().translateBy(direction));
            this.tick(game);
        }

        this.state.updateState(this);
        // should be notified regardless of if player can move e.g. if player drinks invincibility potion
        this.notifyObservers();
    }

    /**
     * A battle takes place when the character and the enemy are in the same cell, within a single tick.
     * A round of a battle occurs as follows:
     *      Character Health = Character Health - ((Enemy Health * Enemy Attack Damage) / 10)
     *      Enemy Health = Enemy Health - ((Character Health * Character Attack Damage) / 5)
     *
     * @param opponent entity the character is fighting
     */
    private void battle(Game game, Enemy opponent) throws PlayerDeadException {
        if (this.canBattleOpponent(opponent)) {
            state.battle(game, opponent);
            if (!this.isAlive()) throw new PlayerDeadException("Player has died... Ending game...");
        }
    }

    /**
     * If the player encounters their olderself and are carrying a sun stone or are wearing midnight armour,
     * or they are invisible, then nothing happens. If not, then a battle ensues.
     *
     * @param opponent
     * @return boolean true if the player can battle the opponent
     */
    private boolean canBattleOpponent(Enemy opponent) {
        if (!(opponent instanceof OlderPlayer)) return true;
        return !(
            this.findInventoryItem("sun_stone") != null ||
            this.findInventoryItem("midnight_armour") != null ||
            this.getState() instanceof PlayerInvisibleState
        );
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
     * Given an item, builds it if it is buildable
     *
     * @param equipment
     * @throws InvalidActionException if the player doesn't have enough resources or fails zombie check
     */
    public void craft(Game game, Buildable equipment) throws InvalidActionException {
        if (equipment.isBuildable(game, inventory)) {
            equipment.craft(inventory);
        } else {
            throw new InvalidActionException(
                "You do not meet the requirements to build this equipment"
            );
        }
    }

    /**
     * Check if the equipment is buildable
     *
     * @param equipment
     * @return boolean true if the equipment is buildable
     */
    public boolean checkBuildable(Game game, Buildable item) {
        return item.isBuildable(game, this.inventory);
    }

    /********************************
     *  Battling Methods            *
     ********************************/

    /**
     * Returns the total attack damage a player is able to inflict upon an opponent.
     * This includes any attack damage provided by equipment e.g. sword
     *
     * @return int indicating the amount of attack
     */
    public int getTotalAttackDamage(MovingEntity opponent) {
        // Normal damage inflicted by player
        int damageToOpponent = this.getBaseAttackDamage();

        // Any extra attack damage provided by weapons
        for (AttackEquipment e : getAttackEquipmentList()) {
            damageToOpponent += e.getHitRate() * e.useEquipment(this, opponent);
        }

        // Any extra attack damage provided by defence equipments
        for (DefenceEquipment e : getDefenceEquipmentList()) {
            damageToOpponent += e.getBonusAttackDamage();
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
    public int applyDefenceToOpponentAttack(MovingEntity opponent) {
        int finalAttackDamage = opponent.getBaseAttackDamage();
        for (DefenceEquipment e : this.getDefenceEquipmentList()) {
            finalAttackDamage = (int) (e.useEquipment(this, opponent));
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

    /**
     * Notifies all observers of the player.
     */
    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    /**
     * Removes all observers
     */
    @Override
    public void removeObservers() {
        observers = new ArrayList<>();
    }

    @Override
    public AnimationQueue getAnimation() {
        double health = (double) getHealth() / maxCharacterHealth;
        return new AnimationQueue(
            "PostTick",
            getId(),
            Arrays.asList("healthbar set " + health, "healthbar tint 0xff0000, over 0.5s"),
            false,
            10
        );
    }

    @Override
    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put("playerState", state.getClass().getSimpleName());
        info.put("ticksLeft", state.ticksLeft());
        info.put("inventory", inventory.toJSON());
        return info;
    }
}
