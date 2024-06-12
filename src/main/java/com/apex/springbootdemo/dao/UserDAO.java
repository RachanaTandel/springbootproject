package com.apex.springbootdemo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.apex.springbootdemo.bean.User;

import jakarta.annotation.PostConstruct;

@Repository
public class UserDAO {

	private Connection connection = null;

	@Value("${dbusername}")
	private String dbUsername;

	@Value("${dbpassword}")
	private String dbPassword;

	@PostConstruct
	private void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/training", dbUsername, dbPassword);
	}

	public int addUser(User user) {
		int executeStatus = 0;
		try {
			if (connection != null) {
				PreparedStatement statement = connection
						.prepareStatement("insert into user (name, age, address) values(?,?,?)");
				statement.setString(1, user.getName());
				statement.setInt(2, user.getAge());
				statement.setString(3, user.getAddress());
				executeStatus = statement.executeUpdate();
				System.out.print(executeStatus);
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return executeStatus;
	}

	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		try {
			PreparedStatement statement = connection.prepareStatement("select * from user ");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				User user = new User();
				user.setName(resultSet.getString(2));
				user.setAge(resultSet.getInt(3));
				user.setAddress(resultSet.getString(4));
				users.add(user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public int deleteUser(int id) {
		int executeStatus = 0;
		try {
			if (connection != null) {
				PreparedStatement statement = connection.prepareStatement("delete from user where id=?");
				statement.setInt(1, id);
				executeStatus = statement.executeUpdate();
				System.out.print(executeStatus);
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return executeStatus;
	}

	public User getUser(int id) {
		User user = null;
		try {
			PreparedStatement statement = connection.prepareStatement("select * from user where id = ?");
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				user = new User();
				user.setId(id);
				user.setName(resultSet.getString(2));
				user.setAge(resultSet.getInt(3));
				user.setAddress(resultSet.getString(4));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public User updateUser(User user) {
		int executeStatus = 0;
		try {
			if (connection != null) {
				if (!userExists(user.getId())) {
					System.out.println("User with ID " + user.getId() + " does not exist.");
					return null;
				}

//				PreparedStatement statement = connection.prepareStatement("update user set name=?, age=?, address=? where id=?");
//				statement.setInt(4, user.getId()); 
//				statement.setString(1, user.getName());
//				statement.setInt(2, user.getAge()); 
//				statement.setString(3, user.getAddress());
//				executeStatus = statement.executeUpdate();
//				System.out.print(executeStatus);
//				statement.close();
				StringBuilder queryBuilder = new StringBuilder("update user set ");
				List<Object> params = new ArrayList<>();

				boolean first = true;

				if (user.getName() != null) {
				    if (!first) {
				        queryBuilder.append(", ");
				    }
				    queryBuilder.append("name=?");
				    params.add(user.getName());
				    first = false;
				}

				if (user.getAge() != 0) { 
				    if (!first) {
				        queryBuilder.append(", ");
				    }
				    queryBuilder.append("age=?");
				    params.add(user.getAge());
				    first = false;
				}

				if (user.getAddress() != null) {
				    if (!first) {
				        queryBuilder.append(", ");
				    }
				    queryBuilder.append("address=?");
				    params.add(user.getAddress());
				    first = false;
				}

				if (!params.isEmpty()) {
				    queryBuilder.append(" where id=?");
				    params.add(user.getId());

				    System.out.println("Generated SQL query: " + queryBuilder.toString());
				    System.out.println("Parameters: " + params);

				    try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
				        int index = 1;
				        for (Object param : params) {
				            if (param instanceof String) {
				                statement.setString(index++, (String) param);
				            } else if (param instanceof Integer) {
				                statement.setInt(index++, (Integer) param);
				            }
				        }

				        executeStatus = statement.executeUpdate();
				        System.out.print(executeStatus);
				    }
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}

	private boolean userExists(int userId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("select count(*) from user where id=?");
		statement.setInt(1, userId);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		int count = resultSet.getInt(1);
		resultSet.close();
		statement.close();
		return count > 0;
	}
}