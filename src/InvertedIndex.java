import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class InvertedIndex {

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;

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

	/**
	 * Returns the number of words stored in the index.
	 * 
	 * @return number of words
	 */
	public int words() {

		return index.keySet().size();

	}

	/**
	 * returns the frequency of a word within a file
	 * 
	 * @param word
	 * @param file
	 * @return
	 */
	private int frequencyInFile( String word, String file ) {

		return index.get( word ).get( file ).size();
	}

	/**
	 * Gets the first index of a word within a file
	 * 
	 * @param word
	 * @param file
	 * @param partial
	 * @return
	 */
	private int getFirstOccurenceInAFile( String word, String file ) {

		return index.get( word ).get( file ).first();

	}

	/**
	 * performs an exact search on the inverted index and returns a list for
	 * results
	 * 
	 * @param queries
	 * @return List of Results
	 */
	public List<Result> exactSearch( String[] queries ) {

		List<Result> results = new ArrayList<>();
		Map<String, Result> resultMap = new HashMap<>();

		for ( String query : queries ) {
			if ( index.containsKey( query ) ) {
				searchSomething( query, query, resultMap, results );
			}
		}
		Collections.sort( results );
		return results;

	}

	/*
	 * Performs a partial search based upon the queries parsed
	 */
	public List<Result> partialSearch( String[] queries ) {

		List<Result> results = new ArrayList<>();
		Map<String, Result> resultMap = new HashMap<>();

		for ( String word : queries ) {
			for ( String match : index.tailMap( word ).keySet() ) {

				if ( !match.startsWith( word ) ) {
					break;
				}

				searchSomething( match, word, resultMap, results );

			}
		}

		Collections.sort( results );
		return results;
	}

	public void searchSomething( String match, String word, Map<String, Result> map, List<Result> results ) {

		for ( String file : index.get( match ).keySet() ) {

			int index = getFirstOccurenceInAFile( match, file );
			int count = frequencyInFile( match, file );

			if ( map.containsKey( file ) ) {
				map.get( file ).addCount( count );
				map.get( file ).setIndex( index );
			}
			else {
				Result result = new Result( file, count, index );
				results.add( result );
				map.put( file, result );
			}
		}
	}

	/**
	 * actually performs the search of a string within the inverted index
	 * 
	 * @param query
	 * @param partial
	 * @return
	 */
	public List<Result> search( String[] queries, boolean partial ) {

		return partial ? partialSearch( queries ) : exactSearch( queries );
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
