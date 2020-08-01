import javafx.stage.FileChooser;

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

        JPanel pText, pRun, pDraw, pLoad, pReset, pOut;
        JButton buttonRun, buttonDraw, buttonLoad, buttonReset;

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

        pDraw = new JPanel();
        pDraw.setBorder(border);
        pDraw.setLayout(new BorderLayout());
        pDraw.setBackground(Color.gray.darker());

        pLoad = new JPanel();
        pLoad.setBorder(border);
        pLoad.setLayout(new BorderLayout());
        pLoad.setBackground(Color.gray.darker());

        pReset = new JPanel();
        pReset.setBorder(border);
        pReset.setLayout(new BorderLayout());
        pReset.setBackground(Color.gray.darker().darker().darker().darker());

        buttonRun = new JButton("RUN");
        buttonRun.setBackground(Color.white);
        buttonRun.setBackground(Color.gray.darker().darker().darker().darker());
        buttonRun.setForeground(Color.WHITE);
        pRun.add(buttonRun);

        buttonRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!isSomethingLoaded) {
                    JFileChooser fileChooser = new JFileChooser("D:\\");
                    fileChooser.showOpenDialog(null);
                    File input = fileChooser.getSelectedFile();
                    if (Objects.nonNull(input))
                        run(input);
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
                        run(selectedFile);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(frame, "Exception Found!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        buttonLoad = new JButton("Load");
        buttonLoad.setBackground(Color.white);
        buttonLoad.setBackground(Color.gray.darker());
        buttonLoad.setForeground(Color.WHITE);

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

        buttonDraw = new JButton("Draw");
        buttonDraw.setBackground(Color.white);
        buttonDraw.setBackground(Color.gray.darker());
        buttonDraw.setForeground(Color.WHITE);

        pDraw.add(buttonDraw);

        buttonDraw.addActionListener(new ActionListener() {
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

        buttonReset = new JButton("Reset");
        buttonReset.setBackground(Color.white);
        buttonReset.setBackground(Color.gray.darker().darker().darker().darker());
        buttonReset.setForeground(Color.WHITE);
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
        scrollableTextArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollableTextArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        pOut.add(scrollableTextArea);


        frameLayout.setHorizontalGroup(frameLayout.createSequentialGroup().addGroup(frameLayout.createParallelGroup().addComponent(pText).addComponent(pOut))
                .addGroup(frameLayout.createParallelGroup().addComponent(pRun).addComponent(pDraw).addComponent(pLoad).addComponent(pReset)));
        frameLayout.setVerticalGroup(frameLayout.createParallelGroup().addGroup(frameLayout.createSequentialGroup().addComponent(pText).addComponent(pOut))
                .addGroup(frameLayout.createSequentialGroup().addComponent(pRun).addComponent(pDraw).addComponent(pLoad).addComponent(pReset)));
        frame.setVisible(true);

    }


    private void run(File input) {
        if (input.canExecute()) {
            InputManager inputManager = new InputManager(input);
            circuit = inputManager.analyzeTheInput();
            if (inputManager.getErrorLine() != -1) {
                JOptionPane.showMessageDialog(frame, "There is a problem found in line " + inputManager.getErrorLine(), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Circuit.setCircuit(circuit);
            ErrorFinder errorFinder = new ErrorFinder(circuit);
            int error = errorFinder.findErrors();
            if (error != 0) {
                JOptionPane.showMessageDialog(frame, "Error " + error + " is found!", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                circuit.initializeGraph();
                circuit.solveCircuit();
                drawCircuit();
                isCircuitSolved = true;
                textAreaOutput.setText(circuit.getOutput());
                try {
                    String path = input.getPath().substring(0, input.getPath().lastIndexOf("\\"));
                    File output;
                    output = new File(path + "\\output.txt");
                    JFileChooser fileChooser = new JFileChooser("D:\\");
                    fileChooser.setFileFilter(new FileNameExtensionFilter("txt file", "txt"));
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    fileChooser.setSelectedFile(output);
                    fileChooser.showSaveDialog(null);
                    output = fileChooser.getSelectedFile();
                    if (Objects.isNull(output)) {
                        output = new File(path + "\\output.txt");
                    }
                    if (!output.getName().trim().endsWith(".txt"))
                        output = new File(output.getAbsolutePath() + ".txt");
                    FileWriter fileWriter = new FileWriter(output);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(circuit.getOutput());
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "Can't write the output file!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }


            }
        } else
            JOptionPane.showMessageDialog(frame, "File Not Executable!", "ERROR", JOptionPane.ERROR_MESSAGE);

    }


    void dialog() {
        JDialog dialogElement = new JDialog();
        dialogElement.setBounds(0, 0, 500, 500);
        GroupLayout layout = new GroupLayout(dialogElement.getContentPane());
        dialogElement.setLayout(layout);
        Container container = dialogElement.getContentPane();
        container.setBackground(Color.gray.darker());

        int elementsNumber = circuit.getElements().size();
        String[] elements = new String[elementsNumber];
        int i = 0;
        for (Map.Entry element : circuit.getElements().entrySet()) {
            elements[i] = ((Element) element.getValue()).name;
            i++;
        }

        JComboBox comboBox = new JComboBox(elements);


        JTextField textField = new JTextField();


        JLabel labelTime = new JLabel("Draw until:");
        labelTime.setFont(new Font("Arial", Font.BOLD, 15));
        labelTime.setForeground(Color.white);

        JLabel labelChoose = new JLabel("Choose the element!");
        labelChoose.setFont(new Font("Arial", Font.BOLD, 20));
        labelChoose.setForeground(Color.white);

        JButton buttonChoose = new JButton("Select");
        buttonChoose.setFont(new Font("Arial", Font.BOLD, 15));
        buttonChoose.setBackground(Color.white);

        JButton buttonReset = new JButton("Reset");
        buttonReset.setFont(new Font("Arial", Font.BOLD, 15));
        buttonReset.setBackground(Color.white);

        JButton buttonDraw = new JButton("Draw");
        buttonDraw.setFont(new Font("Arial", Font.BOLD, 15));
        buttonDraw.setBackground(Color.white);


        int[] chosens = new int[1];
        chosens[0] = 0;
        Element[] chosenElements = new Element[2];
        chosenElements[0] = null;
        chosenElements[1] = null;

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


        buttonChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (chosens[0] == 2)
                    JOptionPane.showMessageDialog(dialogElement, "You can't select more than two elements");
                else if (chosens[0] == 1) {
                    if (chosenElements[0].name.equals((String) comboBox.getSelectedItem()))
                        JOptionPane.showMessageDialog(dialogElement, "This element is already selected!");
                    else {
                        chosens[0]++;
                        chosenElements[1] = circuit.getElements().get((String) comboBox.getSelectedItem());
                        JOptionPane.showMessageDialog(dialogElement, "element " + (String) comboBox.getSelectedItem() + " is selected!");
                    }
                } else if (chosens[0] == 0) {
                    chosens[0]++;
                    chosenElements[0] = circuit.getElements().get((String) comboBox.getSelectedItem());
                    JOptionPane.showMessageDialog(dialogElement, "element " + (String) comboBox.getSelectedItem() + " is selected!");
                }
            }
        });

        buttonDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (textField.getText().equals(null) || !InputManager.isNumberValid(textField.getText()))
                        Graph.setMaxTime(circuit.getMaximumTime());
                    else {
                        Graph.setMaxTime(Math.min(InputManager.unitCalculator(textField.getText()), circuit.getMaximumTime()));
                    }

                    if (chosens[0] == 0) {
                        JOptionPane.showMessageDialog(dialogElement, "No element Selected");
                    }
                    if (chosens[0] == 1) {
                        if (checkBoxVoltage.isSelected())
                            drawVoltage(chosenElements[0]);
                        if (checkBoxCurrent.isSelected())
                            drawCurrent(chosenElements[0]);
                        if (checkBoxPower.isSelected())
                            drawPower(chosenElements[0]);
                    }
                    if (chosens[0] == 2) {
                        if (checkBoxVoltage.isSelected())
                            drawVoltage(chosenElements[0], chosenElements[1]);
                        if (checkBoxCurrent.isSelected())
                            drawCurrent(chosenElements[0], chosenElements[1]);
                        if (checkBoxPower.isSelected())
                            drawPower(chosenElements[0], chosenElements[1]);
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(dialogElement, "Wrong input for time");
                }
            }
        });

        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chosens[0] = 0;
                chosenElements[0] = null;
                chosenElements[1] = null;
            }
        });


        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(labelChoose).addComponent(comboBox)
                .addGroup(layout.createSequentialGroup().addComponent(labelTime).addComponent(textField))
                .addGroup(layout.createSequentialGroup().addComponent(buttonChoose).addComponent(buttonReset).addComponent(buttonDraw))
                .addGroup(layout.createSequentialGroup().addComponent(checkBoxVoltage).addComponent(labelVoltage))
                .addGroup(layout.createSequentialGroup().addComponent(checkBoxCurrent).addComponent(labelCurrent))
                .addGroup(layout.createSequentialGroup().addComponent(checkBoxPower).addComponent(labelPower)));

        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(labelChoose).addComponent(comboBox)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(labelTime).addComponent(textField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(buttonChoose).addComponent(buttonReset).addComponent(buttonDraw))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(checkBoxVoltage).addComponent(labelVoltage))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(checkBoxCurrent).addComponent(labelCurrent))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(checkBoxPower).addComponent(labelPower))
        );


        dialogElement.setVisible(true);

    }

    private void dialogChooseElement() {
        JDialog dialogElement = new JDialog();
        dialogElement.setLayout(null);
        dialogElement.setBounds(0, 0, 500, 500);
        Container container = dialogElement.getContentPane();
        container.setBackground(Color.gray.darker());

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
        Element[] chosenElements = new Element[2];
        chosenElements[0] = null;
        chosenElements[1] = null;

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
                if (chosens[0] == 2)
                    JOptionPane.showMessageDialog(dialogElement, "You can't select more than two elements");
                else if (chosens[0] == 1) {
                    if (chosenElements[0].name.equals((String) comboBox.getSelectedItem()))
                        JOptionPane.showMessageDialog(dialogElement, "This element is already selected!");
                    else {
                        chosens[0]++;
                        chosenElements[1] = circuit.getElements().get((String) comboBox.getSelectedItem());
                        JOptionPane.showMessageDialog(dialogElement, "element " + (String) comboBox.getSelectedItem() + " is selected!");
                    }
                } else if (chosens[0] == 0) {
                    chosens[0]++;
                    chosenElements[0] = circuit.getElements().get((String) comboBox.getSelectedItem());
                    JOptionPane.showMessageDialog(dialogElement, "element " + (String) comboBox.getSelectedItem() + " is selected!");
                }
            }
        });

        buttonDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (textField.getText().equals(null) || !InputManager.isNumberValid(textField.getText()))
                        Graph.setMaxTime(circuit.getMaximumTime());
                    else {
                        Graph.setMaxTime(Math.min(InputManager.unitCalculator(textField.getText()), circuit.getMaximumTime()));
                    }

                    if (chosens[0] == 0) {
                        JOptionPane.showMessageDialog(dialogElement, "No element Selected");
                    }
                    if (chosens[0] == 1) {
                        if (checkBoxVoltage.isSelected())
                            drawVoltage(chosenElements[0]);
                        if (checkBoxCurrent.isSelected())
                            drawCurrent(chosenElements[0]);
                        if (checkBoxPower.isSelected())
                            drawPower(chosenElements[0]);
                    }
                    if (chosens[0] == 2) {
                        if (checkBoxVoltage.isSelected())
                            drawVoltage(chosenElements[0], chosenElements[1]);
                        if (checkBoxCurrent.isSelected())
                            drawCurrent(chosenElements[0], chosenElements[1]);
                        if (checkBoxPower.isSelected())
                            drawPower(chosenElements[0], chosenElements[1]);
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(dialogElement, "Wrong input for time");
                }
            }
        });

        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chosens[0] = 0;
                chosenElements[0] = null;
                chosenElements[1] = null;
            }
        });


        dialogElement.setVisible(true);
    }

    private void drawVoltage(Element element) {
        JDialog dialogVoltage = new JDialog();
        dialogVoltage.setBounds(0, 0, 600, 600);
        dialogVoltage.setLayout(null);

        JLabel labelTitle = new JLabel("Voltage " + element.name);
        labelTitle.setBounds(250, 20, 100, 30);
        dialogVoltage.add(labelTitle);

        JLabel labelTime = new JLabel("Time");
        labelTime.setBounds(500, 550, 50, 50);
        dialogVoltage.add(labelTime);

        JLabel labelMaxTime = new JLabel(Double.toString(Graph.getMaxTime()) + "s");
        labelMaxTime.setBounds(550, 300, 50, 50);
        dialogVoltage.add(labelMaxTime);

        JLabel labelMaxPositive = new JLabel(Double.toString(element.getVoltageMax()) + "V");
        labelMaxPositive.setBounds(10, 40, 50, 50);
        dialogVoltage.add(labelMaxPositive);

        JLabel labelMaxNegative = new JLabel("-" + Double.toString(element.getVoltageMax()) + "V");
        labelMaxNegative.setBounds(10, 520, 50, 50);
        dialogVoltage.add(labelMaxNegative);

        JLabel labelMaxPositiveHalf = new JLabel(Double.toString(element.getVoltageMax() / 2) + "V");
        labelMaxPositiveHalf.setBounds(10, 150, 50, 50);
        dialogVoltage.add(labelMaxPositiveHalf);

        JLabel labelMaxNegativeHalf = new JLabel("-" + Double.toString(element.getVoltageMax() / 2) + "V");
        labelMaxNegativeHalf.setBounds(10, 390, 50, 50);
        dialogVoltage.add(labelMaxNegativeHalf);


        Graph graphVoltage = new Graph(circuit.getDt(), element.getVoltageMax(), element.getVoltagesArray());
        graphVoltage.setBounds(50, 50, 500, 500);
        graphVoltage.setBackground(Color.gray);
        dialogVoltage.add(graphVoltage);


        dialogVoltage.setVisible(true);
    }

    private void drawVoltage(Element element, Element element1) {
        JDialog dialogVoltage = new JDialog();
        dialogVoltage.setBounds(0, 0, 600, 600);
        dialogVoltage.setLayout(null);

        JLabel labelTitle = new JLabel("Voltage " + element.name);
        labelTitle.setBounds(250, 20, 100, 30);
        dialogVoltage.add(labelTitle);

        JLabel labelTitle1 = new JLabel(element1.name);
        labelTitle1.setForeground(Color.RED);
        labelTitle1.setBounds(350, 20, 100, 30);
        dialogVoltage.add(labelTitle1);

        JLabel labelTime = new JLabel("Time");
        labelTime.setBounds(500, 550, 50, 50);
        dialogVoltage.add(labelTime);

        double maxAmount;
        maxAmount = Math.max(element.getVoltageMax(), element1.getVoltageMax());

        JLabel labelMaxTime = new JLabel(Double.toString(Graph.getMaxTime()) + "s");
        labelMaxTime.setBounds(550, 300, 50, 50);
        dialogVoltage.add(labelMaxTime);

        JLabel labelMaxPositive = new JLabel(Double.toString(maxAmount) + "V");
        labelMaxPositive.setBounds(10, 40, 50, 50);
        dialogVoltage.add(labelMaxPositive);

        JLabel labelMaxNegative = new JLabel("-" + Double.toString(maxAmount) + "V");
        labelMaxNegative.setBounds(10, 520, 50, 50);
        dialogVoltage.add(labelMaxNegative);

        JLabel labelMaxPositiveHalf = new JLabel(Double.toString(maxAmount / 2) + "V");
        labelMaxPositiveHalf.setBounds(10, 150, 50, 50);
        dialogVoltage.add(labelMaxPositiveHalf);

        JLabel labelMaxNegativeHalf = new JLabel("-" + Double.toString(maxAmount / 2) + "V");
        labelMaxNegativeHalf.setBounds(10, 390, 50, 50);
        dialogVoltage.add(labelMaxNegativeHalf);


        Graph graphVoltage = new Graph(circuit.getDt(), maxAmount, element.getVoltagesArray(), element1.getVoltagesArray());
        graphVoltage.setBounds(50, 50, 500, 500);
        graphVoltage.setBackground(Color.gray);
        dialogVoltage.add(graphVoltage);


        dialogVoltage.setVisible(true);
    }

    private void drawCurrent(Element element, Element element1) {
        JDialog dialogCurrent = new JDialog();
        dialogCurrent.setBounds(0, 0, 600, 600);
        dialogCurrent.setLayout(null);

        JLabel labelTitle = new JLabel("Currnet " + element.name);
        labelTitle.setBounds(250, 20, 100, 30);
        dialogCurrent.add(labelTitle);

        JLabel labelTitle1 = new JLabel(element1.name);
        labelTitle1.setForeground(Color.RED);
        labelTitle1.setBounds(350, 20, 100, 30);
        dialogCurrent.add(labelTitle1);

        JLabel labelTime = new JLabel("Time");
        labelTime.setBounds(500, 550, 50, 50);
        dialogCurrent.add(labelTime);

        double maxAmount;
        maxAmount = Math.max(element.getCurrentMax(), element1.getCurrentMax());

        JLabel labelMaxTime = new JLabel(Double.toString(Graph.getMaxTime()) + "s");
        labelMaxTime.setBounds(550, 300, 50, 50);
        dialogCurrent.add(labelMaxTime);

        JLabel labelMaxPositive = new JLabel(maxAmount + "A");
        labelMaxPositive.setBounds(10, 40, 50, 50);
        dialogCurrent.add(labelMaxPositive);

        JLabel labelMaxNegative = new JLabel("-" + maxAmount + "A");
        labelMaxNegative.setBounds(10, 520, 50, 50);
        dialogCurrent.add(labelMaxNegative);

        JLabel labelMaxPositiveHalf = new JLabel(Double.toString(maxAmount / 2) + "A");
        labelMaxPositiveHalf.setBounds(10, 150, 50, 50);
        dialogCurrent.add(labelMaxPositiveHalf);

        JLabel labelMaxNegativeHalf = new JLabel("-" + Double.toString(maxAmount / 2) + "A");
        labelMaxNegativeHalf.setBounds(10, 390, 50, 50);
        dialogCurrent.add(labelMaxNegativeHalf);

        Graph graphCurrent = new Graph(circuit.getDt(), maxAmount, element.getCurrentsArray(), element1.getCurrentsArray());
        graphCurrent.setBounds(50, 50, 500, 500);
        graphCurrent.setBackground(Color.gray);
        dialogCurrent.add(graphCurrent);


        dialogCurrent.setVisible(true);
    }

    private void drawCurrent(Element element) {
        JDialog dialogCurrent = new JDialog();
        dialogCurrent.setBounds(0, 0, 600, 600);
        dialogCurrent.setLayout(null);

        JLabel labelTitle = new JLabel("Currnet " + element.name);
        labelTitle.setBounds(250, 20, 100, 30);
        dialogCurrent.add(labelTitle);

        JLabel labelTime = new JLabel("Time");
        labelTime.setBounds(500, 550, 50, 50);
        dialogCurrent.add(labelTime);

        JLabel labelMaxTime = new JLabel(Double.toString(Graph.getMaxTime()) + "s");
        labelMaxTime.setBounds(550, 300, 50, 50);
        dialogCurrent.add(labelMaxTime);

        JLabel labelMaxPositive = new JLabel(Double.toString(element.getCurrentMax()) + "A");
        labelMaxPositive.setBounds(10, 40, 50, 50);
        dialogCurrent.add(labelMaxPositive);

        JLabel labelMaxNegative = new JLabel("-" + Double.toString(element.getCurrentMax()) + "A");
        labelMaxNegative.setBounds(10, 520, 50, 50);
        dialogCurrent.add(labelMaxNegative);

        JLabel labelMaxPositiveHalf = new JLabel(Double.toString(element.getCurrentMax() / 2) + "A");
        labelMaxPositiveHalf.setBounds(10, 150, 50, 50);
        dialogCurrent.add(labelMaxPositiveHalf);

        JLabel labelMaxNegativeHalf = new JLabel("-" + Double.toString(element.getCurrentMax() / 2) + "A");
        labelMaxNegativeHalf.setBounds(10, 390, 50, 50);
        dialogCurrent.add(labelMaxNegativeHalf);

        Graph graphCurrent = new Graph(circuit.getDt(), element.getCurrentMax(), element.getCurrentsArray());
        graphCurrent.setBounds(50, 50, 500, 500);
        graphCurrent.setBackground(Color.gray);
        dialogCurrent.add(graphCurrent);


        dialogCurrent.setVisible(true);
    }

    private void drawPower(Element element) {
        JDialog dialogPower = new JDialog();
        dialogPower.setBounds(0, 0, 600, 600);
        dialogPower.setLayout(null);

        JLabel labelTitle = new JLabel("Power " + element.name);
        labelTitle.setBounds(250, 20, 100, 30);
        dialogPower.add(labelTitle);

        JLabel labelTime = new JLabel("Time");
        labelTime.setBounds(500, 550, 50, 50);
        dialogPower.add(labelTime);

        JLabel labelMaxTime = new JLabel(Double.toString(Graph.getMaxTime()) + "s");
        labelMaxTime.setBounds(550, 300, 50, 50);
        dialogPower.add(labelMaxTime);

        JLabel labelMaxPositive = new JLabel(Double.toString(element.getPowerMax()) + "V");
        labelMaxPositive.setBounds(10, 40, 50, 50);
        dialogPower.add(labelMaxPositive);

        JLabel labelMaxNegative = new JLabel("-" + Double.toString(element.getPowerMax()) + "V");
        labelMaxNegative.setBounds(10, 520, 50, 50);
        dialogPower.add(labelMaxNegative);

        JLabel labelMaxPositiveHalf = new JLabel(Double.toString(element.getPowerMax() / 2) + "V");
        labelMaxPositiveHalf.setBounds(10, 150, 50, 50);
        dialogPower.add(labelMaxPositiveHalf);

        JLabel labelMaxNegativeHalf = new JLabel("-" + Double.toString(element.getPowerMax() / 2) + "V");
        labelMaxNegativeHalf.setBounds(10, 390, 50, 50);
        dialogPower.add(labelMaxNegativeHalf);

        Graph graphPower = new Graph(circuit.getDt(), element.getPowerMax(), element.getPowersArray());
        graphPower.setBounds(50, 50, 500, 500);
        graphPower.setBackground(Color.gray);
        dialogPower.add(graphPower);


        dialogPower.setVisible(true);
    }

    private void drawPower(Element element, Element element1) {
        JDialog dialogPower = new JDialog();
        dialogPower.setBounds(0, 0, 600, 600);
        dialogPower.setLayout(null);

        JLabel labelTitle = new JLabel("Power " + element.name);
        labelTitle.setBounds(250, 20, 100, 30);
        dialogPower.add(labelTitle);

        JLabel labelTitle1 = new JLabel(element1.name);
        labelTitle1.setForeground(Color.RED);
        labelTitle1.setBounds(350, 20, 100, 30);
        dialogPower.add(labelTitle1);

        JLabel labelTime = new JLabel("Time");
        labelTime.setBounds(500, 550, 50, 50);
        dialogPower.add(labelTime);

        JLabel labelMaxTime = new JLabel(Double.toString(Graph.getMaxTime()) + "s");
        labelMaxTime.setBounds(550, 300, 50, 50);
        dialogPower.add(labelMaxTime);

        double maxAmount;
        maxAmount = Math.max(element.getPowerMax(), element1.getPowerMax());

        JLabel labelMaxPositive = new JLabel(maxAmount + "V");
        labelMaxPositive.setBounds(10, 40, 50, 50);
        dialogPower.add(labelMaxPositive);

        JLabel labelMaxNegative = new JLabel("-" + maxAmount + "V");
        labelMaxNegative.setBounds(10, 520, 50, 50);
        dialogPower.add(labelMaxNegative);

        JLabel labelMaxPositiveHalf = new JLabel(Double.toString(maxAmount / 2) + "V");
        labelMaxPositiveHalf.setBounds(10, 150, 50, 50);
        dialogPower.add(labelMaxPositiveHalf);

        JLabel labelMaxNegativeHalf = new JLabel("-" + Double.toString(maxAmount / 2) + "V");
        labelMaxNegativeHalf.setBounds(10, 390, 50, 50);
        dialogPower.add(labelMaxNegativeHalf);

        Graph graphPower = new Graph(circuit.getDt(), maxAmount, element.getPowersArray(), element1.getPowersArray());
        graphPower.setBounds(50, 50, 500, 500);
        graphPower.setBackground(Color.gray);
        dialogPower.add(graphPower);


        dialogPower.setVisible(true);
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


}
