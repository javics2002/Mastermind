package com.example.desktop;

import com.example.aninterface.Scene;
import com.example.libenginepc.EnginePC;
import com.example.logiclib.InitialScene;

import javax.swing.JFrame;


public class MasterMindDesktop {

    public static void main(String[] args) {
        final float _aspectRatio = 2f / 3f;
        int _logicHeight = 720;
        final JFrame frame = new JFrame("MasterMind");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(300, 20);
        frame.setVisible(true);

        EnginePC engine = new EnginePC(frame,_aspectRatio,_logicHeight);

        Scene firstScene = new InitialScene(engine);
        engine.setCurrentScene(firstScene);

        engine.resume();
    }
}
