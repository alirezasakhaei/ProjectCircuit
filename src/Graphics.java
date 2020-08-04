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
                    boolean stupid = true;
                    Element element;
                    for (Map.Entry elementLoop : Circuit.getCircuit().getElements().entrySet()){
                        element = (Element) elementLoop.getValue();
                        int a = element.negativeNode.getName();
                        int b = element.positiveNode.getName();
                        if (!(a == 0 || b == 0)){
                            if ((Math.abs(b - a) != 6)){
                                if ((Math.abs(b - a) != 1)){
                                    stupid = false;
                                }
                                if (Math.min(a,b)%6 == 0)
                                    stupid = false;
                            }
                        } else if (a > 6)
                            stupid = false;
                        else if (b>6)
                            stupid = false;
                    }
                    if (stupid)
                        drawCircuitBad();
                    else
                        drawCircuitEdited();
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
                            textAreaOutput.setText(circuit.getOutput());
                            buttonDrawCircuit.setEnabled(true);
                            buttonDrawGraph.setEnabled(true);
                            buttonSave.setEnabled(true);
                            break;
                        default:
                            JOptionPane.showMessageDialog(frame, "There is a problem found in line " + error, "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
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
        dialogElement.setBounds(0, 0, 500, 500);
        dialogElement.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        Container container = dialogElement.getContentPane();
        container.setBackground(Color.gray.darker().darker().darker().darker());


        JTextField textField = new JTextField();

        JLabel labelTime = new JLabel("Draw until:");
        labelTime.setFont(new Font("Arial", Font.BOLD, 15));
        labelTime.setForeground(Color.white);

        JLabel labelChoose = new JLabel("Choose the elements!");
        labelChoose.setFont(new Font("Arial", Font.BOLD, 20));
        labelChoose.setForeground(Color.white);

        JButton buttonDraw = new JButton("Draw");
        buttonDraw.setFont(new Font("Arial", Font.BOLD, 15));
        buttonDraw.setBackground(Color.white);

        int[] chosens = new int[1];
        chosens[0] = 0;
        Element[] chosenElements = new Element[10];
        for (int j = 0; j < 10; j++)
            chosenElements[j] = null;

        JCheckBox[] checkBoxesElements = new JCheckBox[circuit.getElements().size()];
        JLabel[] labelsElements = new JLabel[circuit.getElements().size()];
        for (int j = 0; j < checkBoxesElements.length; j++) {
            checkBoxesElements[j] = new JCheckBox();
            labelsElements[j] = new JLabel(circuit.getElementNames().get(j));
            checkBoxesElements[j].setOpaque(false);
            labelsElements[j].setForeground(Color.WHITE);
        }

        JCheckBox checkBoxVoltage, checkBoxCurrent, checkBoxPower;
        checkBoxVoltage = new JCheckBox();
        checkBoxCurrent = new JCheckBox();
        checkBoxPower = new JCheckBox();

        checkBoxVoltage.setOpaque(false);
        checkBoxCurrent.setOpaque(false);
        checkBoxPower.setOpaque(false);

        JLabel labelVoltage, labelCurrent, labelPower;

        labelVoltage = new JLabel("Voltage");
        labelVoltage.setForeground(Color.white);

        labelCurrent = new JLabel("Current");
        labelCurrent.setForeground(Color.white);

        labelPower = new JLabel("Power");
        labelPower.setForeground(Color.white);

        buttonDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    chosens[0] = 0;
                    for (int j = 0; j < checkBoxesElements.length; j++) {
                        if (checkBoxesElements[j].isSelected()) {
                            chosenElements[chosens[0]] = Circuit.getCircuit().getElements().get(circuit.getElementNames().get(j));
                            chosens[0]++;
                        }
                    }
                    for (int j = chosens[0]; j < 10; j++) {
                        chosenElements[j] = null;
                    }

                    if (chosens[0] > 10) {
                        JOptionPane.showMessageDialog(dialogElement, "You can't select more than 10 elements!");
                        return;
                    }


                    if (textField.getText().equals(""))
                        Graph.setMaxTime(circuit.getMaximumTime());
                    else {
                        Graph.setMaxTime(Math.min(InputManager.unitCalculator(textField.getText()), circuit.getMaximumTime()));
                    }


                    if (chosens[0] == 0) {
                        JOptionPane.showMessageDialog(dialogElement, "No element Selected");
                        return;
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

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        dialogElement.add(labelChoose, constraints);
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;


        constraints.fill = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
        for (int i = 0; i < labelsElements.length; i++) {
            constraints.gridx = 0;
            constraints.gridy = i + 1;
            dialogElement.add(checkBoxesElements[i], constraints);
            constraints.gridx = 1;
            dialogElement.add(labelsElements[i], constraints);
        }
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 2;
        constraints.anchor = GridBagConstraints.LINE_END;

        constraints.gridy = 1;
        dialogElement.add(labelTime, constraints);
        constraints.fill = GridBagConstraints.EAST;

        constraints.gridy = 2;
        dialogElement.add(checkBoxVoltage, constraints);
        constraints.gridy = 3;
        dialogElement.add(checkBoxCurrent, constraints);
        constraints.gridy = 4;
        dialogElement.add(checkBoxPower, constraints);
        constraints.anchor = GridBagConstraints.LINE_START;

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 3;
        dialogElement.add(labelPower, constraints);
        constraints.gridy = 3;
        dialogElement.add(labelCurrent, constraints);
        constraints.gridy = 2;
        dialogElement.add(labelVoltage, constraints);
        constraints.gridy = 1;
        constraints.gridx = 3;

        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.BOTH;

        dialogElement.add(textField, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        int max = Math.max(4, labelsElements.length);
        if (max % 2 == 0)
            max /= 2;
        else max = (max - 1) / 2;
        constraints.gridx = 5;
        constraints.gridy = max;
        dialogElement.add(buttonDraw, constraints);


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

    private void drawCircuitEdited(){
        JDialog dialog = new JDialog();
        dialog.setBounds(0, 0, 1100, 600);
        dialog.setLayout(null);

        ArrayList<Node> nodes = new ArrayList<>(0);
        nodes.add(0, circuit.getNodes().get(0));
        for (Map.Entry node : circuit.getNodes().entrySet()) {
            if (((Node) node.getValue()).getName() != 0) {
                nodes.add((Node) node.getValue());
            }
        }

        CircuitGraphEdited circuitGraphEdited = new CircuitGraphEdited(circuit.getNodes().size() - 1, circuit, nodes, dialog);
        circuitGraphEdited.setLayout(null);
        circuitGraphEdited.setBounds(0, 0, 1100, 600);

        dialog.add(circuitGraphEdited);

        dialog.setResizable(false);
        dialog.setVisible(true);

    }

    private void drawCircuitBad() {
        Border border = BorderFactory.createLineBorder(Color.BLACK, 3, false);

        JDialog dialog = new JDialog();
        dialog.setBounds(0, 0, 700, 700);
        dialog.setLayout(null);

        JPanel topPanel = new JPanel();
        topPanel.setBounds(0, 0, 700, 50);
        topPanel.setLayout(new FlowLayout());
        dialog.add(topPanel);

        ArrayList<Node> nodes = new ArrayList<>(0);
        nodes.add(0, circuit.getNodes().get(0));
        for (Map.Entry node : circuit.getNodes().entrySet()) {
            if (((Node) node.getValue()).getName() != 0) {
                nodes.add((Node) node.getValue());
            }
        }

        ArrayList<JLabel> labels = new ArrayList<>(0);
        ArrayList<JLabel> namesElement = new ArrayList<>(0);
        ArrayList<JLabel> namesNode = new ArrayList<>(0);




        CircuitGraphBad circuitGraphBad = new CircuitGraphBad(circuit.getNodes().size() - 1, circuit, nodes, dialog, labels, namesElement,namesNode);
        circuitGraphBad.setLayout(null);
        circuitGraphBad.setBorder(border);
        circuitGraphBad.setSize(600,600);
        circuitGraphBad.setBounds(50, 50, 650, 650);

        dialog.add(circuitGraphBad);


        JButton buttonNodeName = new JButton("Nodes Name");
        buttonNodeName.setFont(new Font("Arial",Font.BOLD,10));
        buttonNodeName.setBackground(Color.BLACK);
        buttonNodeName.setForeground(Color.white);
        topPanel.add(buttonNodeName);
        buttonNodeName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (int i = 0; i < namesNode.size(); i++) {
                    namesNode.get(i).setVisible(!namesNode.get(i).isVisible());
                }
            }
        });

        JButton buttonElementName = new JButton("Elements\nName");
        buttonElementName.setFont(new Font("Arial",Font.BOLD,10));
        buttonElementName.setBackground(Color.BLACK);
        buttonElementName.setForeground(Color.white);
        topPanel.add(buttonElementName);
        buttonElementName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (int i = 0; i < namesElement.size(); i++) {
                    namesElement.get(i).setVisible(!namesElement.get(i).isVisible());
                }
            }
        });

        JButton buttonElementLabel = new JButton("Elements\nLabel");
        buttonElementLabel.setFont(new Font("Arial",Font.BOLD,10));
        buttonElementLabel.setBackground(Color.BLACK);
        buttonElementLabel.setForeground(Color.white);
        topPanel.add(buttonElementLabel);
        buttonElementLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (int i = 0; i < labels.size(); i++) {
                    labels.get(i).setVisible(!labels.get(i).isVisible());
                }
            }
        });


        dialog.setVisible(true);
    }

    private void drawCircuit() {
        Border border = BorderFactory.createLineBorder(Color.BLACK, 3, false);

        JDialog dialog = new JDialog();
        dialog.setBounds(0, 0, 1100, 600);
        dialog.setLayout(null);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBounds(0, 0, 1100, 50);
        dialog.add(topPanel);
        topPanel.setVisible(false);

        ArrayList<Node> nodes = new ArrayList<>(0);
        nodes.add(0, circuit.getNodes().get(0));
        for (Map.Entry node : circuit.getNodes().entrySet()) {
            if (((Node) node.getValue()).getName() != 0) {
                nodes.add((Node) node.getValue());
            }
        }

        ArrayList<JLabel> labels = new ArrayList<>(0);
        ArrayList<JLabel> namesElement = new ArrayList<>(0);
        ArrayList<JLabel> namesNode = new ArrayList<>(0);




        CircuitGraph circuitGraph = new CircuitGraph(circuit.getNodes().size() - 1, circuit, nodes, dialog,labels,namesElement,namesNode);
        circuitGraph.setLayout(null);
        circuitGraph.setBorder(border);
        circuitGraph.setBounds(0, 0, 1100, 600);

        dialog.add(circuitGraph);
        /*
        namesNode = new ArrayList<>();
        JLabel name;
        for (int i=1;i<nodes.size();i++){
            name = new JLabel(String.valueOf(nodes.get(i).getName()));
            name.setBounds(nodes.get(i).getLocation().x,nodes.get(i).getLocation().y,100,20);
            dialog.add(name);
            namesNode.add(name);
        } */

        JButton buttonNodeName = new JButton("Nodes Name");
        buttonNodeName.setFont(new Font("Arial",Font.BOLD,10));
        buttonNodeName.setBackground(Color.BLACK);
        buttonNodeName.setForeground(Color.white);
        topPanel.add(buttonNodeName);
        buttonNodeName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (int i = 0; i < namesNode.size(); i++) {
                    namesNode.get(i).setVisible(!namesNode.get(i).isVisible());
                }
            }
        });

        JButton buttonElementName = new JButton("Elements\nName");
        buttonElementName.setFont(new Font("Arial",Font.BOLD,10));
        buttonElementName.setBackground(Color.BLACK);
        buttonElementName.setForeground(Color.white);
        topPanel.add(buttonElementName);
        buttonElementName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (int i = 0; i < namesElement.size(); i++) {
                    namesElement.get(i).setVisible(!namesElement.get(i).isVisible());
                }
            }
        });

        JButton buttonElementLabel = new JButton("Elements\nLabel");
        buttonElementLabel.setFont(new Font("Arial",Font.BOLD,10));
        buttonElementLabel.setBackground(Color.BLACK);
        buttonElementLabel.setForeground(Color.white);
        topPanel.add(buttonElementLabel);
        buttonElementLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (int i = 0; i < labels.size(); i++) {
                    labels.get(i).setVisible(!labels.get(i).isVisible());
                }
            }
        });

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
