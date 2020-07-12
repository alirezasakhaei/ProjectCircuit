import java.io.File;

public class Main {
    public static void main(String[] args) {
        File input = new File("C:\\Users\\Asus\\IdeaProjects\\CircuitSimulator\\src\\input.txt");
        InputManager inputManager = new InputManager(input);
        inputManager.analyzeTheInput();
    }
}