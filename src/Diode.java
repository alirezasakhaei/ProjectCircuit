import java.util.ArrayList;

public class Diode extends Element {
    boolean isOn = true;
    int stackOverFlowed;
    double prePreviousVoltage;

    Diode(String name, Node positiveNode, Node negativeNode) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        stackOverFlowed = 0;
        data = null;
        prePreviousVoltage = 0;
    }

    @Override
    public void updateTime() {
        prePreviousVoltage = getPreviousVoltage();
        currentsArray.add(getCurrent());
        voltagesArray.add(getVoltage());
        powersArray.add(getVoltage() * getCurrent());
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
                return 0;
            }
        } else return 0;
    }

    //  @Override
    //  public double getVoltage(){
    //      if(isOn){
    //          return 0;
    //      }
    //      else {
    //          if(positiveNode.getVoltage()-negativeNode.getVoltage()>0) {
    //              isOn = true;
    //              return 0;
    //          }
    //          return positiveNode.getVoltage() - negativeNode.getVoltage();
    //      }
    //  }

    public void setVoltagesArray(ArrayList<Double> voltages) {
        voltagesArray = voltages;
    }

    public void setCurrentsArray(ArrayList<Double> currents) {
        currentsArray = currents;
    }

    public void setPowersArray(ArrayList<Double> powers) {
        powersArray = powers;
    }
}
