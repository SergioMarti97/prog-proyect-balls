package engine;

import engine.gfx.Renderer;

import java.awt.event.KeyEvent;

public class GameContainer implements Runnable {

    private final double UPDATE_CAP = 1.0 / 60.0;

    private Window window;

    private Renderer renderer;

    private AbstractGame game;

    private Input input;

    private String title;

    private int width;

    private int height;

    private float scale;

    private double frameTime = 0;

    private int frames = 0;

    private int fps;

    private boolean running = false;

    private boolean isCappedTo60fps = false;

    private boolean isShowingFpsInConsole = true;

    private boolean isShowingInformation = false;

    public GameContainer(AbstractGame game) {
        this.game = game;
        width = game.getScreenWidth();
        height = game.getScreenHeight();
        scale = game.getScreenScale();
        title = game.getTitle();
    }

    private void showInformation() {
        renderer.drawText("FPS:" + fps, 0, 0, 0xffffffff );
        renderer.drawText("Mouse X: " + getInput().getMouseX() + " Y: " + getInput().getMouseY(), 0, 25, 0xffffffff);
    }

    public void start() {
        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);
        Thread thread = new Thread(this);
        game.initialize(this);
        running = true;
        thread.run();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        boolean render;

        double firstTime;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime;
        double unprocessedTime = 0;

        while ( running ) {
            render = !isCappedTo60fps; // render = isCappedTo60fps ? false : true

            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            while ( unprocessedTime >= UPDATE_CAP ) {
                unprocessedTime -= UPDATE_CAP;
                render = true;

                game.update(this, (float)UPDATE_CAP);
                input.update();

                if ( input.isKeyUp(KeyEvent.VK_CONTROL) ) {
                    isShowingInformation = !isShowingInformation;
                }

                if ( frameTime >= 1.0 ) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                    if ( isShowingFpsInConsole ) {
                        System.out.println("FPS: " + fps);
                    }
                }
            }

            if ( render ) {
                renderer.clear();
                game.render(this, this.renderer);
                renderer.process();
                if ( isShowingInformation ) {
                    showInformation();
                }
                window.update();
                frames++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        dispose();
    }

    public void dispose() {

    }

    public Window getWindow() {
        return window;
    }

    public Input getInput() {
        return input;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getScale() {
        return scale;
    }

    public String getTitle() {
        return title + " FPS: " + fps;
    }

    public double getUPDATE_CAP() {
        return UPDATE_CAP;
    }

    public boolean isShowingInformation() {
        return isShowingInformation;
    }

    public boolean isCappedTo60fps() {
        return isCappedTo60fps;
    }

    public boolean isShowingFpsInConsole() {
        return isShowingFpsInConsole;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCappedTo60fps(boolean cappedTo60fps) {
        isCappedTo60fps = cappedTo60fps;
    }

    public void setShowingInformation(boolean showingInformation) {
        isShowingInformation = showingInformation;
    }

}
