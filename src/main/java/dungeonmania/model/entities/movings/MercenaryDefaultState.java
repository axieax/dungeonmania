package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.util.Position;

public class MercenaryDefaultState implements MercenaryState {
    Mercenary mercenary;

    public MercenaryDefaultState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }

    /**
     * Follows the player
     */
    @Override
    public void move(Dungeon dungeon, Position playerPos) {
        // TODO Auto-generated method stub
        
    }
}
