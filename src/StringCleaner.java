import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class StringCleaner { // TODO Not abstract!

	/**
	 * Cleans up the string removing any non alphanumeric character
	 * 
	 * @param s
	 * @return
	 */
	public static String stripNonAlphaNumeric( String s ) {

		return s.trim().replaceAll( "\\p{Punct}+", "" ).toLowerCase();
	}

	/**
	 * Cleans up a string and makes it lower case in order to normalize
	 * comparison with other Strings
	 * 
	 * @param s
	 * @return
	 */
	private static String normalize( String s ) { // TODO Avoid 1 letter variable names unless in a for loop

		return StringCleaner.stripNonAlphaNumeric( s ).toLowerCase();
	}

	/**
	 * Cleans up a list of Strings and returns them in sorted order
	 * 
	 * @param list
	 * @return sorted list of cleaned strings
	 */
	public static List<String> cleanAndSort( List<String> list ) { // TODO String[] words

		// TODO ret is not the best variable name
		List<String> ret = new ArrayList<>();

		for ( String element : list ) {
			if ( element.equals( "" ) == false ) {
				ret.add( StringCleaner.normalize( element ) );
			}
		}
		
		// TODO Arrays.sort();

		// TODO Remove comparator?
		ret.sort( new Comparator<String>() {

			@Override
			public int compare( String a, String b ) {

				return a.compareTo( b );
			}
		} );

		return ret;

	}

	/**
	 * cleans the string and splits it by word chars
	 * 
	 * @param s
	 * @return
	 */
	public static List<String> cleanAndSort( String s ) {

		return cleanAndSort( new ArrayList<String>( Arrays.asList( StringCleaner.normalize( s ).split( "\\s+" ) ) ) );
	}

}
