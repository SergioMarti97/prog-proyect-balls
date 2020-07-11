package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.audio.SoundClip;
import engine.engine3d.Vec2d;
import engine.gfx.Renderer;
import engine.physics.Ball;
import engine.physics.CollidingShapes;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BallsGame extends AbstractGame {

    private ArrayList<Ball> balls;

    private ArrayList<CollidingShapes> collidingPairs;

    private SoundClip clip;

    private Vec2d mousePosition;

    private float friction;

    private BallsGame(String title) {
        super(title);
    }

    private float getRandomFloatBetweenRange(int max, int min) {
        return (float) ((Math.random() * ((max - min) + 1)) + min);
    }

    private void fillBallsWithRandomBalls(int width, int height) {
        int maxBalls = 50;
        for (int i = 0; i < maxBalls; i++ ) {
            float radius = getRandomFloatBetweenRange(30, 10);
            float posX = getRandomFloatBetweenRange((int)(width - radius), (int)(radius));
            float posY = getRandomFloatBetweenRange((int)(height - radius), (int)(radius));
            Ball ball = new Ball(posX, posY, radius, 0xffffffff);
            ball.setId(balls.size());
            ball.setVelX(50.0f);
            ball.setVelY(50.0f);
            balls.add(ball);
        }
    }

    private boolean doCirclesOverlap(Ball c1, Ball c2) {
        return Math.abs((c1.getPosX() - c2.getPosX()) * (c1.getPosX() - c2.getPosX()) +
                (c1.getPosY() - c2.getPosY()) * (c1.getPosY() - c2.getPosY())) <=
                ((c1.getRadius() + c2.getRadius()) * (c1.getRadius() + c2.getRadius()));
    }

    private boolean isPointInCircle(Ball c1, float posX, float posY) {
        return Math.abs((c1.getPosX() - posX) * (c1.getPosX() - posX) +
                (c1.getPosY() - posY) * (c1.getPosY() - posY)) <=
                (c1.getRadius() * c1.getRadius());
    }

    @Override
    public void initialize(GameContainer gc) {
        clip = new SoundClip("/audio/sound.wav");
        mousePosition = new Vec2d();
        balls = new ArrayList<>();
        collidingPairs = new ArrayList<>();
        friction = 0.8f;
        fillBallsWithRandomBalls(gc.getWidth(), gc.getHeight());
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if (gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            clip.play();
            balls.clear();
            fillBallsWithRandomBalls(gc.getWidth(), gc.getHeight());
        }

        if ( gc.getInput().isButtonDown(1) ) {
            clip.play();
            mousePosition.setX(gc.getInput().getMouseX());
            mousePosition.setY(gc.getInput().getMouseY());
            for (Ball ball : balls) {
                if (isPointInCircle(ball, mousePosition.getX(), mousePosition.getY())) {
                    ball.setSelected(true);
                }
            }
        }

        if ( gc.getInput().isButtonUp(1) ) {
            for (Ball ball : balls) {
                if (ball.isSelected()) {
                    ball.setSelected(false);
                }
            }
        }

        Vec2d mousePositionLast = new Vec2d();

        if ( gc.getInput().isButtonDown(2) ) {
            clip.play();
            clip.play();
            mousePositionLast.setX(gc.getInput().getMouseX());
            mousePositionLast.setY(gc.getInput().getMouseY());
            for (Ball ball : balls) {
                if (isPointInCircle(ball, mousePositionLast.getX(), mousePositionLast.getY())) {
                    ball.setSelected(true);
                }
            }
        }

        if ( gc.getInput().isButtonUp(2) ) {
            for (Ball ball : balls) {
                if (ball.isSelected()) {
                    float newVelX = 5.0f * (gc.getInput().getMouseX() - mousePositionLast.getX());
                    float newVelY = 5.0f * (gc.getInput().getMouseY() - mousePositionLast.getY());
                    ball.setVelX(newVelX);
                    ball.setVelY(newVelY);
                    ball.setSelected(false);
                }
            }
        }

        for (Ball ball : balls) {
            ball.setAccelerationX(-ball.getVelX() * friction);
            ball.setAccelerationY(-ball.getVelY() * friction);

            float aX = ball.getAccelerationX();
            float aY = ball.getAccelerationY();
            float velX = ball.getVelX();
            float velY = ball.getVelY();

            ball.setVelX(velX + aX * dt);
            ball.setVelY(velY + aY * dt);

            float posX = ball.getPosX();
            float posY = ball.getPosY();

            ball.setPosX(posX + ball.getVelX() * dt);
            ball.setPosY(posY + ball.getVelY() * dt);

            if (posX < 0) {
                posX += (float) (gc.getWidth());
                ball.setPosX(posX);
            }

            if (posX >= gc.getWidth()) {
                posX -= (float) (gc.getWidth());
                ball.setPosX(posX);
            }

            if (posY < 0) {
                posY += (float) (gc.getHeight());
                ball.setPosY(posY);
            }

            if (posY >= gc.getHeight()) {
                posY -= (float) (gc.getHeight());
                ball.setPosY(posY);
            }

            if (Math.abs(ball.getVelX() * ball.getVelX() + ball.getVelY() * ball.getVelY()) < 0.01f) {
                ball.setVelX(0);
                ball.setVelY(0);
            }
        }

        for ( int i = 0; i < balls.size(); i++ ) {
            for (Ball ball : balls) {
                if (balls.get(i).getId() != ball.getId()) {
                    if (doCirclesOverlap(balls.get(i), ball)) {
                        CollidingShapes collidingShapes = new CollidingShapes(balls.get(i), ball);
                        collidingPairs.add(collidingShapes);
                        float distance = (float) Math.sqrt(
                                (balls.get(i).getPosX() - ball.getPosX()) * (balls.get(i).getPosX() - ball.getPosX()) +
                                        (balls.get(i).getPosY() - ball.getPosY()) * (balls.get(i).getPosY() - ball.getPosY()));

                        float overlap = 0.5f * (distance - balls.get(i).getRadius() - ball.getRadius());

                        float differenceX = balls.get(i).getPosX() - ball.getPosX();
                        float differenceY = balls.get(i).getPosY() - ball.getPosY();
                        float ballPosX = balls.get(i).getPosX();
                        float ballPosY = balls.get(i).getPosY();
                        float targetPosX = ball.getPosX();
                        float targetPosY = ball.getPosY();

                        ballPosX -= overlap * differenceX / distance;
                        balls.get(i).setPosX(ballPosX);
                        ballPosY -= overlap * differenceY / distance;
                        balls.get(i).setPosY(ballPosY);

                        targetPosX += overlap * differenceX / distance;
                        ball.setPosX(targetPosX);
                        targetPosY += overlap * differenceY / distance;
                        ball.setPosY(targetPosY);
                    }
                }
            }
        }

        for (CollidingShapes collidingPair : collidingPairs) {
            Ball b1 = (Ball) collidingPair.getShape2D();
            Ball b2 = (Ball) collidingPair.getTarget();

            float distance = (float) (Math.sqrt(
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

            collidingPair.setShape2D(b1);
            collidingPair.setShape2D(b2);
        }

        collidingPairs.clear();

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.clear(0xff00007d);
        mousePosition.setX(gc.getInput().getMouseX());
        mousePosition.setY(gc.getInput().getMouseY());
        for ( Ball ball : balls ) {
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
        GameContainer gc = new GameContainer(new BallsGame("Pelotas"));
        gc.start();
    }

}
