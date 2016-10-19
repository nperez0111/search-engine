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
		if ( outputFile == null ) {
			return index;
		}
		outputToFile( outputFile, results );

		return null;

	}

	private static void outputToFile( Path outputFile, List<List<Result>> results ) throws IOException {

		for ( List<Result> perQuery : results ) {
			for ( Result result : perQuery ) {
				result.toJSON( outputFile );
			}
		}

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
