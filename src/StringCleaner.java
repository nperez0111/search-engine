import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringCleaner {

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
	 * @param word
	 * @return
	 */
	private static String normalize( String word ) {

		return StringCleaner.stripNonAlphaNumeric( word ).toLowerCase();
	}

	/**
	 * Cleans up a list of Strings and returns them in sorted order
	 * 
	 * @param list
	 * @return sorted list of cleaned strings
	 */
	public static String[] cleanAndSort( String[] words ) {

		List<String> cleanedWords = new ArrayList<>();

		for ( String element : words ) {
			if ( element.equals( "" ) == false ) {
				cleanedWords.add( StringCleaner.normalize( element ) );
			}
		}

		Collections.sort( cleanedWords );

		return cleanedWords.toArray( new String[cleanedWords.size()] );

	}

	/**
	 * cleans the string and splits it by word chars
	 * 
	 * @param s
	 * @return
	 */
	public static String[] cleanAndSort( String s ) {

		return cleanAndSort( StringCleaner.normalize( s ).split( "\\s+" ) );
	}

}
