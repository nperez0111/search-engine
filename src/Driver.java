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
			Path outputIndex = getOutputFile( parser, INDEX, "index.json" );

			if ( outputIndex != null ) {

				try {
					index.toJSON( outputIndex );
				}
				catch ( IOException e ) {
					System.out.println( "Outputting index to: " + outputIndex.toString() + " failed" );
				}

			}
		}
		if ( parser.hasValue( EXACT ) ) {
			Path queryFile = getValidDirectory( parser.getValue( EXACT ) );
			Path outputResult = Paths.get( parser.getValue( RESULTS, "results.json" ) );

			if ( queryFile == null ) {
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
			Path queryFile = getValidDirectory( parser.getValue( QUERY ) );
			Path outputResult = Paths.get( parser.getValue( RESULTS, "results.json" ) );

			if ( queryFile == null ) {
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
			System.out.println( "The directory you specified does not exist..." );
			return null;
		}
		return dir.normalize();
	}

	// TODO Since this is private, you aren't really getting anything from this
	// one-line method
	/**
	 * gets the output file from the argument parser or by default uses
	 * index.json in the current directory
	 *
	 * @param parser
	 * @return the path of the output file
	 */
	private static Path getOutputFile( ArgumentParser parser, String flag, String defaulter ) {

		// TODO If you really want it as a path, add a getPath(String flag,
		// String default)
		return Paths.get( parser.getValue( flag, defaulter ) );
	}
}
