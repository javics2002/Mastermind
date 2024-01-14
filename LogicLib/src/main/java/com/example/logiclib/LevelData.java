package com.example.logiclib;

import java.io.Serializable;
import java.util.ArrayList;

// Clase que contiene toda la información de los niveles usada para el guardado del juego.
// Además, contiene información específica de cada nivel que permite reconstruir una escena de juego
// (cuando se cierra el juego mientras se juega a un nivel)
public class LevelData implements Serializable {
	public int reward;
	public int codeSize;
	public int codeOpt;
	public boolean repeat;
	public int attempts;

	public ArrayList<Combination> combinations;
	public Combination currentCombination;
	public Combination resultCombination;
	public int leftAttemptsNumber;
	public int initialAttemptsNumber;
	public int worldID;
	public int levelID;

	@Override
	public String toString() {
		return "LevelData[reward_" + reward + "_codeSize_" + codeSize + "_codeOpt_" + codeOpt + "_repeat_" + repeat + "_attempts_" + attempts +
				"_leftAttemptsNumber_" + leftAttemptsNumber + "_initialAttemptsNumber_" + initialAttemptsNumber + "_combinationsSize_" + combinations.size() +
				"_resultCombinationSize_" + resultCombination.getColors().length + "_currentCombinationSize_" + currentCombination.getColors().length
				+ "_worldID_" + worldID + "_levelID_" + levelID + "]";
	}
}
