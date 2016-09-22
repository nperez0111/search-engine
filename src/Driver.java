import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {

	private final static String DIR = "-dir";
	private final static String INDEX = "-index";

	public static void main( String[] args ) {

		ArgumentParser parser = new ArgumentParser( args );
		Path dir = getDir( parser );
		if ( dir == null ) {
			return;
		}
		try {

			new FileIO( Traverser.validFiles( dir ), getOutput( parser ) );

		}
		catch ( IOException e ) {
			System.out.println( "File may be in use or not exist.." );
			return;
		}
	}

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

	private static Path getOutput( ArgumentParser parser ) {

		return Paths.get( parser.getValue( INDEX, "index.json" ) );
	}
}
