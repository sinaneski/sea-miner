package repository;

import repository.crawler.RepoCrawler;
import repository.crawler.exception.CrawlerException;
import repository.model.Commit;
import repository.model.ProjectChange;
import repository.model.Revision;

import java.util.List;

public class RepoMiner {

    private RepoCrawler crawler;
    private Revision startRevision;
    private Revision endRevision;

    private RepoMiner(RepoCrawler crawler, Revision startRevision, Revision endRevision) {
        this.crawler = crawler;
        this.startRevision = startRevision;
        this.endRevision = endRevision;
    }

    public ProjectChange extract() {
        ProjectChange projectChange = new ProjectChange();

        try {
            List<Revision> revisions = crawler.getRevisions().between(startRevision, endRevision);

            for(Revision revision : revisions) {
                Commit commit = crawler.getCommitAt(revision);
                if(commit == null) { continue; }
                projectChange.add(commit);
            }

        } catch (CrawlerException e) {
            e.printStackTrace();
        }

        return projectChange;
    }

    public void dispose() {
        crawler.dispose();
    }


    public static class Builder {
        private RepoCrawler crawler;
        private Revision startRevision;
        private Revision endRevision;

        public Builder(RepoCrawler crawler) {
            this.crawler = crawler;
            startRevision = Revision.NONE;
            endRevision = Revision.NONE;
        }

        public Builder from(Revision revision) {
            this.startRevision = revision;
            return this;
        }

        public Builder to(Revision revision) {
            this.endRevision = revision;
            return this;
        }

        public RepoMiner build() {
            return new RepoMiner(crawler, startRevision, endRevision);
        }
    }
}
