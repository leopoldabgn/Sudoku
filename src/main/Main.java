package main;

import javax.swing.UIManager;

public class Main
{
	
	public static void main(String[] args)
	{
		UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
		// Permet de valider un bouton selectionn� directement avec Entr�e.
		// (En plus de barre espace)
		new Window(600, 600);
	}
	
}
