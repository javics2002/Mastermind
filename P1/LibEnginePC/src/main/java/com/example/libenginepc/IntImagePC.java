package com.example.libenginepc;
import java.awt.Image;
import com.example.aninterface.IntImage;
public class IntImagePC implements IntImage {

    //Como la clase IntImagen solo tiene dos getters , nos tenemos que basar
    //En la informacion de la imagen
    //Usamos un objeto de la libreria de Java que es Image , lo creamos como un recurso
    //Privado y devolvemos su informacion a partir de metodos publicos

    private Image img;
    IntImagePC(Image img){
        this.img = img;
    }

    //GETTERS
    public Image getImg(){return this.img;}                              //IMAGEN
    @Override
    public int getWidth() {
        return this.img.getWidth(null);
    }    //ANCHO DE LA IMAGEN

    @Override
    public int getHeight() {
        return this.img.getHeight(null);
    }   //ALTO DE LA IMAGEN


}
