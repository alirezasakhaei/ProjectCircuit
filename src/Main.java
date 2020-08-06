import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Graphics graphics = new Graphics();
        graphics.start();
    }
}

class Graphics {
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


        textAreaOutput = new JTextArea();
        textAreaOutput.setEditable(false);


        JScrollPane scrollableTextArea = new JScrollPane(textAreaOutput);
        scrollableTextArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollableTextArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        pOut.add(scrollableTextArea);


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
                    for (Map.Entry<String, Element> elementLoop : Circuit.getCircuit().getElements().entrySet()) {
                        element = elementLoop.getValue();
                        int a = element.negativeNode.getName();
                        int b = element.positiveNode.getName();
                        if (!(a == 0 || b == 0)) {
                            if ((Math.abs(b - a) != 6)) {
                                if ((Math.abs(b - a) != 1)) {
                                    stupid = false;
                                }
                                if (Math.min(a, b) % 6 == 0)
                                    stupid = false;
                            }
                        } else if (a > 6)
                            stupid = false;
                        else if (b > 6)
                            stupid = false;
                    }
                    int maxEarth = 0;
                    Node node;
                    for (Map.Entry<Integer, Node> nodesLoop : Circuit.getCircuit().getNodes().entrySet()) {
                        node = nodesLoop.getValue();
                        node.setEarthConnections();
                        if (node.getEarthConnections() > maxEarth)
                            maxEarth = node.getEarthConnections();
                    }
                    if (maxEarth > 3)
                        stupid = false;
                    if (Circuit.getCircuit().getNodes().size() > 11)
                        stupid = true;

                    if (stupid)
                        drawCircuitBad();
                    else
                        drawCircuitEdited();

                } else JOptionPane.showMessageDialog(frame, "There is no circuit solved!");
            }
        });

        buttonSave = new JButton("Save Output");
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
        dialogVoltage.setBounds(0, 0, 1200, 600);
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
        DecimalFormat decimalFormat = new DecimalFormat("0.###E0");
        String maxString = decimalFormat.format(maxAmount);
        String halfString = decimalFormat.format(maxAmount / 2);

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
        labelTime.setBounds(1100, 500, 50, 50);
        dialogVoltage.add(labelTime);


        JLabel labelMaxTime = new JLabel(editString(decimalFormat.format(Graph.getMaxTime())) + "s");
        labelMaxTime.setBounds(1150, 300, 50, 50);
        dialogVoltage.add(labelMaxTime);

        JLabel labelMaxPositive = new JLabel(editString(maxString) + graphType);
        labelMaxPositive.setBounds(2, 40, 100, 50);
        dialogVoltage.add(labelMaxPositive);

        JLabel labelMaxNegative = new JLabel("-" + editString(maxString) + graphType);
        labelMaxNegative.setBounds(2, 520, 100, 50);
        dialogVoltage.add(labelMaxNegative);

        JLabel labelMaxPositiveHalf = new JLabel(editString(halfString) + graphType);
        labelMaxPositiveHalf.setBounds(2, 150, 100, 50);
        dialogVoltage.add(labelMaxPositiveHalf);

        JLabel labelMaxNegativeHalf = new JLabel("-" + editString(halfString) + graphType);
        labelMaxNegativeHalf.setBounds(2, 390, 100, 50);
        dialogVoltage.add(labelMaxNegativeHalf);


        Graph graphVoltage = new Graph(circuit.getDt(), maxAmount, chosenElements, elementsNumber, graphType);
        graphVoltage.setBounds(50, 50, 1100, 500);
        graphVoltage.setBackground(Color.gray);
        dialogVoltage.add(graphVoltage);


        dialogVoltage.setVisible(true);
    }

    private void drawCircuitEdited() {
        JDialog dialog = new JDialog();
        dialog.setBounds(0, 0, 1100, 600);
        dialog.setLayout(null);

        JPanel topPanel = new JPanel();
        topPanel.setBounds(0, 0, 1100, 50);
        topPanel.setLayout(new FlowLayout());
        dialog.add(topPanel);

        ArrayList<Node> nodes = new ArrayList<>(0);
        nodes.add(0, circuit.getNodes().get(0));
        for (Map.Entry<Integer, Node> node : circuit.getNodes().entrySet()) {
            if ((node.getValue()).getName() != 0) {
                nodes.add(node.getValue());
            }
        }

        ArrayList<JLabel> labels = new ArrayList<>(0);
        ArrayList<JLabel> namesElement = new ArrayList<>(0);
        ArrayList<JLabel> namesNode = new ArrayList<>(0);


        CircuitGraphEdited circuitGraphEdited = new CircuitGraphEdited(circuit.getNodes().size() - 1, circuit, nodes, dialog, labels, namesElement, namesNode);
        circuitGraphEdited.setLayout(null);
        circuitGraphEdited.setBounds(0, 0, 1100, 600);
        dialog.add(circuitGraphEdited);
        dialog.setResizable(false);

        JButton buttonNodeName = new JButton("Nodes Name");
        buttonNodeName.setFont(new Font("Arial", Font.BOLD, 10));
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
        buttonElementName.setFont(new Font("Arial", Font.BOLD, 10));
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
        buttonElementLabel.setFont(new Font("Arial", Font.BOLD, 10));
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

        JButton buttonSwitch = new JButton("Draw Rectangular");
        buttonSwitch.setFont(new Font("Arial", Font.BOLD, 10));
        buttonSwitch.setBackground(Color.BLACK);
        buttonSwitch.setForeground(Color.white);
        topPanel.add(buttonSwitch);
        buttonSwitch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                drawCircuitBad();
                dialog.setVisible(false);
            }
        });


        buttonElementLabel.setVisible(false);
        buttonElementName.setVisible(false);
        buttonNodeName.setVisible(false);

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


        CircuitGraphBad circuitGraphBad = new CircuitGraphBad(circuit.getNodes().size() - 1, circuit, nodes, dialog, labels, namesElement, namesNode);
        circuitGraphBad.setLayout(null);
        circuitGraphBad.setBorder(border);
        circuitGraphBad.setSize(600, 600);
        circuitGraphBad.setBounds(50, 50, 650, 650);

        dialog.add(circuitGraphBad);


        JButton buttonNodeName = new JButton("Nodes Name");
        buttonNodeName.setFont(new Font("Arial", Font.BOLD, 10));
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
        buttonElementName.setFont(new Font("Arial", Font.BOLD, 10));
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
        buttonElementLabel.setFont(new Font("Arial", Font.BOLD, 10));
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

        JButton buttonSwitch = new JButton("Draw linear");
        buttonSwitch.setFont(new Font("Arial", Font.BOLD, 10));
        buttonSwitch.setBackground(Color.BLACK);
        buttonSwitch.setForeground(Color.white);
        topPanel.add(buttonSwitch);
        buttonSwitch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                drawCircuitEdited();
                dialog.setVisible(false);
            }
        });


        dialog.setVisible(true);
    }

    private String editString(String string) {
        if (string.contains("E0")) {
            string = string.replaceAll("E0", "");
        }
        return string;
    }

}

class Circuit {
    private static Circuit circuit;
    private final HashMap<Integer, Node> nodes;
    private final HashMap<String, Element> elements;
    private final ArrayList<Integer> nodeNameQueue;
    private final ArrayList<String> elementNames;
    private final ArrayList<ArrayList<Node>> unions;
    private double dt = 0, dv = 0, di = 0, time, maximumTime;

    public static void setCircuit(Circuit circuit) {
        Circuit.circuit = circuit;
    }

    public static Circuit getCircuit() {
        return circuit;
    }

    public Circuit() {
        nodes = new HashMap<>();
        elements = new HashMap<>();
        nodeNameQueue = new ArrayList<>();
        elementNames = new ArrayList<>();
        unions = new ArrayList<>();
    }

    /////////////////// Get-set codeBox

    public HashMap<Integer, Node> getNodes() {
        return nodes;
    }

    public HashMap<String, Element> getElements() {
        return elements;
    }

    public void setMaximumTime(double maximumTime) {
        this.maximumTime = maximumTime;
    }

    public double getMaximumTime() {
        return maximumTime;
    }

    public ArrayList<Integer> getNodeNameQueue() {
        return nodeNameQueue;
    }

    public void setDt(double dt) {
        this.dt = dt;
    }

    public void setDv(double dv) {
        this.dv = dv;
    }

    public void setDi(double di) {
        this.di = di;
    }

    protected double getDt() {
        return dt;
    }

    protected double getDv() {
        return dv;
    }

    protected double getDi() {
        return di;
    }

    public double getTime() {
        return time;
    }

    public ArrayList<String> getElementNames() {
        return elementNames;
    }

    /////////////////// End of get-set codeBox

    void initializeGraph() {
        initializeUnions();
    }

    protected void setAddedNodes() {
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(0);
        nodes.get(0).setAdded(true);
        nodeNameQueue.add(0);
        while (queue.size() > 0) {
            Node node = nodes.get(queue.poll());
            for (int j = 0; j < node.getPositives().size(); j++) {
                if (elements.get(node.getPositives().get(j)).isVoltageSource()) {
                    elements.get(node.getPositives().get(j)).negativeNode.setUnion(node.getUnion());
                }
            }
            for (int j = 0; j < node.getNegatives().size(); j++) {
                if (elements.get(node.getNegatives().get(j)).isVoltageSource()) {
                    elements.get(node.getNegatives().get(j)).positiveNode.setUnion(node.getUnion());
                }
            }

            for (int i = 0; i < node.getNeighbors().size(); i++) {
                if (!nodes.get(node.getNeighbors().get(i)).isAdded()) {
                    queue.add(node.getNeighbors().get(i));
                    nodes.get(node.getNeighbors().get(i)).setAdded(true);
                    nodeNameQueue.add(node.getNeighbors().get(i));
                }
            }
        }

    }

    boolean checkForVoltageLoop() {
        ArrayList<Element> voltageSources = new ArrayList<>();
        for (String elementName : elementNames) {
            if (elements.get(elementName).isVoltageSource())
                voltageSources.add(elements.get(elementName));
        }
        int i, previousSize;
        do {
            previousSize = voltageSources.size();
            i = 0;

            while (i < voltageSources.size() - 1) {
                for (int j = i + 1; j < voltageSources.size(); j++) {
                    if ((!(voltageSources.get(i).positiveNode == voltageSources.get(j).positiveNode || voltageSources.get(i).positiveNode == voltageSources.get(j).negativeNode))
                            || (!(voltageSources.get(i).negativeNode == voltageSources.get(j).positiveNode || voltageSources.get(i).negativeNode == voltageSources.get(j).negativeNode))) {

                        voltageSources.remove(voltageSources.get(i));
                    }
                }
                i++;
            }

        } while (i < previousSize - 1);
        if (voltageSources.size() < 2)
            return true;
        double voltage = 0;
        Node currentNode = voltageSources.get(0).positiveNode;
        i = 0;
        previousSize = voltageSources.size();
        while (voltageSources.size() > 0) {

            if (previousSize == voltageSources.size()) {
                i++;
                if (i == voltageSources.size()) {
                    if (Math.abs(voltage) >= dv)
                        return false;
                    else {
                        i = 0;
                        currentNode = voltageSources.get(0).positiveNode;
                    }
                }
            } else {
                i = 0;
                previousSize = voltageSources.size();
            }
            if (currentNode.equals(voltageSources.get(i).positiveNode)) {
                voltage += voltageSources.get(i).getVoltage();
                currentNode = voltageSources.get(i).negativeNode;
                voltageSources.remove(i);
            } else if (currentNode.equals(voltageSources.get(i).negativeNode)) {
                voltage -= voltageSources.get(i).getVoltage();
                currentNode = voltageSources.get(i).positiveNode;
                voltageSources.remove(i);
            }
        }
        return Math.abs(voltage) < dv;

    }

    boolean checkForCurrentNode() {
        boolean allElementsAreCurrentSource;
        double current = 0;
        for (Integer integer : nodeNameQueue) {
            allElementsAreCurrentSource = true;
            for (int j = 0; j < nodes.get(integer).getPositives().size(); j++) {
                if (elements.get(nodes.get(integer).getPositives().get(j)).isCurrentSource()) {
                    current -= elements.get(nodes.get(integer).getPositives().get(j)).getCurrent();
                } else allElementsAreCurrentSource = false;

            }
            for (int j = 0; j < nodes.get(integer).getPositives().size(); j++) {
                if (elements.get(nodes.get(integer).getPositives().get(j)).isCurrentSource()) {
                    current += elements.get(nodes.get(integer).getPositives().get(j)).getCurrent();
                } else allElementsAreCurrentSource = false;
            }
            if (allElementsAreCurrentSource && Math.abs(current) >= di)
                return false;
        }

        return true;
    }

