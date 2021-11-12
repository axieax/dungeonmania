package dungeonmania.model.entities.movings.older_player;

import dungeonmania.model.Game;
import dungeonmania.util.Position;
import java.util.Iterator;

public class RewindOlderPlayerState implements OlderPlayerState {

    private Iterator<Position> moves;

    public RewindOlderPlayerState(Iterator<Position> moves) {
        this.moves = moves;
    }

    public void move(Game game, OlderPlayer ilNam) {
        if (moves.hasNext()) {
            ilNam.setPosition(moves.next());
        } else {
            game.removeEntity(ilNam);
        }
    }
}
