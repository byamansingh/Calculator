import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CalculatorLogic {

    public enum State {
        ENTERING_FIRST,
        ENTERING_SECOND,
        RESULT_SHOWN,
        ERROR
    }

    private double firstNumber = 0;
    private double secondNumber = 0;
    private double result = 0;
    private String operator = "";
    private State state = State.ENTERING_FIRST;

    public State getState() {
        return state;
    }

    public double currentOperand() {
        return firstNumber;
    }

    public void setSecondNumber(String secondNumber) {
        this.secondNumber = Double.parseDouble(secondNumber);
    }

    public void setOperator(String op, String displayText) {
        double value = Double.parseDouble(displayText);
        if (state == State.ENTERING_SECOND) {
            secondNumber = value;
            calculate();
            if (state == State.ERROR) {
                return;
            }
            firstNumber = result;
        } else {
            firstNumber = value;
        }
        operator = op;
        state = State.ENTERING_SECOND;
    }

    public double calculate() {
        double value;
        switch (operator) {
            case "+":
                value = firstNumber + secondNumber;
                break;
            case "−":
                value = firstNumber - secondNumber;
                break;
            case "×":
                value = firstNumber * secondNumber;
                break;
            case "÷":
                value = (secondNumber == 0) ? Double.NaN : firstNumber / secondNumber;
                break;
            default:
                value = secondNumber;
        }
        result = value;
        state = Double.isNaN(value) || Double.isInfinite(value) ? State.ERROR : State.RESULT_SHOWN;
        return result;
    }

    public void clear() {
        secondNumber = 0;
        firstNumber = 0;
        operator = "";
        result = 0;
        state = State.ENTERING_FIRST;
    }

    public double percent(double value) {
        double percentValue = (state == State.ENTERING_SECOND)
                ? firstNumber * value / 100
                : value / 100;
        return setUnaryResult(percentValue);
    }

    public double reciprocal(double value) {
        return setUnaryResult(value == 0 ? Double.NaN : 1 / value);
    }

    public double square(double value) {
        return setUnaryResult(value * value);
    }

    public double squareRoot(double value) {
        return setUnaryResult(value < 0 ? Double.NaN : Math.sqrt(value));
    }

    public double negate(double value) {
        return setUnaryResult(-value);
    }

    private double setUnaryResult(double value) {
        result = value;
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            state = State.ERROR;
        } else if (state != State.ENTERING_SECOND) {
            state = State.RESULT_SHOWN;
        }
        return result;
    }

    public static String formatResult(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return "Cannot divide by zero";
        }
        BigDecimal rounded = new BigDecimal(value, new MathContext(10, RoundingMode.HALF_UP))
                .stripTrailingZeros();
        if (rounded.scale() < 0) {
            rounded = rounded.setScale(0);
        }
        return rounded.toPlainString();
    }
}
