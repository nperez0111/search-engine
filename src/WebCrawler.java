import java.net.URL;

public class WebCrawler {

	private final InvertedIndex index;
	private final URLQueue queue;

	public WebCrawler( URL seed, InvertedIndex index ) {
		this.index = index;
		queue = new URLQueue();
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
}