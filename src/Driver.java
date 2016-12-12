
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is the driver class that calls all the other classes to build the
 * inverted index and output it into file as well as search it
 * 
 * @author Nick The Sick
 *
 */
public class Driver {

	private final static String DIR = "-dir";
	private final static String INDEX = "-index";
	private final static String QUERY = "-query";
	private final static String RESULTS = "-results";
	private final static String EXACT = "-exact";
	private final static String UrlFlag = "-url";
	private final static String MULTI = "-multi";
	protected static Logger log = LogManager.getLogger();

	/**
	 * First method to be called parses arguments and calls the correct methods
	 * with the arguments passed in
	 * 
	 * @param args
	 */
	public static void main( String[] args ) {

		ArgumentParser parser = new ArgumentParser( args );
		ThreadSafeInvertedIndex index = new ThreadSafeInvertedIndex();
		SearchInvertedIndex search = new SearchInvertedIndex( index );

		if ( parser.hasValue( DIR ) ) {
			Path inputIndex = getValidDirectory( parser.getValue( DIR ) );
			if ( inputIndex != null ) {
				try {
					if ( parser.hasValue( MULTI ) ) {
						InvertedIndexBuilder.buildMultiThreaded( inputIndex, index, parser.getValue( MULTI, 5 ) );
					}
					else {
						InvertedIndexBuilder.build( inputIndex, index );
					}
				}
				catch ( IOException e ) {
					System.out.println( "Directory " + inputIndex.toString() + ", Not Found or Non-Existant" );
				}
			}
		}

		if ( parser.hasValue( UrlFlag ) ) {

			String url = parser.getValue( UrlFlag );
			log.info( url );

			URL seed = null;
			try {
				seed = new URL( url );
			}
			catch ( MalformedURLException e ) {
				System.out.println( "Invalid URL Passed" );
				return;
			}
			WebCrawler crawler;

			if ( parser.hasFlag( MULTI ) ) {
				crawler = new MultiThreadedWebCrawler( index, parser.getValue( MULTI, 5 ) );
				crawler.crawl( seed );

			}
			else {
				crawler = new WebCrawler( index );
				crawler.crawl( seed );

			}

		}

		if ( parser.hasFlag( INDEX ) ) {
			Path outputIndex = parser.getPath( INDEX, "index.json" );

			if ( outputIndex == null ) {
				System.out.println( "outputfile is not available" );
			}
			else {
				try {
					index.toJSON( outputIndex );
				}
				catch ( IOException e ) {
					System.out.println( "Outputting index to: " + outputIndex.toString() + " failed" );
				}

			}
		}

		if ( parser.hasValue( EXACT ) ) {
			Path queryFile = parser.getPath( EXACT );

			if ( queryFile == null ) {
				log.error( "Query file is not an actual path." );
				return;
			}
			try {
				if ( parser.hasFlag( MULTI ) ) {
					search.exactMultiThreaded( queryFile, parser.getValue( MULTI, 5 ) );
				}
				else {
					search.exact( queryFile );
				}
			}
			catch ( IOException e ) {
				log.error( "Exact Searching Inverted Index Failed" );
			}
		}

		if ( parser.hasValue( QUERY ) ) {
			Path queryFile = parser.getPath( QUERY );

			if ( queryFile == null ) {
				System.out.println( "Query file is not an actual path." );
				return;
			}
			try {
				if ( parser.hasFlag( MULTI ) ) {
					search.partialMultiThreaded( queryFile, parser.getValue( MULTI, 5 ) );
				}
				else {
					search.partial( queryFile );
				}
			}
			catch ( IOException e ) {
				System.out.println( "Partial Searching Inverted Index Failed" );
			}
		}

		if ( parser.hasFlag( RESULTS ) ) {

			Path outputResult = parser.getPath( RESULTS, "results.json" );
			if ( outputResult == null ) {
				System.out.println( "output file does not exist." );
				return;
			}
			search.outputResults( outputResult );

		}

	}

	/**
	 * gets a valid directory and outputs a message if it does not exist
	 * 
	 * @param path
	 * @return
	 */
	private static Path getValidDirectory( String path ) {

		Path dir = Paths.get( path );
		if ( dir == null ) {
			System.out.println( "The Directory: " + path + " you specified does not exist..." );
			return null;
		}
		return dir.normalize();
	}
}
