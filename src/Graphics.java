import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Graphics {
    private Circuit circuit;
    boolean isSomethingLoaded = false;
    JTextArea textAreaOnTop;

    public Graphics() {

    }

    public void run(){
        Border border = BorderFactory.createLineBorder(Color.BLACK,2,false);

        JFrame frame = new JFrame("Circuit Simulator");
        frame.setBounds(0,0,600,600);
        frame.setLayout(null);

        JPanel pText,pRun,pDraw,pLoad;
        JButton buttonRun,buttonDraw,buttonLoad;

        pText = new JPanel();
        pText.setBounds(0,300,300,300);
        pText.setBorder(border);
        pText.setLayout(null);
        frame.add(pText);

        pRun = new JPanel();
        pRun.setBounds(300,0,300,300);
        pRun.setBorder(border);
        pRun.setLayout(null);
        frame.add(pRun);

        pDraw = new JPanel();
        pDraw.setBounds(300,300,300,300);
        pDraw.setBorder(border);
        pDraw.setLayout(null);
        frame.add(pDraw);

        pLoad = new JPanel();
        pLoad.setBounds(0,0,300,300);
        pLoad.setBorder(border);
        pLoad.setLayout(null);
        frame.add(pLoad);

        buttonRun = new JButton("RUN");
        buttonRun.setBounds(100,100,100,100);
        pRun.add(buttonRun);

        buttonRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!isSomethingLoaded) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.showSaveDialog(null);
                    File input = fileChooser.getSelectedFile();
                    InputManager inputManager = new InputManager(input);
                    Circuit circuit = inputManager.analyzeTheInput();
                    Circuit.setCircuit(circuit);
                    CircuitPrinter circuitPrinter = new CircuitPrinter(circuit);
                    circuitPrinter.printData();
                    ErrorFinder errorFinder = new ErrorFinder(circuit);
                    int error = errorFinder.findErrors();
                    if (error != 0) {
                        System.out.println("Error " + error + " is found!");
                    }
                    circuit.initializeGraph();
                    circuit.solveCircuit();
                }

            }
        });


        buttonDraw = new JButton("Draw");
        buttonDraw.setBounds(100,100,100,100);
        pDraw.add(buttonDraw);

        buttonLoad = new JButton("Load");
        buttonLoad.setBounds(100,100,100,100);
        pLoad.add(buttonLoad);

        buttonLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser("D:\\");
                fileChooser.showSaveDialog(null);
                File input = fileChooser.getSelectedFile();
                String preText = "";
                if (input.canExecute()){
                    try {
                        Scanner scanner = new Scanner(input);
                        while (scanner.hasNextLine()) {
                            preText += scanner.nextLine();
                            preText += "\n";
                        }
                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    JTextArea textArea = new JTextArea(preText);
                    textArea.setBounds(5,5,pText.getWidth() - 5,pText.getHeight() - 5);
                    textAreaOnTop = textArea;
                    isSomethingLoaded = true;
                    pText.add(textArea);
                }
            }
        });

        frame.setVisible(true);
    }

}
