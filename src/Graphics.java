import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class Graphics {
    private Circuit circuit;
    boolean isSomethingLoaded = false;
    JTextArea textArea;
    File selectedFile;
    JFrame frame;

    public void start(){
        Border border = BorderFactory.createLineBorder(Color.BLACK,2,false);

        frame = new JFrame("Circuit Simulator");
        frame.setBounds(0,0,600,600);
        frame.setLayout(null);

        JPanel pText,pRun,pDraw,pLoad,pReset;
        JButton buttonRun,buttonDraw,buttonLoad,buttonReset;

        pText = new JPanel();
        pText.setBounds(0,0,300,600);
        pText.setBorder(border);
        pText.setLayout(null);
        frame.add(pText);

        pRun = new JPanel();
        pRun.setBounds(300,0,300,150);
        pRun.setBorder(border);
        pRun.setLayout(null);
        frame.add(pRun);

        pDraw = new JPanel();
        pDraw.setBounds(300,150,300,150);
        pDraw.setBorder(border);
        pDraw.setLayout(null);
        frame.add(pDraw);

        pLoad = new JPanel();
        pLoad.setBounds(300,300,300,150);
        pLoad.setBorder(border);
        pLoad.setLayout(null);
        frame.add(pLoad);

        pReset = new JPanel();
        pReset.setBounds(300,450,300,150);
        pReset.setBorder(border);
        pReset.setLayout(null);
        frame.add(pReset);

        buttonRun = new JButton("RUN");
        buttonRun.setBounds(100,50,100,50);
        pRun.add(buttonRun);

        buttonRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!isSomethingLoaded) {
                    JFileChooser fileChooser = new JFileChooser("D:\\");
                    fileChooser.showSaveDialog(null);
                    File input = fileChooser.getSelectedFile();
                    run(input);
                }else {
                    try {
                        FileWriter fileWriter = new FileWriter(selectedFile);
                        String string = textArea.getText();
                        Scanner scanner = new Scanner(string);
                        while (scanner.hasNextLine()){
                            fileWriter.write(scanner.nextLine());
                            fileWriter.write("\n");
                        }
                        fileWriter.close();
                        run(selectedFile);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(frame, "Exception Found!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        buttonLoad = new JButton("Load");
        buttonLoad.setBounds(100,50,100,50);
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
                        textArea = new JTextArea(preText);
                        textArea.setBounds(5,5,pText.getWidth() - 5,pText.getHeight() - 5);
                        isSomethingLoaded = true;
                        selectedFile = input;
                        pText.add(textArea);
                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        buttonReset = new JButton("Reset");
        buttonReset.setBounds(100,50,100,50);
        pReset.add(buttonReset);

        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                isSomethingLoaded = false;
                textArea.setVisible(false);
                textArea = null;
                selectedFile = null;
            }
        });

        buttonDraw = new JButton("Draw");
        buttonDraw.setBounds(100,50,100,50);
        pDraw.add(buttonDraw);

        frame.setVisible(true);
    }
    private void run(File input){
        if (input.canExecute()) {
            InputManager inputManager = new InputManager(input);
            Circuit circuit = inputManager.analyzeTheInput();
            Circuit.setCircuit(circuit);
            CircuitPrinter circuitPrinter = new CircuitPrinter(circuit);
            circuitPrinter.printData();
            ErrorFinder errorFinder = new ErrorFinder(circuit);
            int error = errorFinder.findErrors();
            if (error != 0) {
                JOptionPane.showMessageDialog(frame, "Error " + error + " is found!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            if (error == 0) {
                circuit.initializeGraph();
                circuit.solveCircuit();
            }
        }else
            JOptionPane.showMessageDialog(frame, "File Not Executable!", "ERROR", JOptionPane.ERROR_MESSAGE);

    }

}
