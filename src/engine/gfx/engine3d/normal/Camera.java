package engine.gfx.engine3d.normal;

import engine.gfx.Renderer;
import engine.maths.Mat4x4;
import engine.maths.Vec3d;

/**
 * Esta clase representa la cámara para renderizar la escena de un mundo 3d.
 * 
 * La cámara matemáticamente hablando solamente es una matriz, calculada por la 
 * posición donde se encuentra el espectador y la dirección en la que está mirando.
 * 
 * Posteriormente, se deben de transformar los puntos tridimensionales o los triangulos
 * en función de esta información.
 * Es por ello que la matriz con la que se debe de transformar al mundo es la matriz
 * de visión, que corresponde con la inversa de la matriz que representa a la cámara.
 * La matriz de visión transforma los puntos en 3d a la posición en 3d que correspondería
 * si se estubiera viendo la escena desde la posición y la dirección de la cámara, en vez
 * del 0, 0, 0.
 * 
 * @class: Camera.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-28
 */
public class Camera {

    /**
     * La matriz de visión. Es la que se acaba utilizando para la transformación del mundo.
     */
    private Mat4x4 matView;

    /**
     * La matriz que representa la cámara. Se necesita para calcular la matriz de visión.
     */
    private Mat4x4 matCamera;

    /**
     * Donde se encuentra la cámara.
     */
    private Vec3d origin;

    /**
     * La dirección donde está apuntando la cámara.
     */
    private Vec3d lookDirection;

    /**
     * La altura a la que se encuentra la cámara.
     */
    private Vec3d up = new Vec3d(0.0f, 1.0f, 0.0f);

    /**
     * El objetivo al cual está mirando la cámara en un principio. Se necesita para calcular lookDirection.
     */
    private Vec3d target = new Vec3d(0.0f, 0.0f, 1.0f);

    /**
     * La rotación de la cámara en los 3 ejes.
     */
    private Vec3d cameraRot = new Vec3d(0.0f, 0.0f, 0.0f);

    /**
     * Constructor
     */
    public Camera() {
        origin = new Vec3d();
        lookDirection = new Vec3d();
        matCamera = calculateMatCamera(up, target, MatrixMath.matrixMakeIdentity());
        matView = MatrixMath.matrixQuickInverse(matCamera);
    }

    /**
     * Este método sirve para calcular la matriz de la cámara.
     * 
     * @param up la altura a la que está la cámara en un principio.
     * @param target el punto "objetivo" donde está apuntando la cámara. 
     * @param transform la transformación que tiene la cámara.
     * @return la matriz que representa la cámara.
     */
    private Mat4x4 calculateMatCamera(Vec3d up, Vec3d target, Mat4x4 transform) {
        lookDirection = MatrixMath.matrixMultiplyVector(transform, target);
        target = MatrixMath.vectorAdd(origin, lookDirection);
        return MatrixMath.matrixPointAt(origin, target, up);
    }

    /**
     * Este método sirve para calcular la la matriz que representa la cámara y la matriz de visión.
     * Realiza las transformaciones necesarias para la rotación de la cámara.
     * Actualiza la matriz de la cámara y de visión del objeto.
     * 
     * @return devuelve la matriz de visión.
     */
    public Mat4x4 getMatView() {
        Mat4x4 matCameraRotX = MatrixMath.matrixMakeRotationX(cameraRot.getX());
        Mat4x4 matCameraRotY = MatrixMath.matrixMakeRotationY(cameraRot.getY());
        Mat4x4 matCameraRotZ = MatrixMath.matrixMakeRotationZ(cameraRot.getZ());
        Mat4x4 matCameraRotXY = MatrixMath.matrixMultiplyMatrix(matCameraRotX, matCameraRotY);
        Mat4x4 matCameraRot = MatrixMath.matrixMultiplyMatrix(matCameraRotXY, matCameraRotZ);
        matCamera = calculateMatCamera(up, target, matCameraRot);
        matView = MatrixMath.matrixQuickInverse(matCamera);
        return matView;
    }

    /**
     * Este método no tiene nada que ver con las operaciones que se hacen para la cámara.
     * Solamente es un método que se utiliza para ahorrar código y poder imprimir toda
     * la información de la cámara.
     * 
     * @param r la clase de renderizado donde se encuentran las funciones de dibujado.
     * @param name el nombre del vector (por ejemplo: origin, target, lookDirection...).
     * @param vec3d el vector.
     * @param offSetX el desplazamiento X en la pantalla. Es decir, la posición X donde se dibuja el texto.
     * @param offSetY el desplazamiento Y en la pantalla. Es decir, la posición Y donde se dibuja el texto.
     * @param color el color del texto.
     */
    private void printVec3d(Renderer r, String name, Vec3d vec3d, int offSetX, int offSetY, int color) {
        r.drawText(
                String.format("  %s X: %.3f Y: %.3f Z: %.3f",
                        name,
                        vec3d.getX(),
                        vec3d.getY(),
                        vec3d.getZ()),
                offSetX, offSetY, color);
    }

    /**
     * Este método sirve para mostrar por pantalla toda la información que contiene el objeto
     * Camera por pantalla. Es realmente útil para debuggear.
     * 
     * @param r la clase de renderizado donde se encuentran las funciones de dibujado.
     * @param offSetX el desplazamiento X en la pantalla. Es decir, la posición X donde se dibuja el texto.
     * @param offSetY el desplazamiento Y en la pantalla. Es decir, la posición Y donde se dibuja el texto.
     * @param color el color del texto.
     */
    public void showInformation(Renderer r, int offSetX, int offSetY, int color) {
        int increment = 30;
        r.drawText("Camera", offSetX, offSetY, color);
        offSetY += increment;
        printVec3d(r, "Origin", origin, offSetX, offSetY, color);
        offSetY += increment;
        printVec3d(r, "Look direction", lookDirection, offSetX, offSetY, color);
        offSetY += increment;
        printVec3d(r, "Up", up, offSetX, offSetY, color);
        offSetY += increment;
        printVec3d(r, "Target", target, offSetX, offSetY, color);
        offSetY += increment;
        printVec3d(r, "Camera rotation", cameraRot, offSetX, offSetY, color);
    }

    /**
     * Rota la cámara en el eje X los radianes pasados por parámetro.
     * 
     * @param angleRad el ángulo de rotación en el eje X.
     */
    public void rotX(float angleRad) {
        float rotX = cameraRot.getX();
        rotX += angleRad;
        cameraRot.setX(rotX);
    }

    /**
     * Rota la cámara en el eje Y los radianes pasados por parámetro.
     *
     * @param angleRad el ángulo de rotación en el eje Y.
     */
    public void rotY(float angleRad) {
        float rotY = cameraRot.getY();
        rotY += angleRad;
        cameraRot.setY(rotY);
    }

    /**
     * Rota la cámara en el eje Z los radianes pasados por parámetro.
     *
     * @param angleRad el ángulo de rotación en el eje Z.
     */
    public void rotZ(float angleRad) {
        float rotZ = cameraRot.getZ();
        rotZ += angleRad;
        cameraRot.setZ(rotZ);
    }

    /**
     * Modifica la posición de origen de la cámara.
     * 
     * @param origin el nuevo origen de la cámara.
     */
    public void setOrigin(Vec3d origin) {
        this.origin = origin;
    }

    public Vec3d getOrigin() {
        return origin;
    }

    public Vec3d getCameraRot() {
        return cameraRot;
    }

    public Vec3d getUp() {
        return up;
    }

    public Vec3d getTarget() {
        return target;
    }

    public Vec3d getLookDirection() {
        return lookDirection;
    }

}
