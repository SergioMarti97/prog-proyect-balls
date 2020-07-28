# Sergio Game Engine Version: 0.0.01 pre-alpha
<p>This is a project made by Sergio Martí Torregrosa during the months of July and august. It pretends to be a simple 
game engine to develop window applications, coded in Java.</p>
<p>For the moment, it uses the java.swing package 
<a>https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html</a>. However, in the future the project 
is wanted to be able to also use JavaFx, or ported it to mobile applications in Android Studio.</p>
<p>The project is not finished. When it be, the version will change to 1.0.0. But now, it is in the pre-alpha state.
The project will be considered finished when it has all the points that appear in the description</p>
<p>Project date start: 06/07/2020.</p>

## Project description
<p>The concept of game engine refers to a programming routines set which allows to build, design and run a 
game or application.</p>
<p>The normal functions of a game engine includes:</p>
<ul>
  <li>2D and 3D Graphic engines.
    <ul>
      <li>Motor 2D.
      </li>
      <li>Motor 3D.
        <ul>
          <li>Orthographic camera.</li>
          <li>Normal camera.</li>
        </ul>
      </li>
    </ul>
  </li>
  <li>Physics engine: it has to simulates the physics rules. Or, in a more simply approach, only 
  has to do collision detection.</li>
  <li>Animation.</li>
  <li>Scripting.</li>
  <li>Manage music and sounds.</li>
  <li>Artificial intelligence (AI).</li>
  <li>Networks.</li> 
  <li>Broadcasting.</li>
  <li>Memory management.</li>
  <li>Graphical user interfaces (GUIs).</li>
  <li>Scripted language support.</li>
</ul>

## Installation and Configuration
<p>Make a new Java project in Eclipse, IntellijIDEA or any other IDE (Integrated Development Environment) 
from this repository.</p>
<p>
   The project will have inside the package "engine" all the required classes to work. To start using the classes,
   reading the project documentation is recommended. Also, is recommended too develop your new applications inside 
   the package "programs", not inside the "engine" package.
</p>

### How to make a new application
1. Make a new class which inherits (extends) from the abstract class "AbstractGame".
2. Implement all the class methods.
3. Make a matching class constructor (it could be private).
4. Inside the main function, make an instance of the class GameContainer. To the constructor of GameContainer, 
instance a new object from your class, and add the title for the application.
5. Call the start method of the object from GameContainer.
6. Compile, build and run your code.

<p>The default size of the window is set to 1080 pixels width, 720 pixels height and 1 for pixel size. 
It is able to set other values with the provided methods in the class GameContainer.</p>

There will be three methods for all applications:
* **initialize**: the user has to create things here.
* **update**: user input management, update or modify objects.
* **render**: draw the program output screen.

Example application:
```java
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
```

## File manifest
<p>Put here all the classes and the relation between them.</p>

## License
<p>There isn't license for the moment.</p>

## Main developer
<p>The main programmer for this project is Sergio Martí Torregrosa.</p>
<p>Thought, all the code is inspired and copied from the PixelGameEngine, develop by David Barr (Javidx9) 
<a>https://github.com/OneLoneCoder</a>. Besides, the One Lone Coder community had help lots.</p>

## Contributing
<p>Pull requests are welcome. For major changes, please open an issue first to discuss what you 
would like to change.</p>
<p>Please make sure to update tests as appropriate.</p>

## Bugs
<p>There are lots of bugs for the moment.</p>
<p>More remarkable: the atoms positions in the 3d molecule is not set fine.</p>

## Problems solutions
<p>There aren't for the moments any issues with the engine core.</p>

## Changes register
<p>Wait to the next update to see a properly summary of all changes between versions.</p>

## News
<p>Date: 28/07/2020</p>

### Working in
Here are the main points where the effort is being made:
* The engine supports image alpha rendering but for now it doesn't work fine at all. Also this makes that the
   rendering goes slow.
* The images now are able to make sprite 2D affine transformations (translation, rotation and scale). 
   In the future, it will be implemented the method to set shear matrices.
* It is being worked in the 3D engine and rendering.
* Orthographic matrix and orthographic 3D rendering.
* In the future, it will be implemented the 2D based-tile collision.

<p>When this is working as intended, the version will be updated to 0.0.02.</p>

### It will work in
In a short while, it will be working in:
* The compatibility with JavaFx.
* Memory management (DAOs).
* Image animations.
* AI.

### Things that there aren't in these moments
* Orthographic 3d rendering.
* Full physics engine.
* Animations.
* Scripting.
* Music management and more complex sounds.
* Artificial intelligence.
* Networks.
* Broadcasting.
* Memory management.
* Graphical User Interfaces (GUIs).
* Scripted language support.

<p>There aren't more news.</p>

## Credits and Acknowledgment
<p>
Big thanks to David Barr and the OneLoneCoder community. Also infinite thanks to my father Rubén Martí Torregrosa 
for teaching me to code and for his endless patience. Finally, I also thank Alexandre Coloma Gisbert as a tutor and 
the entire team of teachers at the CIPFP Batoi for teaching me to code in Java and introduce me to the computing world.
</p>