package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.audio.SoundClip;
import engine.engine3d.Vec2d;
import engine.gfx.Renderer;
import engine.physics.Circle;
import engine.physics.CollidingShapes;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BallsGame extends AbstractGame {

    private final static int SCREEN_WIDTH = 1080; // 320, 1080

    private final static int SCREEN_HEIGHT = 720; // 240, 720

    private final static float SCREEN_SCALE = 1.0f;

    private final int NUM_BALLS = 50;

    private ArrayList<Circle> balls;

    private ArrayList<CollidingShapes> collidingPairs;

    private SoundClip clip;

    private Vec2d mousePosition;

    private float friction;

    private BallsGame() {
        clip = new SoundClip("/audio/sound.wav");
    }

    private float getRandomFloatBetweenRange(int max, int min) {
        return (float) ((Math.random() * ((max - min) + 1)) + min);
    }

    private void fillBallsWithRandomBalls() {
        for ( int i = 0; i < NUM_BALLS; i++ ) {
            float radius = getRandomFloatBetweenRange(30, 10);
            float posX = getRandomFloatBetweenRange((int)(SCREEN_WIDTH - radius), (int)(radius));
            float posY = getRandomFloatBetweenRange((int)(SCREEN_HEIGHT - radius), (int)(radius));
            Circle circle = new Circle(posX, posY, radius, 0xffffffff);
            circle.setId(balls.size());
            balls.add(circle);
        }
    }

    private boolean doCirclesOverlap(Circle c1, Circle c2) {
        return Math.abs((c1.getPosX() - c2.getPosX()) * (c1.getPosX() - c2.getPosX()) +
                (c1.getPosY() - c2.getPosY()) * (c1.getPosY() - c2.getPosY())) <=
                ((c1.getRadius() + c2.getRadius()) * (c1.getRadius() + c2.getRadius()));
    }

    private boolean isPointInCircle(Circle c1, float posX, float posY) {
        return Math.abs((c1.getPosX() - posX) * (c1.getPosX() - posX) +
                (c1.getPosY() - posY) * (c1.getPosY() - posY)) <=
                (c1.getRadius() * c1.getRadius());
    }

    @Override
    public void initialize(GameContainer gc) {
        mousePosition = new Vec2d();
        balls = new ArrayList<>();
        collidingPairs = new ArrayList<>();
        friction = 0.8f;
        fillBallsWithRandomBalls();
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if (gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            clip.play();
            balls.clear();
            fillBallsWithRandomBalls();
        }

        if ( gc.getInput().isButtonDown(1) ) {
            clip.play();
            mousePosition.setX(gc.getInput().getMouseX());
            mousePosition.setY(gc.getInput().getMouseY());
            for ( int i = 0; i < balls.size(); i++ ) {
                if ( isPointInCircle(balls.get(i), mousePosition.getX(), mousePosition.getY()) ) {
                    balls.get(i).setSelected(true);
                }
            }
        }

        if ( gc.getInput().isButtonUp(1) ) {
            for ( int i = 0; i < balls.size(); i++ ) {
                if ( balls.get(i).isSelected() ) {
                    balls.get(i).setSelected(false);
                }
            }
        }

        for ( int i = 0; i < balls.size(); i++ ) {
            balls.get(i).setaX( - balls.get(i).getVelX() * friction);
            balls.get(i).setaY( - balls.get(i).getVelY() * friction);

            float aX = balls.get(i).getaX();
            float aY = balls.get(i).getaY();
            float velX = balls.get(i).getVelX();
            float velY = balls.get(i).getVelY();

            balls.get(i).setVelX(velX + aX * dt);
            balls.get(i).setVelY(velY + aY * dt);

            float posX = balls.get(i).getPosX();
            float posY = balls.get(i).getPosY();

            if ( posX < 0 ) {
                posX += (float)(SCREEN_WIDTH);
                balls.get(i).setPosX(posX);
            }

            if ( posX >= SCREEN_WIDTH ) {
                posX -= (float)(SCREEN_WIDTH);
                balls.get(i).setPosX(posX);
            }

            if ( posY < 0 ) {
                posY += (float)(SCREEN_HEIGHT);
                balls.get(i).setPosY(posY);
            }

            if ( posY >= SCREEN_HEIGHT ) {
                posY -= (float)(SCREEN_HEIGHT);
                balls.get(i).setPosY(posY);
            }

            if (Math.abs(balls.get(i).getVelX() * balls.get(i).getVelX() + balls.get(i).getVelY() * balls.get(i).getVelY()) < 0.01f) {
                balls.get(i).setVelX(0);
                balls.get(i).setVelY(0);
            }
        }

        for ( int i = 0; i < balls.size(); i++ ) {
            for ( int j = 0; j < balls.size(); j++ ) {
                if ( balls.get(i).getId() != balls.get(j).getId() ) {
                    if ( doCirclesOverlap(balls.get(i), balls.get(j)) ) {
                        CollidingShapes collidingShapes = new CollidingShapes(balls.get(i), balls.get(j));
                        collidingPairs.add(collidingShapes);
                        float distance = (float) Math.sqrt(
                                (balls.get(i).getPosX() - balls.get(j).getPosX()) * (balls.get(i).getPosX() - balls.get(j).getPosX()) +
                                (balls.get(i).getPosY() - balls.get(j).getPosY()) * (balls.get(i).getPosY() - balls.get(j).getPosY()));

                        float overlap = 0.5f * (distance - balls.get(i).getRadius() - balls.get(j).getRadius());

                        float differenceX = balls.get(i).getPosX() - balls.get(j).getPosX();
                        float differenceY = balls.get(i).getPosY() - balls.get(j).getPosY();
                        float ballPosX = balls.get(i).getPosX();
                        float ballPosY = balls.get(i).getPosY();
                        float targetPosX = balls.get(j).getPosX();
                        float targetPosY = balls.get(j).getPosY();

                        ballPosX -= overlap * differenceX / distance;
                        balls.get(i).setPosX(ballPosX);
                        ballPosY -= overlap * differenceY / distance;
                        balls.get(i).setPosY(ballPosY);

                        targetPosX += overlap * differenceX / distance;
                        balls.get(j).setPosX(targetPosX);
                        targetPosY += overlap * differenceY / distance;
                        balls.get(j).setPosY(targetPosY);
                    }
                }
            }
        }

        for ( int i = 0; i < collidingPairs.size(); i++ ) {
            Circle b1 = (Circle) collidingPairs.get(i).getShape();
            Circle b2 = (Circle) collidingPairs.get(i).getTarget();

            float distance = (float)(Math.sqrt(
                    (b1.getPosX() - b2.getPosX()) * (b1.getPosX() - b2.getPosX()) +
                    (b1.getPosY() - b2.getPosY()) * (b1.getPosY() - b2.getPosY())
            ));

            float nx = (b2.getPosX() - b1.getPosX()) / distance;
            float ny = (b2.getPosY() - b1.getPosY()) / distance;

            float tx = -ny;
            float ty = nx;

            float dpTan1 = b1.getVelX() * tx + b1.getVelY() * ty;
            float dpTan2 = b2.getVelX() * tx + b2.getVelY() * ty;

            float dpNorm1 = b1.getVelX() * nx + b1.getVelY() * ny;
            float dpNorm2 = b2.getVelX() * nx + b2.getVelY() * ny;

            float m1 = (dpNorm1 * (b1.getMass() - b2.getMass()) + 2.0f * b2.getMass() * dpNorm2) / (b1.getMass() + b2.getMass());
            float m2 = (dpNorm2 * (b2.getMass() - b1.getMass()) + 2.0f * b1.getMass() * dpNorm1) / (b1.getMass() + b2.getMass());

            b1.setVelX(tx * dpTan1 + nx * m1);
            b1.setVelY(ty * dpTan1 + ny * m1);
            b2.setVelX(tx * dpTan2 + nx * m2);
            b2.setVelY(ty * dpTan2 + ny * m2);

            collidingPairs.get(i).setShape(b1);
            collidingPairs.get(i).setShape(b2);
        }

        collidingPairs.clear();

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.clear(0xff00007d);
        mousePosition.setX(gc.getInput().getMouseX());
        mousePosition.setY(gc.getInput().getMouseY());
        for ( Circle ball : balls ) {
            if ( ball.isSelected() ) {
                ball.setPosX(mousePosition.getX());
                ball.setPosY(mousePosition.getY());
            }
            ball.drawYourSelf(r);
        }

        /*
        todo: esto parpadea en el renderizado
        for ( CollidingShapes pair : collidingPairs ) {
            r.drawLine(
                    (int)(pair.getShape().getPosX()), (int)(pair.getShape().getPosY()),
                    (int)(pair.getTarget().getPosX()), (int)(pair.getTarget().getPosY()), 0xffff0000
            );
        }
        */

        r.drawCircle((int)(mousePosition.getX()), (int)(mousePosition.getY()), 2, 0xffffff00);
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new BallsGame());
        gc.setWidth(SCREEN_WIDTH);
        gc.setHeight(SCREEN_HEIGHT);
        gc.setScale(SCREEN_SCALE);
        gc.start();
    }

}
