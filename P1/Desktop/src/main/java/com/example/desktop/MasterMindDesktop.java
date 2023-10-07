package com.example.desktop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;

public class MasterMindDesktop implements Engine {
    public static void main(String[] args){
        final JFrame frame;
        frame = new JFrame("MasterMind");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        JButton button = new JButton();
        button.setText("Toni te queremos");
        panel.add(button);
        panel.setPreferredSize(frame.getSize());

        frame.pack();
        frame.setVisible(true);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                Dimension windowSize = componentEvent.getComponent().getSize();
                Dimension gameSize = new Dimension();

                float aspectRatio = 2f / 3f;
                if((float) windowSize.width / (float) windowSize.height < aspectRatio)
                    gameSize.setSize(windowSize.width, (int) (windowSize.width / aspectRatio));
                else
                    gameSize.setSize((int) (windowSize.height * aspectRatio), windowSize.height);

                int bandWidth = (windowSize.width - gameSize.width) / 2;
                int bandHeight = (windowSize.height - gameSize.height) / 2;

                frame.getContentPane().removeAll();
                frame.add(Box.createHorizontalStrut(bandWidth), BorderLayout.WEST);
                frame.add(Box.createHorizontalStrut(bandWidth), BorderLayout.EAST);
                frame.add(Box.createVerticalStrut(bandHeight), BorderLayout.NORTH);
                frame.add(Box.createVerticalStrut(bandHeight), BorderLayout.SOUTH);
                frame.add(panel, BorderLayout.CENTER);

                frame.revalidate();
                frame.repaint();
            }
        });
    }

    @Override
    public com.example.aninterface.Graphics getGraphics() {
        return null;
    }

    @Override
    public Input getInput() {
        return null;
    }

    @Override
    public Audio getAudio() {
        return null;
    }
}