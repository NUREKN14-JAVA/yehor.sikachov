package com.nixsolutions.usermanagement.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

import com.nixsolutions.usermanagement.User;

class HsqldbUserDao implements UserDao {

	private static final String DELETE_FROM_USERS = "DELETE FROM users WHERE id=?";

	private static final String SELECT_ID = "SELECT id, firstname, lastname, dateofbirth FROM users WHERE id=?";

	private static final String UPDATE_USERS = "UPDATE users SET firstname=?, lastname=?, dateofbirth=? WHERE id=?";

	private static final String SELECT_ALL_QUERY = "SELECT id, firstname, lastname, dateofbirth FROM users";

	private static final String INSERT_QUERY = "INSERT INTO users (firstname, lastname, dateofbirth) VALUES (?, ?, ?)";
	
	ConnectionFactory connectionFactory;
	
	private static final String SELECT_BY_NAMES = "SELECT id, firstname, lastname, dateofbirth FROM users WHERE firstname=? AND lastname=?";
	
	public HsqldbUserDao() {

	}

	public HsqldbUserDao(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public User create(User user) throws DatabaseException {
		try {
			Connection connection = connectionFactory.createConnection();
			PreparedStatement statement = connection.prepareStatement(INSERT_QUERY);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setDate(3, new Date(user.getDateOfBirth().getTime()));
            int n = statement.executeUpdate();
            if (n != 1) {
                throw new DatabaseException("Number of the inserted rows: " + n);
            }
            CallableStatement callableStatement = connection.prepareCall("call IDENTITY()");
            ResultSet keys = callableStatement.executeQuery();
            if (keys.next()){
            	user.setId(new Long(keys.getLong(1)));
            }
            keys.close();
            callableStatement.close();
            statement.close();
            connection.close();
			return user;
        } catch (DatabaseException e) {
            throw e;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
	}

	@Override
	public void update(User user) throws DatabaseException {
		try {
			Connection connection = connectionFactory.createConnection();
			PreparedStatement statement = connection.prepareStatement(UPDATE_USERS);
			
			statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setDate(3, new Date(user.getDateOfBirth().getTime()));
            statement.setLong(4, user.getId());
            
            int n = statement.executeUpdate();
            
            if (n != 1) {
                throw new DatabaseException("Number of the updated rows: " + n);
            }
            
			connection.close();
			statement.close();
        } catch (DatabaseException e) {
            throw e;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

	}

	@Override
	public void delete(User user) throws DatabaseException {
		try {
            Connection connection = connectionFactory.createConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_FROM_USERS);
            statement.setLong(1, user.getId().longValue());
            int n = statement.executeUpdate();
            if (n != 1) {
                throw new DatabaseException("Number of the deleted rows: " + n);
            }
            statement.close();
            connection.close();
        } catch (DatabaseException e) {
            throw e;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
	}

	@Override
	public User find(Long id) throws DatabaseException {
		Connection connection = connectionFactory.createConnection();

        User userRes = new User();
		try {
			PreparedStatement statement = connection.prepareStatement(SELECT_ID);
			statement.setLong(1, id);
			ResultSet result = statement.executeQuery();
			if (!result.next()) {
				throw new DatabaseException("Number of the updated rows: " + id);
			}
            userRes.setId(new Long(result.getLong(1)));
            userRes.setFirstName(result.getString(2));
            userRes.setLastName(result.getString(3));
            userRes.setDateOfBirth(result.getDate(4));
            result.close();
            connection.close();
            statement.close();
		} catch (DatabaseException e) {
	        throw e;
	    } catch (SQLException e) {
	        throw new DatabaseException(e);
	    }
            return userRes;
	}

	@Override
	public Collection findAll() throws DatabaseException {
		Collection result = new LinkedList();
		
		try {
			Connection connection = connectionFactory.createConnection();
			Statement statement = connection.createStatement();	
			ResultSet resultSet = statement.executeQuery(SELECT_ALL_QUERY);
			while (resultSet.next())
			{
				User user = new User();
                user.setId(new Long(resultSet.getLong(1)));
                user.setFirstName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setDateOfBirth(resultSet.getDate(4));
                result.add(user);
			}
            statement.close();
            connection.close();
            resultSet.close();
		} catch (DatabaseException e)
		{
			throw e;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		
		return result;
	}

	@Override
	public Collection find(String firstName, String lastName) throws DatabaseException {
		Collection result = new LinkedList();

        try {
            Connection connection = connectionFactory.createConnection();
            PreparedStatement statement = connection
                    .prepareStatement(SELECT_BY_NAMES);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(new Long(resultSet.getLong(1)));
                user.setFirstName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setDateOfBirth(resultSet.getDate(4));
                result.add(user);
            }
        } catch (DatabaseException e) {
            throw e;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return result;
	}

}
