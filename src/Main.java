import java.io.File;

public class Main {
    public static void main(String[] args) {
        File input = new File("C:\\Users\\Asus\\IdeaProjects\\CircuitSimulator\\src\\input.txt");
        InputManager inputManager = new InputManager(input);
        Circuit circuit = inputManager.analyzeTheInput();
        //checks if dt,dv and di are all set and they are positive
        boolean dVITFlag = inputManager.isDVTI();
        boolean tranFlag = inputManager.isTran();
        if (!tranFlag)
            System.out.println("Time is not given");
        if (!dVITFlag)
            System.out.println("dt or dv or di is not set properly");
        if (tranFlag && dVITFlag){

            // THE REST OF THE CODE MUST BEGIN FROM HERE

        }

    }
}