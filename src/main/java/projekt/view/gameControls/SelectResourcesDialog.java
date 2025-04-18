package projekt.view.gameControls;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import projekt.model.Player;
import projekt.model.ResourceType;
import projekt.view.CardPane;
import projekt.view.IntegerField;
import projekt.view.ResourceCardPane;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A dialog to prompt the user to select a number of resources.
 * The dialog shows the resources the player can choose from and lets the user
 * select a number of each resource.
 * If dropCards is true, the user is prompted to drop cards instead of selecting
 * them.
 * The result of the dialog is a map of the selected resources and their
 * amounts.
 */
public class SelectResourcesDialog extends Dialog<Map<ResourceType, Integer>> {
    private final Map<ResourceType, Integer> selectedResources = new HashMap<>();

    /**
     * Creates a new SelectResourcesDialog for the given player and resources.
     *
     * @param amountToSelect        The amount of resources to select.
     * @param player                The player that is prompted to select resources.
     * @param resourcesToSelectFrom The resources the player can select from. If
     *                              null the player can select any resource.
     * @param dropCards             Whether the player should drop cards instead of
     *                              selecting them.
     */
    public SelectResourcesDialog(
        final int amountToSelect, final Player player,
        final Map<ResourceType, Integer> resourcesToSelectFrom, final boolean dropCards
    ) {
        final DialogPane dialogPane = getDialogPane();
        dialogPane.getButtonTypes().add(ButtonType.OK);
        dialogPane.setContent(init(amountToSelect, player, resourcesToSelectFrom, dropCards));
    }

    /**
     * Initializes the dialog content for selecting or dropping resources.
     * This method sets up a grid layout where the user can select a specific
     * amount of resources by typing how many of each type they want to select.
     * It dynamically validates the input to ensure:
     *   The total number of selected resources equals {@code amountToSelect}
     *   The player does not select more than they have (if dropping cards)
     * The method also shows icons for each resource type and binds the confirm
     * button (OK) to be enabled only when the selection is valid.
     *
     * @param amountToSelect        The number of resources the player must select or drop.
     * @param player                The player interacting with the dialog.
     * @param resourcesToSelectFrom The map of resource types and their available counts.
     *                              If {@code null} or empty, all resource types are available.
     * @param dropCards             If {@code true}, this dialog is for dropping cards; otherwise, it is for selecting them.
     * @return A Region containing the dialog content.
     */
    @StudentImplementationRequired("H3.3")
    private Region init(
        final int amountToSelect, final Player player,
        Map<ResourceType, Integer> resourcesToSelectFrom, final boolean dropCards
    ) {
        System.out.println("SelectResourceDialog");
        if (resourcesToSelectFrom == null || resourcesToSelectFrom.isEmpty()) {
            resourcesToSelectFrom = Arrays.stream(ResourceType.values()).collect(Collectors.toMap(r -> r, r -> -1));
        }
        final Map<ResourceType, Integer> resourcesToSelectFromFinal = resourcesToSelectFrom;
        final String action = dropCards ? "drop" : "select";

        this.setTitle(String.format("%s %d cards", action.substring(0, 1).toUpperCase() + action.substring(1),
            amountToSelect
        ));
        this.setHeaderText(constructTooFewCardsString(amountToSelect, player, action));
        final GridPane mainPane = new GridPane(10, 10);
        mainPane.getStylesheets().add("css/hexmap.css");

        final DialogPane dialogPane = getDialogPane();
        dialogPane.lookupButton(ButtonType.OK).setDisable(true);

        for (final ResourceType resourceType : resourcesToSelectFrom.keySet()) {
            final CardPane resourceCard = new ResourceCardPane(
                resourceType,
                Integer.toString(resourcesToSelectFrom.get(resourceType)),
                50
            );
            mainPane.add(resourceCard, resourceType.ordinal(), 0);

            final IntegerField amountField = new IntegerField();
            amountField.valueProperty().subscribe((oldValue, newValue) -> {
                if (newValue.intValue() <= 0) {
                    this.selectedResources.remove(resourceType);
                } else {
                    final int enteredAmount = newValue.intValue();
                    if (enteredAmount > amountToSelect
                        || dropCards && enteredAmount > resourcesToSelectFromFinal.get(resourceType)) {
                        amountField.setValue(oldValue);
                        return;
                    }
                    this.selectedResources.put(resourceType, enteredAmount);
                }

                final int currentTotalAmount = this.selectedResources.values().stream().mapToInt(Integer::intValue)
                    .sum();
                dialogPane.lookupButton(ButtonType.OK).setDisable(true);
                if (currentTotalAmount > amountToSelect) {
                    this.setHeaderText(
                        String.format("You (%s) can only %s %d cards", player.getName(), action, amountToSelect));
                } else if (currentTotalAmount < amountToSelect) {
                    this.setHeaderText(
                        constructTooFewCardsString(amountToSelect - currentTotalAmount, player, action));
                } else {
                    this.setHeaderText("");
                    dialogPane.lookupButton(ButtonType.OK).setDisable(false);
                }
            });

            mainPane.add(amountField, resourceType.ordinal(), 1);
        }

        setResultConverter(buttonType -> {
            if (ButtonType.OK.equals(buttonType)) {
                return selectedResources;
            }
            return null;
        });

        return mainPane;
    }
    private String constructTooFewCardsString(final int amount, final Player player, final String action) {
        return String.format("You, %s still need to %s %d cards", player.getName(), action, amount);
    }
}
