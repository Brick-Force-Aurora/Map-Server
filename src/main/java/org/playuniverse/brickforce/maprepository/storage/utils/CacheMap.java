package org.playuniverse.brickforce.maprepository.storage.utils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheMap<K, V> extends Thread {

	private static long ID = 0;

	protected final ArrayList<CacheEntry<K, V>> entries = new ArrayList<>();

	protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	protected final Lock write = lock.writeLock();
	protected final Lock read = lock.readLock();

	protected final long timeToLive; // Time to live in timer cycles
	protected final long timerInterval; // Timer interval in miliseconds

	protected boolean active = true;

	public CacheMap(long timeToLive, long timerInterval) {
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

	public boolean containsValue(V value) {
		Objects.requireNonNull(value);
		read.lock();
		try {
			return entries.stream().anyMatch(entry -> entry.value.equals(value));
		} finally {
			read.unlock();
		}
	}

	public boolean containsKey(K key) {
		Objects.requireNonNull(key);
		read.lock();
		try {
			return entries.stream().anyMatch(entry -> entry.key.equals(key));
		} finally {
			read.unlock();
		}
	}

	public boolean removeValue(V value) {
		Objects.requireNonNull(value);
		read.lock();
		Optional<CacheEntry<K, V>> option;
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

	public V removeKey(K key) {
		Objects.requireNonNull(key);
		read.lock();
		Optional<CacheEntry<K, V>> option;
		try {
			option = entries.stream().filter(entry -> entry.key.equals(key)).findFirst();
		} finally {
			read.unlock();
		}
		if (option.isEmpty()) {
			return null;
		}
		CacheEntry<K, V> entry = option.get();
		write.lock();
		try {
			entries.remove(entry);
		} finally {
			write.unlock();
		}
		return entry.value;
	}

	public V set(K key, V value) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		read.lock();
		Optional<CacheEntry<K, V>> option;
		try {
			option = entries.stream().filter(entry -> entry.key.equals(key)).findFirst();
		} finally {
			read.unlock();
		}
		return option.orElseGet(() -> newEntry(key, value)).set(value);
	}

	private CacheEntry<K, V> newEntry(K key, V value) {
		CacheEntry<K, V> entry = new CacheEntry<>(timeToLive, key, value);
		write.lock();
		try {
			entries.add(entry);
		} finally {
			write.unlock();
		}
		return entry;
	}

	public V get(K key) {
		Objects.requireNonNull(key);
		read.lock();
		Optional<CacheEntry<K, V>> option;
		try {
			option = entries.stream().filter(entry -> entry.key.equals(key)).findFirst();
		} finally {
			read.unlock();
		}
		return option.map(entry -> entry.get()).orElse(null);
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
			ArrayList<CacheEntry<K, V>> remove = new ArrayList<>();
			read.lock();
			try {
				int size = entries.size();
				for (int index = 0; index < size; index++) {
					CacheEntry<K, V> entry = entries.get(index);
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
				for (CacheEntry<K, V> entry : remove) {
					entries.remove(entry);
				}
			} finally {
				write.unlock();
			}
		}
	}

	private class CacheEntry<T, E> {

		private final long timeToLive;
		private final T key;

		private long timeLeft;
		private E value;

		public CacheEntry(long timeToLive, T key, E value) {
			this.timeToLive = timeToLive;
			this.key = key;
			this.timeLeft = timeToLive;
			this.value = value;
		}

		public E set(E value) {
			E tmp = this.value;
			this.value = value;
			timeLeft = timeToLive;
			return tmp;
		}

		public E get() {
			timeLeft = timeToLive;
			return value;
		}

	}

}
