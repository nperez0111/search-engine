import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class InvertedIndex {

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private Map<String, Map<String, Set<Integer>>> index;

	/**
	 * Initializes the index.
	 */
	public InvertedIndex() {

		index = new TreeMap<>();

	}

	public InvertedIndex( Map<String, Map<String, Set<Integer>>> fresh ) {
		index = fresh;
	}

	/**
	 * Adds the word and the position it was found to the index.
	 *
	 * @param word
	 *            word to clean and add to index
	 * @param position
	 *            position word was found
	 */
	public void add( String word, String path, int position ) {

		if ( index.get( word ) == null ) {

			index.put( word, new TreeMap<>() );

		}
		if ( index.get( word ).get( path ) == null ) {

			index.get( word ).put( path, new TreeSet<>() );

		}

		index.get( word ).get( path ).add( position );

	}

	public void add( String word, Path p, int position ) {

		add( word, p.toString(), position );

	}

	/**
	 * Returns the number of times a word was found (i.e. the number of
	 * positions associated with a word in the index).
	 *
	 * @param word
	 *            word to look for
	 * @return number of times the word was found
	 */
	public int count( String word ) {

		return index.get( word ) == null ? 0 : index.get( word ).size();

	}

	/**
	 * Returns the number of words stored in the index.
	 * 
	 * @return number of words
	 */
	public int words() {

		return index.keySet().size();

	}

	/**
	 * Tests whether the index contains the specified word.
	 * 
	 * @param word
	 *            word to look for
	 * @return true if the word is stored in the index
	 */
	public boolean contains( String word ) {

		return index.containsKey( word );

	}

	/**
	 * Returns a copy of the words in this index as a sorted list.
	 * 
	 * @return sorted list of words
	 */
	public List<String> getWords() {

		return new ArrayList<String>( index.keySet() );
	}

	public Map<String, Map<String, Set<Integer>>> getData() {

		return Collections.unmodifiableMap( index );
	}

	/**
	 * Returns a copy of the positions for a specific word.
	 * 
	 * @param word
	 *            to find in index
	 * @return sorted list of positions for that word
	 */
	public List<Integer> getPositions( String word ) {

		List<Integer> ret = new ArrayList<>();
		for ( Entry<String, Set<Integer>> l : index.get( word ).entrySet() ) {
			ret.addAll( l.getValue() );
		}
		Collections.sort( ret );

		return ret;

	}

	/**
	 * Returns a string representation of this index.
	 */
	@Override
	public String toString() {

		return index.toString();
	}

}
