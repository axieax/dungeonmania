package dungeonmania.model.entities.collectables.potion;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;

public class HealthPotion extends Potion {

    public HealthPotion(Position position) {
        super("health_potion", position);
    }

    @Override
    public void consume(Game game, Player player) {
        player.setHealth(Player.MAX_CHARACTER_HEALTH);
        player.removeInventoryItem(this.getId());
    }
}
