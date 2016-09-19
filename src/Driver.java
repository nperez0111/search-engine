import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {

	public static void main( String[] args ) {
		// TODO Modify as necessary!
		ArgumentParser parser = new ArgumentParser( args );
		Path dir = Paths.get( parser.getValue( "-dir" ) ).toAbsolutePath().normalize();
		System.out.println( dir.toString() );
		try {
			System.out.print( Traverser.validFiles( dir ) );
		}
		catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
