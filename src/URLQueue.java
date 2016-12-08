import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class URLQueue {

	private final Queue<URL> queue;
	private final Set<String> urlsSeen;
	public static final int SIZE = 50;

	public URLQueue() {
		queue = new LinkedList<>();
		urlsSeen = new HashSet<>();
	}

	/**
	 * adds a url queue to the queue
	 * 
	 * @param url
	 * @return false if queue is full true if the url is in the queue or added
	 *         to the queue
	 */
	public boolean add( URL url ) {

		url = normalize( url );
		String urlString = url.toString();
		if ( urlsSeen.size() < SIZE ) {
			if ( urlsSeen.contains( urlString ) ) {
				return true;
			}
			else {
				Driver.log.info( url.toString() );
				urlsSeen.add( urlString );
				queue.add( url );
				return true;
			}
		}
		return false;
	}

	public List<URL> addAll( List<URL> urls ) {

		List<URL> list = new ArrayList<>();
		for ( URL url : urls ) {

			if ( specialAdd( url ) ) {
				list.add( url );
			}

		}
		return list;
	}

	private boolean specialAdd( URL url ) {

		if ( urlsSeen.contains( url.toString() ) ) {
			return false;
		}
		return add( url );
	}

	/**
	 * Returns whether or not there are more urls to process
	 * 
	 * @return boolean
	 */
	public boolean hasNext() {

		return queue.size() > 0;
	}

	/**
	 * returns true if the urlqueue can add more urls
	 * 
	 * @return boolean
	 */
	public boolean canAddMoreURLs() {

		return urlsSeen.size() < SIZE;
	}

	/**
	 * returns the first element in the queue
	 * 
	 * @return url to parse
	 */
	public URL popQueue() {

		return queue.remove();
	}

	/**
	 * normalizes a URL to a consistent string representation
	 * 
	 * @param url
	 * @return
	 */
	public static URL normalize( URL url ) {

		try {
			return new URL( url.getProtocol(), url.getHost(), url.getFile() );
		}
		catch ( MalformedURLException e ) {
			return null;
		}
	}

	/**
	 * Resolves a URL against the current URL ( Assumed to be the seed URL )
	 * 
	 * @param url
	 * @return
	 */
	public static URL resolveAgainst( URL base, String url ) {

		try {
			return base.toURI().resolve( url ).toURL();
		}
		catch ( MalformedURLException | URISyntaxException | IllegalArgumentException e ) {
			return null;
		}
	}

}