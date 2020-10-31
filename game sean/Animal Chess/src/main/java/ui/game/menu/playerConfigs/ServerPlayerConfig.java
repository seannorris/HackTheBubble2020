package ui.game.menu.playerConfigs;

import logic.player.Player;
import logic.player.SocketPlayer;
import ui.TerminalHandler;
import ui.drawing.colour.ColouredString;
import ui.game.Game;
import ui.game.menu.GameMenu;
import ui.input.selection.UnselectableSection;
import ui.menu.LabeledMenuItem;
import ui.menu.MenuItem;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Represents a server player config menu.<br>
 * {@inheritDoc}
 */
public class ServerPlayerConfig extends PlayerConfig implements OnlinePlayerConfig
{
    private final OnlinePlayerConfig.PortBox portBox;
    
    /**
     * Constructor for ServerPlayerConfig.
     * @param terminalHandler The TerminalHandler.
     * @param enabled If the config menu is enabled for selection.
     */
    public ServerPlayerConfig(TerminalHandler terminalHandler, boolean enabled)
    {
        super(terminalHandler, enabled, true, getExtras(terminalHandler));
        portBox = mostRecentPortBox;
    }
    
    /**
     * The most recent port selection box.
     */
    private static OnlinePlayerConfig.PortBox mostRecentPortBox;
    
    /**
     * Gets a labeled coloured string to show the local IP and the port box.
     * @param handler The TerminalHandler.
     * @return The array of MenuItem containing the IP label and port text box.
     */
    private static MenuItem[] getExtras(TerminalHandler handler)
    {
        mostRecentPortBox = new OnlinePlayerConfig.PortBox(handler, true);
        try
        {
            return new MenuItem[]
            {
                new LabeledMenuItem<>(GameMenu.MENU_ITEM_HEIGHT, new UnselectableSection<>(new ColouredString(InetAddress.getLocalHost().getHostAddress())),
                        new ColouredString("Your IP Address"), GameMenu.DOTS, GameMenu.FILLER),
                new LabeledMenuItem<>(GameMenu.MENU_ITEM_HEIGHT, mostRecentPortBox, new ColouredString("Port"), GameMenu.DOTS, GameMenu.FILLER)
            };
        }
        catch(UnknownHostException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Player getPlayer(Game game)
    {
        return new SocketPlayer.ServerPlayer(game, Short.parseShort(portBox.getText()));
    }
}
