package com.example.desktop;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.example.aninterface.Scene;
import com.example.logiclib.GameScene;
import com.example.libenginepc.EnginePC;
import com.example.logiclib.InitialScene;

public class MasterMindDesktop  {
    static Dimension WindowSize = new Dimension();

    public static void main(String[] args) {
        final JFrame frame= new JFrame("MasterMind");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(300, 20);
        frame.setVisible(true);

        EnginePC engine = new EnginePC(frame);

        InitialScene scene = new InitialScene(engine);
        engine.setCurrentScene(scene);
        engine.resume();
    }
}
