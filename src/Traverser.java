import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Traverser {

	private static boolean isOfCorrectType( Path path ) {

		return path.getFileName().toString().toLowerCase().endsWith( ".txt" );
	}

	private static List<Path> validFiles( Path path, List<Path> accumulator ) throws IOException {

		try ( DirectoryStream<Path> listing = Files.newDirectoryStream( path ) ) {

			for ( Path file : listing ) {

				if ( Files.isDirectory( file ) ) {

					validFiles( file, accumulator );

				}

				else {

					if ( isOfCorrectType( file ) ) {

						accumulator.add( file );

					}

				}

			}

		}

		return accumulator;
	}

	public static List<Path> validFiles( Path path ) throws IOException {

		List<Path> emptyAccumulator = new ArrayList<>();
		return validFiles( path, emptyAccumulator );
	}

}
