package dungeonmania.model.entities.movings;

import java.util.List;

import dungeonmania.model.Game;
import dungeonmania.util.Position;

public class RunState implements MovementState {
    private MovingEntity enemy;

    public RunState(MovingEntity enemy) {
        this.enemy = enemy;
    }

    /**
     * Enemy runs away from player
     */
    @Override
    public void move(Game game, Position playerPos) {
        Position currPos = enemy.getPosition();

        List<Position> possiblePositionsToMove = game.getMoveablePositions(enemy, currPos);

        int optimalPathLength = -1;
        Position optimalPathPosition = currPos;

        PositionGraph positionGraph = new PositionGraph(game, enemy);

        // Move the enemy to the furthest possible position to the player
        for (Position position: possiblePositionsToMove) {
            int pathLen = positionGraph.BFS(enemy.getPosition(), playerPos);
            if (pathLen > optimalPathLength) {
                optimalPathLength = pathLen;
                optimalPathPosition = position;
            }
        }

        if (optimalPathPosition != null) enemy.setPosition(optimalPathPosition);
    }
}