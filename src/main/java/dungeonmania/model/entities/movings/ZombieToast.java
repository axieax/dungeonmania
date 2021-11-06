package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.List;
import java.util.Random;

public class ZombieToast extends MovingEntity implements Observer {

    public static final int MAX_ZOMBIE_HEALTH = 20;
    public static final int MAX_ZOMBIE_ATTACK_DMG = 2;
    public final double ARMOUR_DROP_RATE = 0.2;
    private MovementState movementState;

    public ZombieToast(Position position, int damageMultiplier, SubjectPlayer player) {
        super(
            "zombie_toast",
            position,
            MAX_ZOMBIE_HEALTH,
            MAX_ZOMBIE_ATTACK_DMG,
            true,
            damageMultiplier
        );
        this.movementState = new RandomMovementState(this);
        player.attach(this);
    }

    @Override
    public void tick(Game game) {
        movementState.move(game);
    }

    /**
     * If a player drinks an invincibility potion, change the state
     * of the zombie to make sure it runs away
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
    public void interact(Game game, MovingEntity character) {}

    @Override
    public void moveTo(Position position) {
        this.setPosition(position);
    }

    //////////////////////////////////////////////////////////////////
    public void setMovementState(MovementState state) {
        this.movementState = state;
    }
}
