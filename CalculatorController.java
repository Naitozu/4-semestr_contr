package controller;

import model.CalculatorModel;
import model.HistoryRecord;
import view.ConsoleView;

import java.util.ArrayList;
import java.util.List;

public class CalculatorController {

    private final CalculatorModel model =
            new CalculatorModel();

    private final ConsoleView view =
            new ConsoleView();

    public void start() {
        boolean running = true;

        while (running) {
            view.showMenu();

            switch (view.getChoice()) {
                case 1 -> calculate();
                case 2 -> showHistory();
                case 3 -> saveAll();
                case 4 -> saveSelected();
                case 5 -> running = false;
                default -> view.print("Некорректный выбор. Пожалуйста, введите число от 1 до 5.");
            }
        }
    }

    private void calculate() {
        String expression =
                view.readLine("Введите выражение: ");

        try {
            double result =
                    model.calculate(expression);

            view.print("Результат = " + result);

        } catch (Exception e) {
            view.print("Ошибка вычисления: " + e.getMessage());
        }
    }

    private void showHistory() {
        List<HistoryRecord> history =
                model.getHistory();

        if (history.isEmpty()) {
            view.print("История пуста.");
            return;
        }

        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }
    }

    private void saveAll() {
        try {
            String path =
                    view.readLine("Имя файла или путь: ");

            model.getHistoryManager()
                    .export(model.getHistory(), path);

        } catch (Exception e) {
            view.print("Ошибка сохранения: " + e.getMessage());
        }
    }

    private void saveSelected() {
        try {
            List<HistoryRecord> history = model.getHistory();
            if (history.isEmpty()) {
                view.print("История пуста, сохранять нечего.");
                return;
            }

            showHistory();

            String input =
                    view.readLine("Введите номера через запятую: ");

            String[] indexes = input.split(",");
            List<HistoryRecord> selected = new ArrayList<>();

            for (String index : indexes) {
                int i = Integer.parseInt(index.trim()) - 1;

                if (i >= 0 && i < history.size()) {
                    selected.add(history.get(i));
                } else {
                    view.print("Запись под номером " + (i + 1) + " отсутствует в истории и будет пропущена.");
                }
            }

            if (selected.isEmpty()) {
                view.print("Не выбрано ни одной корректной записи.");
                return;
            }

            String path =
                    view.readLine("Имя файла или путь: ");

            model.getHistoryManager()
                    .export(selected, path);

        } catch (Exception e) {
            view.print("Ошибка сохранения: " + e.getMessage());
        }
    }
}