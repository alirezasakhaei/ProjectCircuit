import java.io.File;

public class Main {
    public static void main(String[] args) {
        File input = new File("D:\\input.txt");
        InputManager inputManager = new InputManager(input);
        Circuit circuit = inputManager.analyzeTheInput();
        Circuit.setCircuit(circuit);
        CircuitPrinter circuitPrinter = new CircuitPrinter(circuit);
        circuitPrinter.printData();
        ErrorFinder errorFinder = new ErrorFinder(circuit);
        int error = errorFinder.findErrors();
        if (error != 0){
            System.out.println("Error " + error + " is found!" );
        }
        circuit.initializeGraph();

/*
        circuit.initializeGraph();
        if (error != 0) {
            System.out.println("Error " + error);
            return;
        }

        circuit.solveCircuit();
        System.out.println(circuit.toString());

        // THE REST OF THE CODE MUST BEGIN FROM HERE
*/

    }
}