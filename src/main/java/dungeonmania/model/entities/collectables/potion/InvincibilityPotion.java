package dungeonmania.model.entities.collectables.potion;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerInvincibleState;
import dungeonmania.model.mode.Hard;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {

    public InvincibilityPotion(Position position) {
        super("invincibility_potion", position);
    }

    @Override
    public void consume(Game game, Player player) {
        // Invincibility potions have no effect in hard mode
        if (!(game.getMode() instanceof Hard)) {
            player.setState(new PlayerInvincibleState(player));
        }

        player.removeInventoryItem(this.getId());
    }
}
