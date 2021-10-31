package dungeonmania.model.entities.movings;

import java.util.List;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity implements Observer {
    final static int MAX_ZOMBIE_HEALTH = 20;
    final static int MAX_ZOMBIE_ATTACK_DMG = 2;

    private EnemyMovementState defaultState;
    private EnemyMovementState runState;
    private EnemyMovementState state;
    public final double ARMOUR_DROP_RATE = 0.2;

    public ZombieToast(Position position, SubjectPlayer player) {
        this(position, MAX_ZOMBIE_HEALTH, MAX_ZOMBIE_ATTACK_DMG, player);
    }
    
    public ZombieToast(Position position, int health, int attackDamage, SubjectPlayer player) {
        super("zombie_toast", position, health, attackDamage, true);
        this.defaultState = new ZombieDefaultState(this);
        this.runState = new ZombieRunState(this);

        this.state = defaultState;
        player.attach(this);
    }

    @Override
    public void tick(Game game) {
        Position playerPos = game.getCharacter().getPosition();
        state.move(game, playerPos);
    }

    /**
     * Determines if a Zombie can move onto a position, given entities at that position
     * @param entitiesNewPos list of entities at new position
     * @return true if Zombie can move onto that tile, else false
     */
    public boolean canZombieMoveOntoPosition(List<Entity> entitiesNewPos) {
        for(Entity entity: entitiesNewPos) {
            // Portals have no effect on zombies
            if (entity.getId().equals("portal")) continue;
            
            if (!entity.isPassable()) return false;
        }
        return true;
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
            this.setState(getRunState());
        } else {
            this.setState(getDefaultState());
        }
    }

    @Override
    public void interact(Game game, MovingEntity character) { }

    @Override
    public Direction getDirection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean collision(Entity entity) {
        // TODO Auto-generated method stub
        return false;
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
    public void setState(EnemyMovementState state) {
        this.state = state;
    }

    public EnemyMovementState getState() {
        return state;
    }

    public EnemyMovementState getDefaultState() {
        return defaultState;
    }

    public EnemyMovementState getRunState() {
        return runState;
    }
}
