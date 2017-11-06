package repository.crawler.exception;

import org.tmatesoft.svn.core.SVNException;

public class SvnCrawlerException extends CrawlerException {
    private final SVNException svnException;
    public SvnCrawlerException(SVNException svnException) {
        this.svnException = svnException;
    }

    @Override
    public String getMessage() {
        return svnException.getMessage();
    }
}
