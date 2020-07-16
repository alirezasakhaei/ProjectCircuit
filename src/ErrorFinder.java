import java.util.Map;

public class ErrorFinder {
    Circuit circuit;

    public ErrorFinder(Circuit circuit) {
        this.circuit = circuit;
    }

    public int findErrors() {
        if (!isDSet())
            return -1;
        if (!isVoltageSourcesParallel())
            return -2;
        if (!isCurrentSourceSeries())
            return -3;
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
        circuit.setAddedNodes(0);
        if (circuit.getNodeNameQueue().size() < circuit.getNodes().size()) {
            return false;
        }
        return true;
    }

    private boolean isCurrentSourceSeries() {
        Element elementOne, elementTwo;
        for (Map.Entry currnetSource : circuit.getElements().entrySet()){
            elementOne = (Element) currnetSource.getValue();
            if (elementOne.isCurrentSource()){
                for (Map.Entry currentSourceTwo : circuit.getElements().entrySet()) {
                    elementTwo = (Element) currentSourceTwo.getValue();
                    if (elementTwo.isCurrentSource()) {
                        if (Element.isSeries(elementOne,elementTwo)) {
                            if (Element.isTheSameKind(elementOne, elementTwo)) {
                                if (elementOne.data != elementTwo.data) {
                                    return false;
                                }
                            } else
                                return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean isVoltageSourcesParallel() {
        Element elementOne, elementTwo;
        for (Map.Entry voltageSource : circuit.getElements().entrySet()) {
            elementOne = (Element) voltageSource.getValue();
            if (elementOne.isVoltageSource()) {
                for (Map.Entry voltageSourceTwo : circuit.getElements().entrySet()) {
                    elementTwo = (Element) voltageSourceTwo.getValue();
                    if (elementTwo.isVoltageSource()) {
                        if (Element.isParallel(elementOne,elementTwo)) {
                            if (Element.isTheSameKind(elementOne, elementTwo)) {
                                if (elementOne.data != elementTwo.data) {
                                    return false;
                                }
                            } else
                                return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
