package dungeonmania.model.entities.movings;

public interface SubjectPlayer {
    public void attach(Observer observer);

    public void removeObservers();

    public void notifyObservers();
}
