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
        boolean flagDt,flagDv,flagDi;
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
                        switch (secondLetter) {
                            case 'v':
                                circuit.setDv(unitCalculator(nthWord(string, 2)));
                                flagDv = true;
                                break;
                            case 'i':
                                circuit.setDi(unitCalculator(nthWord(string, 2)));
                                flagDi = true;
                                break;
                            case 't':
                                circuit.setDt(unitCalculator(nthWord(string, 2)));
                                flagDt = true;
                                break;
                        }
                        break;
                    case 'I' :
                        addIndependentSource(circuit,string,'I');
                        break;
                    case 'V' :
                        addIndependentSource(circuit,string,'V');
                        break;
                    case 'R' :
                        addRLC(circuit,string,'R');
                        break;
                    case 'C' :
                        addRLC(circuit,string,'C');
                        break;
                    case 'L' :
                        addRLC(circuit,string,'L');
                        break;




                }
            }
        } catch (Exception e) {
            System.out.println("There is a problem found in line " + inputLines.size());
        }
    }
    //methods to add an element to the circuit
    public static void addIndependentSource(Circuit circuit, String string,int IV){
        circuit.addNode(Integer.parseInt(nthWord(string,2)));
        circuit.addNode(Integer.parseInt(nthWord(string,3)));
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = Integer.parseInt(scanner.next());
        int negative = Integer.parseInt(scanner.next());
        double offset = unitCalculator(scanner.next());
        double amplitude = unitCalculator(scanner.next());
        double frequency = unitCalculator(scanner.next());
        double phase = unitCalculator(scanner.next());
        switch (IV) {
            case 'I' :
                circuit.addElement(name, positive, negative, "independentCurrent", offset, amplitude, frequency, phase);
                break;
            case 'V' :
                circuit.addElement(name, positive, negative, "independentVoltage", offset, amplitude, frequency, phase);
                break;
        }
    }
    public static void addRLC(Circuit circuit,String string,int RLC){
        circuit.addNode(Integer.parseInt(nthWord(string,2)));
        circuit.addNode(Integer.parseInt(nthWord(string,3)));
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = Integer.parseInt(scanner.next());
        int negative = Integer.parseInt(scanner.next());
        double value = unitCalculator(scanner.next());
        switch (RLC) {
            case 'R' :
                circuit.addElement(name,positive,negative,"resistor",value);
                break;
            case 'C' :
                circuit.addElement(name,positive,negative,"capacitor",value);
                break;
            case 'L' :
                circuit.addElement(name,positive,negative,"inductance",value);
                break;
        }

    }

    //practical methods
    public static double unitCalculator(String dAmount) {
        double dReturn = 0;
        int length = dAmount.length();
        char measure = dAmount.charAt(length - 1);
        int ascii = (int) measure;
        if (ascii > 48 && ascii < 58) {
            System.out.println(dAmount);
            return Double.parseDouble(dAmount);
        } else {
            double number = Double.parseDouble(dAmount.substring(0, dAmount.length()-1));
            switch (measure){
                case 'M' :
                    return number *1000000;
                case 'G' :
                    return number *1000000000;
                case 'k' :
                    return number *1000;
                case 'm' :
                    return number *0.001;
                case 'u' :
                    return number *0.000001;
                case 'n' :
                    return number *0.000000001;
                case 'p' :
                    return number *0.000000001;
            }

        }
        return 0;
    }
    public static String nthWord(String line, int number) {
        Scanner scanner = new Scanner(line);
        String string = null;
        for (int i = 0; i < number; i++) {
            string = scanner.next();
        }
        return string;
    }
}

