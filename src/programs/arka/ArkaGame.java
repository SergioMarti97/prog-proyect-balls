package programs.arka;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.maths.points2d.Vec2DGeneralFloat;
import engine.gfx.shapes2d.shapes.Polygon2D;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Juego de Arkame.
 *
 * @class: ArkaGame.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-29
 */
public class ArkaGame extends AbstractGame {

    private Polygon2D rect;

    /**
     * El constructor de la clase.
     *
     * @param title El título que va a tener el programa en la barra de título de la ventana.
     */
    private ArkaGame(String title) {
        super(title);
    }

    @Override
    public void initialize(GameContainer gc) {
        ArrayList<Vec2DGeneralFloat> points = new ArrayList<>();
        points.add(new Vec2DGeneralFloat(0.0f, 0.0f));
        points.add(new Vec2DGeneralFloat(20.0f, 0.0f));
        points.add(new Vec2DGeneralFloat(10.0f, 10.0f));
        points.add(new Vec2DGeneralFloat(0.0f, 10.0f));
        rect = new Polygon2D(100, 100, points,20, 0xffffffff);
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if ( gc.getInput().isKeyDown(KeyEvent.VK_LEFT) ) {
            float offsetX = rect.getPosX() - 10;
            rect.setPosX(offsetX);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_RIGHT) ) {
            float offsetX = rect.getPosX() + 10;
            rect.setPosX(offsetX);
        }
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        rect.drawYourSelf(r);
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new ArkaGame("Arka"));
        gc.start();
    }

}
