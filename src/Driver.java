import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Driver {
	private final static String DIR = "-dir";

	public static void main( String[] args ) {
		Path dir = getPath( args );
		System.out.println( dir.toString() );
		try {
			List<Path> files = Traverser.validFiles( dir );
			fileIO f = new fileIO();
			for ( Path file : files ) {
				f.parseInput( file );
			}
			System.out.println( f.toString() );
		}
		catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Path getPath( String[] args ) {
		ArgumentParser parser = new ArgumentParser( args );
		if ( !parser.hasFlag( DIR ) ) {
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
}
