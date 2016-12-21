package dragonball.view;

import dragonball.controller.GUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class WorldPane extends StackPane {

	public Label[][] cells;
	Label playerName;
	Label fighterName;
	Label fighterLevel;
	Label dragonballs;
	Label senzuBeans;
	private Button menu;

	@SuppressWarnings("unchecked")
	public WorldPane(GUI gui) {
		BorderPane root = new BorderPane();
		GridPane map = new GridPane();
		FlowPane info = new FlowPane();
		info.setAlignment(Pos.CENTER);
		this.cells = new Label[10][10];
		// setting map
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				cells[i][j] = new Label();
				Image image = new Image("greenGrass.jpg");
				ImageView imageView2 = new ImageView(image);
				imageView2.fitWidthProperty().bind(cells[i][j].widthProperty());
				imageView2.fitHeightProperty().bind(cells[i][j].heightProperty());
				cells[i][j].setGraphic(imageView2);
				map.add(cells[i][j], j, i);
			}
		Image image = new Image("player.gif");
		ImageView imageView2 = new ImageView(image);
		imageView2.fitWidthProperty().bind(cells[9][9].widthProperty());
		imageView2.fitHeightProperty().bind(cells[9][9].heightProperty());
		cells[9][9].setGraphic(imageView2);

		Image image3 = new Image("boss.gif");
		ImageView imageView3 = new ImageView(image3);
		imageView3.fitWidthProperty().bind(cells[0][0].widthProperty());
		imageView3.fitHeightProperty().bind(cells[0][0].heightProperty());
		cells[0][0].setGraphic(imageView3);
		map.setAlignment(Pos.CENTER);
		map.autosize();

		// setting info
		Font font1 = Font.font("Cambria Math", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20);
		this.playerName = new Label("Player Name: ");
		this.fighterName = new Label("Fighter Name: ");
		this.fighterLevel = new Label("Fighter Level: 1");
		this.dragonballs = new Label("Dragonballs: 0");
		this.senzuBeans = new Label("Senzu Beans: 0");
		this.playerName.setFont(font1);
		this.fighterName.setFont(font1);
		this.fighterLevel.setFont(font1);
		this.dragonballs.setFont(font1);
		this.senzuBeans.setFont(font1);
		this.playerName.setTextFill(Color.rgb(227, 0, 0));
		this.fighterName.setTextFill(Color.rgb(227, 0, 0));
		this.fighterLevel.setTextFill(Color.rgb(227, 0, 0));
		this.dragonballs.setTextFill(Color.rgb(227, 0, 0));
		this.senzuBeans.setTextFill(Color.rgb(227, 0, 0));
		info.setPadding(new Insets(10, 10, 10, 10));
		info.setHgap(45);
		info.setVgap(5);
		menu = new Button("MENU");
		menu.setOnAction(gui);
		info.getChildren().addAll(playerName, fighterName, fighterLevel, dragonballs, senzuBeans, menu);
		root.setTop(info);
		root.setCenter(map);

		Image back = new Image("back4.jpg");
		ImageView background = new ImageView(back);
		background.fitWidthProperty().bind(this.widthProperty());
		background.fitHeightProperty().bind(this.heightProperty());
		this.getChildren().add(background);
		this.getChildren().add(root);

	}

	public Label[][] getCells() {
		return cells;
	}

	public Label getPlayerName() {
		return playerName;
	}

	public Label getFighterName() {
		return fighterName;
	}

	public Label getFighterLevel() {
		return fighterLevel;
	}

	public Label getDragonballs() {
		return dragonballs;
	}

	public Label getSenzuBeans() {
		return senzuBeans;
	}

	public Button getMenu() {
		return menu;
	}

}
