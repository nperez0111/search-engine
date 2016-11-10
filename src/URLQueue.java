import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class URLQueue {

	private static final List<URL> urls = new ArrayList<>();
	private static int count = 0;

	public static final int SIZE = 50;

	public static boolean addToQueue( URL url ) {

		if ( urls.size() < SIZE ) {
			for ( URL u : urls ) {
				if ( u.sameFile( url ) ) {
					return false;
				}
			}
			urls.add( url );
			return true;
		}
		return false;
	}

	public static URL popQueue() {

		if ( count + 1 < SIZE ) {
			count++;
			return urls.get( count - 1 );
		}
		return null;
	}

	public static void clear() {

		urls.clear();
		count = 0;
	}

}
