package dungeonmania.model.entities.movings;

import java.util.List;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity implements Observer {
    final static int MAX_ZOMBIE_HEALTH = 20;
    final static int MAX_ZOMBIE_ATTACK_DMG = 2;

    public final double ARMOUR_DROP_RATE = 0.2;

    private ZombieState randomZombieState;
    private ZombieState runZombieState;
    private ZombieState state;

    public ZombieToast(Position position, int damageMultiplier) {
        super("zombie_toast", position, MAX_ZOMBIE_HEALTH, MAX_ZOMBIE_ATTACK_DMG, true, damageMultiplier);
    }

    public ZombieToast(Position position, int damageMultiplier, SubjectPlayer player) {
        this(position, damageMultiplier);
        player.attach(this);
    }
    

    @Override
    public void tick(Game game) {
        state.move(game);
    }

    /**
     * Determines if a Zombie can move onto a position, given entities at that position
     * @param entitiesNewPos list of entities at new position
     * @return true if Zombie can move onto that tile, else false
     */
    public boolean canZombieMoveOntoPosition(List<Entity> entitiesNewPos) {
        for(Entity e: entitiesNewPos) {
            if(e.getId() == "portal") {
                // Portals have no effect on zombies
                continue;
            }
            
            if(!e.isPassable()) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * If a player drinks an invincibility potion, change the state
     * of the zombie to make sure it runs away
     */
    @Override
    public void update(SubjectPlayer player) {
        if(!(player instanceof Player)) {
            return;
        }

        Player character = (Player) player; 
        if(character.getState() instanceof PlayerInvincibleState) {
            this.setState(getRunZombieState());
        } else {
             this.setState(randomZombieState);
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
    public void setState(ZombieState state) {
        this.state = state;
    }

    public ZombieState getState() {
        return state;
    }

    public ZombieState getRandomZombieState() {
        return randomZombieState;
    }

    public ZombieState getRunZombieState() {
        return runZombieState;
    }
}
