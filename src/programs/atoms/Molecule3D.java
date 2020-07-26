package programs.atoms;

import engine.gfx.engine3d.normal.Vec3d;

import java.util.ArrayList;

public class Molecule3D {

    private ArrayList<Atom3D> atoms = new ArrayList<>();

    public Molecule3D() {
        atoms.add(new Atom3D(new Vec3d(0, 0, 0), CarbonHybridization.SP3));
    }

    public ArrayList<Atom3D> getAtoms() {
        return atoms;
    }

}
