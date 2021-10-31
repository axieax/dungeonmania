package dungeonmania.model.mode;

public class Hard implements Mode{
    @Override
    public int damageMultiplier() {
        return 1;
    }

    @Override
    public int tickRate() {
        return 15;
    }

    @Override
    public int initialHealth() {
        return 50;
    }

    @Override
    public int invincibilityPotionMultipler() {
        return 0;
    } 
}
