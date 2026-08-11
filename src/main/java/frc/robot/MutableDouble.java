package frc.robot;

public class MutableDouble {
    private double value;

    public MutableDouble(double val) {
        value = val;
    }

    public double get() {
        return value;
    }

    public void set(double newVal) {
        value = newVal;
    }
}
