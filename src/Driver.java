import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

// TODO Add Javadoc comments to all your classes and methods

public class Driver {

	private final static String DIR = "-dir";
	private final static String INDEX = "-index";
	
	private static ArgumentParser parser;

	// TODO Make ArgumentParser a local variable, consider collapsing some of the logic
	
	public static void main( String[] args ) {

		Path dir = getDir( args );
		if ( dir == null ) {
			return;
		}
		System.out.println( dir.toString() );
		try {
			List<Path> files = Traverser.validFiles( dir );
			FileIO f = new FileIO();
			for ( Path file : files ) {
				f.parseInput( file );
			}
			Path outputFile = getOutput();
			if ( outputFile == null ) {
				return;
			}
			f.writeOutput( outputFile );
		}
		catch ( IOException e ) {
			System.out.println( "File may be in use or not exist.." );
			return;
		}
	}

	private static Path getDir( String[] args ) {

		parser = new ArgumentParser( args );
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

	private static Path getOutput() {

		return Paths.get( parser.getValue( INDEX, "index.json" ) );
	}
}
