package dragonball.view;

import dragonball.controller.GUI;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class StartMenuPane extends BorderPane{
	
	Button newGame;
	Button loadGame;
	GUI gui;
	@SuppressWarnings("unchecked")
	public StartMenuPane (GUI gui){

		Font font = new Font ("Cambria Math" , 40);
		
		newGame = new Button("New Game");
		newGame.setFont(font);
		loadGame = new Button("Load game");
		loadGame.setFont(font);
		newGame.setPrefSize(300, 100);
		loadGame.setPrefSize(300, 100);
		
		newGame.setOnAction(gui);
		loadGame.setOnAction(gui);
	
		BorderPane flow = new BorderPane();

		FlowPane vbox = new FlowPane();
		vbox.getChildren().addAll(newGame, loadGame); // button will be left of text
		vbox.setOrientation(Orientation.VERTICAL);
		vbox.setVgap(50);
		flow.setCenter(vbox);
		Image image = new Image("menu.png");
		ImageView iv1 = new ImageView(image);
		iv1.fitWidthProperty().bind(this.widthProperty());
		iv1.fitHeightProperty().bind(this.heightProperty());
		
		StackPane stackPane = new StackPane();
		stackPane.getChildren().add(iv1); // hbox with button and text on top of image view
		stackPane.getChildren().add(flow);
		vbox.setAlignment(Pos.CENTER);

		
		HBox root = new HBox();
		root.getChildren().add(stackPane);
		this.setCenter(root);

	}
	
	
	public Button getNewGame() {
		return newGame;
	}
	public Button getLoadGame() {
		return loadGame;
	}

}
