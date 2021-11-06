package dungeonmania.model.entities.movings;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.movement.AttackMovementState;
import dungeonmania.model.entities.movings.movement.PositionGraph;
import dungeonmania.model.entities.movings.movement.RunMovementState;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerInvincibleState;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Bribable {
    
    public static final int MAX_MERCENARY_HEALTH = 50;
    public static final int MAX_MERCENARY_ATTACK_DMG = 5;
    public static final int MAX_DISTANCE_TO_BRIBE = 2;
    private static final int BATTLE_RADIUS = 5;
    public final double ARMOUR_DROP_RATE = 0.2;
    
    private boolean isBribed;
    private boolean moveTwice;

    public Mercenary(Position position, int damageMultiplier, SubjectPlayer player) {
        super("mercenary", position, MAX_MERCENARY_HEALTH, MAX_MERCENARY_ATTACK_DMG, damageMultiplier);
        this.setMovementState(new AttackMovementState(this));
        this.moveTwice = false;
        this.isBribed = false;
        player.attach(this);
    }

    @Override
    public void tick(Game game) {
        this.move(game);
        
        Player player = (Player) game.getCharacter();
        // If a player is fighting an enemy within the battle radius, mercenary moves twice as fast
        if (this.isAlive() && moveTwice && getDistanceToPlayer(game, player.getPosition()) <= BATTLE_RADIUS) {
            moveTwice = false;
            this.move(game);
        }
    }

    /**
     * If a player drinks an invincibility potion, and mercenary is not an ally,
     * change the state of the mercenary to make sure it runs away
     */
    @Override
    public void update(SubjectPlayer player) { 
        if (!(player instanceof Player)) return;

        Player character = (Player) player; 
        if (character.getInBattle()) moveTwice = true;
        if (character.getState() instanceof PlayerInvincibleState && !isBribed) {
            this.setMovementState(new RunMovementState(this));
        } else {
            this.setMovementState(new AttackMovementState(this));
        }
    }

    /** 
     * Player interacting with mercenary will check the bribing
     */
    @Override
    public void interact(Game game, MovingEntity character) {
        bribe(game, (Player) character);
    }

    /**
     * Player attempting to bribe the mercenary upon interaction
     */
    @Override
    public void bribe(Game game, Player player) throws InvalidActionException {
        // Player must be within 2 cardinal tiles to the mercenary and 
        // have sufficient treasure (gold) in order to bribe the mercenary
        if (player.hasItemQuantity("treasure", TREASURE_REQUIRED_TO_BRIBE)) {
            if (getDistanceToPlayer(game, player.getPosition()) <= MAX_DISTANCE_TO_BRIBE) {
                player.addAlly(this);
                setBribed(true);
                consume(player, player.findInventoryItem("treasure"));
            } else {
                throw new InvalidActionException("You are too far away to bribe this mercenary");
            }
        } else {
            throw new InvalidActionException("You don't have enough treasure to bribe the mercenary");
        }
    }

    /**
     * Player using up the treasure upon successful bribing
     */
    @Override
    public void consume(Player player, Item item) {
        player.removeInventoryItem(item.getId());
    }

    /**
     * Find the (shortest) distance between the player and the mercenary
     * @param game
     * @param playerPos
     * @return int
     */
    public int getDistanceToPlayer(Game game, Position playerPos) {
        PositionGraph positionGraph = new PositionGraph(game, this);
        return positionGraph.BFS(this.getPosition(), playerPos);
    }

    public void setBribed(boolean isBribed) {
        this.isBribed = isBribed;
    }

}
