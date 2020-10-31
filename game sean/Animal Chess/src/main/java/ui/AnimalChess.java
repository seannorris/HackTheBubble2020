package ui;

import ui.game.menu.GameMenu;
import ui.game.menu.Player;
import ui.input.ExitEscapeHandler;

import java.io.IOException;

/**
 * The main class of the game.
 */
public class AnimalChess
{
    /**
     * The main method for the game.
     * @param args Unused.
     * @throws IOException Ignored.
     */
    public static void main(String[] args) throws IOException
    {
        var terminalHandler = new TerminalHandler(null, null, null);
        
        var player1 = new Player(false, terminalHandler, true);
        var player2 = new Player(true, terminalHandler, true);
        var gameMenu = new GameMenu(terminalHandler, new ExitEscapeHandler(terminalHandler), true, player1, player2);
        
        terminalHandler.setPrintHandler(gameMenu.getContainer());
        terminalHandler.setResizeHandler(gameMenu.getContainer());
        terminalHandler.setKeyHandler(gameMenu.getContainer().getHandler());
        terminalHandler.run();
        terminalHandler.getTerminal().close();
    }
}
