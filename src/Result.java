import java.io.IOException;
import java.nio.file.Path;

public class Result implements Comparable<Result> {

	private int count;
	private int index;
	private final String where;

	public Result( String where, int c, int i ) {
		count = c;
		index = i;
		this.where = where;
	}

	public String getWhere() {

		return where;
	}

	public int getCount() {

		return count;
	}

	public int getIndex() {

		return index;
	}

	@Override
	public int compareTo( Result o ) {

		// TODO Write comparator for result
		return 0;
	}

	public void toJSON( Path outputFile ) throws IOException {

		System.out.println( where + ":" + count + ":" + index + "\n" );

		// TODO Write ToJSON for result
	}
}
