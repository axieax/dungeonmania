package dungeonmania.model.mode;

public interface Mode {
    
    /**
     * @return the damage multipler of the mode
     */
    default int damageMultiplier() {
        return 1;
    }

    /**
     * @return the tick rate of zombie toast spawner
     */
    default int tickRate() {
        return 20;

    }

    /**
     * @return gets the initial health of the player
     */
    default int initialHealth() {
        return 100;
    }

    /**
     * @return the invincibility potion multiplier
     */
    default int invincibilityPotionMultipler() {
        return 1;

    }
}
