package com.gmail.rogermoreta.speedpaint;

public class Circulo extends Figura {

	private int radio;

	public Circulo(int id, int x, int y, int radio) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.radio = radio;
	}

	public int getRadio() {
		return radio;
	}
}