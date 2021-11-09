package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.movement.RandomMovementState;
import dungeonmania.model.entities.movings.movement.RunMovementState;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerInvincibleState;
import dungeonmania.util.Position;

public class Hydra extends Enemy {
    private static final int MAX_HYDRA_HEALTH = 0;
    private static final int MAX_HYDRA_ATTACK_DMG = 0;

    public Hydra(Position position, int damageMultiplier, SubjectPlayer player) {
        super("hydra", position, MAX_HYDRA_HEALTH, MAX_HYDRA_ATTACK_DMG, damageMultiplier);
        this.setMovementState(new RandomMovementState(this));
        player.attach(this);
    }

    /**
     * If a player drinks an invincibility potion, change the state
     * of the hydra to make sure it runs away
     */
    @Override
    public void update(SubjectPlayer player) {
        if (!(player instanceof Player)) {
            return;
        }

        Player character = (Player) player;
        if (character.getState() instanceof PlayerInvincibleState) {
            this.setMovementState(new RunMovementState(this));
        } else {
            this.setMovementState(new RandomMovementState(this));
        }
    }

    @Override
    public void tick(Game game) {
        this.move(game);
    }    
}
