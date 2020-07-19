package engine;

import engine.gfx.Renderer;
import java.awt.event.KeyEvent;

/**
 * La <class>GameContainer</class> es la clase que se encarga de gestionar
 * todos los programas. Principalmente contiene el código del bucle del programa.
 * Se encarga de crear la ventana <class>Window</class>, crear el controlador de
 * las entradas de teclado y ratón <class>Input</class> y el objeto que se encarga
 * de dibujar <class>Renderer</class>.
 *
 * @class: GameContainer.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public class GameContainer implements Runnable {

    private final double UPDATE_CAP = 1.0 / 60.0;

    private final String NAME_VERSION = "0.0.01 pre-alpha";

    private Window window;

    private Renderer renderer;

    private AbstractGame game;

    private Input input;

    private String title;

    private int width = 1080;

    private int height = 720;

    private float scale = 1.0f;

    private double frameTime = 0;

    private int frames = 0;

    private int fps;

    private boolean running = false;

    private boolean isCappedTo60fps = false;

    private boolean isShowingFpsInConsole = true;

    private boolean isShowingInformation = false;

    /**
     * Es el constructor de la clase.
     * @param game es el programa, aplicación o juego que se va a manejar/controlar.
     */
    public GameContainer(AbstractGame game) {
        this.game = game;
        title = game.getTitle();
    }

    /**
     * Este método muestra información por pantalla interesante
     * como los fps, o la posición del ratón en pantalla.
     */
    private void showInformation() {
        renderer.drawText("FPS:" + fps, 0, 0, 0xffffffff );
        renderer.drawText("Mouse X: " + getInput().getMouseX() + " Y: " + getInput().getMouseY(), 0, 25, 0xffffffff);
    }

    /**
     * Es el método el cual se llama para ejectuar el programa. En este método se
     * instancian los campos de:
     * - <class>Window</class>: la ventana del programa.
     * - <class>Renderer</class>: el renderizador, o la clase que tiene todos los métodos de dibujado.
     * - <class>input</class>: el controlador de las entradas del programa. Cliks, teclas, etc...
     * También es donde se llama al <method>initialize</method> de <class>AbstractGame</class>,
     * por lo cual también se instancian todos los objetos que tenga el programa, juego o
     * aplicación.
     * Por último, también se llama a <method>run</method> del campo <field>thread</field>.
     */
    public void start() {
        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);
        Thread thread = new Thread(this);
        game.initialize(this);
        running = true;
        thread.run();
    }

    /**
     * Es el método al cual se tiene que llamar si se quiere parar el programa.
     */
    public void stop() {
        running = false;
    }

    /**
     * Este método hereda de <interface>Runnable</interface>. Se ejecuta
     * en el hilo de ejecución de <class>GameContainer</class>. Aquí es donde
     * se encuentra el bucle del programa, donde se calculan los "frames" por
     * segundo y el tiempo transcurrido entre actualización y actualización.
     * Además, aquí es donde se llaman a los métodos <method>update</method> y
     * <method>render</method> de <class>AbstractGame</class>. Es la parte
     * más importante de esta clase.
     * @see Runnable
     */
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

    /**
     * Este método no se que hace.
     */
    private void dispose() {

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
        return title + " - GameContainer v." + NAME_VERSION + " - fps: " + fps;
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
