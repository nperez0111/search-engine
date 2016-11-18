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
	public static boolean add( URL url ) {

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
	 * <<<<<<< HEAD Adds all of the Urls in a list to the URL queue and returns
	 * false if all of them could not be added
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

	/**
	 * ======= >>>>>>> e7cf70aa76b3da0948fad039145679b7d7fc2953 returns the
	 * first element in the queue
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
