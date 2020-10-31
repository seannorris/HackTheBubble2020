package ui.game;

import ui.TerminalHandler;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredString;
import ui.drawing.colour.WrappingColouredString;
import ui.game.menu.ExitButton;
import ui.input.EscapeHandler;
import ui.input.ExitEscapeHandler;
import ui.input.KeyHandler;
import ui.input.MovementKeyHandler;
import ui.input.selection.UnselectableResponsiveSection;
import ui.input.selection.layout.SelectableCentredSection;
import ui.input.selection.layout.SelectableMinimumSizeSection;
import ui.input.selection.layout.SelectableResponsiveSection;
import ui.menu.Dialog;
import ui.menu.button.Button;

/**
 * Ensures that the terminal window is big enough to display the game by replacing whats on screen with a message if it's too small.<br>
 * {@inheritDoc}
 * @param <T> The type of section to show if big enough.
 */
public class MinimumSize<T extends SelectableResponsiveSection> extends SelectableMinimumSizeSection<SelectableResponsiveSection, T, MinimumSize.MinimumSizeDialog>
{
    /**
     * Constructor for MinimumSize.
     * @param rows Initial number of rows in characters.
     * @param columns Initial number of columns in characters.
     * @param section The section to show normally.
     * @param handler The TerminalHandler.
     */
    public MinimumSize(int rows, int columns, T section, TerminalHandler handler)
    {
        super(rows, columns, section, new MinimumSizeDialog(rows, columns, handler, new ExitEscapeHandler(handler)), Game.HEIGHT, Game.WIDTH, handler);
    }
    
    /**
     * A dialog to show when the terminal window is too small.<br>
     * {@inheritDoc}
     */
    public static class MinimumSizeDialog extends SelectableMinimumSizeSection<SelectableResponsiveSection, SelectableCentredSection<Dialog>, MinimumSizeDialog.TooSmallMessage>
    {
        public static final ColouredString MESSAGE = new ColouredString("Please make your terminal window bigger.");
    
        /**
         * Constructor for MinimumSizeDialog.
         * @param rows Initial number of rows in characters.
         * @param columns Initial number of columns in characters.
         * @param handler The TerminalHandler.
         * @param escapeHandler The escape handler.
         */
        public MinimumSizeDialog(int rows, int columns, TerminalHandler handler, EscapeHandler escapeHandler)
        {
            super(rows, columns, new SelectableCentredSection<>(Dialog.HEIGHT, Dialog.WIDTH,
                    new Dialog(handler, escapeHandler, true)),
                    new TooSmallMessage(handler, escapeHandler), Dialog.HEIGHT, Dialog.WIDTH, handler);
        }
    
        /**
         * The actual too small dialog.<br>
         * {@inheritDoc}
         */
        private static class Dialog extends ui.menu.Dialog
        {
            public static final int WIDTH;
            public static final int HEIGHT;
    
            static
            {
                var temp = new Dialog(null, null, false);
                WIDTH = temp.getColumns();
                HEIGHT = temp.getRows();
            }
    
            /**
             * Constructor for the dialog.
             * @param terminalHandler The TerminalHandler.
             * @param escapeHandler The escape handler.
             * @param enabled If the dialog is enabled.
             */
            public Dialog(TerminalHandler terminalHandler, EscapeHandler escapeHandler, boolean enabled)
            {
                super(new TooSmallMessage(terminalHandler, escapeHandler), getButtons(terminalHandler, enabled), terminalHandler, escapeHandler, enabled, FILLER, Colour.DEFAULT, BackgroundColour.DEFAULT);
            }
    
            /**
             * Gets the buttons for the dialog.
             * @param terminalHandler The TerminalHandler.
             * @param enabled If the dialog is enabled.
             * @return The button array.
             */
            private static Button[] getButtons(TerminalHandler terminalHandler,  boolean enabled)
            {
                return new Button[]{new ExitButton(terminalHandler, enabled)};
            }
        }
    
        /**
         * The message shown inside the too small dialog, as well as on its own if the window is too small for the dialog.<br>
         * {@inheritDoc}
         */
        private static class TooSmallMessage extends UnselectableResponsiveSection<WrappingColouredString>
        {
            private final Handler handler;
    
            /**
             * Constructor for TooSmallMessage.
             * @param handler The TerminalHandler.
             * @param escapeHandler The EscapeHandler.
             */
            private TooSmallMessage(TerminalHandler handler, EscapeHandler escapeHandler)
            {
                super(new WrappingColouredString(MESSAGE, 2, 25));
                this.handler = new Handler(handler, escapeHandler);
            }
    
            /**
             * {@inheritDoc}
             */
            @Override
            public KeyHandler getHandler()
            {
                return handler;
            }
    
            /**
             * A movement key handler that does nothing except handle escape.<br>
             * {@inheritDoc}
             */
            protected static class Handler extends MovementKeyHandler
            {
                /**
                 * Constructor for Handler.
                 * @param handler The TerminalHandler.
                 * @param escapeHandler The escape handler.
                 */
                public Handler(TerminalHandler handler, EscapeHandler escapeHandler)
                {
                    super(handler, escapeHandler);
                }
    
                /**
                 * {@inheritDoc}
                 * In this case do nothing.
                 */
                @Override
                protected KeyHandler moveUp()
                {
                    return this;
                }
    
                /**
                 * {@inheritDoc}
                 * In this case do nothing.
                 */
                @Override
                protected KeyHandler moveDown()
                {
                    return this;
                }
    
                /**
                 * {@inheritDoc}
                 * In this case do nothing.
                 */
                @Override
                protected KeyHandler moveLeft()
                {
                    return this;
                }
    
                /**
                 * {@inheritDoc}
                 * In this case do nothing.
                 */
                @Override
                protected KeyHandler moveRight()
                {
                    return this;
                }
    
                /**
                 * {@inheritDoc}
                 * In this case do nothing.
                 */
                @Override
                protected KeyHandler space()
                {
                    return this;
                }
    
                /**
                 * {@inheritDoc}
                 * In this case do nothing.
                 */
                @Override
                protected KeyHandler enter()
                {
                    return this;
                }
    
                /**
                 * {@inheritDoc}
                 * In this case do nothing.
                 */
                @Override
                protected KeyHandler other(int key)
                {
                    return this;
                }
            }
        }
    }
    
}
