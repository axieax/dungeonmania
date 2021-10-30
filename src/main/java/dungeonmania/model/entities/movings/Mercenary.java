package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity implements Observer {
    public final static int MAX_MERCENARY_HEALTH = 50;
    public final static int MAX_MERCENARY_ATTACK_DMG = 5;

    private MercenaryState enemyState;
    private MercenaryState allyState;
    private MercenaryState state;

    
    public Mercenary(String entityId, Position position) {
        this(entityId, position, MAX_MERCENARY_HEALTH, MAX_MERCENARY_ATTACK_DMG);
    }

    public Mercenary(String entityId, Position position, int health, int attackDamage) {
        super(entityId, position, health, attackDamage);
        this.enemyState = new MercenaryEnemyState(this);
        this.allyState = new MercenaryAllyState(this);

        this.state = enemyState;
    }

    @Override
    public void tick(Dungeon dungeon) {
        Position playerPos = dungeon.getCharacterPosition();
    }

    @Override
    public void update(SubjectPlayer player) {
        // TODO: follow player
        
        // TODO:: If bribed (exists in player ally list) switch to Ally state
    }

    @Override
    public void interact(Dungeon dungeon, MovingEntityBehaviour character) { }

    ////////////////////////////////////////////////////////////////////////////////////
    public void setState(MercenaryState state) {
        this.state = state;
    }

    public MercenaryState getEnemyState() {
        return enemyState;
    }

    public MercenaryState getAllyState() {
        return allyState;
    }
}
