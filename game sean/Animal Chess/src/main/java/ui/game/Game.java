package ui.game;

import logic.GameHandler;
import ui.TerminalHandler;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredString;
import ui.drawing.colour.WrappingColouredString;
import ui.drawing.composition.BasicSectionComposer;
import ui.drawing.composition.BasicSectionGrid;
import ui.drawing.composition.SectionGrid;
import ui.game.menu.GameMenu;
import ui.game.menu.Player;
import ui.input.EscapeHandler;
import ui.input.KeyHandler;
import ui.input.selection.SelectableGrid;
import ui.input.selection.SelectableSection;
import ui.input.selection.UnselectableSection;
import ui.input.selection.layout.SelectableCentredSection;
import ui.menu.Dialog;
import ui.menu.button.Button;
import ui.menu.button.ChangeViewButton;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/**
 * Represents the game area in the context of the UI.<br>
 * {@inheritDoc}
 */
public class Game extends SelectableGrid<SelectableSection, BasicSectionComposer<SelectableSection>, SelectableSection> implements GameHandler, EscapeHandler
{
    private final logic.Game game;
    private final GameBoard board;
    private final Hand leftHand;
    private final Hand rightHand;
    private final TerminalHandler handler;
    private final MinimumSize<SelectableCentredSection<Game>> container;
    private final PauseMenu pauseMenu;
    private final String player1Name;
    private final String player2Name;
    private final ColouredString bottomName;
    private final ColouredString topName;
    private final GameMenu menu;
    
    private boolean flipped;
    private boolean humanMove;
    private Piece selectedPiece;
    private boolean place;
    private boolean paused;
    
    public static final int HAND_BOARD_GAP = 5;
    public static final int WIDTH = 2 * (Hand.WIDTH + HAND_BOARD_GAP) + GameBoard.WIDTH;
    public static final int PADDING_HEIGHT = 3;
    public static final int CENTRE_HEIGHT = Math.max(Hand.HEIGHT, GameBoard.HEIGHT);
    public static final int HEIGHT = CENTRE_HEIGHT + 2 * PADDING_HEIGHT;
    
    /**
     * Constructor for Game.
     * @param game The logic.Game object.
     * @param player1info The player 1 UI object.
     * @param player2info The player 2 UI object.
     * @param menu The game menu.
     * @param terminalHandler The TerminalHandler.
     * @param enabled Boolean; true if the game area is enabled for selection.
     */
    public Game(logic.Game game, Player player1info, Player player2info, GameMenu menu, TerminalHandler terminalHandler, boolean enabled)
    {
        super(getSectionGrid(), terminalHandler, null, enabled);
        var player1 = player1info.getPlayer(this);
        var player2 = player2info.getPlayer(this);
        game.initialise(player1, player2, this);
        this.handler = terminalHandler;
        this.game = game;
        this.player1Name = player1.getName();
        this.player2Name = player2.getName();
        this.topName = new ColouredString(player1Name);
        this.bottomName = new ColouredString(player2Name);
        this.menu = menu;
        
        board = new GameBoard(this, terminalHandler, this, enabled);
        leftHand = new Hand(this, false, terminalHandler, this, enabled);
        rightHand = new Hand(this, true, terminalHandler, this, enabled);
        
        var rows = handler.getTerminal().getHeight();
        var cols = handler.getTerminal().getWidth();
        container = new MinimumSize<>(rows, cols, new SelectableCentredSection<>(rows, cols, this), handler);
        pauseMenu = new PauseMenu(enabled);
        var pauseButton = new SelectableCentredSection<>(PADDING_HEIGHT, GameBoard.WIDTH, new PauseButton(pauseMenu));
        
        var composer = getGrid().getComposer();
        var sections = getGrid().getSections();
        
        sections[1][0] = leftHand;
        sections[1][1] = board;
        sections[1][2] = rightHand;
        sections[0][1] = pauseButton;
        setEscapeHandler(this);
        
        composer.addSection(leftHand, 0, PADDING_HEIGHT);
        composer.addSection(board, Hand.WIDTH + HAND_BOARD_GAP, PADDING_HEIGHT);
        composer.addSection(rightHand, Hand.WIDTH + GameBoard.WIDTH + 2 * HAND_BOARD_GAP, PADDING_HEIGHT);
        composer.addSection(pauseButton, Hand.WIDTH + HAND_BOARD_GAP, 0);
        composer.addSection(new SelectableCentredSection<>(PADDING_HEIGHT, Hand.WIDTH, new UnselectableSection<>(topName)), 0, 0);
        composer.addSection(new SelectableCentredSection<>(PADDING_HEIGHT, Hand.WIDTH, new UnselectableSection<>(bottomName)),
                Hand.WIDTH + GameBoard.WIDTH + 2 * HAND_BOARD_GAP, PADDING_HEIGHT + CENTRE_HEIGHT);
        
        updateNames();
    }
    
