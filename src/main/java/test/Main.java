package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/**
 * Created by malets on 11/8/2016.
 */
public class Main extends JFrame{

    Main (String s) {
        super(s);

        int width = 800;
        int height = 800;

        this.setSize(new Dimension(width, height));
        this.setLayout(new GridLayout(4, 1));

        final DrawPanel panel = new DrawPanel(width, height/4 - 20);
        add(panel);
        final Thread drawThread = new Thread(panel);

        final Button stopStartButton = new Button("Stop");
        add(stopStartButton);

        stopStartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.setWait(!panel.isWait());
                if (panel.isWait()) {
                    stopStartButton.setLabel("Start");
                } else {
                    stopStartButton.setLabel("Stop");
                }
            }
        });

        final TextField wText = new TextField(6);
        add(wText);

        final Button setWButton = new Button("Set");
        add(setWButton);

        setWButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.setW(Double.valueOf(wText.getText()));
            }
        });

        setVisible(true);
        this.setLocationRelativeTo(null);
        drawThread.start();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new Main("Main");
                frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });
    }


    private class DrawPanel extends JComponent implements Runnable {

        private boolean wait = false;
        private double w = 0.5;
        private int circle = 0;

        private long t = System.nanoTime();
        private int height;
        private int width;

        DrawPanel(int width, int height) {
            super();
            this.width = width;
            this.height = height;
        }

        @Override
        public void run() {
            while (true) {
                if (!wait) {
                    repaint();
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException ignored) {}
            }
        }



        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            long tm = System.nanoTime() - t;
            double angle = tm / 100000000.0 / 1.005;

            int x = (int)angle - circle * width;
            if (x > width) circle++;
            double distance = height / 2 * Math.cos(w * x);

            g2d.drawLine(0, height / 2, width, height / 2);
            g2d.drawOval(x, height / 2 - 3 + (int)distance, 6, 6);
            g2d.fillOval(x, height / 2 - 3 + (int)distance, 6, 6);
        }

        public void setWait(boolean wait) {
            this.wait = wait;
        }

        public boolean isWait() {
            return wait;
        }

        public void setW(double w) {
            this.w = w;
        }
    }
}