    private void setVoltagesInUnion(int i) {
        for (int j = 1; j < unions.get(i).size(); j++) {
            for (int k = 0; k < unions.get(i).get(j).getPositives().size(); k++) {
                int q = unions.get(i).indexOf(elements.get(unions.get(i).get(j).getPositives().get(k)).negativeNode);
                if (elements.get(unions.get(i).get(j).getPositives().get(k)).isVoltageSource() && q < j) {
                    unions.get(i).get(j).setVoltage(unions.get(i).get(q).getVoltage() + elements.get(unions.get(i).get(j).getPositives().get(k)).getVoltage());
                }
            }

            for (int k = 0; k < unions.get(i).get(j).getNegatives().size(); k++) {
                int q = unions.get(i).indexOf(elements.get(unions.get(i).get(j).getNegatives().get(k)).positiveNode);
                if (elements.get(unions.get(i).get(j).getNegatives().get(k)).isVoltageSource() && q < j) {
                    unions.get(i).get(j).setVoltage(unions.get(i).get(q).getVoltage() - elements.get(unions.get(i).get(j).getNegatives().get(k)).getVoltage());
                }
            }
        }
    }

    int solveCircuit() {
        double i1, i2;
        int isKclSatisfied;
        Node currentNode;
        ArrayList<Node> currentUnion;
        ErrorFinder errorFinder = new ErrorFinder(circuit);

        for (int i = 0; i < unions.size(); i++) {
            setVoltagesInUnion(i);
        }
        for (time = 0; time <= maximumTime; time += dt) {
            isKclSatisfied = 0;
            if (time / maximumTime > 0.0005) {
                if (!errorFinder.isCurrentSourceSeries()) {
                    return -2;
                }
                if (!errorFinder.isVoltageSourcesParallel())
                    return -3;
                if (!checkForVoltageLoop()) {
                    return -3;
                }
                if (!checkForCurrentNode())
                    return -2;
            }
            for (int j = 0; j < 5000 && isKclSatisfied < unions.size(); j++) {
                isKclSatisfied = 0;
                for (int i = 0; i < unions.size(); i++) {
                    currentUnion = unions.get(i);
                    currentNode = currentUnion.get(0);

                    i1 = obtainCurrent(currentUnion);
                    currentNode.setVoltage(currentNode.getVoltage());
                    setVoltagesInUnion(i);
                    currentNode.setVoltage(currentNode.getVoltage() + dv);
                    setVoltagesInUnion(i);
                    i2 = obtainCurrent(currentUnion);
                    currentNode.setVoltage(currentNode.getVoltage() - dv + dv * (i1 - i2) / di);
                    setVoltagesInUnion(i);
                    if (obtainCurrent(currentUnion) < Math.sqrt(di))
                        isKclSatisfied++;
                }
            }
            helpConvergence();

            for (String elementName : elementNames) {
                elements.get(elementName).updateTime();
            }
            for (Integer integer : nodeNameQueue) {
                nodes.get(integer).updatePreviousVoltage();
            }
        }
        return 0;
    }

    private void helpConvergence() {
        for (Integer integer : nodeNameQueue) {
            if (Math.abs(nodes.get(integer).getVoltage() - nodes.get(integer).getPrePreviousVoltage()) < dv)
                nodes.get(integer).setVoltage((nodes.get(integer).getVoltage() + nodes.get(integer).getPreviousVoltage()) / 2);
        }
    }

    private double obtainCurrent(ArrayList<Node> union) {
        double current = 0;
        for (Node node : union) {
            for (int i = 0; i < node.getPositives().size(); i++) {
                if (elements.get(node.getPositives().get(i)).negativeNode.getUnion() != node.getUnion()) {
                    current -= elements.get(node.getPositives().get(i)).getCurrent();
                }
            }
            for (int i = 0; i < node.getNegatives().size(); i++) {
                if (elements.get(node.getNegatives().get(i)).positiveNode.getUnion() != node.getUnion()) {
                    current += elements.get(node.getNegatives().get(i)).getCurrent();
                }
            }
        }
        return Math.abs(current);
    }

    private void initializeUnions() {
        ArrayList<Integer> seenUnions = new ArrayList<>();
        unions.clear();
        for (Integer integer : nodeNameQueue) {
            if (!seenUnions.contains(nodes.get(integer).getUnion())) {
                ArrayList<Node> temp = new ArrayList<>();
                temp.add(nodes.get(integer));
                unions.add(temp);
                seenUnions.add(nodes.get(integer).getUnion());
            } else {
                unions.get(seenUnions.indexOf(nodes.get(integer).getUnion())).add(nodes.get(integer));
            }
        }
    }

    protected void checkLoopValidation(ArrayList<String> elementsUsed, ArrayList<Integer> nodesPassed, int currentNode) {
        if (nodesPassed.size() > 1 && currentNode == 0) {
            for (Integer integer : nodesPassed) {
                nodes.get(integer).setAdded(true);
            }
            return;
        }
        for (int i = 0; i < nodes.get(currentNode).getPositives().size(); i++) {
            if (!elementsUsed.contains(nodes.get(currentNode).getPositives().get(i))) {
                nodesPassed.add(elements.get(nodes.get(currentNode).getPositives().get(i)).negativeNode.getName());
                elementsUsed.add(nodes.get(currentNode).getPositives().get(i));
                checkLoopValidation(new ArrayList<>(elementsUsed), new ArrayList<>(nodesPassed), elements.get(nodes.get(currentNode).getPositives().get(i)).negativeNode.getName());
                nodesPassed.remove(nodesPassed.size() - 1);
                elementsUsed.remove(elementsUsed.size() - 1);
            }
        }
        for (int i = 0; i < nodes.get(currentNode).getNegatives().size(); i++) {
            if (!elementsUsed.contains(nodes.get(currentNode).getNegatives().get(i))) {
                nodesPassed.add(elements.get(nodes.get(currentNode).getNegatives().get(i)).positiveNode.getName());
                elementsUsed.add(nodes.get(currentNode).getNegatives().get(i));
                checkLoopValidation(new ArrayList<>(elementsUsed), new ArrayList<>(nodesPassed), elements.get(nodes.get(currentNode).getNegatives().get(i)).positiveNode.getName());
                nodesPassed.remove(nodesPassed.size() - 1);
                elementsUsed.remove(elementsUsed.size() - 1);
            }
        }
    }

    String getOutput() {
        StringBuilder output = new StringBuilder();
        try {
            int[] nodeNames = new int[nodeNameQueue.size()];
            for (int i = 0; i < nodeNames.length; i++) {
                nodeNames[i] = nodeNameQueue.get(i);
            }
            Arrays.sort(nodeNames);
            DecimalFormat decimalFormat = new DecimalFormat("0.###E0");
            for (int i = 1; i < nodeNames.length; i++) {
                output.append(nodeNames[i]);
                for (int j = 0; j < nodes.get(nodeNames[i]).getVoltagesArray().size(); j++) {
                    output.append(" ");
                    output.append(decimalFormat.format(nodes.get(nodeNames[i]).getVoltagesArray().get(j)).replace("E0", ""));
                }
                output.append("\n");
            }
            output.append("\n");
            for (String elementName : elementNames) {
                output.append(elementName);
                for (int j = 0; j < elements.get(elementName).getVoltagesArray().size(); j++) {
                    output.append(" ");
                    output.append(decimalFormat.format(elements.get(elementName).getVoltagesArray().get(j)).replace("E0", ""));
                    output.append(" ");
                    output.append(decimalFormat.format(elements.get(elementName).getCurrentsArray().get(j)).replace("E0", ""));
                    output.append(" ");
                    output.append(decimalFormat.format(elements.get(elementName).getVoltagesArray().get(j) * elements.get(elementName).getCurrentsArray().get(j)).replace("E0", ""));
                }
                output.append("\n");
            }
            return output.toString();
        } catch (OutOfMemoryError e) {
            return "Out of Memory!\ntry increasing dt or reducing maximum time.";
        }
    }
}

class InputManager {
    protected final File input;
    protected final Circuit circuit;
    protected final ElementAdder elementAdder;
    protected String string = null;
    protected boolean flagTran = false, isInputValid = true, isEveryNumberValid = true;
    private int errorLine;

    InputManager(File input) {
        this.input = input;
        circuit = new Circuit();
        elementAdder = new ElementAdder(circuit);
        errorLine = -1;
    }

    public int getErrorLine() {
        return errorLine;
    }

    // this method is instantly called in the main method (right after the inputManager object is created)
    public Circuit analyzeTheInput() {
        ArrayList<String> inputLines = new ArrayList<>(0);
        try {
            Scanner inputScanner = new Scanner(input);
            while (inputScanner.hasNextLine() && isInputValid && isEveryNumberValid && !flagTran) {
                string = inputScanner.nextLine();
                string = string.trim();
                isEveryNumberValid = isEveryNumberValid();
                if (isEveryNumberValid) {
                    isInputValid = false;
                    inputLines.add(string);
                    char firstLetter = string.charAt(0);
                    switch (firstLetter) {
                        case '*':
                            isInputValid = true;
                            break;
                        case 'd':
                            isInputValid = setD(circuit, string);
                            break;

                        case 'R':
                            isInputValid = addRLC('R');
                            break;
                        case 'C':
                            isInputValid = addRLC('C');
                            break;
                        case 'L':
                            isInputValid = addRLC('L');
                            break;

                        case 'I':
                            isInputValid = addIndependentSource('I');
                            break;
                        case 'V':
                            isInputValid = addIndependentSource('V');
                            break;

                        case 'F':
                            isInputValid = addCurrentDependent('I');
                            break;
                        case 'H':
                            isInputValid = addCurrentDependent('V');
                            break;
                        case 'G':
                            isInputValid = addVoltageDependent('I');
                            break;
                        case 'E':
                            isInputValid = addVoltageDependent('V');
                            break;
                        // adding Ideal Diode
                        case 'D':
                            isInputValid = addDiode();
                            break;
                        case '.':
                            Scanner scannerTran = new Scanner(string);
                            if (scannerTran.next().equals(".tran")) {
                                circuit.setMaximumTime(unitCalculator(scannerTran.next()));
                                flagTran = true;
                                isInputValid = true;
                            }

                    }
                } else {
                    errorLine = (inputLines.size() + 1);
                    //    System.out.println("(invalid number)There is a problem found in line " + (inputLines.size() + 1));
                }
            }
            if (!isInputValid) {
                errorLine = (inputLines.size());
                //  System.out.println("(invalid input)There is a problem found in line " + inputLines.size());
            }
        } catch (Exception e) {
            errorLine = (inputLines.size());
            // System.out.println("(Exception)There is a problem found in line " + inputLines.size());
        }

        return circuit;
    }

    public boolean setD(Circuit circuit, String string) {
        Scanner scanner = new Scanner(string);
        char vdi = string.charAt(1);
        scanner.next();
        double dAmount = unitCalculator(scanner.next());
        if (dAmount <= 0)
            return false;
        switch (vdi) {
            case 'v':
                circuit.setDv(dAmount);
                break;
            case 'i':
                circuit.setDi(dAmount);
                break;
            case 't':
                circuit.setDt(dAmount);
                break;
            default:
                return false;
        }
        return true;
    }

    //methods to add an element to the circuit
    public boolean addRLC(int RLC) {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = Integer.parseInt(scanner.next());
        int negative = Integer.parseInt(scanner.next());
        if (positive < 0 || negative < 0)
            return false;
        elementAdder.addNode(positive);
        elementAdder.addNode(negative);
        double value = unitCalculator(scanner.next());
        if (value <= 0)
            return false;
        if (positive != negative) {
            switch (RLC) {
                case 'R':
                    elementAdder.addElement(name, positive, negative, "resistor", value);
                    break;
                case 'C':
                    elementAdder.addElement(name, positive, negative, "capacitor", value);
                    break;
                case 'L':
                    elementAdder.addElement(name, positive, negative, "inductance", value);
                    break;
                default:
                    return false;
            }
        }
        return true;

    }

    public boolean addIndependentSource(int IV) {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = scanner.nextInt();
        int negative = scanner.nextInt();
        if (positive < 0 || negative < 0)
            return false;
        elementAdder.addNode(positive);
        elementAdder.addNode(negative);
        double offset = unitCalculator(scanner.next());
        double amplitude = unitCalculator(scanner.next());
        double frequency = unitCalculator(scanner.next());
        double phase = unitCalculator(scanner.next());
        switch (IV) {
            case 'I':
                elementAdder.addElement(name, positive, negative, "independentCurrent", offset, amplitude, frequency, phase);
                break;
            case 'V':
                elementAdder.addElement(name, positive, negative, "independentVoltage", offset, amplitude, frequency, phase);
                break;
        }
        return true;
    }

    public boolean addCurrentDependent(int IV) {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = scanner.nextInt();
        int negative = scanner.nextInt();
        if (positive < 0 || negative < 0)
            return false;
        elementAdder.addNode(positive);
        elementAdder.addNode(negative);
        String elementDependedOn = scanner.next();
        double gain = scanner.nextDouble();
        switch (IV) {
            case 'I':
                elementAdder.addElement(name, positive, negative, "CurrentDependentCurrent", elementDependedOn, gain);
                break;
            case 'V':
                elementAdder.addElement(name, positive, negative, "CurrentDependentVoltage", elementDependedOn, gain);
                break;
        }
        return true;
    }

