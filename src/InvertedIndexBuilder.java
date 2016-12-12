import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Builds the inverted index based off a file
 * 
 * @author
 *
 */
public class InvertedIndexBuilder {

	/**
	 * Take a file and put all the words into an index into the index
	 *
	 * @param input
	 * @param index
	 * @throws IOException
	 */
	private static void parseInput( Path input, InvertedIndex index ) throws IOException {

		try ( BufferedReader reader = Files.newBufferedReader( input, Charset.forName( "UTF8" ) ); ) {
			String line = null;
			int count = 1;
			String fileName = input.toString();

			while ( ( line = reader.readLine() ) != null ) {
				count = parseLine( line, fileName, count, index );

			}
		}
	}

	/**
	 * Parses a single line of a file and adds it to the index
	 * 
	 * @param line
	 * @param fileName
	 * @param count
	 * @param index
	 * @return
	 */
	public static int parseLine( String line, String fileName, int count, InvertedIndex index ) {

		String[] list = StringCleaner.stripNonAlphaNumeric( line ).split( "\\s+" );
		return parseLine( list, fileName, count, index );
	}

	/**
	 * parses an array of words into an index
	 * 
	 * @param list
	 * @param fileName
	 * @param count
	 * @param index
	 * @return
	 */
	public static int parseLine( String[] list, String fileName, int count, InvertedIndex index ) {

		for ( String word : list ) {
			word = word.trim();

			if ( !word.equals( "" ) ) {
				index.add( word, fileName, count );
				count++;
			}
		}
		return count;
	}

	/**
	 * This goes through all the files from the input path and adds all the
	 * necessary data to the InvertedIndex. Then Prints the InvertedIndex when
	 * finished adding all the necessary data.
	 *
	 * @param inputPath
	 * @param outputFile
	 * @param index
	 * @throws IOException
	 */
	public static InvertedIndex buildMultiThreaded( Path inputPath, ThreadSafeInvertedIndex index, int threads )
			throws IOException {

		List<Path> files = DirectoryTraverser.validFiles( inputPath );

		WorkQueue minions = new WorkQueue( threads );

		for ( Path file : files ) {

			minions.execute( new ParserTask( file, index ) );

		}

		minions.finish();
		minions.shutdown();
		return index;

	}

	/**
	 * This goes through all the files from the input path and adds all the
	 * necessary data to the InvertedIndex. Then Prints the InvertedIndex when
	 * finished adding all the necessary data.
	 *
	 * @param inputPath
	 * @param outputFile
	 * @param index
	 * @throws IOException
	 */
	public static InvertedIndex build( Path inputPath, InvertedIndex index ) throws IOException {

		List<Path> files = DirectoryTraverser.validFiles( inputPath );

		for ( Path file : files ) {
			parseInput( file, index );
		}
		return index;

	}

	/**
	 * Class that holds all the neccessary info to parse a directory into an
	 * inverted index
	 * 
	 * @author nickthesick
	 *
	 */
	private static class ParserTask implements Runnable {

		private final Path file;
		private final ThreadSafeInvertedIndex index;

		public ParserTask( Path file, ThreadSafeInvertedIndex index ) {
			this.file = file;
			this.index = index;
		}

		@Override
		public void run() {

			// InvertedIndex index = new InvertedIndex();
			try {
				parseInput( file, index );
			}
			catch ( IOException e ) {
				Driver.log.error( "unable to parse file:" + file.toString() );
			}
		}

	}

}
