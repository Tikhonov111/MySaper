package saper;

public class Cell {

    int x;
    int y;
    boolean isMine, isFlag;
    boolean isOpen, finalCell;
    int countNearBombs;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
        this.isOpen = isOpen;
        this.isFlag = isFlag;
        this.countNearBombs = countNearBombs;
        this.finalCell = finalCell;
    }

    void mine() {
        isMine = true;
    }

    void open() {
        isOpen = true;
    }

    void flagged() {
        isFlag = true;
    }

    void unflagged() {
        isFlag = false;
    }
}
