package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity implements Observer {
    public final static int MAX_MERCENARY_HEALTH = 50;
    public final static int MAX_MERCENARY_ATTACK_DMG = 5;
    public final static int TREASURE_REQUIRED_TO_BRIBE = 1;
    
    private EnemyMovementState defaultState;
    private EnemyMovementState runState;
    private EnemyMovementState state;

    public Mercenary(Position position) {
        this(position, MAX_MERCENARY_HEALTH, MAX_MERCENARY_ATTACK_DMG);
    }

    public Mercenary(Position position, int health, int attackDamage) {
        super("mercenary", position, health, attackDamage);
        this.defaultState = new MercenaryDefaultState(this);
        this.runState = new MercenaryRunState(this);

        this.state = defaultState;
    }

    @Override
    public void tick(Game game) {
        Position playerPos = game.getCharacter().getPosition();
        state.move(game, playerPos);
    }

    /**
     * If a player drinks an invincibility potion, and mercenary is not an ally,
     * change the state of the mercenary to make sure it runs away
     */
    @Override
    public void update(SubjectPlayer player) { 
        if (!(player instanceof Player)) {
            return;
        }

        Player character = (Player) player; 
        if (character.getState() instanceof PlayerInvincibleState && character.getAllies() == null) {
            this.setState(getRunState());
        } else {
            this.setState(getDefaultState());
        }
        
        // TODO: If a player is fighting an enemy, mercenary moves twice as fast to take advantage
    }

    /** 
     * Character attempts to bribe mercenary
     */
    @Override
    public void interact(Game game, MovingEntity character) { 
        Player player = (Player) character;
        if (player.getInventory().hasItemQuantity("treasure", TREASURE_REQUIRED_TO_BRIBE)) {
            player.addAlly(this);
        }
    }

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
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    public void setState(EnemyMovementState state) {
        this.state = state;
    }

    public EnemyMovementState getDefaultState() {
        return defaultState;
    }

    public EnemyMovementState getRunState() {
        return runState;
    }
}