    public boolean addVoltageDependent(int IV) {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = scanner.nextInt();
        int negative = scanner.nextInt();
        if (positive < 0 || negative < 0)
            return false;
        elementAdder.addNode(positive);
        elementAdder.addNode(negative);
        int positiveDependent = scanner.nextInt();
        int negativeDependent = scanner.nextInt();
        if (positiveDependent < 0 || negativeDependent < 0)
            return false;
        elementAdder.addNode(positiveDependent);
        elementAdder.addNode(negativeDependent);
        double gain = scanner.nextDouble();
        switch (IV) {
            case 'I':
                elementAdder.addElement(name, positive, negative, "voltageDependentCurrent", positiveDependent, negativeDependent, gain);
                break;
            case 'V':
                elementAdder.addElement(name, positive, negative, "voltageDependentVoltage", positiveDependent, negativeDependent, gain);
                break;
        }
        return true;
    }

    public boolean addDiode() {
        Scanner scanner = new Scanner(string);
        String name = scanner.next();
        int positive = scanner.nextInt();
        int negative = scanner.nextInt();
        if (positive < 0 || negative < 0)
            return false;
        elementAdder.addNode(positive);
        elementAdder.addNode(negative);
        int isIdeal = scanner.nextInt();
        if (isIdeal == 1) {
            elementAdder.addElement(name, positive, negative, "diode");
        } else {
            return false;
        }
        return true;
    }

    //practical method

    public boolean isEveryNumberValid() {
        Scanner scanner = new Scanner(string);
        while (scanner.hasNext()) {
            if (!isNumberValid(scanner.next()))
                return false;
        }
        return true;
    }

    public static boolean isNumberValid(String number) {
        char[] numberChar = number.toCharArray();
        final char[] valids = {'G', 'M', 'k', 'm', 'u', 'n', 'p'};
        char lastChar = numberChar[number.length() - 1];
        int dotsNumber = 0;
        if (numberChar[0] < '0' || numberChar[0] > '9')
            return true;
        for (int i = 0; i < number.length() - 1; i++) {
            if (numberChar[i] == '.')
                dotsNumber++;
            else if (numberChar[i] < '0' || numberChar[i] > '9')
                return false;
        }
        if (dotsNumber > 1)
            return false;
        if (!(lastChar >= '0' && lastChar <= '9')) {
            boolean validFlag = false;
            for (char valid : valids) {
                if (lastChar == valid)
                    validFlag = true;
            }
            return validFlag;
        }
        return true;
    }

    public static double unitCalculator(String dAmount) {
        int length = dAmount.length();
        char measure = dAmount.charAt(length - 1);
        if ((int) measure >= '0' && (int) measure <= '9') {
            return Double.parseDouble(dAmount);
        } else {
            double number = Double.parseDouble(dAmount.substring(0, dAmount.length() - 1));
            switch (measure) {
                case 'M':
                    return number * 1000000;
                case 'G':
                    return number * 1000000000;
                case 'k':
                    return number * 1000;
                case 'm':
                    return number * 0.001;
                case 'u':
                    return number * 0.000001;
                case 'n':
                    return number * 0.000000001;
                case 'p':
                    return number * 0.000000000001;
                default:
                    return 0;
            }

        }
    }
}

class ElementAdder {
    final Circuit circuit;

    public ElementAdder(Circuit circuit) {
        this.circuit = circuit;
    }

    //node
    void addNode(int name) {
        int i = circuit.getNodes().size();
        if (!circuit.getNodes().containsKey(0)) {
            i++;
        }
        if (!circuit.getNodes().containsKey(name)) {
            if (name == 0) {
                circuit.getNodes().put(name, new Node(name));
                circuit.getNodes().get(name).setUnion(0);
            } else {
                circuit.getNodes().put(name, new Node(name));
                circuit.getNodes().get(name).setUnion(i);
            }
        }

    }

    //resistor,capacitor,inductor
    void addElement(String name, int positive, int negative, String type, double value) {
        if (!circuit.getElementNames().contains(name)) {
            switch (type) {
                case "resistor":
                    circuit.getElements().put(name, new Resistor(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative), value));
                    break;
                case "capacitor":
                    circuit.getElements().put(name, new Capacitor(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative), value));
                    break;
                case "inductance":
                    circuit.getElements().put(name, new Inductor(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative), value));
                    break;
                default:
                    return;
            }
            circuit.getNodes().get(positive).setNeighbors(negative);
            circuit.getNodes().get(negative).setNeighbors(positive);
            circuit.getNodes().get(positive).setPositives(name);
            circuit.getNodes().get(negative).setNegatives(name);
            circuit.getElementNames().add(name);
        }
    }

    //diode
    void addElement(String name, int positive, int negative, String type) {
        if (type.equals("diode") && !circuit.getElementNames().contains(name)) {
            circuit.getElements().put(name, new Diode(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative)));
            circuit.getNodes().get(positive).setNeighbors(negative);
            circuit.getNodes().get(negative).setNeighbors(positive);
            circuit.getNodes().get(positive).setPositives(name);
            circuit.getNodes().get(negative).setNegatives(name);
            circuit.getElementNames().add(name);
        }
    }

    //independent sources
    void addElement(String name, int positive, int negative, String type, double offset, double amplitude, double frequency, double phase) {
        if (!circuit.getElementNames().contains(name)) {
            switch (type) {
                case "independentCurrent":
                    IndependentCurrentSource independentCurrentSource = new IndependentCurrentSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative), offset, amplitude, frequency, phase);
                    independentCurrentSource.setCurrentSource(true);
                    circuit.getElements().put(name, independentCurrentSource);
                    break;
                case "independentVoltage":
                    IndependentVoltageSource independentVoltageSource = new IndependentVoltageSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative), offset, amplitude, frequency, phase);
                    independentVoltageSource.setVoltageSource(true);
                    circuit.getElements().put(name, independentVoltageSource);

                    break;
                default:
                    return;
            }
            circuit.getNodes().get(positive).setNeighbors(negative);
            circuit.getNodes().get(negative).setNeighbors(positive);
            circuit.getNodes().get(positive).setPositives(name);
            circuit.getNodes().get(negative).setNegatives(name);
            circuit.getElementNames().add(name);
        }
    }

    //voltage dependent sources
    void addElement(String name, int positive, int negative, String type, int positiveDepended, int negativeDepended, double gain) {
        if (!circuit.getElementNames().contains(name)) {
            switch (type) {
                case "voltageDependentCurrent":
                    VoltageDependentCurrentSource voltageDependentCurrentSource = new VoltageDependentCurrentSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative),
                            circuit.getNodes().get(positiveDepended), circuit.getNodes().get(negativeDepended), gain);
                    voltageDependentCurrentSource.setCurrentSource(true);
                    circuit.getElements().put(name, voltageDependentCurrentSource);
                    break;
                case "voltageDependentVoltage":
                    VoltageDependentVoltageSource voltageDependentVoltageSource = new VoltageDependentVoltageSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative),
                            circuit.getNodes().get(positiveDepended), circuit.getNodes().get(negativeDepended), gain);
                    voltageDependentVoltageSource.setVoltageSource(true);
                    circuit.getElements().put(name, voltageDependentVoltageSource);
                    break;
                default:
                    return;
            }
            circuit.getNodes().get(positive).setNeighbors(negative);
            circuit.getNodes().get(negative).setNeighbors(positive);
            circuit.getNodes().get(positive).setPositives(name);
            circuit.getNodes().get(negative).setNegatives(name);
            circuit.getElementNames().add(name);
        }
    }

    //current dependent source
    void addElement(String name, int positive, int negative, String type, String elementDependent, double gain) {
        if (!circuit.getElementNames().contains(name)) {
            switch (type) {
                case "CurrentDependentCurrent":
                    CurrentDependentCurrentSource currentDependentCurrentSource = new CurrentDependentCurrentSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative),
                            elementDependent, gain);
                    currentDependentCurrentSource.setCurrentSource(true);
                    circuit.getElements().put(name, currentDependentCurrentSource);
                    break;
                case "CurrentDependentVoltage":
                    CurrentDependentVoltageSource currentDependentVoltageSource = new CurrentDependentVoltageSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative),
                            elementDependent, gain);
                    currentDependentVoltageSource.setVoltageSource(true);
                    circuit.getElements().put(name, currentDependentVoltageSource);
                    break;
                default:
                    return;
            }
            circuit.getNodes().get(positive).setNeighbors(negative);
            circuit.getNodes().get(negative).setNeighbors(positive);
            circuit.getNodes().get(positive).setPositives(name);
            circuit.getNodes().get(negative).setNegatives(name);
            circuit.getElementNames().add(name);
        }
    }
}

class ErrorFinder {
    final Circuit circuit;

    public ErrorFinder(Circuit circuit) {
        this.circuit = circuit;
    }

    public int findErrors() {
        if (!isDSet())
            return -1;
        if (!isGroundAdded())
            return -4;
        if (!isLoopValid())
            return -5;

        return 0;
    }

    private boolean isGroundAdded() {
        return circuit.getNodes().containsKey(0);
    }

    private boolean isDSet() {
        if (circuit.getDi() == 0)
            return false;
        if (circuit.getDv() == 0)
            return false;
        return circuit.getDt() != 0;
    }

    private boolean isLoopValid() {
        circuit.setAddedNodes();
        if (circuit.getNodeNameQueue().size() < circuit.getNodes().size()) {
            return false;
        }

        for (int i = 0; i < circuit.getNodeNameQueue().size(); i++) {
            circuit.getNodes().get(circuit.getNodeNameQueue().get(i)).setAdded(false);
        }
        circuit.checkLoopValidation(new ArrayList<>(), new ArrayList<>(), 0);
        for (int i = 0; i < circuit.getNodeNameQueue().size(); i++) {
            if (!circuit.getNodes().get(circuit.getNodeNameQueue().get(i)).isAdded()) {
                return false;
            }
        }
        return true;
    }

    public boolean isCurrentSourceSeries() {
        Element elementOne, elementTwo;
        for (Map.Entry<String, Element> currentSource : circuit.getElements().entrySet()) {
            elementOne = currentSource.getValue();
            if (elementOne.isCurrentSource()) {
                for (Map.Entry<String, Element> currentSourceTwo : circuit.getElements().entrySet()) {
                    elementTwo = currentSourceTwo.getValue();
                    if (elementTwo.isCurrentSource()) {
                        if (elementTwo.equals(elementTwo))
                            continue;
                        int result = Element.isSeries(elementOne, elementTwo);
                        if (result == 5)
                            continue;
                        double current1 = elementOne.getCurrent(), current2 = elementTwo.getCurrent();
                        for (int i = 0; i < Circuit.getCircuit().getElementNames().size(); i++) {
                            if (Element.isParallel(Circuit.getCircuit().getElements().get(Circuit.getCircuit().getElementNames().get(i)), elementOne)) {
                                if (!Circuit.getCircuit().getElements().get(Circuit.getCircuit().getElementNames().get(i)).isCurrentSource())
                                    continue;
                                else if (Circuit.getCircuit().getElements().get(Circuit.getCircuit().getElementNames().get(i)).positiveNode.equals(elementOne.positiveNode)) {
                                    current1 += Circuit.getCircuit().getElements().get(Circuit.getCircuit().getElementNames().get(i)).getCurrent();
                                }
                            }
                            if (Element.isParallel(Circuit.getCircuit().getElements().get(Circuit.getCircuit().getElementNames().get(i)), elementTwo)) {
                                if (Circuit.getCircuit().getElements().get(Circuit.getCircuit().getElementNames().get(i)).isCurrentSource() &&
                                        Circuit.getCircuit().getElements().get(Circuit.getCircuit().getElementNames().get(i)).positiveNode.equals(elementTwo.positiveNode)) {
                                    current2 += Circuit.getCircuit().getElements().get(Circuit.getCircuit().getElementNames().get(i)).getCurrent();
                                }
                            }
                        }
                        if (result == 2) {
                            if (Math.abs(current1 - current2) > circuit.getDi()) {
                                return false;
                            }
                        } else if (result == 1) {
                            if (Math.abs(current1 + current2) > circuit.getDi()) {
                                return false;
                            }
                        }


                    }
                }
            }
        }
        return true;
    }

