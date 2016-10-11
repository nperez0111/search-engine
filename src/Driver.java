import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {

	private final static String DIR = "-dir";
	private final static String INDEX = "-index";
	private final static String QUERY = "-query";
	private final static String RESULTS = "-results";
	private final static String EXACT = "-exact";

	public static void main( String[] args ) {

		ArgumentParser parser = new ArgumentParser( args );
		Path dir = getDir( parser );
		if ( dir == null ) {
			return;
		}
		try {

			InvertedIndex index = InvertedIndexBuilder.build( dir, getOutputFile( parser, INDEX, "index.json" ),
					new InvertedIndex() );
			if ( parser.hasFlag( QUERY ) ) {
				PartialSearchInvertedIndex.search( dir, getOutputFile( parser, RESULTS, "results.json" ), index );
			}
			else if ( parser.hasFlag( EXACT ) ) {
				// TODO Write what to do when asked for exact search
			}

		}
		catch ( IOException e ) {
			System.out.println( "File may be in use or not exist.." );
			return;
		}

	}

	/**
	 * Gets the directory from the argument parser
	 * 
	 * @param parser
	 * @return a path or null if the directory is not found
	 */
	private static Path getDir( ArgumentParser parser ) {

		if ( !parser.hasFlag( DIR ) || !parser.hasValue( DIR ) ) {
			System.out.println( "Sorry you must specify a directory..." );
			return null;
		}

		Path dir = Paths.get( parser.getValue( DIR ) );
		if ( dir == null ) {
			System.out.println( "The directory you specified does not exist..." );
			return null;
		}
		return dir.normalize();
	}

	/**
	 * gets the output file from the argument parser or by default uses
	 * index.json in the current directory
	 * 
	 * @param parser
	 * @return the path of the output file
	 */
	private static Path getOutputFile( ArgumentParser parser, String whichFile, String defaultFile ) {

		return Paths.get( parser.getValue( whichFile, defaultFile ) );
	}
}
