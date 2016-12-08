import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

public class WebCrawler {

	private final ThreadSafeInvertedIndex index;
	private final ThreadSafeURLQueue queue;

	public WebCrawler( URL seed, ThreadSafeInvertedIndex index ) {
		this.index = index;
		queue = new ThreadSafeURLQueue();
		queue.add( seed );
	}

	/*
	 * starts the proccess to begin the crawl
	 */
	public void crawl() {

		do {
			URL popped = queue.popQueue();
			if ( popped != null ) {
				parseIntoIndex( popped );
			}
			else {
				System.out.println( "ran out of elements to proccess" );
				break;
			}
		}
		while ( queue.hasNext() );
	}

	/**
	 * Downloads the url seed provided and inputs all its words into the
	 * inverted index while also building the urlqueue
	 * 
	 * @param base
	 * @param index
	 */
	public void parseIntoIndex( URL base ) {

		String html = HTMLCleaner.fetchHTML( base.toString() );
		String[] words = HTMLCleaner.parseWords( HTMLCleaner.cleanHTML( html ) );
		InvertedIndexBuilder.parseLine( words, base.toString(), 1, index );

		if ( queue.canAddMoreURLs() ) {
			// Goes through all possible urls and if urlqueue is full we stop
			// trying to add

			for ( String link : LinkParser.listLinks( html ) ) {
				URL url = URLQueue.resolveAgainst( base, link );
				if ( url != null ) {
					if ( queue.add( url ) == false ) {
						return;
					}
				}
			}
		}

	}

	private static final Logger log = Driver.log;

	private class PageTask implements Runnable {

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
					URL url = URLQueue.normalize( URLQueue.resolveAgainst( seed, link ) );

					if ( url != null ) {

						urls.add( url );

					}

				}
				log.info( urls.toString() );
				for ( URL url : queue.addAll( urls ) ) {

					log.info( "Added:" + url.toString() );
					workers.execute( new PageTask( url, index, workers, queue ) );

				}

			}

		}

	}

	public void crawlMultiThreaded( int threads ) {

		WorkQueue minions = new WorkQueue( threads );
		URL seed = queue.popQueue();
		log.info( "Seed:" + seed.toString() );
		log.info( "minions starting" );
		minions.execute( new PageTask( seed, index, minions, queue ) );
		minions.finish();
		minions.shutdown();
		log.info( "minions finish" );
	}

}