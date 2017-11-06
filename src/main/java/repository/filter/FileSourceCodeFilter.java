package repository.filter;

import repository.model.ChangeItem;

import java.util.Arrays;
import java.util.List;

public class FileSourceCodeFilter implements FileFilter {

    List<String> fileExtensions;
    public FileSourceCodeFilter() {

        this("java", "cpp", "h", "hpp");
    }

    public FileSourceCodeFilter (String ... extensions) {
        fileExtensions = Arrays.asList(extensions);
    }

    @Override
    public boolean accept(ChangeItem file) {
        if (file == null) return false;

        return accept(file.getPath());
    }


    private boolean accept(String path) {

        for(String ext : fileExtensions) {
            if (path.endsWith("." + ext)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "FileSourceCodeFilter";
    }
}
