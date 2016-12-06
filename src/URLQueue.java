import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class URLQueue {

	private final List<URL> urls;
	private final Map<String, URL> map;
	private int count;

	public static final int SIZE = 50;

	public URLQueue() {
		urls = new ArrayList<>();
		map = new HashMap<>();
		count = 0;
	}

	/**
	 * adds a url queue to the queue
	 * 
	 * @param url
	 * @return false if queue is full true if the url is in the queue or added
	 *         to the queue
	 */
	public boolean add( URL url ) {

		String normalizedURL = normalize( url );
		try {
			url = new URL( normalizedURL );
		}
		catch ( MalformedURLException e ) {
			return false;
		}
		if ( urls.size() < SIZE ) {
			if ( map.containsKey( normalizedURL ) ) {
				return true;
			}
			else {
				Driver.log.trace( "put: " + normalizedURL );
				map.put( normalizedURL, url );
				urls.add( url );
				return true;
			}
		}
		return false;
	}

	public boolean specialAdd( URL u ) {

		if ( map.containsKey( normalize( u ) ) ) {

			return false;
		}
		if ( add( u ) == false ) {
			return false;
		}

		return true;
	}

	/**
	 * Adds all of the Urls in a list to the URL queue and returns false if all
	 * of them could not be added
	 * 
	 * @param urls
	 * @return / public int addAll( List<URL> urls ) {
	 * 
	 *         int c = 0; for ( URL url : urls ) { if ( map.containsKey(
	 *         url.toString() ) ) {
	 * 
	 *         } else { if ( add( url ) == false ) { return c; } c++; } } return
	 *         c; }
	 */

	/**
	 * Returns whether or not there are more urls to process
	 * 
	 * @return boolean
	 */
	public boolean hasNext() {

		if ( SIZE == count || count == urls.size() ) {
			return false;
		}
		return true;
	}

	/**
	 * returns true if the urlqueue can add more urls
	 * 
	 * @return boolean
	 */
	public boolean canAddMoreURLs() {

		return urls.size() < SIZE;
	}

	/**
	 * returns the first element in the queue
	 * 
	 * @return url to parse
	 */
	public URL popQueue() {

		if ( canProccessMoreURLs() ) {
			count++;
			// System.out.println( "pop(" + ( count - 1 ) + "): " + normalize(
			// urls.get( count - 1 ) ) );
			return urls.get( count - 1 );
		}
		return null;
	}

	/**
	 * clears out the urlqueue to reset it
	 */
	public void clear() {

		urls.clear();
		map.clear();
		count = 0;
	}

	/**
	 * normalizes a URL to a consistent string representation
	 * 
	 * @param url
	 * @return
	 */
	private String normalize( URL url ) {

		return url.getProtocol() + "://" + url.getHost() + url.getFile();
	}

	/**
	 * Resolves a URL against the current URL ( Assumed to be the seed URL )
	 * 
	 * @param url
	 * @return
	 */
	public URL resolveAgainst( String url ) {

		try {
			return urls.get( count - 1 ).toURI().resolve( url ).toURL();
		}
		catch ( MalformedURLException | URISyntaxException | IllegalArgumentException e ) {
			return null;
		}
	}

	/**
	 * returns whether or not the queue is full
	 * 
	 * @return
	 */
	public boolean canProccessMoreURLs() {

		return count != SIZE;
	}

	public int getSize() {

		return urls.size();
	}

}
