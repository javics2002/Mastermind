package com.example.desktop;

import com.example.aninterface.Scene;
import com.example.libenginepc.EnginePC;
import com.example.logiclib.InitialScene;

import javax.swing.JFrame;

import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

public class MasterMindDesktop {
    static GraphicsDevice device = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    public static void main(String[] args) {
        final float _aspectRatio = 2f / 3f;
        int _logicHeight = 720;
        final JFrame frame = new JFrame("MasterMind");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boolean fullscreen = true;

        if (fullscreen)
            frame.setUndecorated(true);
        else
            frame.setLocation(300, 20);

        frame.setVisible(true);

        final EnginePC engine = new EnginePC(frame,_aspectRatio,_logicHeight);

        Scene firstScene = new InitialScene(engine);
        engine.setCurrentScene(firstScene);

        engine.resume();
        device.setFullScreenWindow(fullscreen ? frame : null);

        frame.addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent event) {
                frame__windowStateChanged(event, engine);
            }
        });
    }

    public static void frame__windowStateChanged(WindowEvent event, EnginePC engine){
        // minimized
        if ((event.getNewState() & Frame.ICONIFIED) == Frame.ICONIFIED){
            engine.pause();
        }
        // maximized
        else {
            engine.resume();
        }
    }
}
