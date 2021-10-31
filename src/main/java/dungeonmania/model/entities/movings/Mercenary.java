package dungeonmania.model.entities.movings;

import java.util.List;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity implements Observer {
    
    public static final int MAX_MERCENARY_HEALTH = 50;
    public static final int MAX_MERCENARY_ATTACK_DMG = 5;
    public static final int TREASURE_REQUIRED_TO_BRIBE = 1;
    public static final int MAX_DISTANCE_TO_BRIBE = 2;
    private static final int BATTLE_RADIUS = 5;
    public final double ARMOUR_DROP_RATE = 0.2;
    
    private MovementState state;
    private boolean moveTwice;

    public Mercenary(Position position, int damageMultiplier) {
        super("mercenary", position, MAX_MERCENARY_HEALTH, MAX_MERCENARY_ATTACK_DMG, true, damageMultiplier);
        this.state = new DefaultState(this);
        this.moveTwice = false;
    }

    @Override
    public void tick(Game game) {
        Player player = (Player) game.getCharacter();
        Position playerPos = player.getPosition();
        state.move(game, playerPos);

        // If a player is fighting an enemy within the battle radius, mercenary moves twice as fast to take advantage
        if (moveTwice && getDistanceToPlayer(game, playerPos) <= BATTLE_RADIUS) {
            state.move(game, playerPos);
            moveTwice = false;
        }
    }

    /**
     * If a player drinks an invincibility potion, and mercenary is not an ally,
     * change the state of the mercenary to make sure it runs away
     */
    @Override
    public void update(SubjectPlayer player) { 
        if (!(player instanceof Player)) {
            return;
        }

        Player character = (Player) player; 
        if (character.getInBattle()) moveTwice = true;
        if (character.getState() instanceof PlayerInvincibleState && character.getAllies() == null) {
            this.setState(new RunState(this));
        } else {
            this.setState(new DefaultState(this));
        }
    }

    /** 
     * Player attempts to bribe mercenary
     */
    @Override
    public void interact(Game game, MovingEntity character) throws InvalidActionException {
        Player player = (Player) character;
        // Player must be within 2 cardinal tiles to the mercenary and 
        // have sufficient treasure (gold) in order to bribe the mercenary
        if (player.hasItemQuantity("treasure", TREASURE_REQUIRED_TO_BRIBE)) {
            if (getDistanceToPlayer(game, player.getPosition()) <= MAX_DISTANCE_TO_BRIBE) {
                player.addAlly(this);
            } else {
                throw new InvalidActionException("You are too far away to bribe this mercenary");
            }
        } else {
            throw new InvalidActionException("You don't have enough treasure to bribe the mercenary");
        }
    }

    @Override
    public Direction getDirection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void moveTo(Position position) {
        this.setPosition(position);
    }

    @Override
    public boolean isEnemy() {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    public void setState(MovementState state) {
        this.state = state;
    }

    public int getDistanceToPlayer(Game game, Position playerPos) {
        Position currPos = this.getPosition();

        List<Position> possiblePositionsToMove = game.getMoveablePositions(this, currPos);

        int optimalPathLength = -1;

        PositionGraph positionGraph = new PositionGraph(game, this);

        // Find the shortest possible path from the mercenary to the player
        for (Position position: possiblePositionsToMove) {
            int pathLen = positionGraph.BFS(position, playerPos);
            if (pathLen > optimalPathLength) {
                optimalPathLength = pathLen;
            }
        }
        return optimalPathLength;
    }

    public void move(Game game, Position playerPos) {
        Position currPos = this.getPosition();

        List<Position> possiblePositionsToMove = game.getMoveablePositions(this, currPos);

        int optimalPathLength = Integer.MAX_VALUE;
        Position optimalPathPosition = currPos;

        PositionGraph positionGraph = new PositionGraph(game, this);

        // Move the mercenary to the closest possible position to the player
        for (Position position : possiblePositionsToMove) {
            int pathLen = positionGraph.BFS(this.getPosition(), playerPos);
            if (pathLen < optimalPathLength) {
                optimalPathLength = pathLen;
                optimalPathPosition = position;
            }
        }

        if (optimalPathPosition != null) this.setPosition(optimalPathPosition);
    }
}
