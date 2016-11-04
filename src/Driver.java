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
		Path dir = getDir( parser, DIR );
		InvertedIndex index;
		if ( dir == null ) {
			return;
		}
		try {

			index = InvertedIndexBuilder.build( dir );
			Path outputFile = getOutputFile( parser, INDEX, "index.json" );

			if ( outputFile != null ) {

				index.toJSON( outputFile );

			}
			if ( parser.hasFlag( QUERY ) || parser.hasFlag( EXACT ) ) {
				String flag = parser.hasFlag( QUERY ) ? QUERY : EXACT;
				Path queryFile = getDir( parser, flag );
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
	private static Path getDir( ArgumentParser parser, String flag ) {

		if ( !parser.hasFlag( flag ) || !parser.hasValue( flag ) ) {
			System.out.println( "Sorry you must specify a directory..." );
			return null;
		}

		Path dir = Paths.get( parser.getValue( flag ) );
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
	private static Path getOutputFile( ArgumentParser parser, String flag, String defaulter ) {

		return Paths.get( parser.getValue( flag, defaulter ) );
	}
}
