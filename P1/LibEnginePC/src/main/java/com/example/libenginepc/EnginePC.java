package com.example.libenginepc;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.State;
public class EnginePC implements Engine {
    private JFrame myView;           //VENTANA
    private Thread renderThread;    //HILO DE RENDER
    private boolean running;        //BOOLEANO PARA SABER SI LA APLIACION ESTA CORRIENDO

    private State currentScene;    //ESCENA ACTUAL
    //private GraphicsPC graphics;    //GRAPHICS QUE DIBUJA
}