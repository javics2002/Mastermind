package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;

public class Text extends GameObject {
    private String _text;
    private final int _color;
    private Font _font;
    private final boolean _centered;

	Text(String text, Font font, Engine engine, int posX, int posY, int color, boolean centered) {
		super(engine, posX, posY, 0, 0, 1f);

		_centered = centered;

		_text = text;
		_font = font;

        _width = _graphics.getStringWidth(_text, _font);
		_height = _graphics.getStringHeight(_text, _font);

		_color = color;
	}

	@Override
	public void render(Graphics graphics) {
		if (_centered)
			_graphics.drawText(_text, _font, _positionX - _graphics.getStringWidth(_text, _font) / 2f,
					_positionY - _graphics.getStringHeight(_text, _font) / 2f, _scale, _color);
		else
			_graphics.drawText(_text, _font, _positionX, _positionY, _scale, _color);
	}

	@Override
	public void update(double deltaTime) {
	}

	@Override
	public boolean handleEvents(Input.TouchEvent e) {
		return false;
	}

	public void setText(String newText) {
		_text = newText;
	}

	public String getText() {
		return _text;
	}

	public Font getFont() {
		return _font;
	}
}
