import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class fileIO {

	InvertedIndex index;

	public fileIO() {
		index = new InvertedIndex();
	}

	private String cleanUpLine( String s ) {

		return s.replaceAll( "[^0-9a-zA-Z ]", "" );
	}

	private List<String> getWordsInLine( String line ) {

		return new ArrayList<String>( Arrays.asList( line.split( " " ) ) );
	}

	private void addWordsToIndex( List<String> words, String fileName, int offsetPosition ) {

		int c = 0;
		for ( String word : words ) {
			index.add( word, fileName, offsetPosition + c++ );
		}
	}

	public void parseInput( Path input ) throws IOException {

		try ( BufferedReader reader = Files.newBufferedReader( input, Charset.defaultCharset() ); ) {
			String line = null;
			int count = 0;
			String fileName = input.getFileName().toString();

			while ( ( line = reader.readLine() ) != null ) {
				List<String> words = getWordsInLine( cleanUpLine( line ) );
				addWordsToIndex( words, fileName, count );
				count += words.size();
			}
		}
	}

	public void writeOutput( Path output ) throws IOException {

		try ( BufferedReader writer = Files.newBufferedReader( output, Charset.defaultCharset() ); ) {

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
