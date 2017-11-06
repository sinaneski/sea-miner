package repository.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import repository.model.Commit;
import repository.model.ProjectChange;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ProjectCommitJsonWriter implements ProjectChangeWriter {

    private final String filename;

    public ProjectCommitJsonWriter(String filename) {
        this.filename = filename;
    }

    public void write(final ProjectChange projectChange) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<Commit> commitList = projectChange.getCommits();

            objectMapper.writeValue(new File(filename), commitList);
        }
        catch (IOException e) {
            log.error (e.getMessage());
        }
    }

}
