package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity implements Observer {
    public final static int MAX_MERCENARY_HEALTH = 50;
    public final static int MAX_MERCENARY_ATTACK_DMG = 5;

    private MercenaryState defaultState;
    private MercenaryState runState;
    private MercenaryState state;

    public Mercenary(String entityId, Position position) {
        this(entityId, position, MAX_MERCENARY_HEALTH, MAX_MERCENARY_ATTACK_DMG);
    }

    public Mercenary(String entityId, Position position, int health, int attackDamage) {
        super(entityId, position, health, attackDamage);
        this.defaultState = new MercenaryDefaultState(this);
        this.runState = new MercenaryRunState(this);

        this.state = defaultState;
    }

    @Override
    public void tick(Dungeon dungeon) {
        Position playerPos = dungeon.getCharacterPosition();
        state.move(dungeon, playerPos);
    }

    @Override
    public void update(SubjectPlayer player) { }

    /** 
     * Character attempts to bribe mercenary
     */
    @Override
    public void interact(Dungeon dungeon, MovingEntityBehaviour character) { 
        
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
