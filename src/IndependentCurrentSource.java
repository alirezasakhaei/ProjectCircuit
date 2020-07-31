public class IndependentCurrentSource extends Element {
    double offset,amplitude,frequency,phase;
    IndependentCurrentSource(String name,Node positive,Node negative,double offset,double amplitude,double frequency,double phase){
        this.name=name;
        positiveNode=positive;
        negativeNode=negative;
        this.offset=offset;
        this.amplitude=amplitude;
        this.frequency=frequency;
        this.phase=phase;
        data = String.valueOf(offset) + "," + String.valueOf(amplitude) + "," + String.valueOf(frequency) + "," + String.valueOf(phase);
        if (amplitude == 0)
            setLabel(Double.toString(offset));
        else
            setLabel(offset + "+" + amplitude + "sin(2PI" + frequency + "t+" + phase + ")");

    }
    double getCurrent() {
        return offset + amplitude * Math.sin(2 * Math.PI * frequency * Circuit.getCircuit().getTime() + phase);
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }
}
