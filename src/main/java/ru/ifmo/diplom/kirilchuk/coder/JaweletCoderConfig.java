package ru.ifmo.diplom.kirilchuk.coder;

/**
 * This is just config for coder facade 
 */
public class JaweletCoderConfig {

    private int dispThresholdLL    = 4;
    private int dispThresholdOther = 100; //TODO: not used
    private int transformLevel     = 0;
    private int arithmeticCodeThreshold = 127;

    public int getDispThresholdLL() {
        return dispThresholdLL;
    }

    public JaweletCoderConfig setDispThresholdLL(int dispersionThreshold) {
        this.dispThresholdLL = dispersionThreshold;
        return this;
    }
    
    public int getDispThresholdOther() {
        return dispThresholdOther;
    }

    public JaweletCoderConfig setDispThresholdOther(int dispThresholdOther) {
        this.dispThresholdOther = dispThresholdOther;
        return this;
    }

    public int getTransformLevel() {
        return transformLevel;
    }

    public JaweletCoderConfig setTransformLevel(int transformLevel) {
        this.transformLevel = transformLevel;
        return this;
    }
    
    public int getArithmeticCodeThreshold() {
        return arithmeticCodeThreshold;
    }

    public JaweletCoderConfig setArithmeticCodeThreshold(int arithmeticCodeThreshold) {
        this.arithmeticCodeThreshold = arithmeticCodeThreshold;
        return this;
    }
    
}
