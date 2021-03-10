package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class LogFile {
    private static DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static LocalDateTime now;
    private static String filename;
    private static void CreateFile() {
        try {
            now = LocalDateTime.now();
            String formatDate = now.format(formatterDate);
            filename = formatDate + ".txt";
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("LogFile created: " + myObj.getName() + "\n" + myObj.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void writeToLog(String logMessage) {
        try {
            CreateFile();
            now = LocalDateTime.now();
            String formatTime = now.format(formatterTime);
            FileWriter myWriter = new FileWriter(filename, true);
            myWriter.write(formatTime +" "+ logMessage + System.lineSeparator());
            myWriter.close();
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }

}

