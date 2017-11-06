package repository.model;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class Commit {

	private Revision revision;

	private Date date;
	private String author;
	private String message;
	private List<ChangeItem> changeItemList;
}
