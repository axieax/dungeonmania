package dungeonmania.model.mode;

public interface Mode {
    /**
     * Get the damage multiplier defined by a mode
     *
     * @return damage multiplier
     */
    default int damageMultiplier() {
        return 1;
    }

    /**
     * Get the tick rate defined by a mode
     *
     * @return tick rate for zombie toast spawner and spider spawning
     */
    default int tickRate() {
        return 20;
    }

    /**
     * Get the initial health
     *
     * @return initial health of the player
     */
    default int initialHealth() {
        return 100;
    }
}
