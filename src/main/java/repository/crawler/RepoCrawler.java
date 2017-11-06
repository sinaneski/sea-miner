package repository.crawler;

import repository.crawler.exception.CrawlerException;
import repository.model.Commit;
import repository.model.Revision;
import repository.model.Revisions;

public interface RepoCrawler {

    void setCollectFileContentProperty(boolean value);

    void setCollectFileDiffProperty(boolean value);

    Revisions getRevisions() throws CrawlerException;

    Revision getLastRevision() throws CrawlerException;

    Commit getCommitAt(Revision revision) throws CrawlerException;

    void dispose();
}
