import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class URLQueue {

	private static final List<URL> urls = new ArrayList<>();
	private static int count = 0;

	public static final int SIZE = 50;

	/**
	 * adds a url queue to the queue
	 * 
	 * @param url
	 * @return false if queue is full true if the url is in the queue or added
	 *         to the queue
	 */
	public static boolean addToQueue( URL url ) {

		if ( urls.size() < SIZE ) {
			for ( URL u : urls ) {
				if ( u.sameFile( url ) ) {
					return true;
				}
			}
			urls.add( url );
			return true;
		}
		return false;
	}

	/**
	 * returns the first element in the queue
	 * 
	 * @return url to parse
	 */
	public static URL popQueue() {

		if ( count + 1 < SIZE ) {
			count++;
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

}
