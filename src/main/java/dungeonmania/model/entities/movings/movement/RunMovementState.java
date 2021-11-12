package dungeonmania.model.entities.movings.movement;

import java.util.List;
import java.util.Map;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;

public class RunMovementState extends MovementState {

    public RunMovementState(Enemy enemy) {
        super(enemy);
    }

    /**
     * Find the best position to move to get away from the player.
     * 
     * @param game
     * @return Position to move to next
     */
    @Override
    public Position findNextPosition(Game game) {
        Player player = game.getCharacter();

        List<Position> possiblePositionsToMove = game.getMoveablePositions(this.getEnemy(), this.getEnemy().getPosition());

        int optimalPathLength = Integer.MIN_VALUE;
        Position optimalPathPosition = this.getEnemy().getPosition();

        PositionGraph positionGraph = new PositionGraph(game, this.getEnemy());
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
        return optimalPathPosition;
    }

}
