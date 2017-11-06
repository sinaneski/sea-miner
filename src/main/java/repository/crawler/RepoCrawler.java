package repository.crawler;

import repository.crawler.exception.CrawlerException;
import repository.filter.CommitFilter;
import repository.filter.FileFilter;
import repository.model.Commit;
import repository.model.Revision;
import repository.model.Revisions;

public interface RepoCrawler {

    void setCollectFileContentProperty(boolean value);

    void setCollectFileDiffProperty(boolean value);

    void addFilter(FileFilter fileFilter);

    void addFilter(CommitFilter commitFilter);

    Revisions getRevisions() throws CrawlerException;

    Revision getLastRevision() throws CrawlerException;

    Commit getCommitAt(Revision revision) throws CrawlerException;
}