    public boolean isVoltageSourcesParallel() {
        Element elementOne, elementTwo;
        for (Map.Entry<String, Element> voltageSource : circuit.getElements().entrySet()) {
            elementOne = voltageSource.getValue();
            if (elementOne.isVoltageSource()) {
                for (Map.Entry<String, Element> voltageSourceTwo : circuit.getElements().entrySet()) {
                    elementTwo = voltageSourceTwo.getValue();
                    if (elementTwo.isVoltageSource()) {
                        if (Element.isParallel(elementOne, elementTwo)) {
                            if (elementOne.positiveNode.equals(elementTwo.positiveNode)) {
                                if (Math.abs(elementOne.getVoltage() - elementTwo.getVoltage()) > circuit.getDv()) {
                                    return false;
                                }
                                elementOne.setParallelToVoltageSource(false);
                                elementTwo.setParallelToVoltageSource(true);
                            } else if (Math.abs(elementOne.getVoltage() + elementTwo.getVoltage()) > circuit.getDv()) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}

class Node {
    private double voltage;
    private int union;
    private int branchsNumer;
    private boolean added;
    private final ArrayList<Integer> neighbors;
    private final ArrayList<String> positives;
    private final ArrayList<String> negatives;
    private final int name;
    private double previousVoltage;
    private double prePreviousVoltage;
    private final ArrayList<Double> voltagesArray;
    private int earthConnections = 0, earthConnectionsPro = 0;
    private Point location;
    int x, y;
    int xBad, yBad;
    int xEdit, yEdit;
    int counter = 0;
    double voltageToSave = 0;


    public Node(int name) {
        this.name = name;
        voltage = 0;
        previousVoltage = 0;
        prePreviousVoltage = 0;
        added = false;
        neighbors = new ArrayList<>();
        positives = new ArrayList<>();
        negatives = new ArrayList<>();
        voltagesArray = new ArrayList<>();
    }


    public int getEarthConnectionsPro() {
        return earthConnectionsPro;
    }

    public void setEarthConnectionsPro() {
        for (String positive : positives) {
            if (Circuit.getCircuit().getElements().get(positive).negativeNode.name == 0) {
                earthConnectionsPro++;
            }
        }
        for (String negative : negatives) {
            if (Circuit.getCircuit().getElements().get(negative).positiveNode.name == 0) {
                earthConnectionsPro++;
            }
        }
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getEarthConnections() {
        return earthConnections;
    }

    public void setEarthConnections() {
        earthConnections = 0;
        for (String positive : positives) {
            if (Circuit.getCircuit().getElements().get(positive).negativeNode.name == 0) {
                earthConnections++;
            }
        }
        for (String negative : negatives) {
            if (Circuit.getCircuit().getElements().get(negative).positiveNode.name == 0) {
                earthConnections++;
            }
        }
    }

    public ArrayList<Double> getVoltagesArray() {
        return voltagesArray;
    }

    public boolean equals(Node node) {
        return name == node.name;
    }


    int getName() {
        return name;
    }

    void setNeighbors(int neighbor) {
        if (!neighbors.contains(neighbor))
            neighbors.add(neighbor);
    }

    void setPositives(String name) {
        if (!positives.contains(name)) {
            branchsNumer++;
            positives.add(name);
        }
    }

    void setNegatives(String name) {
        if (!negatives.contains(name)) {
            branchsNumer++;
            negatives.add(name);
        }
    }

    ArrayList<String> getPositives() {
        return positives;
    }

    ArrayList<String> getNegatives() {
        return negatives;
    }

    ArrayList<Integer> getNeighbors() {
        return neighbors;
    }

    public int getUnion() {
        return union;
    }

    public void setUnion(int union) {
        this.union = union;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    void setVoltage(double voltage) {
        if (name != 0)
            this.voltage = voltage;
    }

    double getVoltage() {
        if (name != 0)
            return voltage;
        else return 0;
    }

    double getPreviousVoltage() {
        return previousVoltage;
    }

    public double getPrePreviousVoltage() {
        return prePreviousVoltage;
    }

    void updatePreviousVoltage() {
        double temp = Circuit.getCircuit().getMaximumTime() / 500;
        if (Circuit.getCircuit().getTime() / temp > counter) {
            temp /= Circuit.getCircuit().getDt();
            voltagesArray.add(voltageToSave / temp);
            counter++;
            voltageToSave = 0;
        } else {
            voltageToSave += getVoltage();
        }
        // voltagesArray.add(voltage);
        prePreviousVoltage = previousVoltage;
        previousVoltage = voltage;
    }

    public static int elementsBetween(Node node1, Node node2) {
        int number = 0;
        for (int i = 0; i < node1.getNegatives().size(); i++)
            if (node2.getName() == Circuit.getCircuit().getElements().get(node1.getNegatives().get(i)).positiveNode.getName()) {
                number++;
            }
        for (int i = 0; i < node1.getPositives().size(); i++)
            if (node2.getName() == Circuit.getCircuit().getElements().get(node1.getPositives().get(i)).negativeNode.getName()) {
                number++;
            }

        return number;
    }


    @Override
    public String toString() {
        return "Node{" +
                "voltage=" + voltage +
                ", union=" + union +
                ", neighbors=" + neighbors +
                ", name=" + name +
                '}';
    }
}

class Graph extends JPanel {
    final int elementsNumber;
    static double maxTime;
    final double dt;
    final double maxAmount;
    final int[][] finalArrays;
    final Color[] colors = {Color.red, Color.green, Color.yellow, Color.pink, Color.ORANGE, Color.magenta, Color.cyan, Color.darkGray, Color.lightGray, Color.gray};
    ArrayList<Double> protoArray, protoArray1;

    Graph(double dt, double maxAmount, Element[] chosenElements, int elementsNumber, char graphType) {
        setSize(1100, 501);
        this.elementsNumber = elementsNumber;
        this.maxAmount = maxAmount;
        this.dt = dt;
        finalArrays = new int[10][];
        double[] fixedArray;
        int[] finalArray;
        for (int i = 0; i < elementsNumber; i++) {
            fixedArray = new double[1100];
            double stepTime = maxTime / 1100;
            int stepDt = (int) (stepTime / dt);
            switch (graphType) {
                case 'V':
                    for (int j = 0; j < 1100; j++) {
                        double sum = 0;
                        for (int k = 0; k < stepDt; k++) {
                            sum += chosenElements[i].getVoltagesArray().get(j * stepDt + k);
                        }
                        fixedArray[j] = sum / stepDt;
                    }
                    break;
                case 'A':
                    for (int j = 0; j < 1100; j++) {
                        double sum = 0;
                        for (int k = 0; k < stepDt; k++) {
                            sum += chosenElements[i].getCurrentsArray().get(j * stepDt + k);
                        }
                        fixedArray[j] = sum / stepDt;
                    }
                    break;
                case 'W':
                    for (int j = 0; j < 1100; j++) {
                        double sum = 0;
                        for (int k = 0; k < stepDt; k++) {
                            sum += chosenElements[i].getVoltagesArray().get(j * stepDt + k) * chosenElements[i].getCurrentsArray().get(j * stepDt + k);
                        }
                        fixedArray[j] = sum / stepDt;
                    }
                    break;
            }
            finalArray = new int[1100];
            for (int j = 0; j < 1100; j++) {
                finalArray[j] = (int) (240 * (fixedArray[j] / maxAmount));
            }
            finalArrays[i] = finalArray;
        }

    }

    public static double getMaxTime() {
        return maxTime;
    }

    public static void setMaxTime(double maxTimeIn) {
        maxTime = maxTimeIn;
    }


    @Override
    public void paint(java.awt.Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        g.drawLine(0, 250, 1100, 250);
        g.drawLine(0, 0, 0, 500);

        for (int i = 0; i < elementsNumber; i++) {
            g2d.setColor(colors[i]);
            for (int j = 0; j < 1099; j++) {
                g2d.drawLine(j, 250 - finalArrays[i][j], j + 1, 250 - finalArrays[i][j + 1]);
            }
        }

        // dashed lines
        g2d.setColor(Color.black);

        float[] dashingPattern1 = {2f, 2f};
        Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
        g2d.setStroke(stroke1);

        g2d.drawLine(0, 10, 1100, 10);
        g2d.drawLine(0, 70, 1100, 70);
        g2d.drawLine(0, 130, 1100, 130);
        g2d.drawLine(0, 190, 1100, 190);
        g2d.drawLine(0, 310, 1100, 310);
        g2d.drawLine(0, 370, 1100, 370);
        g2d.drawLine(0, 430, 1100, 430);
        g2d.drawLine(0, 490, 1100, 490);


        for (int i = 70; i < 1090; i = i + 60)
            g2d.drawLine(i, 10, i, 490);
        /*
        g2d.drawLine(70, 10, 70, 490);
        g2d.drawLine(130, 10, 130, 490);
        g2d.drawLine(190, 10, 190, 490);
        g2d.drawLine(250, 10, 250, 490);
        g2d.drawLine(310, 10, 310, 490);
        g2d.drawLine(370, 10, 370, 490);
        g2d.drawLine(430, 10, 430, 490);
        g2d.drawLine(490, 10, 490, 490);
        g2d.drawLine(430, 10, 430, 490);
        g2d.drawLine(430, 10, 430, 490);
        g2d.drawLine(430, 10, 430, 490);
        g2d.drawLine(430, 10, 430, 490);
        g2d.drawLine(430, 10, 430, 490); */


    }
}

class CircuitGraphBad extends JPanel {
    int nodesNumber;
    Circuit circuit;
    ArrayList<Node> nodes;
    JDialog dialog;

    ArrayList<JLabel> labels;
    ArrayList<JLabel> namesElement;
    ArrayList<JLabel> namesNode;

    public CircuitGraphBad(int nodesNumber, Circuit circuit, ArrayList<Node> nodes, JDialog dialog, ArrayList<JLabel> labels, ArrayList<JLabel> namesElement, ArrayList<JLabel> namesNode) {
        this.nodesNumber = nodesNumber;
        this.circuit = circuit;
        this.nodes = nodes;
        this.dialog = dialog;
        this.labels = labels;
        this.namesElement = namesElement;
        this.namesNode = namesNode;

        dialog.setResizable(false);
        drawEarthConnecteds();
        drawHorizontals();
        drawVerticals();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                if (Circuit.getCircuit().getNodes().containsKey(j + 1 + (30 - (i + 1) * 6))) {
                    Circuit.getCircuit().getNodes().get(j + 1 + (30 - (i + 1) * 6)).xBad = 50 + 100 * j;
                    Circuit.getCircuit().getNodes().get(j + 1 + (30 - (i + 1) * 6)).yBad = 50 + 100 * i;
                }
            }
        }
        nodesNames();

    }

    @Override
    public void paint(java.awt.Graphics g) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                if (Circuit.getCircuit().getNodes().containsKey(j + 1 + (30 - (i + 1) * 6))) {
                    g.fillOval(48 + 100 * j, 48 + 100 * i, 5, 5);
                }
            }
        }

        for (int i = 1; i < 7; i++) {
            if (Circuit.getCircuit().getNodes().containsKey(i)) {
                if (Circuit.getCircuit().getNodes().get(i).getEarthConnections() > 1)
                    g.drawLine(100 * (Circuit.getCircuit().getNodes().get(i).getName()) - 50, 450, 100 * (Circuit.getCircuit().getNodes().get(i).getName()) - 20, 450);
                if (Circuit.getCircuit().getNodes().get(i).getEarthConnections() > 2)
                    g.drawLine(100 * (Circuit.getCircuit().getNodes().get(i).getName()) - 50, 450, 100 * (Circuit.getCircuit().getNodes().get(i).getName()) - 80, 450);
            }
        }
        for (int i = 1; i < 31; i++) {
            int between;
            Node nodeOne, nodeTwo;
            if (Circuit.getCircuit().getNodes().containsKey(i)) {
                nodeOne = Circuit.getCircuit().getNodes().get(i);
                if (Circuit.getCircuit().getNodes().containsKey(i + 1)) {
                    nodeTwo = Circuit.getCircuit().getNodes().get(i + 1);
                    between = Node.elementsBetween(nodeOne, nodeTwo);
                    if (between > 1) {
                        g.drawLine(100 * (i % 6) - 50, 500 - 100 * ((i - 1) / 6) - 75, 100 * (i % 6) - 50, 500 - 100 * ((i - 1) / 6) - 50);
                        g.drawLine(100 * ((i + 1) % 6) - 50, 500 - 100 * ((i + 1 - 1) / 6) - 75, 100 * ((i + 1) % 6) - 50, 500 - 100 * ((i + 1 - 1) / 6) - 50);
                    }
                    if (between > 2) {
                        g.drawLine(100 * (i % 6) - 50, 500 - 100 * ((i - 1) / 6) - 50, 100 * (i % 6) - 50, 500 - 100 * ((i - 1) / 6) - 25);
                        g.drawLine(100 * ((i + 1) % 6) - 50, 500 - 100 * ((i + 1 - 1) / 6) - 50, 100 * ((i + 1) % 6) - 50, 500 - 100 * ((i + 1 - 1) / 6) - 25);
                    }
                }
            }
        }
        for (int i = 1; i < 26; i++) {
            int between;
            Node nodeOne, nodeTwo;
            if (Circuit.getCircuit().getNodes().containsKey(i)) {
                nodeOne = Circuit.getCircuit().getNodes().get(i);
                if (Circuit.getCircuit().getNodes().containsKey(i + 6)) {
                    nodeTwo = Circuit.getCircuit().getNodes().get(i + 6);
                    between = Node.elementsBetween(nodeOne, nodeTwo);
                    if (between > 1) {
                        g.drawLine(100 * (i % 6) - 50, 500 - 100 * ((i - 1) / 6) - 50, 100 * (i % 6) - 25, 500 - 100 * ((i - 1) / 6) - 50);
                        g.drawLine(100 * ((i + 6) % 6) - 50, 500 - 100 * ((i + 6 - 1) / 6) - 50, 100 * ((i + 6) % 6) - 25, 500 - 100 * ((i + 6 - 1) / 6) - 50);
                    }
                    if (between > 2) {
                        g.drawLine(100 * (i % 6) - 50, 500 - 100 * ((i - 1) / 6) - 50, 100 * (i % 6) - 75, 500 - 100 * ((i - 1) / 6) - 50);
                        g.drawLine(100 * ((i + 6) % 6) - 50, 500 - 100 * ((i + 6 - 1) / 6) - 50, 100 * ((i + 6) % 6) - 75, 500 - 100 * ((i + 6 - 1) / 6) - 50);
                    }
                }
            }


        }


        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(25, 555, 575, 555);
        g2d.setStroke(new BasicStroke(8));
        g2d.drawLine(0, 0, 600, 0);
        g2d.drawLine(0, 0, 0, 600);
        g2d.drawLine(600, 0, 600, 600);
        g2d.drawLine(0, 600, 600, 600);
    }

    private void drawVerticals() {
        Element element;
        ElementShape elementShape;
        Node nodeOne, nodeTwo;
        boolean isBetween, flag;
        int elementsBetween, elementNumber = 1;
        JLabel label, name;
        for (int i = 1; i < 25; i++) {
            flag = Circuit.getCircuit().getNodes().containsKey(i);
            if (!Circuit.getCircuit().getNodes().containsKey(i + 6))
                flag = false;
            if (flag) {
                nodeOne = Circuit.getCircuit().getNodes().get(i);
                nodeTwo = Circuit.getCircuit().getNodes().get(i + 6);
                elementsBetween = Node.elementsBetween(nodeOne, nodeTwo);
                elementNumber = 1;
                if (elementsBetween > 0) {
                    for (Map.Entry<String, Element> elementLoop : Circuit.getCircuit().getElements().entrySet()) {
                        element = elementLoop.getValue();
                        isBetween = element.positiveNode.getName() == nodeOne.getName() && element.negativeNode.getName() == nodeTwo.getName();
                        if (element.positiveNode.getName() == nodeTwo.getName() && element.negativeNode.getName() == nodeOne.getName())
                            isBetween = true;
                        if (isBetween) {
                            if (element.positiveNode.getName() > element.negativeNode.getName())
                                elementShape = new ElementShape(element, false, true);
                            else
                                elementShape = new ElementShape(element, false, false);
                            dialog.add(elementShape);
                            name = new JLabel(element.name);
                            name.setFont(new Font("Arial", Font.ITALIC, 8));
                            dialog.add(name);
                            label = new JLabel(element.label);
                            label.setFont(new Font("Arial", Font.ITALIC, 8));
                            dialog.add(label);

                            namesElement.add(name);
                            labels.add(label);


                            switch (elementNumber) {
                                case 1:
                                    elementShape.setBounds(95 + 100 * (i % 6 - 1) + 0, 500 - 100 * ((i - 1) / 6) - 100, 10, 100);
                                    name.setBounds(95 + 100 * (i % 6 - 1) + 0, 495 - 100 * ((i - 1) / 6) - 100, 100, 20);
                                    label.setBounds(95 + 100 * (i % 6 - 1) + 0, 505 - 100 * ((i - 1) / 6) - 100, 100, 20);
                                    break;
                                case 2:
                                    elementShape.setBounds(95 + 100 * (i % 6 - 1) + 25, 500 - 100 * ((i - 1) / 6) - 100, 10, 100);
                                    name.setBounds(95 + 100 * (i % 6 - 1) + 25, 495 - 100 * ((i - 1) / 6) - 100 + 70, 100, 20);
                                    label.setBounds(95 + 100 * (i % 6 - 1) + 25, 505 - 100 * ((i - 1) / 6) - 100 + 70, 100, 20);
                                    break;
                                case 3:
                                    elementShape.setBounds(95 + 100 * (i % 6 - 1) - 25, 500 - 100 * ((i - 1) / 6) - 100, 10, 100);
                                    name.setBounds(95 + 100 * (i % 6 - 1) - 25, 495 - 100 * ((i - 1) / 6) - 100 + 70, 100, 20);
                                    label.setBounds(95 + 100 * (i % 6 - 1) - 25, 505 - 100 * ((i - 1) / 6) - 100 + 70, 100, 20);
                                    break;
                            }
                            elementNumber++;
                        }
                    }
                }
            }
        }
    }

    private void drawEarthConnecteds() {
        Node node;
        ElementShape elementShape;
        int elementNumber = 1;
        Element element;
        JLabel label, name;
        for (Map.Entry<Integer, Node> nodeMap : Circuit.getCircuit().getNodes().entrySet()) {
            elementNumber = 1;
            node = nodeMap.getValue();
            if (node.getName() < 7) {
                for (int j = 0; j < node.getNegatives().size(); j++) {
                    element = circuit.getElements().get(node.getNegatives().get(j));
                    if (element.positiveNode.getName() == 0) {
                        elementShape = new ElementShape(element);
                        name = new JLabel(element.name);
                        name.setFont(new Font("Arial", Font.ITALIC, 8));

                        label = new JLabel(element.label);
                        label.setFont(new Font("Arial", Font.ITALIC, 8));

                        dialog.add(elementShape);
                        dialog.add(name);
                        dialog.add(label);

                        namesElement.add(name);
                        labels.add(label);

                        int addedY = 70;

                        switch (elementNumber) {
                            case 1:
                                addedY = 0;
                                elementShape.setBounds(95 + 100 * (node.getName() - 1), 500, 10, 100);
                                name.setBounds(95 + 100 * (node.getName() - 1), 505 + addedY, 100, 20);
                                label.setBounds(95 + 100 * (node.getName() - 1), 515 + addedY, 100, 20);
                                break;
                            case 2:
                                elementShape.setBounds(95 + 25 + 100 * (node.getName() - 1), 500, 10, 100);
                                name.setBounds(95 + 25 + 100 * (node.getName() - 1), 500 + addedY, 100, 20);
                                label.setBounds(95 + 25 + 100 * (node.getName() - 1), 510 + addedY, 100, 20);
                                break;
                            case 3:
                                elementShape.setBounds(95 - 25 + 100 * (node.getName() - 1), 500, 10, 100);
                                name.setBounds(95 - 25 + 100 * (node.getName() - 1), 500 + addedY, 100, 20);
                                label.setBounds(95 - 25 + 100 * (node.getName() - 1), 510 + addedY, 100, 20);
                                break;
                        }


                        elementNumber++;
                    }
                }
                for (int j = 0; j < node.getPositives().size(); j++) {
                    element = circuit.getElements().get(node.getPositives().get(j));
                    if (element.negativeNode.getName() == 0) {
                        elementShape = new ElementShape(element);
                        name = new JLabel(element.name);
                        name.setFont(new Font("Arial", Font.ITALIC, 8));

                        label = new JLabel(element.label);
                        label.setFont(new Font("Arial", Font.ITALIC, 8));

                        dialog.add(elementShape);
                        dialog.add(name);
                        dialog.add(label);

                        namesElement.add(name);
                        labels.add(label);

                        int addedY = 70;
                        switch (elementNumber) {
                            case 1:
                                addedY = 0;
                                elementShape.setBounds(95 + 100 * (node.getName() - 1), 500, 10, 100);
                                name.setBounds(95 + 100 * (node.getName() - 1), 500 + addedY, 100, 20);
                                label.setBounds(95 + 100 * (node.getName() - 1), 510 + addedY, 100, 20);
                                break;
                            case 2:
                                elementShape.setBounds(95 + 25 + 100 * (node.getName() - 1), 500, 10, 100);
                                name.setBounds(95 + 25 + 100 * (node.getName() - 1), 500 + addedY, 100, 20);
                                label.setBounds(95 + 25 + 100 * (node.getName() - 1), 510 + addedY, 100, 20);
                                break;
                            case 3:
                                elementShape.setBounds(95 - 25 + 100 * (node.getName() - 1), 500, 10, 100);
                                name.setBounds(95 - 25 + 100 * (node.getName() - 1), 500 + addedY, 100, 20);
                                label.setBounds(95 - 25 + 100 * (node.getName() - 1), 510 + addedY, 100, 20);
                                break;
                        }

                        elementNumber++;
                    }
                }
            }
        }
    }

    private void drawHorizontals() {
        Element element;
        ElementShape elementShape;
        Node nodeOne, nodeTwo;
        boolean isBetween, flag;
        int elementsBetween, elementsNumber = 1;
        JLabel label, name;
        for (int i = 1; i < 31; i++) {
            flag = i % 6 != 0;
            if (!Circuit.getCircuit().getNodes().containsKey(i))
                flag = false;
            if (!Circuit.getCircuit().getNodes().containsKey(i + 1))
                flag = false;

            if (flag) {
                nodeOne = Circuit.getCircuit().getNodes().get(i);
                nodeTwo = Circuit.getCircuit().getNodes().get(i + 1);
                elementsBetween = Node.elementsBetween(nodeOne, nodeTwo);
                elementsNumber = 1;
                if (elementsBetween > 0) {
                    for (Map.Entry<String, Element> elementLoop : Circuit.getCircuit().getElements().entrySet()) {
                        element = elementLoop.getValue();
                        isBetween = element.positiveNode.getName() == nodeOne.getName() && element.negativeNode.getName() == nodeTwo.getName();
                        if (element.positiveNode.getName() == nodeTwo.getName() && element.negativeNode.getName() == nodeOne.getName())
                            isBetween = true;

                        if (isBetween) {
                            if (element.positiveNode.getName() > element.negativeNode.getName())
                                elementShape = new ElementShape(element, true, true);

                            else
                                elementShape = new ElementShape(element, true, false);

                            name = new JLabel(element.name);
                            name.setFont(new Font("Arial", Font.ITALIC, 8));
                            label = new JLabel(element.label);
                            label.setFont(new Font("Arial", Font.ITALIC, 8));

                            dialog.add(elementShape);
                            dialog.add(name);
                            dialog.add(label);

                            namesElement.add(name);
                            labels.add(label);

                            switch (elementsNumber) {
                                case 1:
                                    elementShape.setBounds(100 + 100 * (i % 6 - 1), 495 - 100 * ((i - 1) / 6) + 0, 100, 10);
                                    name.setBounds(100 + 100 * (i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) + 0 - 15, 100, 20);
                                    label.setBounds(100 + 100 * (i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) + 0 + 5, 100, 20);
                                    break;
                                case 3:
                                    elementShape.setBounds(100 + 100 * (i % 6 - 1), 495 - 100 * ((i - 1) / 6) + 25, 100, 10);
                                    name.setBounds(100 + 100 * (i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) + 25 - 15, 100, 20);
                                    label.setBounds(100 + 100 * (i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) + 0 + 25, 100, 20);
                                    break;
                                case 2:
                                    elementShape.setBounds(100 + 100 * (i % 6 - 1), 495 - 100 * ((i - 1) / 6) - 25, 100, 10);
                                    name.setBounds(100 + 100 * (i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) - 25 - 15, 100, 20);
                                    label.setBounds(100 + 100 * (i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) - 25 + 5, 100, 20);
                                    break;

                            }

                            elementsNumber++;

                        }
                    }
                }
            }
        }
    }

    private void nodesNames() {
        JLabel name;
        for (int i = 1; i < 31; i++) {
            boolean contains = false;
            if (Circuit.getCircuit().getNodes().containsKey(i)) {
                name = new JLabel(String.valueOf(Circuit.getCircuit().getNodes().get(i).getName()));
                name.setBounds(Circuit.getCircuit().getNodes().get(i).xBad + 45, Circuit.getCircuit().getNodes().get(i).yBad + 35, 100, 20);
                dialog.add(name);
                for (int j = 0; j < namesNode.size(); j++) {
                    if (namesNode.get(j).getText().equals(name.getText()))
                        contains = true;
                }
                if (!contains)
                    namesNode.add(name);
            }
        }
    }
}

class CircuitGraphEdited extends JPanel {
    final int nodesNumber;
    final Circuit circuit;
    final ArrayList<Node> nodes;
    final JDialog dialog;
    ArrayList<Integer> locations;
    ArrayList<JLabel> labels;
    ArrayList<JLabel> namesElement;
    ArrayList<JLabel> namesNode;

    public CircuitGraphEdited(int nodesNumber, Circuit circuit, ArrayList<Node> nodes, JDialog dialog, ArrayList<JLabel> labels, ArrayList<JLabel> namesElement, ArrayList<JLabel> namesNode) {
        this.nodesNumber = nodesNumber;
        this.circuit = circuit;
        this.nodes = nodes;
        this.dialog = dialog;
        this.labels = labels;
        this.namesElement = namesElement;
        this.namesNode = namesNode;

        locations = new ArrayList<>();
        int location;
        locations.add(0);
        for (int i = 1; i < nodes.size(); i++) {
            location = 100;
            nodes.get(i).setEarthConnections();
            if (nodes.get(i).getEarthConnections() > 0)
                locations.add(i, 0);
            else {
                for (int j = 1; j < nodes.size() && j != i; j++) {
                    if (Node.elementsBetween(nodes.get(i), nodes.get(j)) > 0 && Math.abs(i - j) < location)
                        location = Math.abs(i - j);
                }
                locations.add(i, location);
            }
        }


        nodesLabel();
        drawEarthConnecteds();
        drawHorizontals();
    }

    @Override
    public void paint(java.awt.Graphics g) {
        int earth;
        JLabel label;
        for (int i = 1; i < nodes.size(); i++) {
            g.fillOval(48 + (i - 1) * 100, 448 - locations.get(i) * 50, 5, 5);
            label = new JLabel(String.valueOf(nodes.get(i).getName()));
            label.setBounds(45 + (i - 1) * 100, 455, 50, 50);
            dialog.add(label);
            nodes.get(i).setEarthConnections();
            earth = nodes.get(i).getEarthConnections();
            if (earth > 1) {
                g.drawLine(50 + (i - 1) * 100, 450, 50 + (i - 1) * 100 + 15 * (earth - 1), 450);
            }
        }
        int parralles;
        for (int i = 1; i < nodes.size() - 1; i++) {
            for (int j = (i + 1); j < nodes.size(); j++) {
                parralles = Node.elementsBetween(nodes.get(i), nodes.get(j));
                if (parralles > 0) {
                    g.drawLine(50 + 100 * (i - 1), 450 - 50 * locations.get(i), 50 + 100 * (i - 1), 450 - 50 * (j - i) - 3 * i);
                    g.drawLine(50 + 100 * (j - 1), 450 - 50 * locations.get(j), 50 + 100 * (j - 1), 450 - 50 * (j - i) - 3 * i);
                }
                for (int k = 0; k < parralles; k++) {
                    g.drawLine(50 + 100 * (i - 1) + 100, 450 - 50 * (j - i) + k * 15 - 3 * i, 50 + 100 * (j - 1), 450 - 50 * (j - i) + k * 15 - 3 * i);
                    if (Math.abs(i-j) == locations.get(j))
                        g.drawLine(50 + 100 * (j - 1), 450 - 50*locations.get(j), 50 + 100 * (j - 1), 450 - 50*locations.get(j) + 15*(parralles-1));
                    if (Math.abs(i-j) == locations.get(i))
                        g.drawLine(50 + 100 * (i - 1), 450 - 50*locations.get(i), 50 + 100 * (i - 1), 450 - 50*locations.get(i) + 15*(parralles -1));
                }
            }
        }


        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(5));
        g.drawLine(50, 552, 1000, 552);
        g2d.setStroke(new BasicStroke(8));
        g2d.drawLine(5, 50, 1080, 50);
        g2d.drawLine(5, 50, 5, 560);
        g2d.drawLine(1080, 50, 1080, 560);
        g2d.drawLine(5, 560, 1080, 560);


    }

    private void drawHorizontals() {
        Element element;
        ElementShape elementShape;
        boolean isBetween;
        int parralles;
        JLabel label, name;
        for (int i = 1; i < nodes.size() - 1; i++) {
            for (int j = (i + 1); j < nodes.size(); j++) {
                parralles = 0;
                for (Map.Entry<String, Element> elementLoop : Circuit.getCircuit().getElements().entrySet()) {
                    element = elementLoop.getValue();
                    isBetween = element.positiveNode.getName() == nodes.get(j).getName() && element.negativeNode.getName() == nodes.get(i).getName();
                    if (element.positiveNode.getName() == nodes.get(i).getName() && element.negativeNode.getName() == nodes.get(j).getName())
                        isBetween = true;
                    if (isBetween) {
                        elementShape = new ElementShape(element);
                        int diff = j - i;
                        elementShape.setBounds(100 * i - 50, 445 - 50 * (j - i) + parralles * 15 - 3 * (i), 100, 10);
                        dialog.add(elementShape);

                        name = new JLabel(element.name);
                        name.setBounds(100 * i + 70 * (parralles % 2) - 50, 445 - 50 * (j - i) + parralles * 15 - 15 - 3 * (i), 100, 20);
                        name.setFont(new Font("Arial", Font.ITALIC, 8));
                        namesElement.add(name);
                        dialog.add(name);

                        label = new JLabel(element.label);
                        label.setBounds(100 * i + 70 * (parralles % 2) - 50, 445 - 50 * (j - i) + parralles * 15 + 5 - 3 * (i), 100, 20);
                        label.setFont(new Font("Arial", Font.ITALIC, 8));
                        labels.add(label);
                        dialog.add(label);

                        parralles++;

                    }
                }
            }
        }
    }

    private void drawEarthConnecteds() {
        Node node;
        ElementShape elementShape;
        int elementNumber;
        Element element;
        JLabel label, name;
        for (int i = 1; i < nodes.size(); i++) {
            elementNumber = 1;
            node = nodes.get(i);
            for (int j = 0; j < node.getNegatives().size(); j++) {
                element = circuit.getElements().get(node.getNegatives().get(j));
                if (element.positiveNode.getName() == 0) {
                    elementShape = new ElementShape(element);
                    elementShape.setBounds((45 + 100 * (i - 1)) + 15 * (elementNumber - 1), 450, 10, 100);
                    dialog.add(elementShape);

                    name = new JLabel(element.name);
                    name.setBounds((45 + 100 * (i - 1)) + 15 * (elementNumber - 1) - 5, 445 + 80 * (elementNumber % 2), 100, 20);
                    name.setFont(new Font("Arial", Font.ITALIC, 8));
                    namesElement.add(name);
                    dialog.add(name);

                    label = new JLabel(element.label);
                    label.setBounds((45 + 100 * (i - 1)) + 15 * (elementNumber - 1) - 5, 455 + 80 * (elementNumber % 2), 100, 20);
                    label.setFont(new Font("Arial", Font.ITALIC, 8));
                    labels.add(label);
                    dialog.add(label);

                    elementNumber++;
                }
            }
            for (int j = 0; j < node.getPositives().size(); j++) {
                element = circuit.getElements().get(node.getPositives().get(j));
                if (element.negativeNode.getName() == 0) {
                    elementShape = new ElementShape(element);
                    elementShape.setBounds((45 + 100 * (i - 1)) + 15 * (elementNumber - 1), 450, 10, 100);
                    dialog.add(elementShape);

                    name = new JLabel(element.name);
                    name.setBounds((45 + 100 * (i - 1)) + 15 * (elementNumber - 1) - 5, 445 + 80 * (elementNumber % 2), 100, 20);
                    name.setFont(new Font("Arial", Font.ITALIC, 8));
                    dialog.add(name);

                    label = new JLabel(element.label);
                    label.setBounds((45 + 100 * (i - 1)) + 15 * (elementNumber - 1) - 5, 455 + 80 * (elementNumber % 2), 100, 20);
                    label.setFont(new Font("Arial", Font.ITALIC, 8));
                    dialog.add(label);


                    elementNumber++;
                }
            }
        }


    }

    private void nodesLabel() {
        JLabel label;
        for (int i = 1; i < nodes.size(); i++) {
            label = new JLabel(String.valueOf(nodes.get(i).getName()));
            label.setBounds(45 + (i - 1) * 100, 415 - locations.get(i) * 50, 50, 50);
            dialog.add(label);
            namesNode.add(label);
        }
    }
}

class ElementShape extends JPanel {
    Element element;
    char type;
    boolean isHorizental;
    boolean isUpWardRightWard;

    ElementShape(Element element, boolean isHorizental, boolean isUpWardRightWard) {
        this.element = element;
        this.isHorizental = isHorizental;
        this.isUpWardRightWard = isUpWardRightWard;
        type = element.name.charAt(0);
    }

    ElementShape(Element element) {
        this.element = element;
        if (element.positiveNode.getName() == 0 || element.negativeNode.getName() == 0) {
            isHorizental = false;
        } else {
            isHorizental = true;
        }
        type = element.name.charAt(0);
        if (element.positiveNode.getName() > element.negativeNode.getName())
            isUpWardRightWard = true;
        else
            isUpWardRightWard = false;
    }

    @Override
    public void paint(java.awt.Graphics g) {
        if (isHorizental && isUpWardRightWard) {
            setSize(100, 10);
            g.drawLine(25, 5, 0, 5);
            g.drawLine(75, 5, 100, 5);
            switch (type) {
                case 'R':
                    setSize(100, 10);
                    g.drawLine(25, 5, 0, 5);
                    g.drawLine(75, 5, 100, 5);
                    g.drawLine(25, 5, 35, 0);
                    g.drawLine(35, 0, 45, 10);
                    g.drawLine(45, 10, 55, 0);
                    g.drawLine(55, 0, 65, 10);
                    g.drawLine(65, 10, 75, 5);
                    break;
                case 'C':
                    g.drawLine(40, 5, 0, 5);
                    g.drawLine(60, 5, 100, 5);
                    g.drawLine(40, 0, 40, 10);
                    g.drawLine(60, 0, 60, 10);
                    break;
                case 'L':
                    for (int k = 0; k < 5; k++)
                        g.drawOval(25 + 10 * k, 3, 10, 10);
                    break;

                case 'V':
                    g.drawLine(25, 0, 25, 10);
                    g.drawLine(75, 0, 75, 10);
                    g.drawLine(25, 1, 75, 1);
                    g.drawLine(25, 9, 75, 9);

                    g.drawLine(56, 3, 56, 7);
                    g.drawLine(59, 5, 53, 5);

                    g.drawLine(45, 3, 45, 7);

                    break;

                case 'I':
                    g.drawLine(25, 0, 25, 10);
                    g.drawLine(75, 0, 75, 10);
                    g.drawLine(25, 1, 75, 1);
                    g.drawLine(25, 9, 75, 9);

                    g.drawLine(30, 5, 70, 5);
                    g.drawLine(30, 5, 35, 0);
                    g.drawLine(30, 5, 35, 10);
                    break;


                case 'E':
                case 'H':
                    g.drawLine(50, 0, 25, 5);
                    g.drawLine(50, 0, 75, 5);
                    g.drawLine(50, 10, 25, 5);
                    g.drawLine(50, 10, 75, 5);

                    g.drawLine(56, 3, 56, 7);
                    g.drawLine(59, 5, 53, 5);

                    g.drawLine(45, 3, 45, 7);
                    break;

                case 'G':
                case 'F':
                    g.drawLine(50, 0, 25, 5);
                    g.drawLine(50, 0, 75, 5);
                    g.drawLine(50, 10, 25, 5);
                    g.drawLine(50, 10, 75, 5);

                    g.drawLine(30, 5, 70, 5);
                    g.drawLine(30, 5, 35, 0);
                    g.drawLine(30, 5, 35, 10);
                    break;

                case 'D':
                    g.drawLine(25, 5, 45, 5);
                    g.drawLine(55, 5, 75, 5);
                    g.drawLine(45, 0, 45, 10);
                    g.drawLine(55, 0, 55, 10);

                    g.drawLine(55, 0, 45, 5);
                    g.drawLine(55, 10, 45, 5);
                    break;
            }
        }

        if (isHorizental && !isUpWardRightWard) {
            setSize(100, 10);
            g.drawLine(25, 5, 0, 5);
            g.drawLine(75, 5, 100, 5);
            switch (type) {
                case 'R':
                    setSize(100, 10);
                    g.drawLine(25, 5, 0, 5);
                    g.drawLine(75, 5, 100, 5);
                    g.drawLine(25, 5, 35, 0);
                    g.drawLine(35, 0, 45, 10);
                    g.drawLine(45, 10, 55, 0);
                    g.drawLine(55, 0, 65, 10);
                    g.drawLine(65, 10, 75, 5);
                    break;
                case 'C':
                    g.drawLine(40, 5, 0, 5);
                    g.drawLine(60, 5, 100, 5);
                    g.drawLine(40, 0, 40, 10);
                    g.drawLine(60, 0, 60, 10);
                    break;
                case 'L':
                    for (int k = 0; k < 5; k++)
                        g.drawOval(25 + 10 * k, 3, 10, 10);
                    break;

                case 'V':
                    g.drawLine(25, 0, 25, 10);
                    g.drawLine(75, 0, 75, 10);
                    g.drawLine(25, 1, 75, 1);
                    g.drawLine(25, 9, 75, 9);

                    g.drawLine(44, 3, 44, 7);
                    g.drawLine(41, 5, 47, 5);

                    g.drawLine(55, 3, 55, 7);
                    break;

                case 'I':
                    g.drawLine(25, 0, 25, 10);
                    g.drawLine(75, 0, 75, 10);
                    g.drawLine(25, 1, 75, 1);
                    g.drawLine(25, 9, 75, 9);

                    g.drawLine(30, 5, 70, 5);

                    g.drawLine(70, 5, 65, 0);
                    g.drawLine(70, 5, 65, 10);
                    break;


                case 'E':
                case 'H':
                    g.drawLine(50, 0, 25, 5);
                    g.drawLine(50, 0, 75, 5);
                    g.drawLine(50, 10, 25, 5);
                    g.drawLine(50, 10, 75, 5);

                    g.drawLine(44, 3, 44, 7);
                    g.drawLine(41, 5, 47, 5);

                    g.drawLine(55, 3, 55, 7);
                    break;

                case 'G':
                case 'F':
                    g.drawLine(50, 0, 25, 5);
                    g.drawLine(50, 0, 75, 5);
                    g.drawLine(50, 10, 25, 5);
                    g.drawLine(50, 10, 75, 5);

                    g.drawLine(30, 5, 70, 5);
                    g.drawLine(70, 5, 65, 0);
                    g.drawLine(70, 5, 65, 10);
                    break;

                case 'D':
                    g.drawLine(25, 5, 45, 5);
                    g.drawLine(55, 5, 75, 5);
                    g.drawLine(45, 0, 45, 10);
                    g.drawLine(55, 0, 55, 10);

                    g.drawLine(45, 0, 55, 5);
                    g.drawLine(45, 10, 55, 5);
                    break;
            }
        }

        if (!isHorizental && isUpWardRightWard) {
            setSize(10, 100);
            g.drawLine(5, 25, 5, 0);
            g.drawLine(5, 75, 5, 100);
            switch (type) {
                case 'R':
                    g.drawLine(5, 25, 0, 35);
                    g.drawLine(0, 35, 10, 45);
                    g.drawLine(10, 45, 0, 55);
                    g.drawLine(0, 55, 10, 65);
                    g.drawLine(10, 65, 5, 75);
                    break;
                case 'C':
                    g.drawLine(5, 40, 5, 0);
                    g.drawLine(5, 60, 5, 100);
                    g.drawLine(0, 40, 10, 40);
                    g.drawLine(0, 60, 10, 60);
                    break;
                case 'L':
                    for (int k = 0; k < 5; k++)
                        g.drawOval(3, 25 + 10 * k, 10, 10);
                    break;
                case 'V':
                    g.drawLine(0, 25, 10, 25);
                    g.drawLine(0, 75, 10, 75);
                    g.drawLine(1, 25, 1, 75);
                    g.drawLine(9, 25, 9, 75);

                    g.drawLine(3, 44, 7, 44);
                    g.drawLine(5, 41, 5, 47);

                    g.drawLine(3, 55, 7, 55);
                    break;
                case 'I':
                    g.drawLine(0, 25, 10, 25);
                    g.drawLine(0, 75, 10, 75);
                    g.drawLine(1, 25, 1, 75);
                    g.drawLine(9, 25, 9, 75);

                    g.drawLine(5, 30, 5, 70);
                    g.drawLine(5, 70, 0, 65);
                    g.drawLine(5, 70, 10, 65);
                    break;

                case 'E':
                case 'H':
                    g.drawLine(0, 50, 5, 25);
                    g.drawLine(0, 50, 5, 75);
                    g.drawLine(10, 50, 5, 25);
                    g.drawLine(10, 50, 5, 75);

                    g.drawLine(3, 44, 7, 44);
                    g.drawLine(5, 41, 5, 47);

                    g.drawLine(3, 55, 7, 55);
                    break;

                case 'G':
                case 'F':
                    g.drawLine(0, 50, 5, 25);
                    g.drawLine(0, 50, 5, 75);
                    g.drawLine(10, 50, 5, 25);
                    g.drawLine(10, 50, 5, 75);

                    g.drawLine(5, 30, 5, 70);
                    g.drawLine(5, 70, 0, 65);
                    g.drawLine(5, 70, 10, 65);
                    break;

                case 'D':
                    g.drawLine(5, 25, 5, 45);
                    g.drawLine(5, 75, 5, 55);
                    g.drawLine(0, 45, 10, 45);
                    g.drawLine(0, 55, 10, 55);

                    g.drawLine(0, 45, 5, 55);
                    g.drawLine(10, 45, 5, 55);
                    break;

            }
        }

        if (!isHorizental && !isUpWardRightWard) {
            setSize(10, 100);
            g.drawLine(5, 25, 5, 0);
            g.drawLine(5, 75, 5, 100);
            switch (type) {
                case 'R':
                    g.drawLine(5, 25, 0, 35);
                    g.drawLine(0, 35, 10, 45);
                    g.drawLine(10, 45, 0, 55);
                    g.drawLine(0, 55, 10, 65);
                    g.drawLine(10, 65, 5, 75);
                    break;
                case 'C':
                    g.drawLine(5, 40, 5, 0);
                    g.drawLine(5, 60, 5, 100);
                    g.drawLine(0, 40, 10, 40);
                    g.drawLine(0, 60, 10, 60);
                    break;
                case 'L':
                    for (int k = 0; k < 5; k++)
                        g.drawOval(3, 25 + 10 * k, 5, 10);
                    break;
                case 'V':
                    g.drawLine(0, 25, 10, 25);
                    g.drawLine(0, 75, 10, 75);
                    g.drawLine(1, 25, 1, 75);
                    g.drawLine(9, 25, 9, 75);

                    g.drawLine(3, 56, 7, 56);
                    g.drawLine(5, 59, 5, 53);

                    g.drawLine(3, 45, 7, 45);
                    break;
                case 'I':
                    g.drawLine(0, 25, 10, 25);
                    g.drawLine(0, 75, 10, 75);
                    g.drawLine(1, 25, 1, 75);
                    g.drawLine(9, 25, 9, 75);

                    g.drawLine(5, 30, 5, 70);
                    g.drawLine(5, 30, 0, 35);
                    g.drawLine(5, 30, 10, 35);
                    break;
                case 'E':
                case 'H':
                    g.drawLine(0, 50, 5, 25);
                    g.drawLine(0, 50, 5, 75);
                    g.drawLine(10, 50, 5, 25);
                    g.drawLine(10, 50, 5, 75);

                    g.drawLine(3, 56, 7, 56);
                    g.drawLine(5, 59, 5, 53);

                    g.drawLine(3, 45, 7, 45);
                    break;

                case 'G':
                case 'F':
                    g.drawLine(0, 50, 5, 25);
                    g.drawLine(0, 50, 5, 75);
                    g.drawLine(10, 50, 5, 25);
                    g.drawLine(10, 50, 5, 75);

                    g.drawLine(5, 30, 5, 70);
                    g.drawLine(5, 30, 0, 35);
                    g.drawLine(5, 30, 10, 35);
                    break;
                case 'D':
                    g.drawLine(5, 25, 5, 45);
                    g.drawLine(5, 75, 5, 55);
                    g.drawLine(0, 45, 10, 45);
                    g.drawLine(0, 55, 10, 55);

                    g.drawLine(0, 55, 5, 45);
                    g.drawLine(10, 55, 5, 45);
                    break;
            }
        }
    }
}

abstract class Element extends Circuit {
    Node positiveNode, negativeNode;
    ArrayList<Double> currentsArray = new ArrayList<>();
    String label;

    public static int isSeries(Element elementOne, Element elementTwo) {
        if (elementOne.equals(elementTwo))
            return 5;
        ArrayList<Integer> temp = new ArrayList<>();
        ArrayList<ArrayList<Integer>> roadsPositive = seriesHelper(elementOne.positiveNode.getName(), elementTwo.positiveNode.getName(), elementOne.negativeNode.getName(), temp);
        temp = new ArrayList<>();
        ArrayList<ArrayList<Integer>> roadsNegative = seriesHelper(elementOne.negativeNode.getName(), elementTwo.negativeNode.getName(), elementOne.positiveNode.getName(), temp);

        if (Objects.isNull(roadsPositive) || Objects.isNull(roadsNegative))
            return 0;

        boolean passedNegative, passedPositive;
        passedNegative = roadsPositive.get(0).contains(elementTwo.negativeNode.getName());
        passedPositive = roadsNegative.get(0).contains(elementTwo.positiveNode.getName());


        for (ArrayList<Integer> list : roadsPositive) {
            if (passedNegative) {
                if (!list.contains(elementTwo.negativeNode.getName())) {
                    return 0;
                }
            } else if (list.contains(elementTwo.negativeNode.getName())) {
                return 0;
            }
            list.remove(Integer.valueOf(elementTwo.negativeNode.getName()));
            list.remove(Integer.valueOf(elementTwo.positiveNode.getName()));
        }

        for (ArrayList<Integer> arrayList : roadsNegative) {
            if (passedPositive) {
                if (!arrayList.contains(elementTwo.positiveNode.getName())) {
                    return 0;
                }
            } else if (arrayList.contains(elementTwo.positiveNode.getName())) {
                return 0;
            }
            arrayList.remove(Integer.valueOf(elementTwo.negativeNode.getName()));
            arrayList.remove(Integer.valueOf(elementTwo.positiveNode.getName()));
        }
        for (ArrayList<Integer> integerArrayList : roadsPositive) {
            for (ArrayList<Integer> integers : roadsNegative) {
                if (integerArrayList.removeAll(integers) || integers.removeAll(integerArrayList)) {
                    return 0;
                }
            }
        }
        if (passedNegative)
            return 2;
        else return 1;
    }

    protected ArrayList<Double> voltagesArray = new ArrayList<>();
    protected String name;
    private boolean isVoltageSource = false;
    private boolean isCurrentSource = false;

    public ArrayList<Double> getCurrentsArray() {
        return currentsArray;
    }

    public ArrayList<Double> getVoltagesArray() {
        return voltagesArray;
    }

    public double getVoltage() {
        return positiveNode.getVoltage() - negativeNode.getVoltage();
    }

    public double getPreviousVoltage() {
        return positiveNode.getPreviousVoltage() - negativeNode.getPreviousVoltage();
    }

    protected String provideLabel(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.###E0");
        return decimalFormat.format(value).replace("E0", "");
    }

    public void updateTime() {
        currentsArray.add(getCurrent());
        voltagesArray.add(getVoltage());
    }

    public double getCurrentMax() {
        double max = 0;
        for (Double aDouble : currentsArray) {
            if (Math.abs(aDouble) >= max)
                max = Math.abs(aDouble);
        }
        return max;
    }

    public double getPowerMax() {
        double max = 0;
        for (int i = 0; i < voltagesArray.size(); i++) {
            double aDouble = voltagesArray.get(i) * currentsArray.get(i);
            if (Math.abs(aDouble) >= max)
                max = Math.abs(aDouble);
        }
        return max;
    }

    public double getVoltageMax() {
        double max = 0;
        for (Double aDouble : voltagesArray) {
            if (Math.abs(aDouble) >= max)
                max = Math.abs(aDouble);
        }
        return max;
    }

    abstract double getCurrent();

    public static boolean isParallel(Element elementOne, Element elementTwo) {
        return (elementOne.positiveNode.equals(elementTwo.positiveNode) && elementOne.negativeNode.equals(elementTwo.negativeNode))
                || (elementOne.positiveNode.equals(elementTwo.negativeNode) && elementOne.negativeNode.equals(elementTwo.positiveNode));
    }

    abstract void setLabel(String label);

    private static ArrayList<ArrayList<Integer>> seriesHelper(int currentNode, int destinationNode, int forbiddenNode, ArrayList<Integer> nodesPassed) {
        nodesPassed.add(currentNode);
        if (currentNode == destinationNode) {
            ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
            temp.add(nodesPassed);
            return temp;
        }
        boolean deadEnd = true;
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for (int i = 0; i < Circuit.getCircuit().getNodes().get(currentNode).getNeighbors().size(); i++) {
            if (!(nodesPassed.contains(Circuit.getCircuit().getNodes().get(currentNode).getNeighbors().get(i)) || Circuit.getCircuit().getNodes().get(currentNode).getNeighbors().get(i) == forbiddenNode)) {
                deadEnd = false;
                ArrayList<ArrayList<Integer>> temp = seriesHelper(Circuit.getCircuit().getNodes().get(currentNode).getNeighbors().get(i), destinationNode, forbiddenNode, new ArrayList<>(nodesPassed));
                if (Objects.nonNull(temp)) {
                    result.addAll(temp);
                }
            }
        }
        if (deadEnd)
            return null;
        return result;


    }

    public void setParallelToVoltageSource(boolean b) {
    }

    public boolean isVoltageSource() {
        return isVoltageSource;
    }

    public void setVoltageSource(boolean voltageSource) {
        isVoltageSource = voltageSource;
    }

    public boolean isCurrentSource() {
        return isCurrentSource;
    }

    public void setCurrentSource(boolean currentSource) {
        isCurrentSource = currentSource;
    }

    public boolean equals(Element element) {
        return this.name.equals(element.name);
    }

    @Override
    public String toString() {
        return "Element{" +
                "name='" + name + '\'' +
                ", positiveNode=" + positiveNode.getName() +
                ", negativeNode=" + negativeNode.getName() +
                ", current=" + getCurrent() +
                ", voltage=" + getVoltage() +
                '}' + "\n";
    }
}

class Resistor extends Element {
    private final double resistance;

    public Resistor(String name, Node positiveNode, Node negativeNode, double resistance) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.resistance = resistance;
        setLabel(provideLabel(resistance));
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    double getCurrent() {
        return getVoltage() / resistance;
    }
}

class Capacitor extends Element {
    private final double capacity;

    public Capacitor(String name, Node positiveNode, Node negativeNode, double capacity) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.capacity = capacity;
        setLabel(provideLabel(capacity));
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    double getCurrent() {

        return capacity * (getVoltage() - getPreviousVoltage()) / Circuit.getCircuit().getDt();
    }


}

class Inductor extends Element {
    private final double inductance;
    private double previousCurrent;

    public Inductor(String name, Node positiveNode, Node negativeNode, double inductance) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.inductance = inductance;
        previousCurrent = 0;
        setLabel(provideLabel(inductance));
    }

    @Override
    double getCurrent() {
        return (Circuit.getCircuit().getDt() * getVoltage()) / inductance + previousCurrent;
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void updateTime() {
        super.updateTime();
        previousCurrent = getCurrent();
    }

}

class Diode extends Element {
    boolean isOn = true;
    int stackOverFlowed;

    Diode(String name, Node positiveNode, Node negativeNode) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        stackOverFlowed = 0;
    }


    @Override
    double getCurrent() {
        if (!isOn) {
            return 0;
        }
        double current = 0;
        boolean positiveIsGood = true;
        if (stackOverFlowed != 1) {
            try {
                for (int i = 0; i < positiveNode.getPositives().size(); i++) {
                    if (!positiveNode.getPositives().get(i).equals(this.name)) {
                        if (Circuit.getCircuit().getElements().get(positiveNode.getPositives().get(i)).isVoltageSource()) {
                            positiveIsGood = false;
                            break;
                        }
                        current -= Circuit.getCircuit().getElements().get(positiveNode.getPositives().get(i)).getCurrent();
                    }
                }
                if (positiveIsGood) {
                    for (int i = 0; i < positiveNode.getNegatives().size(); i++) {
                        current += Circuit.getCircuit().getElements().get(positiveNode.getNegatives().get(i)).getCurrent();
                    }
                } else {
                    current = 0;
                    for (int i = 0; i < negativeNode.getPositives().size(); i++) {
                        current += Circuit.getCircuit().getElements().get(negativeNode.getPositives().get(i)).getCurrent();
                    }
                    for (int i = 0; i < negativeNode.getNegatives().size(); i++) {
                        if (!negativeNode.getNegatives().get(i).equals(this.name)) {
                            current -= Circuit.getCircuit().getElements().get(negativeNode.getNegatives().get(i)).getCurrent();
                        }
                    }
                }
                if (current > 0)
                    return current;
                else {
                    isOn = false;
                    return 0;
                }
            } catch (StackOverflowError e) {
                stackOverFlowed = 1;
                return 0;
            }
        } else return 0;
    }

    @Override
    void setLabel(String label) {
    }

    @Override
    public double getVoltage() {
        if (isOn) {
            return 0;
        } else {
            if (positiveNode.getVoltage() - negativeNode.getVoltage() > 0) {
                isOn = true;
                return 0;
            }
            return positiveNode.getVoltage() - negativeNode.getVoltage();
        }
    }

}

class IndependentVoltageSource extends Element {
    final double offset;
    final double amplitude;
    final double frequency;
    final double phase;
    int stackOverFlowed;


