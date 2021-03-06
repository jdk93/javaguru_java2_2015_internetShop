package lv.javaguru.java2.database.jdbc;

        import lv.javaguru.java2.database.DBException;
        import lv.javaguru.java2.database.NewItemDAO;
        import lv.javaguru.java2.domain.Category;
        import lv.javaguru.java2.domain.NewItem;
        import org.springframework.stereotype.Component;

        import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by Anna on 27.02.15.
 */

@Component("JDBC_NewItemDAO")
public class NewItemDAOImpl extends DAOImpl implements NewItemDAO {


    @Override
    public void create(NewItem newItem) throws DBException {

        if (newItem == null){
            return;
        }

        Connection connection = null;

        try {
            connection = getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into News values (default, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, newItem.getDateID());
            preparedStatement.setString(2, newItem.getTitle());
            preparedStatement.setString(3, newItem.getBody());
            preparedStatement.setInt(4, newItem.getLikes());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()){
                newItem.setNum(rs.getLong(1));
            }
        } catch (Throwable e) {
            System.out.println("Exception");
            e.printStackTrace();
            throw new DBException(e);
        } finally {
            closeConnection(connection);
        }

    }


    @Override
    public List<NewItem> getAll() throws DBException {
        List<NewItem> news = new ArrayList<NewItem>();
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from news");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                NewItem newItem = new NewItem();
                newItem.setNum(resultSet.getLong("Num"));
                newItem.setDateID(resultSet.getString("DateId"));
                newItem.setTitle(resultSet.getString("Title"));
                newItem.setBody(resultSet.getString("Body"));
                newItem.setLikes(resultSet.getInt("Likes"));
                news.add(newItem);
            }
        } catch (Throwable e) {
            System.out.println("Exception");
            e.printStackTrace();
            throw new DBException(e);
        } finally {
            closeConnection(connection);
        }
        return news;
    }

    @Override
    public List<NewItem> getAll(String param) throws DBException {
        return null;
    }

    @Override
    public void delete(long id) throws DBException {
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from news where Num = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (Throwable e) {
            System.out.println("Exception");
            e.printStackTrace();
            throw new DBException(e);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public NewItem getById(long id) throws DBException {
        Connection connection = null;

        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from News where Num = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            NewItem newItem = null;
            if (resultSet.next()) {
                newItem = new NewItem();
                newItem.setDateID(resultSet.getString("DateId"));
                newItem.setTitle(resultSet.getString("Title"));
                newItem.setBody(resultSet.getString("Body"));
                newItem.setLikes(resultSet.getInt("Likes"));
                newItem.setNum(resultSet.getLong("Num"));
            }
            return newItem;
        } catch (Throwable e) {
            System.out.println("Exception while execute UserDAOImpl.getById()");
            e.printStackTrace();
            throw new DBException(e);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void update(NewItem newItem) throws DBException {
        if (newItem == null) {
            return;
        }

        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update news set Likes = ? where Num = ?");
            preparedStatement.setInt(1, (newItem.getLikes() + 1));
            preparedStatement.setLong(2, newItem.getNum());
            preparedStatement.executeUpdate();
        } catch (Throwable e) {
            System.out.println("Exception while execute UserDAOImpl.update()");
            e.printStackTrace();
            throw new DBException(e);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public List<NewItem> getNewsFromCat(Category category, String param) throws DBException {
        return null;
    }

    @Override
    public List<NewItem> getNewsFromCat(Category category) throws DBException {
        return null;
    }


}