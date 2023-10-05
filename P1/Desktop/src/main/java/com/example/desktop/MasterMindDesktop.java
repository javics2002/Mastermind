package com.example.desktop;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MasterMindDesktop extends Interface {
    private JFrame renderView;
    private Graphics2D graphics2D;
    public static void main(String[] args){
        this.renderView = new JFrame("MasterMind");
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);
        renderView.setVisible(true);
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawLine(0, 0, 100, 100);
            }
        };

        renderView.add(p);
    }

}