package ru.sberbank.service;

public interface Crud<K, T> {
	boolean create(T obj);
	T read(K key);
	boolean update(T obj);
	boolean delete(K key);
}
