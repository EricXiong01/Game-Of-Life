package ui;

import model.core.Matrix;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class UserInterface extends JFrame implements ActionListener {
    private Matrix matrix;
    private int height;
    private int width;
    private int sizeOfCell;
    private int upperCorner;
    private int leftCorner;
    private JButton startAndPauseBtn;
    private JButton saveBtn;
    private JButton clearBtn;
    private Timer timer;
    private Boolean enableDraw;

    //EFFECTS: construct the panel and initialize the UI
    public UserInterface() {
        super("Game Of Life");
        height = 30;
        width = 33;
        sizeOfCell = 16;
        upperCorner = 95;
        leftCorner = 20;
        enableDraw = false;
        setMatrix();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(570, 600));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());

        initializeComponents();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        DrawingMouseListener dml = new DrawingMouseListener();
        addMouseListener(dml);
        addMouseMotionListener(dml);
    }

    //MODIFIES: this
    //EFFECTS: reset the matrix
    private void setMatrix() {
        matrix = new Matrix(width, height);
    }

    //MODIFIES: this
    //EFFECTS: initialize and add components to the panel
    private void initializeComponents() {
        saveBtn = new JButton("Next");
        saveBtn.setActionCommand("apply");
        saveBtn.addActionListener(this);
        add(saveBtn);

        startAndPauseBtn = new JButton("Start");
        startAndPauseBtn.setActionCommand("action");
        startAndPauseBtn.addActionListener(this);
        add(startAndPauseBtn);

        clearBtn = new JButton("Clear");
        clearBtn.setActionCommand("clear");
        clearBtn.addActionListener(this);
        add(clearBtn);
    }

    @Override
    //EFFECTS: display the welcome screen
    public void paint(Graphics g) {
        g.drawString("This is the Game Of Life, a Turing complete game that can simulate any", 50, 100);
        g.drawString("Turing machine.", 50, 120);
        g.drawString("There are three rules:", 50, 140);
        g.drawString("1. Any live cell with two or three live neighbours survives.", 70, 160);
        g.drawString("2. Any dead cell with three live neighbours becomes a live cell.", 70, 180);
        g.drawString("3. All other live cells die in the next generation.", 70, 200);
        g.drawString("   Similarly, all other dead cells stay dead.", 70, 220);
        g.drawString("Filled blocks represent live cells while empty blocks represent dead cells.", 50, 250);
        g.drawString("Clicking or dragging on the cell change the cell to the other type.", 50, 270);
        g.drawString("Press \"Next\" to enter the next generation.", 50, 290);
        g.drawString("Press \"Start\" to automatically update the generation.", 50, 310);
        g.drawString("Press \"Clear\" to clear everything.", 50, 330);
        g.drawString("Now, press \"Next\" to begin", 200, 450);
    }

    @Override
    //MODIFIES: this
    //EFFECTS: act to clicking of buttons
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("action")) { //toggle auto-update of screen
            startAndPauseAction();
        }
        if (e.getActionCommand().equals("apply")) { //update to the next screen
            matrix.generateNext();
            updateWholeGraph();
            enableDraw = true;
        }
        if (e.getActionCommand().equals("clear")) { //clear the screen
            setMatrix();
            matrix.generateNext();
            updateWholeGraph();
            startAndPauseAction();
        }
    }

    //MODIFIES: this
    //EFFECTS: toggle auto-update of matrix and screen (off to on, and vice versa)
    private void startAndPauseAction() {
        if (startAndPauseBtn.getText().equals("Start")) {
            startAndPauseBtn.setText("Pause");
            updateWholeGraph();
            timer = clock();
        } else {
            startAndPauseBtn.setText("Start");
            timer.cancel();
        }
    }

    //MODIFIES: this
    //EFFECTS: setup and return a timer that will update after certain intervals
    private Timer clock() {
        Timer timer = new Timer();
        timer.schedule(new UpdateTask(), 600, 600);
        return timer;
    }

    private class UpdateTask extends TimerTask {
        @Override
        //MODIFIES: this
        //EFFECTS: update the matrix and screen
        public void run() {
            matrix.generateNext();
            updateConvenientGraph(getGraphics());
        }
    }

    //MODIFIES: this
    //EFFECTS: clear the screen and generate the screen
    private void updateWholeGraph() {
        paintComponents(getGraphics());
        updateFullGraphLayer(getGraphics());
    }

    private class DrawingMouseListener extends MouseAdapter {
        int curY;
        int curX;
        Boolean isClicked;

        //MODIFIES: this
        //EFFECTS: change the matrix (0 to 1, and vice versa) and update the screen when pressed
        public void mousePressed(MouseEvent e) {
            handleMousePressed(e.getPoint());
            isClicked = true;
        }

        //MODIFIES: this
        //EFFECTS: change the matrix (0 to 1, and vice versa) and update the screen when dragged
        public void mouseDragged(MouseEvent e) {
            handleMouseDragged(e.getPoint());
        }

        //MODIFIES: this
        //EFFECTS: reset the behaviour and initialize when mouse released
        public void mouseReleased(MouseEvent e) {
            curX = -1;
            curY = -1;
        }

        //MODIFIES: this
        //EFFECTS: change the matrix (0 to 1, and vice versa) and update the screen with continuous point inputs
        private void handleMouseDragged(Point point) {
            int ri = ((point.y - upperCorner) / sizeOfCell);
            int i = ((point.x - leftCorner) / sizeOfCell);
            if (curX != i || curY != ri) {
                if (!isClicked) {
                    handleMousePressed(point);
                }
                curY = ri;
                curX = i;
                isClicked = false;
            }
        }

        //MODIFIES: this
        //EFFECTS: change the matrix (0 to 1, and vice versa) and update the screen at given point
        private void handleMousePressed(Point point) {
            if (enableDraw) {
                int ri = ((point.y - upperCorner) / sizeOfCell);
                int i = ((point.x - leftCorner) / sizeOfCell);
                if (0 <= ri && 0 <= i && ri < height && i < width) {
                    matrix.changeElement(ri, i);
                    if (matrix.getElement(ri, i) == 1) {
                        getGraphics().fillRect(leftCorner + sizeOfCell * i, upperCorner + sizeOfCell * ri, sizeOfCell, sizeOfCell);
                    } else {
                        getGraphics().clearRect(leftCorner + sizeOfCell * i, upperCorner + sizeOfCell * ri, sizeOfCell, sizeOfCell);
                        getGraphics().drawRect(leftCorner + sizeOfCell * i, upperCorner + sizeOfCell * ri, sizeOfCell, sizeOfCell);
                    }
                }
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: generate the screen from beginning
    private void updateFullGraphLayer(Graphics g) {
        for (int ri = 0; ri < height; ri++) {
            for (int i = 0; i < width; i++) {
                g.drawRect(leftCorner + sizeOfCell * i, upperCorner + sizeOfCell * ri, sizeOfCell, sizeOfCell);
                if (matrix.getElement(ri, i) == 1) {
                    g.fillRect(leftCorner + sizeOfCell * i, upperCorner + sizeOfCell * ri, sizeOfCell, sizeOfCell);
                }
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: update the parts of the screen that needs to be updated
    private void updateConvenientGraph(Graphics g) {
        int[][] differenceMatrix = matrix.difference();
        for (int ri = 0; ri < height; ri++) {
            for (int i = 0; i < width; i++) {
                if (differenceMatrix[ri][i] == 1) {
                    g.fillRect(leftCorner + sizeOfCell * i, upperCorner + sizeOfCell * ri, sizeOfCell, sizeOfCell);
                }
                if (differenceMatrix[ri][i] == -1) {
                    g.clearRect(leftCorner + sizeOfCell * i, upperCorner + sizeOfCell * ri, sizeOfCell, sizeOfCell);
                    g.drawRect(leftCorner + sizeOfCell * i, upperCorner + sizeOfCell * ri, sizeOfCell, sizeOfCell);
                }
            }
        }
    }
}
