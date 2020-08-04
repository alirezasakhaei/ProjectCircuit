import java.util.ArrayList;
import java.util.Map;

public class ErrorFinder {
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
                        if (result == 2 || result == 3) {
                            if (Math.abs(current1 - current2) > circuit.getDi()) {
                                return false;
                            }
                        } else if (result == 1 || result == 4) {
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
