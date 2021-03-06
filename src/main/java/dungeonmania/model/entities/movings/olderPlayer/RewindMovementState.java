package dungeonmania.model.entities.movings.olderPlayer;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.movement.MovementState;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RewindMovementState extends MovementState {

    private Iterator<Position> moves;

    public RewindMovementState(OlderPlayer player, Iterator<Position> moves) {
        super(player);
        this.moves = moves;
    }

    /**
     * Follows the past moves made by the player.
     *
     * @param game
     */
    @Override
    public void move(Game game) {
        Position nextMove = this.findNextPosition(game);
        if (nextMove != null) {
            this.setEnemyDirection(nextMove);
            this.interact(game);
            this.getEnemy().setPosition(nextMove);
        } else game.removeEntity(this.getEnemy());
    }

    /**
     * Gets the next move
     *
     * @param game game state
     * @return Position of next move
     */
    public Position findNextPosition(Game game) {
        return moves.hasNext() ? moves.next() : null;
    }

    /**
     * Returns the rest of the moves as a list
     *
     * @return list of moves remaining
     */
    public List<Position> remainingMoves() {
        List<Position> positions = new ArrayList<>();
        while (moves.hasNext()) {
            positions.add(moves.next());
        }
        moves = positions.iterator();
        return positions;
    }
}
