package saper;

import saper.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameField {

    private final int SIDE = 9;
    private final int SIZE = SIDE * SIDE;
    Cell[][] allCells = new Cell[SIDE][SIDE];
    final int NUMBER_OF_BOMBS = 11;
    Random random = new Random();
    int countOfClosesCell = SIZE;

    public GameField() {
        createGame();
    }

    private void createGame() {   // Создаёт игровое поле и разбрасывает мины в случайном порядке
        int x, y;
        boolean isMine = false;
        int countMines = 0;
        int countNearBombs = 0;
        for (y = 0; y < SIDE; y++) {
            for (x = 0; x < SIDE; x++) {
                allCells[y][x] = new Cell(x, y);
            }
        }

        while (countMines < NUMBER_OF_BOMBS) {
            x = random.nextInt(SIDE);
            y = random.nextInt(SIDE);

            if (!allCells[y][x].isMine) {     //   Исключает попадание двух бомб в одну клетку, чтобы их количество всегда cовпадало со значением переменной NUMBER_OF_BOMBS
                allCells[y][x].mine();
                countMines++;
            }
        }
        countNearBombs();

    }

    List<Cell> getNeighbors(Cell gameObject) {   // Обходит всех соседей переданной в аргументе клетки
        java.util.List<Cell> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (allCells[y][x] == gameObject) {
                    continue;
                }
                result.add(allCells[y][x]);
            }
        }
        return result;
    }

    private void countNearBombs() {             // Подсчёт мин в смежных полях!
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                Cell gameObject = allCells[y][x];
                if (!gameObject.isMine) {
                    for (Cell neighbor : getNeighbors(gameObject)) {
                        if (neighbor.isMine) {
                            gameObject.countNearBombs++;
                        }
                    }
                }
            }
        }
    }

    List<Cell> iterateAllCells() {
        List<Cell> list = new ArrayList<>();
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                Cell gameObject = allCells[y][x];
                list.add(gameObject);
            }
        }
        return list;
    }

    Picture getPic(Cell cell) {

        if (!cell.isOpen) {
            if (!cell.isFlag) {
                return Picture.CLOSED;
            }
            if (cell.isFlag == true) {
                return Picture.FLAGED;
            }
        }

        if (cell.isOpen) {
            if (cell.isMine && cell.finalCell) {
                return Picture.BOMBED;
            }

            if (cell.isMine && !cell.finalCell) {
                return Picture.BOMB;

            }

            if (cell.countNearBombs == 0 && !cell.isMine) {
                return Picture.ZERO;

            }

            if (cell.countNearBombs > 0) {
                if (cell.countNearBombs == 1) {
                    return Picture.NUM1;

                }
                if (cell.countNearBombs == 2) {
                    return Picture.NUM2;
                }
                if (cell.countNearBombs == 3) {
                    return Picture.NUM3;
                }
                if (cell.countNearBombs == 4) {
                    return Picture.NUM4;
                }
                if (cell.countNearBombs == 5) {
                    return Picture.NUM5;
                }
                if (cell.countNearBombs == 6) {
                    return Picture.NUM6;
                }
                if (cell.countNearBombs == 7) {
                    return Picture.NUM7;
                }

                if (cell.countNearBombs == 8) {
                    return Picture.NUM8;
                }
            }
        }
        return null;
    }

    public int getSIZE() {
        return SIZE;
    }
    public int getSIDE() {
        return SIDE;
    }
}
