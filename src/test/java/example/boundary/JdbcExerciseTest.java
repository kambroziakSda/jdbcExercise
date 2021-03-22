package example.boundary;

import example.DatabaseConnectionProvider;
import example.DatabaseUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;

public class JdbcExerciseTest {

    @Before
    public void before() throws IOException, SQLException {
        DatabaseUtil.prepareDatabase();
    }

    /**
     * Test demonstruje wielokrotne tworzenie połączenia i zamykanie go za każdym razem za pomocą konstrukcji try-with-resources
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     * @see <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">try-with-resources</a>
     * DriverManager.getConnection korzysta z  Java SE Service Provider mechanism i dlatego nie jest potrzebne
     * ładowanie sterownika za pomocą kostrukcji Class.forName
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/sql/DriverManager.html">DriverManager</a>
     */

    @Test
    public void getConnection() throws SQLException, ClassNotFoundException {
        //Class.forName("com.mysql.jdbc.Driver");
        for (int i = 0; i < 100; i++) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?characterEncoding=utf8",
                    "root", "sda")) {

                boolean valid = connection.isValid(1000);
                System.out.println(valid);
            }
        }
    }

    /**
     * Test demonstruje pobranie danych z bazy za pomocą klas Statement oraz nawigowanie po wynikach reprezentowanych przez instancje klasy ResultSet
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testSimpleSelect() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = statement.executeQuery("select * from example_users")) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                System.out.println("User: " + name);
                int age = resultSet.getInt(4);
                System.out.println("Age: " + age);
            }

            resultSet.beforeFirst();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                System.out.println("User2: " + name);
                int age = resultSet.getInt(4);
                System.out.println("Age2: " + age);
            }

            while (resultSet.previous()) {
                String name = resultSet.getString("name");
                System.out.println("User3: " + name);
                int age = resultSet.getInt(4);
                System.out.println("Age3: " + age);
            }
        }
    }

    /**
     * Test demonstruje możliwość aktualizowania danych za pomocą klasy ResultSet.
     * Aby było to możliwe konieczne jest podanie wartosci ResultSet.CONCUR_UPDATABLE podczas tworzenia instancji klasy Statement
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testResultSetUpdate() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
             ResultSet resultSet = statement.executeQuery("select * from example_users")) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                System.out.println("User: " + name);
                int age = resultSet.getInt(4);
                System.out.println("Age: " + age);
                resultSet.updateInt(4, 50);
                resultSet.updateRow();
            }

            ResultSet afterUpdate = statement.executeQuery("select * from example_users");

            while (afterUpdate.next()) {
                int age = afterUpdate.getInt(4);
                System.out.println("Age: " + age);
            }
        }
    }

    /**
     * Test demonstruje usuwanie danych za pomocą SQL delete
     * Wykorzystywana metoda executeUpdate zwraca ilość usnietych wierszy
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testDelete() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {

            int deleted = statement.executeUpdate("delete from example_users");

            System.out.println("deleted count: " + deleted);
        }
    }

    /**
     * Test demonstruje aktualizowanie danych.
     * We wszystkich sytuacjach gdy komenda sql zmienia dane w bazie (delete, update) zamiast executeQuery wywołujemy executeUpdate
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testUpdate() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {

            int updated = statement.executeUpdate("update example_users set age=50");

            System.out.println("updated count: " + updated);
        }
    }

    /**
     * Test demonstruje przykład SQL INJECTION poprzez błędne budowanie zapytania z parametrami
     * TAK NIE ROBIMY!!!
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testGetUserByNameAndPassword() throws IOException, SQLException {
        String userName = "Jan";
        String password = "'123' OR 1=1";
        String sql = "select * from example_users WHERE name='" + userName + "' AND password=" + password;
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                System.out.println(true);
            }
        }
    }

    /**
     * Test demonstruje poprawne budowanie zapytania z parametrami poprzez wykorzystanie PreparedStatement
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testGetUserByNameAndPasswordWithPreparedStatement() throws IOException, SQLException {
        String userName = "Jan";
        String password = "123";
        String sql = "select * from example_users WHERE name=? AND password=?";
        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            Assert.assertTrue(resultSet.next());
        }
    }

    /**
     * Test demonstruje wykonanie wielkorotnego insereta za pomocą operacji executeBatch
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testInsert() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("insert into example_users (name,password,age)" +
                    "values (?,?,?)");

            preparedStatement.setString(1, "Krzysztof");
            preparedStatement.setString(2, "456");
            preparedStatement.setInt(3, 18);
            preparedStatement.addBatch();


            preparedStatement.setString(1, "Krzysztof2");
            preparedStatement.setString(2, "4562");
            preparedStatement.setInt(3, 188);
            preparedStatement.addBatch();

            int[] ints = preparedStatement.executeBatch();

            System.out.println("updated count: " + ints[0]);
        }
    }

    /**
     * Test demonstruje wykonanie dwóch operacji w jednej transakcji
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testInsertWithTransaction() throws IOException, SQLException {
        Connection connection = DatabaseConnectionProvider.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into example_users (name, password, age) values(?,?,?)");
            preparedStatement.setString(1, "U1");
            preparedStatement.setString(2, "password");
            preparedStatement.setInt(3, 25);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("INSERT into example_users (name, password, age) values(?,?,?)");
            preparedStatement.setString(1, "U2");
            preparedStatement.setString(2, "password");
            preparedStatement.setInt(3, 10);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
        }
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * from example_users");

        while (resultSet.next()) {
            System.out.println(resultSet.getString(2));
        }
        connection.close();
    }

    /**
     * Test demonstruje izolowanie transakcji.
     * Dopóki transakcja pierwsza nie zostanie skomitowana, zmiany w niej wprowadzone nie beda widoczne przez inne transakcje
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testInsertWithTransaction2() throws IOException, SQLException {
        Connection connection = DatabaseConnectionProvider.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into example_users (name, password, age) values(?,?,?)");
            preparedStatement.setString(1, "U1");
            preparedStatement.setString(2, "password");
            preparedStatement.setInt(3, 25);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
        }

        Connection connection2 = DatabaseConnectionProvider.getConnection();
        ResultSet resultSet = connection2.createStatement().executeQuery("SELECT * from example_users");

        while (resultSet.next()) {
            System.out.println(resultSet.getString(2));
        }
        connection2.close();
        connection.commit();
        connection.close();
    }
}