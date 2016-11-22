import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class URLQueue {

	private static final List<URL> urls = new ArrayList<>();
	private static final Map<String, URL> map = new HashMap<>();
	private static int count = 0;

	public static final int SIZE = 50;

	/**
	 * adds a url queue to the queue
	 * 
	 * @param url
	 * @return false if queue is full true if the url is in the queue or added
	 *         to the queue
	 */
	public static boolean add( URL url ) {

		if ( urls.size() < SIZE ) {
			if ( !map.containsKey( normalize( url ) ) ) {
				System.out.println( "put: " + normalize( url ) );
				map.put( normalize( url ), url );
				urls.add( url );
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds all of the Urls in a list to the URL queue and returns false if all
	 * of them could not be added
	 * 
	 * @param urls
	 * @return
	 */
	public static boolean addAll( List<URL> urls ) {

		for ( URL url : urls ) {
			if ( add( url ) == false ) {
				return false;
			}
		}
		return true;
	}

	public static boolean hasNext() {

		if ( SIZE == count || count == urls.size() ) {
			return false;
		}
		return true;
	}

	/**
	 * returns the first element in the queue
	 * 
	 * @return url to parse
	 */
	public static URL popQueue() {

		if ( count != SIZE ) {
			count++;
			System.out.println( "pop(" + ( count - 1 ) + "): " + normalize( urls.get( count - 1 ) ) );
			return urls.get( count - 1 );
		}
		return null;
	}

	/**
	 * clears out the urlqueue to reset it
	 */
	public static void clear() {

		urls.clear();
		count = 0;
	}

	/**
	 * normalizes a URL to a consistent string representation
	 * 
	 * @param url
	 * @return
	 */
	private static String normalize( URL url ) {

		return url.getProtocol() + "://" + url.getHost() + url.getFile();
	}

	public static URL resolveAgainst( String url ) {

		try {
			URL r = urls.get( count - 1 ).toURI().resolve( url ).toURL();
			return r;
		}
		catch ( MalformedURLException | URISyntaxException e ) {
			System.out.println( url + "didnt work" );
			return null;
		}
	}

}
