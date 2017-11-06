package repository.filter;

import repository.model.Commit;

public interface CommitFilter {
    boolean accept(Commit commit);
    String getName();
}
