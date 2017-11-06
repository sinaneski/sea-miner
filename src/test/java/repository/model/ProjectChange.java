package repository.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
public class ProjectChange {

    private List<Commit> commits = new ArrayList<>();
    private List<Revision> revisions = new ArrayList<>();
    private Map<String, Commit> filenameRevisionMap = new HashMap<>();
    private Map<String, Commit> authorRevisionMap = new HashMap<>();


    public void add(Commit commit) {
        addRevision(commit);
        addCommit(commit);
        addAuthorCommit(commit);
        addFileCommit(commit);
    }

    private void addCommit(Commit commit) {
        commits.add(commit);
    }

    public void addRevision(Commit commit) {
        revisions.add(commit.getRevision());
    }


    public void addFileCommit(Commit commit) {
        if(commit == null) {
            return;
        }
        List<ChangeItem> files= commit.getChangeItemList();

        for(ChangeItem file : files) {
           filenameRevisionMap.put(file.getPath(), commit);
        }
    }

    public void addAuthorCommit(Commit commit) {
        if(commit == null) {
            return;
        }
        authorRevisionMap.put(commit.getAuthor(), commit);
    }
}
