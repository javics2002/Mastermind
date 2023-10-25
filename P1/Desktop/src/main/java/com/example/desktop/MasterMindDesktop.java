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
        float aspectRatio = 2f / 3f;
        int height = 720;
        int titleBarHeight = 30;
        WindowSize = new Dimension((int) (height * aspectRatio), height + titleBarHeight);

        final JFrame frame= new JFrame("MasterMind");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(300, 20);
        frame.setVisible(true);

        EnginePC engine = new EnginePC(frame, WindowSize.width, WindowSize.height);
        //Scene scene = new InitialScene(engine);

        //Estos son los valores maximos que permito
        //Scene scene = new GameScene(engine, 14, 6, 10);
        Scene scene = new GameScene(engine, 5, 6, 10);
        engine.set_currentScene(scene);
        engine.resume();
    }
}
