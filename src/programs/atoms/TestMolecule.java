package programs.atoms;

import engine.AbstractGame;
import engine.GameContainer;
import engine.audio.SoundClip;
import engine.gfx.Renderer;
import engine.gfx.images.Image;
import engine.gfx.images.ImageTile;
import engine.gfx.shapes2d.points2d.Vec2DFloat;

import java.awt.event.KeyEvent;

public class TestMolecule extends AbstractGame {

    private final int NUM_ATOMS = 6; // C H O N P S

    private SoundClip clip;

    private Molecule molecule;

    private int puttingAtom = 0;

    private Image[] images = new Image[NUM_ATOMS];

    private TestMolecule(String title) {
        super(title);
    }

    @Override
    public void initialize(GameContainer gc) {
        clip = new SoundClip("/audio/sound.wav");
        molecule = new Molecule();

        int tile_size = 64;
        images = new Image[NUM_ATOMS];
        ImageTile imageTile = new ImageTile("/atoms/atoms_tiles.png", tile_size, tile_size);
        for ( int i = 0; i < NUM_ATOMS; i++ ) {
            images[i] = new Image(tile_size, tile_size);
            images[i] = imageTile.getTileImage(i, 0);
        }

    }

    @Override
    public void update(GameContainer gc, float dt) {
        if ( gc.getInput().isKeyDown(KeyEvent.VK_C) ) {
            puttingAtom = 0;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_H) ) {
            puttingAtom = 1;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_O) ) {
            puttingAtom = 2;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_N) ) {
            puttingAtom = 3;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_P) ) {
            puttingAtom = 4;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_S) ) {
            puttingAtom = 5;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_L) ) {
            boolean isShowingLinks = molecule.isShowingLinks();
            molecule.setShowingLinks(!isShowingLinks);
        }

        Vec2DFloat mousePosition;
        if ( gc.getInput().isButtonDown(1) ) {
            mousePosition = new Vec2DFloat((float)(gc.getInput().getMouseX()), (float)(gc.getInput().getMouseY()));
            IdAndPos idAndPos = molecule.isPointInsideALink(mousePosition.getX(), mousePosition.getY());
            if ( molecule.isPointInside(mousePosition) != null ) {
                boolean moleculeIsSelected = molecule.isSelected();
                molecule.setSelected(!moleculeIsSelected);
            }
            if ( idAndPos != null ) {
                clip.play();
                Atom atomToAdd = new Atom();
                atomToAdd.setImage(images[puttingAtom]);
                float radius;
                switch ( puttingAtom ) {
                    case 0: case 2: case 3: default: // Carbon, Oxygen, Nitrogen
                        break;
                    case 1: // Hydrogen
                        radius = atomToAdd.getRadius();
                        atomToAdd.setRadius(radius/2.0f);
                        break;
                    case 4: case 5: // Phosphorus, Sulfur
                        radius = atomToAdd.getRadius();
                        atomToAdd.setRadius(radius * 1.5f);
                        break;
                }
                molecule.addAtomTo(idAndPos.getId(), atomToAdd, idAndPos.getPosition());
            }
        }
        if ( gc.getInput().isButtonDown(3) ) {
            mousePosition = new Vec2DFloat((float)(gc.getInput().getMouseX()), (float)(gc.getInput().getMouseY()));
            Atom atomToDelete = molecule.isPointInside(mousePosition);
            if ( atomToDelete != null ) {
                molecule.deleteAtom(atomToDelete.getPosition());
            }
        }

    }

    @Override
    public void render(GameContainer gc, Renderer r) {

        if ( molecule.isSelected() ) {
            molecule.setPosition(new Vec2DFloat((float)(gc.getInput().getMouseX()), (float)(gc.getInput().getMouseY())));
        }

        molecule.drawYourSelf(r);

        String nameAtom;
        switch ( puttingAtom ) {
            case 0: default:
                nameAtom = "Carbon";
                break;
            case 1:
                nameAtom = "Hydrogen";
                break;
            case 2:
                nameAtom = "Oxygen";
                break;
            case 3:
                nameAtom = "Nitrogen";
                break;
            case 4:
                nameAtom = "Phosphorus";
                break;
            case 5:
                nameAtom = "Sulfur";
                break;
        }

        r.drawText(String.format("Adding %s. Code: %d", nameAtom, puttingAtom), 10, 10, 0xffffffff);
        r.drawText("Press 'C' to add Carbon", 10, 40, 0xffffffff);
        r.drawText("Press 'H' to add Hydrogen", 10, 70, 0xffffffff);
        r.drawText("Press 'O' to add Oxygen", 10, 100, 0xffffffff);
        r.drawText("Press 'N' to add Nitrogen", 10, 130, 0xffffffff);
        r.drawText("Press 'P' to add Phosphor", 10, 160, 0xffffffff);
        r.drawText("Press 'S' to add Sulfur", 10, 190, 0xffffffff);
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new TestMolecule("Test atoms"));
        gc.setCappedTo60fps(false);
        gc.start();
    }

}
