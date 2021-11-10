package dungeonmania.model.entities.movings;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.movement.AttackMovementState;
import dungeonmania.model.entities.movings.movement.PositionGraph;
import dungeonmania.model.entities.movings.movement.RunMovementState;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerInvincibleState;
import dungeonmania.util.Position;

public abstract class BribableEnemy extends Enemy {
    public static final int TREASURE_REQUIRED_TO_BRIBE = 1;
    public static final int BATTLE_RADIUS = 5;
    public static final int MAX_DISTANCE_TO_BRIBE = 2;
    public static final double ARMOUR_DROP_RATE = 0.25;
    
    private boolean bribed;
    private boolean moveTwice;
    private int mindControlTicks = 10;

    public BribableEnemy(String prefix, Position position, int health, int attackDamage, int damageMultiplier) {
        super(prefix, position, health, attackDamage, damageMultiplier);
        this.bribed = false;
        this.moveTwice = false;
    }

    public boolean isBribed() {
        return bribed;
    }

    public void setBribed(boolean bribed) {
        this.bribed = bribed;
    }

    public int getMindControlTicks() {
        return mindControlTicks;
    }

    @Override
    public double getArmourDropRate() {
        return ARMOUR_DROP_RATE;
    }

    @Override
    public void tick(Game game) {
        Player player = (Player) game.getCharacter();

        // Check that the effects for a mind controlled enemy will only last 10 ticks
        // After 10 ticks, the enemy will return to its normal state (no longer an ally)
        this.mindControlTicks--;
        if (this.getMindControlTicks() == 0) player.removeAlly(this);
        
        this.move(game);
        
        // If a player is fighting an enemy within the battle radius, BribableEnemy moves twice as fast
        if (this.isAlive() && moveTwice && getDistanceToPlayer(game, player.getPosition()) <= BATTLE_RADIUS) {
            moveTwice = false;
            this.move(game);
        }
    }

    /**
     * If a player drinks an invincibility potion, and BribableEnemy is not an ally,
     * change the state of the enemy to make sure it runs away
     */
    @Override
    public void update(SubjectPlayer player) { 
        if (!(player instanceof Player)) return;

        Player character = (Player) player; 
        if (character.getInBattle()) moveTwice = true;
        if (character.getState() instanceof PlayerInvincibleState && !this.isBribed()) {
            this.setMovementState(new RunMovementState(this));
        } else {
            this.setMovementState(new AttackMovementState(this));
        }
    }

    /**
     * Player interacting with BribableEnemy will first check if it can be mind controlled
     * If not, it will check if it can be bribed
     */
    @Override
    public void interact(Game game, Entity character) {
        if (!mindControl(game, (Player) character))
            bribe(game, (Player) character);
    }

    /**
     * Find the (shortest) distance between the player and the BribableEnemy
     * @param game
     * @param playerPos
     * @return int
     */
    public int getDistanceToPlayer(Game game, Position playerPos) {
        PositionGraph positionGraph = new PositionGraph(game, this);
        return positionGraph.BFS(this.getPosition(), playerPos);
    }

    /**
     * Condition for player to mind control the enemy
     * @param game
     * @param player
     * @return true if conditions are met, false otherwise
     */
    public boolean mindControl(Game game, Player player) {
        // Player must have the sceptre to mindcontrol the mercenary
        // The effect will only last 10 ticks
        Item item = player.findInventoryItem("sceptre");
        
        if (item == null)
            return false;
        
        player.addAlly(this);
        this.mindControlTicks = 10;
        return true;
    }

    /**
     * Condition for player to bribe the enemy
     * @param game
     * @param player
     * @throws InvalidActionException
     */
    public abstract void bribe(Game game, Player player) throws InvalidActionException;
}