    IndependentVoltageSource(String name, Node positive, Node negative, double offset, double amplitude, double frequency, double phase) {
        this.name = name;
        positiveNode = positive;
        negativeNode = negative;
        this.offset = offset;
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phase = phase;
        stackOverFlowed = 0;
        if (amplitude == 0)
            setLabel(provideLabel(offset));
        else
            setLabel(provideLabel(offset) + "+" + provideLabel(amplitude) + "sin(2PI" + provideLabel(frequency) + "t+" + provideLabel(phase) + ")");
    }

    @Override
    public void setParallelToVoltageSource(boolean b) {
        if (stackOverFlowed == 0) {
            if (b)
                stackOverFlowed = 1;
            else stackOverFlowed = -1;
        }
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    double getCurrent() {
        double current = 0;
        boolean positiveIsGood = true;
        if (stackOverFlowed != 1) {
            try {

                for (int i = 0; i < positiveNode.getPositives().size(); i++) {
                    if (!positiveNode.getPositives().get(i).equals(this.name)) {
                        if (Circuit.getCircuit().getElements().get(positiveNode.getPositives().get(i)).isVoltageSource()) {
                            positiveIsGood = false;
                            break;
                        }
                        current -= Circuit.getCircuit().getElements().get(positiveNode.getPositives().get(i)).getCurrent();
                    }
                }
                if (positiveIsGood) {
                    for (int i = 0; i < positiveNode.getNegatives().size(); i++) {
                        current += Circuit.getCircuit().getElements().get(positiveNode.getNegatives().get(i)).getCurrent();
                    }
                } else {
                    current = 0;
                    for (int i = 0; i < negativeNode.getPositives().size(); i++) {
                        current += Circuit.getCircuit().getElements().get(negativeNode.getPositives().get(i)).getCurrent();
                    }
                    for (int i = 0; i < negativeNode.getNegatives().size(); i++) {
                        if (!negativeNode.getNegatives().get(i).equals(this.name)) {
                            current -= Circuit.getCircuit().getElements().get(negativeNode.getNegatives().get(i)).getCurrent();
                        }
                    }
                }
                return current;
            } catch (StackOverflowError e) {
                return 0;
            }
        } else return 0;
    }

    @Override
    public double getVoltage() {
        double theta = 2 * Math.PI * frequency * Circuit.getCircuit().getTime() + phase;
        return offset + amplitude * Math.sin(theta);
    }

}

class IndependentCurrentSource extends Element {
    final double offset, amplitude, frequency, phase;

