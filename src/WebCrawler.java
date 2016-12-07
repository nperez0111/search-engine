import java.net.URL;

public class WebCrawler {

	private final URL seed;
	private final InvertedIndex index;

	public WebCrawler( URL seed, InvertedIndex index ) {
		this.seed = seed;
		this.index = index;
		URLQueue.add( seed );
	}

	public void crawl() {

		do {
			URL popped = URLQueue.popQueue();
			if ( popped != null ) {
				LinkParser.search( popped, index );
			}
			else {
				System.out.println( "ran out of elements to proccess" );
				break;
			}
		}
		while ( URLQueue.hasNext() );
		URLQueue.clear();
	}
}
