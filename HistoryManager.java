package model;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {

    private static final String HISTORY_FILE = "history.dat";

    @SuppressWarnings("unchecked")
    public List<HistoryRecord> loadHistory() {
        File file = new File(HISTORY_FILE);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {

            return (List<HistoryRecord>) in.readObject();

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void saveHistory(List<HistoryRecord> history) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(HISTORY_FILE))) {

            out.writeObject(history);

        } catch (IOException e) {
            System.out.println("Ошибка сохранения истории");
        }
    }

    public String getHistoryFilePath() {
        return new File(HISTORY_FILE).getAbsolutePath();
    }

    public void export(List<HistoryRecord> records, String userInput) throws IOException {
        Path path;

        // 1. Если пользователь не указывает имя и путь - берем дефолтный файл истории
        if (userInput == null || userInput.isBlank()) {
            path = Paths.get(HISTORY_FILE);
        } 
        // 2. Если указано имя файла и расширение (txt, log, md)
        else if (userInput.endsWith(".txt") || userInput.endsWith(".log") || userInput.endsWith(".md")) {
            path = Paths.get(userInput);
        } 
        // 3. Другие случаи
        else {
            File file = new File(userInput);
            // Если указан лишь путь (существующая папка) или заканчивается на слэш - сохраняем как log.log
            if (file.isDirectory() || userInput.endsWith("/") || userInput.endsWith("\\")) {
                path = Paths.get(userInput, "log.log");
            } else {
                path = Paths.get(userInput);
            }
        }

        // Автоматически создаем папки по пути, если их нет
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (HistoryRecord r : records) {
                writer.write(r.toString());
                writer.newLine();
            }
        }

        System.out.println("Файл успешно сохранен по пути: " + path.toAbsolutePath());
    }
}