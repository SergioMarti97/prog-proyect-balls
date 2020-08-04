package programs.atoms.threedimensions;

import engine.maths.Mat4x4;
import engine.gfx.engine3d.normal.MatrixMath;
import engine.maths.Vec3d;
import programs.atoms.CarbonHybridization;

import java.util.ArrayList;

public class Molecule3D {

    private ArrayList<Atom3D> atoms = new ArrayList<>();

    private Vec3d center = new Vec3d();

    public Molecule3D() {
        Atom3D atom3D = new Atom3D(new Vec3d(0, 0, 0), CarbonHybridization.SP3);
        Mat4x4 matScale = MatrixMath.matrixMakeScale(atom3D.getRadius(), atom3D.getRadius(), atom3D.getRadius());
        atom3D.transform(matScale);
        atoms.add(atom3D);
    }

    public ArrayList<Atom3D> getAtoms() {
        return atoms;
    }

    public void addAtom(Atom3D newAtom, int id, int link) {
        Vec3d accumulated = new Vec3d();

        for ( Atom3D atom3D : atoms ) {

            accumulated.addToX(atom3D.getPoints3dFinal()[0].getX());
            accumulated.addToY(atom3D.getPoints3dFinal()[0].getY());
            accumulated.addToZ(atom3D.getPoints3dFinal()[0].getZ());

            if ( atom3D.getId() == id ) {
                Mat4x4 matScale = MatrixMath.matrixMakeScale(newAtom.getRadius(), newAtom.getRadius(), newAtom.getRadius());

                Mat4x4 rotX = MatrixMath.matrixMakeRotationX(atom3D.getRotation().getX());
                Mat4x4 rotY = MatrixMath.matrixMakeRotationY(atom3D.getRotation().getY());
                Mat4x4 rotZ = MatrixMath.matrixMakeRotationZ(atom3D.getRotation().getZ());
                Mat4x4 rotXY = MatrixMath.matrixMultiplyMatrix(rotX, rotY);
                Mat4x4 rotFatherAtom = MatrixMath.matrixMultiplyMatrix(rotZ, rotXY);

                Mat4x4 rotFinal;
                Mat4x4 matTranslation;
                Mat4x4 matRotTrans;
                Mat4x4 matFinal;
                Mat4x4 rotXZ;

                int angleRotZGrades;
                int angleRotYGrades;
                switch ( link ) {
                    case 1: default:
                        rotX = MatrixMath.matrixMakeRotationX((float)(Math.PI));
                        rotFinal = MatrixMath.matrixMultiplyMatrix(rotFatherAtom, rotX);
                        matTranslation = MatrixMath.matrixMakeTranslation(
                                atom3D.getPoints3dFinal()[1].getX(),
                                atom3D.getPoints3dFinal()[1].getY(),
                                atom3D.getPoints3dFinal()[1].getZ()
                        );
                        newAtom.setRotation(new Vec3d((float)(Math.PI), 0.0f, 0.0f));
                        newAtom.setTranslate(atom3D.getPoints3dModel()[1]);
                        matRotTrans = MatrixMath.matrixMultiplyMatrix(rotFinal, matTranslation);
                        matFinal = MatrixMath.matrixMultiplyMatrix(matScale, matRotTrans);
                        newAtom.transform(matFinal);
                        break;
                    case 2:
                        angleRotZGrades = -60;
                        rotZ = MatrixMath.matrixMakeRotationZ((float)(angleRotZGrades * Math.PI / 180));
                        rotFinal = MatrixMath.matrixMultiplyMatrix(rotFatherAtom, rotZ);
                        matTranslation = MatrixMath.matrixMakeTranslation(
                                atom3D.getPoints3dFinal()[2].getX(),
                                atom3D.getPoints3dFinal()[2].getY(),
                                atom3D.getPoints3dFinal()[2].getZ()
                        );
                        newAtom.setRotation(new Vec3d((float)(angleRotZGrades * Math.PI / 180), 0.0f, 0.0f));
                        newAtom.setTranslate(atom3D.getPoints3dModel()[2]);
                        matRotTrans = MatrixMath.matrixMultiplyMatrix(rotFinal, matTranslation);
                        matFinal = MatrixMath.matrixMultiplyMatrix(matScale, matRotTrans);
                        newAtom.transform(matFinal);
                        break;
                    case 3:
                        angleRotZGrades = 60;
                        angleRotYGrades = 120;
                        rotZ = MatrixMath.matrixMakeRotationZ((float)(angleRotZGrades * Math.PI / 180));
                        rotY = MatrixMath.matrixMakeRotationY((float)(angleRotYGrades * Math.PI / 180));
                        rotXZ = MatrixMath.matrixMultiplyMatrix(rotY, rotZ);
                        rotFinal = MatrixMath.matrixMultiplyMatrix(rotFatherAtom, rotXZ);
                        matTranslation = MatrixMath.matrixMakeTranslation(
                                atom3D.getPoints3dFinal()[3].getX(),
                                atom3D.getPoints3dFinal()[3].getY(),
                                atom3D.getPoints3dFinal()[3].getZ()
                        );
                        newAtom.setRotation(new Vec3d((float)(angleRotZGrades * Math.PI / 180), (float)(angleRotYGrades * Math.PI / 180), 0.0f));
                        newAtom.setTranslate(atom3D.getPoints3dModel()[3]);
                        matRotTrans = MatrixMath.matrixMultiplyMatrix(rotFinal, matTranslation);
                        matFinal = MatrixMath.matrixMultiplyMatrix(matScale, matRotTrans);
                        newAtom.transform(matFinal);
                        break;
                    case 4:
                        angleRotZGrades = 60;
                        angleRotYGrades = 240;
                        rotZ = MatrixMath.matrixMakeRotationZ((float)(angleRotZGrades * Math.PI / 180));
                        rotY = MatrixMath.matrixMakeRotationY((float)(angleRotYGrades * Math.PI / 180));
                        rotXZ = MatrixMath.matrixMultiplyMatrix(rotY, rotZ);
                        rotFinal = MatrixMath.matrixMultiplyMatrix(rotFatherAtom, rotXZ);
                        matTranslation = MatrixMath.matrixMakeTranslation(
                                atom3D.getPoints3dFinal()[4].getX(),
                                atom3D.getPoints3dFinal()[4].getY(),
                                atom3D.getPoints3dFinal()[4].getZ()
                        );
                        newAtom.setRotation(new Vec3d((float)(angleRotZGrades * Math.PI / 180), (float)(angleRotYGrades * Math.PI / 180), 0.0f));
                        newAtom.setTranslate(atom3D.getPoints3dModel()[4]);
                        matRotTrans = MatrixMath.matrixMultiplyMatrix(rotFinal, matTranslation);
                        matFinal = MatrixMath.matrixMultiplyMatrix(matScale, matRotTrans);
                        newAtom.transform(matFinal);
                        break;
                }

                accumulated.addToX(newAtom.getPoints3dFinal()[0].getX());
                accumulated.addToY(newAtom.getPoints3dFinal()[0].getY());
                accumulated.addToZ(newAtom.getPoints3dFinal()[0].getZ());
            }
        }
        newAtom.setId(atoms.size());
        atoms.add(newAtom);

        center.set(
                accumulated.getX() / atoms.size(),
                accumulated.getY() / atoms.size(),
                accumulated.getZ() / atoms.size()
        );
    }

    public void rotate(Mat4x4 matRot) {
        Vec3d difference = new Vec3d();
        Mat4x4 matRotAtom;
        Mat4x4 matRotFinal;
        Mat4x4 matTranslate;
        Mat4x4 matFinal;
        for ( Atom3D atom3D : atoms ) {
            for ( int i = 0; i < atom3D.getPoints3dModel().length; i++ ) {
                difference.setX(atom3D.getPoints3dModel()[i].getX() - center.getX());
                difference.setY(atom3D.getPoints3dModel()[i].getY() - center.getY());
                difference.setZ(atom3D.getPoints3dModel()[i].getZ() - center.getZ());

                matRotAtom = MatrixMath.matrixMakeRotation(atom3D.getRotation());

                matRotFinal = MatrixMath.matrixMultiplyMatrix(matRotAtom, matRot);

                matTranslate = MatrixMath.matrixMakeTranslation(atom3D.getTranslate());

                matFinal = MatrixMath.matrixMultiplyMatrix(matTranslate, matRotFinal);

                difference = MatrixMath.matrixMultiplyVector(matFinal, difference);

                atom3D.getPoints3dFinal()[i] = MatrixMath.vectorAdd(difference, center);
            }
        }
    }

}
