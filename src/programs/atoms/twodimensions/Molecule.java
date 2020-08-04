package programs.atoms.twodimensions;

import engine.gfx.Renderer;
import engine.gfx.Drawable;
import engine.maths.points2d.Vec2DGeneralFloat;
import programs.atoms.IdAndPos;

import java.util.ArrayList;

public class Molecule implements Drawable {

    private Vec2DGeneralFloat position = new Vec2DGeneralFloat(0.0f, 0.0f);

    private float rotation = 0.0f;

    private ArrayList<Atom> atoms;

    private boolean isShowingLinks = true;

    private boolean isSelected = false;

    public Molecule() {
        atoms = new ArrayList<>();
        addAtom(new Atom(), position);
    }

    public Molecule(float x, float y) {
        this();
        position = new Vec2DGeneralFloat(x, y);
    }

    public float getRotation() {
        return rotation;
    }

    public Vec2DGeneralFloat getPosition() {
        return position;
    }

    public ArrayList<Atom> getAtoms() {
        return atoms;
    }

    public boolean isShowingLinks() {
        return isShowingLinks;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Atom getAtom(int index) {
        return atoms.get(index);
    }

    private void addAtom(Atom atom, Vec2DGeneralFloat position) {
        atom.setPosition(position);
        atom.setRotation(rotation);
        atom.setId(atoms.size());
        atoms.add(atom);
    }

    public void addAtomTo(int idAtom, Atom newAtom, int position) {
        for ( Atom atom : atoms ) {
            if ( atom.getId() == idAtom ) {
                Vec2DGeneralFloat linkPosition = atom.getLinkPosition(position);
                newAtom.setPosition(linkPosition);
                newAtom.setRotation((rotation + 60 + atom.getRotation()) % 360);
                atom.getLinkedAtomsIndexes().add(new IdAndPos(newAtom.getId(), position));
            }
        }
        newAtom.setShowingLinks(isShowingLinks);
        newAtom.setId(atoms.size());
        atoms.add(newAtom);
    }

    public void deleteAtom(Vec2DGeneralFloat position) {
        for ( int i = atoms.size() - 1; i >= 0; i-- ) {
            if ( atoms.get(i).getPosition().equals(position) ) {
                atoms.remove(i);
                break;
            }
        }
    }

    public void setShowingLinks(boolean showingLinks) {
        isShowingLinks = showingLinks;
        for ( Atom atom : atoms ) {
            atom.setShowingLinks(showingLinks);
        }
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setPosition(Vec2DGeneralFloat position) {
        float disX = position.getX() - this.position.getX();
        float disY = position.getY() - this.position.getY();
        for ( Atom atom : atoms ) {
            float atomPositionX = atom.getPosition().getX();
            float atomPositionY = atom.getPosition().getY();
            atom.setPosition(new Vec2DGeneralFloat(atomPositionX + disX, atomPositionY + disY));
        }
        this.position = position;
    }

    public void setRotation(float rotation) {

        Vec2DGeneralFloat mediumPosition = new Vec2DGeneralFloat();
        for ( Atom atom : atoms ) {
            mediumPosition.addToX(atom.getPosition().getX());
            mediumPosition.addToY(atom.getPosition().getY());
        }
        float x = mediumPosition.getX() / atoms.size();
        float y = mediumPosition.getY() / atoms.size();
        mediumPosition.setX(x);
        mediumPosition.setY(y);

        for ( Atom atom : atoms ) {
            float diffX = atom.getPosition().getX() - mediumPosition.getX();
            float diffY = atom.getPosition().getY() - mediumPosition.getY();
            Vec2DGeneralFloat difference = new Vec2DGeneralFloat(diffX, diffY);
        }

        this.rotation = rotation;
    }

    @Override
    public void drawYourSelf(Renderer r) {
        for ( Atom atom : atoms ) {
            atom.drawYourSelf(r);
        }
    }


    public Atom isPointInside(float x, float y) {
        for ( Atom atom : atoms ) {
            if ( atom.isPointInside(x, y) ) {
                return atom;
            }
        }
        return null;
    }

    public Atom isPointInside(Vec2DGeneralFloat position) {
        return isPointInside(position.getX(), position.getY());
    }

    public IdAndPos isPointInsideALink(float x, float y) {
        int position;
        for ( Atom atom : atoms ) {
            position = atom.isPointInsideALink(x, y);
            if ( position > 0 ) {
                return new IdAndPos(atom.getId(), position);
            }
        }
        return null;
    }

    public IdAndPos isPointInsideALink(Vec2DGeneralFloat position) {
        return isPointInsideALink(position.getX(), position.getY());
    }

}
