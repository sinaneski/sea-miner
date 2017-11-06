package repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ChangeItem {
    private String id;
    private ItemKind itemKind;
    private ChangeKind changeKind;
    private String path;
    private String oldPath;
    private Revision revision;
    private Revision oldRevision;
    private FileDiff diff;

    @JsonIgnore
    private String content;

    public ChangeItem(Revision revision, String path) {
        this.itemKind = ItemKind.FILE;
        this.revision = revision;
        this.path = path;
        this.id= path;
    }

    public boolean isModified() {
        return changeKind.equals(ChangeKind.MODIFIED);
    }

    public boolean isDeleted() {
        return changeKind.equals(ChangeKind.DELETED);
    }
}
