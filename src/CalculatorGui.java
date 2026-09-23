import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class CalculatorGui implements ActionListener {

    private static final Color BG_LIGHT = Color.decode("#F3F3F3");
    private static final Color FUNCTION_BG = Color.decode("#F9F9F9");
    private static final Color FUNCTION_FG = Color.decode("#393939");
    private static final Color NUMBER_BG = Color.decode("#FFFFFF");
    private static final Color NUMBER_FG = Color.decode("#4A4A4A");
    private static final Color EQUALS_BG = Color.decode("#0067C0");
    private static final Color EQUALS_FG = Color.decode("#FFFFFF");

    private static final java.util.Set<String> FUNCTION_KEYS = java.util.Set.of(
            "%", "CE", "C", "⌫", "1/x", "x²", "√x", "÷", "×", "−", "+"
    );

    private final CalculatorLogic logic = new CalculatorLogic();

    private final JLabel display = new JLabel();
    private final JLabel miniDisplay = new JLabel();
    private final Map<String, JButton> buttonsByLabel = new HashMap<>();

    public CalculatorGui() {

        JFrame frame = new JFrame();
        frame.setSize(334, 506);
        frame.setTitle("Calculator");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel displayPanel = new JPanel(new GridBagLayout());
        displayPanel.setBackground(BG_LIGHT);
        GridBagConstraints dc = new GridBagConstraints();
        dc.fill = GridBagConstraints.BOTH;
        dc.weightx = 1;
        dc.gridx = 0;

        miniDisplay.setText("");
        miniDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
        miniDisplay.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        miniDisplay.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        miniDisplay.setOpaque(true);
        miniDisplay.setBackground(BG_LIGHT);
        dc.gridy = 0;
        dc.weighty = 0.35;
        dc.ipady = 10;
        displayPanel.add(miniDisplay, dc);

        display.setText("0");
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font("Segoe UI", Font.PLAIN, 55));
        display.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        display.setOpaque(true);
        display.setBackground(BG_LIGHT);
        dc.gridy = 1;
        dc.weighty = 0.65;
        displayPanel.add(display, dc);

        String[] buttons = {
                "%",   "CE",  "C",   "⌫",
                "1/x", "x²",  "√x", "÷",
                "7",   "8",   "9",   "×",
                "4",   "5",   "6",   "−",
                "1",   "2",   "3",   "+",
                "+/-", "0",   ".",   "="
        };

        JPanel buttonPanel = new JPanel(new GridLayout(6, 4, 3, 4));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        buttonPanel.setBackground(BG_LIGHT);
        buttonPanel.setOpaque(true);

        for (String label : buttons) {
            JButton button = new JButton(label);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
            button.setContentAreaFilled(false);
            button.setOpaque(true);
            button.setFocusPainted(false);

            Color baseColor;
            if ("=".equals(label)) {
                baseColor = EQUALS_BG;
                button.setBackground(baseColor);
                button.setForeground(EQUALS_FG);
            } else if (FUNCTION_KEYS.contains(label)) {
                baseColor = FUNCTION_BG;
                button.setBackground(baseColor);
                button.setForeground(FUNCTION_FG);
            } else {
                baseColor = NUMBER_BG;
                button.setBackground(baseColor);
                button.setForeground(NUMBER_FG);
            }

            if (isSymbolLabel(label)) {
                button.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
            }

            attachHoverEffect(button, baseColor);
            button.addActionListener(this);
            buttonPanel.add(button);
            buttonsByLabel.put(label, button);
        }

        frame.add(displayPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        displayPanel.setPreferredSize(new Dimension(320, 120));

        attachKeyboardBindings(frame.getRootPane());

        frame.setVisible(true);
    }

    private static boolean isSymbolLabel(String label) {
        return label.equals("%") || label.equals("⌫") || label.equals("1/x")
                || label.equals("x²") || label.equals("√x") || label.equals("÷");
    }

    private static void attachHoverEffect(JButton button, Color baseColor) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }

            public void mousePressed(MouseEvent e) {
                button.setBackground(baseColor.darker().darker());
            }

            public void mouseReleased(MouseEvent e) {
                button.setBackground(baseColor.darker());
            }
        });
    }

    private void attachKeyboardBindings(JRootPane rootPane) {
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        bindKey(inputMap, actionMap, KeyEvent.VK_0, "0");
        bindKey(inputMap, actionMap, KeyEvent.VK_1, "1");
        bindKey(inputMap, actionMap, KeyEvent.VK_2, "2");
        bindKey(inputMap, actionMap, KeyEvent.VK_3, "3");
        bindKey(inputMap, actionMap, KeyEvent.VK_4, "4");
        bindKey(inputMap, actionMap, KeyEvent.VK_5, "5");
        bindKey(inputMap, actionMap, KeyEvent.VK_6, "6");
        bindKey(inputMap, actionMap, KeyEvent.VK_7, "7");
        bindKey(inputMap, actionMap, KeyEvent.VK_8, "8");
        bindKey(inputMap, actionMap, KeyEvent.VK_9, "9");
        bindKey(inputMap, actionMap, KeyEvent.VK_NUMPAD0, "0");
        bindKey(inputMap, actionMap, KeyEvent.VK_NUMPAD1, "1");
        bindKey(inputMap, actionMap, KeyEvent.VK_NUMPAD2, "2");
        bindKey(inputMap, actionMap, KeyEvent.VK_NUMPAD3, "3");
        bindKey(inputMap, actionMap, KeyEvent.VK_NUMPAD4, "4");
        bindKey(inputMap, actionMap, KeyEvent.VK_NUMPAD5, "5");
        bindKey(inputMap, actionMap, KeyEvent.VK_NUMPAD6, "6");
        bindKey(inputMap, actionMap, KeyEvent.VK_NUMPAD7, "7");
        bindKey(inputMap, actionMap, KeyEvent.VK_NUMPAD8, "8");
        bindKey(inputMap, actionMap, KeyEvent.VK_NUMPAD9, "9");
        bindKey(inputMap, actionMap, KeyEvent.VK_PERIOD, ".");
        bindKey(inputMap, actionMap, KeyEvent.VK_DECIMAL, ".");
        bindKey(inputMap, actionMap, KeyEvent.VK_PLUS, "+");
        bindKey(inputMap, actionMap, KeyEvent.VK_ADD, "+");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, java.awt.event.InputEvent.SHIFT_DOWN_MASK), "key-+-shift");
        actionMap.put("key-+-shift", new AbstractActionAdapter("+"));
        bindKey(inputMap, actionMap, KeyEvent.VK_MINUS, "−");
        bindKey(inputMap, actionMap, KeyEvent.VK_SUBTRACT, "−");
        bindKey(inputMap, actionMap, KeyEvent.VK_MULTIPLY, "×");
        bindKey(inputMap, actionMap, KeyEvent.VK_SLASH, "÷");
        bindKey(inputMap, actionMap, KeyEvent.VK_DIVIDE, "÷");
        bindKey(inputMap, actionMap, KeyEvent.VK_ENTER, "=");
        bindKey(inputMap, actionMap, KeyEvent.VK_EQUALS, "=");
        bindKey(inputMap, actionMap, KeyEvent.VK_BACK_SPACE, "⌫");
        bindKey(inputMap, actionMap, KeyEvent.VK_DELETE, "CE");
        bindKey(inputMap, actionMap, KeyEvent.VK_ESCAPE, "C");
    }

    private void bindKey(InputMap inputMap, ActionMap actionMap, int keyCode, String command) {
        Object key = "key-" + command + "-" + keyCode;
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), key);
        actionMap.put(key, new AbstractActionAdapter(command));
    }

    private class AbstractActionAdapter extends javax.swing.AbstractAction {
        private final String command;

        AbstractActionAdapter(String command) {
            this.command = command;
        }

        public void actionPerformed(ActionEvent e) {
            flashButton(command);
            CalculatorGui.this.handle(command);
        }
    }

    private void flashButton(String label) {
        JButton button = buttonsByLabel.get(label);
        if (button == null) {
            return;
        }
        Color pressed = button.getBackground().darker();
        Color original = button.getBackground();
        button.setBackground(pressed);
        Timer timer = new Timer(120, ev -> button.setBackground(original));
        timer.setRepeats(false);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        handle(e.getActionCommand());
    }

    private void handle(String clicked) {
        CalculatorLogic.State state = logic.getState();

        if (state == CalculatorLogic.State.ERROR && !clicked.equals("C") && !clicked.equals("CE")) {
            logic.clear();
            display.setText("0");
            miniDisplay.setText("");
            state = CalculatorLogic.State.ENTERING_FIRST;
        }

        if (clicked.matches("[0-9]")) {
            if (state == CalculatorLogic.State.RESULT_SHOWN) {
                miniDisplay.setText("");
                display.setText(clicked);
            } else if (display.getText().equals("0") || display.getText().isEmpty()) {
                display.setText(clicked);
            } else {
                display.setText(display.getText() + clicked);
            }
            return;
        }

        switch (clicked) {
            case "+": case "−": case "×": case "÷":
                logic.setOperator(clicked, display.getText());
                miniDisplay.setText(CalculatorLogic.formatResult(logic.currentOperand()) + clicked);
                display.setText("");
                break;

            case "C":
                miniDisplay.setText("");
                display.setText("0");
                logic.clear();
                break;

            case "CE":
                display.setText("0");
                break;

            case "%":
                applyUnary(logic::percent);
                break;

            case "1/x":
                applyUnary(logic::reciprocal);
                break;

            case "x²":
                applyUnary(logic::square);
                break;

            case "√x":
                applyUnary(logic::squareRoot);
                break;

            case "+/-":
                applyUnary(logic::negate);
                break;

            case ".":
                if (!display.getText().contains(".")) {
                    display.setText((display.getText().isEmpty() ? "0" : display.getText()) + ".");
                }
                break;

            case "⌫":
                String current = display.getText();
                if (current.length() <= 1) {
                    display.setText("0");
                } else {
                    String trimmed = current.substring(0, current.length() - 1);
                    display.setText(trimmed.equals("-") || trimmed.isEmpty() ? "0" : trimmed);
                }
                break;

            case "=":
                if (!miniDisplay.getText().isEmpty()) {
                    logic.setSecondNumber(display.getText());
                    miniDisplay.setText(miniDisplay.getText() + display.getText());
                    display.setText(CalculatorLogic.formatResult(logic.calculate()));
                }
                break;
        }
    }

    private void applyUnary(java.util.function.DoubleUnaryOperator op) {
        double value = display.getText().isEmpty() ? 0 : Double.parseDouble(display.getText());
        display.setText(CalculatorLogic.formatResult(op.applyAsDouble(value)));
    }
}
