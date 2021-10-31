package dungeonmania.model.mode;

public class Peaceful implements Mode {
    @Override
    public int damageMultiplier() {
        return 0;
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
