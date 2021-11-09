package dungeonmania.model.entities.movings.movement;

import java.util.List;
import java.util.Random;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class RandomMovementState implements MovementState {
    
    private MovingEntity enemy;

    public RandomMovementState(MovingEntity enemy) {
        this.enemy = enemy;
    }

    /**
     * Enemy makes a random movement choice
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
            // Interact with all entities in that direction
            List<Entity> entities = game.getEntities(enemy.getPosition().translateBy(enemy.getDirection()));
            entities.forEach(entity -> entity.interact(game, enemy));
            enemy.setPosition(enemy.getPosition().translateBy(enemy.getDirection()));
        }
        else enemy.setDirection(Direction.NONE);
    }
}