    IndependentCurrentSource(String name, Node positive, Node negative, double offset, double amplitude, double frequency, double phase) {
        this.name = name;
        positiveNode = positive;
        negativeNode = negative;
        this.offset = offset;
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phase = phase;
        if (amplitude == 0)
            setLabel(provideLabel(offset));
        else
            setLabel(provideLabel(offset) + "+" + provideLabel(amplitude) + "sin(2PI" + provideLabel(frequency) + "t+" + provideLabel(phase) + ")");

    }

    double getCurrent() {
        return offset + amplitude * Math.sin(2 * Math.PI * frequency * Circuit.getCircuit().getTime() + phase);
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }
}

class VoltageDependentCurrentSource extends Element {
    final double gain;
    final Node positiveDependence;
    final Node negativeDependence;

    VoltageDependentCurrentSource(String name, Node positiveNode, Node negativeNode, Node positiveDependence, Node negativeDependence, double gain) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.positiveDependence = positiveDependence;
        this.negativeDependence = negativeDependence;
        this.gain = gain;
        setLabel(provideLabel(gain) + "(" + positiveDependence.getName() + "," + negativeDependence.getName() + ")");
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    double getCurrent() {
        return (positiveDependence.getVoltage() - negativeDependence.getVoltage()) * gain;
    }
}

class VoltageDependentVoltageSource extends Element {
    final double gain;
    final Node positiveDependence;
    final Node negativeDependence;
    int stackOverFlowed;

