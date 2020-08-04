package engine.gfx.engine3d.normal;

import engine.maths.Vec3d;

import java.io.*;
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
        ArrayList<Vec3d> vertex = new ArrayList<>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(path)); //"resources/monkey.obj"
            String line = "";

            while ( line != null ) {
                String[] splitLine = line.split(" ");
                if ( splitLine[0].equalsIgnoreCase("v") ) {
                    float x = Float.parseFloat(splitLine[1]);
                    float y = Float.parseFloat(splitLine[2]);
                    float z = Float.parseFloat(splitLine[3]);
                    vertex.add(new Vec3d(x, y, z));
                }

                if ( splitLine[0].equalsIgnoreCase("f") ) {
                    int[] face = new int[3];
                    for ( int i = 1; i < splitLine.length; i++ ) {
                        String[] numVertex = splitLine[i].split("//");
                        face[i - 1] = Integer.parseInt(numVertex[0]);
                    }
                    tris.add(
                            new Triangle(
                                    new Vec3d[] {
                                            vertex.get(face[0] - 1),
                                            vertex.get(face[1] - 1),
                                            vertex.get(face[2] - 1)
                                    })
                    );
                }

                line = bf.readLine();
            }
            bf.close();
            return true;
        } catch ( FileNotFoundException e ) {
            System.out.println("No se encuentra el fichero.");
            return false;
        } catch ( IOException e ) {
            System.out.println("No se puede leer el fichero");
            return false;
        }
    }

    public void showInformation() {
        for ( int i = 0; i < tris.size() - 2; i++ ) {
            System.out.println(String.format("Triangulo %d", i));
            for ( int j = 0; j < tris.get(i).getP().length; j++ ) {
                System.out.println(
                        String.format("X: %f Y: %f Z: %f",
                                tris.get(i).getP()[j].getX(),
                                tris.get(i).getP()[j].getY(),
                                tris.get(i).getP()[j].getZ())
                );
            }
        }
    }

}
