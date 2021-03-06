import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeMap;

/**
 * Searches an inverted index and stores the results within the results treemap
 * to then be outputted when output to result is called
 * 
 * @author nickthesick
 *
 */
public class SearchInvertedIndex {

	private final TreeMap<String, List<Result>> results;
	private final ThreadSafeInvertedIndex index;

	/**
	 * Searches an invertedindex provided to then output the result in a proper
	 * json format
	 * 
	 * @param index
	 */
	public SearchInvertedIndex( ThreadSafeInvertedIndex index ) {
		this.index = index;
		results = new TreeMap<>();
	}

	/**
	 * Performs a partial search into an inverted index provided
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
	 * Performs an exact search into an invertedindex provided
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
	 * performs a exact search using the inputfile as the source of all the
	 * queries in a multithreaded manner
	 * 
	 * @param inputFile
	 * @param threads
	 * @throws IOException
	 */
	public void exactMultiThreaded( Path inputFile, int threads ) throws IOException {

		multiSearch( inputFile, false, threads );
	}

	/**
	 * performs a partial search using the inputfile as the source of all the
	 * queries in a multithreaded manner
	 * 
	 * @param inputFile
	 * @param threads
	 * @throws IOException
	 */
	public void partialMultiThreaded( Path inputFile, int threads ) throws IOException {

		multiSearch( inputFile, true, threads );
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

		try ( BufferedReader reader = Files.newBufferedReader( inputFile, Charset.forName( "UTF8" ) ); ) {
			String line = null;
			while ( ( line = reader.readLine() ) != null ) {

				String[] words = StringCleaner.cleanAndSort( line );
				List<Result> result = index.search( words, partial );
				results.put( String.join( " ", words ), result );
			}
		}

	}

	private static class SearcherTask implements Runnable {

		private final String[] words;
		private final ThreadSafeInvertedIndex index;
		private final boolean partial;
		private final TreeMap<String, List<Result>> results;

		public SearcherTask( String words, ThreadSafeInvertedIndex index, boolean partial,
				TreeMap<String, List<Result>> results ) {
			this.words = StringCleaner.cleanAndSort( words );
			this.index = index;
			this.partial = partial;
			this.results = results;
		}

		@Override
		public void run() {

			results.put( String.join( " ", words ), index.search( words, partial ) );
		}

	}

	private void multiSearch( Path inputFile, boolean partial, int threads ) throws IOException {

		WorkQueue minions = new WorkQueue( threads );
		try ( BufferedReader reader = Files.newBufferedReader( inputFile, Charset.forName( "UTF8" ) ); ) {
			String line = null;
			while ( ( line = reader.readLine() ) != null ) {

				minions.execute( new SearcherTask( line, index, partial, results ) );

			}
			minions.finish();
			minions.shutdown();
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
			Driver.log.error( "Error outputing results to File." );
		}

	}
}
