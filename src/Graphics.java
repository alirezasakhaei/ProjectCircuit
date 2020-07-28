import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Scanner;

public class Graphics {
    private Circuit circuit;
    boolean isSomethingLoaded = false, isCircuitSolved = false;
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
        pText.setBackground(Color.white);
        frame.add(pText);

        pRun = new JPanel();
        pRun.setBounds(300,0,300,150);
        pRun.setBorder(border);
        pRun.setLayout(null);
        pRun.setBackground(Color.gray.darker().darker().darker().darker());
        frame.add(pRun);

        pDraw = new JPanel();
        pDraw.setBounds(300,150,300,150);
        pDraw.setBorder(border);
        pDraw.setLayout(null);
        pDraw.setBackground(Color.gray.darker());
        frame.add(pDraw);

        pLoad = new JPanel();
        pLoad.setBounds(300,300,300,150);
        pLoad.setBorder(border);
        pLoad.setLayout(null);
        pLoad.setBackground(Color.gray.darker());
        frame.add(pLoad);

        pReset = new JPanel();
        pReset.setBounds(300,450,300,150);
        pReset.setBorder(border);
        pReset.setLayout(null);
        pReset.setBackground(Color.gray.darker().darker().darker().darker());
        frame.add(pReset);

        buttonRun = new JButton("RUN");
        buttonRun.setBounds(100,50,100,50);
        buttonRun.setBackground(Color.white);
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
        buttonLoad.setBackground(Color.white);
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

        buttonDraw = new JButton("Draw");
        buttonDraw.setBounds(100,50,100,50);
        buttonDraw.setBackground(Color.white);
        pDraw.add(buttonDraw);

        buttonDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (isCircuitSolved) {
                    dialogChooseElement();
                }else {
                    JOptionPane.showMessageDialog(frame,"There is no circuit solved!");
                }
            }
        });

        buttonReset = new JButton("Reset");
        buttonReset.setBounds(100,50,100,50);
        buttonReset.setBackground(Color.white);
        pReset.add(buttonReset);

        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                isSomethingLoaded = false;
                isCircuitSolved = false;
                textArea.setVisible(false);
                textArea = null;
                selectedFile = null;
            }
        });

        frame.setVisible(true);
    }
    private void run(File input){
        if (input.canExecute()) {
            InputManager inputManager = new InputManager(input);
            circuit = inputManager.analyzeTheInput();
            Circuit.setCircuit(circuit);
            ErrorFinder errorFinder = new ErrorFinder(circuit);
            int error = errorFinder.findErrors();
            if (error != 0) {
                JOptionPane.showMessageDialog(frame, "Error " + error + " is found!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            if (error == 0) {
                circuit.initializeGraph();
                circuit.solveCircuit();
                isCircuitSolved = true;
            }
        }else
            JOptionPane.showMessageDialog(frame, "File Not Executable!", "ERROR", JOptionPane.ERROR_MESSAGE);

    }
    private void dialogChooseElement(){
        JDialog dialogElement = new JDialog();
        dialogElement.setLayout(null);
        dialogElement.setBounds(0,0,500,500);
        Container container = dialogElement.getContentPane();
        container.setBackground(Color.black);

        int elementsNumber = circuit.getElements().size();
        String[] elements = new String[elementsNumber];
        int i = 0;
        for (Map.Entry element : circuit.getElements().entrySet()){
            elements[i] = ((Element) element.getValue()).name;
            i++;
        }

        JComboBox comboBox = new JComboBox(elements);
        comboBox.setBounds(200,150,100,20);
        dialogElement.add(comboBox);

        JLabel labelChoose = new JLabel("Choose the element!");
        labelChoose.setFont(new Font("Arial",Font.BOLD,20));
        labelChoose.setBounds(150,50,200,20);
        labelChoose.setForeground(Color.white);
        dialogElement.add(labelChoose);

        JButton buttonChoose = new JButton("Draw");
        buttonChoose.setFont(new Font("Arial",Font.BOLD,10));
        buttonChoose.setBounds(210,250,80,80);
        buttonChoose.setBackground(Color.white);
        dialogElement.add(buttonChoose);


        buttonChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dialogElement.setVisible(false);
                draw(circuit.getElements().get((String) comboBox.getSelectedItem()));
            }
        });

        dialogElement.setVisible(true);
    }

    private void draw(Element element){
        JDialog dialogVoltage,dialogCurrent,dialogPower;
        // Voltage
        dialogVoltage = new JDialog();
        dialogVoltage.setBounds(0,0,500,500);
        dialogVoltage.setLayout(null);





        dialogVoltage.setVisible(true);




        // Current
        dialogCurrent = new JDialog();
        dialogCurrent.setBounds(0,0,500,500);
        dialogCurrent.setLayout(null);




        //dialogCurrent.setVisible(true);



        // Power
        dialogPower = new JDialog();
        dialogPower.setBounds(0,0,500,500);
        dialogPower.setLayout(null);





        //dialogPower.setVisible(true);
    }

}
