package programs.atoms.twodimensions;

import engine.gfx.Renderer;
import engine.gfx.images.Image;
import engine.maths.Mat3x3;
import engine.maths.MatrixOperations;
import engine.gfx.Drawable;
import engine.gfx.SelectableByMouse;
import engine.maths.points2d.Vec2DGeneralFloat;
import programs.atoms.IdAndPos;

import java.util.ArrayList;

public class Atom implements Drawable, SelectableByMouse {

    private final int NUM_LINK_POSITIONS = 4;

    private int id = 0;

    private float radius = 40.0f;

    private float rotation = 0.0f;

    private Image image = new Image("/atoms/Carbon.png");

    private Vec2DGeneralFloat position = new Vec2DGeneralFloat(0.0f, 0.0f);

    private ArrayList<Vec2DGeneralFloat> linkPositions = new ArrayList<>();

    private ArrayList<IdAndPos> linkedAtomsIndexes = new ArrayList<>();

    private boolean isShowingLinks = true;

    public Atom() {
        for ( int i = 0; i < NUM_LINK_POSITIONS; i++ ) {
            linkPositions.add(new Vec2DGeneralFloat());
        }
    }

    public Atom(float x, float y) {
        this();
        position.setX(x);
        position.setY(y);
        recalculateLinkPositions(rotation, position);
    }

    public Atom(Vec2DGeneralFloat position) {
        this(position.getX(), position.getY());
        recalculateLinkPositions(rotation, position);
    }

    private void recalculateLinkPositions(float rotation, Vec2DGeneralFloat position) {
        float x = position.getX();
        float y = position.getY();
        Vec2DGeneralFloat linkPosition;
        float linkX;
        float linkY;
        for ( int i = 0; i < NUM_LINK_POSITIONS; i++ ) {
            if ( i == 0 ) {
                linkPositions.get(i).setX(x);
                linkPositions.get(i).setY(y);
            } else {
                linkPosition = new Vec2DGeneralFloat(0.0f, - radius);
                linkPosition.translateThisAngle(rotation + ( (i - 1) * 120.0f ));
                linkPosition.addToX(x);
                linkPosition.addToY(y);
                linkX = linkPosition.getX();
                linkY = linkPosition.getY();
                linkPositions.get(i).setX(linkX);
                linkPositions.get(i).setY(linkY);
            }
        }
    }

    public int getId() {
        return id;
    }

    public float getRadius() {
        return radius;
    }

    public Vec2DGeneralFloat getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public boolean isShowingLinks() {
        return isShowingLinks;
    }

    public Vec2DGeneralFloat getLinkPosition(int id) {
        return linkPositions.get(id);
    }

    public ArrayList<IdAndPos> getLinkedAtomsIndexes() {
        return linkedAtomsIndexes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        recalculateLinkPositions(this.rotation, position);
    }

    public void setShowingLinks(boolean showingLinks) {
        isShowingLinks = showingLinks;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setImage(Image image) {
        this.image = image;
        radius = ( image.getW() + image.getH() ) / 4.0f;
    }

    public void setPosition(Vec2DGeneralFloat position) {
        float x = position.getX();
        float y = position.getY();
        this.position.setX(x);
        this.position.setY(y);
        recalculateLinkPositions(rotation, this.position);
    }

    public void setPositionAndRotation(Vec2DGeneralFloat position, float rotation) {
        float x = position.getX();
        float y = position.getY();
        this.position.setX(x);
        this.position.setY(y);
        this.rotation = rotation;
        recalculateLinkPositions(this.rotation, this.position);
    }

    @Override
    public void drawYourSelf(Renderer r) {
        Mat3x3 matrixA = new Mat3x3();
        Mat3x3 matrixB = new Mat3x3();
        Mat3x3 matrixC;
        Mat3x3 matrixD = new Mat3x3();
        Mat3x3 matrixE;
        Mat3x3 matrixFinal;

        matrixA.setAsTranslate(-image.getW() / 2.0f, -image.getH() / 2.0f);
        matrixB.setAsRotate(0);
        matrixC = MatrixOperations.multiply(matrixA, matrixB);
        float scale = radius / 40.0f;
        matrixD.setAsScale(scale, scale); // todo algo esta mal aquí por que el primer atomo no es del mismo tamaño que los demás.
        matrixE = MatrixOperations.multiply(matrixC, matrixD);

        matrixA.setAsTranslate(position.getX(), position.getY());

        matrixFinal = MatrixOperations.multiply(matrixE, matrixA);

        r.drawImage(image, matrixFinal);

        if (isShowingLinks) {
            for (Vec2DGeneralFloat linkPosition : linkPositions) {
                r.drawCircle(linkPosition.getX().intValue(), linkPosition.getY().intValue(), 5, 0xffff0000);
            }
        }

    }

    private boolean isPointInsideCircle(float circleX, float circleY, float x, float y, float radius) {
        return Math.abs((circleX - x) * (circleX - x) + (circleY - y) * (circleY - y)) <= (radius * radius);
    }

    @Override
    public boolean isPointInside(float x, float y) {
        return isPointInsideCircle(position.getX(), position.getY(), x, y, radius / 4);
    }

    public int isPointInsideALink(float x, float y) {
        for ( int i = 0; i < linkPositions.size(); i++ ) {
            if ( isPointInsideCircle(linkPositions.get(i).getX(), linkPositions.get(i).getY(), x, y, 10) ) {
                return i;
            }
        }
        return -1;
    }

}
