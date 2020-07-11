package engine;

import engine.gfx.Renderer;

public abstract class AbstractGame {

    private int screenWidth = 1080;

    private int screenHeight = 720;

    private float screenScale = 1.0f;

    private String title = "default";

    public AbstractGame() {

    }

    public AbstractGame(String title) {
        this.title = title;
    }

    public AbstractGame(String title, int screenWidth, int screenHeight, float screenScale) {
        this.title = title;
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

    public String getTitle() {
        return title;
    }

}
