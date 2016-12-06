import java.net.URL;
import java.util.List;

public class ThreadSafeURLQueue extends URLQueue {

	private ReadWriteLock lock;

	public ThreadSafeURLQueue() {
		super();
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
	public boolean addAll( List<URL> urls ) {

		lock.lockReadWrite();
		boolean success = super.addAll( urls );
		lock.unlockReadWrite();
		return success;
	}

	@Override
	public boolean hasNext() {

		lock.lockReadOnly();
		boolean success = super.hasNext();
		lock.unlockReadOnly();
		return success;
	}

	@Override
	public boolean canAddMoreURLs() {

		lock.lockReadOnly();
		boolean success = super.canAddMoreURLs();
		lock.unlockReadOnly();
		return success;
	}

	@Override
	public URL popQueue() {

		lock.lockReadOnly();
		URL url = super.popQueue();
		lock.unlockReadOnly();
		return url;
	}

	@Override
	public void clear() {

		lock.lockReadWrite();
		super.clear();
		lock.unlockReadWrite();
	}

	@Override
	public URL resolveAgainst( String url ) {

		lock.lockReadOnly();
		URL cleanUrl = super.resolveAgainst( url );
		lock.unlockReadOnly();
		return cleanUrl;
	}

	@Override
	public boolean canProccessMoreURLs() {

		lock.lockReadOnly();
		boolean success = super.canProccessMoreURLs();
		lock.unlockReadOnly();
		return success;
	}

}
