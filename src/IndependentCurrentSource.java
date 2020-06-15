public class IndependentCurrentSource extends Element {
    double current,offset,amplitude,frequency,phase;
    IndependentCurrentSource(Node positive,Node negative,double current,double offset,double amplitude,double frequency,double phase){
        this.current=current;
        this.offset=offset;
        this.amplitude=amplitude;
        this.frequency=frequency;
        this.phase=phase;
    }
}
