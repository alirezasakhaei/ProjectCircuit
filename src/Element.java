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
    public double getCurrentMax(){
        double max = 0;
        for (int i =0; i<currentsArray.size();i++){
            if (Math.abs(currentsArray.get(i)) >= max)
                max = Math.abs(currentsArray.get(i));
        }
        return max;
    }

    public double getPowerMax(){
        double max = 0;
        for (int i =0; i<powersArray.size();i++){
            if (Math.abs(powersArray.get(i)) >= max)
                max = Math.abs(powersArray.get(i));
        }
        return max;
    }


    public double getVoltageMax(){
        double max = 0;
        for (int i =0; i<voltagesArray.size();i++){
            if (Math.abs(voltagesArray.get(i)) >= max)
                max = Math.abs(voltagesArray.get(i));
        }
        return max;
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
