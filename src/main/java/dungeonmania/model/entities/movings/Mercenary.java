package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity implements Observer {
    public final static int MAX_MERCENARY_HEALTH = 50;
    public final static int MAX_MERCENARY_ATTACK_DMG = 5;
    public final static int TREASURE_REQUIRED_TO_BRIBE = 1;

    public final double ARMOUR_DROP_RATE = 0.2;

    private MercenaryState defaultState;
    private MercenaryState runState;
    private MercenaryState state;

    public Mercenary(Position position) {
        this(position, MAX_MERCENARY_HEALTH, MAX_MERCENARY_ATTACK_DMG);
    }

    public Mercenary(Position position, int health, int attackDamage) {
        super("mercenary", position, health, attackDamage, true);
        this.defaultState = new MercenaryDefaultState(this);
        this.runState = new MercenaryRunState(this);

        this.state = defaultState;
    }

    @Override
    public void tick(Game game) {
        Position playerPos = game.getCharacterPosition();
        state.move(game, playerPos);
    }

    /**
     * Change state if player is invincible and mercenary not ally of player
     */
    @Override
    public void update(SubjectPlayer player) { 
        // TODO: If a player is fighting an enemy, mercenary moves twice as fast to take advantage
    }

    /** 
     * Character attempts to bribe mercenary
     */
    @Override
    public void interact(Game game, MovingEntity character) { 
        Player player = (Player) character;

        if(player.getTreasure() >= TREASURE_REQUIRED_TO_BRIBE) {
            player.addAlly(this);
        }
    }

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
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    public void setState(MercenaryState state) {
        this.state = state;
    }

    public MercenaryState getDefaultState() {
        return defaultState;
    }

    public MercenaryState getRunState() {
        return runState;
    }
}
