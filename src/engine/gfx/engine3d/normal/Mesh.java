package engine.gfx.engine3d.normal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public boolean loadFromObjectFile(String path) {
        // todo: comprobar que este algoritmo funciona bien para archivos de tipo .obj
        try {
            BufferedReader bf = new BufferedReader(new FileReader(String.valueOf(Mesh.class.getResourceAsStream(path)))); // String.valueOf(Mesh.class.getResourceAsStream(path))
            String line = "";
            ArrayList<Vec3d> vertex = new ArrayList<>();
            do {
                if ( line.charAt(0) == 'v' ) {
                    Vec3d v = new Vec3d();
                    String[] lineSplited = line.split(" ");
                    v.setX(Float.parseFloat(lineSplited[1]));
                    v.setX(Float.parseFloat(lineSplited[2]));
                    v.setX(Float.parseFloat(lineSplited[3]));
                    vertex.add(v);
                }
                if ( line.charAt(0) == 'f' ) {
                    int[] f = new int[3];
                    String[] lineSplited = line.split(" ");
                    f[0] = Integer.parseInt(lineSplited[1]);
                    f[1] = Integer.parseInt(lineSplited[2]);
                    f[2] = Integer.parseInt(lineSplited[3]);
                    tris.add(new Triangle(new Vec3d[]{vertex.get(f[0] - 1), vertex.get(f[1] - 1), vertex.get(f[2] - 1)}));
                }
                line = bf.readLine();
            } while ( line != null );
        } catch ( FileNotFoundException e ) {
            System.out.println("Archivo no encontrado.");
            return false;
        } catch ( IOException e ) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
