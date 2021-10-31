package dungeonmania.model.entities.movings;

import java.util.List;

import dungeonmania.model.Game;
import dungeonmania.util.Position;

public class MercenaryRunState implements EnemyMovementState {
    Mercenary mercenary;

    public MercenaryRunState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }

    /**
     * Runs away from player
     */
    @Override
    public void move(Game game, Position playerPos) {
        Position currPos = mercenary.getPosition();

        List<Position> possiblePositionsToMove = game.getMoveablePositions(this.mercenary, currPos);

        int optimalPathLength = -1;
        Position optimalPathPosition = currPos;

        PositionGraph positionGraph = new PositionGraph(game, this.mercenary);

        // Move the mercenary to the furthest possible position to the player
        for (Position position: possiblePositionsToMove) {
            int pathLen = positionGraph.BFS(this.mercenary.getPosition(), playerPos);
            if (pathLen > optimalPathLength) {
                optimalPathLength = pathLen;
                optimalPathPosition = position;
            }
        }

        if (optimalPathPosition != null) this.mercenary.setPosition(optimalPathPosition);
    }
}
