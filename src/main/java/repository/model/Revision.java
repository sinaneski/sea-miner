package repository.model;

import lombok.Data;

@Data
public class Revision{

    private String value;

    public Revision(String value) {
        this.value = value;
    }

    public static Revision fromLong(long value) {
        return new Revision(String.valueOf(value));
    }

    public long toLong() {
        return Long.parseLong(value);
    }

    public static Revision NONE =  new Revision("-1L");

}
