package programs.atoms.threedimensions;

import engine.gfx.engine3d.normal.Mat4x4;
import engine.gfx.engine3d.normal.MatrixMath;
import engine.gfx.engine3d.normal.Vec3d;
import engine.gfx.images.Image;
import programs.atoms.AtomKind;
import programs.atoms.CarbonHybridization;
import programs.atoms.IdAndPos;

import java.util.ArrayList;

public class Atom3D {

    private int id = 0;

    private float radius = 0.5f;

    private Image image = new Image("/atoms/Carbon.png");

    private int numPoints;

    private Vec3d[] points3dModel;

    private Vec3d[] points3dFinal;

    private ArrayList<IdAndPos> linkedAtomsIndexes = new ArrayList<>();

    private Vec3d translate = new Vec3d();

    private Vec3d rotation = new Vec3d();

    public Atom3D(Vec3d center, CarbonHybridization hybridization) {
        switch ( hybridization ) {
            case SP3: default:
                initializeArrayPoints(5);
                buildSp3Hybridization(center);
                break;
            case SP2:
                initializeArrayPoints(4);
                buildSp2Hybridization(center);
                break;
            case SP:
                initializeArrayPoints(3);
                buildSpHybridization(center);
                break;
        }
    }

    public Atom3D(Vec3d center, CarbonHybridization hybridization, AtomKind atomKind) {
        switch ( hybridization ) {
            case SP3: default:
                initializeArrayPoints(5);
                buildSp3Hybridization(center);
                break;
            case SP2:
                initializeArrayPoints(4);
                buildSp2Hybridization(center);
                break;
            case SP:
                initializeArrayPoints(3);
                buildSpHybridization(center);
                break;
        }

        switch ( atomKind ) {
            case CARBON: default:
                image = new Image("/atoms/Carbon.png");
                break;
            case HYDROGEN:
                points3dModel = new Vec3d[2];
                points3dFinal = new Vec3d[2];
                points3dModel[0] = center;
                points3dFinal[0] = center;
                Vec3d point = new Vec3d(center.getX(), center.getY(), center.getZ());
                point.addToY(1);
                points3dModel[1] = point;
                points3dFinal[1] = point;
                radius = 0.1f;
                image = new Image("/atoms/Hydrogen.png");
                break;
            case OXYGEN:
                image = new Image("/atoms/Oxygen.png");
                break;
            case NITROGEN:
                image = new Image("/atoms/Nitrogen.png");
                break;
            case PHOSPHOR:
                image = new Image("/atoms/Phosphorus.png");
                break;
            case SULFUR:
                image = new Image("/atoms/Sulfur.png");
                break;
        }
    }

    private void initializeArrayPoints(int numPoints) {
        points3dModel = new Vec3d[numPoints];
        points3dFinal = new Vec3d[numPoints];
        for (int i = 0; i < numPoints; i++ ) {
            points3dModel[i] = new Vec3d();
            points3dFinal[i] = new Vec3d();
        }
    }

    private void buildSp3Hybridization(Vec3d center) {
        Vec3d linkPoint;
        points3dModel[0] = center;
        points3dFinal[0] = center;
        Vec3d point = new Vec3d(center.getX(), center.getY(), center.getZ());
        point.addToY(1);
        points3dModel[1] = point;
        points3dFinal[1] = point;
        Mat4x4 matrixRotateZ = MatrixMath.matrixMakeRotationZ((float)(120.0f * Math.PI / 180.0f));
        linkPoint = MatrixMath.matrixMultiplyVector(matrixRotateZ, point);
        points3dModel[2] = linkPoint;
        points3dFinal[2] = linkPoint;
        Mat4x4 matrixRotateY = MatrixMath.matrixMakeRotationY((float)(120.0f * Math.PI / 180.0f));
        Mat4x4 matrixRotateZY = MatrixMath.matrixMultiplyMatrix(matrixRotateZ, matrixRotateY);
        linkPoint = MatrixMath.matrixMultiplyVector(matrixRotateZY, point);
        points3dModel[3] = linkPoint;
        points3dFinal[3] = linkPoint;
        matrixRotateY = MatrixMath.matrixMakeRotationY((float)(240.0f * Math.PI / 180.0f));
        matrixRotateZY = MatrixMath.matrixMultiplyMatrix(matrixRotateZ, matrixRotateY);
        linkPoint = MatrixMath.matrixMultiplyVector(matrixRotateZY, point);
        points3dModel[4] = linkPoint;
        points3dFinal[4] = linkPoint;
    }

    private void buildSp2Hybridization(Vec3d center) {
        Vec3d linkPoint;
        points3dModel[0] = center;
        points3dFinal[0] = center;
        Vec3d point = new Vec3d(center.getX(), center.getY(), center.getZ());
        point.addToY(1);
        points3dModel[1] = point;
        points3dFinal[1] = point;
        Mat4x4 matrixRotateZ = MatrixMath.matrixMakeRotationZ((float)(120.0f * Math.PI / 180.0f));
        linkPoint = MatrixMath.matrixMultiplyVector(matrixRotateZ, point);
        points3dModel[2] = linkPoint;
        points3dFinal[2] = linkPoint;
        linkPoint = MatrixMath.matrixMultiplyVector(matrixRotateZ, linkPoint);
        points3dModel[3] = linkPoint;
        points3dFinal[3] = linkPoint;
    }

    private void buildSpHybridization(Vec3d center) {
        Vec3d linkPoint;
        points3dModel[0] = center;
        points3dFinal[0] = center;
        Vec3d point = new Vec3d(center.getX(), center.getY(), center.getZ());
        point.addToY(1);
        points3dModel[1] = point;
        points3dFinal[1] = point;
        Mat4x4 matrixRotateZ = MatrixMath.matrixMakeRotationZ((float)(180.0f * Math.PI / 180.0f));
        linkPoint = MatrixMath.matrixMultiplyVector(matrixRotateZ, point);
        points3dModel[2] = linkPoint;
        points3dFinal[2] = linkPoint;
    }

    public void transform(Mat4x4 transform) {
        Vec3d pTransformed;
        for (int i = 0; i < points3dModel.length; i++) {
            pTransformed = MatrixMath.matrixMultiplyVector(transform, points3dModel[i]);
            points3dFinal[i] = pTransformed;
        }
    }

    public int getId() {
        return id;
    }

    public Vec3d[] getPoints3dModel() {
        return points3dModel;
    }

    public Vec3d[] getPoints3dFinal() {
        return points3dFinal;
    }

    public Image getImage() {
        return image;
    }

    public float getRadius() {
        return radius;
    }

    public Vec3d getTranslate() {
        return translate;
    }

    public Vec3d getRotation() {
        return rotation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setTranslate(Vec3d translate) {
        this.translate = translate;
    }

    public void setRotation(Vec3d rotation) {
        this.rotation = rotation;
    }

}
