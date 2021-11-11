package dungeonmania.model.entities.movings.movement;

import java.util.List;
import java.util.Random;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerInvisibleState;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class AttackMovementState implements MovementState {
    
    private MovingEntity enemy;

    public AttackMovementState(MovingEntity enemy) {
        this.enemy = enemy;
    }

    /**
     * Enemy follows player
     */
    @Override
    public void move(Game game) {
        Player player = (Player) game.getCharacter();

        List<Position> possiblePositionsToMove = game.getMoveablePositions(enemy, enemy.getPosition());

        int optimalPathLength = Integer.MAX_VALUE;
        Position optimalPathPosition = enemy.getPosition();

        PositionGraph positionGraph = new PositionGraph(game, enemy);

        // Move the enemy to the closest possible position to the player
        for (Position position : possiblePositionsToMove) {
            int pathLen = positionGraph.BFS(position, player.getPosition());
            if (pathLen < optimalPathLength) {
                optimalPathLength = pathLen;
                optimalPathPosition = position;
            }
        }

        // If the player is invisible, move the enemy randomly (will not follow player)
        if (player.getState() instanceof PlayerInvisibleState) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(possiblePositionsToMove.size());
            optimalPathPosition = possiblePositionsToMove.get(randomIndex);
        }

        enemy.setPosition(optimalPathPosition);
        Position offset = Position.calculatePositionBetween(enemy.getPosition(), optimalPathPosition);
            if (Direction.LEFT.getOffset().equals(offset)) enemy.setDirection(Direction.LEFT);
            else if (Direction.UP.getOffset().equals(offset)) enemy.setDirection(Direction.UP);
            else if (Direction.RIGHT.getOffset().equals(offset)) enemy.setDirection(Direction.RIGHT);
            else if (Direction.DOWN.getOffset().equals(offset)) enemy.setDirection(Direction.DOWN);
            else enemy.setDirection(Direction.NONE);

        if (player.getPosition().equals(enemy.getPosition())){
            player.battle(game, enemy);
        }
    }
}
