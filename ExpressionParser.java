package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExpressionParser {

    public double evaluate(String expression) {
        // Очищаем пробелы и унифицируем знаки степени
        String prepared = expression.replace(" ", "").replace("**", "^");
        List<String> tokens = tokenize(prepared);
        List<String> rpn = infixToRPN(tokens);
        return calculateRPN(rpn);
    }

    private List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        int i = 0;
        while (i < expr.length()) {
            char c = expr.charAt(i);
            
            // Считываем числа (включая дробные с точкой)
            if (Character.isDigit(c) || c == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i++));
                }
                tokens.add(sb.toString());
            } 
            // Считываем целочисленное деление //
            else if (c == '/' && i + 1 < expr.length() && expr.charAt(i + 1) == '/') {
                tokens.add("//");
                i += 2;
            } 
            // Одиночные операторы и скобки
            else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%' || c == '(' || c == ')') {
                tokens.add(String.valueOf(c));
                i++;
            } else {
                i++; // Игнорируем некорректные символы
            }
        }
        return tokens;
    }

    private List<String> infixToRPN(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                if (!stack.isEmpty()) stack.pop(); // Удаляем "("
            } else {
                while (!stack.isEmpty() && getPrecedence(stack.peek()) >= getPrecedence(token)) {
                    output.add(stack.pop());
                }
                stack.push(token);
            }
        }
        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }
        return output;
    }

    private double calculateRPN(List<String> rpn) {
        Stack<Double> stack = new Stack<>();
        for (String token : rpn) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else {
                if (stack.size() < 2) throw new IllegalArgumentException("Неверное математическое выражение");
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/": 
                        if (b == 0) throw new ArithmeticException("Деление на ноль!");
                        stack.push(a / b); 
                        break;
                    case "//": 
                        if (b == 0) throw new ArithmeticException("Деление на ноль!");
                        stack.push((double) ((long) (a / b))); 
                        break;
                    case "%": stack.push(a % b); break;
                    case "^": stack.push(Math.pow(a, b)); break;
                    default: throw new IllegalArgumentException("Неизвестный оператор: " + token);
                }
            }
        }
        if (stack.size() != 1) throw new IllegalArgumentException("Неверное математическое выражение");
        return stack.pop();
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int getPrecedence(String op) {
        switch (op) {
            case "+": case "-": return 1;
            case "*": case "/": case "//": case "%": return 2;
            case "^": return 3;
            default: return -1;
        }
    }
}