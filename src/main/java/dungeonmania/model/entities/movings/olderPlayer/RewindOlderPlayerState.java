package dungeonmania.model.entities.movings.olderPlayer;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.movement.MovementState;
import dungeonmania.util.Position;
import java.util.Iterator;

public class RewindOlderPlayerState extends MovementState {

    private Iterator<Position> moves;

    public RewindOlderPlayerState(OlderPlayer player, Iterator<Position> moves) {
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
            this.updatePosition();
        } else game.removeEntity(this.getEnemy());
    }

    public Position findNextPosition(Game game) {
        return moves.hasNext() ? moves.next() : null;
    }
}
