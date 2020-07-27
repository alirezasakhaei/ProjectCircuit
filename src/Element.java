import java.util.ArrayList;


public abstract class Element extends Circuit {
    Node positiveNode, negativeNode;
    String data = null;
    ArrayList<Double> currentsArray = new ArrayList<Double>();
    protected ArrayList<Double> voltagesArray = new ArrayList<Double>();
    protected ArrayList<Double> powersArray = new ArrayList<Double>();
    protected String name;
    private boolean isVoltageSource = false;
    private boolean isCurrentSource = false;

    public ArrayList<Double> getCurrentsArray() {
        return currentsArray;
    }

    public ArrayList<Double> getVoltagesArray() {
        return voltagesArray;
    }

    public ArrayList<Double> getPowersArray() {
        return powersArray;
    }

    public double getVoltage() {
        return positiveNode.getVoltage() - negativeNode.getVoltage();
    }

    public double getPreviousVoltage() {
        return positiveNode.getPreviousVoltage() - negativeNode.getPreviousVoltage();
    }

    public void updateTime() {
        currentsArray.add(getCurrent());
        voltagesArray.add(getVoltage());
        powersArray.add(getVoltage() * getCurrent());
    }

    abstract double getCurrent();

    public static boolean isParallel(Element elementOne, Element elementTwo) {
        if (elementOne.positiveNode.equals(elementTwo.positiveNode) && elementOne.negativeNode.equals(elementTwo.negativeNode))
            return true;
        if (elementOne.positiveNode.equals(elementTwo.negativeNode) && elementOne.negativeNode.equals(elementTwo.positiveNode))
            return true;
        return false;
    }

    public static boolean isSeries(Element elementOne, Element elementTwo) {
        if (isParallel(elementOne, elementTwo))
            return false;
        boolean isNeighbor = false;
        Node commonNode = null;
        if (elementOne.positiveNode.equals(elementTwo.positiveNode)) {
            isNeighbor = true;
            commonNode = elementOne.positiveNode;
        }
        if (elementOne.positiveNode.equals(elementTwo.negativeNode)) {
            isNeighbor = true;
            commonNode = elementOne.positiveNode;
        }
        if (elementOne.negativeNode.equals(elementTwo.positiveNode)) {
            isNeighbor = true;
            commonNode = elementOne.negativeNode;
        }
        if (elementOne.negativeNode.equals(elementTwo.negativeNode)) {
            isNeighbor = true;
            commonNode = elementOne.negativeNode;
        }
        if (!isNeighbor)
            return false;
        if (commonNode.getNegatives().size() + commonNode.getPositives().size() > 2) {
            return false;
        } else
            return true;
    }

    public static boolean isTheSameKind(Element elementOne, Element elementTwo) {
        if (elementOne.getClass().getName() == elementTwo.getClass().getName())
            return true;
        return false;
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

    @Override
    public String toString() {
        return "Element{" +
                "name='" + name + '\'' +
                ", positiveNode=" + positiveNode.getName() +
                ", negativeNode=" + negativeNode.getName() +
                ", current=" + getCurrent() +
                '}' + "\n";
    }
}
