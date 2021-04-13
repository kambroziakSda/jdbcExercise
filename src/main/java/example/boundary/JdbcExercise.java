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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by krzysztof on 13.12.17.
 */
public class JdbcExercise {

    public static void main(String args[]) throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteka?characterEncoding=utf8&serverTimezone=UTC", "root", "sda");
        try (ReaderDAO readerDAO = new ReaderDAO(connection)) {
            Reader reader = createReader();
            boolean insertReader = insertReader(readerDAO, reader);
            System.out.println("insertReader: " + insertReader);

            List<Reader> readers = new ArrayList<>();

            Reader reader1 = createReader();
            Reader reader2 = createReader();
            reader2.setCity("Warszawa");

            readers.add(reader1);
            readers.add(reader2);

            List<Integer> integers = readerDAO.insertReaders(readers);

            System.out.println("Inserted readers: " + integers.stream().mapToInt(i -> i).sum());

            int readerId = 5;
            Optional<Reader> optReaderById = readerDAO.findReaderById(readerId);

            if (optReaderById.isPresent()) {
                System.out.println("Found reader: " + optReaderById.get());
            } else {
                System.out.println("Reader not found by id: " + readerId);
            }

            int readerIdToDelete = 5;
            Integer deleteReader = readerDAO.deleteReader(readerIdToDelete);

            if (deleteReader == 0) {
                System.out.println("not deleted - reader not found by id: " + readerIdToDelete);
            } else {
                System.out.println("reader deleted by id: " + readerIdToDelete);
            }

            int readerIdToUpdate = 6;

            Reader newReaderData = createReader();
            newReaderData.setCity("Kraków");
            newReaderData.setId(readerIdToUpdate);

            int updatedCount = readerDAO.updateReader(newReaderData);

            if (updatedCount == 0) {
                System.out.println("not updated - reader not found by id: " + readerIdToUpdate);
            } else {
                System.out.println("reader updated by id: " + readerIdToUpdate);
            }

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