    VoltageDependentVoltageSource(String name, Node positiveNode, Node negativeNode, Node positiveDependence, Node negativeDependence, double gain) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.positiveDependence = positiveDependence;
        this.negativeDependence = negativeDependence;
        this.gain = gain;
        stackOverFlowed = 0;
        setLabel(provideLabel(gain) + "(" + positiveDependence.getName() + "," + negativeDependence.getName() + ")");
    }

    @Override
    public void setParallelToVoltageSource(boolean b) {
        if (stackOverFlowed == 0) {
            if (b)
                stackOverFlowed = 1;
            else stackOverFlowed = -1;
        }
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    double getCurrent() {
        double current = 0;
        boolean positiveIsGood = true;
        if (stackOverFlowed != 1) {
            try {
                for (int i = 0; i < positiveNode.getPositives().size(); i++) {
                    if (!positiveNode.getPositives().get(i).equals(this.name)) {
                        if (Circuit.getCircuit().getElements().get(positiveNode.getPositives().get(i)).isVoltageSource()) {
                            positiveIsGood = false;
                            break;
                        }
                        current -= Circuit.getCircuit().getElements().get(positiveNode.getPositives().get(i)).getCurrent();
                    }
                }
                if (positiveIsGood) {
                    for (int i = 0; i < positiveNode.getNegatives().size(); i++) {
                        current += Circuit.getCircuit().getElements().get(positiveNode.getNegatives().get(i)).getCurrent();
                    }
                } else {
                    current = 0;
                    for (int i = 0; i < negativeNode.getPositives().size(); i++) {
                        current += Circuit.getCircuit().getElements().get(negativeNode.getPositives().get(i)).getCurrent();
                    }
                    for (int i = 0; i < negativeNode.getNegatives().size(); i++) {
                        if (!negativeNode.getNegatives().get(i).equals(this.name)) {
                            current -= Circuit.getCircuit().getElements().get(negativeNode.getNegatives().get(i)).getCurrent();
                        }
                    }
                }
                return current;
            } catch (StackOverflowError e) {
                return 0;
            }
        } else return 0;
    }

    @Override
    public double getVoltage() {

        return (positiveDependence.getPreviousVoltage() - negativeDependence.getPreviousVoltage()) * gain;
    }
}

class CurrentDependentCurrentSource extends Element {
    final double gain;
    final String dependentElement;

