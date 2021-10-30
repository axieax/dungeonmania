package dungeonmania.model.entities.collectables.potion;

import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.movings.PlayerInvincibleState;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {


    public InvincibilityPotion(String entityId, Position position) {
        super(entityId, position);
    }

    @Override
    public void consume(Player player) {
        player.setState(new PlayerInvincibleState(player));
        player.removeInventoryItem(this.getId());
    }

}
