import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class StringCleaner {

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
	private static String normalize( String s ) {

		return StringCleaner.stripNonAlphaNumeric( s ).toLowerCase();
	}

	/**
	 * Cleans up a list of Strings and returns them in sorted order
	 * 
	 * @param list
	 * @return sorted list of cleaned strings
	 */
	public static List<String> cleanAndSort( List<String> list ) {

		List<String> ret = new ArrayList<>();

		for ( String element : list ) {

			ret.add( StringCleaner.normalize( element ) );

		}

		ret.sort( new Comparator<String>() {

			@Override
			public int compare( String a, String b ) {

				return a.compareTo( b );
			}
		} );

		return ret;

	}

	public static List<String> cleanAndSort( String s ) {

		return cleanAndSort( new ArrayList<String>( Arrays.asList( s.split( " " ) ) ) );
	}

}
