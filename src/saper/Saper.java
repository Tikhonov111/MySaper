package saper;

import saper.Cell;
import saper.GameField;
import saper.Picture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Saper extends JFrame {
    private JPanel panel;
    private JLabel label;
    private final int IMAGE_SIZE = 50;
    private GameField field;
    private boolean stoppedGame, youLose;
    private int lives = 3;

    Saper() {
        field = new GameField();
        setImages();
        initPanel();
        initLabel();
        initFrame();
    }

    private void initLabel() {
        Font font = new Font("name", Font.PLAIN, 25);
        label = new JLabel( "              Welcome to the Game");
        label.setFont(font);
        add(label, BorderLayout.NORTH);

    }

    private void initPanel() {
        panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                List<Cell> list = field.iterateAllCells();
                for (Cell cell : list) {
                    g.drawImage((Image) field.getPic(cell).image, cell.x * IMAGE_SIZE, cell.y * IMAGE_SIZE, this);
                }
            }
        };

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;
                Cell gameObject = field.allCells[x][y];
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        pressLeftButton(x, y);
                        break;
                    case MouseEvent.BUTTON3:
                        pressRightButton(x, y);
                        break;
                }

                panel.repaint();
                actualMessage();
            }
        });

        panel.setPreferredSize(new Dimension(field.getSIDE() * IMAGE_SIZE, field.getSIDE()* IMAGE_SIZE));
        add(panel);
    }

    private void initFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("SAPER");
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }

    private void setImages() {
        for (Picture pic : Picture.values()) {
            pic.image = getImage(pic.name().toLowerCase());
        }
    }

    private Image getImage(String name) {
        String finalname = "/img/" + name + ".png";
        ImageIcon icon =  new ImageIcon(getClass().getResource(finalname));
        return icon.getImage();
    }




    private String getMessage() {
        StringBuilder builder = new StringBuilder();

        if ( winCheck() ) {
            builder.append("        Congratulations you have won!");
        }  if (youLose ) {
            builder.append("                      Game Over");
        }  if (!youLose && !winCheck()) {
            builder.append( "The Game Has Begun! ");
        }
        return builder.toString();
    }

    private void actualMessage() {
        if(lives > 0 && !winCheck())
            label.setText(getMessage() + "            lives: " + lives);
        else {label.setText(getMessage());}
    }

    void openZeroCells(Cell cell) {

        List<Cell> neighbor = field.getNeighbors(cell);
        for (Cell temp : neighbor) {
            if (temp.countNearBombs == 0 && !temp.isOpen) {
                temp.open();
                openZeroCells(temp);

            } else if (temp.countNearBombs > 0 && !temp.isMine) {
                temp.open();
            }
        }
    }

    public void gameOver() {
        youLose = true;
        stoppedGame = true;
        showAllBombs();
    }

    public boolean winCheck() {

        List<Cell> list = field.iterateAllCells();
        field.countOfClosesCell = (field.getSIZE());

        for (Cell temp : list) {

            if (temp.isOpen && !temp.isMine) {
                field.countOfClosesCell--;
            }

            if (field.countOfClosesCell == field.NUMBER_OF_BOMBS) {
                return true;
            }
        }
        return false;
    }

    public void showAllBombs() {
        List<Cell> list = field.iterateAllCells();

        for (Cell temp : list) {
            if (temp.isMine) {
                temp.open();
            }
        }
    }

    public void pressLeftButton(int x, int y) {

        winCheck();

        if (!field.allCells[y][x].isFlag && !stoppedGame) {
            field.allCells[y][x].open();

            if (field.allCells[y][x].countNearBombs == 0 && !field.allCells[y][x].isMine) {
                openZeroCells(field.allCells[y][x]);
            }

            if (field.allCells[y][x].isMine) {
                lives--;
                field.allCells[y][x].finalCell = true;
                if (lives == 0) {
                    gameOver();

                }
            }
        }

        if (winCheck()) {
            stoppedGame = true;
        }
    }

    public void pressRightButton(int x, int y) {
        if (!field.allCells[y][x].isFlag) {
            field.allCells[y][x].flagged();
        } else if (field.allCells[y][x].isFlag) {
            field.allCells[y][x].unflagged();
        }
    }
}
