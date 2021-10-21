package dungeonmania.model.entities.movingEntity;

public interface Battle {

    public void battle(MovingEntity one, MovingEntity two);
    // Character Health = Character Health - ((Enemy Health * Enemy Attack Damage) / 10)
    // Enemy Health = Enemy Health - ((Character Health * Character Attack Damage) / 5)

}
