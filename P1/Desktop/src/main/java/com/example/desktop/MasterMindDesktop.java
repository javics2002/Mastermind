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

import com.example.logiclib.InitialScene;
import com.example.libenginepc.EnginePC;

public class MasterMindDesktop  {
    static int activePanel = 0;
    static Dimension windowSize = new Dimension();

    public static void main(String[] args){
        //JPanel activePanel = loadPanel(0);

        //final JFrame frame = createFrame("MasterMind");
        final JFrame frame= new JFrame("MasterMind");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIgnoreRepaint(true);
        frame.setVisible(true);





        // Asi deberia de ser la Creación del motor de PC y la escena inicial
        // NO PUEDO CREERME QUE HAYA LLEGADO HASTA AQUI CREANDO COSAS , SOY UN GNIO CHAVALES LLEVO 4 HORAS DESPIERTO HACIENDO ESTO
         EnginePC engine = new EnginePC(frame, 900, 900);     //MOTOR
         InitialScene scene = new InitialScene(engine);      //PRIMERA ESCENA
        engine.setCurrentScene(scene);
        engine.resume();                                //COMIENZA LA EJECUCION

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

               // botonEjemplo.setAlignmentX(Component.LEFT_ALIGNMENT);
                //botonEjemplo.setAlignmentY(Component.CENTER_ALIGNMENT);
                createMouseAdapter(panel);
                panel.repaint();
                break;
            default:
                panel.setBackground(Color.ORANGE);
                break;
        }

        return panel;
    }


    public void addVisualElement(Component component,JPanel activePanel) {
        activePanel.add(component);
    }

    public static void  createMouseAdapter(JPanel activePanel){
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Obtiene las coordenadas del clic del ratón
                int x = e.getX();
                int y = e.getY();

                // Crea un objeto Input.TouchEvent usando las coordenadas y el tipo de evento CLICKED
                Input.TouchEvent event = new Input.TouchEvent(x, y,0, Input.InputType.RELEASED);


            }
        };

        // Asigna el MouseAdapter al componente que deseas rastrear (por ejemplo, un JPanel)
        activePanel.addMouseListener(mouseAdapter);
    }
    public void setActivePanel(int index)
    {
     activePanel=index;
    }
}
