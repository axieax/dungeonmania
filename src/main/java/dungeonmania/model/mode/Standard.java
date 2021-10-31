package dungeonmania.model.mode;

public class Standard implements Mode {
    @Override
    public int damageMultiplier() {
        return 1;
    }

    @Override
    public int tickRate() {
        return 20;
    }

    @Override
    public int initialHealth() {
        return 100;
    }

    @Override
    public int invincibilityPotionMultipler() {
        return 1;
    }
}
