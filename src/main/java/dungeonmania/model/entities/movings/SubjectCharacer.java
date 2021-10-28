package dungeonmania.model.entities.movings;

public interface SubjectCharacer {
    public void attach(Observer observer);

    public void detach(Observer observer);

    public void notifyObservers();
}
