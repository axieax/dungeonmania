package dungeonmania.model.entities.movings.movement;

import java.util.List;
import java.util.Random;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class RandomMovementState implements MovementState {
    
    private MovingEntity enemy;

    public RandomMovementState(MovingEntity enemy) {
        this.enemy = enemy;
    }

    /**
     * Enemy runs away from player
     */
    @Override
    public void move(Game game) {
        List<Position> possiblePositions = game.getMoveablePositions(enemy, enemy.getPosition());

        // All 4 directions are blocked, do not move anywhere
        if (!possiblePositions.isEmpty()) {
            Random rand = new Random();
            Position randPosition = possiblePositions.get(rand.nextInt(possiblePositions.size()));
            Position offset = Position.calculatePositionBetween(enemy.getPosition(), randPosition);
            if (Direction.LEFT.getOffset().equals(offset)) enemy.setDirection(Direction.LEFT);
            else if (Direction.UP.getOffset().equals(offset)) enemy.setDirection(Direction.UP);
            else if (Direction.RIGHT.getOffset().equals(offset)) enemy.setDirection(Direction.RIGHT);
            else if (Direction.DOWN.getOffset().equals(offset)) enemy.setDirection(Direction.DOWN);
            enemy.setPosition(randPosition);
        }
        else enemy.setDirection(Direction.NONE);
    }
}
