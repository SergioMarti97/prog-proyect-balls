package engine;

import engine.gfx.Renderer;

/**
 * La clase AbstracGame es la clase de la que heredan todos los posibles programas que sean
 * manejados por <class>GameContainer</class>.
 *
 * @class: AbstractGame.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public abstract class AbstractGame {

    /**
     * El título del programa o aplicación.
     */
    private String title;

    /**
     * El constructor de la clase.
     * @param title El título que va a tener el programa en la barra de título de la ventana.
     */
    public AbstractGame(String title) {
        this.title = title;
    }

    /**
     * El método de inicialización del programa. Donde se instancian los objetos que se van
     * a ir utilizando, se asignan las variables globales, etc...
     * @param gc El objeto <class>GameContainer</class> que maneja programa <class>AbstractGame</class>.
     */
    public abstract void initialize(GameContainer gc);

    /**
     * El método de actualización del programa. En esta parte del código es donde se manejan
     * las entradas del usuario con la <class>Input</class> y se actualizan los objetos o las
     * variables globales.
     * @param gc El objeto <class>GameContainer</class> que maneja programa <class>AbstractGame</class>.
     * @param dt Es la variable del tiempo transcurrido, el equivalente a fElapsedTime.
     */
    public abstract void update(GameContainer gc, float dt);

    /**
     * El método de dibujado (renderizado). En esta parte del código es donde se dibujan
     * todos los gráficos que se mostraran los programas.
     * @param gc El objeto <class>GameContainer</class> que maneja programa <class>AbstractGame</class>.
     * @param r El objeto <class>Render</class> que contiene todas las funciones de dibujado.
     */
    public abstract void render(GameContainer gc, Renderer r);

    /**
     * El "geter" para el <field>title</field>
     */
    public String getTitle() {
        return title;
    }

}
