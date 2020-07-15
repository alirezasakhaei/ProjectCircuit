import java.io.File;

public class Main {
    public static void main(String[] args) {
        File input = new File("D:\\input.txt");
        InputManager inputManager = new InputManager(input);
        Circuit circuit = inputManager.analyzeTheInput();
        Graphics graphics = new Graphics(circuit);
        graphics.draw();

      /*  int error = circuit.initializeGraph();
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