package engine;

import engine.gfx.Renderer;

public abstract class AbstractGame {

    private int screenWidth = 1080;

    private int screenHeight = 720;

    private float screenScale = 1.0f;

    public AbstractGame() {
    }

    public AbstractGame(int screenWidth, int screenHeight, float screenScale) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.screenScale = screenScale;
    }

    public abstract void initialize(GameContainer gc);

    public abstract void update(GameContainer gc, float dt);

    public abstract void render(GameContainer gc, Renderer r);

    public float getScreenScale() {
        return screenScale;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

}
