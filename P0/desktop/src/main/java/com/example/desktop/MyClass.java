package com.example.desktop;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Button;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Canvas;
import java.awt.Graphics2D;

public class MyClass {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Frame");

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Panel panel = new Panel();

//        final TextField text = new TextField(20);
//        panel.add(text);
//
//        Button button = new Button("Send");
//        panel.add(button);
//        MouseListener mouseListener = new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent mouseEvent) {
//                JOptionPane.showMessageDialog(frame, text.getText());
//            }
    //        @Override
    //        public void mousePressed(MouseEvent mouseEvent) {
    //
    //        }
    //
    //        @Override
    //        public void mouseReleased(MouseEvent mouseEvent) {
    //
    //        }
    //
    //        @Override
    //        public void mouseEntered(MouseEvent mouseEvent) {
    //
    //        }
    //
    //        @Override
    //        public void mouseExited(MouseEvent mouseEvent) {
    //
    //        }
//        };
//        button.addMouseListener(mouseListener);

        final Canvas canvas = new Canvas();
        canvas.setSize(frame.getWidth(), frame.getHeight());
        canvas.setBackground(Color.white);

        Button clearButton = new Button("Clear");
        panel.add(clearButton);
        MouseListener clearListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Graphics g = canvas.getGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        };
        clearButton.addMouseListener(clearListener);

        Button drawButton = new Button("Draw");
        panel.add(drawButton);
        MouseListener drawListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Graphics g = canvas.getGraphics();
                g.setColor(Color.BLACK);
                int radius = 20;
                g.fillOval(canvas.getWidth() / 2 - radius, canvas.getHeight() / 2 - radius, 2 * radius, 2 * radius);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        };
        drawButton.addMouseListener(drawListener);

        panel.add(canvas);

        frame.setContentPane(panel);

    }
}