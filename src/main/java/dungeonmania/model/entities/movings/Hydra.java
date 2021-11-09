package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.movement.RandomMovementState;
import dungeonmania.util.Position;

public class Hydra extends Enemy {
    private static final int MAX_HYDRA_HEALTH = 0;
    private static final int MAX_HYDRA_ATTACK_DMG = 0;

    public Hydra(Position position, int damageMultiplier, SubjectPlayer player) {
        super("hydra", position, MAX_HYDRA_HEALTH, MAX_HYDRA_ATTACK_DMG, damageMultiplier);
        this.setMovementState(new RandomMovementState(this));
        player.attach(this);
    }

    @Override
    public void update(SubjectPlayer player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void tick(Game game) {
        // TODO Auto-generated method stub
        
    }    
}
