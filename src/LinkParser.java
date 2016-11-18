import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses links from HTML. Assumes the HTML is valid, and all attributes are
 * properly quoted and URL encoded.
 *
 * <p>
 * See the following link for details on the HTML Anchor tag:
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTML/Element/a"> https:
 * //developer.mozilla.org/en-US/docs/Web/HTML/Element/a </a>
 * 
 * @see LinkTester
 */
public class LinkParser {

	/*
	 * \\p{javaWhitespace}* matches any amount of whitespace
	 */
	public static final String REGEX = "(?is)<a(?:[^>]+?)href\\p{javaWhitespace}*=\\p{javaWhitespace}*\"(.*?)?\"(?:.+?)?>";

	/**
	 * The group in the regular expression that captures the raw link.
	 */
	public static final int GROUP = 1;

	/**
	 * Parses the provided text for HTML links.
	 *
	 * @param text
	 *            - valid HTML code, with quoted attributes and URL encoded
	 *            links
	 * @return list of URLs found in HTML code
	 */
	public static ArrayList<String> listLinks( String text ) {

		// list to store links
		ArrayList<String> links = new ArrayList<String>();

		// compile string into regular expression
		Pattern p = Pattern.compile( REGEX );

		// match provided text against regular expression
		Matcher m = p.matcher( text );

		// loop through every match found in text
		while ( m.find() ) {
			// add the appropriate group from regular expression to list
			links.add( m.group( GROUP ) );
		}
		return links;
	}

	private static ArrayList<URL> listURLs( String text ) {

		ArrayList<URL> urls = new ArrayList<>();
		for ( String link : listLinks( text ) ) {
			try {
				urls.add( new URL( link ) );
			}
			catch ( MalformedURLException e ) {

			}
		}
		return urls;
	}

	public static void search( URL seed, InvertedIndex index ) {

		String html;
		html = HTMLCleaner.fetchHTML( seed.toString() );
		String[] words = HTMLCleaner.parseWords( html );
		InvertedIndexBuilder.parseLine( words, seed.toString(), 1, index );
		ArrayList<URL> links = listURLs( html );

		URLQueue.addAll( links );

	}

}