package ui;

import model.core.Matrix;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserInterface extends JFrame implements ActionListener {
    private Matrix matrix;
    private int height;
    private int width;
    private int sizeOfCell;
    private int upperCorner;
    private int leftCorner;
    private Boolean update;
    private JButton startAndPauseBtn;
    private JButton saveBtn;
    private JLabel widthFieldText;
    private JTextField widthField;
    private JLabel heightFieldText;
    private JTextField heightField;

    public UserInterface() {
        super("Game Of Life");
        height = 30;
        width = 33;
        sizeOfCell = 16;
        upperCorner = 95;
        leftCorner = 20;
        matrix = new Matrix(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(570, 600));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());

        initializeComponents();

        matrix.changeElement(2,2);
        matrix.changeElement(2,2);
        matrix.changeElement(3,2);
        matrix.changeElement(2,3);
        matrix.changeElement(3,3);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        DrawingMouseListener dml = new DrawingMouseListener();
        addMouseListener(dml);
        addMouseMotionListener(dml);
        update = false;
    }

    private void initializeComponents() {
        startAndPauseBtn = new JButton("Start");
        startAndPauseBtn.setActionCommand("action");
        startAndPauseBtn.addActionListener(this);
        add(startAndPauseBtn);
/*
        widthFieldText = new JLabel("width:");
        widthField = new JTextField(5);
        add(widthFieldText);
        add(widthField);

        heightFieldText = new JLabel("height:");
        heightField = new JTextField(5);
        add(heightFieldText);
        add(heightField);


 */
        saveBtn = new JButton("Apply");
        saveBtn.setActionCommand("apply");
        saveBtn.addActionListener(this);

        add(saveBtn);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("action")) {
            if (startAndPauseBtn.getText() == "Start") {
                startAndPauseBtn.setText("Pause");
                update = false;
            } else {
                startAndPauseBtn.setText("Start");
                update = true;
            }
        }
        if (e.getActionCommand().equals("apply")) {
            matrix.generateNext();
            updatePlot();
        }
    }

    private void updatePlot() {
        paintComponents(getGraphics());
        updateGraph(getGraphics());
    }

    private class DrawingMouseListener extends MouseAdapter {
        int curY;
        int curX;

        public void mousePressed(MouseEvent e) {
            handleMousePressed(e.getPoint());
        }

        public void mouseDragged(MouseEvent e) {
            handleMouseDragged(e.getPoint());
        }

        public void mouseReleased(MouseEvent e) {
            curX = -1;
            curY = -1;
        }

        private void handleMouseDragged(Point point) {
            int ri = Math.round((point.y - upperCorner)/sizeOfCell);
            int i = Math.round((point.x - leftCorner)/sizeOfCell);
            if (curX != i || curY != ri) {
                handleMousePressed(point);
            }
        }

        private void handleMousePressed(Point point) {
            int ri = Math.round((point.y - upperCorner)/sizeOfCell);
            int i = Math.round((point.x - leftCorner)/sizeOfCell);
            matrix.changeElement(i, ri);
            updatePlot();
        }
    }

    private void updateGraph(Graphics g) {
        for (int ri = 0; ri < height; ri++) {
            for (int i = 0; i < width; i++) {
                g.drawRect(leftCorner+sizeOfCell*i, upperCorner+sizeOfCell*ri, sizeOfCell, sizeOfCell);
                if (matrix.getElement(i, ri) == 1) {
                    g.fillRect(leftCorner+sizeOfCell*i, upperCorner+sizeOfCell*ri, sizeOfCell, sizeOfCell);
                }
            }
        }
    }
}
