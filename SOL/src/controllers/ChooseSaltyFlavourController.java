package controllers;

import application.*;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;


import java.util.ArrayList;

public class ChooseSaltyFlavourController extends ChooseFlavourController {

    @FXML
    private GridPane cheesesGrid;

    @FXML
    private GridPane vegetablesGrid;

    @FXML
    private GridPane proteinsGrid;


    @FXML
    private GridPane greensGrid;

    @FXML
    private Label flavourNumberLabel;



    private static final ToggleGroup pizzaCheeseGroup = new ToggleGroup();
    private static final ToggleGroup pizzaVegetableGroup = new ToggleGroup();
    private static final ToggleGroup pizzaProteinGroup = new ToggleGroup();
    private static final ToggleGroup pizzaGreensGroup = new ToggleGroup();


    private static final String FLAVOUR_TYPE = "salgado";
    private static final Salty SALTY_INGREDIENTS = new Salty();

    @Override
    public void initialize() {
        flavourNumberLabel.setText("ESCOLHA OS INGREDIENTES DO " + (sharedControl.getFlavorsCounter() + 1) + "º SABOR");

        initializeToggleGroup(SALTY_INGREDIENTS.getIngredientsByType("cheese"), pizzaCheeseGroup, cheesesGrid);
        initializeToggleGroup(SALTY_INGREDIENTS.getIngredientsByType("vegetable"), pizzaVegetableGroup, vegetablesGrid);
        initializeToggleGroup(SALTY_INGREDIENTS.getIngredientsByType("protein"), pizzaProteinGroup, proteinsGrid);
        initializeToggleGroup(SALTY_INGREDIENTS.getIngredientsByType("green leaf"), pizzaGreensGroup, greensGrid);

        double previousFlavourPrice = initializeFlavorPrice();

        if (previousFlavourPrice == 0) // a pizza não existe
            goAheadButton.setDisable(true);
        else {
            currentPizza.setPrice(currentPizza.getPrice() - previousFlavourPrice);
            currentOrder.setTotalPrice(currentOrder.getPrice() - previousFlavourPrice);
        }

        ChangeListener<Object> updateTotalListener = (observable, oldValue, newValue) -> {
            double currentFlavorPrice = getCurrentFlavourPrice();

            if (currentFlavorPrice == 0)
                goAheadButton.setDisable(true);
            else
                goAheadButton.setDisable(false);

            priceText.setText("TOTAL DO PEDIDO: R$ " + String.format("%.2f", currentOrder.getPrice() + currentFlavorPrice));
        };

        pizzaCheeseGroup.selectedToggleProperty().addListener(updateTotalListener);
        pizzaVegetableGroup.selectedToggleProperty().addListener(updateTotalListener);
        pizzaProteinGroup.selectedToggleProperty().addListener(updateTotalListener);
        pizzaGreensGroup.selectedToggleProperty().addListener(updateTotalListener);

        priceText.setText("TOTAL DO PEDIDO: R$ " + String.format("%.2f", currentOrder.getPrice() + previousFlavourPrice));

        goBackButton.setOnAction(event -> goBack(FLAVOUR_TYPE));
        goAheadButton.setOnAction(event -> goAhead(FLAVOUR_TYPE));
        changeFlavourTypeButton.setOnAction(event -> goToSugaryFlavorsPage());
    }

    double initializeFlavorPrice() {
        int currentFlavorNumber = sharedControl.getFlavorsCounter();
        Flavour currentFlavour;
        double flavourPrice = 0.0;

        if (currentFlavorNumber < currentPizza.getFlavors().size()) {
            currentFlavour = currentPizza.getFlavors().get(currentFlavorNumber);

            for (String ingredient : currentFlavour.getIngredients()) {

                String ingredientType = SALTY_INGREDIENTS.findType(ingredient);
                double priceOfCurrentIngredient = SALTY_INGREDIENTS.getPrice(ingredientType, ingredient);

                switch (ingredientType) {
                    case "cheese" ->
                            selectButtonByIngredient(pizzaCheeseGroup, ingredient + "\n" + "R$ " + String.format("%.2f", priceOfCurrentIngredient));
                    case "vegetable" ->
                            selectButtonByIngredient(pizzaVegetableGroup, ingredient + "\n" + "R$ " + String.format("%.2f", priceOfCurrentIngredient));
                    case "protein" ->
                            selectButtonByIngredient(pizzaProteinGroup, ingredient + "\n" + "R$ " + String.format("%.2f", priceOfCurrentIngredient));
                    case "green leaf" ->
                            selectButtonByIngredient(pizzaGreensGroup, ingredient + "\n" + "R$ " + String.format("%.2f", priceOfCurrentIngredient));
                }

                flavourPrice += priceOfCurrentIngredient;
            }
        }

        return flavourPrice;
    }

    double getCurrentFlavourPrice() {

        ChangeableButton cheeseSelectedToggle = (ChangeableButton) pizzaCheeseGroup.getSelectedToggle();
        ChangeableButton vegetableSelectedToggle = (ChangeableButton) pizzaVegetableGroup.getSelectedToggle();
        ChangeableButton proteinSelectedToggle = (ChangeableButton) pizzaProteinGroup.getSelectedToggle();
        ChangeableButton greensSelectedToggle = (ChangeableButton) pizzaGreensGroup.getSelectedToggle();

        double cheesePrice = cheeseSelectedToggle != null ? (double) cheeseSelectedToggle.getUserData() : 0;
        double vegetablePrice = vegetableSelectedToggle != null ? (double) vegetableSelectedToggle.getUserData() : 0;
        double proteinPrice = proteinSelectedToggle != null ? (double) proteinSelectedToggle.getUserData() : 0;
        double greensPrice = greensSelectedToggle != null ? (double) greensSelectedToggle.getUserData() : 0;

        return cheesePrice + vegetablePrice + proteinPrice + greensPrice;
    }


    ArrayList<String> getIngredients() {
        ChangeableButton cheeseSelectedToggle = (ChangeableButton) pizzaCheeseGroup.getSelectedToggle();
        ChangeableButton vegetableSelectedToggle = (ChangeableButton) pizzaVegetableGroup.getSelectedToggle();
        ChangeableButton proteinSelectedToggle = (ChangeableButton) pizzaProteinGroup.getSelectedToggle();
        ChangeableButton greensSelectedToggle = (ChangeableButton) pizzaGreensGroup.getSelectedToggle();

        String cheeseIngredient = cheeseSelectedToggle != null ? cheeseSelectedToggle.getText().split("\n")[0] : null;
        String vegetableIngredient = vegetableSelectedToggle != null ? vegetableSelectedToggle.getText().split("\n")[0] : null;
        String proteinIngredient = proteinSelectedToggle != null ? proteinSelectedToggle.getText().split("\n")[0] : null;
        String greensIngredient = greensSelectedToggle != null ? greensSelectedToggle.getText().split("\n")[0] : null;

        ArrayList<String> ingredients = new ArrayList<>();

        if (cheeseIngredient != null)
            ingredients.add(cheeseIngredient);
        if (vegetableIngredient != null)
            ingredients.add(vegetableIngredient);
        if (proteinIngredient != null)
            ingredients.add(proteinIngredient);
        if (greensIngredient != null)
            ingredients.add(greensIngredient);

        return ingredients;
    }
}


