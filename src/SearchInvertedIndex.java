import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public abstract class SearchInvertedIndex {

	/**
	 * Performs a partial search into an inverted index provided, returns same
	 * inverted index back
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @param index
	 * @return
	 * @throws IOException
	 */
	public static InvertedIndex partial( Path inputFile, Path outputFile, InvertedIndex index ) throws IOException {

		return SearchInvertedIndex.search( inputFile, outputFile, index, true );

	}

	/**
	 * Performs an exact search into an invertedindex provided, returns same
	 * inverted index back
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @param index
	 * @return
	 * @throws IOException
	 */
	public static InvertedIndex exact( Path inputFile, Path outputFile, InvertedIndex index ) throws IOException {

		return SearchInvertedIndex.search( inputFile, outputFile, index, false );

	}

	/**
	 * Generalized searching into the invertedindex partial describes whether or
	 * not the search is partial
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @param index
	 * @param partial
	 * @return
	 * @throws IOException
	 */
	private static InvertedIndex search( Path inputFile, Path outputFile, InvertedIndex index, boolean partial )
			throws IOException {

		TreeSet<String> queries = SearchInvertedIndex.getSearchQueries( inputFile );
		// Collections.sort( queries );
		// This stores the list of results returned per query
		List<List<Result>> results = new ArrayList<>();

		for ( String query : queries ) {
			results.add( index.search( query, partial ) );
		}
		if ( outputFile == null ) {
			return index;
		}
		outputToFile( outputFile, results, queries );

		return null;

	}

	/**
	 * Outputs the results per query to the specified output file
	 * 
	 * @param outputFile
	 * @param results
	 * @param queries
	 * @throws IOException
	 */
	private static void outputToFile( Path outputFile, List<List<Result>> results, TreeSet<String> queries )
			throws IOException {

		try ( BufferedWriter writer = Files.newBufferedWriter( outputFile, Charset.defaultCharset() ); ) {
			int c = 0;
			int j = 0;
			List<Object> querys = Arrays.asList( queries.toArray() );
			writer.write( "{\n" );
			for ( List<Result> perQuery : results ) {
				writer.write( "\t\"" + querys.get( c ).toString() + "\": [\n" );
				for ( Result result : perQuery ) {
					result.toJSON( writer );
					j++;
					if ( perQuery.size() != j ) {
						writer.write( ",\n" );
					}
					else {
						writer.write( "\n" );
					}
				}
				j = 0;
				c++;
				writer.write( "\t]" + ( results.size() == c ? "" : "," ) + "\n" );
			}
			writer.write( "}\n" );
		}

	}

	/**
	 * returns all the normalized search queries within a file with no repitions
	 * 
	 * @param inputFile
	 * @return
	 * @throws IOException
	 */
	private static TreeSet<String> getSearchQueries( Path inputFile ) throws IOException {

		TreeSet<String> list = new TreeSet<>();
		try ( BufferedReader reader = Files.newBufferedReader( inputFile, Charset.forName( "UTF8" ) ); ) {
			String line = null;
			while ( ( line = reader.readLine() ) != null ) {
				list.add( String.join( " ", StringCleaner.cleanAndSort( line ) ) );
			}
		}
		return list;
	}
}
