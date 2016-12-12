import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

public class MultiThreadedWebCrawler extends WebCrawler {

	private static final Logger log = Driver.log;
	private final ThreadSafeInvertedIndex index;
	private final int threads;
	ReadWriteLock lock;

	public MultiThreadedWebCrawler( ThreadSafeInvertedIndex index, int threads ) {
		super( index );
		this.index = index;
		this.threads = threads;
		lock = new ReadWriteLock();
	}

	@Override
	public boolean add( URL url ) {

		lock.lockReadWrite();
		boolean success = super.add( url );
		lock.unlockReadWrite();
		return success;
	}

	@Override
	public boolean hasNext() {

		lock.lockReadWrite();
		boolean success = super.hasNext();
		lock.unlockReadWrite();
		return success;
	}

	@Override
	public URL popQueue() {

		lock.lockReadWrite();
		URL popped = super.popQueue();
		lock.unlockReadWrite();
		return popped;
	}

	/**
	 * this class holds all the appropriate info to parse a page into an
	 * inverted index
	 * 
	 * @author nickthesick
	 *
	 */
	private class PageTask implements Runnable {

		private final URL seed;
		private final ThreadSafeInvertedIndex index;
		private final WorkQueue workers;

		public PageTask( URL seed, ThreadSafeInvertedIndex index, WorkQueue workers ) {
			this.seed = seed;
			this.index = index;
			this.workers = workers;
			log.info( "Seed:" + seed.toString() );

		}

		@Override
		public void run() {

			String html = HTMLCleaner.fetchHTML( seed.toString() );
			String[] words = HTMLCleaner.parseWords( HTMLCleaner.cleanHTML( html ) );
			InvertedIndexBuilder.parseLine( words, seed.toString(), 1, index );

			if ( canAddMoreURLs() ) {

				List<URL> urls = new ArrayList<>();
				for ( String link : LinkParser.listLinks( html ) ) {
					URL url = normalize( resolveAgainst( seed, link ) );

					if ( url != null ) {

						urls.add( url );

					}

				}
				log.info( urls.toString() );
				for ( URL url : addAll( urls ) ) {

					log.info( "Added:" + url.toString() );
					workers.execute( new PageTask( url, index, workers ) );

				}

			}

		}

	}

	/**
	 * This crawl parses the seed which will then start doing a webcrawl to
	 * other pages parsing them in as well.
	 */
	@Override
	public void crawl( URL seed ) {

		WorkQueue minions = new WorkQueue( threads );
		log.info( "Seed:" + seed.toString() );
		log.info( "minions starting" );
		add( seed );
		minions.execute( new PageTask( popQueue(), index, minions ) );
		minions.finish();
		minions.shutdown();
		log.info( "minions finish" );
	}

}
