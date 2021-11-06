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
    private MovementState state;

    public ZombieToast(Position position, int damageMultiplier, SubjectPlayer player) {
        super(
            "zombie_toast",
            position,
            MAX_ZOMBIE_HEALTH,
            MAX_ZOMBIE_ATTACK_DMG,
            true,
            damageMultiplier
        );
        this.state = new DefaultState(this);
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
    public void moveTo(Position position) {
        this.setPosition(position);
    }

    //////////////////////////////////////////////////////////////////
    public void setState(MovementState state) {
        this.state = state;
    }

    public void move(Game game, Position playerPos) {
        List<Position> possiblePositions = game.getMoveablePositions(this, this.getPosition());

        // All 4 directions are blocked, do not move anywhere
        if (!possiblePositions.isEmpty()) {
            Random rand = new Random();
            Position randPosition = possiblePositions.get(rand.nextInt(possiblePositions.size()));
            Position offset = Position.calculatePositionBetween(this.getPosition(), randPosition);
            if (Direction.LEFT.getOffset().equals(offset)) this.setDirection(Direction.LEFT);
            else if (Direction.UP.getOffset().equals(offset)) this.setDirection(Direction.UP);
            else if (Direction.RIGHT.getOffset().equals(offset)) this.setDirection(Direction.RIGHT);
            else if (Direction.DOWN.getOffset().equals(offset)) this.setDirection(Direction.DOWN);
            this.setPosition(randPosition);
        }
        else this.setDirection(Direction.NONE);
    }
}
