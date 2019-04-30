package dovsyannikov;

import java.util.*;

public class Cell {

    public int y;
    public int x;

    static int[] dXdYNeighbours = {-1, -1, -1, 0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1};

    public Cell(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public Cell() {
        this.y = y;
        this.x = x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (y != cell.y) return false;
        return x == cell.x;
    }

    @Override
    public int hashCode() {
        int result = y;
        result = 31 * result + x;
        return result;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "y=" + y +
                ", x=" + x +
                '}';
    }

    public static Collection<Cell> neighboursCell(Cell c) {

        List<Cell> neighboursCell = List.of(new Cell(c.y - 1, c.x - 1),
                new Cell(c.y - 1, c.x),
                new Cell(c.y - 1, c.x + 1),
                new Cell(c.y, c.x - 1),
                new Cell(c.y, c.x + 1),
                new Cell(c.y + 1, c.x - 1),
                new Cell(c.y + 1, c.x),
                new Cell(c.y + 1, c.x + 1));

        return neighboursCell;
    }
}
