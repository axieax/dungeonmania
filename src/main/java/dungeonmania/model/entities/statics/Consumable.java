package dungeonmania.model.entities.statics;

import dungeonmania.model.entities.movings.Player;

public interface Consumable {
    /**
     * Apply any effects of any consumable item
     * @param item that is consumable
     */
    public void consume(Player player);
}
