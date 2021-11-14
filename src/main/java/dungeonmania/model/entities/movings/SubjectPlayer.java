package dungeonmania.model.entities.movings;

public interface SubjectPlayer {

    /**
     * Attach a given observer
     *
     * @param observer observer to attach
     */
    public void attach(Observer observer);

    /**
     * Remove observers from the subject
     */
    public void removeObservers();

    /**
     * Notify observers of the subject
     */
    public void notifyObservers();
}
