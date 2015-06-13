public class Coord {
    private int x;
    private int y;

    public Coord() {
        x = 0;
        y = 0;
    }

    public Coord(Coord coord) {
        x = coord.getX();
        y = coord.getY();
    }

    public void set(Coord coord) {
        x = coord.getX();
        y = coord.getY();
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    public boolean equals(Coord coord) {
        if (x == coord.getX() && y == coord.getY()) {
            return true;
        }
        else {
           return false;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
