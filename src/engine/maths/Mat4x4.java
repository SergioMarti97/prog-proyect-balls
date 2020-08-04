package engine.maths;

public class Mat4x4 {

    private float[][] m;

    public Mat4x4() {
        m = new float[4][4];
    }

    public Mat4x4(float[][] m) {
        this.m = m;
    }

    public float[][] getM() {
        return m;
    }

    public void setM(float[][] m) {
        this.m = m;
    }

}
