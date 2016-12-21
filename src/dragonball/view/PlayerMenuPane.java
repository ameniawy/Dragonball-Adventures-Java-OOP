package dragonball.view;

import dragonball.controller.GUI;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class PlayerMenuPane extends StackPane {
	private Button newGame;
	private Button saveGame;
	private Button loadGame;
	private Button continueBtn;
	private Button createNewFighter;
	private Button switchFighter;
	private Button assignAttack;
	private Button upgradeFighter;
	private Button exit;

	@SuppressWarnings("unchecked")
	public PlayerMenuPane(GUI gui) {

		Image image = new Image("menu.png");
		ImageView iv1 = new ImageView(image);
		iv1.fitWidthProperty().bind(this.widthProperty());
		iv1.fitHeightProperty().bind(this.heightProperty());
		this.getChildren().add(iv1);

		FlowPane buttons = new FlowPane();
		buttons.setOrientation(Orientation.VERTICAL);
		buttons.setVgap(20);
		
		Font font = new Font ("Cambria Math" , 35);

		this.exit = new Button("Exit");
		this.exit.setPrefSize(330, 50);
		this.exit.setFont(font);
		exit.setOnAction(gui);
		
		this.continueBtn = new Button("Continue Button");
		this.continueBtn.setPrefSize(330, 50);
		this.continueBtn.setFont(font);
		continueBtn.setOnAction(gui);

		this.newGame = new Button("New Game");
		this.newGame.setPrefSize(330, 50);
		this.newGame.setFont(font);
		newGame.setOnAction(gui);

		this.saveGame = new Button("Save Game");
		this.saveGame.setPrefSize(330, 50);
		this.saveGame.setFont(font);
		saveGame.setOnAction(gui);

		this.loadGame = new Button("Load Game");
		this.loadGame.setPrefSize(330, 50);
		this.loadGame.setFont(font);
		loadGame.setOnAction(gui);

		this.createNewFighter = new Button("Creat New Fighter");
		this.createNewFighter.setPrefSize(330, 50);
		this.createNewFighter.setFont(font);
		createNewFighter.setOnAction(gui);

		this.switchFighter = new Button("Switch Fighter");
		this.switchFighter.setPrefSize(330, 50);
		this.switchFighter.setFont(font);
		switchFighter.setOnAction(gui);

		this.assignAttack = new Button("Assign Attack");
		this.assignAttack.setPrefSize(330, 50);
		this.assignAttack.setFont(font);
		assignAttack.setOnAction(gui);

		this.upgradeFighter = new Button("Upgrade Fighter");
		this.upgradeFighter.setPrefSize(330, 50);
		this.upgradeFighter.setFont(font);
		upgradeFighter.setOnAction(gui);

		buttons.getChildren().addAll(continueBtn, newGame, saveGame, loadGame, createNewFighter, switchFighter,
				assignAttack, upgradeFighter,exit);
		buttons.setAlignment(Pos.CENTER);

		this.getChildren().add(buttons);
	}

	public Button getNewGame() {
		return newGame;
	}

	public Button getSaveGame() {
		return saveGame;
	}

	public Button getLoadGame() {
		return loadGame;
	}

	public Button getCreateNewFighter() {
		return createNewFighter;
	}

	public Button getSwitchFighter() {
		return switchFighter;
	}

	public Button getAssignAttack() {
		return assignAttack;
	}

	public Button getUpgradeFighter() {
		return upgradeFighter;
	}

	public Button getContinueBtn() {
		return continueBtn;
	}

	public Button getExit() {
		return exit;
	}



}
