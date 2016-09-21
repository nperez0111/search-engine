import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileIO {

	InvertedIndex index;

	public FileIO() {
		index = new InvertedIndex();
	}

	private StringBuilder getJSON() {

		return JSONBuilder.makeInvertedIndexJSON( index.getData() );
	}

	private String cleanUpLine( String s ) {

		return s.trim().replaceAll( "\\p{Punct}+", "" ).toLowerCase();
	}

	private List<String> getWordsInLine( String line ) {

		List<String> list = new ArrayList<>( Arrays.asList( line.split( " " ) ) );
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

	// Take in input of files and put them into the index
	public void parseInput( Path input ) throws IOException {

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

	// Write output to file
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
}
