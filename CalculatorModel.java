package model;

import java.util.List;

public class CalculatorModel {

    private final ExpressionParser parser =
            new ExpressionParser();

    private final HistoryManager historyManager =
            new HistoryManager();

    private final List<HistoryRecord> history =
            historyManager.loadHistory();

    public double calculate(String expression) {

        double result = parser.evaluate(expression);

        history.add(
                new HistoryRecord(expression, result));

        historyManager.saveHistory(history);

        return result;
    }

    public List<HistoryRecord> getHistory() {
        return history;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}