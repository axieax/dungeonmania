package dungeonmania.model.entities.movings;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.player.Player;

public interface Bribable {
    public static final int TREASURE_REQUIRED_TO_BRIBE = 1;

    public void bribe(Game game, Player player) throws InvalidActionException;

    public void consume(Player player, Item item);
}
