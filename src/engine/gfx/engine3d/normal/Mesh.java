package engine.gfx.engine3d.normal;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

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
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();

            File file = new File(Objects.requireNonNull(classLoader.getResource(path)).getFile());
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);

            BufferedReader bf = new BufferedReader(new FileReader(file));
            String line = "";

            ArrayList<Vec3d> vertex = new ArrayList<>();

            do {

                if ( line.charAt(0) == 'v' ) {
                    Vec3d v = new Vec3d();
                    String[] lineSplit = line.split(" ");
                    v.setX(Float.parseFloat(lineSplit[1]));
                    v.setX(Float.parseFloat(lineSplit[2]));
                    v.setX(Float.parseFloat(lineSplit[3]));
                    vertex.add(v);
                }

                if ( line.charAt(0) == 'f' ) {
                    int[] f = new int[3];
                    String[] lineSplit = line.split(" ");
                    f[0] = Integer.parseInt(lineSplit[1]);
                    f[1] = Integer.parseInt(lineSplit[2]);
                    f[2] = Integer.parseInt(lineSplit[3]);
                    tris.add(new Triangle(new Vec3d[]{vertex.get(f[0] - 1), vertex.get(f[1] - 1), vertex.get(f[2] - 1)}));
                }

                line = bf.readLine();

            } while ( line != null );

            return true;

        } catch ( FileNotFoundException e ) {
            System.out.println("Archivo no encontrado.");
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return false;
    }

    private static File getFileFromResources(String fileName) {

        ClassLoader classLoader = Mesh.class.getClassLoader();

        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

    private static void printFile(File file) throws IOException {

        if (file == null) return;

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    public static void main(String[] args) {
        File file = getFileFromResources("/monkey.obj");
        try {
            printFile(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
