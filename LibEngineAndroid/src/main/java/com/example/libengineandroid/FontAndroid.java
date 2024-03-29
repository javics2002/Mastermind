package com.example.libengineandroid;

import android.graphics.Typeface;

import com.example.aninterface.Font;

public class FontAndroid implements Font {
	Typeface _font; // La fuente de texto
	float _size; // El tamaño de la fuente

	FontAndroid(Typeface font, float size) {
		_font = font;
		_size = size;
	}

	// Getter para obtener la fuente de texto
	public Typeface getFont() {
		return _font;
	}

	@Override
	public int getFontSize() {
		return (int) _size;
	}
}