package com.example.libenginepc;
import com.example.aninterface.Font;


public class FontPC implements Font {
    java.awt.Font _font;
    FontPC(java.awt.Font font){
        _font = font;
    }
    public java.awt.Font getFont(){
        return _font;
    }
    @Override
    public int getFontSize() {
        return _font.getSize();
    }
}
