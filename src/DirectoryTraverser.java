import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DirectoryTraverser {

	/**
	 * Returns true if the file path specified ends with .txt
	 * 
	 * @param path
	 * @return boolean
	 */
	private static boolean isOfCorrectType( Path path ) {

		return path.getFileName().toString().toLowerCase().endsWith( ".txt" );
	}

	/**
	 * This is what performs the actual traversal. Returns a List of Valid Files
	 * as specified by isOfCorrectType
	 * 
	 * @param path
	 * @param accumulator
	 * @return List<Path>
	 * @throws IOException
	 */
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

	/**
	 * This Takes in a directory Searches all files within the directory and
	 * returns the Paths of the files which end with `.txt` non case
	 * sensitively.
	 * 
	 * @param Path
	 *            directory to be searched
	 * @return List<Path>, all files found with .txt
	 * @throws IOException
	 */
	public static List<Path> validFiles( Path path ) throws IOException {

		List<Path> emptyAccumulator = new ArrayList<>();
		return validFiles( path, emptyAccumulator );
	}

}
