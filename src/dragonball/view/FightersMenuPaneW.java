package dragonball.view;

import dragonball.controller.GUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class FightersMenuPaneW extends StackPane {

	Button saiyan;
	Button majin;
	Button namekian;
	Button frieza;
	Button earthling;
	TextField fighterName;
	TextField playerName;
	Label fighterPic;
	Label fighterInfo;
	private Button okay;
	private String choosenFighter;

	@SuppressWarnings({ "unchecked", "static-access" })
	public FightersMenuPaneW(GUI gui) {
		// default fighter
		choosenFighter = "Saiyan";
		Image image = new Image("background.png");
		ImageView iv1 = new ImageView(image);
		iv1.fitWidthProperty().bind(this.widthProperty());
		iv1.fitHeightProperty().bind(this.heightProperty());
		this.getChildren().add(iv1);
		Font font = Font.font("Cambria Math", FontWeight.BOLD, FontPosture.REGULAR, 15);

		BorderPane root = new BorderPane();

		VBox top = new VBox(20);
		top.setPrefSize(200, 200);
		top.setPadding(new Insets(100, 250, 10, 250));
		Label chooseFighter = new Label("Choose a Fighter!");
		chooseFighter.setFont(Font.font("Cambria Math", FontWeight.BOLD, FontPosture.REGULAR, 40));
		chooseFighter.setTextFill(Color.rgb(227, 0, 9));
		HBox hbox = new HBox();
		hbox.getChildren().add(chooseFighter);
		hbox.setAlignment(Pos.CENTER);

		this.playerName = new TextField();
		this.playerName.setPromptText("Enter Player's Name");
		this.playerName.resize(300, 50);
		this.playerName.setPrefSize(300, 30);
		this.playerName.setPrefColumnCount(5);

		top.getChildren().add(hbox);
		GridPane content = new GridPane();
		BorderPane choosingFighter = new BorderPane();

		this.fighterName = new TextField();
		this.fighterName.setPromptText("Enter Fighter's name");

		HBox fighters = new HBox();
		this.saiyan = new Button("Saiyan");
		this.saiyan.setPrefSize(100, 100);
		this.saiyan.setFont(font);
		saiyan.setOnAction(gui);

		this.frieza = new Button("Frieza");
		this.frieza.setPrefSize(100, 100);
		this.frieza.setFont(font);
		frieza.setOnAction(gui);

		this.namekian = new Button("Namekian");
		this.namekian.setPrefSize(100, 100);
		this.namekian.setFont(font);
		namekian.setOnAction(gui);

		this.earthling = new Button("Earthling");
		this.earthling.setPrefSize(100, 100);
		this.earthling.setFont(font);
		earthling.setOnAction(gui);

		this.majin = new Button("Majin");
		this.majin.setPrefSize(100, 100);
		this.majin.setFont(font);
		majin.setOnAction(gui);

		fighters.getChildren().addAll(saiyan, frieza, namekian, earthling, majin);
		fighters.setPadding(new Insets(50, 20, 10, 20));
		fighters.setAlignment(Pos.CENTER);
		choosingFighter.setTop(fighterName);
		choosingFighter.setCenter(fighters);

		BorderPane info = new BorderPane();
		info.setPadding(new Insets(10, 20, 50, 20));
		this.fighterPic = new Label("Display Picture");
		fighterPic.setFont(Font.font("Constantia ", FontWeight.LIGHT, FontPosture.ITALIC, 20));
		fighterPic.setTextFill(Color.rgb(211, 211, 211));
		this.fighterInfo = new Label("Fighter's Info");
		this.fighterInfo.setAlignment(Pos.BOTTOM_LEFT);
		this.fighterInfo.setFont(Font.font("Constantia ", FontWeight.BOLD, FontPosture.ITALIC, 20));
		fighterInfo.setTextFill(Color.rgb(211, 211, 211));

		info.setRight(fighterPic);
		info.setLeft(fighterInfo);

		this.okay = new Button("I'm finished");
		this.okay.setFont(font);
		this.okay.setOnAction(gui);
		getOkay().setAlignment(Pos.CENTER);
		getOkay().setPrefSize(100, 50);
		content.add(info, 0, 0);
		content.setAlignment(Pos.CENTER);
		content.add(choosingFighter, 0, 1);
		root.setCenter(content);
		root.setTop(top);
		root.setBottom(getOkay());
		root.setAlignment(getOkay(), Pos.CENTER);
		root.setPadding(new Insets(10, 20, 100, 20));
		this.getChildren().add(root);

	}

	public TextField getPlayerName() {
		return playerName;
	}

	public Button getSaiyan() {
		return saiyan;
	}

	public Button getMajin() {
		return majin;
	}

	public Button getNamekian() {
		return namekian;
	}

	public Button getFrieza() {
		return frieza;
	}

	public Button getEarthling() {
		return earthling;
	}

	public TextField getFighterName() {
		return fighterName;
	}

	public Label getFighterPic() {
		return fighterPic;
	}

	public Label getFighterInfo() {
		return fighterInfo;
	}

	public String getChoosenFighter() {
		return choosenFighter;
	}

	public void setChoosenFighter(String choosenFighter) {
		this.choosenFighter = choosenFighter;
	}

	public Button getOkay() {
		return okay;
	}

}
