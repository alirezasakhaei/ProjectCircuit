public class IndependentVoltageSource extends Element{
    double offset,amplitude,frequency,phase;
    IndependentVoltageSource(String name,Node positive,Node negative,double offset,double amplitude,double frequency,double phase){
        this.name=name;
        positiveNode=positive;
        negativeNode=negative;
        this.offset=offset;
        this.amplitude=amplitude;
        this.frequency=frequency;
        this.phase=phase;
        data = String.valueOf(offset) + "," + String.valueOf(amplitude) + "," + String.valueOf(frequency) + "," + String.valueOf(phase);
    }


    @Override
    double getCurrent() {
        current=0;
        for (int i = 0; i < positiveNode.getPositives().size(); i++) {
                if (!positiveNode.getPositives().get(i).equals(this.name))
                    current -= Circuit.getCircuit().getElements().get(positiveNode.getPositives().get(i)).getCurrent();

        }
        for (int i = 0; i < negativeNode.getPositives().size(); i++) {
            current+=Circuit.getCircuit().getElements().get(positiveNode.getNegatives().get(i)).getCurrent();
        }
        return current;
    }
}
