package dungeonmania.model.entities.movings.movement;

import java.util.List;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;

public class RunMovementState implements MovementState {
    
    private MovingEntity enemy;

    public RunMovementState(MovingEntity enemy) {
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
            int pathLen = positionGraph.BFS(position, player.getPosition());
            if (pathLen > optimalPathLength) {
                optimalPathLength = pathLen;
                optimalPathPosition = position;
            }
        }

        if (optimalPathPosition != null) enemy.setPosition(optimalPathPosition);
    }
}
