import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ThreadSafeInvertedIndex extends InvertedIndex {

	private ReadWriteLock lock;

	public ThreadSafeInvertedIndex() {
		super();
		lock = new ReadWriteLock();
	}

	@Override
	public void add( String word, String path, int position ) {

		lock.lockReadWrite();
		super.add( word, path, position );
		lock.unlockReadWrite();

	}

	@Override
	public void add( String word, Path path, int position ) {

		lock.lockReadWrite();
		super.add( word, path, position );
		lock.unlockReadWrite();

	}

	@Override
	public int countOfFilesWithWord( String word ) {

		lock.lockReadOnly();
		int ret = super.countOfFilesWithWord( word );
		lock.unlockReadOnly();
		return ret;
	}

	@Override
	public int words() {

		lock.lockReadOnly();
		int ret = super.words();
		lock.unlockReadOnly();
		return ret;
	}

	@Override
	public List<Result> exactSearch( String[] queries ) {

		lock.lockReadOnly();
		List<Result> results = super.exactSearch( queries );
		lock.unlockReadOnly();
		return results;
	}

	@Override
	public List<Result> partialSearch( String[] queries ) {

		lock.lockReadOnly();
		List<Result> results = super.partialSearch( queries );
		lock.unlockReadOnly();
		return results;
	}

	@Override
	public void searchSomething( String match, String word, Map<String, Result> map, List<Result> results ) {

		lock.lockReadOnly();
		super.searchSomething( match, word, map, results );
		lock.unlockReadOnly();
	}

	@Override
	public List<Result> search( String[] queries, boolean partial ) {

		lock.lockReadOnly();
		List<Result> results = super.search( queries, partial );
		lock.unlockReadOnly();
		return results;
	}

	@Override
	public boolean contains( String word ) {

		lock.lockReadOnly();
		boolean result = super.contains( word );
		lock.unlockReadOnly();
		return result;
	}

	@Override
	public void toJSON( Path output ) throws IOException {

		lock.lockReadOnly();
		super.toJSON( output );
		lock.unlockReadOnly();
	}
}
