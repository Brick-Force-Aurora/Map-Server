package org.playuniverse.brickforce.maprepository.storage.utils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache<E> extends Thread {

	private static long ID = 0;

	protected final ArrayList<CacheEntry<E>> entries = new ArrayList<>();

	protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	protected final Lock write = lock.writeLock();
	protected final Lock read = lock.readLock();

	protected final long timeToLive; // Time to live in timer cycles
	protected final long timerInterval; // Timer interval in miliseconds

	protected boolean active = true;

	public Cache(long timeToLive, long timerInterval) {
		this.timeToLive = timeToLive;
		this.timerInterval = timerInterval;
		setName("Cache " + (ID++));
		start();
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public long getTimeToLiveInMilis() {
		return timeToLive * timerInterval;
	}

	public long getTimerInterval() {
		return timerInterval;
	}

	public void dispose() {
		active = false;
		write.lock();
		try {
			entries.clear();
		} finally {
			write.unlock();
		}
	}

	public int size() {
		read.lock();
		try {
			return entries.size();
		} finally {
			read.unlock();
		}
	}

	public boolean contains(E value) {
		read.lock();
		try {
			return entries.stream().anyMatch(entry -> entry.value.equals(value));
		} finally {
			read.unlock();
		}
	}

	public boolean remove(E value) {
		read.lock();
		Optional<CacheEntry<E>> option;
		try {
			option = entries.stream().filter(entry -> entry.value.equals(value)).findFirst();
		} finally {
			read.unlock();
		}
		if (option.isEmpty()) {
			return false;
		}
		write.lock();
		try {
			return entries.remove(option.get());
		} finally {
			write.unlock();
		}
	}

	public E remove(int index) {
		write.lock();
		try {
			return entries.remove(index).get();
		} finally {
			write.unlock();
		}
	}

	public void add(E value) {
		write.lock();
		try {
			entries.add(new CacheEntry<>(timeToLive, value));
		} finally {
			write.unlock();
		}
	}

	public E first() {
		return get(0);
	}

	public E get(int index) {
		read.lock();
		try {
			return entries.get(index).get();
		} finally {
			read.unlock();
		}
	}

	public E last() {
		read.lock();
		try {
			return entries.get(entries.size() - 1).get();
		} finally {
			read.unlock();
		}
	}

	@Override
	public void run() {
		while (active) {
			try {
				sleep(timerInterval);
			} catch (InterruptedException e) {
				dispose();
				return;
			}
			ArrayList<CacheEntry<E>> remove = new ArrayList<>();
			read.lock();
			try {
				int size = entries.size();
				for (int index = 0; index < size; index++) {
					CacheEntry<E> entry = entries.get(index);
					entry.timeLeft--;
					if (entry.timeLeft != 0) {
						continue;
					}
					remove.add(entry);
				}
			} finally {
				read.unlock();
			}
			if (remove.isEmpty()) {
				continue;
			}
			write.lock();
			try {
				for (CacheEntry<E> entry : remove) {
					entries.remove(entry);
				}
			} finally {
				write.unlock();
			}
		}
	}

	private class CacheEntry<T> {

		private final long timeToLive;
		private final T value;

		private long timeLeft;

		public CacheEntry(long timeToLive, T value) {
			this.timeToLive = timeToLive;
			this.timeLeft = timeToLive;
			this.value = value;
		}

		public T get() {
			timeLeft = timeToLive;
			return value;
		}

	}

}
