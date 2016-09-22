import java.util.Arrays;
import java.util.Map;
import java.util.Set;

// TODO Make a similar JSONWriter class to avoid storing in a StringBuffer

/**
 * Outputs certain Java objects to a file in a "pretty" JSON format using the
 * tab character to indent nested elements and the new line character to
 * separate multiple elements. Only a small number of objects are currently
 * supported.
 * 
 * @see <a href="http://json.org/">http://json.org/</a>
 */
public class JSONBuilder {

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
	private static String quote( String text ) {

		return String.format( "\"%s\"", text );
	}

	/**
	 * Returns n tab characters.
	 * 
	 * @param n
	 *            number of tab characters
	 * @return n tab characters concatenated
	 */
	private static String tab( int n ) {

		char[] tabs = new char[n];
		Arrays.fill( tabs, TAB );
		return String.valueOf( tabs );
	}

	private static StringBuilder makeArray( Set<Integer> elements, int amountToTab ) {

		StringBuilder str = new StringBuilder();
		str.append( "[" + END );
		int c = elements.size();

		for ( Integer i : elements ) {

			str.append( tab( amountToTab ) + i.toString() + ( --c == 0 ? "" : "," ) + END );

		}
		str.append( tab( amountToTab - 1 ) + "]" );
		return str;
	}

	public static StringBuilder makeArray( Set<Integer> el ) {

		return makeArray( el, 1 );
	}

	private static StringBuilder makeObject( Map<String, Integer> elements, int amountToTab ) {

		StringBuilder str = new StringBuilder();
		str.append( "{" + END );
		int c = elements.keySet().size();

		for ( String key : elements.keySet() ) {

			str.append( tab( amountToTab ) + quote( key ) + ": " + elements.get( key ) + ( c > 1 ? "," : "" ) + END );
			c--;

		}

		str.append( tab( amountToTab - 1 ) + "}" + END );

		return str;
	}

	public static StringBuilder makeObject( Map<String, Integer> el ) {

		return makeObject( el, 1 );
	}

	private static StringBuilder makeObjectWithArray( Map<String, Set<Integer>> elements, int amountToTab ) {

		StringBuilder str = new StringBuilder();
		str.append( "{" + END );
		int c = elements.keySet().size();

		for ( String s : elements.keySet() ) {

			str.append( tab( amountToTab ) + quote( s ) + ": " );
			str.append( JSONBuilder.makeArray( elements.get( s ), amountToTab + 1 ) );
			str.append( c > 1 ? "," : "" );
			str.append( END );
			c--;

		}

		str.append( tab( amountToTab - 1 ) + "}" );

		return str;
	}

	public static StringBuilder makeObjectWithArray( Map<String, Set<Integer>> elements ) {

		return makeObjectWithArray( elements, 1 );
	}

	public static StringBuilder makeInvertedIndexJSON( Map<String, Map<String, Set<Integer>>> elements,
			int amountToTab ) {

		StringBuilder str = new StringBuilder();
		str.append( "{" + END );
		int c = elements.keySet().size();

		for ( String key : elements.keySet() ) {

			str.append( tab( amountToTab ) + quote( key ) + ": " );
			str.append( makeObjectWithArray( elements.get( key ), amountToTab + 1 ) );
			str.append( ( c > 1 ? "," : "" ) + END );
			c--;

		}

		str.append( tab( amountToTab - 1 ) + "}" + END );

		return str;
	}

	public static StringBuilder makeInvertedIndexJSON( Map<String, Map<String, Set<Integer>>> elements ) {

		return makeInvertedIndexJSON( elements, 1 );
	}
}
