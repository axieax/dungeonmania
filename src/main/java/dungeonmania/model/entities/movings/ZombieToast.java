package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ZombieToast extends MovingEntity implements Observer {

    static final int MAX_ZOMBIE_HEALTH = 20;
    static final int MAX_ZOMBIE_ATTACK_DMG = 2;
    public final double ARMOUR_DROP_RATE = 0.2;
    private MovementState state;

    public ZombieToast(Position position, int damageMultiplier) {
        super(
            "zombie_toast",
            position,
            MAX_ZOMBIE_HEALTH,
            MAX_ZOMBIE_ATTACK_DMG,
            true,
            damageMultiplier
        );
        this.state = new DefaultState(this);
    }

    public ZombieToast(Position position, int damageMultiplier, SubjectPlayer player) {
        this(position, damageMultiplier);
        player.attach(this);
    }

    @Override
    public void tick(Game game) {
        Position playerPos = game.getCharacter().getPosition();
        state.move(game, playerPos);
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
            this.setState(new RunState(this));
        } else {
            this.setState(new DefaultState(this));
        }
    }

    @Override
    public void interact(Game game, MovingEntity character) {}

    @Override
    public Direction getDirection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void moveTo(Position position) {
        this.setPosition(position);
    }

    @Override
    public boolean isEnemy() {
        return true;
    }

    //////////////////////////////////////////////////////////////////
    public void setState(MovementState state) {
        this.state = state;
    }

    public MovementState getState() {
        return state;
    }

    public void move(Game game, Position playerPos) {
        List<Position> possiblePositions = game.getMoveablePositions(this, this.getPosition());

        // All 4 directions are blocked, do not move anywhere
        if (!possiblePositions.isEmpty()) {
            Random rand = new Random();
            Position randPosition = possiblePositions.get(rand.nextInt(possiblePositions.size()));
            this.setPosition(randPosition);
        }
    }
}
