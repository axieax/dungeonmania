package dungeonmania.model.entities.movings.movement;

import java.util.List;
import java.util.Map;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class RunMovementState implements MovementState {
    
    private Enemy enemy;

    public RunMovementState(Enemy enemy) {
        this.enemy = enemy;
    }

    /**
     * Enemy runs away from player
     */
    @Override
    public void move(Game game) {
        Player player = game.getCharacter();

        List<Position> possiblePositionsToMove = game.getMoveablePositions(enemy, enemy.getPosition());

        int optimalPathLength = -1;
        Position optimalPathPosition = enemy.getPosition();

        PositionGraph positionGraph = new PositionGraph(game, enemy);
        // Move the enemy to the furthest possible position to the player
        for (Position position: possiblePositionsToMove) {
            Map<Integer, Position> prev = positionGraph.dijkstra(position);
            int pathLen = 0;
            Position curr = prev.get(player.getPosition().hashCode());
            while (curr != null && curr != player.getPosition()) {
                curr = prev.get(curr.hashCode());
                pathLen++;
            }
            // gets the longest shortest path

            if (pathLen > optimalPathLength) {
                optimalPathLength = pathLen;
                optimalPathPosition = position;
            }
        }

        if (optimalPathPosition != null) {
            Position offset = Position.calculatePositionBetween(
            enemy.getPosition(),
            optimalPathPosition
        );
            if (Direction.LEFT.getOffset().equals(offset)) enemy.setDirection(Direction.LEFT);
            else if (Direction.UP.getOffset().equals(offset)) enemy.setDirection(Direction.UP);
            else if (Direction.RIGHT.getOffset().equals(offset)) enemy.setDirection(Direction.RIGHT);
            else if (Direction.DOWN.getOffset().equals(offset)) enemy.setDirection(Direction.DOWN);
            else enemy.setDirection(Direction.NONE);
            // Interact with all entities in that direction
            List<Entity> entities = game.getEntities(enemy.getPosition().translateBy(enemy.getDirection()));
            entities.forEach(entity -> entity.interact(game, enemy));
            enemy.setPosition(enemy.getPosition().translateBy(enemy.getDirection()));
        } else {
            enemy.setDirection(Direction.NONE);
        }
        
    }
}
