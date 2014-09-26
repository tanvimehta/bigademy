package com.bigademy.dao;

public interface ICrudDao<T> {
	public T read(Long id);

	public void update(T o);

	public void delete(T o);

	public void delete(Long id);
}
