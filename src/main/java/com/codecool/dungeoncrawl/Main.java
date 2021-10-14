package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    GameMap map = MapLoader.loadMap("/map3.txt");
    int canvasWidth = 512;      // make it divisible by 32!
    int canvasHeight = 512;
    Canvas canvas = new Canvas(canvasWidth, canvasHeight);

    GraphicsContext context = canvas.getGraphicsContext2D();
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
            }
        });
        ui.add(button, 0, 1);

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
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
                map.moveMonsters();
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                map.moveMonsters();
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1,0);
                map.moveMonsters();
                refresh();
                break;
        }
    }

    private void refresh() {
        context.setFill(Color.BLACK);
        int[] contextStartPos = getFirstPos(map.getPlayer());
        context.fillRect(contextStartPos[0], contextStartPos[1], canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < canvasWidth / Tiles.TILE_WIDTH; x++) {
            for (int y = 0; y < canvasHeight / Tiles.TILE_WIDTH; y++) {
                Cell cell = map.getCell(x+contextStartPos[0], y+contextStartPos[1]);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                }else if (cell.getItem() != null){
                    Tiles.drawTile(context, cell.getItem(), x, y);
                }
                else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }

        if (map.getPlayer().getCell().getType() == CellType.LATTER){
            map = MapLoader.loadMap("/map2.txt");
        }

        healthLabel.setText("" + map.getPlayer().getHealth());
    }

    public int[] getFirstPos(Player player) {
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
