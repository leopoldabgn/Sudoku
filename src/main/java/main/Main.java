package main;

import javax.swing.UIManager;

public class Main
{
	
	public static void main(String[] args)
	{
		UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
		// Permet de valider un bouton selectionné directement avec Entrée.
		// (En plus de barre espace)
		new Window(600, 600);
	}
	
}
