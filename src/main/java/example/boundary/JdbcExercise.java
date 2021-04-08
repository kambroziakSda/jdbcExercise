package example.boundary;

import example.DatabaseConnectionProvider;
import example.DatabaseUtil;
import example.control.library.ReaderDAO;
import example.entity.library.Reader;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by krzysztof on 13.12.17.
 */
public class JdbcExercise {

    public static void main(String args[]) throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteka?characterEncoding=utf8&serverTimezone=UTC", "root", "sda");
        try (ReaderDAO readerDAO = new ReaderDAO(connection)){
            Reader reader = createReader();
            boolean insertReader = insertReader(readerDAO, reader);
            System.out.println("insertReader: " + insertReader);

        }

    }

    private static boolean insertReader(ReaderDAO readerDAO, Reader reader) {
        try {
            boolean insertReader = readerDAO.insertReader(reader);
            if (insertReader) {
                System.out.println("User added!");
                return true;
            } else {
                System.out.println("User not added");
            }
        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return false;
    }

    private static Reader createReader() {
        Reader reader = new Reader();
        reader.setAddress("Grunwaldzka 33333");
        reader.setCity("Gdansk");
        reader.setEmail("test@example.org");
        reader.setFirstName("Jan");
        reader.setLastName("Kowalski");
        reader.setLogin("testowy_user");
        reader.setPassword("admin123");
        reader.setPhone("123456");
        reader.setPostalCode("80-800");
        reader.setState("Pomorskie");
        return reader;
    }
}



