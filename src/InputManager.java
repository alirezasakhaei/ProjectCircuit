import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class InputManager {
    File input;
    Circuit circuit;

    InputManager(File input) {
        this.input = input;
        circuit = new Circuit();
    }
    // this method is instantly called in the main method (right after the inputManager object is created

    public void analyzeTheInput() {
        ArrayList<String> inputLines = new ArrayList<String>(0);
        boolean flagDt, flagDv, flagDi;
        try {
            Scanner inputScanner = new Scanner(input);
            while (inputScanner.hasNextLine()) {
                String string = inputScanner.nextLine();
                string = string.trim();
                inputLines.add(string);
                char firstLetter = string.charAt(0);
                char secondLetter = string.charAt(1);
                switch (firstLetter) {
                    case 'd':
                        Scanner scanner = new Scanner(string);
                        scanner.next();
                        String dAmount = scanner.next();
                        switch (secondLetter) {
                            case 'v':
                                circuit.setDv(unitCalculator(dAmount));
                                flagDv = true;
                                break;
                            case 'i':
                                circuit.setDi(unitCalculator(dAmount));
                                flagDi = true;
                                break;
                            case 't':
                                circuit.setDt(unitCalculator(dAmount));
                                flagDt = true;
                                break;
                        }
                        break;
                    case 'I':
                        addIndependentSource(circuit, string, 'I');
                        break;
                    case 'V':
                        addIndependentSource(circuit, string, 'V');
                        break;
                    case 'R':
                        addRLC(circuit, string, 'R');
                        break;
                    case 'C':
                        addRLC(circuit, string, 'C');
                        break;
                    case 'L':
                        addRLC(circuit, string, 'L');
                        break;
                    case 'F':
                        addCurrentDependent(circuit, string, 'I');
                        break;
                    case 'H':
                        addCurrentDependent(circuit, string, 'V');
                        break;
                    case 'G':
                        addVoltageDependent(circuit, string, 'I');
                        break;
                    case 'E':
                        addVoltageDependent(circuit, string, 'V');
                        break;
                        // adding Ideal Diode
                    case 'D' :



                }
            }
        } catch (Exception e) {
            System.out.println("There is a problem found in line " + inputLines.size());
        }
    }

    //methods to add an element to the circuit
    public static void addIndependentSource(Circuit circuit, String string, int IV) {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = scanner.nextInt();
        int negative = scanner.nextInt();
        circuit.addNode(positive);
        circuit.addNode(negative);
        double offset = unitCalculator(scanner.next());
        double amplitude = unitCalculator(scanner.next());
        double frequency = unitCalculator(scanner.next());
        double phase = unitCalculator(scanner.next());
        switch (IV) {
            case 'I':
                circuit.addElement(name, positive, negative, "independentCurrent", offset, amplitude, frequency, phase);
                break;
            case 'V':
                circuit.addElement(name, positive, negative, "independentVoltage", offset, amplitude, frequency, phase);
                break;
        }
    }

    public static void addRLC(Circuit circuit, String string, int RLC) {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = Integer.parseInt(scanner.next());
        int negative = Integer.parseInt(scanner.next());
        circuit.addNode(positive);
        circuit.addNode(negative);
        double value = unitCalculator(scanner.next());
        switch (RLC) {
            case 'R':
                circuit.addElement(name, positive, negative, "resistor", value);
                break;
            case 'C':
                circuit.addElement(name, positive, negative, "capacitor", value);
                break;
            case 'L':
                circuit.addElement(name, positive, negative, "inductance", value);
                break;
        }

    }

    public static void addCurrentDependent(Circuit circuit, String string, int IV) {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = scanner.nextInt();
        int negative = scanner.nextInt();
        circuit.addNode(positive);
        circuit.addNode(negative);
        String elementDependedOn = scanner.next();
        double gain = scanner.nextDouble();
        switch (IV) {
            case 'I':
                circuit.addElement(name, positive, negative, "CurrentDependentCurrent", elementDependedOn, gain);
                break;
            case 'V':
                circuit.addElement(name, positive, negative, "CurrentDependentVoltage", elementDependedOn, gain);
                break;
        }
    }

    public static void addVoltageDependent(Circuit circuit, String string, int IV) {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = scanner.nextInt();
        int negative = scanner.nextInt();
        circuit.addNode(positive);
        circuit.addNode(negative);
        int positiveDependent = scanner.nextInt();
        int negativeDependent = scanner.nextInt();
        double gain = scanner.nextDouble();
        switch (IV) {
            case 'I':
                circuit.addElement(name, positive, negative, "voltageDependentCurrent", positiveDependent, negativeDependent, gain);
                break;
            case 'V':
                circuit.addElement(name, positive, negative, "voltageDependentVoltage", positiveDependent, negativeDependent, gain);
                break;
        }
    }

    public static void addDiode(Circuit circuit, String string){
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = scanner.nextInt();
        int negative = scanner.nextInt();
        circuit.addNode(positive);
        circuit.addNode(negative);
        int isIdeal = scanner.nextInt();
        switch (isIdeal){
            case 1 :
                circuit.addElement(name,positive,negative,"diode");
                break;
        }
    }

    //practical methods
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
            }

        }
        return 0;
    }
}


