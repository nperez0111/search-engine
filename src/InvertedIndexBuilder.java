import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class InvertedIndexBuilder {

	/**
	 * Cleans up the line removing any non alphanumeric character
	 * 
	 * @param s
	 * @return
	 */
	private static String cleanUpLine( String s ) {

		return s.trim().replaceAll( "\\p{Punct}+", "" ).replaceAll( "\\s+", " " ).toLowerCase();
	}

	/**
	 * Take a file and put all the words into an index into the index
	 * 
	 * @param input
	 * @param index
	 * @throws IOException
	 */
	private static void parseInput( Path input, InvertedIndex index ) throws IOException {

		try ( BufferedReader reader = Files.newBufferedReader( input, Charset.forName( "UTF8" ) ); ) {
			String line = null;
			int count = 1;
			String fileName = input.toString();

			while ( ( line = reader.readLine() ) != null ) {
				List<String> list = new ArrayList<>(
						Arrays.asList( InvertedIndexBuilder.cleanUpLine( line ).split( "\\s" ) ) );
				for ( String word : list ) {
					if ( !word.trim().equals( "" ) ) {
						index.add( word.trim(), fileName, count );
						count++;
					}
				}

			}
		}
	}

	/**
	 * This goes through all the files from the input path and adds all the
	 * necessary data to the InvertedIndex. Then Prints the InvertedIndex when
	 * finished adding all the necessary data.
	 * 
	 * @param inputPath
	 * @param outputFile
	 * @param index
	 * @throws IOException
	 */
	public static InvertedIndex build( Path inputPath ) throws IOException {

		List<Path> files = DirectoryTraverser.validFiles( inputPath );
		InvertedIndex index = new InvertedIndex();
		for ( Path file : files ) {
			parseInput( file, index );
		}
		return index;

	}
}
