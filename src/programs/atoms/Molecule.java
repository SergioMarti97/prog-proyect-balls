package programs.atoms;

import engine.gfx.Renderer;
import engine.gfx.shapes2d.Drawable;
import engine.gfx.shapes2d.SelectableByMouse;
import engine.gfx.shapes2d.points2d.Vec2DFloat;

import java.util.ArrayList;

public class Molecule implements Drawable, SelectableByMouse {

    private Vec2DFloat position = new Vec2DFloat(0.0f, 0.0f);

    private float rotation = 0.0f;

    private ArrayList<Atom> atoms;

    private boolean isShowingLinks = true;

    public Molecule() {
        atoms = new ArrayList<>();
        Atom atom = new Atom();
        addAtom(atom, position);
    }

    public Molecule(float x, float y) {
        this();
        position = new Vec2DFloat(x, y);
    }

    public float getRotation() {
        return rotation;
    }

    public Vec2DFloat getPosition() {
        return position;
    }

    public ArrayList<Atom> getAtoms() {
        return atoms;
    }

    public boolean isShowingLinks() {
        return isShowingLinks;
    }

    public Atom getAtom(int index) {
        return atoms.get(index);
    }

    private void addAtom(Atom atom, Vec2DFloat position) {
        atom.setPosition(position);
        atom.setRotation(rotation);
        atom.setId(atoms.size());
        atoms.add(atom);
    }

    public void addAtomTo(int idAtom, Atom newAtom, int position) {
        for ( Atom atom : atoms ) {
            if ( atom.getId() == idAtom ) {
                Vec2DFloat linkPosition = atom.getLinkPosition(position);
                newAtom.setPosition(linkPosition);
                newAtom.setRotation(rotation + 60 * atoms.size());
                atom.getLinkedAtomsIndexes().add(new IdAndPosition(newAtom.getId(), position));
            }
        }
        newAtom.setShowingLinks(isShowingLinks);
        newAtom.setId(atoms.size());
        atoms.add(newAtom);
    }

    private void resetAllAtomsPositions(Vec2DFloat position) { // todo esto es una cosa recursiva, hacerlo de forma iterativa es un dolor de muelas
        for ( Atom atom : atoms ) {
            atom.setPosition(position);
            for ( Atom atom1 : atoms ) {
                for ( IdAndPosition linkedAtomIndex : atom.getLinkedAtomsIndexes() ) {
                    if ( atom1.getId() == linkedAtomIndex.getId() ) {
                        Vec2DFloat linkPosition = atom.getLinkPosition(linkedAtomIndex.getPosition());
                        atom1.setPosition(linkPosition);
                    }
                }
            }
        }
    }

    private void resetAllAtomsPositions(float rotation) { // todo esto es una cosa recursiva, hacerlo de forma iterativa es un dolor de muelas
        atoms.get(0).setRotation(rotation);
        for ( Atom atom : atoms ) {
            for ( Atom atom1 : atoms ) {
                for ( IdAndPosition linkedAtomIndex : atom.getLinkedAtomsIndexes() ) {
                    if ( atom1.getId() == linkedAtomIndex.getId() ) {
                        Vec2DFloat linkPosition = atom.getLinkPosition(linkedAtomIndex.getPosition());
                        atom1.setPosition(linkPosition);
                    }
                }
            }
        }
    }

    public void setShowingLinks(boolean showingLinks) {
        isShowingLinks = showingLinks;
        for ( Atom atom : atoms ) {
            atom.setShowingLinks(showingLinks);
        }
    }

    public void setPosition(Vec2DFloat position) {
        this.position = position;
        resetAllAtomsPositions(this.position);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        resetAllAtomsPositions(rotation);
    }

    @Override
    public void drawYourSelf(Renderer r) {
        for ( Atom atom : atoms ) {
            atom.drawYourSelf(r);
        }
    }

    @Override
    public boolean isPointInside(float x, float y) {
        for ( Atom atom : atoms ) {
            if ( atom.isPointInside(x, y) ) {
                return true;
            }
        }
        return false;
    }

    public IdAndPosition isPointInsideALink(float x, float y) {
        int position;
        for ( Atom atom : atoms ) {
            position = atom.isPointInsideALink(x, y);
            if ( position > 0 ) {
                return new IdAndPosition(atom.getId(), position);
            }
        }
        return null;
    }

}
