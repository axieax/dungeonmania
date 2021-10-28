package dungeonmania.model.entities.collectables.potion;

import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {
    private static final int MAX_USES = 3;
    private int usesLeft;

    public InvincibilityPotion(String entityId, Position position) {
        super(entityId, position);
        this.usesLeft = MAX_USES;
    }

    public InvincibilityPotion(String entityId, Position position, int maxPotionUses) {
        this(entityId, position);
        this.usesLeft = maxPotionUses;
    }

    @Override
    public void consume(Player player) {
        // TODO Auto-generated method stub

    }

    /**
     * Consumes the potion and reduces use by 1
     */
    public void usePotion() {
        this.usesLeft -= 1;
    }

    //////////////////////////////////////////////////////////////////////////////////
    public int getUsesLeft() {
        return usesLeft;
    }
}
