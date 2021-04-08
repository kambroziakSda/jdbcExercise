package example.boundary;

import example.DatabaseConnectionProvider;
import example.DatabaseUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.function.Function;

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
        for (int i = 0; i < 500; i++) {
            // try () {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?characterEncoding=utf8&serverTimezone=UTC",
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
                String name2 = resultSet.getString(2);
                int userId = resultSet.getInt("id");
                System.out.println("User name is: " + name);
                System.out.println("User name by index is: " + name2);
                System.out.println("User id is: " + userId);
            }

            resultSet.beforeFirst();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String name2 = resultSet.getString(2);
                int userId = resultSet.getInt("id");
                System.out.println("User name is: " + name);
                System.out.println("User name by index is: " + name2);
                System.out.println("User id is: " + userId);
            }

        }
    }

    @Test
    public void testSimpleSelectByIndexes() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = statement.executeQuery("select id, name, age, password from example_users")) {

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int age = resultSet.getInt(3);
                String password = resultSet.getString(4);
                System.out.println("User id is: " + id);
                System.out.println("User name is: " + name);
                System.out.println("User age is: " + age);
                System.out.println("User password is: " + password);
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

            resultSet.moveToInsertRow();
            resultSet.updateInt("id", 10);
            resultSet.updateString("name", "testowy");
            resultSet.updateString("password", "admin123");
            resultSet.updateInt("age", 18);
            resultSet.insertRow();
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
            System.out.println("Delete count: " + deleted);

            statement.execute("select * from example_users");
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

            int count = statement.executeUpdate("update example_users set age=50 WHERE id=1");

            System.out.println("Updated records: " + count);

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
        String password = "'123456' OR 1=1";
        String sql = "select * from example_users WHERE name='" + userName + "' AND password=" + password;

        if (password.contains(" ")) {
            System.out.println("Błedne hasło");
            return;
        }

        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                System.out.println("Zalogowano do aplikacji!");
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
        String password = "'123456' OR 1=1";
        String sql = "select * from example_users WHERE name=? AND password=?";

        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userName);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Zalogowano!");
            } else {
                System.out.println("Błędny login lub hasło!");
            }


        }

    }

    /**
     * Test demonstruje wykonanie wielkorotnego insereta za pomocą operacji executeBatch
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testInsertBatch() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("insert into example_users (name,password,age)" +
                     "values (?,?,?)")) {
            ;

            preparedStatement.setString(1, "Krzysztof");
            preparedStatement.setString(2, "admin123");
            preparedStatement.setInt(3, 18);

            preparedStatement.addBatch();

            preparedStatement.setString(1, "Adam");
            preparedStatement.setString(2, "admin1234");
            preparedStatement.setInt(3, 45);

            preparedStatement.addBatch();

            int[] ints = preparedStatement.executeBatch();
            int insertedCount = 0;
            for (int i = 0; i < ints.length; i++) {
                insertedCount += ints[i];
            }

            System.out.println("Inserted count: " + insertedCount);


        }

    }

    @Test
    public void testInsertBatch2() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("insert into example_users (name,password,age)" +
                     "values (?,?,?), (?,?,?)")) {

            preparedStatement.setString(1, "Krzysztof");
            preparedStatement.setString(2, "admin123");
            preparedStatement.setInt(3, 18);

            preparedStatement.setString(4, "Adam");
            preparedStatement.setString(5, "admin1234");
            preparedStatement.setInt(6, 45);

            preparedStatement.addBatch();

            int[] ints = preparedStatement.executeBatch();

            int insertedCount = Arrays.stream(ints).sum();

            System.out.println("Inserted count: " + insertedCount);

        }
    }

    @Test
    public void testBatchUpdate() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("update example_users set age = ? WHERE id = ? ")) {

            preparedStatement.setInt(1, 20);
            preparedStatement.setInt(2, 1);

            preparedStatement.addBatch();

            preparedStatement.setInt(1, 50);
            preparedStatement.setInt(2, 100);

            preparedStatement.addBatch();

            int[] ints = preparedStatement.executeBatch();

            System.out.println("Update count 1: " + ints[0]);
            System.out.println("Update count 2: " + ints[1]);

            int updatedCount = Arrays.stream(ints).sum();

            System.out.println("Updated count: " + updatedCount);
        }

    }

    @Test
    public void testInsert() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("insert into example_users (name,password,age)" +
                     "values (?,?,?)")) {

            preparedStatement.setString(1, "Andrzej");
            preparedStatement.setString(2, "111");
            preparedStatement.setInt(3, 30);

            int insertedCount = preparedStatement.executeUpdate();

            System.out.println("Inserted count: " + insertedCount);

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
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("insert into example_users (name,password,age)" +
                     "values (?,?,?)")) {

            connection.setAutoCommit(false);

            preparedStatement.setString(1, "User");
            preparedStatement.setString(2, "test");
            preparedStatement.setInt(3, 30);

            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "User2");
            preparedStatement.setString(2, "testowy2");
            preparedStatement.setInt(3, 20);

            preparedStatement.executeUpdate();

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from example_users");

            while (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }

            connection.commit();

        }
        //try {
        //  connection.setAutoCommit(false);

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
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("insert into example_users (name,password,age)" +
                     "values (?,?,?)")) {

            connection.setAutoCommit(false);

            preparedStatement.setString(1, "User");
            preparedStatement.setString(2, "test");
            preparedStatement.setInt(3, 30);

            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "User2");
            preparedStatement.setString(2, "testowy2");
            preparedStatement.setInt(3, 20);

            preparedStatement.executeUpdate();

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from example_users");

            while (resultSet.next()) {
                System.out.println("In transaction: " + resultSet.getString("name"));
            }

            try (Connection newConnection = DatabaseConnectionProvider.getConnection();
                 PreparedStatement statementFromNewTransaction = newConnection.prepareStatement("select * from example_users WHERE name in (?,?)")
            ) {
                statementFromNewTransaction.setString(1, "User");
                statementFromNewTransaction.setString(2, "User2");

                ResultSet resultSetFromNewTransaction = statementFromNewTransaction.executeQuery();

                if (resultSetFromNewTransaction.next()) {
                    System.out.println("User found");
                } else {
                    System.out.println("No user found");
                }


            }
            connection.commit();
        }

    }

    @Test
    public void testCreateTable() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement()) {

            String createTableSql = "CREATE TABLE IF NOT EXISTS `example_student` (\n" +
                    "  `id`           INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                    "  `name`         VARCHAR(20)      NOT NULL DEFAULT '',\n" +
                    "  `city`         VARCHAR(20)               DEFAULT '',\n" +
                    "  `password`     VARCHAR(20)      NOT NULL DEFAULT '',\n" +
                    "  `birthday`     DATE,\n" +
                    "  `averagegrade` DOUBLE,\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ")\n" +
                    "  ENGINE = InnoDB\n" +
                    "  AUTO_INCREMENT = 1\n" +
                    "  DEFAULT CHARSET = utf8;";

            statement.execute(createTableSql);

        }
    }


}