import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Graphics {
    private Circuit circuit;
    private boolean isSomethingLoaded = false, isCircuitSolved = false;
    private JTextArea textAreaInput,textAreaOutput;
    private File selectedFile;
    private JFrame frame;

    public void start(){
        Border border = BorderFactory.createLineBorder(Color.BLACK,3,false);

        frame = new JFrame("Circuit Simulator");
        frame.setBounds(0,0,600,600);
        frame.setLayout(null);


        JPanel pText,pRun,pDraw,pLoad,pReset,pOut;
        JButton buttonRun,buttonDraw,buttonLoad,buttonReset;

        pOut = new JPanel();
        pOut.setBounds(0,300,300,300);
        pOut.setBorder(border);
        pOut.setLayout(new BorderLayout());
        pOut.setBackground(Color.white);
        frame.add(pOut);

        pText = new JPanel();
        pText.setBounds(0,0,300,300);
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
                        String string = textAreaInput.getText();
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
                        textAreaInput = new JTextArea(preText);
                        textAreaInput.setBounds(5,5,pText.getWidth() - 5,pText.getHeight() - 5);
                        isSomethingLoaded = true;
                        selectedFile = input;
                        pText.add(textAreaInput);
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
                textAreaInput.setVisible(false);
                textAreaInput = null;
                selectedFile = null;
            }
        });

        textAreaOutput = new JTextArea();
        textAreaOutput.setEditable(false);
        //textAreaOutput.setAutoscrolls(true);


        JScrollPane scrollableTextArea = new JScrollPane(textAreaOutput);
        scrollableTextArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollableTextArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableTextArea.setBounds(5, 5, pOut.getWidth() - 5, pOut.getHeight() - 5);
        pOut.add(scrollableTextArea);

        pOut.add(scrollableTextArea);
        //moz

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
                drawCircuit();
                isCircuitSolved = true;
                textAreaOutput.setText(circuit.getOutput());
                try {
                    String path = input.getPath().substring(0, input.getPath().lastIndexOf("\\"));
                    File outputFile = new File(path + "\\output.txt");
                    FileWriter fileWriter = new FileWriter(outputFile);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(circuit.getOutput());
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "Can't write the output file!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
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
                drawVoltage(circuit.getElements().get((String) comboBox.getSelectedItem()));
                drawCurrent(circuit.getElements().get((String) comboBox.getSelectedItem()));
                drawPower(circuit.getElements().get((String) comboBox.getSelectedItem()));

            }
        });

        dialogElement.setVisible(true);
    }

    private void drawVoltage(Element element){
        JDialog dialogVoltage = new JDialog();
        dialogVoltage.setBounds(0,0,600,600);
        dialogVoltage.setLayout(null);

        JLabel labelTitle = new JLabel("Voltage " + element.name);
        labelTitle.setBounds(250,20,100,30);
        dialogVoltage.add(labelTitle);

        JLabel labelTime = new JLabel("Time");
        labelTime.setBounds(500,550,50,50);
        dialogVoltage.add(labelTime);

        JLabel labelMaxTime = new JLabel(Double.toString(circuit.getMaximumTime()) + "s");
        labelMaxTime.setBounds(550,300,50,50);
        dialogVoltage.add(labelMaxTime);

        JLabel labelMaxPositive = new JLabel(Double.toString(element.getVoltageMax()) + "V");
        labelMaxPositive.setBounds(10,40,50,50);
        dialogVoltage.add(labelMaxPositive);

        JLabel labelMaxNegative = new JLabel("-" + Double.toString(element.getVoltageMax()) + "V");
        labelMaxNegative.setBounds(10,520,50,50);
        dialogVoltage.add(labelMaxNegative);

        JLabel labelMaxPositiveHalf = new JLabel(Double.toString(element.getVoltageMax()/2) + "V");
        labelMaxPositiveHalf.setBounds(10,150,50,50);
        dialogVoltage.add(labelMaxPositiveHalf);

        JLabel labelMaxNegativeHalf = new JLabel("-" + Double.toString(element.getVoltageMax()/2) + "V");
        labelMaxNegativeHalf.setBounds(10,390,50,50);
        dialogVoltage.add(labelMaxNegativeHalf);



        Graph graphVoltage = new Graph(circuit.getMaximumTime(),circuit.getDt(),element.getVoltageMax(),element.getVoltagesArray());
        graphVoltage.setBounds(50,50,500,500);
        graphVoltage.setBackground(Color.gray);
        dialogVoltage.add(graphVoltage);


        dialogVoltage.setVisible(true);
    }

    private void drawCurrent(Element element){
        JDialog dialogCurrent = new JDialog();
        dialogCurrent.setBounds(0,0,600,600);
        dialogCurrent.setLayout(null);

        JLabel labelTitle = new JLabel("Currnet " + element.name);
        labelTitle.setBounds(250,20,100,30);
        dialogCurrent.add(labelTitle);

        JLabel labelTime = new JLabel("Time");
        labelTime.setBounds(500,550,50,50);
        dialogCurrent.add(labelTime);

        JLabel labelMaxTime = new JLabel(Double.toString(circuit.getMaximumTime()) + "s");
        labelMaxTime.setBounds(550,300,50,50);
        dialogCurrent.add(labelMaxTime);

        JLabel labelMaxPositive = new JLabel(Double.toString(element.getCurrentMax()) + "A");
        labelMaxPositive.setBounds(10,40,50,50);
        dialogCurrent.add(labelMaxPositive);

        JLabel labelMaxNegative = new JLabel("-" + Double.toString(element.getCurrentMax()) + "A");
        labelMaxNegative.setBounds(10,520,50,50);
        dialogCurrent.add(labelMaxNegative);

        JLabel labelMaxPositiveHalf = new JLabel(Double.toString(element.getCurrentMax()/2) + "A");
        labelMaxPositiveHalf.setBounds(10,150,50,50);
        dialogCurrent.add(labelMaxPositiveHalf);

        JLabel labelMaxNegativeHalf = new JLabel("-" + Double.toString(element.getCurrentMax()/2) + "A");
        labelMaxNegativeHalf.setBounds(10,390,50,50);
        dialogCurrent.add(labelMaxNegativeHalf);

        Graph graphCurrent = new Graph(circuit.getMaximumTime(),circuit.getDt(),element.getCurrentMax(),element.getCurrentsArray());
        graphCurrent.setBounds(50,50,500,500);
        graphCurrent.setBackground(Color.gray);
        dialogCurrent.add(graphCurrent);


        dialogCurrent.setVisible(true);
    }

    private void drawPower(Element element){
        JDialog dialogPower = new JDialog();
        dialogPower.setBounds(0,0,600,600);
        dialogPower.setLayout(null);

        JLabel labelTitle = new JLabel("Power " + element.name);
        labelTitle.setBounds(250,20,100,30);
        dialogPower.add(labelTitle);

        JLabel labelTime = new JLabel("Time");
        labelTime.setBounds(500,550,50,50);
        dialogPower.add(labelTime);

        JLabel labelMaxTime = new JLabel(Double.toString(circuit.getMaximumTime()) + "s");
        labelMaxTime.setBounds(550,300,50,50);
        dialogPower.add(labelMaxTime);

        JLabel labelMaxPositive = new JLabel(Double.toString(element.getPowerMax()) + "V");
        labelMaxPositive.setBounds(10,40,50,50);
        dialogPower.add(labelMaxPositive);

        JLabel labelMaxNegative = new JLabel("-" + Double.toString(element.getPowerMax()) + "V");
        labelMaxNegative.setBounds(10,520,50,50);
        dialogPower.add(labelMaxNegative);

        JLabel labelMaxPositiveHalf = new JLabel(Double.toString(element.getPowerMax()/2) + "V");
        labelMaxPositiveHalf.setBounds(10,150,50,50);
        dialogPower.add(labelMaxPositiveHalf);

        JLabel labelMaxNegativeHalf = new JLabel("-" + Double.toString(element.getPowerMax()/2) + "V");
        labelMaxNegativeHalf.setBounds(10,390,50,50);
        dialogPower.add(labelMaxNegativeHalf);

        Graph graphPower = new Graph(circuit.getMaximumTime(),circuit.getDt(),element.getPowerMax(),element.getPowersArray());
        graphPower.setBounds(50,50,500,500);
        graphPower.setBackground(Color.gray);
        dialogPower.add(graphPower);


        dialogPower.setVisible(true);
    }

    private void drawCircuit(){
        Border border = BorderFactory.createLineBorder(Color.BLACK,3,false);

        JDialog dialog = new JDialog();
        dialog.setBounds(0,0,600,600);
        dialog.setLayout(null);

        JLabel label = new JLabel("Simulated Circuit");
        label.setBounds(250,10,100,40);
        dialog.add(label);

        ArrayList<Node> nodes = new ArrayList<>(0);
        nodes.add(0,circuit.getNodes().get(0));
        for (Map.Entry node : circuit.getNodes().entrySet())
            nodes.add((Node) node.getValue());
        drawEarthConnecteds(dialog,nodes);



        CircuitGraph circuitGraph = new CircuitGraph(circuit.getNodes().size() - 1,circuit);
        circuitGraph.setLayout(null);
        circuitGraph.setBorder(border);
        circuitGraph.setBounds(50,50,500,500);
        dialog.add(circuitGraph);



        dialog.setVisible(true);
    }
    private void drawEarthConnecteds(Dialog dialog, ArrayList<Node> nodes){
        Node node;
        ElementShape elementShape;
        int elementNumber = 1;
        Element element;
        for (int i=1;i<nodes.size();i++){
            elementNumber = 1;
            node = nodes.get(i);
            for (int j=0; j<node.getNegatives().size(); j++){
                element = circuit.getElements().get(node.getNegatives().get(j));
                if (element.positiveNode.getName() == 0){
                    elementShape = new ElementShape(element);
                    elementShape.setBounds(50 ,425,10,50);
                    //mooooooooooooooz
                    dialog.add(elementShape);
                    elementNumber++;
                }
            }
            for (int j=0; j<node.getPositives().size(); j++){
                element = circuit.getElements().get(node.getPositives().get(j));
                if (element.negativeNode.getName() == 0){
                    elementShape = new ElementShape(element);
                    elementShape.setBounds(50 ,425,10,50);
                    dialog.add(elementShape);
                    elementNumber++;
                }
            }
        }



    }

}
