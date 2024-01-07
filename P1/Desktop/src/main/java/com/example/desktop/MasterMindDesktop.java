package com.example.desktop;

import com.example.aninterface.Scene;
import com.example.libenginepc.EnginePC;
import com.example.logiclib.InitialScene;

import javax.swing.JFrame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class MasterMindDesktop {
    static GraphicsDevice device = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    public static void main(String[] args) {
        final float _aspectRatio = 2f / 3f;
        int _logicHeight = 720;
        final JFrame frame = new JFrame("MasterMind");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boolean fullscreen = false;

        if (fullscreen)
            frame.setUndecorated(true);
        else
            frame.setLocation(300, 20);

        frame.setVisible(true);
        EnginePC engine = new EnginePC(frame,_aspectRatio,_logicHeight);

        Scene firstScene = new InitialScene(engine);
        engine.setCurrentScene(firstScene);

        engine.resume();
        device.setFullScreenWindow(fullscreen ? frame : null);
    }
}
