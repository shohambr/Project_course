package ServiceLayer;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class EventLoggerTest {
    private static final String LOG_FILE = "src/main/resources/logs/event-log.txt";

    @BeforeEach
    void clearLogFile() throws IOException {
        Files.write(Paths.get(LOG_FILE), new byte[0]); // clear the file
    }

    @Test
    void logEvent_ShouldWriteToLogFile() throws IOException {
        // Arrange
        String username = "testUser";
        String description = "User logged in";

        // Act
        EventLogger.logEvent(username, description);

        // Assert
        List<String> lines = Files.readAllLines(Paths.get(LOG_FILE));
        String logContent = String.join("\n", lines);
        assertTrue(logContent.contains(username));
        assertTrue(logContent.contains(description));
    }

}
