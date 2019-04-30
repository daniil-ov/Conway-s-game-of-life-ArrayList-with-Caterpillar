package dovsyannikov;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static dovsyannikov.Cell.dXdYNeighbours;

public class Life {

    private static int widthField = 5000;
    private static int heightField = 5000;

    private static int centerX = 2100;
    private static int centerY = -2100;

    public static ArrayList<ArrayList<Cell>> Life = new ArrayList<>(330730);

    static String fileName = "caterpillar.rle";
    //static String fileName = "glader.rle";

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(700, 700);
        StdDraw.setPenColor(Color.BLACK);

        createField();
        runLife();
    }


    private static void createField() {

        ReadFile.read(fileName, Charset.defaultCharset());
    }


    private static void runLife() {

        while (true) {

            drawCurrentLife();
            change();
            moveField();

            //задержка 0.5 сек
            //StdDraw.pause(500);
        }
    }


    private static void moveField() {

        char key = ' ';

        if (StdDraw.hasNextKeyTyped()) {

            key = StdDraw.nextKeyTyped();
        }

        if (widthField > 2 && widthField < 16000000) {

            switch (key) {

                case '-':
                    widthField *= 2;
                    heightField *= 2;
                    break;

                case '+':
                    widthField /= 2;
                    heightField /= 2;
                    break;

                case 's':
                    centerY -= heightField / 3;
                    break;

                case 'w':
                    centerY += heightField / 3;
                    break;

                case 'a':
                    centerX -= widthField / 3;
                    break;

                case 'd':
                    centerX += widthField / 3;
                    break;
            }
        }
    }


    private static void drawCurrentLife() {

        long start = System.currentTimeMillis();

        StdDraw.clear();

        StdDraw.setXscale(centerX - (widthField / 2), centerX + (widthField / 2));
        StdDraw.setYscale(centerY - (heightField / 2), centerY + (heightField / 2));

        for (ArrayList strings : Life) {
            for (Object tmpCell : strings) {
                Cell tmpC = (Cell) tmpCell;
                StdDraw.filledSquare(tmpC.x, tmpC.y, 0.000001);
            }
        }

        StdDraw.show();

        long finish = System.currentTimeMillis();

        System.out.println("Время отрисовки: " + (finish - start));
    }


    private static int countNeighbours(Cell c, int indexString) {

        int cnt = 0;
        Cell tmpCell = new Cell();

        for (int i = 0; i < 6; i += 2) {

            tmpCell.y = c.y + dXdYNeighbours[i];
            tmpCell.x = c.x + dXdYNeighbours[i + 1];

            if (Life.get(indexString + 1).contains(tmpCell)) {

                cnt++;
            }
        }

        for (int i = 6; i < 9; i += 2) {

            tmpCell.y = c.y + dXdYNeighbours[i];
            tmpCell.x = c.x + dXdYNeighbours[i + 1];

            if (Life.get(indexString).contains(tmpCell)) {

                cnt++;
            }
        }

        for (int i = 10; i < 15; i += 2) {

            tmpCell.y = c.y + dXdYNeighbours[i];
            tmpCell.x = c.x + dXdYNeighbours[i + 1];

            if (Life.get(indexString - 1).contains(tmpCell)) {

                cnt++;
            }
        }

        return cnt;
    }


    private static void change() {

        long start = System.currentTimeMillis();

        Set<Cell> bufferForRevivalFirstString = new HashSet<>();
        Set<Cell> bufferStringCurrentString = new HashSet<>();
        Set<Cell> bufferStringNextString = new HashSet<>();

        if (!checkFutureLifeForFirstString() && Life.get(1).isEmpty()) {
            //System.out.println("В первой строке жизнь не возродится и первая строка пустая!");

            if (!(Life.get(Life.size() - 4).isEmpty())) {
                Life.add(new ArrayList<>());
            }

            if (Life.get(2).isEmpty()) {
                Life.remove(2);
            }

            //System.out.println(Life.toString());

            processLife(bufferStringCurrentString, bufferStringNextString);

        } else {

            //System.out.println("+В первой строке возродится жизнь!");

            Life.add(new ArrayList<>());
            if (!(Life.get(Life.size() - 4).isEmpty())) {
                Life.add(Life.size(), new ArrayList<>());
            }

            if (Life.get(Life.size() - 5).isEmpty()) {
                Life.remove(Life.size() - 5);
            }

            //System.out.println(Life.toString());

            processLifeWithRevivalFirstString(bufferForRevivalFirstString, bufferStringCurrentString, bufferStringNextString);
        }

        long finish = System.currentTimeMillis();

        System.out.println("Время рассчета след. поколения: " + (finish - start));
    }


    private static void processLife(Set<Cell> buf1, Set<Cell> buf2) {

        for (int IndexString = 2; IndexString < Life.size() - 1; IndexString++) {

            if (IndexString % 2 == 0) {

                handlerString(buf1, IndexString);

            } else {

                handlerString(buf2, IndexString);
            }
        }
    }


    private static void processLifeWithRevivalFirstString(Set<Cell> buf0, Set<Cell> buf1, Set<Cell> buf2) {

        for (int IndexString = 1; IndexString < Life.size() - 1; IndexString++) {

            if (IndexString % 3 == 0) {

                handlerString(buf2, IndexString);

            } else if (IndexString % 3 == 2) {

                handlerString(buf1, IndexString);

            } else {

                handlerString(buf0, IndexString);
            }
        }
    }


    private static void handlerString(Set<Cell> buf, int indexString) {

        swapBuffer(buf, indexString);

        for (Cell tmpC : Life.get(indexString)) {

            int cntCellNeighbors = countNeighbours(tmpC, indexString);

            if ((cntCellNeighbors == 3) || (cntCellNeighbors == 2)) {
                buf.add(tmpC);
            }
        }

        Set<Cell> tmpGetNeighborhood = getANeighborhoodFromTheUpperAndLowerNeighbors(indexString);

        for (Cell tmpC : tmpGetNeighborhood) {
            buf.add(tmpC);
        }
    }


    private static void swapBuffer(Set<Cell> buf, int indexStringForSwap) {

        if (indexStringForSwap - 2 >= 0) {

            ArrayList<Cell> tmpAL = new ArrayList<>(buf);
            Life.set(indexStringForSwap - 2, tmpAL);

            buf.clear();
        }
    }


    private static Set<Cell> getANeighborhoodFromTheUpperAndLowerNeighbors(int IndexString) {

        Set<Cell> tmpCellsForRevivalCurrentString = new HashSet<>();

        Cell tmpCell = new Cell();

        for (Cell tmpC : Life.get(IndexString - 1)) {

            for (int i = 0; i < 6; i += 2) {

                tmpCell.y = tmpC.y + dXdYNeighbours[i];
                tmpCell.x = tmpC.x + dXdYNeighbours[i + 1];

                if (countNeighbours(tmpCell, IndexString) == 3) {

                    tmpCellsForRevivalCurrentString.add(new Cell(tmpCell.y, tmpCell.x));
                }
            }
        }

        for (Cell tmpC : Life.get(IndexString + 1)) {

            for (int i = 10; i < 15; i += 2) {

                tmpCell.y = tmpC.y + dXdYNeighbours[i];
                tmpCell.x = tmpC.x + dXdYNeighbours[i + 1];

                if (countNeighbours(tmpCell, IndexString) == 3) {

                    tmpCellsForRevivalCurrentString.add(new Cell(tmpCell.y, tmpCell.x));
                }
            }
        }

        return tmpCellsForRevivalCurrentString;
    }


    private static boolean checkFutureLifeForFirstString() {

        boolean checkLife = false;

        Cell tmpCell = new Cell();

        for (Cell tmpC : Life.get(2)) {

            //проверем верхних соседей 2 строчки
            for (int i = 10; i < 15; i += 2) {

                tmpCell.y = tmpC.y + dXdYNeighbours[i];
                tmpCell.x = tmpC.x + dXdYNeighbours[i + 1];

                if (countNeighbours(tmpCell, 1) == 3) {

                    return !checkLife;
                }
            }
        }

        tmpCell = null;

        return checkLife;
    }
}