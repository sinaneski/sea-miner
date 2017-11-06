package repository.filter;

import org.junit.Assert;
import org.junit.Test;
import repository.model.ChangeItem;
import repository.model.Commit;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommitHasChangedFileTest {

    @Test
    public void accept_ShouldReturnFalse_WhenChangeItemsListIsNull() throws Exception {

        CommitHasChangedFile filter = new CommitHasChangedFile();

        Commit commit = mock(Commit.class);
        when(commit.getChangeItemList()).thenReturn(null);

        Assert.assertFalse(filter.accept(commit));

    }

    @Test
    public void accept_ShouldReturnFalse_WhenChangeItemsListIsEmpty() throws Exception {

        CommitHasChangedFile filter = new CommitHasChangedFile();

        Commit commit = mock(Commit.class);
        when(commit.getChangeItemList()).thenReturn(new ArrayList<>());

        Assert.assertFalse(filter.accept(commit));
    }

    @Test
    public void accept_ShouldReturnTrue_WhenCommitHasChangedItem() throws Exception {

        CommitHasChangedFile filter = new CommitHasChangedFile();

        Commit commit = mock(Commit.class);
        ChangeItem changeItem = mock(ChangeItem.class);
        when(commit.getChangeItemList()).thenReturn(Arrays.asList(changeItem));

        Assert.assertTrue(filter.accept(commit));
    }

    @Test
    public void getName() throws Exception {
        Assert.assertEquals("CommitHasChangedFile", new CommitHasChangedFile().getName());
    }

}