package ru.sberbank.service.entity;

import ru.sberbank.service.common.mapping.Mapping;

import java.util.Objects;

public class User implements Mapping {
	private int id;
	private String name;
	private String lastName;

	public User() {
		this.id = -1;
		this.name = null;
		this.lastName = null;
	}

	public User(int id, String name, String lastName) {
		this();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return (this.name);
	}

	public String getLastName() {
		return (this.lastName);
	}

	public int getId() {
		return (this.id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return id == user.id &&
				Objects.equals(name, user.name) &&
				Objects.equals(lastName, user.lastName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, lastName);
	}

	@Override
	public String toString() {
		return (id + " " + name + " " + lastName);
	}

	@Override
	public void setObject(Object obj) {
		if (obj instanceof String) {
			if (name == null) {
				this.name = (String) obj;
			} else if (lastName == null) {
				this.lastName = (String) obj;
			}
		} else if (obj instanceof Integer) {
			if (this.id < 0) {
				this.id = (Integer) obj;
			}
		}
	}

	@Override
	public Object map() {
		return (this);
	}

	@Override
	public Object createObject() {
		return (new User());
	}
}
