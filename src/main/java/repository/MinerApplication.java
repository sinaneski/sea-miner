package repository;

import lombok.extern.slf4j.Slf4j;
import repository.crawler.RepoCrawler;
import repository.crawler.SvnCrawler;
import repository.crawler.exception.CrawlerException;
import repository.filter.CommitHasChangedFile;
import repository.filter.FileSourceCodeFilter;
import repository.io.ProjectCommitJsonWriter;
import repository.model.ProjectChange;
import repository.model.Revision;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class MinerApplication {

    public static void main(String[] args) {
        MinerApplication minerApplication = new MinerApplication();
        try {
            minerApplication.run();
        }
        catch (CrawlerException e) {
            log.error(e.getMessage());
        }
    }

    public void run() throws CrawlerException {
        RepoCrawler crawler = SvnCrawler.openConnection(getSvnTestRepoUrl());

        crawler.setCollectFileContentProperty(true);
        crawler.setCollectFileDiffProperty(true);

        crawler.addFilter(new FileSourceCodeFilter());
        crawler.addFilter(new CommitHasChangedFile());

        RepoMiner miner = new RepoMiner.Builder(crawler)
                .from(Revision.fromLong(1))
                .to(Revision.fromLong(7))
                .build();
        ProjectChange change = miner.extract();

        crawler.dispose();

        ProjectCommitJsonWriter projectCommitJsonWriter = new ProjectCommitJsonWriter("commits.json");
        projectCommitJsonWriter.write(change);
    }

    private String getSvnTestRepoUrl() {
        Path resourceDirectory = Paths.get("src/test/resources/svntest-repo");
        return "file://" + resourceDirectory.toAbsolutePath();
    }

}