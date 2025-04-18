package projekt.view.menus;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import projekt.model.PlayerImpl;
import projekt.model.PlayerImpl.Builder;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A Builder to create the create game view.
 * The create game view lets users add and remove players and start the game.
 * It is possible to give each player a name, a color and to select whether the
 * player is a bot or not.
 */
public class CreateGameBuilder extends MenuBuilder {
    private final ObservableList<PlayerImpl.Builder> observablePlayers;
    private final Supplier<Boolean> startGameHandler;

    /**
     * Creates a new CreateGameBuilder with the given players and handlers.
     *
     * @param players          The list of players to display and modify.
     * @param returnHandler    The handler to call when the user wants to return to
     *                         the main menu
     * @param startGameHandler The handler to call when the user wants to start the
     *                         game
     */
    @DoNotTouch
    public CreateGameBuilder(
        final ObservableList<PlayerImpl.Builder> players,
        final Runnable returnHandler,
        final Supplier<Boolean> startGameHandler
    ) {
        super("Start new Game", returnHandler);
        this.startGameHandler = startGameHandler;
        this.observablePlayers = players;
    }

    @Override
    protected Node initCenter() {
        final VBox mainBox = new VBox();
        mainBox.setStyle("-fx-font-size: 2em");
        // For icons see https://pictogrammers.com/library/mdi/
        final VBox playerListVBox = new VBox();
        this.observablePlayers.subscribe(() -> {
            playerListVBox.getChildren().clear();
            for (final PlayerImpl.Builder playerBuilder : this.observablePlayers) {
                final HBox playerListingHBox = new HBox();
                playerListingHBox.setAlignment(Pos.CENTER);
                final TextField playerNameTextField = new TextField(playerBuilder.nameOrDefault());
                playerNameTextField.setOnKeyPressed(e -> {
                    final String newName = playerNameTextField.getText();
                    if (newName.isBlank()) {
                        playerBuilder.name(null);
                        playerNameTextField.setText(playerBuilder.nameOrDefault());
                        playerNameTextField.selectAll();
                    } else {
                        playerBuilder.name(newName);
                    }
                });
                playerListingHBox.getChildren().addAll(
                    playerNameTextField,
                    createBotOrPlayerSelector(playerBuilder),
                    createPlayerColorPicker(playerBuilder),
                    createRemovePlayerButton(playerBuilder.getId())
                );
                playerListVBox.getChildren().add(playerListingHBox);
            }
        });

        final Button startGameButton = new Button("Start Game");
        final Label startGameErrorLabel = new Label();
        startGameButton.setOnAction(e -> {
            if (!this.startGameHandler.get()) {
                startGameErrorLabel.setText("Cannot start game");
            }
        });

        mainBox.getChildren().addAll(
            createAddPlayerButton(), 
            playerListVBox,
            startGameButton,
            startGameErrorLabel
        );
        mainBox.alignmentProperty().set(Pos.TOP_CENTER);
        return mainBox;
    }

    /**
     * Creates a button to add a new player to the game.
     * The button adds a new player to the list of players when clicked.
     *
     * @return a button to add a new player to the game
     */
    @StudentImplementationRequired("H3.4")
    private Node createAddPlayerButton() {
        // TODO: H3.4
        final HBox addRemoveButtonRow = new HBox();
        addRemoveButtonRow.setAlignment(Pos.CENTER);
        final Button addPlayerButton = new Button(String.format("%c  Add Player", 0xF02D8));
        addPlayerButton.setOnAction(e -> this.observablePlayers.add(this.nextPlayerBuilder()));
        addRemoveButtonRow.getChildren().addAll(addPlayerButton);
        return addRemoveButtonRow;
    }

    /**
     * Creates a color picker to select the color of the player.
     * Two players cannot have the same color.
     *
     * @param playerBuilder the builder for the player to create the color picker
     *                      for
     * @return a color picker to select the color of the player
     */
    @StudentImplementationRequired("H3.4")
    private Node createPlayerColorPicker(final Builder playerBuilder) {
        // TODO: H3.4
        final ColorPicker colorPicker = new ColorPicker(playerBuilder.getColor());
        colorPicker.setOnAction(e -> {
            final Color newColor = colorPicker.getValue();
            if (this.observablePlayers
                .stream()
                .filter(Predicate.not(playerBuilder::equals))
                .anyMatch(x -> x.getColor().equals(newColor))) {
                new Alert(Alert.AlertType.ERROR, "Two Players cannot have the same color!").showAndWait();
                colorPicker.setValue(playerBuilder.getColor());
            } else {
                playerBuilder.color(newColor);
            }
        });
        return colorPicker;
    }

    /**
     * Creates a node to select whether the player is a bot or not.
     * Includes logic for changing icons based on whether the player is human or AI and a swap functionality.
     *
     * @param playerBuilder the builder for the player to create the selector for
     * @return a node to select whether the player is a bot or not
     */
    @StudentImplementationRequired("H3.4")
    private Node createBotOrPlayerSelector(final Builder playerBuilder) {
        // TODO: H3.4
        enum PlayerTypeIcons {
            BOT(String.format("%c", 0xF06A9)),
            PLAYER(String.format("%c", 0xF17C4));

            public final String icon;

            PlayerTypeIcons(final String icon) {
                this.icon = icon;
            }

            public static String getIcon(final PlayerImpl.Builder builder) {
                return builder.isAi() ? BOT.icon : PLAYER.icon;
            }
        }
        final Button botOrPlayerSelectorButton = new Button();
        botOrPlayerSelectorButton.textProperty()
            .bind(playerBuilder.aiProperty().map(x -> PlayerTypeIcons.getIcon(playerBuilder)));
        botOrPlayerSelectorButton.setOnAction(e -> playerBuilder.ai(!playerBuilder.isAi()));
        return botOrPlayerSelectorButton;
    }

    /**
     * Creates a button to remove the player with the given id.
     *
     * @param id the id of the player to remove
     * @return a button to remove the player with the given id
     */
    @StudentImplementationRequired("H3.4")
    private Button createRemovePlayerButton(final int id) {
        // TODO: H3.4
        final Button removePlayerButton = new Button(String.format("%c", 0xF0375));
        removePlayerButton.setOnAction(e -> {
            this.removePlayer(id);
        });
        return removePlayerButton;
    }

    /**
     * Removes the player with the given id and updates the ids of the remaining
     * players. IDs after the removed ID are updated to maintain a continuous list of numbers.
     * Player indices are 1-based: Player 1, player 2
     *
     * @param id the id of the player to remove
     */
    @StudentImplementationRequired("H3.4")
    private void removePlayer(final int id) {
        // TODO: H3.4
        for(int i=id-1; i< observablePlayers.size();i++){
            observablePlayers.get(i).id(i);
        }
        observablePlayers.remove(id-1);
    }

    /**
     * Returns a new {@link PlayerImpl.Builder} for the player with the next id.
     *
     * @return a new {@link PlayerImpl.Builder} for the player with the next id
     */
    public PlayerImpl.Builder nextPlayerBuilder() {
        return new PlayerImpl.Builder(this.observablePlayers.size() + 1);
    }

}
