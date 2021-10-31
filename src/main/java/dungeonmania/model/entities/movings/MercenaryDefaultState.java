package dungeonmania.model.entities.movings;

import java.util.List;

import dungeonmania.model.Game;
import dungeonmania.util.Position;

public class MercenaryDefaultState implements EnemyMovementState {
    Mercenary mercenary;

    public MercenaryDefaultState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }

    /**
     * Follows the player
     */
    @Override
    public void move(Game game, Position playerPos) {
        Position currPos = mercenary.getPosition();

        List<Position> possiblePositionsToMove = game.getMoveablePositions(this.mercenary);

        int optimalPathLength = Integer.MAX_VALUE;
        Position optimalPathPosition;

        PositionGraph positionGraph = new PositionGraph(game, this.mercenary);

        // Move the mercenary to the closest possible position to the player
        for (Position position : possiblePositionsToMove) {
            int pathLen = positionGraph.BFS(this.mercenary.getPosition(), playerPos);
            if (pathLen < optimalPathLength) {
                optimalPathLength = pathLen;
                optimalPathPosition = position;
            }
        }

        if (optimalPathPosition != null) this.mercenary.setPosition(optimalPathPosition);
    }
}
