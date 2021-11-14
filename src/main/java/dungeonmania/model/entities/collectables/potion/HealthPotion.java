package dungeonmania.model.entities.collectables.potion;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;

public class HealthPotion extends Potion {

    public HealthPotion(Position position) {
        super("health_potion", position);
    }

    public void consume(Game game, Player player) {
        player.setHealth(player.getMaxCharacterHealth());
        player.removeInventoryItem(this.getId());
    }
}
