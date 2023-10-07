package com.example.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.tools.Tool;

import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;

public class MasterMindDesktop implements Engine {
    static int activePanel = 0;
    static Dimension windowSize = new Dimension();

    public static void main(String[] args){
        JPanel activePanel = loadPanel(0);

        final JFrame frame = createFrame("MasterMind");
    }

    static final JFrame createFrame(String frameTitle){
        final JFrame frame;
        frame = new JFrame(frameTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.pack();
        frame.setVisible(true);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                windowSize = componentEvent.getComponent().getSize();
                updateFrame(frame);
            }
        });

        return frame;
    }

    private static void updateFrame(final JFrame frame) {
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
        frame.add(loadPanel(activePanel), BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }

    static JPanel loadPanel(int index){
        final JPanel panel = new JPanel();
        switch (index){
            case 0: //Title panel
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                JLabel titleLabel = new JLabel("Master Mind");
                titleLabel.setFont(new Font("DAGGERSQUARE", Font.PLAIN, 48));
                titleLabel.setSize(200, 300);
                titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                titleLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
                titleLabel.setBorder(new EmptyBorder(200, 0, 100, 0));
                panel.add(titleLabel);

                JButton playButton = new JButton();
                playButton.setText("Jugar");
                playButton.setFont(new Font("DAGGERSQUARE", Font.PLAIN, 24));
                playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                playButton.setAlignmentY(Component.CENTER_ALIGNMENT);
                //playButton.setIcon(new ImageIcon("C:\\Users\\Javier\\Desktop\\a.png"));
                playButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        activePanel = 1;
                        updateFrame((JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource()));
                    }
                });
                panel.add(playButton);
                break;
            default:
                panel.setBackground(Color.ORANGE);
                break;
        }

        return panel;
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