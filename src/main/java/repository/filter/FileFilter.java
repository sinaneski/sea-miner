package repository.filter;

import repository.model.ChangeItem;

public interface FileFilter {
    boolean accept(ChangeItem file);
    String getName();
}