    /**
     * Gets a new section grid to contain the game elements.
     * @return The new section grid.
     */
    private static SectionGrid<SelectableSection, BasicSectionComposer<SelectableSection>, SelectableSection> getSectionGrid()
    {
        var composer = new BasicSectionComposer<SelectableSection>(WIDTH, HEIGHT);
        var sections = new SelectableSection[2][3];
        
        return new BasicSectionGrid<>(composer, sections);
    }
    
    /**
     * Updates the player names when the board flips.
     */
    public void updateNames()
    {
        topName.setString(limit(flipped ? player1Name : player2Name));
        bottomName.setString(limit(flipped ? player2Name : player1Name));
        topName.setColour(flipped ? Piece.PLAYER1_COLOUR : Piece.PLAYER2_COLOUR);
        bottomName.setColour(flipped ? Piece.PLAYER2_COLOUR : Piece.PLAYER1_COLOUR);
    }
    
    /**
     * Limits the name to the space it's allowed to occupy.
     * @param in The string to limit.
     * @return The truncated string.
     */
    private String limit(String in)
    {
        if(in.length() < Hand.WIDTH)
            return in;
        
        else return in.substring(0, Hand.WIDTH - 3) + "...";
    }
    
    /**
     * Calls the play loop of the logic.Game object.
     * Generates a game finished dialog with the winner.
     * @return The key handler for the game finished dialog.
     */
    public KeyHandler play()
    {
        moveEnd();
        game.play();
        
        if(paused)
            return handler.getKeyHandler();
        
        var gameFinishedDialog = new GameFinishedDialog(true);
        handler.setPrintHandler(gameFinishedDialog.getContainer());
        handler.setResizeHandler(gameFinishedDialog.getContainer());
        return gameFinishedDialog.getContainer().getHandler();
    }
    
