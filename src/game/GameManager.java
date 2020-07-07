package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.engine3d.Mat4x4;
import engine.engine3d.Mesh;
import engine.engine3d.Triangle;
import engine.engine3d.Vec3d;
import engine.gfx.ImageTile;
import engine.gfx.Renderer;
import engine.audio.SoundClip;
import engine.gfx.Image;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameManager extends AbstractGame {

    private Image image;

    private Image image2;

    private SoundClip clip;

    private final static int SCREEN_WIDTH = 320; // 320, 1080

    private final static int SCREEN_HEIGHT = 240; // 240, 720

    private final static float SCREEN_SCALE = 4.0f;

    // Projection matrix
    private float fNear = 0.1f;
    private float fFar = 1000.0f;
    private float fFov = 90.0f;
    private float fAspectRatio = (float) SCREEN_HEIGHT / (float) SCREEN_WIDTH;
    private float fFovRad = (float) (1.0f / Math.tan(fFov * 0.5f / 180.0f * Math.PI));
    private Mat4x4 projectionMatrix, matRotZ, matRotX;
    private float theta = 0.0f;
    private Mesh mesh;

    public GameManager() {
        image = new Image("/test3.png");
        image2 = new ImageTile("/test2.png", 16, 16);
        image2.setAlpha(true);
        clip = new SoundClip("/audio/sound.wav");
    }

    private Vec3d MultiplyMatrixVector(Vec3d i, Mat4x4 m) {
        Vec3d o = new Vec3d();
        try {
            o.setX(i.getX() * m.getM()[0][0] + i.getY() * m.getM()[1][0] + i.getZ() * m.getM()[2][0] + m.getM()[3][0]);
            o.setY(i.getX() * m.getM()[0][1] + i.getY() * m.getM()[1][1] + i.getZ() * m.getM()[2][1] + m.getM()[3][1]);
            o.setZ(i.getX() * m.getM()[0][2] + i.getY() * m.getM()[1][2] + i.getZ() * m.getM()[2][2] + m.getM()[3][2]);
            float w = i.getX() * m.getM()[0][3] + i.getY() * m.getM()[1][3] + i.getZ() * m.getM()[2][3] + m.getM()[3][3];
            if (w != 0.0f) {
                o.setX(o.getX() / w);
                o.setY(o.getY() / w);
                o.setZ(o.getZ() / w);
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return o;
    }

    private Triangle transformTriangle(Triangle triangle, Mat4x4 m) {
        Triangle triangleTransformed = new Triangle();
        Vec3d point0 =  MultiplyMatrixVector(triangle.getP()[0], m);
        Vec3d point1 =  MultiplyMatrixVector(triangle.getP()[1], m);
        Vec3d point2 =  MultiplyMatrixVector(triangle.getP()[2], m);
        triangleTransformed.setP(new Vec3d[]{point0, point1, point2});
        return triangleTransformed;
    }

    @Override
    public void initialize(GameContainer gc) {
        mesh = new Mesh();
        ArrayList<Triangle> tris = new ArrayList<>();
        // SOUTH
        tris.add(new Triangle(new Vec3d[]{new Vec3d(0.0f, 0.0f, 0.0f), new Vec3d(0.0f, 1.0f, 0.0f), new Vec3d(1.0f, 1.0f, 0.0f)}));
        tris.add(new Triangle(new Vec3d[]{new Vec3d(0.0f, 0.0f, 0.0f), new Vec3d(1.0f, 1.0f, 0.0f), new Vec3d(1.0f, 0.0f, 0.0f)}));
        // EAST
        tris.add(new Triangle(new Vec3d[]{new Vec3d(1.0f, 0.0f, 0.0f), new Vec3d(1.0f, 1.0f, 0.0f), new Vec3d(1.0f, 1.0f, 1.0f)}));
        tris.add(new Triangle(new Vec3d[]{new Vec3d(1.0f, 0.0f, 0.0f), new Vec3d(1.0f, 1.0f, 1.0f), new Vec3d(1.0f, 0.0f, 1.0f)}));
        // NORTH
        tris.add(new Triangle(new Vec3d[]{new Vec3d(1.0f, 0.0f, 1.0f), new Vec3d(1.0f, 1.0f, 1.0f), new Vec3d(0.0f, 1.0f, 1.0f)}));
        tris.add(new Triangle(new Vec3d[]{new Vec3d(1.0f, 0.0f, 1.0f), new Vec3d(0.0f, 1.0f, 1.0f), new Vec3d(0.0f, 0.0f, 1.0f)}));
        // WEST
        tris.add(new Triangle(new Vec3d[]{new Vec3d(0.0f, 0.0f, 1.0f), new Vec3d(0.0f, 1.0f, 1.0f), new Vec3d(0.0f, 1.0f, 0.0f)}));
        tris.add(new Triangle(new Vec3d[]{new Vec3d(0.0f, 0.0f, 1.0f), new Vec3d(0.0f, 1.0f, 0.0f), new Vec3d(0.0f, 0.0f, 0.0f)}));
        // TOP
        tris.add(new Triangle(new Vec3d[]{new Vec3d(0.0f, 1.0f, 0.0f), new Vec3d(0.0f, 1.0f, 1.0f), new Vec3d(1.0f, 1.0f, 1.0f)}));
        tris.add(new Triangle(new Vec3d[]{new Vec3d(0.0f, 1.0f, 0.0f), new Vec3d(1.0f, 1.0f, 1.0f), new Vec3d(1.0f, 1.0f, 0.0f)}));
        // BOTTOM
        tris.add(new Triangle(new Vec3d[]{new Vec3d(1.0f, 0.0f, 1.0f), new Vec3d(0.0f, 0.0f, 1.0f), new Vec3d(0.0f, 0.0f, 0.0f)}));
        tris.add(new Triangle(new Vec3d[]{new Vec3d(1.0f, 0.0f, 1.0f), new Vec3d(0.0f, 0.0f, 0.0f), new Vec3d(1.0f, 0.0f, 0.0f)}));

        mesh.setTris(tris);

        // Projection Matrix
        projectionMatrix = new Mat4x4();
        projectionMatrix.getM()[0][0] = fAspectRatio * fFovRad;
        projectionMatrix.getM()[1][1] = fFovRad;
        projectionMatrix.getM()[2][2] = fFar / (fFar - fNear);
        projectionMatrix.getM()[3][2] = (-fFar * fNear) / (fFar - fNear);
        projectionMatrix.getM()[2][3] = 1.0f;
        projectionMatrix.getM()[3][3] = 0.0f;

        matRotZ = new Mat4x4();
        matRotX = new Mat4x4();
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if(gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            clip.play();
        }
        theta += dt;
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        //r.drawFillRect(gc.getInput().getMouseX() - 16, gc.getInput().getMouseY() - 16, 32, 32, 0xffffccff);
        r.drawLine(0, 0, gc.getInput().getMouseX(), gc.getInput().getMouseY(), 0xffE5B501);
        //r.drawFillCircle(gc.getInput().getMouseX(), gc.getInput().getMouseY(), 50, 0xffffffff);

        // Rotation Z
        matRotZ.getM()[0][0] = (float)(Math.cos(theta));
        matRotZ.getM()[0][1] = (float)(Math.sin(theta));
        matRotZ.getM()[1][0] = -(float)(Math.sin(theta));
        matRotZ.getM()[1][1] = (float)(Math.cos(theta));
        matRotZ.getM()[2][2] = 1.0f;
        matRotZ.getM()[3][3] = 1.0f;
        // Rotation X
        matRotX.getM()[0][0] = 1;
        matRotX.getM()[1][1] = (float)(Math.cos(theta * 0.5f));
        matRotX.getM()[1][2] = (float)(Math.sin(theta * 0.5f));
        matRotX.getM()[2][1] = -(float)(Math.sin(theta * 0.5f));
        matRotX.getM()[2][2] = (float)(Math.cos(theta * 0.5f));
        matRotX.getM()[3][3] = 1.0f;

        for (Triangle triangle : mesh.getTris()) {

            Triangle triangleRotatedX = transformTriangle(triangle, matRotX);
            Triangle triangleRotatedXZ = transformTriangle(triangleRotatedX, matRotZ);
            Triangle triangleTranslated = triangleRotatedXZ;
            triangleTranslated.getP()[0].addToZ(3.0);
            triangleTranslated.getP()[1].addToZ(3.0);
            triangleTranslated.getP()[2].addToZ(3.0);

            Triangle triangleProjected = transformTriangle(triangleTranslated, projectionMatrix);

            // Scale into view
            triangleProjected.getP()[0].addToX(1.0);
            triangleProjected.getP()[0].addToY(1.0);
            triangleProjected.getP()[1].addToX(1.0);
            triangleProjected.getP()[1].addToY(1.0);
            triangleProjected.getP()[2].addToX(1.0);
            triangleProjected.getP()[2].addToY(1.0);

            triangleProjected.getP()[0].multiplyXBy(0.5 * (float) SCREEN_WIDTH);
            triangleProjected.getP()[0].multiplyYBy(0.5 * (float) SCREEN_WIDTH);
            triangleProjected.getP()[1].multiplyXBy(0.5 * (float) SCREEN_WIDTH);
            triangleProjected.getP()[1].multiplyYBy(0.5 * (float) SCREEN_WIDTH);
            triangleProjected.getP()[2].multiplyXBy(0.5 * (float) SCREEN_WIDTH);
            triangleProjected.getP()[2].multiplyYBy(0.5 * (float) SCREEN_WIDTH);

            r.drawTriangle(
                    (int)(triangleProjected.getP()[0].getX()), (int)(triangleProjected.getP()[0].getY()),
                    (int)(triangleProjected.getP()[1].getX()), (int)(triangleProjected.getP()[1].getY()),
                    (int)(triangleProjected.getP()[2].getX()), (int)(triangleProjected.getP()[2].getY()),
                    0xff00ffff
            );
        }

        r.drawTriangle(40, 10, 100, 40, 50, 50, 0xffff5033);
        r.drawCircle(200, 90, 34, 0xff20ff99);
        r.drawRect(100, 110, 130, 90, 0xff5555ff);
        r.drawFillRect(130, 4, 12, 12, 0xff5510ff);
        r.drawText("Mouse X: " + gc.getInput().getMouseX() + " Y: " + gc.getInput().getMouseY(), 0, 10, 0xff00ffff);

        r.drawImage(image, 100, 100);
        //r.drawImageTile((ImageTile) image2, gc.getInput().getMouseX() - 8, gc.getInput().getMouseY() - 8, 1, 1);

    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new GameManager());
        gc.setWidth(SCREEN_WIDTH);
        gc.setHeight(SCREEN_HEIGHT);
        gc.setScale(SCREEN_SCALE);
        gc.start();
    }

}
