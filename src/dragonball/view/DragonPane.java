package dragonball.view;

import dragonball.controller.GUI;
import dragonball.model.dragon.Dragon;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class DragonPane extends StackPane {
	Label name;
	Label superAttacks;
	Label ultimateAttacks;
	Label senzuBeans;
	Label abilityPoints;
	Button superAttacksBtn;
	Button ultimateAttacksBtn;
	Button senzuBeansBtn;
	Button abilityPointsBtn;
	Dragon dragon;

	@SuppressWarnings("unchecked")
	public DragonPane(GUI gui) {
		ImageView background = new ImageView(new Image("back4.jpg"));
		background.fitWidthProperty().bind(this.widthProperty());
		background.fitHeightProperty().bind(this.heightProperty());

		Font font1 = Font.font("Cambria Math", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20);
		Font font2 = Font.font("Cambria Math", FontWeight.BOLD, FontPosture.REGULAR, 30);
		Font font3 = Font.font("Cambria Math", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 50);

		this.name = new Label("name");
		this.name.setFont(font3);
		this.name.setAlignment(Pos.CENTER);

		GridPane btnlbl = new GridPane();
		btnlbl.setHgap(30);
		btnlbl.setVgap(50);
		btnlbl.setAlignment(Pos.CENTER);
		HBox labels = new HBox(40);
		this.superAttacks = new Label("Super Attack");
		this.superAttacks.setFont(font1);
		this.superAttacks.setTextFill(Color.rgb(227, 0, 0));
		this.superAttacks.setAlignment(Pos.CENTER);

		this.ultimateAttacks = new Label("Ultimate Attack");
		this.ultimateAttacks.setFont(font1);
		this.ultimateAttacks.setTextFill(Color.rgb(227, 0, 0));
		this.ultimateAttacks.setAlignment(Pos.CENTER);

		this.senzuBeans = new Label("Senzu Bean");
		this.senzuBeans.setFont(font1);
		this.senzuBeans.setTextFill(Color.rgb(227, 0, 0));
		this.senzuBeans.setAlignment(Pos.CENTER);

		this.abilityPoints = new Label("Ability Points");
		this.abilityPoints.setTextFill(Color.rgb(227, 0, 0));
		this.abilityPoints.setFont(font1);
		this.abilityPoints.setAlignment(Pos.CENTER);

		labels.getChildren().addAll(senzuBeans, abilityPoints, superAttacks, ultimateAttacks);

		HBox buttons = new HBox(40);
		this.superAttacksBtn = new Button("Super Attack");
		this.superAttacksBtn.setFont(font2);
		this.superAttacksBtn.setAlignment(Pos.CENTER);
		this.superAttacksBtn.setOnAction(gui);

		this.ultimateAttacksBtn = new Button("Ultimate Attack");
		this.ultimateAttacksBtn.setFont(font2);
		this.ultimateAttacksBtn.setAlignment(Pos.CENTER);
		this.ultimateAttacksBtn.setOnAction(gui);

		this.senzuBeansBtn = new Button("Senzu Bean");
		this.senzuBeansBtn.setFont(font2);
		this.senzuBeansBtn.setAlignment(Pos.CENTER);
		this.senzuBeansBtn.setOnAction(gui);

		this.abilityPointsBtn = new Button("Abiltiy Point");
		this.abilityPointsBtn.setFont(font2);
		this.abilityPointsBtn.setAlignment(Pos.CENTER);
		this.abilityPointsBtn.setOnAction(gui);

		buttons.getChildren().addAll(senzuBeansBtn, abilityPointsBtn, superAttacksBtn, ultimateAttacksBtn);
		btnlbl.add(senzuBeans, 0, 0);
		btnlbl.add(abilityPoints, 1, 0);
		btnlbl.add(superAttacks, 2, 0);
		btnlbl.add(ultimateAttacks, 3, 0);
		btnlbl.add(senzuBeansBtn, 0, 1);
		btnlbl.add(abilityPointsBtn, 1, 1);
		btnlbl.add(superAttacksBtn, 2, 1);
		btnlbl.add(ultimateAttacksBtn, 3, 1);
		VBox root = new VBox(30);
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(name, btnlbl);
		this.getChildren().addAll(background, root);
	}

	public Label getAbilityPoints() {
		return abilityPoints;
	}

	public Button getSuperAttacksBtn() {
		return superAttacksBtn;
	}

	public Button getUltimateAttacksBtn() {
		return ultimateAttacksBtn;
	}

	public Button getSenzuBeansBtn() {
		return senzuBeansBtn;
	}

	public Button getAbilityPointsBtn() {
		return abilityPointsBtn;
	}

	public Label getName() {
		return name;
	}

	public Label getSuperAttacks() {
		return superAttacks;
	}

	public Label getUltimateAttacks() {
		return ultimateAttacks;
	}

	public Label getSenzuBeans() {
		return senzuBeans;
	}

	public Dragon getDragon() {
		return dragon;
	}

	public void setDragon(Dragon dragon) {
		this.dragon = dragon;
	}
}
