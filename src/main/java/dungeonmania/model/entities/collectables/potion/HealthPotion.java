package dungeonmania.model.entities.collectables.potion;

import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Position;

public class HealthPotion extends Potion {

    public HealthPotion(Position position) {
        super("health_potion", position);
    }

    @Override
    public void consume(Player player) {
        player.setHealth(Player.MAX_CHARACTER_HEALTH);
        player.removeInventoryItem(this.getId());
    }
}
