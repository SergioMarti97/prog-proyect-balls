package engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * La clase que controla la ventana. Se construye con la
 * información presente en <class>GameContainer</class>
 *
 * @class: Window.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public class Window {

    private JFrame frame;

    private BufferedImage image;

    private Canvas canvas;

    private BufferStrategy bs;

    private Graphics g;

    private GameContainer gc;

    /**
     * Este método es el que crea:
     * - la imágen <class>BufferedImage</class>.
     * - El frame <class>JFrame</class>. Es la própia clase del paquete <package>swing</package> de Java que
     *   crea la ventana.
     * - El canvas o lienzo <class>Canvas</class>.
     * - El bufferStrategy <class>BufferStrategy</class>.
     * - El objeto de gráficos <class>Graphics</class>.
     * @param gc El objeto de la clase <class>GameContainer</class>, que contiene toda la información de la aplicación.
     */
    public Window(GameContainer gc) {
        this.gc = gc;
        image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);

        canvas = new Canvas();
        Dimension s = new Dimension((int)(gc.getWidth() * gc.getScale()), (int)(gc.getHeight() * gc.getScale()));
        canvas.setPreferredSize(s);
        canvas.setMaximumSize(s);
        canvas.setMinimumSize(s);

        frame = new JFrame(gc.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        g = bs.getDrawGraphics();
    }

    /**
     * El método update se encarga de actualizar el título de la barra de título de la ventana
     * y de redibujar la ventana.
     */
    public void update() {
        frame.setTitle(gc.getTitle());
        g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        bs.show();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public BufferedImage getImage() {
        return image;
    }

    public JFrame getFrame() {
        return frame;
    }

}
