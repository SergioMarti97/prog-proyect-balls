package programs;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;

/**
 * Esta es un ejemplo de una aplicación creada con este mótor.
 *
 * @class: AbstractGame.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-20
 */
public class Example extends AbstractGame {

    /**
     * El constructor de la clase.
     *
     * @param title El título que va a tener el programa en la barra de título de la ventana.
     */
    public Example(String title) {
        super(title);
    }


    /**
     * Calcula un valor Hexadecimal correspondiente a un color
     * dentro de un rango expecificado.
     *
     * @param max Color máximo.
     * @param min Color mínimo.
     * @return Devuelve un código de color hexadecimal.
     */
    private int getRandomColorHexBetweenRange(int max, int min) {
        return (int) ((Math.random() * ((max - min) + 1)) + min);
    }

    /**
     * El método de inicialización.
     * @param gc El objeto <class>GameContainer</class> que maneja programa <class>AbstractGame</class>.
     */
    @Override
    public void initialize(GameContainer gc) {

    }

    /**
     * El método de actualización.
     * @param gc El objeto <class>GameContainer</class> que maneja programa <class>AbstractGame</class>.
     * @param dt Es la variable del tiempo transcurrido, el equivalente a fElapsedTime.
     */
    @Override
    public void update(GameContainer gc, float dt) {

    }

    /**
     * El método de dibujado.
     * @param gc El objeto <class>GameContainer</class> que maneja programa <class>AbstractGame</class>.
     * @param r  El objeto <class>Render</class> que contiene todas las funciones de dibujado.
     */
    @Override
    public void render(GameContainer gc, Renderer r) {
        for ( int x = 0; x < gc.getWidth(); x++ ) {
            for ( int y = 0; y < gc.getHeight(); y++ ) {
                r.setPixel(x, y, getRandomColorHexBetweenRange(0xffffffff, 0xff000000));
            }
        }
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new Example("Example"));
        gc.setWidth(540);
        gc.setHeight(360);
        gc.setScale(2.0f);
        gc.start();
    }

}
