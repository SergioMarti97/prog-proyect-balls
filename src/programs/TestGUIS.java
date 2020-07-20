package programs;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.gui.GUIWindow;

public class TestGUIS extends AbstractGame {

    private GUIWindow window;

    private TestGUIS(String title) {
        super(title);
    }

    @Override
    public void initialize(GameContainer gc) {
        window = new GUIWindow(gc.getInput(), (gc.getWidth() / 2), (gc.getHeight() / 2), 500, 250);
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if ( gc.getInput().isButtonDown(1) ) {
            if ( window.isPointInside(gc.getInput().getMouseX(), gc.getInput().getMouseY()) ) {
                window.setSelected(!window.isSelected());
            }
        }
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        if ( window.isSelected() ) {
            int mouseX = gc.getInput().getMouseX();
            int mouseY = gc.getInput().getMouseY();
            window.setPosX(mouseX);
            window.setPosY(mouseY);
        }
        window.drawYourSelf(r);
        r.drawCircle(gc.getInput().getMouseX(), gc.getInput().getMouseY(), 2, 0xffffffff);
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new TestGUIS("Test Graphical User Interface"));
        gc.setShowingInformation(true);
        gc.setCappedTo60fps(true);
        gc.start();
    }

}
