import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Graphics {
    private Circuit circuit;
    private boolean isSomethingLoaded = false, isCircuitSolved = false;
    private JTextArea textAreaInput, textAreaOutput;
    private File selectedFile;
    private JFrame frame;

    public void start() {
        Border border = BorderFactory.createLineBorder(Color.BLACK, 3, false);

        frame = new JFrame("Circuit Simulator");
        frame.setBounds(0, 0, 600, 600);
        GroupLayout frameLayout = new GroupLayout(frame.getContentPane());
        frame.setLayout(frameLayout);

        JPanel pText, pRun, pDrawGraph, pLoad, pReset, pOut, pSave, pDrawCircuit;
        JButton buttonSolve, buttonDrawGraph, buttonLoad, buttonReset, buttonSave, buttonDrawCircuit;

        pOut = new JPanel();
        pOut.setBorder(border);
        pOut.setLayout(new BorderLayout());
        pOut.setBackground(Color.white);

        pText = new JPanel();
        pText.setBorder(border);
        pText.setLayout(new BorderLayout());
        pText.setBackground(Color.YELLOW);

        pRun = new JPanel();
        pRun.setBorder(border);
        pRun.setLayout(new BorderLayout());
        pRun.setBackground(Color.gray.darker().darker().darker().darker());

        pDrawGraph = new JPanel();
        pDrawGraph.setBorder(border);
        pDrawGraph.setLayout(new BorderLayout());
        pDrawGraph.setBackground(Color.gray.darker());

        pLoad = new JPanel();
        pLoad.setBorder(border);
        pLoad.setLayout(new BorderLayout());
        pLoad.setBackground(Color.gray.darker());

        pReset = new JPanel();
        pReset.setBorder(border);
        pReset.setLayout(new BorderLayout());
        pReset.setBackground(Color.gray.darker().darker().darker().darker());

        pDrawCircuit = new JPanel();
        pDrawCircuit.setBorder(border);
        pDrawCircuit.setLayout(new BorderLayout());
        pDrawCircuit.setBackground(Color.gray.darker().darker().darker().darker());

        pSave = new JPanel();
        pSave.setBorder(border);
        pSave.setLayout(new BorderLayout());
        pSave.setBackground(Color.gray.darker().darker().darker().darker());

        Color red = new Color(230, 90, 90);


        buttonSolve = new JButton("Solve");
        buttonSolve.setBackground(Color.gray.darker().darker().darker().darker());
        buttonSolve.setForeground(red);
        pRun.add(buttonSolve);


        buttonDrawCircuit = new JButton("Draw Circuit");
        buttonDrawCircuit.setBackground(Color.gray.darker().darker().darker().darker());
        buttonDrawCircuit.setForeground(red);
        buttonDrawCircuit.setEnabled(false);

        pDrawCircuit.add(buttonDrawCircuit);

        buttonDrawCircuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (isCircuitSolved) {
                    drawCircuit();
                    drawCircuitPro();
                } else JOptionPane.showMessageDialog(frame, "There is no circuit solved!");

            }
        });


        buttonSave = new JButton("Save");
        buttonSave.setBackground(Color.gray.darker().darker().darker().darker());
        buttonSave.setEnabled(false);

        buttonSave.setForeground(red);

        pSave.add(buttonSave);


        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (isCircuitSolved) {
                    try {
                        String path = "D:\\output.txt";
                        File output;
                        output = new File(path);
                        JFileChooser fileChooser = new JFileChooser("D:\\");
                        fileChooser.setFileFilter(new FileNameExtensionFilter("txt file", "txt"));
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        fileChooser.setSelectedFile(output);
                        fileChooser.showSaveDialog(null);
                        output = fileChooser.getSelectedFile();
                        if (Objects.isNull(output)) {
                            output = new File(path);
                        }
                        if (!output.getName().trim().endsWith(".txt"))
                            output = new File(output.getAbsolutePath() + ".txt");
                        FileWriter fileWriter = new FileWriter(output);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(circuit.getOutput());
                        bufferedWriter.close();
                        fileWriter.close();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "Can't write the output file!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } else JOptionPane.showMessageDialog(frame, "There is no circuit solved!");

            }
        });

        buttonDrawGraph = new JButton("Draw Graph");
        buttonDrawGraph.setBackground(Color.gray.darker().darker().darker().darker());
        buttonDrawGraph.setForeground(red);
        buttonDrawGraph.setEnabled(false);

        pDrawGraph.add(buttonDrawGraph);

        buttonDrawGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (isCircuitSolved) {
                    //dialog();
                    dialogChooseElement();
                } else {
                    JOptionPane.showMessageDialog(frame, "There is no circuit solved!");
                }
            }
        });


        buttonSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int error = -10;
                if (!isSomethingLoaded) {
                    JFileChooser fileChooser = new JFileChooser("D:\\");
                    fileChooser.showOpenDialog(null);
                    File input = fileChooser.getSelectedFile();
                    if (Objects.isNull(input))
                        return;
                    error = run(input);
                } else {
                    try {
                        FileWriter fileWriter = new FileWriter(selectedFile);
                        String string = textAreaInput.getText();
                        Scanner scanner = new Scanner(string);
                        while (scanner.hasNextLine()) {
                            fileWriter.write(scanner.nextLine());
                            fileWriter.write("\n");
                        }
                        fileWriter.close();
                        error = run(selectedFile);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(frame, "Exception Found!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (error != -10) {
                    switch (error) {
                        case -1:
                        case -2:
                        case -3:
                        case -4:
                        case -5:
                            JOptionPane.showMessageDialog(frame, "Error " + error + " is found!", "ERROR", JOptionPane.ERROR_MESSAGE);
                            break;
                        case -6:
                            JOptionPane.showMessageDialog(frame, "File Not Executable!", "ERROR", JOptionPane.ERROR_MESSAGE);
                            break;
                        case 0:
                            buttonDrawCircuit.setEnabled(true);
                            buttonDrawGraph.setEnabled(true);
                            buttonSave.setEnabled(true);
                            break;
                        default:
                            JOptionPane.showMessageDialog(frame, "There is a problem found in line " + error, "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    textAreaOutput.setText(circuit.getOutput());

                }
            }
        });

        buttonLoad = new JButton("Load");
        buttonLoad.setBackground(Color.gray.darker().darker().darker().darker());
        buttonLoad.setForeground(red);

        pLoad.add(buttonLoad);

        textAreaInput = new JTextArea();
        textAreaInput.setEditable(false);
        textAreaInput.setBackground(Color.WHITE);
        textAreaInput.setForeground(Color.BLACK);

        JScrollPane scrollableTextAreaInput = new JScrollPane(textAreaInput);
        scrollableTextAreaInput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollableTextAreaInput.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        pText.add(scrollableTextAreaInput);


        buttonLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser("D:\\");
                fileChooser.showOpenDialog(null);
                File input = fileChooser.getSelectedFile();
                String preText = "";
                if (Objects.nonNull(input))
                    if (input.canExecute()) {
                        try {
                            Scanner scanner = new Scanner(input);
                            while (scanner.hasNextLine()) {
                                preText += scanner.nextLine();
                                preText += "\n";
                            }
                            textAreaInput.setText(preText);
                            textAreaInput.setEditable(true);

                            //textAreaInput.setBounds(5, 5, pText.getWidth() - 5, pText.getHeight() - 5);


                            isSomethingLoaded = true;
                            selectedFile = input;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
            }
        });


        buttonReset = new JButton("Reset");
        buttonReset.setBackground(Color.white);
        buttonReset.setBackground(Color.gray.darker().darker().darker().darker());
        buttonReset.setForeground(red);
        pReset.add(buttonReset);

        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                    isSomethingLoaded = false;
                    isCircuitSolved = false;
                    textAreaInput.setVisible(false);
                    textAreaInput = null;
                    selectedFile = null;
                textAreaOutput.setText("");
                buttonSave.setEnabled(false);
                    buttonDrawCircuit.setEnabled(false);
                    buttonDrawGraph.setEnabled(false);

            }
        });

        textAreaOutput = new JTextArea();
        textAreaOutput.setEditable(false);
        //textAreaOutput.setAutoscrolls(true);


        JScrollPane scrollableTextArea = new JScrollPane(textAreaOutput);
        scrollableTextArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollableTextArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        pOut.add(scrollableTextArea);


        frameLayout.setHorizontalGroup(frameLayout.createSequentialGroup().addGroup(frameLayout.createParallelGroup().addComponent(pText).addComponent(pOut))
                .addGroup(frameLayout.createParallelGroup().addComponent(pRun).addComponent(pDrawCircuit).addComponent(pDrawGraph).addComponent(pLoad).addComponent(pSave).addComponent(pReset)));
        frameLayout.setVerticalGroup(frameLayout.createParallelGroup().addGroup(frameLayout.createSequentialGroup().addComponent(pText).addComponent(pOut))
                .addGroup(frameLayout.createSequentialGroup().addComponent(pRun).addComponent(pDrawCircuit).addComponent(pDrawGraph).addComponent(pLoad).addComponent(pSave).addComponent(pReset)));
        frame.setVisible(true);

    }

    private int run(File input) {
        if (input.canExecute()) {
            InputManager inputManager = new InputManager(input);
            circuit = inputManager.analyzeTheInput();
            if (inputManager.getErrorLine() != -1) {
                return inputManager.getErrorLine();
            }
            Circuit.setCircuit(circuit);
            ErrorFinder errorFinder = new ErrorFinder(circuit);
            int error = errorFinder.findErrors();
            if (error != 0) {
                return error;
            } else {
                circuit.initializeGraph();
                error = circuit.solveCircuit();
                if (error == 0) {
                    isCircuitSolved = true;
                    return 0;
                } else return error;
            }
        } else {
            return -6;
        }
    }

    private void dialogChooseElement() {
        JDialog dialogElement = new JDialog();
        dialogElement.setLayout(null);
        dialogElement.setBounds(0, 0, 500, 500);
        Container container = dialogElement.getContentPane();
        container.setBackground(Color.gray.darker().darker().darker().darker());

        int elementsNumber = circuit.getElements().size();
        String[] elements = new String[elementsNumber];
        int i = 0;
        for (Map.Entry element : circuit.getElements().entrySet()) {
            elements[i] = ((Element) element.getValue()).name;
            i++;
        }

        JComboBox comboBox = new JComboBox(elements);
        comboBox.setBounds(200, 120, 100, 20);
        dialogElement.add(comboBox);

        JTextField textField = new JTextField();
        textField.setBounds(240, 150, 100, 30);
        dialogElement.add(textField);

        JLabel labelTime = new JLabel("Draw until:");
        labelTime.setFont(new Font("Arial", Font.BOLD, 15));
        labelTime.setBounds(140, 150, 100, 20);
        labelTime.setForeground(Color.white);
        dialogElement.add(labelTime);

        JLabel labelChoose = new JLabel("Choose the element!");
        labelChoose.setFont(new Font("Arial", Font.BOLD, 20));
        labelChoose.setBounds(150, 30, 200, 20);
        labelChoose.setForeground(Color.white);
        dialogElement.add(labelChoose);

        JButton buttonChoose = new JButton("Select");
        buttonChoose.setFont(new Font("Arial", Font.BOLD, 15));
        buttonChoose.setBounds(100, 200, 80, 80);
        buttonChoose.setBackground(Color.white);
        dialogElement.add(buttonChoose);

        JButton buttonReset = new JButton("Reset");
        buttonReset.setFont(new Font("Arial", Font.BOLD, 15));
        buttonReset.setBounds(200, 200, 80, 80);
        buttonReset.setBackground(Color.white);
        dialogElement.add(buttonReset);

        JButton buttonDraw = new JButton("Draw");
        buttonDraw.setFont(new Font("Arial", Font.BOLD, 15));
        buttonDraw.setBounds(300, 200, 80, 80);
        buttonDraw.setBackground(Color.white);
        dialogElement.add(buttonDraw);


        int[] chosens = new int[1];
        chosens[0] = 0;
        Element[] chosenElements = new Element[10];
        for (int j = 0; j < 10; j++)
            chosenElements[j] = null;

        JCheckBox checkBoxVoltage, checkBoxCurrent, checkBoxPower;
        checkBoxVoltage = new JCheckBox();
        checkBoxVoltage.setBounds(200, 300, 25, 25);
        dialogElement.add(checkBoxVoltage);

        checkBoxCurrent = new JCheckBox();
        checkBoxCurrent.setBounds(200, 350, 25, 25);
        dialogElement.add(checkBoxCurrent);

        checkBoxPower = new JCheckBox();
        checkBoxPower.setBounds(200, 400, 25, 25);
        dialogElement.add(checkBoxPower);

        checkBoxVoltage.setOpaque(false);
        checkBoxCurrent.setOpaque(false);
        checkBoxPower.setOpaque(false);

        JLabel labelVoltage, labelCurrent, labelPower;

        labelVoltage = new JLabel("Voltage");
        labelVoltage.setForeground(Color.white);
        labelVoltage.setBounds(250, 290, 100, 50);
        dialogElement.add(labelVoltage);

        labelCurrent = new JLabel("Current");
        labelCurrent.setForeground(Color.white);
        labelCurrent.setBounds(250, 340, 100, 50);
        dialogElement.add(labelCurrent);

        labelPower = new JLabel("Power");
        labelPower.setForeground(Color.white);
        labelPower.setBounds(250, 390, 100, 50);
        dialogElement.add(labelPower);

        buttonChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                boolean isSelected = false;
                if (chosens[0] == 10)
                    JOptionPane.showMessageDialog(dialogElement, "You can't select more than 10 elements!");
                else {
                    for (int j = 0; j < chosens[0]; j++) {
                        if (chosenElements[j].name.equals(comboBox.getSelectedItem()))
                            isSelected = true;
                    }
                    if (isSelected) {
                        JOptionPane.showMessageDialog(dialogElement, "This element is already selected!");
                    } else {
                        JOptionPane.showMessageDialog(dialogElement, "Element " + comboBox.getSelectedItem() + " is selected!");
                        chosenElements[chosens[0]] = Circuit.getCircuit().getElements().get(comboBox.getSelectedItem());
                        chosens[0]++;
                    }
                }
            }
        });

        buttonDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (textField.getText().equals(""))
                        Graph.setMaxTime(circuit.getMaximumTime());
                    else {
                        Graph.setMaxTime(Math.min(InputManager.unitCalculator(textField.getText()), circuit.getMaximumTime()));
                    }

                    if (chosens[0] == 0) {
                        JOptionPane.showMessageDialog(dialogElement, "No element Selected");
                    }
                    if (checkBoxVoltage.isSelected())
                        drawGraph(chosenElements, chosens[0], 'V');
                    if (checkBoxCurrent.isSelected())
                        drawGraph(chosenElements, chosens[0], 'A');
                    if (checkBoxPower.isSelected())
                        drawGraph(chosenElements, chosens[0], 'W');


                } catch (Exception e) {
                    JOptionPane.showMessageDialog(dialogElement, "Wrong input for time");
                }
            }
        });

        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chosens[0] = 0;
                for (int j = 0; j < 10; j++)
                    chosenElements[j] = null;
            }
        });

        dialogElement.setVisible(true);
    }

    private void drawGraph(Element[] chosenElements, int elementsNumber, char graphType) {
        JDialog dialogVoltage = new JDialog();
        dialogVoltage.setBounds(0, 0, 600, 600);
        dialogVoltage.setLayout(null);

        final Color[] colors = {Color.red, Color.green, Color.yellow, Color.pink, Color.ORANGE, Color.magenta, Color.cyan, Color.darkGray, Color.lightGray, Color.gray};
        double maxAmount = 0;


        JLabel title = new JLabel();
        switch (graphType) {
            case 'V':
                title.setText("Voltage");
                for (int i = 0; i < elementsNumber; i++)
                    if (chosenElements[i].getVoltageMax() > maxAmount)
                        maxAmount = chosenElements[i].getVoltageMax();
                break;
            case 'A':
                title.setText("Current");
                for (int i = 0; i < elementsNumber; i++)
                    if (chosenElements[i].getCurrentMax() > maxAmount)
                        maxAmount = chosenElements[i].getCurrentMax();
                break;
            case 'W':
                title.setText("Power");
                for (int i = 0; i < elementsNumber; i++)
                    if (chosenElements[i].getPowerMax() > maxAmount)
                        maxAmount = chosenElements[i].getPowerMax();
                break;
        }
        title.setBounds(270, 10, 60, 20);
        dialogVoltage.add(title);

        JLabel[] titles = new JLabel[elementsNumber];
        for (int i = 0; i < elementsNumber; i++) {
            titles[i] = new JLabel(chosenElements[i].name);
            titles[i].setBounds(50 * (i + 1), 30, 50, 30);
            titles[i].setForeground(colors[i]);
            dialogVoltage.add(titles[i]);
        }

        JLabel labelTime = new JLabel("Time");
        labelTime.setBounds(500, 550, 50, 50);
        dialogVoltage.add(labelTime);


        JLabel labelMaxTime = new JLabel(Graph.getMaxTime() + "s");
        labelMaxTime.setBounds(550, 300, 50, 50);
        dialogVoltage.add(labelMaxTime);

        JLabel labelMaxPositive = new JLabel(Double.toString(maxAmount) + graphType);
        labelMaxPositive.setBounds(10, 40, 50, 50);
        dialogVoltage.add(labelMaxPositive);

        JLabel labelMaxNegative = new JLabel("-" + maxAmount + graphType);
        labelMaxNegative.setBounds(10, 520, 50, 50);
        dialogVoltage.add(labelMaxNegative);

        JLabel labelMaxPositiveHalf = new JLabel(Double.toString(maxAmount / 2) + graphType);
        labelMaxPositiveHalf.setBounds(10, 150, 50, 50);
        dialogVoltage.add(labelMaxPositiveHalf);

        JLabel labelMaxNegativeHalf = new JLabel("-" + maxAmount / 2 + graphType);
        labelMaxNegativeHalf.setBounds(10, 390, 50, 50);
        dialogVoltage.add(labelMaxNegativeHalf);


        Graph graphVoltage = new Graph(circuit.getDt(), maxAmount, chosenElements, elementsNumber, graphType);
        graphVoltage.setBounds(50, 50, 500, 500);
        graphVoltage.setBackground(Color.gray);
        dialogVoltage.add(graphVoltage);


        dialogVoltage.setVisible(true);
    }

    private void drawCircuit() {
        Border border = BorderFactory.createLineBorder(Color.BLACK, 3, false);

        JDialog dialog = new JDialog();
        dialog.setBounds(0, 0, 700, 700);
        dialog.setLayout(null);

        JLabel label = new JLabel("Simulated Circuit");
        label.setBounds(300, 10, 100, 40);
        dialog.add(label);

        ArrayList<Node> nodes = new ArrayList<>(0);
        nodes.add(0, circuit.getNodes().get(0));
        for (Map.Entry node : circuit.getNodes().entrySet()) {
            if (((Node) node.getValue()).getName() != 0) {
                nodes.add((Node) node.getValue());
            }
        }


        CircuitGraph circuitGraph = new CircuitGraph(circuit.getNodes().size() - 1, circuit, nodes, dialog);
        circuitGraph.setLayout(null);
        circuitGraph.setBorder(border);
        circuitGraph.setBounds(50, 50, 600, 600);

        dialog.add(circuitGraph);


        dialog.setVisible(true);
    }

    private void drawCircuitPro() {
        ArrayList<Node> nodes = new ArrayList<>(0);
        nodes.add(0, circuit.getNodes().get(0));
        for (Map.Entry node : circuit.getNodes().entrySet()) {
            if (((Node) node.getValue()).getName() != 0) {
                nodes.add((Node) node.getValue());
            }
        }

        JDialog dialog = new JDialog();
        if (nodes.size() <= 10)
            dialog.setBounds(0, 0, nodes.size() * 100 + 50, 650);
        else
            dialog.setBounds(0, 0, 1200, 650);

        CircuitGraphPro circuitGraphPro = new CircuitGraphPro(circuit.getNodes().size() - 1, circuit, nodes, dialog);
        circuitGraphPro.setLayout(null);
        circuitGraphPro.setBounds(0, 50, dialog.getWidth(), 600);
        dialog.add(circuitGraphPro);


        dialog.setVisible(true);

    }
}
