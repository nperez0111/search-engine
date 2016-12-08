import java.net.URL;

import org.apache.logging.log4j.Logger;

public class ThreadSafeURLQueue extends URLQueue {

	private Logger log = Driver.log;
	private ReadWriteLock lock;

	public ThreadSafeURLQueue() {
		super();
		lock = new ReadWriteLock();
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

		lock.lockReadWrite();
		URL url = super.popQueue();
		lock.unlockReadWrite();
		return url;
	}

	@Override
	public boolean add( URL url ) {

		lock.lockReadWrite();
		boolean success = super.add( url );
		lock.unlockReadWrite();
		return success;
	}

}
