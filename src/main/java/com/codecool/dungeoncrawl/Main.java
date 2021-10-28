package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.dao.PlayerDao;
import com.codecool.dungeoncrawl.dao.PlayerDaoJdbc;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

import java.util.ArrayList;
import java.util.Optional;

public class Main extends Application {
    GameMap map = MapLoader.loadMap("/map.txt");
    GameMap inventoryMap = MapLoader.loadMap("/inventory.txt");
    int canvasWidth = 16 * Tiles.TILE_WIDTH;
    int canvasHeight = 16 * Tiles.TILE_WIDTH;
    int inventoryCanvasWidth = canvasWidth;
    int inventoryCanvasHeight = Tiles.TILE_WIDTH * 2;
    Canvas canvas = new Canvas(canvasWidth, canvasHeight);
    Canvas inventoryCanvas = new Canvas(inventoryCanvasWidth, inventoryCanvasHeight);
    GraphicsContext context = canvas.getGraphicsContext2D();
    GraphicsContext inventoryContext = inventoryCanvas.getGraphicsContext2D();
    private final GameDatabaseManager manager = new GameDatabaseManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        manager.setup();
        GridPane ui = new GridPane();
        ui.setPrefWidth(500);
        ui.setPadding(new Insets(10));

        Button button = new Button("Pick up");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                map.getPlayer().pickUpItem();
                refresh();
            }
        });
        ui.add(button, 0, 1);

        BorderPane borderPane = new BorderPane();
        FlowPane canvases = new FlowPane() ;
        canvases.getChildren().addAll(canvas, inventoryCanvas);

        borderPane.setCenter(canvases);

        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);
        button.setOnKeyPressed(this::onKeyPressed);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if(map.getPlayer().isAlive() && !map.getPlayer().hasWon(map.getPlayer().getCell())) {
            switch (keyEvent.getCode()) {
                case UP:
                    map.getPlayer().move(0, -1);
                    map.moveMonsters();
                    refresh();
                    break;
                case DOWN:
                    map.getPlayer().move(0, 1);
                    map.moveMonsters();
                    refresh();
                    break;
                case LEFT:
                    map.getPlayer().move(-1, 0);
                    map.moveMonsters();
                    refresh();
                    break;
                case RIGHT:
                    map.getPlayer().move(1, 0);
                    map.moveMonsters();
                    refresh();
                    break;
                case S:
                    Object source = keyEvent.getSource();
                    if(keyEvent.isControlDown() && !(source instanceof Button)){
                        saveGameButton();
                    }
                    break;
            }
        }
    }

    private void fillCanvas(GameMap mapToSet, Canvas canvasToSet, GraphicsContext contextToSet, int contextStartX, int contextStartY, int width, int height) {
        contextToSet.setFill(Color.BLACK);
        contextToSet.fillRect(contextStartX, contextStartY, canvasToSet.getWidth(), canvasToSet.getHeight());
        for (int x = 0; x < width / Tiles.TILE_WIDTH; x++) {
            for (int y = 0; y < height / Tiles.TILE_WIDTH; y++) {
                Cell cell = mapToSet.getCell(x+contextStartX, y+contextStartY);
                if (cell.getActor() != null) {
                    Tiles.drawTile(contextToSet, cell.getActor(), x, y);
                }else if (cell.getItem() != null){
                    Tiles.drawTile(contextToSet, cell.getItem(), x, y);
                }
                else {
                    Tiles.drawTile(contextToSet, cell, x, y);
                }
            }
        }
    }

    private List<Item> convertPlayerHealthToHealthBars() {
        int divisorOfHealthScore = 10;
        int healthScore = map.getPlayer().getHealth() / divisorOfHealthScore;
        List<Item> healthBars = new ArrayList<>();
        for (int i=0; i<healthScore; i++) {
            healthBars.add(new HealthBar());
        }
        return healthBars;
    }

    private boolean ifIndexWithinCanvasWidth(int indexToCheck, int canvasWidth) {
        return indexToCheck < canvasWidth/Tiles.TILE_WIDTH - 2;
    }

    private void setInventoryBarItems(List<Item> items, int whichRowIndex) {
        int startColIndex = 2;
        boolean isAlreadyTypeSword = false;
        for (Item item: items) {
            if (item != null) {
                if (item instanceof Sword) {
                    if (isAlreadyTypeSword) continue;
                    else isAlreadyTypeSword = true;
                }
                inventoryMap.getCell(startColIndex, whichRowIndex).setItem(item);
                if (ifIndexWithinCanvasWidth(startColIndex, inventoryCanvasWidth)) {
                    startColIndex++;
                }
            }
        }
    }

    private void refreshInventory() {
        inventoryMap = MapLoader.loadMap("/inventory.txt");
        List<Item> inventory = map.getPlayer().getInventory();
        List<Item> healthBars = convertPlayerHealthToHealthBars();
        setInventoryBarItems(healthBars, 0);
        setInventoryBarItems(inventory, 1);
    }

    private void refresh() {
        Optional<String> currentMap = Optional.ofNullable(map.getName());
        if (map.getPlayer().getCell().getType() == CellType.LATTER){
            if(currentMap.orElse("/map.txt").equals("/map.txt")){
                switchMap("/map2.txt");
            }
            else{
                switchMap("/map.txt");
            }
        }else if (map.getPlayer().getCell().getType() == CellType.HOUSE){
            if(currentMap.orElse("/map2.txt").equals("/map2.txt")){
                switchMap("/map3.txt");
            }
            else{
                switchMap("/map2.txt");
            }
        }

        int[] contextStartPos = getCanvasStartPos(map.getPlayer());
        refreshInventory();

        fillCanvas(map, canvas, context, contextStartPos[0], contextStartPos[1], canvasWidth, canvasHeight);
        fillCanvas(inventoryMap, inventoryCanvas, inventoryContext, 0, 0, inventoryCanvasWidth, inventoryCanvasHeight);


        if (map.getPlayer().getCell().getType() == CellType.LATTER){
            map = MapLoader.loadMap("/map2.txt");
        }

    }

    private int[] getCanvasStartPos(Player player) {
        int playerPosX = player.getX();
        int playerPosY = player.getY();
        int mapHeight = map.getHeight();
        int mapWidth = map.getWidth();
        int yPointer = canvasHeight / Tiles.TILE_WIDTH / 2;
        int xPointer = canvasWidth / Tiles.TILE_WIDTH / 2;
        int startX = playerPosX - xPointer;
        int startY = playerPosY - yPointer;
        int endX = playerPosX + xPointer;
        int endY = playerPosY + yPointer;

        if (startX < 0) startX = 0;
        if (startY < 0) startY = 0;
        if (endY >= mapHeight) startY = mapHeight - canvasHeight / Tiles.TILE_WIDTH;
        if (endX >= mapWidth) startX = mapWidth - canvasWidth / Tiles.TILE_WIDTH;

        return new int[] {startX, startY};
    }


    private void switchMap(String mapName){

        Player player = map.getPlayer();
        List<Item> inventory = player.getInventory();
        int playerHealth = player.getHealth();
        int playerAttack = player.getAttack();
        Weapon weapon = player.getWeapon();
        map = MapLoader.loadMap(mapName);

        player = map.getPlayer();
        player.setInventory(inventory);
        player.setHealth(playerHealth);
        player.setAttack(playerAttack);
        player.setWeapon(weapon);

    }

    private void saveGameButton(){

        String defaultText = "alpha", name;

        while(true){
            Optional<String> opt = showDialog(defaultText);
            if(opt.isPresent()){
                name = opt.get();
                if(manager.checkName(name)){
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Confirm");
                    confirm.setHeaderText("Would you like to overwrite the already existing state?");
                    Optional<ButtonType> result = confirm.showAndWait();
                    if(result.get() != ButtonType.OK){
                        defaultText = name;
                        continue;
                    }
                    map.getPlayer().setName(name);
                    manager.updatePlayer(map.getPlayer(), map);
                }
                else{
                    map.getPlayer().setName(name);
                    manager.savePlayer(map.getPlayer(), map);
                    manager.loadMap(name);
                }
                break;
            }
            break;
        }
    }

    private Optional<String> showDialog(String name){
        TextInputDialog dialog = new TextInputDialog(name);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().setAll(cancelButton, saveButton);
        dialog.setTitle("title");
        dialog.setHeaderText("Name:");
        return dialog.showAndWait();
    }
}
