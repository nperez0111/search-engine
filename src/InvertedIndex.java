import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class InvertedIndex {

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private final Map<String, Map<String, Set<Integer>>> index;

	/**
	 * Initializes the index.
	 */
	public InvertedIndex() {

		index = new TreeMap<>();

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

	/**
	 * Adds the words and the position it was found within the specified path
	 * 
	 * @param word
	 * @param path
	 * @param position
	 */
	public void add( String word, Path path, int position ) {

		add( word, path.toString(), position );

	}

	/**
	 * Returns the number of files a word is in.
	 *
	 * @param word
	 *            word to look for
	 * @return number of files the word was found in.
	 */
	public int countOfFilesWithWord( String word ) {

		return index.get( word ) == null ? 0 : index.get( word ).size();

	}

	public Set<String> getFilesOfWord( String word ) {

		return index.get( word ).keySet();
	}

	/**
	 * Returns the number of words stored in the index.
	 * 
	 * @return number of words
	 */
	public int words() {

		return index.keySet().size();

	}

	public int frequencyInFile( String word, String file ) {

		return index.get( word ).get( file ).size();
	}

	public int getFirstOccurenceInFile( String word, String file ) {

		for ( Integer i : index.get( word ).get( file ) ) {
			return i.intValue();
		}

		return -1;
	}

	/**
	 * Returns all the matching results
	 * 
	 * @param query
	 * @param partial
	 * @return
	 */
	private List<Result> resultOfWord( String word ) {

		List<Result> results = new ArrayList<>();
		Set<String> files = getFilesOfWord( word );
		for ( String file : files ) {
			int count = frequencyInFile( word, file );
			int index = getFirstOccurenceInFile( word, file );
			results.add( new Result( file, count, index ) );
		}

		return results;

	}

	private boolean startsWithAnyWord( List<String> queries, String word ) {

		for ( String query : queries ) {
			if ( word.startsWith( query ) ) {
				return true;
			}
		}
		return false;
	}

	public List<Result> search( String query, boolean partial ) {

		List<String> queries = StringCleaner.cleanAndSort( query );
		List<Result> results = new ArrayList<>();
		for ( String word : getWords() ) {
			if ( partial ) {
				if ( startsWithAnyWord( queries, word ) ) {

					results.addAll( resultOfWord( word ) );
				}
				else {

					if ( query.charAt( 0 ) < word.charAt( 0 ) ) {
						// TODO test break;
					}
				}

			}
			else {
				if ( word.equals( query ) ) {
					results.addAll( resultOfWord( word ) );
				}
			}
		}
		return results;

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

	/**
	 * Returns a string representation of this index.
	 */
	@Override
	public String toString() {

		return index.toString();
	}

	/**
	 * Outputs the invertedIndex to a file.
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void toJSON( Path output ) throws IOException {

		JSONWriter.toJSON( index, output );
	}

}
