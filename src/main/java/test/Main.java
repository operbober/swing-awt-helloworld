package test;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by malets on 11/8/2016.
 */
public class Main extends JFrame{

    Main (String s) {
        super(s);

        int width = 300;
        int height = 300;

        DrawPanel panel = new DrawPanel(width, height);
        panel.setPreferredSize(new Dimension(width, height));
        add(panel);
        pack();
        setVisible(true);
        this.setLocationRelativeTo(null);
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

        private long t = System.nanoTime();
        private int height;
        private int width;

        DrawPanel(int width, int height) {
            super();
            this.width = width;
            this.height = height;
            new Thread(this).start();
        }

        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException ignored) {}
            }
        }

        private double w = 0.5;
        private int circle = 0;

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
    }
}
