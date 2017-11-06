package repository.filter;

import lombok.extern.slf4j.Slf4j;
import repository.model.ChangeItem;
import repository.model.Commit;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CrawlerFilter {
    private List<FileFilter> fileFilters = new ArrayList<>();
    private List<CommitFilter> commitFilters = new ArrayList<>();

    public boolean acceptCommit(Commit commit) {
        if(commit == null) {return false;}

        for (CommitFilter commitFilter : commitFilters) {
            if(!commitFilter.accept(commit)) {
                log.info("Commit <" + commit.getRevision() + "> filtered by " + commitFilter.getName() );
                return false;
            }
        }
        return true;
    }


    public boolean acceptFile(ChangeItem changeItem) {
        for (FileFilter fileFilter : fileFilters) {
            if(!fileFilter.accept(changeItem)) {
                log.info("File <" + changeItem.getPath() + "> at <" + changeItem.getRevision()+"> filtered by " + fileFilter.getName() );
                return false;
            }
        }
        return true;
    }

    public void add(CommitFilter commitFilter) {
        commitFilters.add(commitFilter);
    }

    public void add(FileFilter fileFilter) {
        fileFilters.add(fileFilter);
    }
}
