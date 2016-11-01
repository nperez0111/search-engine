import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
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

	private List<String> wordsThatMatch( String word, boolean partial ) {

		List<String> list = new ArrayList<>();
		for ( String s : getWords() ) {
			if ( partial ) {
				if ( !s.startsWith( word ) ) {
					continue;
				}
			}
			else {
				if ( !s.equals( word ) ) {
					continue;
				}
			}
			list.add( s );
		}
		return list;
	}

	private List<String> wordsThatMatch( List<String> words, boolean partial ) {

		List<String> list = new ArrayList<>();
		for ( String s : getWords() ) {
			if ( partial ) {
				if ( !startsWithAnyWord( words, s ) ) {
					continue;
				}
			}
			else {
				if ( !equalsAnyWord( words, s ) ) {
					continue;
				}
			}
			list.add( s );
		}
		return list;
	}

	/*
	 * public int getFirstOccurenceInAnyFile( List<String> words ) {
	 * 
	 * Integer lowest = Integer.MAX_VALUE; for ( String s : words ) { for (
	 * Set<Integer> set : index.get( s ).values() ) { for ( Integer i : set ) {
	 * if ( lowest.intValue() > i.intValue() ) { lowest = i; } } } }
	 * 
	 * return lowest; }
	 */

	public int getFirstOccurenceInAFile( String word, String file, boolean partial ) {

		Integer lowest = Integer.MAX_VALUE;

		for ( String s : wordsThatMatch( word, partial ) ) {
			// System.out.println( "word:" + word + ":" + s );
			// System.out.println( "File:" + file + ",index:" + index.get( s
			// ).toString() );
			if ( index.get( s ).get( file ) == null ) {
				continue;
			}
			for ( Integer i : index.get( s ).get( file ) ) {

				if ( lowest.intValue() > i.intValue() ) {
					lowest = i;
				}

			}
		}

		return lowest;

	}

	private boolean startsWithAnyWord( List<String> queries, String word ) {

		for ( String query : queries ) {
			if ( word.startsWith( query ) ) {
				return true;
			}
		}
		return false;
	}

	private boolean equalsAnyWord( List<String> queries, String word ) {

		for ( String query : queries ) {
			if ( word.equals( query ) ) {
				return true;
			}
		}
		return false;
	}

	private boolean wordIsInList( String query, List<Result> lis, int index, int count ) {

		for ( Result r : lis ) {
			if ( r.getWhere().equals( query ) ) {
				r.incCount( count );
				if ( r.getIndex() > index ) {
					r.setIndex( index );
				}
				return true;
			}
		}
		return false;

	}

	public List<Result> search( String query, boolean partial ) {

		List<String> queries = StringCleaner.cleanAndSort( query );
		String cleanedQuery = String.join( " ", queries );
		List<Result> results = new ArrayList<>();
		List<String> words = wordsThatMatch( queries, partial );
		for ( String word : words ) {

			for ( String file : index.get( word ).keySet() ) {

				int index = getFirstOccurenceInAFile( word, file, partial );
				int count = frequencyInFile( word, file );

				if ( !wordIsInList( file, results, index, count ) ) {

					results.add( new Result( cleanedQuery, file, count, index ) );

				}
			}
		}
		// System.out.println( results.toString() );
		Collections.sort( results );
		// System.out.println( results.toString() );
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
