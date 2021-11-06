package dungeonmania.model.entities.movings;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;

public abstract class BribableEnemy extends Enemy{
    public static final int TREASURE_REQUIRED_TO_BRIBE = 1;
    private boolean isBribed;

    public BribableEnemy(String prefix, Position position, int health, int attackDamage, int damageMultiplier) {
        super(prefix, position, health, attackDamage, damageMultiplier);
        this.isBribed = false;
    }

    public boolean isBribed() {
        return isBribed;
    }

    public void setBribed(boolean isBribed) {
        this.isBribed = isBribed;
    }

    public abstract void bribe(Game game, Player player) throws InvalidActionException;

    public abstract void consume(Player player, Item item);
}
