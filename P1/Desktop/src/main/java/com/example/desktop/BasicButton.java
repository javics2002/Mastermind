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
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.example.aninterface.Interface;

public class BasicButton implements Interface {

    private int x;
    private int y;
    private int w;
    private int h;
    static BufferedImage buttonImage;
    static Graphics2D graficos;
    BasicButton(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        //Tratamos de leer el archivo imagen y si no lanzamos la excepcion
        try {
            buttonImage = ImageIO.read(new File("button.png"));
        } catch (IOException e) {
            e.printStackTrace();

        }

        graficos=buttonImage.createGraphics();

    }

    @Override
    public void render( ) {
        //Metodo de renderizado del boton
        graficos.drawImage(buttonImage, x, y, w, h, null);

    }
    @Override
    public void update() { }
    @Override
    public boolean handleEvent(Input.TouchEvent e)
    {
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        int x = pointerInfo.getLocation().x;
        int y = pointerInfo.getLocation().y;
        System.out.println("Coordenadas del ratón: (" + x + ", " + y + ")");
        return true;
    }

    /*//Ejemplos de limites
        int LimIzq= 20;
        int LimDcho= 40;

        int LimTop=20;
        int LimBot=100;
        //Como para ver si cae en lo que es el boton
        if(e.type== Input.InputTouchType.PRESSED && (e.x>=LimIzq && <= LimDcho)&&(e.y>=LimTop && e.y<=LimBot))
        {
            //Ejecutar alguna accion si estamos dentro de las coordenadas
            System.out.print("ESTAS ENCIMA DE MIIII");

            return true; //Como hemos podido manejar el evento devolvemos true

        }
        return false; //No hemos podido manejar el evento*/

}