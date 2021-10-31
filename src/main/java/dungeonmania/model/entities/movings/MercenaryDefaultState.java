package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.util.Position;

public class MercenaryDefaultState implements EnemyMovementState {
    Mercenary mercenary;

    public MercenaryDefaultState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }

    /**
     * Follows the player
     */
    @Override
    public void move(Game game, Position playerPos) {
        // TODO Auto-generated method stub
        
    }
}
