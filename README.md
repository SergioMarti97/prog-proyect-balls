# Sergio Game Engine Version: 0.0.01 pre-alpha
This is a project made by Sergio Martí Torregrosa during the months of July and august. It pretends to be a simple 
game engine to develop window applications, coded in Java.
For the moment, it uses the java.swing package 
< https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html>. However, in the future the project 
is wanted to be able to also use JavaFx, or ported it to mobile applications in Android Studio.


```html
<html>
  <head>
  </head>
  <boyd>
    <h1>Sergio Game Engine. V. 0.0.01 pre-alfa.</h1>
    <p>Este es un proyecto programado en Java el cual pretende ser un motor simple que de una forma rápida y sencilla, 
    permita crear aplicaciones de ventana.</p>
    <p>De momento utiliza el paquete java.swing 
    (<a>https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html</a>). Pero en un futuro, se quiere 
    pasar a que el proyecto también pueda utilizar JavaFx, o aplicaciones móviles con AndroidStudio</p>
    <p>Fecha de creación del proyecto: 06/07/2020.</p>
    <h2>Descripción del proyecto</h2>
    <p>El término motor de videojuego (en inglés game engine), o simplemente motor de juego, hace referencia a una serie de 
    rutinas de programación que permiten el diseño, la creación y el funcionamiento de un videojuego.</p>
    <p>La funcionalidad típica que provee un motor de videojuego incluye:</p>
    <ul>
        <li>Un motor gráfico para renderizar gráficos 2D y 3D.
          <ul>
            <li>Motor 2D.</li>
            <li>Motor 3D.
              <ul>
                <li>Cámara ortográfica.</li>
                <li>Cámara normal.</li>
              </ul>
            </li>
          </ul>
        </li>
        <li>Un motor físico que simule las leyes de la física (o simplemente para generar detección de colisiones).</li>
        <li>Animación.</li>
        <li>Scripting.</li>
        <li>Sonidos.</li>
        <li>Inteligencia artificial.</li>
        <li>Redes.</li> 
        <li>Retransmisión.</li>
        <li>Gestión de memoria.</li>
        <li>Escenarios gráficos.</li>
        <li>Soporte para lenguaje por secuencia de comandos.</li>
    </ul>
    <h2>Instalación y Configuración</h2>
    <p>Crear un nuevo proyecto de Java en Eclipse, IntellijIDEA o otro IDE a partir de este repositorio.
    El  nuevo proyecto tendrá en el paquete engine todas las clases que el motor utiliza. Se recomienda que para utilizar 
    las claes se lea la documentación del proyecto.</p>
    <h2>Como crear una aplicación</h2>
    <p>Para crear una aplicación, se deben de seguir los siguientes pasos:</p>
    <ol>
      <li>Crear una clase nueva.</li>
      <li>Esta clase nueva debe de heredar de la clase abstracta: "AbstractGame".</li>
      <li>Implementar las funciones de la clase "AbstractGame":
        <ul>
          <li><strong>initialize:</strong> función de inicialización.</li>
          <li><strong>update:</strong> función de actualización.</li>
          <li><strong>render:</strong> función de dibujado.</li>
        </ul>
      </li>
      <li>Crear un constructor. El constructor debe de tener el nombre del programa para que se pueda ver en la barra 
      de título de la ventana. El constructor puede ser publico o privado, dependiendo si la función "main" va a estar en 
      la própia clase o en otra clase que haga uso de esta.</li>
      <li>En la función "main" instanciar un objeto de la clase "GameContainer". En su constructor pasar una instancia de la
      clase de tu aplicación. A continuación se pueden especificar mediante los "setters" de la clase "GameContainer" el 
      tamaño en píxeles de la pantalla (por defecto: 1080 píxeles de ancho por 720 píxeles de alto, siendo los pixeles de 
      tamaño 1). Por último, llamar al método del objeto "GameContainer" "start()".</li>
      <li>Compilar y ejecutar el programa.</li>
    </ol>
    <p>Esta sería una aplicación de ejemplo:</p>
    <code>
      import engine.AbstractGame;
      import engine.GameContainer;
      import engine.gfx.Renderer;
      
      /**
       * Esta es un ejemplo de una aplicación creada con este mótor.
       *
       * @class: AbstractGame.
       * @autor: Sergio Martí Torregrosa. sMartiTo
       * @version: 0.0.01 pre-alpha.
       * @date: 2020-07-20
       */
      public class Example extends AbstractGame {
      
          /**
           * El constructor de la clase.
           *
           * @param title El título que va a tener el programa en la barra de título de la ventana.
           */
          public Example(String title) {
              super(title);
          }
      
      
          /**
           * Calcula un valor Hexadecimal correspondiente a un color
           * dentro de un rango expecificado.
           *
           * @param max Color máximo.
           * @param min Color mínimo.
           * @return Devuelve un código de color hexadecimal.
           */
          private int getRandomColorHexBetweenRange(int max, int min) {
              return (int) ((Math.random() * ((max - min) + 1)) + min);
          }
      
          /**
           * El método de inicialización.
           * @param gc El objeto <class>GameContainer</class> que maneja programa <class>AbstractGame</class>.
           */
          @Override
          public void initialize(GameContainer gc) {
      
          }
      
          /**
           * El método de actualización.
           * @param gc El objeto <class>GameContainer</class> que maneja programa <class>AbstractGame</class>.
           * @param dt Es la variable del tiempo transcurrido, el equivalente a fElapsedTime.
           */
          @Override
          public void update(GameContainer gc, float dt) {
      
          }
      
          /**
           * El método de dibujado.
           * @param gc El objeto <class>GameContainer</class> que maneja programa <class>AbstractGame</class>.
           * @param r  El objeto <class>Render</class> que contiene todas las funciones de dibujado.
           */
          @Override
          public void render(GameContainer gc, Renderer r) {
              for ( int x = 0; x < gc.getWidth(); x++ ) {
                  for ( int y = 0; y < gc.getHeight(); y++ ) {
                      r.setPixel(x, y, getRandomColorHexBetweenRange(0xffffffff, 0xff000000));
                  }
              }
          }
      
          public static void main(String[] args) {
              GameContainer gc = new GameContainer(new Example("Example"));
              gc.setWidth(540);
              gc.setHeight(360);
              gc.setScale(2.0f);
              gc.start();
          }
      
      }
    </code>
    <h2>Manifiesto de archivos</h2>
    <p>Poner aquí todos los archivos y clases que hay.</p>
    <h2>Licencia</h2>
    <p>¿?</p>
    <h2>Desarrollador</h2>
    <p>El programador principal de este código es Sergio Martí Torregrosa.</p>
    <p>Sin embargo, me he basado en el código desarrollado por David Barr (alias Javidx9) de su própio motor 
    olcPixelGameEngine para crear este.</p>
    <p>También mencionar la ayuda del chat de Discord de OneLoneCoder.</p>
    <h2>Bugs.</h2>
    <h3>Versión 0.0.01: pre-alfa:</h3>
    <p>Esta plagado de bugs... El motor 3D no funciona, la colisión de poligonos tampoco. Los frames por segundo 
    son muy bajos.</p>
    <h2>Solución de problemas</h2>
    <p>De momento solamente yo estoy gastando el motor.</p>
    <h2>Registro de cambios</h2>
    <p>De momento no ha habido ningún cambio significativo desde el comienzo del programa.</p>
    <h2>Noticias</h2>
    <p>De momento no hay noticias.</p>
    <h2>Creditos y Agradecimientos</h2>
    <p>Muchísimas gracias a David Barr y la comunidad de OneLoneCoder. También infinitas gracias a mi padre 
    Rubén Martí Torregrosa por enseñarme a programar y por su paciencia inacabable. Por último agradecer también 
    el trabajo de a Alexandre Coloma I Gisbert como tutor y a todo el equipo de profesores del centro educativo 
    CIPFP Batoi por enseñarme a programar en Java e introducirme en el mundo de la informática.</p>
  </boyd>
</html>
```