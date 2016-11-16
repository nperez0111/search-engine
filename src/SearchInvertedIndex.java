import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

// TODO First hybrid class that does stuff and stores stuff

public class SearchInvertedIndex {

	private final TreeMap<String, List<Result>> results;
	private final InvertedIndex index;

	public SearchInvertedIndex( InvertedIndex index ) {
		this.index = index;
		results = new TreeMap<>();
	}

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
	public void partial( Path inputFile ) throws IOException {

		search( inputFile, true );

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
	public void exact( Path inputFile ) throws IOException {

		search( inputFile, false );

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
	private void search( Path inputFile, boolean partial ) throws IOException {

		TreeSet<String> queries = SearchInvertedIndex.getSearchQueries( inputFile );

		for ( String query : queries ) {
			List<Result> result = index.search( StringCleaner.cleanAndSort( query ), partial );
			results.put( query, result );
		}

	}

	/**
	 * Outputs the results per query to the specified output file
	 * 
	 * @param outputFile
	 * @param resultes
	 * @param queries
	 * @throws IOException
	 */
	public void outputResults( Path outputFile ) {

		try ( BufferedWriter writer = Files.newBufferedWriter( outputFile, Charset.forName( "UTF-8" ) ); ) {
			int c = 0;
			int j = 0;
			writer.write( "{\n" );
			for ( String query : results.keySet() ) {
				writer.write( "\t\"" + query + "\": [\n" );

				List<Result> perQuery = results.get( query );
				for ( Result result : perQuery ) {

					JSONWriter.resultToJSON( writer, result.getWhere(), result.getCount(), result.getIndex() );

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
		catch ( IOException e ) {
			System.out.println( "Error outputing results to File." );
		}

	}

	/**
	 * returns all the normalized search queries within a file with no
	 * repetitions
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
