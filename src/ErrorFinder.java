import java.util.Map;

public class ErrorFinder {
    Circuit circuit;
    public ErrorFinder(Circuit circuit) {
        this.circuit = circuit;
    }
    public int findErrors(){
        if (!isDSet())
            return -1;
        if (!isVoltageSourcesParallel())
            return -2;

        if (!isGroundAdded())
            return -4;

        if (!isLoopValid())
            return -5;












        return 0;
    }
    public boolean isGroundAdded(){
        if (!circuit.getNodes().containsKey(0))
            return false;
        return true;
    }
    public boolean isDSet(){
        if (circuit.getDi() == 0)
            return false;
        if (circuit.getDv() == 0)
            return false;
        if (circuit.getDt() == 0)
            return false;
        return true;
    }
    public boolean isLoopValid(){
        circuit.setAddedNodes(0);
        circuit.setAddedNodes(0);
        if (circuit.getNodeNameQueue().size() < circuit.getNodes().size()) {
            return false;
        }
        return true;
    }
    public boolean isCurrentSourceSeries(){

        return true;
    }
    public boolean isVoltageSourcesParallel(){
        int count;
        Element elementOne,elementTwo;
        for (Map.Entry voltageSource : circuit.getVoltageSources().entrySet()){
            elementOne = (Element) voltageSource.getValue();
            count = 0;
            for (Map.Entry voltageSourceTwo : circuit.getVoltageSources().entrySet()){
                elementTwo = (Element) voltageSourceTwo.getValue();
                if (elementOne.isParallel(elementTwo)){
                    if (Element.isTheSameKind(elementOne,elementTwo)){
                        if (elementOne.data == elementTwo.data)
                            count++;
                    }
                }
            }
            if (count > 1)
                return false;
        }
        return true;
    }

}
