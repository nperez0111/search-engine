import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

public class HTMLDownloader {

	private static final Logger log = Driver.log;

	private static class PageTask implements Runnable {

		private final URL seed;
		private final ThreadSafeInvertedIndex index;
		private final WorkQueue workers;
		private ThreadSafeURLQueue queue;

		public PageTask( URL seed, ThreadSafeInvertedIndex index, WorkQueue workers, ThreadSafeURLQueue queue ) {
			this.seed = seed;
			this.index = index;
			this.workers = workers;
			this.queue = queue;
			log.info( "Seed:" + seed.toString() );

		}

		@Override
		public void run() {

			String html = HTMLCleaner.fetchHTML( seed.toString() );
			String[] words = HTMLCleaner.parseWords( HTMLCleaner.cleanHTML( html ) );
			InvertedIndexBuilder.parseLine( words, seed.toString(), 1, index );
			if ( queue.canAddMoreURLs() ) {
				List<URL> urls = new ArrayList<>();
				for ( String link : LinkParser.listLinks( html ) ) {
					URL url = normalize( resolve( seed, link ) );

					if ( url != null ) {

						urls.add( url );

					}

				}
				for ( URL url : queue.addAll( urls ) ) {
					log.info( "Added:" + url.toString() );
					workers.execute( new PageTask( url, index, workers, queue ) );

				}

			}

		}

		private URL normalize( URL url ) {

			if ( url == null ) {
				return null;
			}
			try {
				return new URL( url.getProtocol() + "://" + url.getHost() + url.getFile() );
			}
			catch ( MalformedURLException e ) {
				return null;
			}
		}

		private URL resolve( URL url, String link ) {

			try {
				return url.toURI().resolve( link ).toURL();
			}
			catch ( Exception e ) {
				return null;
			}
		}

	}

	public static void parseIntoIndexMultiThread( URL seed, ThreadSafeInvertedIndex index, int threads ) {

		WorkQueue minions = new WorkQueue( threads );
		ThreadSafeURLQueue queue = new ThreadSafeURLQueue();
		log.info( "seed" );
		queue.add( seed );
		log.info( "minions starting" );
		minions.execute( new PageTask( seed, index, minions, queue ) );
		minions.finish();
		minions.shutdown();
		log.info( "minions finish" );
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
				URL url = URLQueue.resolveAgainst( seed, link );
				if ( url != null ) {
					if ( queue.add( url ) == false ) {
						return;
					}
				}
			}
		}

	}
}
