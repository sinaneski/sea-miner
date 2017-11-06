package repository.filter;

import org.junit.Assert;
import org.junit.Test;
import repository.model.ChangeItem;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileSourceCodeFilterTest {

    @Test
    public void accept_ShouldReturnTrue_WhenChangeItemJavaSourceFile() throws Exception {

        FileSourceCodeFilter filter = new FileSourceCodeFilter();

        ChangeItem changeItem = mock(ChangeItem.class);
        when(changeItem.getPath()).thenReturn("/path/MyFile.java");

        Assert.assertTrue(filter.accept(changeItem));
    }

    @Test
    public void accept_ShouldReturnTrue_WhenChangeItemIsXmlFile() throws Exception {

        FileSourceCodeFilter filter = new FileSourceCodeFilter("xml");

        ChangeItem changeItem = mock(ChangeItem.class);
        when(changeItem.getPath()).thenReturn("/path/MyFile.xml");

        Assert.assertTrue(filter.accept(changeItem));
    }

    @Test
    public void accept_ShouldReturnFalse_WhenChangeItemIsJavaClassFile() throws Exception {

        FileSourceCodeFilter filter = new FileSourceCodeFilter();

        ChangeItem changeItem = mock(ChangeItem.class);
        when(changeItem.getPath()).thenReturn("/path/MyFile.class");

        Assert.assertFalse(filter.accept(changeItem));
    }

    @Test
    public void getName() throws Exception {
        Assert.assertEquals("FileSourceCodeFilter", new FileSourceCodeFilter().getName());
    }

}