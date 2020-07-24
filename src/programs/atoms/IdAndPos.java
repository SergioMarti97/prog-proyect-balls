package programs.atoms;

/**
 * Esta clase sirve para guardar el identificador del
 * átomo y la posición (0, 1, 2 o 3).
 *
 */
public class IdAndPos {

    private int id;

    private int position;

    public IdAndPos(int id, int position) {
        this.id = id;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
