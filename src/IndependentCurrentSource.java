public class IndependentCurrentSource extends Element {
    final double offset, amplitude, frequency, phase;

    IndependentCurrentSource(String name, Node positive, Node negative, double offset, double amplitude, double frequency, double phase) {
        this.name = name;
        positiveNode = positive;
        negativeNode = negative;
        this.offset = offset;
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phase = phase;
        if (amplitude == 0)
            setLabel(provideLabel(offset));
        else
            setLabel(provideLabel(offset) + "+" + provideLabel(amplitude) + "sin(2PI" + provideLabel(frequency) + "t+" + provideLabel(phase) + ")");

    }

    double getCurrent() {
        return offset + amplitude * Math.sin(2 * Math.PI * frequency * Circuit.getCircuit().getTime() + phase);
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }
}
