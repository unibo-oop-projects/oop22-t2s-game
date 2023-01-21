package it.unibo.t2sgame.view.impl;

import java.util.Optional;

import it.unibo.t2sgame.core.api.Game;
import it.unibo.t2sgame.core.api.GameEngine;
import it.unibo.t2sgame.input.api.InputComponent;
import it.unibo.t2sgame.input.impl.KeyboardInputController;
import it.unibo.t2sgame.model.api.Entity;
import it.unibo.t2sgame.view.api.GameScene;
import it.unibo.t2sgame.view.api.Graphic;
import it.unibo.t2sgame.view.api.GraphicComponent;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameSceneJavaFXImpl implements GameScene{

    private Group root;
    private Scene scene;
    private Canvas canvas;
    private KeyboardInputController keyInController;
    private Game game;
    private GraphicsContext gContext;
    private GameEngine gameEngine;

    @Override
    public void initialize() {
        Stage stage = new Stage();
        this.root = new Group();
        this.canvas = new Canvas(1080, 720);
        this.gContext = this.canvas.getGraphicsContext2D();
        this.scene = new Scene(this.root, 1080, 720, Color.BLACK);
        this.scene.setOnKeyPressed(event -> keyInController.notifyKeyPressed(event.getCode().getCode()));
        this.scene.setOnKeyReleased(event -> keyInController.notifyKeyReleased(event.getCode().getCode()));

        this.root.getChildren().add(this.canvas);
        stage.setScene(this.scene);
        stage.setTitle("T2S-game");
        stage.show();
    } 

    @Override
    public void render() {
        this.gameEngine.getGame().get().getWorld().getEntities().forEach(entity -> entity
            .getComponent(GraphicComponent.class)
            .ifPresent(gc -> this.draw((GraphicComponent) gc, entity)));
    }

    private void draw(GraphicComponent gc, Entity entity){
        this.gContext.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        gc.setGraphics(new GraphicJavaFXImpl(this.gContext));
        gc.update(entity);
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
        /**
         * This is a code only used to test the correct functionlaity it will be changed
         */
        this.game.getWorld().getPlayers().get(0)
            .getComponent(InputComponent.class)
            .ifPresent(c -> this.keyInController = (KeyboardInputController)((InputComponent)c).getInputController());
    }

    @Override
    public void setKeyboardInputController(KeyboardInputController keyInController) {
        this.keyInController = keyInController;
    }

    @Override
    public void setEngine(GameEngine gameEngine) {
        this.gameEngine =  gameEngine;
    }
}
