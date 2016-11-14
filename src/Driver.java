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
	 * First method to be called parses arguments and calls the correct methods
	 * with the arguments passed in
	 * 
	 * @param args
	 */
	public static void main( String[] args ) {

		ArgumentParser parser = new ArgumentParser( args );
		InvertedIndex index = new InvertedIndex();

		if ( parser.hasValue( DIR ) ) {
			Path inputIndex = getValidDirectory( parser.getValue( DIR ) );
			if ( inputIndex != null ) {
				try {
					InvertedIndexBuilder.build( inputIndex, index );
				}
				catch ( IOException e ) {
					System.out.println( "Directory " + inputIndex.toString() + ", Not Found or Non-Existant" );
				}
			}
		}
		if ( parser.hasFlag( INDEX ) ) {
			Path outputIndex = parser.getPath( INDEX, "index.json" );

			if ( outputIndex == null ) {
				System.out.println( "outputfile is not available" );
			}
			else {
				try {
					index.toJSON( outputIndex );
				}
				catch ( IOException e ) {
					System.out.println( "Outputting index to: " + outputIndex.toString() + " failed" );
				}

			}
		}
		if ( parser.hasValue( EXACT ) ) {
			Path queryFile = parser.getPath( EXACT );
			Path outputResult = parser.getPath( RESULTS, "results.json" );

			if ( queryFile == null ) {
				System.out.println( "Query file is not an actual path." );
				return;
			}
			try {
				SearchInvertedIndex.exact( queryFile, outputResult, index );
			}
			catch ( IOException e ) {
				System.out.println( "Exact Searching Inverted Index Failed" );
			}
		}

		if ( parser.hasValue( QUERY ) ) {
			Path queryFile = parser.getPath( QUERY );
			Path outputResult = parser.getPath( RESULTS, "results.json" );

			if ( queryFile == null ) {
				System.out.println( "Query file is not an actual path." );
				return;
			}
			try {
				SearchInvertedIndex.partial( queryFile, outputResult, index );
			}
			catch ( IOException e ) {
				System.out.println( "Partial Searching Inverted Index Failed" );
			}
		}

		/*
		 * TODO if (dir flag) build stuff
		 * 
		 * if index flag write index
		 * 
		 * if exact flag search
		 * 
		 * if query flag search
		 * 
		 * if result flag write search
		 */

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
			System.out.println( "The Directory: " + path + " you specified does not exist..." );
			return null;
		}
		return dir.normalize();
	}
}
