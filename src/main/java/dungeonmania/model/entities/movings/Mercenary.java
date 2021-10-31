package dungeonmania.model.entities.movings;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.model.Game;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity implements Observer {
    public static final int MAX_MERCENARY_HEALTH = 50;
    public static final int MAX_MERCENARY_ATTACK_DMG = 5;
    public static final int TREASURE_REQUIRED_TO_BRIBE = 1;
    private static final int BATTLE_RADIUS = 5;

    public final double ARMOUR_DROP_RATE = 0.2;
    
    private EnemyMovementState defaultState;
    private EnemyMovementState runState;
    private EnemyMovementState state;

    public Mercenary(Position position) {
        this(position, MAX_MERCENARY_HEALTH, MAX_MERCENARY_ATTACK_DMG);
    }

    public Mercenary(Position position, int health, int attackDamage) {
        super("mercenary", position, health, attackDamage, true);
        this.defaultState = new DefaultState(this);
        this.runState = new RunState(this);

        this.state = defaultState;
    }

    @Override
    public void tick(Game game) {
        Player player = (Player) game.getCharacter();
        Position playerPos = player.getPosition();
        state.move(game, playerPos);

        // Check the cardinally adjacent entities to the player (this denotes they are fighting an enemy)
        // If a player is fighting an enemy within the battle radius, mercenary moves twice as fast to take advantage
        game.getCardinallyAdjacentEntities(playerPos)
            .stream()
            .filter(e -> e instanceof MovingEntity)
            .collect(Collectors.toList())
            .size();

        if (player.numEnemiesCardinallyAdjacent(game) > 0 && getDistanceToPlayer(game, playerPos) <= BATTLE_RADIUS) {
            state.move(game, playerPos);
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
        if (character.getState() instanceof PlayerInvincibleState && character.getAllies() == null) {
            this.setState(getRunState());
        } else {
            this.setState(getDefaultState());
        }
    }

    /** 
     * Player attempts to bribe mercenary
     */
    @Override
    public void interact(Game game, MovingEntity character) { 
        Player player = (Player) character;
        if (player.getInventory().hasItemQuantity("treasure", TREASURE_REQUIRED_TO_BRIBE)) {
            player.addAlly(this);
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
    public void setState(EnemyMovementState state) {
        this.state = state;
    }

    public EnemyMovementState getDefaultState() {
        return defaultState;
    }

    public EnemyMovementState getRunState() {
        return runState;
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
