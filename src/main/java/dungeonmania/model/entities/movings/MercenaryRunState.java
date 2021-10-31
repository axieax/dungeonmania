package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.util.Position;

public class MercenaryRunState implements MercenaryState {
    Mercenary mercenary;

    public MercenaryRunState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }

    /**
     * Runs away from player
     */
    @Override
    public void move(Game game, Position playerPos) {
        // TODO Auto-generated method stub
    }
}
