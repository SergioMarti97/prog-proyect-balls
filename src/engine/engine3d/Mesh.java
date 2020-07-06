package engine.engine3d;

import java.util.ArrayList;

public class Mesh {

    private ArrayList<Triangle> tris;

    public Mesh() {
        tris = new ArrayList<>();
    }

    public Mesh(ArrayList<Triangle> tris) {
        this.tris = tris;
    }

    public ArrayList<Triangle> getTris() {
        return tris;
    }

    public void setTris(ArrayList<Triangle> tris) {
        this.tris = tris;
    }

}
