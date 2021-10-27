package dungeonmania.model.entities.movings;

public class InvisibleState implements PlayerState {
    Player player;

    public InvisibleState(Player player) {
        this.player = player;
    }

    @Override
    public void battle(MovingEntity opponent) {
        // TODO Auto-generated method stub
        
    }
    
}
