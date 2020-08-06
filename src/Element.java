import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Element extends Circuit {
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
