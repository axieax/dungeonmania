package dungeonmania.model.entities.movings.movement;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CircularMovementState extends MovementState {

    private boolean initialMovement;
    private List<Direction> circularMovementPath;
    private boolean reverseMovement;
    private int indexOfNextMove;

    public CircularMovementState(Enemy enemy) {
        super(enemy);
        this.initialMovement = true;
        // Default "circling" movement
        this.circularMovementPath =
            Arrays.asList(
                Direction.RIGHT,
                Direction.DOWN,
                Direction.DOWN,
                Direction.LEFT,
                Direction.LEFT,
                Direction.UP,
                Direction.UP,
                Direction.RIGHT
            );
        // Index of circularMovementPath
        this.indexOfNextMove = 0;
        this.reverseMovement = false;
    }

    public CircularMovementState(Enemy enemy, int indexOfNextMove) {
        this(enemy);
        this.indexOfNextMove = indexOfNextMove;
    }

    /**
     * Finds the next tile, ensuring that the entity maintains a "circular path"
     * and reverses direction if necessary.
     *
     * @param game dungeon the entity is contained in
     * @return Position to move next
     */
    @Override
    public Position findNextPosition(Game game) {
        // initially always move up if possible, else stay in spot
        if (initialMovement) {
            Position newPos = this.getEnemy().getPosition().translateBy(Direction.UP);
            boolean canMove = game
                .getEntities(newPos)
                .stream()
                .allMatch(e -> !this.getEnemy().collision(e));
            if (canMove) {
                this.initialMovement = false;
                return this.getEnemy().getPosition().translateBy(Direction.UP);
            }
            return this.getEnemy().getPosition();
        } else {
            Direction nextMoveInPath = circularMovementPath.get(indexOfNextMove);
            Position newPos = this.getEnemy().getPosition().translateBy(nextMoveInPath);
            boolean canMove = game
                .getEntities(newPos)
                .stream()
                .allMatch(e -> !this.getEnemy().collision(e));
            if (!canMove) {
                this.reverseMovement = !this.reverseMovement;
                indexOfNextMove = Math.abs(circularMovementPath.size() - (indexOfNextMove + 4)) % 8;
                Collections.reverse(this.circularMovementPath);
                return this.getEnemy().getPosition();
            }

            if (indexOfNextMove >= circularMovementPath.size() - 1) { // end of movement path
                indexOfNextMove = 0;
            } else {
                indexOfNextMove += 1;
            }
            return newPos;
        }
    }

    /**
     * Checks if is initial movement
     *
     * @return true if initial movement, false otherwise
     */
    public boolean isInitialMovement() {
        return initialMovement;
    }

    /**
     * Checks if is reverse movement
     *
     * @return true if reverse movement, false otherwise
     */
    public boolean isReverseMovement() {
        return reverseMovement;
    }

    /**
     * Get index of next move
     *
     * @return index
     */
    public int getIndexOfNextMove() {
        return indexOfNextMove;
    }
}
