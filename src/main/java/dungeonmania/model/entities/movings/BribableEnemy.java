package dungeonmania.model.entities.movings;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;

public abstract class BribableEnemy extends Enemy {
    public static final int TREASURE_REQUIRED_TO_BRIBE = 1;
    private boolean bribed;

    public BribableEnemy(String prefix, Position position, int health, int attackDamage, int damageMultiplier) {
        super(prefix, position, health, attackDamage, damageMultiplier);
        this.bribed = false;
    }

    public boolean isBribed() {
        return bribed;
    }

    public void setBribed(boolean bribed) {
        this.bribed = bribed;
    }

    public abstract void bribe(Game game, Player player) throws InvalidActionException;
}
