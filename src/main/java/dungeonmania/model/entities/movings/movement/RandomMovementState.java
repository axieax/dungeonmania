package dungeonmania.model.entities.movings.movement;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.util.Position;
import java.util.List;
import java.util.Random;

public class RandomMovementState extends MovementState {

    public RandomMovementState(Enemy enemy) {
        super(enemy);
    }

    /**
     * Finds a random adjacent position to make.
     * 
     * @param game dungeon
     * @return Position to go to next
     */
    @Override
    public Position findNextPosition(Game game) {
        List<Position> possiblePositions = game.getMoveablePositions(
            this.getEnemy(),
            this.getEnemy().getPosition()
        );

        if (!possiblePositions.isEmpty()) {
            Random rand = new Random();
            Position randPosition = possiblePositions.get(rand.nextInt(possiblePositions.size()));
            return randPosition;
        }

        // All 4 directions are blocked, do not move anywhere
        return this.getEnemy().getPosition();
    }
}
