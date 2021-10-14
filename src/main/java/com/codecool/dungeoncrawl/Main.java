package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Apple;
import com.codecool.dungeoncrawl.logic.items.Item;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {
    GameMap map = MapLoader.loadMap("/map.txt");
    GameMap inventoryMap = MapLoader.loadMap("/inventory.txt");
    int canvasWidth = 16 * Tiles.TILE_WIDTH;
    int canvasHeight = 16 * Tiles.TILE_WIDTH;
    Canvas canvas = new Canvas(canvasWidth, canvasHeight);
    Canvas inventoryCanvas = new Canvas(canvasWidth, Tiles.TILE_WIDTH * 2);
    GraphicsContext context = canvas.getGraphicsContext2D();
    GraphicsContext inventoryContext = inventoryCanvas.getGraphicsContext2D();
    Label healthLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));
        ui.setVgap(map.getHeight() * Tiles.TILE_WIDTH-70);

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);

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
        switch (keyEvent.getCode()) {
            case UP:
                map.getPlayer().move(0, -1);
                map.moveMonsters();
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1,0);
                refresh();
                break;
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

    private void refreshInventory() {
        List<Item> inventory = map.getPlayer().getInventory();
        int inventoryStartColIndex = 2;
        for (Item item: inventory) {
            if (item instanceof Apple) {
                continue;
            }
            inventoryMap.getCell(inventoryStartColIndex, 1).setItem(item);
            inventoryStartColIndex++;
        }
    }

    private void refresh() {
        int[] contextStartPos = getFirstPos(map.getPlayer());
        refreshInventory();

        fillCanvas(map, canvas, context, contextStartPos[0], contextStartPos[1], canvasWidth, canvasHeight);
        fillCanvas(inventoryMap, inventoryCanvas, inventoryContext, 0, 0, canvasWidth, Tiles.TILE_WIDTH * 2);


        if (map.getPlayer().getCell().getType() == CellType.LATTER){
            map = MapLoader.loadMap("/map2.txt");
        }

        healthLabel.setText("" + map.getPlayer().getHealth());
    }

    private int[] getFirstPos(Player player) {
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

        if (startX < 0) {
            startX = 0;
        }
        if (startY < 0) {
            startY = 0;
        }
        if (endY >= mapHeight) {
            startY = mapHeight - canvasHeight / Tiles.TILE_WIDTH;
        }
        if (endX >= mapWidth) {
            startX = mapWidth - canvasWidth / Tiles.TILE_WIDTH;
        }

        return new int[] {startX, startY};
    }

}
