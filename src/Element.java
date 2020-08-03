import java.util.ArrayList;


public abstract class Element extends Circuit {
    Node positiveNode, negativeNode;
    String data = null;
    ArrayList<Double> currentsArray = new ArrayList<Double>();
    String label;
    void setLabel(String label){};
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
        return (elementOne.positiveNode.equals(elementTwo.positiveNode) && elementOne.negativeNode.equals(elementTwo.negativeNode))
                || (elementOne.positiveNode.equals(elementTwo.negativeNode) && elementOne.negativeNode.equals(elementTwo.positiveNode));
    }

    public static int isSeries(Element elementOne, Element elementTwo) {
        //positives are series : 1
        //elementOne positive and elementTwo negative are series : 2
        //elementOne negative and elementTwo positive are series : 3
        //negatives are series : 4
        if (elementOne != elementTwo) {
            int result = isSeriesss(elementOne, elementTwo, true);
            if (result == 1)
                return 1;
            else if (result == -1)
                return 2;
            else {
                result = isSeriesss(elementOne, elementTwo, false);
                if (result == 1)
                    return 3;
                else if (result == -1)
                    return 4;
            }
            return 0;
        } else return 0;
    }


    private static int isSeriesss(Element elementOne, Element elementTwo, boolean positive) {
        boolean isNeighbor = false, canContinue;
        int isPositiveOfDestination = 0;
        Node commonNode;
        if (positive)
            commonNode = elementOne.positiveNode;
        else commonNode = elementOne.negativeNode;
        if (elementOne.positiveNode.equals(elementTwo.positiveNode)) {
            isNeighbor = true;
            commonNode = elementOne.positiveNode;
            isPositiveOfDestination = 1;
        } else if (elementOne.positiveNode.equals(elementTwo.negativeNode)) {
            isNeighbor = true;
            commonNode = elementOne.positiveNode;
            isPositiveOfDestination = -1;
        } else if (elementOne.negativeNode.equals(elementTwo.positiveNode)) {
            isNeighbor = true;
            commonNode = elementOne.negativeNode;
            isPositiveOfDestination = 1;
        } else if (elementOne.negativeNode.equals(elementTwo.negativeNode)) {
            isNeighbor = true;
            commonNode = elementOne.negativeNode;
            isPositiveOfDestination = -1;
        }
        canContinue = commonNode.getNegatives().size() + commonNode.getPositives().size() == 2;
        if (canContinue)
            if (isNeighbor) {
                return isPositiveOfDestination;
            } else {
                if (positive) {
                    if (commonNode.getPositives().size() == 1)
                        return isSeriesss(Circuit.getCircuit().getElements().get(commonNode.getNegatives().get(0)), elementTwo, true);
                    else if (commonNode.getPositives().get(0).equals(elementOne.name))
                        return isSeriesss(Circuit.getCircuit().getElements().get(commonNode.getPositives().get(1)), elementTwo, false);
                    else
                        return isSeriesss(Circuit.getCircuit().getElements().get(commonNode.getPositives().get(0)), elementTwo, false);
                } else {
                    if (commonNode.getNegatives().size() == 1)
                        return isSeriesss(Circuit.getCircuit().getElements().get(commonNode.getPositives().get(0)), elementTwo, false);
                    else if (commonNode.getNegatives().get(0).equals(elementOne.name))
                        return isSeriesss(Circuit.getCircuit().getElements().get(commonNode.getNegatives().get(1)), elementTwo, true);
                    else
                        return isSeriesss(Circuit.getCircuit().getElements().get(commonNode.getNegatives().get(0)), elementTwo, true);
                }
            }
        else return 0;


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
