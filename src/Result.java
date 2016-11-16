public class Result implements Comparable<Result> {

	private int count;
	private int index;
	private final String where;

	/**
	 * Stores a search result
	 * 
	 * @param word
	 * @param where
	 * @param c
	 * @param i
	 */
	public Result( String where, int count, int index ) {
		this.count = count;
		this.index = index;
		this.where = where;
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
	public void addCount( int amountToAddBy ) {

		count += amountToAddBy;
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
	public void setIndex( int newIndex ) {

		if ( newIndex < index ) {
			index = newIndex;
		}
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
	 * just is a toString for debug purposes
	 */
	@Override
	public String toString() {

		return where + ":" + count + ":" + index;

	}
}
