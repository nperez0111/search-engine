import java.net.URL;
import java.nio.file.Path;

public class HTMLDownloader {

	private static class PageTask implements Runnable {

		private final URL url;
		private final ThreadSafeInvertedIndex index;

		public PageTask( URL url, ThreadSafeInvertedIndex index ) {
			this.url = url;
			this.index = index;
		}

		@Override
		public void run() {

		}

	}

	public static void parseIntoIndexMultiThread( URL seed, InvertedIndex index, URLQueue queue, int threads ) {

		WorkQueue minions = new WorkQueue( threads );

		minions.finish();
		minions.shutdown();
	}

	/**
	 * Downloads the url seed provided and inputs all its words into the
	 * inverted index while also building the urlqueue
	 * 
	 * @param seed
	 * @param index
	 * @param queue
	 */
	public static void parseIntoIndex( URL seed, InvertedIndex index, URLQueue queue ) {

		String html = HTMLCleaner.fetchHTML( seed.toString() );
		String[] words = HTMLCleaner.parseWords( HTMLCleaner.cleanHTML( html ) );
		InvertedIndexBuilder.parseLine( words, seed.toString(), 1, index );

		if ( queue.canAddMoreURLs() ) {
			// Goes through all possible urls and if urlqueue is full we stop
			// trying to add

			for ( String link : LinkParser.listLinks( html ) ) {
				URL url = queue.resolveAgainst( link );
				if ( url != null ) {
					if ( queue.add( url ) == false ) {
						return;
					}
				}
			}
		}

	}
}
