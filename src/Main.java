import java.io.File;

public class Main {
    public static void main(String[] args) {
       //File input = new File("C:\\Users\\Asus\\IdeaProjects\\CircuitSimulator\\src\\input.txt");
        File input = new File("C:\\input.txt");
        InputManager inputManager = new InputManager(input);
        Circuit circuit = inputManager.analyzeTheInput();
        //checks if dt,dv and di are all set and they are positive
        boolean tranFlag = inputManager.isTran();
        if (!tranFlag)
            System.out.println("Time is not given");
        if (tranFlag){


            int error=circuit.initializeGraph();
            if(error!=0) {
                System.out.println("Error " + error);
                return;
            }

            circuit.solveCircuit();
            System.out.println(circuit.toString());

            // THE REST OF THE CODE MUST BEGIN FROM HERE

        }

    }
}