    CurrentDependentCurrentSource(String name, Node positiveNode, Node negativeNode, String dependentElement, double gain) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.dependentElement = dependentElement;
        this.gain = gain;
        setLabel(provideLabel(gain) + "(" + dependentElement + ")");

    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    double getCurrent() {
        return Circuit.getCircuit().getElements().get(dependentElement).getCurrent() * gain;
    }
}

class CurrentDependentVoltageSource extends Element {

    final double gain;
    final String elementDependent;
    int stackOverFlowed;


    CurrentDependentVoltageSource(String name, Node positiveNode, Node negativeNode, String elementDependent, double gain) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.elementDependent = elementDependent;
        this.gain = gain;
        setLabel(provideLabel(gain) + "(" + elementDependent + ")");


    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void setParallelToVoltageSource(boolean b) {
        if (stackOverFlowed == 0) {
            if (b)
                stackOverFlowed = 1;
            else stackOverFlowed = -1;
        }
    }

    @Override
    double getCurrent() {
        double current = 0;
        boolean positiveIsGood = true;
        if (stackOverFlowed != 1) {
            try {
                for (int i = 0; i < positiveNode.getPositives().size(); i++) {
                    if (!positiveNode.getPositives().get(i).equals(this.name)) {
                        if (Circuit.getCircuit().getElements().get(positiveNode.getPositives().get(i)).isVoltageSource()) {
                            positiveIsGood = false;
                            break;
                        }
                        current -= Circuit.getCircuit().getElements().get(positiveNode.getPositives().get(i)).getCurrent();
                    }
                }
                if (positiveIsGood) {
                    for (int i = 0; i < positiveNode.getNegatives().size(); i++) {
                        current += Circuit.getCircuit().getElements().get(positiveNode.getNegatives().get(i)).getCurrent();
                    }
                } else {
                    current = 0;
                    for (int i = 0; i < negativeNode.getPositives().size(); i++) {
                        current += Circuit.getCircuit().getElements().get(negativeNode.getPositives().get(i)).getCurrent();
                    }
                    for (int i = 0; i < negativeNode.getNegatives().size(); i++) {
                        if (!negativeNode.getNegatives().get(i).equals(this.name)) {
                            current -= Circuit.getCircuit().getElements().get(negativeNode.getNegatives().get(i)).getCurrent();
                        }
                    }
                }
                return current;
            } catch (StackOverflowError e) {
                return 0;
            }
        } else return 0;
    }

    @Override
    public double getVoltage() {
        return Circuit.getCircuit().getElements().get(elementDependent).getCurrent() * gain;
    }
}








