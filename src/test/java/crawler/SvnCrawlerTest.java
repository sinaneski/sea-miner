package crawler;

import org.junit.Assert;
import org.junit.Test;
import repository.crawler.SvnCrawler;
import repository.crawler.exception.CrawlerException;
import repository.crawler.exception.SvnCrawlerException;
import repository.model.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SvnCrawlerTest {

    @Test
    public void getLastRevision_ShouldReturnLastRevision7_WhenGiven_SVNTEST_REPO() throws CrawlerException {
        String url = getSvnTestRepoUrl();
        SvnCrawler repo = SvnCrawler.openConnection(url);
        Assert.assertEquals(Revision.fromLong(7), repo.getLastRevision());
    }

    private String getSvnTestRepoUrl() {
        Path resourceDirectory = Paths.get("src/test/resources/svntest-repo");
        return "file://" + resourceDirectory.toAbsolutePath();
    }

    @Test
    public void getCommitsAt_ShouldReturnCommitAt3With1FileChanged_WhenGivenParameter3() throws CrawlerException {
        String url = getSvnTestRepoUrl();
        SvnCrawler repo = SvnCrawler.openConnection(url);
        Commit commit = repo.getCommitAt(Revision.fromLong(3));

        Assert.assertNotNull(commit);

        Assert.assertEquals(1, commit.getChangeItemList().size());
        Assert.assertEquals("/src/main/java/sea/sample/Factorial.java", commit.getChangeItemList().get(0).getPath());
    }

    @Test
    public void getCommitsAt_ShouldReturnCommitAt3WithProperFileDiff_WhenGivenParameter3() throws CrawlerException {
        String url = getSvnTestRepoUrl();
        SvnCrawler repo = SvnCrawler.openConnection(url);
        Commit commit = repo.getCommitAt(Revision.fromLong(3));

        Assert.assertNotNull(commit);

        FileDiff fileDiff = FileDiff.fromContent("Index: Factorial.java\n" +
                "===================================================================\n" +
                "--- Factorial.java\t(revision 3)\n" +
                "+++ Factorial.java\t(revision 2)\n" +
                "@@ -1,13 +1,10 @@\n" +
                " package sea.sample;\n" +
                " \n" +
                " public class Factorial {\n" +
                "-\n" +
                "-    public static long calculate(int n) {\n" +
                "-\n" +
                "-        int result = 1;\n" +
                "-        for(int i = 2; i <= n; i++) {\n" +
                "+    public static int calculate(int n)\n" +
                "+    {\tint result = 1;\n" +
                "+        for(int i = 2; i <= n; i++)\n" +
                "             result = result * i;\n" +
                "-        }\n" +
                "         return result;\n" +
                "     }\n" +
                " }\n");

        Assert.assertEquals(fileDiff, commit.getChangeItemList().get(0).getDiff());
        Assert.assertEquals(3, commit.getChangeItemList().get(0).getDiff().getAddedLineCount());
        Assert.assertEquals(6, commit.getChangeItemList().get(0).getDiff().getDeletedLineCount());
    }

    @Test
    public void getCommitsAt_ShouldReturnCommitAt4With2FileChanged_WhenGivenParameter4() throws CrawlerException {
        String url = getSvnTestRepoUrl();
        SvnCrawler repo = SvnCrawler.openConnection(url);
        Commit commit = repo.getCommitAt(Revision.fromLong(4));

        Assert.assertNotNull(commit);

        Assert.assertEquals(2, commit.getChangeItemList().size());
        Assert.assertEquals("/src/main/java/sea/sample/GreetingApp.java", commit.getChangeItemList().get(0).getPath());
        Assert.assertEquals("/src/main/java/sea/sample/Fibonacci.java", commit.getChangeItemList().get(1).getPath());
    }

    @Test
    public void getCommitsAt_ShouldReturnCommitAt4WithProperFileDiff_WhenGivenParameter4() throws CrawlerException {
        String url = getSvnTestRepoUrl();
        SvnCrawler repo = SvnCrawler.openConnection(url);
        Commit commit = repo.getCommitAt(Revision.fromLong(4));

        Assert.assertNotNull(commit);

        FileDiff fileDiff = FileDiff.fromContent("Index: GreetingApp.java\n" +
                "===================================================================\n" +
                "--- GreetingApp.java\t(revision 4)\n" +
                "+++ GreetingApp.java\t(revision 3)\n" +
                "@@ -4,17 +4,5 @@\n" +
                "     public static void main(String[] args) {\n" +
                "         System.out.println(\"Sample app ...\");\n" +
                "         System.out.println(Factorial.calculate(5));\n" +
                "-\n" +
                "-        int n = 10, t1 = 0, t2 = 1;\n" +
                "-        System.out.print(\"First \" + n + \" terms: \");\n" +
                "-\n" +
                "-        for (int i = 1; i <= n; ++i)\n" +
                "-        {\n" +
                "-            System.out.print(t1 + \" + \");\n" +
                "-\n" +
                "-            int sum = t1 + t2;\n" +
                "-            t1 = t2;\n" +
                "-            t2 = sum;\n" +
                "-        }\n" +
                "     }\n" +
                " }\n");

        Assert.assertEquals(fileDiff, commit.getChangeItemList().get(0).getDiff());
        Assert.assertEquals(0, commit.getChangeItemList().get(0).getDiff().getAddedLineCount());
        Assert.assertEquals(12, commit.getChangeItemList().get(0).getDiff().getDeletedLineCount());
    }


    @Test(expected = SvnCrawlerException.class)
    public void getLastRevision_ShouldThrowException_WhenGivenRepoNotExist() throws CrawlerException {
        String url = "file:///nonexist";

        SvnCrawler repo = SvnCrawler.openConnection(url);

        Assert.assertEquals(1, repo.getLastRevision());
    }
}