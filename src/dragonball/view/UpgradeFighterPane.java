package dragonball.view;

import dragonball.controller.GUI;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class UpgradeFighterPane extends StackPane {

	Label health;
	Label physical;
	Label stamina;
	Label ki;
	Label blast;
	Label abilityPoints;
	Button healthBtn;
	Button physicalBtn;
	Button staminaBtn;
	Button kiBtn;
	Button blastBtn;
	Button okay;

	@SuppressWarnings("unchecked")
	public UpgradeFighterPane(GUI gui) {
		ImageView background = new ImageView(new Image("upgrade.jpg"));
		background.fitWidthProperty().bind(this.widthProperty());
		background.fitHeightProperty().bind(this.heightProperty());
		Font font1 = new Font("Cambria Math", 40);
		Font font2 = new Font("Cambria Math", 40);

		VBox info = new VBox(50);
		VBox buttons = new VBox(20);
		HBox root = new HBox(50);

		this.okay = new Button("I'm finished");
		this.okay.setFont(font2);

		this.abilityPoints = new Label("Available Ability Points: ");
		this.abilityPoints.setFont(font1);
		this.health = new Label("Max Health Points: ");
		this.health.setFont(font1);
		this.physical = new Label("Physical Damge: ");
		this.physical.setFont(font1);
		this.stamina = new Label("Max Stamina: ");
		this.stamina.setFont(font1);
		this.ki = new Label("Max Ki: ");
		this.ki.setFont(font1);
		this.blast = new Label("Blast Damage: ");
		this.blast.setFont(font1);
		
		health.setTextFill(Color.rgb(227,0,9));
		physical.setTextFill(Color.rgb(227,0,9));
		stamina.setTextFill(Color.rgb(227,0,9));
		ki.setTextFill(Color.rgb(227,0,9));
		blast.setTextFill(Color.rgb(227,0,9));
		abilityPoints.setTextFill(Color.rgb(227,0,9));
		//loadGame.setTextFill(Color.rgb(227,0,9));
		
		this.healthBtn = new Button("Upgrade HP");
		this.healthBtn.setFont(font2);
		this.staminaBtn = new Button("Upgrade Stamina");
		this.staminaBtn.setFont(font2);
		this.kiBtn = new Button("Upgrade Ki");
		this.kiBtn.setFont(font2);
		this.blastBtn = new Button("Upgrade BDamage");
		this.blastBtn.setFont(font2);
		this.physicalBtn = new Button("Upgrade PDamage");
		this.physicalBtn.setFont(font2);
		
		this.healthBtn.setPrefWidth(400);
		this.staminaBtn.setPrefWidth(400);
		this.kiBtn.setPrefWidth(400);
		this.blastBtn.setPrefWidth(400);
		this.okay.setPrefWidth(400);
		this.physicalBtn.setPrefWidth(400);

		this.healthBtn.setOnAction(gui);
		this.staminaBtn.setOnAction(gui);
		this.kiBtn.setOnAction(gui);
		this.blastBtn.setOnAction(gui);
		this.okay.setOnAction(gui);
		this.physicalBtn.setOnAction(gui);
		
		info.getChildren().addAll(health, stamina, physical, blast, ki,abilityPoints);
		buttons.getChildren().addAll(healthBtn, staminaBtn, physicalBtn, blastBtn, kiBtn,okay);
		
		root.getChildren().addAll(info, buttons);
		root.setPadding(new Insets(60, 60, 60, 60));		

		this.getChildren().addAll(background, root);
	}

	public Label getAbilityPoints() {
		return abilityPoints;
	}

	public Button getOkay() {
		return okay;
	}

	public Label getHealth() {
		return health;
	}

	public Label getPhysical() {
		return physical;
	}

	public Label getStamina() {
		return stamina;
	}

	public Label getKi() {
		return ki;
	}

	public Label getBlast() {
		return blast;
	}

	public Button getHealthBtn() {
		return healthBtn;
	}

	public Button getPhysicalBtn() {
		return physicalBtn;
	}

	public Button getStaminaBtn() {
		return staminaBtn;
	}

	public Button getKiBtn() {
		return kiBtn;
	}

	public Button getBlastBtn() {
		return blastBtn;
	}
}
