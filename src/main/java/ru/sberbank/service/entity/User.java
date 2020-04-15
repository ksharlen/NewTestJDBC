package ru.sberbank.service.entity;

import java.util.Objects;

public class User {
	private final int id;
	private String name;
	private String lastName;

	public User(int id, String name, String lastName) {
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
}
