import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class InputManager {
    protected File input;
    protected Circuit circuit;
    protected ElementAdder elementAdder;
    protected String string = null;
    protected boolean flagTran = false, isInputValid = true, isEveryNumberValid = true;

    InputManager(File input) {
        this.input = input;
        circuit = new Circuit();
        elementAdder = new ElementAdder(circuit);
    }

    // this method is instantly called in the main method (right after the inputManager object is created)
    public Circuit analyzeTheInput() {
        ArrayList<String> inputLines = new ArrayList<String>(0);
        try {
            Scanner inputScanner = new Scanner(input);
            while (inputScanner.hasNextLine() && isInputValid && isEveryNumberValid && !flagTran) {
                string = inputScanner.nextLine();
                string = string.trim();
                isEveryNumberValid = isEveryNumberValid();
                if (isEveryNumberValid) {
                    isInputValid = false;
                    inputLines.add(string);
                    char firstLetter = string.charAt(0);
                    switch (firstLetter) {
                        case '*':
                            isInputValid = true;
                            break;
                        case 'd':
                            isInputValid = setD(circuit, string);
                            break;

                        case 'R':
                            isInputValid = addRLC('R');
                            break;
                        case 'C':
                            isInputValid = addRLC('C');
                            break;
                        case 'L':
                            isInputValid = addRLC('L');
                            break;

                        case 'I':
                            isInputValid = addIndependentSource('I');
                            break;
                        case 'V':
                            isInputValid = addIndependentSource('V');
                            break;

                        case 'F':
                            isInputValid = addCurrentDependent('I');
                            break;
                        case 'H':
                            isInputValid = addCurrentDependent('V');
                            break;
                        case 'G':
                            isInputValid = addVoltageDependent('I');
                            break;
                        case 'E':
                            isInputValid = addVoltageDependent('V');
                            break;
                        // adding Ideal Diode
                        case 'D':
                            isInputValid = addDiode();
                            break;
                        case '.':
                            Scanner scannerTran = new Scanner(string);
                            if (scannerTran.next().equals(".tran")) {
                                circuit.setMaximumTime(unitCalculator(scannerTran.next()));
                                flagTran = true;
                                isInputValid = true;
                            }

                    }
                } else {
                    System.out.println("(invalid number)There is a problem found in line " + (inputLines.size() + 1));
                }
            }
            if (!isInputValid) {
                System.out.println("(invalid input)There is a problem found in line " + inputLines.size());
            }
        } catch (Exception e) {
            System.out.println("(Exception)There is a problem found in line " + inputLines.size());
        }

        return circuit;
    }

    public boolean setD(Circuit circuit, String string) {
        Scanner scanner = new Scanner(string);
        char vdi = string.charAt(1);
        scanner.next();
        double dAmount = unitCalculator(scanner.next());
        if (dAmount <= 0)
            return false;
        switch (vdi) {
            case 'v':
                circuit.setDv(dAmount);
                break;
            case 'i':
                circuit.setDi(dAmount);
                break;
            case 't':
                circuit.setDt(dAmount);
                break;
            default:
                return false;
        }
        return true;
    }

    //methods to add an element to the circuit
    public boolean addRLC(int RLC) {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = Integer.parseInt(scanner.next());
        int negative = Integer.parseInt(scanner.next());
        if (positive < 0 || negative < 0)
            return false;
        elementAdder.addNode(positive);
        elementAdder.addNode(negative);
        double value = unitCalculator(scanner.next());
        if (value <= 0)
            return false;
        if (positive != negative) {
            switch (RLC) {
                case 'R':
                    elementAdder.addElement(name, positive, negative, "resistor", value);
                    break;
                case 'C':
                    elementAdder.addElement(name, positive, negative, "capacitor", value);
                    break;
                case 'L':
                    elementAdder.addElement(name, positive, negative, "inductance", value);
                    break;
                default:
                    return false;
            }
        }
        return true;

    }

    public boolean addIndependentSource(int IV) {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = scanner.nextInt();
        int negative = scanner.nextInt();
        if (positive < 0 || negative < 0)
            return false;
        elementAdder.addNode(positive);
        elementAdder.addNode(negative);
        double offset = unitCalculator(scanner.next());
        double amplitude = unitCalculator(scanner.next());
        double frequency = unitCalculator(scanner.next());
        double phase = unitCalculator(scanner.next());
        switch (IV) {
            case 'I':
                elementAdder.addElement(name, positive, negative, "independentCurrent", offset, amplitude, frequency, phase);
                break;
            case 'V':
                elementAdder.addElement(name, positive, negative, "independentVoltage", offset, amplitude, frequency, phase);
                break;
        }
        return true;
    }

    public boolean addCurrentDependent(int IV) {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = scanner.nextInt();
        int negative = scanner.nextInt();
        if (positive < 0 || negative < 0)
            return false;
        elementAdder.addNode(positive);
        elementAdder.addNode(negative);
        String elementDependedOn = scanner.next();
        double gain = scanner.nextDouble();
        switch (IV) {
            case 'I':
                elementAdder.addElement(name, positive, negative, "CurrentDependentCurrent", elementDependedOn, gain);
                break;
            case 'V':
                elementAdder.addElement(name, positive, negative, "CurrentDependentVoltage", elementDependedOn, gain);
                break;
        }
        return true;
    }

    public boolean addVoltageDependent(int IV) {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = scanner.nextInt();
        int negative = scanner.nextInt();
        if (positive < 0 || negative < 0)
            return false;
        elementAdder.addNode(positive);
        elementAdder.addNode(negative);
        int positiveDependent = scanner.nextInt();
        int negativeDependent = scanner.nextInt();
        double gain = scanner.nextDouble();
        switch (IV) {
            case 'I':
                elementAdder.addElement(name, positive, negative, "voltageDependentCurrent", positiveDependent, negativeDependent, gain);
                break;
            case 'V':
                elementAdder.addElement(name, positive, negative, "voltageDependentVoltage", positiveDependent, negativeDependent, gain);
                break;
        }
        return true;
    }

    public boolean addDiode() {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = scanner.nextInt();
        int negative = scanner.nextInt();
        if (positive < 0 || negative < 0)
            return false;
        elementAdder.addNode(positive);
        elementAdder.addNode(negative);
        int isIdeal = scanner.nextInt();
        switch (isIdeal) {
            case 1:
                elementAdder.addElement(name, positive, negative, "diode");
                break;
        }
        return true;
    }

    public boolean isTran() {
        return flagTran;
    }

    //practical method

    public boolean isEveryNumberValid() {
        Scanner scanner = new Scanner(string);
        while (scanner.hasNext()) {
            if (!isNumberValid(scanner.next()))
                return false;
        }
        return true;
    }

    public static boolean isNumberValid(String number) {
        char[] numberChar = number.toCharArray();
        final char[] valids = {'G', 'M', 'k', 'm', 'u', 'n', 'p'};
        char lastChar = numberChar[number.length() - 1];
        int dotsNumber = 0;
        if (numberChar[0] < '0' || numberChar[0] > '9')
            return true;
        for (int i = 0; i < number.length() - 1; i++) {
            if (numberChar[i] == '.')
                dotsNumber++;
            else if (numberChar[i] < '0' || numberChar[i] > '9')
                return false;
        }
        if (dotsNumber > 1)
            return false;
        if (!(lastChar >= '0' && lastChar <= '9')) {
            boolean validFlag = false;
            for (int i = 0; i < valids.length; i++) {
                if (lastChar == valids[i])
                    validFlag = true;
            }
            if (!validFlag)
                return false;
        }
        return true;
    }

    public static double unitCalculator(String dAmount) {
        int length = dAmount.length();
        char measure = dAmount.charAt(length - 1);
        int ascii = (int) measure;
        if (ascii >= '0' && ascii <= '9') {
            return Double.parseDouble(dAmount);
        } else {
            double number = Double.parseDouble(dAmount.substring(0, dAmount.length() - 1));
            switch (measure) {
                case 'M':
                    return number * 1000000;
                case 'G':
                    return number * 1000000000;
                case 'k':
                    return number * 1000;
                case 'm':
                    return number * 0.001;
                case 'u':
                    return number * 0.000001;
                case 'n':
                    return number * 0.000000001;
                case 'p':
                    return number * 0.000000000001;
                default:
                    return 0;
            }

        }
    }
}


