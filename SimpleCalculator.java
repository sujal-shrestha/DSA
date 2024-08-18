import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class SimpleCalculator extends JFrame {
    private JTextField inputField;
    private StringBuilder expression;
    private boolean isResultDisplayed;

    public SimpleCalculator() {
        setLookAndFeel();
        setTitle("Simple Calculator");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        expression = new StringBuilder();
        isResultDisplayed = false;

        inputField = new JTextField();
        inputField.setEditable(false);
        inputField.setFont(new Font("Arial", Font.BOLD, 24));
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        inputField.setBackground(new Color(245, 245, 245));
        inputField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(inputField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", "(", ")", "+",
            "C", "=", " ", " "
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.setBackground(getButtonColor(text));
            button.setForeground(Color.DARK_GRAY);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Color getButtonColor(String text) {
        if (text.matches("[0-9]")) {
            return new Color(225, 240, 255);  
        } else if (text.equals("C") || text.equals("=")) {
            return new Color(255, 228, 225);  
        } else if (text.equals("(") || text.equals(")")) {
            return new Color(235, 245, 235); 
        } else {
            return new Color(245, 245, 245); 
        }
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("=")) {
                try {
                    double result = evaluateExpression(expression.toString());
                    inputField.setText(String.valueOf(result));
                    expression.setLength(0);
                    expression.append(result);
                    isResultDisplayed = true;
                } catch (Exception ex) {
                    inputField.setText("Error");
                    expression.setLength(0);
                }
            } else if (command.equals("C")) {
                expression.setLength(0);
                inputField.setText("");
            } else {
                if (isResultDisplayed && !command.matches("[+\\-*/()]")) {
                    expression.setLength(0);
                }
                expression.append(command);
                inputField.setText(expression.toString());
                isResultDisplayed = false;
            }
        }
    }

    private double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operations = new Stack<>();
        int i = 0;

        while (i < expression.length()) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch)) {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                numbers.push(Double.parseDouble(sb.toString()));
                i--;
            } else if (ch == '(') {
                operations.push(ch);
            } else if (ch == ')') {
                while (operations.peek() != '(') {
                    numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                operations.pop();
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!operations.isEmpty() && hasPrecedence(ch, operations.peek())) {
                    numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                operations.push(ch);
            }
            i++;
        }

        while (!operations.isEmpty()) {
            numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }

    private double applyOperation(char operation, double b, double a) {
        switch (operation) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                return a / b;
        }
        return 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleCalculator().setVisible(true));
    }
}
