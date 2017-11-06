package repository.model;


public enum ChangeKind {
    ADDED (1, "Added"),
    DELETED	(2, "Deleted"),
    RENAMED (3, "Renamed"),
    MODIFIED (4, "Modified"),
    MOVED (5, "Moved"),
    UNKNOWN (-1, "Unknown");

    private int code;
    private String description;

    ChangeKind(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
