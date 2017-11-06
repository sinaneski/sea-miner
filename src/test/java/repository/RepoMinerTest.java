package repository;

import org.junit.Assert;
import org.junit.Test;
import repository.crawler.RepoCrawler;
import repository.crawler.SvnCrawler;
import repository.crawler.exception.CrawlerException;
import repository.model.ProjectChange;
import repository.model.Revision;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RepoMinerTest {

    @Test
    public void extract_ShouldReturn7Commits_WhenMinerFrom1to7() throws CrawlerException {

        RepoCrawler crawler = SvnCrawler.openConnection(getSvnTestRepoUrl());

        RepoMiner miner = new RepoMiner.Builder(crawler)
                .from(Revision.fromLong(1))
                .to(Revision.fromLong(7))
                .build();

        ProjectChange change = miner.extract();

        miner.dispose();
        Assert.assertEquals(7, change.getRevisions().size());
        Assert.assertEquals(7, change.getCommits().size());
    }

    private String getSvnTestRepoUrl() {
        Path resourceDirectory = Paths.get("src/test/resources/svntest-repo");
        return "file://" + resourceDirectory.toAbsolutePath();
    }

    @Test
    public void extract_ShouldReturn4Commits_WhenMinerFrom4to7() throws CrawlerException {

        RepoCrawler crawler = SvnCrawler.openConnection(getSvnTestRepoUrl());

        RepoMiner miner = new RepoMiner.Builder(crawler)
                .from(Revision.fromLong(4))
                .to(Revision.fromLong(7))
                .build();

        ProjectChange change = miner.extract();
        miner.dispose();
        Assert.assertEquals(4, change.getRevisions().size());
        Assert.assertEquals(4, change.getCommits().size());
    }
}