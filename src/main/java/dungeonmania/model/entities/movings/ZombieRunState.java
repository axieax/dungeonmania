package dungeonmania.model.entities.movings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieRunState implements EnemyMovementState {
    private ZombieToast zombie;

    public ZombieRunState(ZombieToast zombie) {
        this.zombie = zombie;
    }

    /**
     * Runs away from player
     */
    @Override
    public void move(Game game, Position playerPos) {
        Position currPos = zombie.getPosition();

        List<Position> possiblePositionsToMove = game.getMoveablePositions(this.zombie, currPos);

        int optimalPathLength = -1;
        Position optimalPathPosition = currPos;

        PositionGraph positionGraph = new PositionGraph(game, this.zombie);

        // Move the zombie to the furthest possible position to the player
        for (Position position: possiblePositionsToMove) {
            int pathLen = positionGraph.BFS(this.zombie.getPosition(), playerPos);
            if (pathLen > optimalPathLength) {
                optimalPathLength = pathLen;
                optimalPathPosition = position;
            }
        }

        if (optimalPathPosition != null) this.zombie.setPosition(optimalPathPosition);
    }

}
