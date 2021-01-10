package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface extends JFrame implements ActionListener {
    private JButton startAndPauseBtn;

    public UserInterface() {
        super("Game Of Life");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 500));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());

        initializeComponents();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
    }

    private void initializeComponents() {
        startAndPauseBtn = new JButton("Start");
        startAndPauseBtn.setActionCommand("action");
        startAndPauseBtn.addActionListener(this);

        add(startAndPauseBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("action")) {
            if (startAndPauseBtn.getText() == "Start") {
                startAndPauseBtn.setText("Pause");
            } else {
                startAndPauseBtn.setText("Start");
            }
        }
    }
}
