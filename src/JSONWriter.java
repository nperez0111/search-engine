import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Outputs certain Java objects to a file in a "pretty" JSON format using the
 * tab character to indent nested elements and the new line character to
 * separate multiple elements. Only a small number of objects are currently
 * supported.
 * 
 * @see <a href="http://json.org/">http://json.org/</a>
 */
public class JSONWriter {

	/** Tab character used for pretty JSON output. */
	private static final char TAB = '\t';

	/** End of line character used for pretty JSON output. */
	private static final char END = '\n';

	/**
	 * Returns a quoted version of the provided text.
	 * 
	 * @param text
	 * @return "text" in quotes
	 */
	public static String quote( String text ) {

		return String.format( "\"%s\"", text );
	}

	/**
	 * Returns n tab characters.
	 * 
	 * @param n
	 *            number of tab characters
	 * @return n tab characters concatenated
	 */
	public static String tab( int n ) {

		char[] tabs = new char[n];
		Arrays.fill( tabs, TAB );
		return String.valueOf( tabs );
	}

	/**
	 * Writes an array to file
	 * 
	 * @param writer
	 * @param elements
	 * @param amountToTab
	 * @throws IOException
	 */
	private static void makeArray( BufferedWriter writer, Set<Integer> elements, int amountToTab ) throws IOException {

		writer.write( "[" + END );
		int c = elements.size();

		for ( Integer i : elements ) {

			writer.write( tab( amountToTab ) + i.toString() + ( --c == 0 ? "" : "," ) + END );

		}
		writer.write( tab( amountToTab - 1 ) + "]" );

	}

	/**
	 * Writes object to file
	 * 
	 * @param writer
	 * @param elements
	 * @param amountToTab
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings( "unused" )
	private static void makeObject( BufferedWriter writer, Map<String, Integer> elements, int amountToTab )
			throws IOException {

		writer.write( "{" + END );
		int c = elements.keySet().size();

		for ( String key : elements.keySet() ) {

			writer.write( tab( amountToTab ) + quote( key ) + ": " + elements.get( key ) + ( c > 1 ? "," : "" ) + END );
			c--;

		}

		writer.write( tab( amountToTab - 1 ) + "}" + END );

	}

	/**
	 * Writes an object with an inner array to file
	 * 
	 * @param writer
	 * @param treeMap
	 * @param amountToTab
	 * @throws IOException
	 */
	private static void makeObjectWithArray( BufferedWriter writer, TreeMap<String, TreeSet<Integer>> treeMap,
			int amountToTab ) throws IOException {

		writer.write( "{" + END );
		int c = treeMap.keySet().size();

		for ( String s : treeMap.keySet() ) {

			writer.write( tab( amountToTab ) + quote( s ) + ": " );
			JSONWriter.makeArray( writer, treeMap.get( s ), amountToTab + 1 );
			writer.write( c > 1 ? "," : "" );
			writer.write( END );
			c--;

		}

		writer.write( tab( amountToTab - 1 ) + "}" );

	}

	/**
	 * Writes an entire InvertedIndex to file
	 * 
	 * @param elements
	 * @param output
	 * @param amountToTab
	 * @throws IOException
	 */
	private static void pleaseJSON( TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements, Path output,
			int amountToTab ) throws IOException {

		try ( BufferedWriter writer = Files.newBufferedWriter( output, Charset.defaultCharset() ); ) {
			writer.write( "{" + END );
			int c = elements.keySet().size();

			for ( String key : elements.keySet() ) {

				writer.write( tab( amountToTab ) + quote( key ) + ": " );
				makeObjectWithArray( writer, elements.get( key ), amountToTab + 1 );
				writer.write( ( c > 1 ? "," : "" ) + END );
				c--;

			}

			writer.write( tab( amountToTab - 1 ) + "}" + END );
		}
		return;
	}

	/**
	 * Writes an InvertedIndex to the file specified.
	 * 
	 * @param elements
	 * @param output
	 * @throws IOException
	 */
	public static void toJSON( TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements, Path output )
			throws IOException {

		int amountToTab = 1;

		JSONWriter.pleaseJSON( elements, output, amountToTab );
	}

	public static void resultToJSON( Writer writer, String where, int count, int index ) throws IOException {

		writer.write( tab( 2 ) + "{" + END );
		writer.write( tab( 3 ) + quote( "where" ) + ": " + quote( where ) + "," + END );
		writer.write( tab( 3 ) + quote( "count" ) + ": " + count + "," + END );
		writer.write( tab( 3 ) + quote( "index" ) + ": " + index + END );
		writer.write( tab( 2 ) + "}" );

	}

}
