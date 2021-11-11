package dungeonmania.model.entities.movings.movement;

import java.util.List;
import java.util.Random;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.MovingEntity;
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
            // Interact with all entities in that direction
            List<Entity> entities = game.getEntities(randPosition);
            entities.forEach(
                entity -> {
                    // Cannot interact with moving entities when moving
                    if (!(entity instanceof MovingEntity)) entity.interact(game, enemy);
                }
            );
            enemy.setPosition(randPosition);
        }
        else enemy.setDirection(Direction.NONE);
    }
}
