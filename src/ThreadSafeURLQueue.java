import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

public class ThreadSafeURLQueue extends URLQueue {

	private Logger log = Driver.log;
	private ReadWriteLock lock;

	public ThreadSafeURLQueue() {
		super();
		lock = new ReadWriteLock();
	}

	public boolean addToQueue( URL url ) {

		lock.lockReadWrite();
		boolean success = super.specialAdd( url );
		lock.unlockReadWrite();
		return success;
	}

	public List<URL> addAll( List<URL> urls ) {

		List<URL> list = new ArrayList<>();
		for ( URL url : urls ) {

			if ( addToQueue( url ) ) {
				log.debug( "URL:" + url.toString() );
				list.add( url );
			}

		}
		return list;
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
