import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO Too general of a class name
// TODO Remove any writing logic from here, move into a JSONWriter
// TODO Rename this InvertedIndexBuilder

public class FileIO {

	// TODO Always use public or private
	// TODO Shouldn't need this at allllllll
	InvertedIndex index;

	public FileIO() {
		index = new InvertedIndex();
	}

	private StringBuilder getJSON() { // private static void toJSON(InvertedIndex index)

		return JSONBuilder.makeInvertedIndexJSON( index.getData() );
	}

	private String cleanUpLine( String s ) { // TODO Just make this static

		return s.trim().replaceAll( "\\p{Punct}+", "" ).toLowerCase();
	}

	// TODO Split and directly add words to the index
	private List<String> getWordsInLine( String line ) {

		List<String> list = new ArrayList<String>( Arrays.asList( line.split( " " ) ) );
		List<String> words = new ArrayList<>();
		for ( String word : list ) {
			words.add( word.trim() );
		}
		return words;
	}

	private int addWordsToIndex( List<String> words, String fileName, int offsetPosition ) {

		int c = 0;
		for ( String word : words ) {
			if ( !word.equals( "" ) ) {
				index.add( word, fileName, offsetPosition + ++c );
			}
		}
		return c;
	}

	public void parseInput( Path input ) throws IOException {
		// TODO Charset.forName("UTF8")
		try ( BufferedReader reader = Files.newBufferedReader( input, Charset.defaultCharset() ); ) {
			String line = null;
			int count = 0;
			String fileName = input.toString();

			while ( ( line = reader.readLine() ) != null ) {
				List<String> words = getWordsInLine( cleanUpLine( line ) );

				count += addWordsToIndex( words, fileName, count );
			}
		}
	}

	public void writeOutput( Path output ) throws IOException {

		StringBuilder JSON = getJSON();
		try ( BufferedWriter writer = Files.newBufferedWriter( output, Charset.defaultCharset() ); ) {
			writer.write( JSON.toString() );
			writer.newLine();
		}
	}

	/**
	 * Returns a string representation of this index.
	 */
	@Override
	public String toString() {

		return index.toString();
	}
	
	/*

	public static void parseDirectory(Path directory, InvertedIndex index) {
	
		if (directory)
			recursive call
		else if text file
			parseFile(Path file, InvertedIndex index)
		
	}
	
	public static void parseFile(Path file, InvertedIndex index) {
	

	}
	 */
}
