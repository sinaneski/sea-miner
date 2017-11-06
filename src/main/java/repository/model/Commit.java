package repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class Commit {

	private Revision revision;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Istanbul")
	private Date date;
	private String author;
	private String message;
	private List<ChangeItem> changeItemList;

}
