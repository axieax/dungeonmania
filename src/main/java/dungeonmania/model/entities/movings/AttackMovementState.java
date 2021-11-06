package dungeonmania.model.entities.movings;

import java.util.List;
import java.util.Random;

import dungeonmania.model.Game;
import dungeonmania.util.Position;

public class AttackMovementState implements MovementState {
    
    private MovingEntity enemy;

    public AttackMovementState(MovingEntity enemy) {
        this.enemy = enemy;
    }

    /**
     * Enemy runs away from player
     */
    @Override
    public void move(Game game) {
        Player player = (Player) game.getCharacter();

        List<Position> possiblePositionsToMove = game.getMoveablePositions(enemy, enemy.getPosition());

        int optimalPathLength = Integer.MAX_VALUE;
        Position optimalPathPosition = enemy.getPosition();

        PositionGraph positionGraph = new PositionGraph(game, enemy);

        // Move the mercenary to the closest possible position to the player
        for (Position position : possiblePositionsToMove) {
            int pathLen = positionGraph.BFS(position, player.getPosition());
            if (pathLen < optimalPathLength) {
                optimalPathLength = pathLen;
                optimalPathPosition = position;
            }
        }

        // If the player is invisible, move the mercenary randomly (will not follow player)
        if (player.getState() instanceof PlayerInvisibleState) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(possiblePositionsToMove.size());
            optimalPathPosition = possiblePositionsToMove.get(randomIndex);
        }

        enemy.setPosition(optimalPathPosition);

        if (player.getPosition().equals(enemy.getPosition())){
            player.battle(game, enemy);
        }
    }
}
