import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class PartialSearchInvertedIndex {

	public static InvertedIndex search( Path inputFile, Path outputFile, InvertedIndex index ) throws IOException {

		List<String> queries = PartialSearchInvertedIndex.getSearchQueries( inputFile );

		// This stores the list of results returned per query
		List<List<Result>> results = new ArrayList<>();

		for ( String query : queries ) {
			results.add( index.search( query, true ) );
		}

		return null;

	}

	private static List<String> getSearchQueries( Path inputFile ) throws IOException {

		List<String> list = new ArrayList<>();
		try ( BufferedReader reader = Files.newBufferedReader( inputFile, Charset.forName( "UTF8" ) ); ) {
			String line = null;
			while ( ( line = reader.readLine() ) != null ) {
				list.add( StringCleaner.stripNonAlphaNumeric( line ) );
			}
		}
		return list;
	}
}
