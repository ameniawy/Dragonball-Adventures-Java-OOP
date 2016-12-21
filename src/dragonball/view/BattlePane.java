package dragonball.view;

import dragonball.controller.GUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class BattlePane extends StackPane {
	ImageView hP;
	ImageView hF;
	Label healthTP;
	Label healthTF;
	Label kiP;
	Label kiF;
	Label staminaP;
	Label staminaF;
	Label nameLvlP;
	Label nameLvlF;
	ImageView fighterPic;
	ImageView foePic;

	@SuppressWarnings("static-access")
	public BattlePane(GUI gui) {
		Image back = new Image("battle.jpg");
		ImageView background = new ImageView(back);
		background.fitWidthProperty().bind(this.widthProperty());
		background.fitHeightProperty().bind(this.heightProperty());

		Font font1 = Font.font("Cambria Math", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 30);

		BorderPane infoBar = new BorderPane();
		VBox fighter = new VBox();
		fighter.setAlignment(Pos.TOP_LEFT);
		hP = new ImageView(new Image("healthp.png"));
		this.nameLvlP = new Label("Fighter Name: ");
		this.nameLvlP.setFont(font1);
		this.nameLvlP.setTextFill(Color.rgb(0, 0, 0));
		this.nameLvlP.setAlignment(Pos.CENTER_LEFT);
		this.staminaP = new Label("Fighter stamina: ");
		this.staminaP.setFont(font1);
		this.staminaP.setTextFill(Color.rgb(0, 0, 0));
		this.staminaP.setAlignment(Pos.CENTER_LEFT);
		this.healthTP = new Label("Fighter's health");
		this.healthTP.setFont(font1);
		this.healthTP.setTextFill(Color.rgb(0, 0, 0));
		this.healthTP.setAlignment(Pos.CENTER_LEFT);
		this.kiP = new Label("Fighter's Ki: ");
		this.kiP.setFont(font1);
		this.kiP.setTextFill(Color.rgb(0, 0, 0));
		this.kiP.setAlignment(Pos.CENTER_LEFT);
		fighter.getChildren().addAll(hP, nameLvlP, healthTP, staminaP, kiP);
		VBox foe = new VBox();
		foe.setAlignment(Pos.TOP_RIGHT);
		hF = new ImageView(new Image("healthf.png"));
		this.nameLvlF = new Label("Foe Name: ");
		this.nameLvlF.setFont(font1);
		this.nameLvlF.setTextFill(Color.rgb(0, 0, 0));
		this.nameLvlF.setAlignment(Pos.CENTER_LEFT);
		this.staminaF = new Label("Foe stamina: ");
		this.staminaF.setFont(font1);
		this.staminaF.setTextFill(Color.rgb(0, 0, 0));
		this.staminaF.setAlignment(Pos.CENTER_LEFT);
		this.healthTF = new Label("Foe's health");
		this.healthTF.setFont(font1);
		this.healthTF.setTextFill(Color.rgb(0, 0, 0));
		this.healthTF.setAlignment(Pos.CENTER_LEFT);
		this.kiF = new Label("Foe's Ki: ");
		this.kiF.setFont(font1);
		this.kiF.setTextFill(Color.rgb(0, 0, 0));
		this.kiF.setAlignment(Pos.CENTER_LEFT);
		foe.getChildren().addAll(hF, nameLvlF, healthTF, staminaF, kiF);
		
		this.fighterPic = new ImageView(new Image("fighterPic.gif"));
		this.setAlignment(fighterPic, Pos.CENTER_LEFT);
		HBox fighterBox = new HBox();
		fighterBox.setPadding(new Insets(10, 10, 10, 10));
		fighterBox.getChildren().add(fighterPic);
		fighterBox.setAlignment(Pos.CENTER_LEFT);

		this.foePic = new ImageView(new Image("foePic.gif"));
		this.setAlignment(foePic, Pos.CENTER_RIGHT);
		HBox foeBox = new HBox();
		foeBox.setPadding(new Insets(10, 10, 10, 10));
		foeBox.getChildren().add(foePic);
		foeBox.setAlignment(Pos.CENTER_RIGHT);

		infoBar.setLeft(fighter);
		infoBar.setRight(foe);

		this.getChildren().addAll(background, infoBar, fighterBox, foeBox);
	}

	public void sethP(ImageView hP) {
		this.hP = hP;
	}

	public void sethF(ImageView hF) {
		this.hF = hF;
	}

	public void setHealthTP(Label healthTP) {
		this.healthTP = healthTP;
	}

	public void setHealthTF(Label healthTF) {
		this.healthTF = healthTF;
	}

	public void setKiP(Label kiP) {
		this.kiP = kiP;
	}

	public void setKiF(Label kiF) {
		this.kiF = kiF;
	}

	public void setStaminaP(Label staminaP) {
		this.staminaP = staminaP;
	}

	public void setStaminaF(Label staminaF) {
		this.staminaF = staminaF;
	}

	public void setNameLvlP(Label nameLvlP) {
		this.nameLvlP = nameLvlP;
	}

	public void setNameLvlF(Label nameLvlF) {
		this.nameLvlF = nameLvlF;
	}

	public void setFighterPic(ImageView fighterPic) {
		this.fighterPic = fighterPic;
	}

	public void setFoePic(ImageView foePic) {
		this.foePic = foePic;
	}

	public ImageView getFighterPic() {
		return fighterPic;
	}

	public ImageView getFoePic() {
		return foePic;
	}

	public ImageView gethP() {
		return hP;
	}

	public ImageView gethF() {
		return hF;
	}

	public Label getHealthTP() {
		return healthTP;
	}

	public Label getHealthTF() {
		return healthTF;
	}

	public Label getKiP() {
		return kiP;
	}

	public Label getKiF() {
		return kiF;
	}

	public Label getStaminaP() {
		return staminaP;
	}

	public Label getStaminaF() {
		return staminaF;
	}

	public Label getNameLvlP() {
		return nameLvlP;
	}

	public Label getNameLvlF() {
		return nameLvlF;
	}

}
