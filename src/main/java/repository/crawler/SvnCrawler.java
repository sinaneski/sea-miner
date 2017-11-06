package repository.crawler;

import lombok.extern.slf4j.Slf4j;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import repository.crawler.exception.CrawlerException;
import repository.crawler.exception.SvnCrawlerException;
import repository.filter.CommitFilter;
import repository.filter.CrawlerFilter;
import repository.filter.FileFilter;
import repository.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SvnCrawler implements RepoCrawler {

    private final String url;
    private final SvnOperationFactory svnOperationFactory;
    private final SVNRepository svnRepository;

    private CrawlerFilter filter = new CrawlerFilter();
    private boolean collectFileContentProperty = false;
    private boolean collectFileDiffProperty = true;

    SvnOperations svnOperations = new SvnOperations();

    public static SvnCrawler openConnection(String svnURL) throws CrawlerException {
        return openConnection(svnURL, "anonymous", "anonymous".toCharArray());
    }

    public static SvnCrawler openConnection(String svnURL, String name, char [] password) throws CrawlerException {
        final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
        SVNRepository svnRepository;

        try {
            SVNURL svnurl = SVNURL.parseURIEncoded(svnURL);
            svnRepository = svnOperationFactory.getRepositoryPool().createRepository(svnurl, true);
        }
        catch (SVNException e) {
            log.error(e.getMessage());
            throw new SvnCrawlerException(e);
        }

        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
        svnRepository.setAuthenticationManager(authManager);
        return new SvnCrawler(svnOperationFactory, svnRepository, svnURL);
    }

    private SvnCrawler(SvnOperationFactory svnOperationFactory, SVNRepository svnRepository, String svnURL) {
        this.svnOperationFactory = svnOperationFactory;
        this.svnRepository = svnRepository;
        this.url = svnURL;
    }

    public void dispose() {
        svnRepository.closeSession();
        svnOperationFactory.dispose();
    }


    public Revisions getRevisions() throws CrawlerException {
        try {
           return svnOperations.getRevisions(svnRepository);
        }
        catch (SVNException e) {
            log.error(e.getMessage());
            throw new SvnCrawlerException(e);
        }
    }

    public Revision getLastRevision() throws CrawlerException {
        try {
            return svnOperations.getLastRevision(svnRepository);
        }
        catch (SVNException e) {
            log.error(e.getMessage());
            throw new SvnCrawlerException(e);
        }
    }

    public Commit getCommitAt(Revision revision) throws CrawlerException {

        try {
            Commit commit = svnOperations.getCommitAt(svnRepository, revision);

            List<ChangeItem> files = getChangedFiles(revision);
            commit.setChangeItemList(files);

            return filter.acceptCommit(commit) ? commit : null;
        }
        catch (SVNException e) {
            log.error(e.getMessage());
            throw new SvnCrawlerException(e);
        }
    }

    private List<ChangeItem> getChangedFiles(Revision revision) throws SVNException {
        List<ChangeItem> files = svnOperations.getChangedFileAt(svnRepository, revision)
                .stream().filter(f -> filter.acceptFile(f)).collect(Collectors.toList());

        for (ChangeItem c: files ) {
            c.setDiff(getFileDiff(c));
            c.setContent(getContent(c));
        }
        return files;
    }

    private FileDiff getFileDiff(ChangeItem changeItem) {

        if (!isCollectFileDiffProperty() || !changeItem.isModified()) {
            return FileDiff.empty;
        }
        String diffContent = "";
        try {
            diffContent = svnOperations.getFileDiffContent(svnOperationFactory, url + changeItem.getPath(), changeItem.getRevision(), changeItem.getOldRevision());
        }
        catch (SVNException e) {
            log.error("getChangedFileDiffContent" +  e.getMessage());
        }

        return FileDiff.fromContent(diffContent);
    }

    private String getContent(ChangeItem changeItem)  {

        String content = "";

        if (!isCollectFileContentProperty()
                || changeItem.getItemKind().equals(ItemKind.FILE)
                || changeItem.isDeleted()) {
            return content;
        }

        try {
            content = svnOperations.getFileContent(svnRepository, changeItem.getPath(), changeItem.getRevision());
        }
        catch (SVNException e) {
            log.error(e.getMessage());
        }
        return  content;
    }

    @Override
    public void setCollectFileContentProperty(boolean value) {
        collectFileContentProperty = value;
    }

    @Override
    public void setCollectFileDiffProperty(boolean value) {
        collectFileDiffProperty = value;
    }

    @Override
    public void addFilter(CommitFilter commitFilter) {
        filter.add(commitFilter);
    }

    @Override
    public void addFilter(FileFilter fileFilter) {
        filter.add(fileFilter);
    }

    public boolean isCollectFileContentProperty() {
        return collectFileContentProperty;
    }

    public boolean isCollectFileDiffProperty() {
        return collectFileDiffProperty;
    }

}
