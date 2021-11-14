package dungeonmania.model.entities.collectables.potion;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerInvisibleState;
import dungeonmania.util.Position;

public class InvisibilityPotion extends Potion {

    public InvisibilityPotion(Position position) {
        super("invisibility_potion", position);
    }

    public void consume(Game game, Player player) {
        player.setState(new PlayerInvisibleState(player));
        player.removeInventoryItem(this.getId());
    }
}
