package repository.model;

import java.util.ArrayList;
import java.util.List;

public class Revisions {
    private List<Revision> revisionList = new ArrayList<>();

    public void add(Revision revision) {
        revisionList.add(revision);
    }

    public List<Revision> all() {
        return revisionList;
    }

    public List<Revision> between(Revision start, Revision end) {
        boolean started = false;
        List<Revision> filtered = new ArrayList<>();
        if(Revision.NONE.equals(start)) { started = true; }

        for(Revision revision : revisionList) {
            if(revision.equals(start)) { started = true; }
            if(started) { filtered.add(revision); }
            if(started && revision.equals(end)) { return filtered; }
        }
        return filtered;
    }
}
