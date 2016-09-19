import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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
public class SimpleJSONWriter {

	/** Tab character used for pretty JSON output. */
	public static final char TAB = '\t';

	/** End of line character used for pretty JSON output. */
	public static final char END = '\n';

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
	 * Writes a {@link TreeSet} of {@link Integer} objects as a "pretty" JSON
	 * array to file using UTF8. Always includes a blank line at the end of the
	 * file. Outputs a warning to the console if an exception is encountered.
	 * 
	 * @param path
	 *            path to write file
	 * @param elements
	 *            set of integer elements
	 * @return true if everything was successful, false otherwise
	 */
	public static boolean writeArray( Path path, TreeSet<Integer> elements, int amountToTab ) {

		/*
		 * TODO: Modify this method as necessary. You may NOT throw any
		 * exceptions, and you must return false if you encounter any
		 * exceptions. Also, when dealing with int-like values, make sure you
		 * use toString() before you write! For example:
		 * 
		 * writer.write(elements.first().toString())
		 */
		try ( BufferedWriter writer = Files.newBufferedWriter( path, Charset.defaultCharset() ); ) {
			writer.write( "[" + END );
			for ( Integer i : elements ) {
				writer.write( tab( amountToTab ) + i.toString() + ( elements.last() == i ? "" : "," ) + END );
			}
			writer.write( tab( amountToTab - 1 ) + "]" + END );
			writer.flush();
			;
			return true;
		}
		catch ( Exception e ) {
			return false;
		}
	}

	public static boolean writeArray( Path p, TreeSet<Integer> el ) {
		return writeArray( p, el, 1 );
	}

	/**
	 * Writes a {@link TreeMap} with {@link String} keys and {@link Integer}
	 * values as a "pretty" JSON object to file using UTF8. Always includes a
	 * blank line at the end of the file. Outputs a warning to the console if an
	 * exception is encountered.
	 * 
	 * @param path
	 *            path to write file
	 * @param elements
	 *            map of elements
	 * @return true if everything was successful, false otherwise
	 */
	public static boolean writeObject( Path path, TreeMap<String, Integer> elements, int amountToTab ) {

		/*
		 * TODO: Modify this method as necessary. You may NOT throw any
		 * exceptions, and you must return false if you encounter any
		 * exceptions. Also, when dealing with int-like values, make sure you
		 * use toString() before you write!
		 */
		try ( BufferedWriter writer = Files.newBufferedWriter( path, Charset.defaultCharset() ); ) {
			writer.write( "{" + END );
			int c = elements.keySet().size();
			for ( String key : elements.keySet() ) {
				writer.write(
						tab( amountToTab ) + quote( key ) + ": " + elements.get( key ) + ( c > 1 ? "," : "" ) + END );
				c--;
			}
			writer.write( tab( amountToTab - 1 ) + "}" + END );
			writer.flush();
			return true;
		}
		catch ( Exception e ) {
			return false;
		}
	}

	public static boolean writeObject( Path p, TreeMap<String, Integer> el ) {
		return writeObject( p, el, 1 );
	}

	/**
	 * Writes a nested {@link TreeMap} with {@link String} keys and values that
	 * are nested {@link TreeSet} collections of {@link Integer} objects as a
	 * "pretty" nested JSON object to file using UTF8. Always includes a blank
	 * line at the end of the file. Outputs a warning to the console if an
	 * exception is encountered.
	 * 
	 * @param path
	 *            path to write file
	 * @param elements
	 *            nested map of elements
	 * @return true if everything was successful, false otherwise
	 */
	public static boolean writeNestedObject( Path path, TreeMap<String, TreeSet<Integer>> elements, int amountToTab ) {

		/*
		 * TODO: Modify this method as necessary. You may NOT throw any
		 * exceptions, and you must return false if you encounter any
		 * exceptions.
		 */
		try ( BufferedWriter writer = Files.newBufferedWriter( path, Charset.defaultCharset() ); ) {
			writer.write( "{" + END );
			int c = elements.keySet().size();
			for ( String s : elements.keySet() ) {
				writer.write( tab( amountToTab ) + quote( s ) + ": " );
				writer.write( "[" + END );
				for ( Integer i : elements.get( s ) ) {
					writer.write( tab( amountToTab + 1 ) + i.toString() + ( elements.get( s ).last() == i ? "" : "," )
							+ END );
				}
				writer.write( tab( amountToTab ) + "]" + ( c > 1 ? "," : "" ) + END );
				c--;
			}
			writer.write( "}" + END );
			writer.flush();
			return true;
		}
		catch ( Exception e ) {
			return false;
		}
	}

	public static boolean writeNestedObject( Path path, TreeMap<String, TreeSet<Integer>> elements ) {
		return writeNestedObject( path, elements, 1 );
	}

	/*
	 * NOTE: You may add methods to this class! There are clever ways to reuse
	 * code if you create helper methods and creative ways to generalize this
	 * code with Generics, but the goal here is to practice file IO and
	 * exception handling so do not get too caught up in design yet.
	 */

	/*
	 * NOTE: This class uses TreeMap and TreeSet only because they are easier to
	 * test for grading. This does not mean you have to use these data
	 * structures in your projects.
	 */

	public static void main( String[] args ) {
		PrintWriter writer = new PrintWriter( System.out );
		Integer i = 65;
		writer.write( i.toString() );
		writer.flush();
	}
}
