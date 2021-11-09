package dungeonmania.model.entities.movings;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.collectables.TheOneRing;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.movings.movement.AttackMovementState;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;

public class Assassin extends BribableEnemy {
    
    public static final int MAX_ASSASSIN_HEALTH = 60;
    public static final int MAX_ASSASSIN_ATTACK_DMG = 12;

    public Assassin(Position position, int damageMultiplier, SubjectPlayer player) {
        super("assassin", position, MAX_ASSASSIN_HEALTH, MAX_ASSASSIN_ATTACK_DMG, damageMultiplier);
        this.setMovementState(new AttackMovementState(this));
        player.attach(this);
    }

    /**
     * Player attempting to bribe the assassin upon interaction
     */
    @Override
    public void bribe(Game game, Player player) throws InvalidActionException {
        // Player must be within 2 cardinal tiles to the assassin and 
        // have 1 treasure (gold) and TheOneRing in order to bribe the assassin
        Item item = player.findInventoryItem("treasure");
        Item ring = player.findInventoryItem("one_ring");
        if (item != null && ring != null) {
            if (getDistanceToPlayer(game, player.getPosition()) <= MAX_DISTANCE_TO_BRIBE) {
                player.addAlly(this);
                ((Treasure) item).consume(game, player);
                ((TheOneRing) ring).consume(game, player);
            } else {
                throw new InvalidActionException("You are too far away to bribe this assassin");
            }
        } else {
            throw new InvalidActionException("You need both treasure and TheOneRing to bribe this assassin");
        }
    }
}
