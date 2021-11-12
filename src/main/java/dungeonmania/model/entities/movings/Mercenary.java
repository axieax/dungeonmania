package dungeonmania.model.entities.movings;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.movings.movement.AttackMovementState;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.util.Position;

public class Mercenary extends BribableEnemy {
    
    public static final int MAX_MERCENARY_HEALTH = 50;
    public static final int MAX_MERCENARY_ATTACK_DMG = 5;
    

    public Mercenary(Position position, int damageMultiplier, SubjectPlayer player) {
        super("mercenary", position, MAX_MERCENARY_HEALTH, MAX_MERCENARY_ATTACK_DMG, damageMultiplier);
        this.setMovementState(new AttackMovementState(this));
        player.attach(this);
    }

    /**
     * Player attempting to bribe the mercenary upon interaction
     */
    @Override
    public void bribe(Game game, Player player) throws InvalidActionException {
        // Player must be within 2 cardinal tiles to the mercenary and 
        // have 1 treasure (gold) in order to bribe the mercenary
        if (getDistanceToPlayer(game, player.getPosition()) > MAX_DISTANCE_TO_BRIBE)
            throw new InvalidActionException("You are too far away to bribe this mercenary");

        Item sunstone = player.findInventoryItem("sun_stone");
        Item treasure = player.findInventoryItem("treasure");
        
        if (sunstone == null && treasure == null)
            throw new InvalidActionException("You don't have enough treasure to bribe this mercenary");
        
        player.addAlly(this);
        if (sunstone != null) player.removeInventoryItem(sunstone.getId());
        else ((Treasure) treasure).consume(game, player);
    }

    /**
     * Mercenary is allowed to pass through portals
     */
    @Override
    public boolean collision(Entity entity) {
        if (entity instanceof Portal) return false;
        return !entity.isPassable();
    }
}
