import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputManager {
    File input;
    Circuit circuit;

    InputManager(File input) {
        this.input = input;
        circuit = new Circuit();
    }

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
                                circuit.dv = unitCalculator(nthWord(string, 2));
                                flagDv = true;
                                break;
                            case 'i':
                                circuit.di = unitCalculator(nthWord(string, 2));
                                flagDi = true;
                                break;
                            case 't':
                                circuit.dt = unitCalculator(nthWord(string, 2));
                                flagDt = true;
                                break;
                        }
                        break;
                    case 'I' :
                        addCurrentSource(circuit,string);
                        break;


                }
            }
        } catch (Exception e) {
            System.out.println("There is a problem found in line " + inputLines.size());
        }
    }
    public static void addCurrentSource(Circuit circuit, String string){
        circuit.addNode(Integer.parseInt(nthWord(string,2)));
        circuit.addNode(Integer.parseInt(nthWord(string,3)));

    }
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

