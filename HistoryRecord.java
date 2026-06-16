package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class HistoryRecord implements Serializable {

    private final String expression;
    private final double result;
    private final LocalDateTime dateTime;

    public HistoryRecord(String expression, double result) {
        this.expression = expression;
        this.result = result;
        this.dateTime = LocalDateTime.now();
    }

    public String getExpression() {
        return expression;
    }

    public double getResult() {
        return result;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return expression + " = " + result + " [" + dateTime + "]";
    }
}