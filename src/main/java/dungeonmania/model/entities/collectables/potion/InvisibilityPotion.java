package dungeonmania.model.entities.collectables.potion;

import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.movings.PlayerInvisibleState;
import dungeonmania.util.Position;

public class InvisibilityPotion extends Potion {

    public InvisibilityPotion(String entityId, Position position) {
        super(entityId, position);
    }

    @Override
    public void consume(Player player) {
        player.setState(new PlayerInvisibleState(player));
        player.removeInventoryItem(this.getId());
    }
}
