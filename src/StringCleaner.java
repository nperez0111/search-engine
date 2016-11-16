import java.util.Arrays;

/**
 * Provides methods to clean strings used throughout
 * 
 * @author nickthesick
 *
 */
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

		String[] cleanedWords = new String[words.length];

		for ( int i = 0; i < words.length; i++ ) {
			cleanedWords[i] = StringCleaner.normalize( words[i] );
		}

		Arrays.sort( cleanedWords );

		return cleanedWords;

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
