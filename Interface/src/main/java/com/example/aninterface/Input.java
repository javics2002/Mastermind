package com.example.aninterface;

public interface Input {
	enum InputType {PRESSED, RELEASED, MOVE}

	class TouchEvent {
		public int x, y;
		public InputType type;

		public TouchEvent(int x_, int y_, InputType type_) {
			x = x_;
			y = y_;
			type = type_;
		}
	}
}
