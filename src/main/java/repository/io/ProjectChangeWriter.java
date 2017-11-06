package repository.io;

import repository.model.ProjectChange;

public interface ProjectChangeWriter {
    void write(final ProjectChange projectChange);
}
