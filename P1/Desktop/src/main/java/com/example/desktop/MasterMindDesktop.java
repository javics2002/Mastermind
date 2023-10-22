package com.example.desktop;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.example.aninterface.Scene;
import com.example.logiclib.GameScene;
import com.example.libenginepc.EnginePC;
import com.example.logiclib.InitialScene;

public class MasterMindDesktop  {
    static Dimension WindowSize = new Dimension();

    public static void main(String[] args){
        WindowSize = new Dimension(400, 600);

        final JFrame frame= new JFrame("MasterMind");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIgnoreRepaint(true);
        frame.setVisible(true);

        EnginePC engine = new EnginePC(frame, WindowSize.width, WindowSize.height);
        Scene scene = new GameScene(engine);
        engine.setCurrentScene(scene);
        engine.resume();
    }
}
