import java.io.BufferedWriter;
import java.io.IOException;

public class Result implements Comparable<Result> {

	private int count;
	private int index;
	private final String where;
	private final String word;

	public Result( String word, String where, int c, int i ) {
		this.word = word;
		count = c;
		index = i;
		this.where = where;
	}

	/**
	 * Returns the word stored in the result
	 * 
	 * @return word
	 */
	public String getWord() {

		return word;
	}

	/**
	 * Returns the location of the file as a String
	 * 
	 * @return Path as String
	 */
	public String getWhere() {

		return where;
	}

	/**
	 * Returns the amount of times a word appears in a file
	 * 
	 * @return frequency
	 */
	public int getCount() {

		return count;
	}

	/**
	 * Increments the amount of times a word has appeared in a file
	 */
	public void incCount( int c ) {

		count += c;
	}

	/**
	 * Returns the first index the word appeared in the file in
	 * 
	 * @return index integer
	 */
	public int getIndex() {

		return index;
	}

	/**
	 * allows changing the index
	 * 
	 * @param i
	 */
	public void setIndex( int i ) {

		index = i;
	}

	/**
	 * Comparator for results
	 */
	@Override
	public int compareTo( Result o ) {

		if ( count != o.getCount() ) {
			return Integer.compare( o.getCount(), count );
		}
		if ( index != o.getIndex() ) {
			return Integer.compare( index, o.getIndex() );
		}

		return where.toLowerCase().compareTo( o.getWhere().toLowerCase() );
	}

	/**
	 * Outputs a single result to the file
	 * 
	 * @param outputFile
	 * @throws IOException
	 */
	public void toJSON( BufferedWriter writer ) throws IOException {

		// System.out.println( where + ":" + count + ":" + index + "\n" );

		JSONWriter.resultToJSON( writer, where, count, index );
	}

	/**
	 * just is a toString for debug purposes
	 */
	@Override
	public String toString() {

		return word + ":" + where + ":" + count + ":" + index;

	}
}
