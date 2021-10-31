package dungeonmania.model.mode;

public class Hard implements Mode{
    @Override
    public int tickRate() {
        return 15;
    }

    @Override
    public int initialHealth() {
        return 80;
    }

    @Override
    public int invincibilityPotionMultipler() {
        return 0;
    } 
}
