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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.example.logiclib.GameScene;
import com.example.logiclib.InitialScene;
import com.example.libenginepc.EnginePC;

public class MasterMindDesktop  {
    static Dimension windowSize = new Dimension();

    public static void main(String[] args){
        // JPanel activePanel = loadPanel(0);

        // final JFrame frame = createFrame("MasterMind");
        final JFrame frame= new JFrame("MasterMind");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIgnoreRepaint(true);
        frame.setVisible(true);

        EnginePC engine = new EnginePC(frame,500,500);
        GameScene scene = new GameScene(engine);
        engine.setCurrentScene(scene);
        engine.resume();
    }

}
