package main.model;

public class Statistics {
    private int nRotate, nScan, nFront;

    public void addRotate() {
        nRotate++;
    }

    public void addScan() {
        nScan++;
    }

    public void addFront() {
        nFront++;
    }

    public int getNRotate() {
        return nRotate;
    }

    public int getNScan() {
        return nScan;
    }

    public int getNFront() {
        return nFront;
    }
}
