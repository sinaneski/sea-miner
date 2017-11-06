package repository.filter;

import repository.model.Commit;

public class CommitHasChangedFile implements CommitFilter {

    @Override
    public boolean accept(Commit commit) {
        if(commit == null) return false;

        return commit.getChangeItemList() != null && commit.getChangeItemList().size() > 0;
    }

    @Override
    public String getName() {
        return "CommitHasChangedFile";
    }
}
