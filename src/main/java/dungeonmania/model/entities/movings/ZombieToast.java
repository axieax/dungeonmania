package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.movement.RandomMovementState;
import dungeonmania.model.entities.movings.movement.RunMovementState;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerInvincibleState;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.util.Position;
import java.util.Arrays;

public class ZombieToast extends Enemy {

    public static final int MAX_ZOMBIE_HEALTH = 20;
    public static final int MAX_ZOMBIE_ATTACK_DMG = 2;
    public final double ARMOUR_DROP_RATE = 0.2;
    private int animationTick = 0;

    public ZombieToast(Position position, int damageMultiplier, SubjectPlayer player) {
        super("zombie_toast", position, MAX_ZOMBIE_HEALTH, MAX_ZOMBIE_ATTACK_DMG, damageMultiplier);
        this.setMovementState(new RandomMovementState(this));
        player.attach(this);
    }

    @Override
    public void tick(Game game) {
        animationTick += 1;
        this.move(game);
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
    public void moveTo(Position position) {
        this.setPosition(position);
    }

    @Override
    public boolean collision(Entity entity) {
        if(
            entity instanceof Portal
        ) return false;
        return !entity.isPassable();
    }
    
    public AnimationQueue getAnimation() {
        final int skin = animationTick % 15;
        return new AnimationQueue(
            "PostTick",
            getId(),
            Arrays.asList("sprite zombie_toast_" + ((skin < 10) ? "0" + skin : skin)),
            false,
            -1
        );
    }
}
