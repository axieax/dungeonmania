package dungeonmania.model.entities.movings;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.collectables.TheOneRing;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.movings.movement.AttackMovementState;
import dungeonmania.model.entities.movings.movement.PositionGraph;
import dungeonmania.model.entities.movings.movement.RunMovementState;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerInvincibleState;
import dungeonmania.util.Position;

public class Assassin extends BribableEnemy {
    
    public static final int MAX_MERCENARY_HEALTH = 60;
    public static final int MAX_MERCENARY_ATTACK_DMG = 12;
    public static final int MAX_DISTANCE_TO_BRIBE = 2;
    private static final int BATTLE_RADIUS = 6;
    public final double ARMOUR_DROP_RATE = 0.25;
    
    private boolean moveTwice;

    public Assassin(Position position, int damageMultiplier, SubjectPlayer player) {
        super("assassin", position, MAX_MERCENARY_HEALTH, MAX_MERCENARY_ATTACK_DMG, damageMultiplier);
        this.setMovementState(new AttackMovementState(this));
        this.moveTwice = false;
        player.attach(this);
    }

    @Override
    public void tick(Game game) {
        this.move(game);
        
        Player player = (Player) game.getCharacter();
        // If a player is fighting an enemy within the battle radius, assassin moves twice as fast
        if (this.isAlive() && moveTwice && getDistanceToPlayer(game, player.getPosition()) <= BATTLE_RADIUS) {
            moveTwice = false;
            this.move(game);
        }
    }

    /**
     * If a player drinks an invincibility potion, and assassin is not an ally,
     * change the state of the assassin to make sure it runs away
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
     * Player interacting with assassin will check the bribing
     */
    @Override
    public void interact(Game game, Entity character) {
        bribe(game, (Player) character);
    }

    /**
     * Player attempting to bribe the assassin upon interaction
     */
    @Override
    public void bribe(Game game, Player player) throws InvalidActionException {
        // Player must be within 2 cardinal tiles to the assassin and 
        // have 1 treasure (gold) and TheOneRing in order to bribe the assassin
        Item item = player.findInventoryItem("treasure");
        Item ring = player.findInventoryItem("one_ring");
        if (item != null && item instanceof Treasure && ring != null && ring instanceof TheOneRing) {
            if (getDistanceToPlayer(game, player.getPosition()) <= MAX_DISTANCE_TO_BRIBE) {
                player.addAlly(this);
                ((Treasure) item).consume(game, player);
                ((TheOneRing) ring).consume(game, player);
            } else {
                throw new InvalidActionException("You are too far away to bribe this assassin");
            }
        } else {
            throw new InvalidActionException("You need both treasure and TheOneRing to bribe the assassin");
        }
    }

    /**
     * Find the (shortest) distance between the player and the assassin
     * @param game
     * @param playerPos
     * @return int
     */
    public int getDistanceToPlayer(Game game, Position playerPos) {
        PositionGraph positionGraph = new PositionGraph(game, this);
        return positionGraph.BFS(this.getPosition(), playerPos);
    }
}
