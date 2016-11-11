import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This is the driver class that calls all the other classes to build the
 * inverted index and output it into file as well as search it
 * 
 * @author
 *
 */
public class Driver {

	private final static String DIR = "-dir";
	private final static String INDEX = "-index";
	private final static String QUERY = "-query";
	private final static String RESULTS = "-results";
	private final static String EXACT = "-exact";

	/**
	 * TODO
	 * @param args
	 */
	public static void main( String[] args ) {

		ArgumentParser parser = new ArgumentParser( args );
		InvertedIndex index = new InvertedIndex();
		
		try {
			if ( parser.hasFlag( DIR ) && parser.hasValue( DIR ) ) {
				Path inputIndex = getValidDirectory( parser.getValue( DIR ) );
				if ( inputIndex != null ) {
					InvertedIndexBuilder.build( inputIndex, index );
				}
				
				// TODO Move writing the index into a separate if block
				Path outputIndex = getOutputFile( parser, INDEX, "index.json" );

				if ( outputIndex != null ) {

					index.toJSON( outputIndex );

				}
			}

			if ( parser.hasValue( QUERY ) || parser.hasValue( EXACT ) ) {
				String flag = parser.hasFlag( QUERY ) ? QUERY : EXACT;
				Path queryFile = getValidDirectory( parser.getValue( flag ) );
				Path outputResult = getOutputFile( parser, RESULTS, "results.json" );

				if ( queryFile == null ) {
					return;
				}

				if ( parser.hasFlag( QUERY ) ) {

					SearchInvertedIndex.partial( queryFile, outputResult, index );
				}
				else if ( parser.hasFlag( EXACT ) ) {
					SearchInvertedIndex.exact( queryFile, outputResult, index );
				}
			}
			
			/* TODO
			if (dir flag)
				build stuff
			
			if index flag
				write index
			
			if exact flag
				search
			
			if query flag
				search
			
			if result flag
				write search
			*/
		}
		catch (IOException e ) {
			// TODO Would be nice to know more about what went wrong? Which file? Was it building or searching or writing?
			System.out.println( "File may be in use or not exist.." );
			return;
		}
	}

	/**
	 * gets a valid directory and outputs a message if it does not exist
	 * 
	 * @param path
	 * @return
	 */
	private static Path getValidDirectory( String path ) {

		Path dir = Paths.get( path );
		if ( dir == null ) {
			System.out.println( "The directory you specified does not exist..." );
			return null;
		}
		return dir.normalize();
	}

	// TODO Since this is private, you aren't really getting anything from this one-line method
	/**
	 * gets the output file from the argument parser or by default uses
	 * index.json in the current directory
	 *
	 * @param parser
	 * @return the path of the output file
	 */
	private static Path getOutputFile( ArgumentParser parser, String flag, String defaulter ) {
		// TODO If you really want it as a path, add a getPath(String flag, String default)
		return Paths.get( parser.getValue( flag, defaulter ) );
	}
}
