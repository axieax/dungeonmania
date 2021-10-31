package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.util.Position;

public interface MercenaryState {
    public void move(Game game, Position playerPos);
}
