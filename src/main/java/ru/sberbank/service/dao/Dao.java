package ru.sberbank.service.dao;

public interface Dao<K, T> {
	boolean create(T obj);
	T read(K key);
	boolean update(T obj);
	boolean delete(K key);
}
