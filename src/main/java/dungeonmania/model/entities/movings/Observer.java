package dungeonmania.model.entities.movings;

public interface Observer {
    /**
     * Updates an Observer
     *
     * @param player subject
     */
    public void update(SubjectPlayer player);
}
