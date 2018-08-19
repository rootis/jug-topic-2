package eu.eagercode.jugbootwebflux.model;

public class Topic {

    private final String title;
    private final String description;

    public Topic(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
