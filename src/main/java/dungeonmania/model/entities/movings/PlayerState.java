package dungeonmania.model.entities.movings;

public interface PlayerState {
    public void battle(MovingEntity opponent);

    public void updateState(Player player);
}