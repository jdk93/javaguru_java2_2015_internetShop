package lv.javaguru.java2.database.jdbc;

import lv.javaguru.java2.database.ClientDAO;
import lv.javaguru.java2.database.DBException;
import lv.javaguru.java2.domain.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientDAOImpl extends DAOImpl implements ClientDAO {

    @Override
    public void create(Client client) throws DBException {
        if (client == null) {
            return;
        }

        Connection connection = null;

        try {
            connection = getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into CLIENTS values (default, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getSurname());
            preparedStatement.setString(3, client.getPersCode());
            preparedStatement.setString(4, client.getGender());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()){
                client.setId(rs.getLong(1));
            }
        } catch (Throwable e) {
            System.out.println("Exception while execute ClientDAOImpl.create()");
            e.printStackTrace();
            throw new DBException(e);
        } finally {
            closeConnection(connection);
        }

    }

    @Override
    public Client getById(Long id) throws DBException {
        Connection connection = null;

        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from CLIENTS where ClientId = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Client client = null;
            if (resultSet.next()) {
                client = new Client();
                client.setId(resultSet.getLong("ClientId"));
                client.setName(resultSet.getString("Name"));
                client.setSurname(resultSet.getString("Surname"));
                client.setPersCode(resultSet.getString("Personal_code"));
                client.setGender(resultSet.getString("Gender"));
            }
            return client;
        } catch (Throwable e) {
            System.out.println("Exception while execute UserDAOImpl.getById()");
            e.printStackTrace();
            throw new DBException(e);
        } finally {
            closeConnection(connection);
        }
    }

    public List<Client> getAll() throws DBException {
        List<Client> clients = new ArrayList<Client>();
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from CLIENTS");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getLong("ClientId"));
                client.setName(resultSet.getString("Name"));
                client.setSurname(resultSet.getString("Surname"));
                client.setPersCode(resultSet.getString("Personal_code"));
                client.setGender(resultSet.getString("Gender"));
                clients.add(client);
            }
        } catch (Throwable e) {
            System.out.println("Exception while getting customer list UserDAOImpl.getList()");
            e.printStackTrace();
            throw new DBException(e);
        } finally {
            closeConnection(connection);
        }
        return clients;
    }

    @Override
    public void delete(Long id) throws DBException {
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from CLIENTS where ClientId = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (Throwable e) {
            System.out.println("Exception while execute UserDAOImpl.delete()");
            e.printStackTrace();
            throw new DBException(e);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void update(Client client) throws DBException {
        if (client == null) {
            return;
        }

        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update CLIENTS set Surname = ?" +
                            "where ClientId = ?");
            preparedStatement.setString(1, client.getSurname());
            preparedStatement.setLong(2, client.getId());
            preparedStatement.executeUpdate();
        } catch (Throwable e) {
            System.out.println("Exception while execute ClientDAOImpl.update()");
            e.printStackTrace();
            throw new DBException(e);
        } finally {
            closeConnection(connection);
        }
    }

}