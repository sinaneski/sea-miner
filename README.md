# SEA-Miner (Software Evolution Analysis - Miner)

[![Build Status](https://travis-ci.org/sinaneski/sea-miner.svg?branch=master)](https://travis-ci.org/sinaneski/sea-miner)

SEA-Miner is a Java project to mine software repositories.

## General Information

RepoCrawler:

```java
 public interface RepoCrawler {
 
     void setCollectFileContentProperty(boolean value);
 
     void setCollectFileDiffProperty(boolean value);
 
     void addFilter(FileFilter fileFilter);
 
     void addFilter(CommitFilter commitFilter);
 
     Revisions getRevisions() throws CrawlerException;
 
     Revision getLastRevision() throws CrawlerException;
 
     Commit getCommitAt(Revision revision) throws CrawlerException;
 
     void dispose();
 }
```

SvnCrawler is a RepoCrawler. Crawl Commit information from SVN repositories.

```java
 RepoCrawler crawler = SvnCrawler.openConnection(getSvnTestRepoUrl());
 RepoCrawler crawler2 = SvnCrawler.openConnection(getSvnTestRepoUrl(), "name", "password");

```

You can change file content and diff collection properties for crawl operation.

```java
 crawler.setCollectFileContentProperty(true);
 crawler.setCollectFileDiffProperty(true);
```

You can add FileFilter or CommitFilter to crawler

```java
 crawler.addFilter(new FileSourceCodeFilter());
 crawler.addFilter(new CommitHasChangedFile());
```

RepoMiner
```java

RepoMiner miner = new RepoMiner.Builder(crawler)
                .from(Revision.fromLong(1))
                .to(Revision.fromLong(7))
                .build();

```

## BUILD

* compile : `mvn clean compile`
* test    : `mvn test`
* package : `mvn package`

## LICENSE
 
This project is licensed under the terms of the MIT license.
