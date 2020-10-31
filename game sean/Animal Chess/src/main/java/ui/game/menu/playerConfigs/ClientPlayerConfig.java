package ui.game.menu.playerConfigs;

import logic.player.Player;
import logic.player.SocketPlayer;
import ui.TerminalHandler;
import ui.drawing.colour.ColouredString;
import ui.game.Game;
import ui.game.menu.GameMenu;
import ui.menu.LabeledMenuItem;
import ui.menu.MenuItem;
import ui.menu.TextBox;

/**
 * Represents a client player config menu.<br>
 * {@inheritDoc}
 */
public class ClientPlayerConfig extends PlayerConfig implements OnlinePlayerConfig
{
    private final TextBox ipBox;
    private final PortBox portBox;
    
    /**
     * Constructor for ClientPlayerConfig.
     * @param terminalHandler The TerminalHandler.
     * @param enabled If the config menu is enabled for selection.
     */
    public ClientPlayerConfig(TerminalHandler terminalHandler, boolean enabled)
    {
        super(terminalHandler, enabled, false, getExtras(terminalHandler));
        portBox = mostRecentPortBox;
        ipBox = mostRecentIpBox;
    }
    
    /**
     * The most recent port selection box.
     */
    private static PortBox mostRecentPortBox;
    
    /**
     * The most recent IP selection box.
     */
    private static TextBox mostRecentIpBox;
    
    /**
     * Gets the IP and port text boxes.
     * @param handler The TerminalHandler.
     * @return The array of MenuItem containing the text boxes.
     */
    private static MenuItem[] getExtras(TerminalHandler handler)
    {
        mostRecentPortBox = new PortBox(handler, true);
        mostRecentIpBox = new TextBox(15, handler, true);
        return new MenuItem[]
        {
            new LabeledMenuItem<>(GameMenu.MENU_ITEM_HEIGHT, mostRecentIpBox, new ColouredString("Server IP Address"), GameMenu.DOTS, GameMenu.FILLER),
            new LabeledMenuItem<>(GameMenu.MENU_ITEM_HEIGHT, mostRecentPortBox, new ColouredString("Port"), GameMenu.DOTS, GameMenu.FILLER)
        };
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Player getPlayer(Game game)
    {
        return new SocketPlayer.ClientPlayer(game, ipBox.getText(), Short.parseShort(portBox.getText()));
    }
}