    /**
     * Called at the end of each turn so the game state can be rendered.
     */
    @Override
    public void moveEnd()
    {
        board.updateBoard(game.getBoard(), null, flipped);
        leftHand.updateHand(getLeftHand(), false, null, flipped);
        rightHand.updateHand(getRightHand(), false, null, flipped);
        updateNames();
        checkEnabled();
        try
        {
            handler.draw();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        if(!getGame().isWon())
            handler.setKeyHandler(getContainer().getHandler());
    }
    
    /**
     * Returns the hand to the left of the board.
     * @return The left hand.
     */
    private List<logic.piece.Piece> getLeftHand()
    {
        return flipped ? game.getPlayer1Hand() : game.getPlayer2Hand();
    }
    
    /**
     * Returns the hand to the right of the board.
     * @return The right hand.
     */
    private List<logic.piece.Piece> getRightHand()
    {
        return flipped ? game.getPlayer2Hand() : game.getPlayer1Hand();
    }
    
    /**
     * Gets a move from the user by enabling their available pieces and allowing them to select one.
     */
    @Override
    public void doHumanMove()
    {
        flipped = game.isPlayer2Turn();
        
        getSelectables();
        
        humanMove = true;
        try
        {
            checkEnabled();
            handler.setKeyHandler(focus(getSections()[0][1].primaryNext(handler.getKeyHandler())));
            handler.draw();
            while(humanMove && handler.getInput() && humanMove)
                checkEnabled();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        if(humanMove)
            game.win(!game.isPlayer2Turn());
    }
    
    /**
     * Enables the selectable pieces for the current player.
     */
    private void getSelectables()
    {
        var selectablePieces = new HashSet<logic.Game.Coord>();
        for(var piece : game.getBoard().values())
            if(piece.isPlayer2() == game.isPlayer2Turn())
                selectablePieces.add(piece.getPosition());
    
        board.updateBoard(game.getBoard(), selectablePieces, flipped);
        leftHand.updateHand(getLeftHand(), false, null, flipped);
        rightHand.updateHand(getRightHand(), getRightHand().size() > 0, null, flipped);
        updateNames();
    }
    
    /**
     * Performs a move to a coordinate with the selected piece.
     * @param coord The coordinate to move to.
     */
    public void moveTo(logic.Game.Coord coord)
    {
        if(place)
            game.place(selectedPiece.getPiece(), coord);
        else
            game.move(selectedPiece.getPiece(), selectedPiece.getPiece().getMove(coord));
        
        humanMove = false;
        place = false;
        selectedPiece = null;
    }
    
    /**
     * Called when a piece is selected by the user.
     * Enables all the available spaces to move to.
     * @param piece The piece to select.
     */
    public void selectPiece(Piece piece)
    {
        if(selectedPiece != null)
            if(piece.equals(selectedPiece))
            {
                getSelectables();
                place = false;
                selectedPiece = null;
            }
            else
                moveTo(piece.getPiece() == null ? board.getCoord(piece) : piece.getPiece().getPosition());
        else
        {
            selectedPiece = piece;
            var selectableCoords = new HashSet<logic.Game.Coord>();
            if(piece.getPiece().getPosition() == null)
            {
                place = true;
                board.getPlaceable(selectableCoords, piece.getPiece());
            }
            else
            {
                for(var move : piece.getPiece().getMoveDirections())
                {
                    var newPos = logic.Game.Coord.move(piece.getPiece().getPosition(), move);
                    if(game.canMove(piece.getPiece(), newPos))
                        selectableCoords.add(newPos);
                }
                selectableCoords.add(piece.getPiece().getPosition());
            }
            board.updateBoard(game.getBoard(), selectableCoords, flipped);
            leftHand.updateHand(getLeftHand(), false, null, flipped);
            rightHand.updateHand(getRightHand(), false, piece, flipped);
            updateNames();
        }
    }
    
    /**
     * Getter for the logic.Game.
     * @return the logic.Game.
     */
    public logic.Game getGame()
    {
        return game;
    }
    
    /**
     * Getter for the game container.
     * @return the container.
     */
    public MinimumSize<SelectableCentredSection<Game>> getContainer()
    {
        return container;
    }
    
    /**
     * {@inheritDoc}
     * In this case if a piece is selected deselect it otherwise pause the game.
     */
    @Override
    public KeyHandler escapePressed()
    {
        if(selectedPiece == null)
        {
            paused = true;
            handler.setPrintHandler(pauseMenu.getContainer());
            handler.setResizeHandler(pauseMenu.getContainer());
            return pauseMenu.getContainer().getHandler();
        }
        getSelectables();
        place = false;
        selectedPiece = null;
        
        return getHandler();
    }
    
    /**
     * Getter for the TerminalHandler.
     * @return The TerminalHandler.
     */
    public TerminalHandler getTerminalHandler()
    {
        return handler;
    }
    
    /**
     * Getter for the paused state of the game.
     * @return Boolean; true if game is paused.
     */
    public boolean isPaused()
    {
        return paused;
    }
    
    /**
     * A button to exit to the main game menu from the pause menu or from the game finished dialog.<br>
     * {@inheritDoc}
     */
    private class ExitToMenuButton extends ChangeViewButton<MinimumSize<SelectableCentredSection<GameMenu>>, MinimumSize<SelectableCentredSection<GameMenu>>>
    {
        public static final String TEXT = "Return To Menu";
    
        /**
         * Constructor for ExitToMenuButton.
         * @param enabled If the button should be enabled.
         */
        public ExitToMenuButton(boolean enabled)
        {
            super(menu.getContainer(), menu.getContainer(), handler, TEXT, Colour.DEFAULT, Colour.BLUE_BOLD_BRIGHT, BackgroundColour.DEFAULT, Piece.ENABLED_COLOUR, enabled);
        }
    
        /**
         * {@inheritDoc}
         * In this case exit the game.
         */
        @Override
        public KeyHandler select(KeyHandler current)
        {
            humanMove = false;
            game.win(!game.isPlayer2Turn());
            return super.select(current);
        }
    }
    
    /**
     * Get the buttons for the game finished dialog.
     * @return The array of buttons.
     */
    private Button[] getGameFinishedButtons()
    {
        return new Button[]
                {
                        new ExitToMenuButton(true),
                        new ui.game.menu.ExitButton(handler, true)
                };
    }
    
    /**
     * The game finished dialog.<br>
     * {@inheritDoc}
     */
    public class GameFinishedDialog extends Dialog
    {
        private final MinimumSize<SelectableCentredSection<GameFinishedDialog>> container;
    
        /**
         * Constructor for GameFinishedDialog.
         * @param enabled If the dialog is enabled.
         */
        public GameFinishedDialog(boolean enabled)
        {
            super(new WrappingColouredString(new ColouredString(String.format("%s has won the game!!!", game.getWinner() ? player2Name : player1Name)), 10, 30), getGameFinishedButtons(), handler, new GameMenu.EscapeHandler(handler, menu), enabled, GameMenu.FILLER, GameMenu.BORDER_COLOUR, GameMenu.BORDER_BACKGROUND_COLOUR);
            var rows = handler.getTerminal().getHeight();
            var cols = handler.getTerminal().getWidth();
            container = new MinimumSize<>(rows, cols, new SelectableCentredSection<>(rows, cols, this), handler);
        }
    
        /**
         * Getter for the dialog's container.
         * @return the container.
         */
        public MinimumSize<SelectableCentredSection<GameFinishedDialog>> getContainer()
        {
            return container;
        }
    }
    
    /**
     * The escape handler for use in pause menu.
     * Resumes the game.
     */
    private final PauseMenuEscapeHandler pauseMenuEscapeHandler = new PauseMenuEscapeHandler();
    
    /**
     * The pause menu for the game.<br>
     * {@inheritDoc}
     */
    public class PauseMenu extends Dialog
    {
        private final MinimumSize<SelectableCentredSection<PauseMenu>> container;
        
        public static final String TEXT = "The game is paused.";
    
        /**
         * Constructor for the pause menu.
         * @param enabled If the pause menu is enabled.
         */
        public PauseMenu(boolean enabled)
        {
            super(new WrappingColouredString(new ColouredString(TEXT), 10, 30), getPauseMenuButtons(), handler, pauseMenuEscapeHandler, enabled, GameMenu.FILLER, GameMenu.BORDER_COLOUR, GameMenu.BORDER_BACKGROUND_COLOUR);
            var rows = handler.getTerminal().getHeight();
            var cols = handler.getTerminal().getWidth();
            container = new MinimumSize<>(rows, cols, new SelectableCentredSection<>(rows, cols, this), handler);
        }
    
        /**
         * Getter for the pause menu container.
         * @return The container.
         */
        public MinimumSize<SelectableCentredSection<PauseMenu>> getContainer()
        {
            return container;
        }
    }
    
    /**
     * Gets the buttons for the bottom of the pause menu.
     * @return the array of buttons.
     */
    private Button[] getPauseMenuButtons()
    {
        return new Button[]
        {
                new PauseMenuResumeButton(),
                new ExitToMenuButton(true),
                new ui.game.menu.ExitButton(handler, true)
        };
    }
    
    /**
     * The resume button for the pause menu.<br>
     * {@inheritDoc}
     */
    private class PauseMenuResumeButton extends ChangeViewButton<MinimumSize<SelectableCentredSection<Game>>, MinimumSize<SelectableCentredSection<Game>>>
    {
        public static final String TEXT = "Resume";
    
        /**
         * Constructor for PauseMenuResumeButton.
         */
        public PauseMenuResumeButton()
        {
            super(container, container, handler, TEXT, Colour.DEFAULT, Colour.GREEN_BOLD_BRIGHT, BackgroundColour.DEFAULT, Piece.ENABLED_COLOUR, true);
        }
    
        /**
         * {@inheritDoc}
         * In this case resume the game.
         */
        @Override
        public KeyHandler select(KeyHandler current)
        {
            paused = false;
            return super.select(current);
        }
    }
    
    /**
     * The pause button for the game.<br>
     * {@inheritDoc}
     */
    private class PauseButton extends ChangeViewButton<MinimumSize<SelectableCentredSection<PauseMenu>>, MinimumSize<SelectableCentredSection<PauseMenu>>>
    {
        public static final String TEXT = "Pause";
    
        /**
         * Constructor for PauseButton.
         * @param menu The pause menu to open.
         */
        public PauseButton(PauseMenu menu)
        {
            super(menu.container, menu.container, handler, TEXT, Colour.DEFAULT, Colour.DEFAULT, Piece.ENABLED_COLOUR, BackgroundColour.GREEN, true);
        }
    
        /**
         * {@inheritDoc}
         * In this case set paused to true.
         */
        @Override
        public KeyHandler select(KeyHandler current)
        {
            paused = true;
            return super.select(current);
        }
    }
    
    /**
     * Resumes the game from the pause menu when escape is pressed.<br>
     * {@inheritDoc}
     */
    private class PauseMenuEscapeHandler implements ui.input.EscapeHandler
    {
        /**
         * {@inheritDoc}
         * In this case unpause the game.
         */
        @Override
        public KeyHandler escapePressed()
        {
            paused = false;
            handler.setPrintHandler(getContainer());
            handler.setResizeHandler(getContainer());
            return getContainer().getHandler();
        }
    }
}
