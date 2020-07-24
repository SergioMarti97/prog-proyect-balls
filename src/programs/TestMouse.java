package programs;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;

public class TestMouse extends AbstractGame {

    public TestMouse(String title) {
        super(title);
    }

    @Override
    public void initialize(GameContainer gc) {

    }

    @Override
    public void update(GameContainer gc, float dt) {

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawText(String.format("Wheel scroll: %d", gc.getInput().getScroll()), 10, 10, 0xffffffff);
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new TestMouse("Test Mouse Wheel"));
        gc.start();
    }

}
