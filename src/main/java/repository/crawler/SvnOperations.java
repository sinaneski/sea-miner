package repository.crawler;

import lombok.extern.slf4j.Slf4j;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.internal.wc2.ng.SvnDiffGenerator;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc2.SvnDiff;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;
import repository.model.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;

@Slf4j
public class SvnOperations {

    public Revision getLastRevision(SVNRepository svnRepository) throws SVNException {
        return Revision.fromLong(svnRepository.getLatestRevision());
    }

    public Revisions getRevisions(SVNRepository svnRepository) throws SVNException {
        Revisions revisions = new Revisions();
        long startRevision = 1;
        Collection logEntries;

        logEntries = getSvnLog(svnRepository, startRevision, svnRepository.getLatestRevision());

        for(Iterator<SVNLogEntry> entries = logEntries.iterator(); entries.hasNext();) {
            SVNLogEntry logEntry = entries.next();
            revisions.add(Revision.fromLong(logEntry.getRevision()));
        }

        return revisions;
    }

    private Collection getSvnLog(SVNRepository svnRepository, long startRevision, long endRevision) throws SVNException {
        return svnRepository.log(new String[]{""},
                null,
                startRevision,
                endRevision,
                true,
                true);
    }

    public Commit getCommitAt(SVNRepository svnRepository, Revision revision) throws SVNException {

        SVNLogEntry logEntry = getSVNLogEntryAt(svnRepository, revision.toLong());

        return logEntry == null ? null : convertSvnLogEntryToCommit(logEntry);
    }

    private SVNLogEntry getSVNLogEntryAt(SVNRepository svnRepository, long revision) throws SVNException {

        Collection logEntries = getSvnLog(svnRepository, revision, revision);

        Iterator<SVNLogEntry> iterator = logEntries.iterator();

        while(iterator.hasNext()) {
            return iterator.next();

        }
        return null;
    }

    private Commit convertSvnLogEntryToCommit(SVNLogEntry logEntry) {
        Revision revision = Revision.fromLong(logEntry.getRevision());

        Commit commit = new Commit();
        commit.setAuthor(logEntry.getAuthor());
        commit.setDate(logEntry.getDate());
        commit.setRevision(revision);
        commit.setMessage(logEntry.getMessage());

        return commit;
    }


    public List<ChangeItem> getChangedFileAt(SVNRepository svnRepository, Revision revision) throws SVNException {

        List<ChangeItem> changeItemList = new ArrayList<>();
        SVNLogEntry logEntry = getSVNLogEntryAt(svnRepository, revision.toLong());

        if(!hasChangedFile(logEntry)) {  return changeItemList;}

        Set changedPathsSet = logEntry.getChangedPaths().keySet();

        Iterator<SVNLogEntryPath> changedPaths = changedPathsSet.iterator();

        while (changedPaths.hasNext()) {

            SVNLogEntryPath entryPath =  logEntry.getChangedPaths().get(changedPaths.next());

            ChangeItem changeItem = svnLogEntryPathToChangeFile(revision, entryPath);
            if (changeItem == null) { continue; }
            changeItemList.add(changeItem);
        }

        return changeItemList;
    }


    private boolean hasChangedFile(SVNLogEntry logEntry) {
        return logEntry != null && logEntry.getChangedPaths().size() > 0;
    }

    private ChangeItem svnLogEntryPathToChangeFile(Revision revision, SVNLogEntryPath entryPath ) {

        ChangeItem changeItem = new ChangeItem(revision, entryPath.getPath());
        changeItem.setPath(entryPath.getPath());
        changeItem.setOldPath(entryPath.getCopyPath());
        long copyRevision = entryPath.getCopyRevision() > 0 ?  entryPath.getCopyRevision() :  revision.toLong() - 1;
        changeItem.setOldRevision(Revision.fromLong(copyRevision));
        changeItem.setChangeKind(typeToChangeKind(entryPath.getType()));
        changeItem.setItemKind(nodeKindToItemKind(entryPath.getKind()));

        return changeItem;
    }

    private  ChangeKind typeToChangeKind(char type) {

        switch (type) {
            case SVNLogEntryPath.TYPE_ADDED: {
                return ChangeKind.ADDED;
            }
            case SVNLogEntryPath.TYPE_DELETED: {
                return ChangeKind.DELETED;
            }
            case SVNLogEntryPath.TYPE_MODIFIED: {
                return ChangeKind.MODIFIED;
            }
            case SVNLogEntryPath.TYPE_REPLACED: {
                return ChangeKind.RENAMED;
            }
            default: return ChangeKind.UNKNOWN;
        }
    }

    private ItemKind nodeKindToItemKind(SVNNodeKind kind) {
        if(kind.equals(SVNNodeKind.NONE)) {
            return ItemKind.NONE;
        }

        if(kind.equals(SVNNodeKind.FILE)) {
            return ItemKind.FILE;
        }

        if(kind.equals(SVNNodeKind.DIR)) {
            return ItemKind.DIR;
        }

        return ItemKind.UNKNOWN;
    }


    public String getFileDiffContent(SvnOperationFactory svnOperationFactory, String path, Revision oldRevision, Revision newRevision) throws SVNException {

        SVNURL fileUrl = SVNURL.parseURIEncoded(path);
        return getFileDiffContent(svnOperationFactory, fileUrl, oldRevision, newRevision);
    }

    public String getFileDiffContent(SvnOperationFactory svnOperationFactory,SVNURL fileUrl , Revision oldRevision, Revision newRevision) throws SVNException {

        SVNRevision svnRevision = SVNRevision.create(oldRevision.toLong());
        SVNRevision svnRevision2 = SVNRevision.create(newRevision.toLong());
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final SvnDiffGenerator diffGenerator = new SvnDiffGenerator();
        diffGenerator.setBasePath(new File(""));
        final SvnDiff diff = svnOperationFactory.createDiff();
        diff.setSources(SvnTarget.fromURL(fileUrl, svnRevision), SvnTarget.fromURL(fileUrl, svnRevision2));
        diff.setDiffGenerator(diffGenerator);
        diff.setOutput(byteArrayOutputStream);
        diff.run();

        final String actualDiffOutput = new String(byteArrayOutputStream.toByteArray());

        return actualDiffOutput;
    }

    public String getFileContent(SVNRepository svnRepository, String filename, Revision revision) throws SVNException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        svnRepository.getFile(filename, revision.toLong(), null, byteArrayOutputStream);
        final String output = new String(byteArrayOutputStream.toByteArray());
        return output;
    }
}
