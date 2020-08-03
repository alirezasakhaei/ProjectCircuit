import java.util.ArrayList;
import java.util.Map;

public class ErrorFinder {
    Circuit circuit;

    public ErrorFinder(Circuit circuit) {
        this.circuit = circuit;
    }

    public int findErrors() {
        if (!isDSet())
            return -1;
        //if (!isVoltageSourcesParallel())
        //    return -2;
        //if (!isCurrentSourceSeries())
        //    return -3;
        if (!isGroundAdded())
            return -4;
        if (!isLoopValid())
            return -5;

        return 0;
    }

    private boolean isGroundAdded() {
        if (!circuit.getNodes().containsKey(0))
            return false;
        return true;
    }

    private boolean isDSet() {
        if (circuit.getDi() == 0)
            return false;
        if (circuit.getDv() == 0)
            return false;
        if (circuit.getDt() == 0)
            return false;
        return true;
    }

    private boolean isLoopValid() {
        circuit.setAddedNodes(0);
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
                        if (Element.isSeries(elementOne, elementTwo)) {
                            if (elementOne.positiveNode.equals(elementTwo.negativeNode)) {
                                if (Math.abs(elementOne.getCurrent() - elementTwo.getCurrent()) > circuit.getDi()) {
                                    return false;
                                }
                            } else if (Math.abs(elementOne.getCurrent() + elementTwo.getCurrent()) > circuit.getDi()) {
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
                            if (Math.abs(elementOne.getVoltage() - elementTwo.getVoltage()) > circuit.getDv()) {
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
