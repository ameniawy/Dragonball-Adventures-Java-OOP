package dragonball.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JOptionPane;

import dragonball.bonus.FoeBrain;
import dragonball.model.attack.Attack;
import dragonball.model.attack.PhysicalAttack;
import dragonball.model.attack.SuperAttack;
import dragonball.model.attack.UltimateAttack;
import dragonball.model.battle.Battle;
import dragonball.model.battle.BattleEvent;
import dragonball.model.battle.BattleEventType;
import dragonball.model.cell.Collectible;
import dragonball.model.character.fighter.Earthling;
import dragonball.model.character.fighter.Fighter;
import dragonball.model.character.fighter.Frieza;
import dragonball.model.character.fighter.Majin;
import dragonball.model.character.fighter.Namekian;
import dragonball.model.character.fighter.NonPlayableFighter;
import dragonball.model.character.fighter.PlayableFighter;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.dragon.Dragon;
import dragonball.model.dragon.DragonWishType;
import dragonball.model.exceptions.DuplicateAttackException;
import dragonball.model.exceptions.MapIndexOutOfBoundsException;
import dragonball.model.exceptions.MaximumAttacksLearnedException;
import dragonball.model.exceptions.MissingFieldException;
import dragonball.model.exceptions.NotASaiyanException;
import dragonball.model.exceptions.NotEnoughAbilityPointsException;
import dragonball.model.exceptions.NotEnoughKiException;
import dragonball.model.exceptions.NotEnoughSenzuBeansException;
import dragonball.model.exceptions.UnknownAttackTypeException;
import dragonball.model.game.Game;
import dragonball.model.game.GameListener;
import dragonball.model.game.GameState;
import dragonball.view.BattlePane;
import dragonball.view.DragonPane;
import dragonball.view.FightersMenuPane;
import dragonball.view.FightersMenuPaneW;
import dragonball.view.PlayerMenuPane;
import dragonball.view.StartMenuPane;
import dragonball.view.UpgradeFighterPane;
import dragonball.view.WorldPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

@SuppressWarnings({ "rawtypes", "serial" })
public class GUI extends Application implements EventHandler, GameListener {
	FoeBrain foeBrain;
	
	Game game;
	transient ImageView imageView2;
	transient WorldPane world;
	transient StartMenuPane startMenu;
	transient FightersMenuPane fightersMenu;
	transient PlayerMenuPane playerMenu;
	transient BattlePane battle;
	transient FightersMenuPaneW fightersMn;
	transient DragonPane dragon;
	transient UpgradeFighterPane upgrade;
	transient Scene upgradeScene;
	transient Scene battleScene;
	transient Scene worldScene;
	transient Scene startMenuScene;
	transient Scene playerMenuScene;
	transient Scene fightersMenuScene;
	transient Scene fightersMnScene;
	transient Scene dragonScene;
	transient Stage stage;
	static Thread thread;
	static File sound;
	static boolean muted = false; // This should explain itself
	static float volume = 100.0f; // This is the volume that goes from 0 to 100
	static float pan = 0.0f;
	// The balance between the speakers 0 is both sides and it goes from -1 to 1
	// sound starts playing
	static double seconds = 0.0d; // The amount of seconds to wait before the
									// sound starts playing
	static boolean looped_forever = false;
	// It will keep looping forever if this is true
	static int loop_times = 0;
	// Set the amount of extra times you want the sound to loop (you don't need
	// to have looped_forever set to true)
	static int loops_done = 0;

	// When the program is running this is counting the times the sound has
	// looped so it knows when to stop
	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {

		sound = new File("sound.wav"); // Write you own file location
		thread = new Thread(play);
		thread.start();
		looped_forever = true;
		stage = new Stage();
		game = new Game();
		world = new WorldPane(this);
		this.worldScene = new Scene(world);
		worldScene.setOnKeyReleased(this);

		startMenu = new StartMenuPane(this);
		this.startMenuScene = new Scene(startMenu);

		fightersMenu = new FightersMenuPane(this);
		this.fightersMenuScene = new Scene(fightersMenu);

		fightersMn = new FightersMenuPaneW(this);
		this.fightersMnScene = new Scene(fightersMn);
		fightersMn.getPlayerName().setVisible(false);

		playerMenu = new PlayerMenuPane(this);
		this.playerMenuScene = new Scene(playerMenu);

		upgrade = new UpgradeFighterPane(this);
		this.upgradeScene = new Scene(upgrade);

		battle = new BattlePane(this);
		this.battleScene = new Scene(battle);

		dragon = new DragonPane(this);
		this.dragonScene = new Scene(dragon);
		stage.setScene(startMenuScene);
		stage.sizeToScene();
		stage.setOnCloseRequest(this);
		stage.setFullScreen(true);
		stage.show();

	}

	public FightersMenuPaneW getFightersMn() {
		return fightersMn;
	}

	public Scene getFightersMnScene() {
		return fightersMnScene;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void handle(Event event) {
		// System.out.println(event.getEventType().getName());
		if (event.getEventType().getName().equals("WINDOW_CLOSE_REQUEST")) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Exit");
			alert.setHeaderText("Do you want to leave?");
			alert.setContentText("I didn't think you're a quitter.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				looped_forever = false;
				thread.stop();
				stage.close();
			} else {
				// ... user chose CANCEL or closed the dialog
				event.consume();
			}
		} else if (event.getEventType().getSuperType().getName().equals("KEY")) {
			KeyEvent keyEvent = (KeyEvent) event;

			if (event.getEventType().getName().equals("KEY_RELEASED")) {
				if (game.getState() == GameState.WORLD || true) {
					int row = game.getWorld().getPlayerRow();
					int column = game.getWorld().getPlayerColumn();
					imageView2 = new ImageView(new Image("greenGrass.jpg"));
					imageView2.fitWidthProperty().bind(world.getCells()[row][column].widthProperty());
					imageView2.fitHeightProperty().bind(world.getCells()[row][column].heightProperty());
					world.getCells()[row][column].setGraphic(imageView2);
					try {
						switch (keyEvent.getCode()) {
						case UP:
							game.getWorld().moveUp();
							break;
						case DOWN:
							game.getWorld().moveDown();
							break;
						case LEFT:
							game.getWorld().moveLeft();
							break;
						case RIGHT:
							game.getWorld().moveRight();
							break;
						default:
							break;
						}
					} catch (MapIndexOutOfBoundsException e) {
					}
					row = game.getWorld().getPlayerRow();
					column = game.getWorld().getPlayerColumn();
					imageView2 = new ImageView(new Image("player.gif"));
					imageView2.fitWidthProperty().bind(world.getCells()[row][column].widthProperty());
					imageView2.fitHeightProperty().bind(world.getCells()[row][column].heightProperty());
					world.getCells()[row][column].setGraphic(imageView2);
					// game.onDragonCalled();
					//this.game.getPlayer().setDragonBalls(6);

				}
				/*
				 * if (keyEvent.getCode() ==
				 * KeyCode.ESCAPE&&game.getState()!=GameState.BATTLE) { Alert
				 * alertC = new Alert(AlertType.CONFIRMATION);
				 * alertC.setTitle("Exit"); alertC.setHeaderText(
				 * "Do you want to leave?"); alertC.setContentText(
				 * "I didn't think you're a quitter."); alertC.initOwner(stage);
				 * Optional<ButtonType> resultC = alertC.showAndWait(); if
				 * (resultC.get() == ButtonType.OK) { looped_forever = false;
				 * thread.stop(); stage.close(); alertC.close(); } }
				 */

			}
		} else if (event.getEventType().getName().equals("ACTION")) {
			ActionEvent action = (ActionEvent) event;
			if (action.getSource() == this.getStartMenu().getNewGame()) {
				// scene.setOnKeyReleased(this);
				stage.setScene(this.fightersMenuScene);
				stage.sizeToScene();
				stage.setFullScreen(true);
			} else if (action.getSource() == this.getStartMenu().getLoadGame()) {
				
				Boolean flagFile = true;
				Boolean flag = false;
				do {
					FileChooser fileChooser = new FileChooser();
					FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SER files (*.ser)",
							"*.ser");
					fileChooser.getExtensionFilters().add(extFilter);
					fileChooser.setTitle("Open Saved File");
					File loadFile = fileChooser.showOpenDialog(stage);
					if (loadFile == null) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Not Loaded");
						alert.setContentText("You didn't select a file.");
						alert.initOwner(this.stage);
						alert.showAndWait();
						flag = false;
						flagFile = false;
					} else {
						String path = loadFile.getPath();
						try {
							game.load(path);
							flag = false;
						} catch (ClassNotFoundException v) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error");
							alert.setHeaderText("Corrupted File, choose again.");
							alert.setContentText(v.getMessage());
							alert.initOwner(this.stage);
							alert.showAndWait();
							flag = true;
						} catch (IOException b) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error");
							alert.setHeaderText("IOException, choose again.");
							alert.setContentText(b.getMessage());
							alert.initOwner(this.stage);
							alert.showAndWait();
							flag = true;
						}
					}

				} while (flag);

				game.setListener(this);
				int row = game.getWorld().getPlayerRow();
				int column = game.getWorld().getPlayerColumn();
				imageView2 = new ImageView(new Image("greenGrass.jpg"));
				imageView2.fitWidthProperty().bind(world.getCells()[9][9].widthProperty());
				imageView2.fitHeightProperty().bind(world.getCells()[9][9].heightProperty());
				world.getCells()[9][9].setGraphic(imageView2);

				imageView2 = new ImageView(new Image("player.gif"));
				imageView2.fitWidthProperty().bind(world.getCells()[row][column].widthProperty());
				imageView2.fitHeightProperty().bind(world.getCells()[row][column].heightProperty());
				world.getCells()[row][column].setGraphic(imageView2);
				
				if (flagFile){
					this.getWorld().getPlayerName().setText("Player's Name: " + game.getPlayer().getName());
					this.getWorld().getFighterName().setText("Fighter's Name: " + game.getPlayer().getActiveFighter().getName());
					this.getWorld().getFighterLevel().setText("Fighter's Level: "+game.getPlayer().getActiveFighter().getLevel());
					this.getWorld().getSenzuBeans().setText("Senzu Beans: "+game.getPlayer().getSenzuBeans());
					this.getWorld().getDragonballs().setText("Dragonballs: "+game.getPlayer().getDragonBalls());
					stage.setScene(this.worldScene);
				}
				stage.sizeToScene();
				stage.setOnCloseRequest(this);
				stage.setFullScreen(true);
				stage.show();

			} else if (action.getSource() == this.getFightersMenu().getSaiyan()) {
				Saiyan fighter = new Saiyan("");
				this.getFightersMenu().getFighterInfo()
						.setText("MaxHP :" + fighter.getMaxHealthPoints() + "\nMax Stamina: " + fighter.getMaxStamina()
								+ "\nMax Ki: " + fighter.getMaxKi() + "\nPhysical Damag: " + fighter.getPhysicalDamage()
								+ "\nBlast Damage: " + fighter.getBlastDamage());
				this.getFightersMenu().setChoosenFighter("Saiyan");
				imageView2 = new ImageView(new Image("saiyan.png"));
				imageView2.setSmooth(true);
				this.getFightersMenu().getFighterPic().setText("");
				this.getFightersMenu().getFighterPic().setGraphic(imageView2);

			} else if (action.getSource() == this.getFightersMenu().getFrieza()) {
				Frieza fighter = new Frieza("");
				this.getFightersMenu().getFighterInfo()
						.setText("MaxHP :" + fighter.getMaxHealthPoints() + "\nMax Stamina: " + fighter.getMaxStamina()
								+ "\nMax Ki: " + fighter.getMaxKi() + "\nPhysical Damag: " + fighter.getPhysicalDamage()
								+ "\nBlast Damage: " + fighter.getBlastDamage());
				this.getFightersMenu().setChoosenFighter("Frieza");
				imageView2 = new ImageView(new Image("frieza.png"));
				imageView2.setSmooth(true);
				this.getFightersMenu().getFighterPic().setText("");
				this.getFightersMenu().getFighterPic().setGraphic(imageView2);

			} else if (action.getSource() == this.getFightersMenu().getNamekian()) {
				Namekian fighter = new Namekian("");
				this.getFightersMenu().getFighterInfo()
						.setText("MaxHP :" + fighter.getMaxHealthPoints() + "\nMax Stamina: " + fighter.getMaxStamina()
								+ "\nMax Ki: " + fighter.getMaxKi() + "\nPhysical Damag: " + fighter.getPhysicalDamage()
								+ "\nBlast Damage: " + fighter.getBlastDamage());
				this.getFightersMenu().setChoosenFighter("Namekian");
				imageView2 = new ImageView(new Image("namekian.png"));
				imageView2.setSmooth(true);
				this.getFightersMenu().getFighterPic().setText("");
				this.getFightersMenu().getFighterPic().setGraphic(imageView2);

			} else if (action.getSource() == this.getFightersMenu().getMajin()) {
				Majin fighter = new Majin("");
				this.getFightersMenu().getFighterInfo()
						.setText("MaxHP :" + fighter.getMaxHealthPoints() + "\nMax Stamina: " + fighter.getMaxStamina()
								+ "\nMax Ki: " + fighter.getMaxKi() + "\nPhysical Damag: " + fighter.getPhysicalDamage()
								+ "\nBlast Damage: " + fighter.getBlastDamage());
				this.getFightersMenu().setChoosenFighter("Majin");
				imageView2 = new ImageView(new Image("majin.png"));
				imageView2.setSmooth(true);
				this.getFightersMenu().getFighterPic().setText("");
				this.getFightersMenu().getFighterPic().setGraphic(imageView2);

			} else if (action.getSource() == this.getFightersMenu().getEarthling()) {
				Earthling fighter = new Earthling("");
				this.getFightersMenu().getFighterInfo()
						.setText("MaxHP :" + fighter.getMaxHealthPoints() + "\nMax Stamina: " + fighter.getMaxStamina()
								+ "\nMax Ki: " + fighter.getMaxKi() + "\nPhysical Damag: " + fighter.getPhysicalDamage()
								+ "\nBlast Damage: " + fighter.getBlastDamage());
				this.getFightersMenu().setChoosenFighter("Earthling");
				imageView2 = new ImageView(new Image("earthling.png"));
				imageView2.setSmooth(true);
				this.getFightersMenu().getFighterPic().setText("");
				this.getFightersMenu().getFighterPic().setGraphic(imageView2);
			} else if (action.getSource() == this.getFightersMenu().getOkay()) {
				String playerName = this.getFightersMenu().getPlayerName().getText();
				String fighterName = this.getFightersMenu().getFighterName().getText();
				String race = this.getFightersMenu().getChoosenFighter();
				PlayableFighter fighter = null;
				switch (race) {
				case "Earthling":
					fighter = new Earthling(fighterName);
					break;
				case "Saiyan":
					fighter = new Saiyan(fighterName);
					break;
				case "Namekian":
					fighter = new Namekian(fighterName);
					break;
				case "Frieza":
					fighter = new Frieza(fighterName);
					break;
				case "Majin":
					fighter = new Majin(fighterName);
					break;
				default:
					fighter = new Saiyan(fighterName);
					break;
				}

				try {
					game = new Game();
				} catch (MissingFieldException r) {
					JOptionPane.showMessageDialog(null, "missing field Exception");
				} catch (UnknownAttackTypeException ee) {
					JOptionPane.showMessageDialog(null, "unknown attackType");
				}
				game.getPlayer().setName(playerName);
				game.getPlayer().getFighters().add(fighter);
				game.getPlayer().setActiveFighter(fighter);

				this.getWorld().getPlayerName().setText("Player's Name: " + playerName);
				this.getWorld().getFighterName().setText("Fighter's Name: " + fighterName);
				this.getWorld().getFighterLevel().setText("Fighter's Level: 1");
				this.getWorld().getSenzuBeans().setText("Senzu Beans: 0");
				this.getWorld().getDragonballs().setText("Dragonballs: 0");

				game.setListener(this);
				world = new WorldPane(this);
				this.worldScene = new Scene(world);
				worldScene.setOnKeyReleased(this);
				this.getWorld().getPlayerName().setText("Player's Name: " + playerName);
				this.getWorld().getFighterName().setText("Fighter's Name: " + fighterName);
				this.getWorld().getFighterLevel().setText("Fighter's Level: "+game.getPlayer().getActiveFighter().getLevel());
				this.getWorld().getSenzuBeans().setText("Senzu Beans: "+game.getPlayer().getSenzuBeans());
				this.getWorld().getDragonballs().setText("Dragonballs: "+game.getPlayer().getDragonBalls());
				stage.setScene(this.worldScene);
				stage.sizeToScene();
				stage.centerOnScreen();
				stage.setFullScreen(true);
				// stage.initStyle(StageStyle.UTILITY);

			} else if (action.getSource() == this.getWorld().getMenu()) {
				stage.setScene(this.playerMenuScene);
				stage.sizeToScene();
				stage.centerOnScreen();
				stage.setFullScreen(true);
				// stage.centerOnScreen();
			} else if (action.getSource() == this.getPlayerMenu().getNewGame()) {
				System.out.println("new");
				// scene.setOnKeyReleased(this);
				stage.setScene(this.fightersMenuScene);
				stage.sizeToScene();
				stage.centerOnScreen();
				stage.setFullScreen(true);
			} else if (action.getSource() == this.getPlayerMenu().getContinueBtn()) {
				// scene.setOnKeyReleased(this);
				stage.setScene(this.worldScene);
				stage.setFullScreen(true);
			} else if (action.getSource() == this.getPlayerMenu().getSaveGame()) {

				FileChooser fileChooser = new FileChooser();

				// Set extension filter
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SER files (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);

				// Show save file dialog
				File file = fileChooser.showSaveDialog(stage);
				if (file == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Didn't Save");
					alert.setContentText("You didn't select a file.");
					alert.initOwner(this.stage);
					alert.showAndWait();

				} else {
					try {
						game.save(file.getPath());
					} catch (IOException e) {

						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Didn't Save");
						alert.setContentText(e.getMessage());
						alert.initOwner(this.stage);
						alert.showAndWait();
					}
				}

			} else if (action.getSource() == this.getPlayerMenu().getLoadGame()) {
				int row = game.getWorld().getPlayerRow();
				int column = game.getWorld().getPlayerColumn();
				imageView2 = new ImageView(new Image("greenGrass.jpg"));
				imageView2.fitWidthProperty().bind(world.getCells()[row][column].widthProperty());
				imageView2.fitHeightProperty().bind(world.getCells()[row][column].heightProperty());
				world.getCells()[row][column].setGraphic(imageView2);
				Boolean flagFile = true;
				Boolean flag = false;
				do {
					FileChooser fileChooser = new FileChooser();
					FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SER files (*.ser)",
							"*.ser");
					fileChooser.getExtensionFilters().add(extFilter);
					fileChooser.setTitle("Open Saved File");
					File loadFile = fileChooser.showOpenDialog(stage);
					if (loadFile == null) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Not Loaded");
						alert.setContentText("You didn't select a file.");
						alert.initOwner(this.stage);
						alert.showAndWait();
						flag = false;
						flagFile = false;
					} else {
						String path = loadFile.getPath();
						try {
							game.load(path);
							flag = false;
						} catch (ClassNotFoundException v) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error");
							alert.setHeaderText("Corrupted File, choose again.");
							alert.setContentText(v.getMessage());
							alert.initOwner(this.stage);
							alert.showAndWait();
							flag = true;
						} catch (IOException b) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error");
							alert.setHeaderText("IOException, choose again.");
							alert.setContentText(b.getMessage());
							alert.initOwner(this.stage);
							alert.showAndWait();
							flag = true;
						}
					}

				} while (flag);

				
				game.setListener(this);
				row = game.getWorld().getPlayerRow();
				column = game.getWorld().getPlayerColumn();
				imageView2 = new ImageView(new Image("greenGrass.jpg"));
				imageView2.fitWidthProperty().bind(world.getCells()[9][9].widthProperty());
				imageView2.fitHeightProperty().bind(world.getCells()[9][9].heightProperty());
				world.getCells()[9][9].setGraphic(imageView2);

				imageView2 = new ImageView(new Image("player.gif"));
				imageView2.fitWidthProperty().bind(world.getCells()[row][column].widthProperty());
				imageView2.fitHeightProperty().bind(world.getCells()[row][column].heightProperty());
				world.getCells()[row][column].setGraphic(imageView2);
				
				if (flagFile){
					this.getWorld().getPlayerName().setText("Player's Name: " + game.getPlayer().getName());
					this.getWorld().getFighterName().setText("Fighter's Name: " + game.getPlayer().getActiveFighter().getName());
					this.getWorld().getFighterLevel().setText("Fighter's Level: "+game.getPlayer().getActiveFighter().getLevel());
					this.getWorld().getSenzuBeans().setText("Senzu Beans: "+game.getPlayer().getSenzuBeans());
					this.getWorld().getDragonballs().setText("Dragonballs: "+game.getPlayer().getDragonBalls());
					stage.setScene(this.worldScene);
				}
				stage.sizeToScene();
				stage.setOnCloseRequest(this);
				stage.setFullScreen(true);
				stage.show();
				
			} else if (action.getSource() == this.getPlayerMenu().getExit()) {
				Alert alertC = new Alert(AlertType.CONFIRMATION);
				alertC.setTitle("Exit");
				alertC.setHeaderText("Do you want to leave?");
				alertC.setContentText("I didn't think you're a quitter.");
				alertC.initOwner(stage);
				Optional<ButtonType> resultC = alertC.showAndWait();
				if (resultC.get() == ButtonType.OK) {
					looped_forever = false;
					thread.stop();
					stage.close();
					alertC.close();
				}
			} else if (action.getSource() == this.getPlayerMenu().getAssignAttack()) {
				Boolean flag = true;
				do {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Assign Attack");
					alert.setHeaderText("Choose type of Attack to replace");
					alert.setContentText("Choose your option.");

					ButtonType buttonTypeOne = new ButtonType("Super Attack");
					ButtonType buttonTypeTwo = new ButtonType("Ultimate Attack");
					ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
					alert.initOwner(stage);
					alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == buttonTypeOne) {

						List<String> choices = new ArrayList<>();
						for (int i = 0; i < game.getPlayer().getSuperAttacks().size(); i++) {
							choices.add(game.getPlayer().getSuperAttacks().get(i).getName());
						}

						ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);
						dialog.setTitle("Assign Attack");
						dialog.setHeaderText("Choose a new attack");
						dialog.setContentText("attack: ");
						dialog.initOwner(stage);
						Optional<String> newAttack = dialog.showAndWait();
						if (newAttack.isPresent()) {
							List<String> choices2 = new ArrayList<>();
							for (int j = 0; j < game.getPlayer().getActiveFighter().getSuperAttacks().size(); j++) {
								choices2.add(game.getPlayer().getActiveFighter().getSuperAttacks().get(j).getName());
							}
							ChoiceDialog<String> dialog2 = new ChoiceDialog<>("null", choices2);
							dialog2.setTitle("Assign Attack");
							dialog2.setHeaderText("Replace old attack");
							dialog2.setContentText("attack: ");
							dialog2.initOwner(stage);
							Optional<String> oldAttack = dialog2.showAndWait();

							if (oldAttack.isPresent()) {
								SuperAttack oldA = null;
								for (int i = 0; i < game.getPlayer().getActiveFighter().getSuperAttacks().size(); i++) {
									if (oldAttack.get().equals(
											game.getPlayer().getActiveFighter().getSuperAttacks().get(i).getName()))
										oldA = game.getPlayer().getActiveFighter().getSuperAttacks().get(i);
								}
								SuperAttack newA = null;
								for (int i = 0; i < game.getPlayer().getSuperAttacks().size(); i++) {
									if (newAttack.get().equals(game.getPlayer().getSuperAttacks().get(i).getName()))
										newA = game.getPlayer().getSuperAttacks().get(i);
								}
								try {
									game.getPlayer().assignAttack(game.getPlayer().getActiveFighter(), newA, oldA);
									flag = false;
								} catch (DuplicateAttackException de) {
									flag = true;
									Alert alertDE = new Alert(AlertType.ERROR);
									alertDE.setTitle("Invalid");
									alertDE.setHeaderText("Duplicate Attacks");
									alertDE.setContentText(de.getMessage());
									alertDE.initOwner(stage);
									alertDE.showAndWait();
								} catch (MaximumAttacksLearnedException me) {
									flag = true;
									Alert alertDE = new Alert(AlertType.ERROR);
									alertDE.setTitle("Invalid");
									alertDE.setHeaderText("Maximum Attacks");
									alertDE.setContentText(me.getMessage());
									alertDE.initOwner(stage);
									alertDE.showAndWait();
								} catch (NotASaiyanException se) {
									flag = true;
									Alert alertDE = new Alert(AlertType.ERROR);
									alertDE.setTitle("Invalid");
									alertDE.setHeaderText("Not Saiyan");
									alertDE.setContentText(se.getMessage());
									alertDE.initOwner(stage);
									alertDE.showAndWait();
								}
							}
						}

					} else if (result.get() == buttonTypeTwo) {

						List<String> choices = new ArrayList<>();
						for (int i = 0; i < game.getPlayer().getUltimateAttacks().size(); i++) {
							choices.add(game.getPlayer().getUltimateAttacks().get(i).getName());
						}
						ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);
						dialog.setTitle("Assign Attack");
						dialog.setHeaderText("Choose a new attack");
						dialog.setContentText("attack: ");
						dialog.initOwner(stage);
						// Traditional way to get the response value.
						Optional<String> newAttack = dialog.showAndWait();
						if (newAttack.isPresent()) {
							List<String> choices2 = new ArrayList<>();
							for (int j = 0; j < game.getPlayer().getActiveFighter().getUltimateAttacks().size(); j++) {
								choices2.add(game.getPlayer().getActiveFighter().getUltimateAttacks().get(j).getName());
							}
							ChoiceDialog<String> dialog2 = new ChoiceDialog<>("", choices2);
							dialog2.setTitle("Assign Attack");
							dialog2.setHeaderText("Replace old attack");
							dialog2.setContentText("attack: ");
							dialog2.initOwner(stage);
							Optional<String> oldAttack = dialog2.showAndWait();
							if (oldAttack.isPresent()) {
								UltimateAttack oldA = null;
								for (int i = 0; i < game.getPlayer().getActiveFighter().getUltimateAttacks()
										.size(); i++) {
									if (oldAttack.get().equals(
											game.getPlayer().getActiveFighter().getUltimateAttacks().get(i).getName()))
										oldA = game.getPlayer().getActiveFighter().getUltimateAttacks().get(i);
								}
								UltimateAttack newA = null;
								for (int i = 0; i < game.getPlayer().getUltimateAttacks().size(); i++) {
									if (newAttack.get().equals(game.getPlayer().getUltimateAttacks().get(i).getName()))
										newA = game.getPlayer().getUltimateAttacks().get(i);
								}
								try {
									game.getPlayer().assignAttack(game.getPlayer().getActiveFighter(), newA, oldA);
									flag = false;

								} catch (DuplicateAttackException de) {
									flag = true;
									Alert alertDE = new Alert(AlertType.ERROR);
									alertDE.setTitle("Invalid");
									alertDE.setHeaderText("Duplicate Attacks");
									alertDE.setContentText(de.getMessage());
									alertDE.initOwner(stage);
									alertDE.showAndWait();
								} catch (MaximumAttacksLearnedException me) {
									flag = true;
									Alert alertDE = new Alert(AlertType.ERROR);
									alertDE.setTitle("Invalid");
									alertDE.setHeaderText("Maximum Attacks");
									alertDE.setContentText(me.getMessage());
									alertDE.initOwner(stage);
									alertDE.showAndWait();
								} catch (NotASaiyanException se) {
									flag = true;
									Alert alertDE = new Alert(AlertType.ERROR);
									alertDE.setTitle("Invalid");
									alertDE.setHeaderText("Not Saiyan");
									alertDE.setContentText(se.getMessage());
									alertDE.initOwner(stage);
									alertDE.showAndWait();
								}
							}
						}
					} else {
						// ... user chose CANCEL or closed the dialog
						alert.close();
						flag = false;
					}
				} while (flag);

			} else if (action.getSource() == this.getPlayerMenu().getCreateNewFighter()) {
				this.fightersMn.getFighterName().clear();
				stage.setScene(this.fightersMnScene);
				stage.setFullScreen(true);
			} else if (action.getSource() == this.getFightersMn().getSaiyan()) {
				Saiyan fighter = new Saiyan("");
				this.getFightersMn().getFighterInfo()
						.setText("MaxHP :" + fighter.getMaxHealthPoints() + "\nMax Stamina: " + fighter.getMaxStamina()
								+ "\nMax Ki: " + fighter.getMaxKi() + "\nPhysical Damag: " + fighter.getPhysicalDamage()
								+ "\nBlast Damage: " + fighter.getBlastDamage());
				this.getFightersMn().setChoosenFighter("Saiyan");
				imageView2 = new ImageView(new Image("saiyan.png"));
				imageView2.setSmooth(true);
				this.getFightersMn().getFighterPic().setText("");
				this.getFightersMn().getFighterPic().setGraphic(imageView2);

			} else if (action.getSource() == this.getFightersMn().getFrieza()) {
				Frieza fighter = new Frieza("");
				this.getFightersMn().getFighterInfo()
						.setText("MaxHP :" + fighter.getMaxHealthPoints() + "\nMax Stamina: " + fighter.getMaxStamina()
								+ "\nMax Ki: " + fighter.getMaxKi() + "\nPhysical Damag: " + fighter.getPhysicalDamage()
								+ "\nBlast Damage: " + fighter.getBlastDamage());
				this.getFightersMn().setChoosenFighter("Frieza");
				imageView2 = new ImageView(new Image("frieza.png"));
				imageView2.setSmooth(true);
				this.getFightersMn().getFighterPic().setText("");
				this.getFightersMn().getFighterPic().setGraphic(imageView2);

			} else if (action.getSource() == this.getFightersMn().getNamekian()) {
				Namekian fighter = new Namekian("");
				this.getFightersMn().getFighterInfo()
						.setText("MaxHP :" + fighter.getMaxHealthPoints() + "\nMax Stamina: " + fighter.getMaxStamina()
								+ "\nMax Ki: " + fighter.getMaxKi() + "\nPhysical Damag: " + fighter.getPhysicalDamage()
								+ "\nBlast Damage: " + fighter.getBlastDamage());
				this.getFightersMn().setChoosenFighter("Namekian");
				imageView2 = new ImageView(new Image("namekian.png"));
				imageView2.setSmooth(true);
				this.getFightersMn().getFighterPic().setText("");
				this.getFightersMn().getFighterPic().setGraphic(imageView2);

			} else if (action.getSource() == this.getFightersMn().getMajin()) {
				Majin fighter = new Majin("");
				this.getFightersMn().getFighterInfo()
						.setText("MaxHP :" + fighter.getMaxHealthPoints() + "\nMax Stamina: " + fighter.getMaxStamina()
								+ "\nMax Ki: " + fighter.getMaxKi() + "\nPhysical Damag: " + fighter.getPhysicalDamage()
								+ "\nBlast Damage: " + fighter.getBlastDamage());
				this.getFightersMn().setChoosenFighter("Majin");
				imageView2 = new ImageView(new Image("majin.png"));
				imageView2.setSmooth(true);
				this.getFightersMn().getFighterPic().setText("");
				this.getFightersMn().getFighterPic().setGraphic(imageView2);

			} else if (action.getSource() == this.getFightersMn().getEarthling()) {
				Earthling fighter = new Earthling("");
				this.getFightersMn().getFighterInfo()
						.setText("MaxHP :" + fighter.getMaxHealthPoints() + "\nMax Stamina: " + fighter.getMaxStamina()
								+ "\nMax Ki: " + fighter.getMaxKi() + "\nPhysical Damag: " + fighter.getPhysicalDamage()
								+ "\nBlast Damage: " + fighter.getBlastDamage());
				this.getFightersMn().setChoosenFighter("Earthling");
				imageView2 = new ImageView(new Image("earthling.png"));
				imageView2.setSmooth(true);
				this.getFightersMn().getFighterPic().setText("");
				this.getFightersMn().getFighterPic().setGraphic(imageView2);
			} else if (action.getSource() == this.getPlayerMenu().getUpgradeFighter()) {
				this.getUpgrade().getHealth()
						.setText("Max Health Points: " + game.getPlayer().getActiveFighter().getMaxHealthPoints());
				this.getUpgrade().getPhysical()
						.setText("Physical Damge: " + game.getPlayer().getActiveFighter().getPhysicalDamage());
				this.getUpgrade().getStamina()
						.setText("Max Stamina: " + game.getPlayer().getActiveFighter().getMaxStamina());
				this.getUpgrade().getKi().setText("Max Ki: " + game.getPlayer().getActiveFighter().getMaxKi());
				this.getUpgrade().getBlast()
						.setText("Blast Damage: " + game.getPlayer().getActiveFighter().getBlastDamage());
				this.getUpgrade().getAbilityPoints()
						.setText("Ability Points: " + game.getPlayer().getActiveFighter().getAbilityPoints());
				stage.setScene(this.upgradeScene);
				stage.setFullScreen(true);
			} else if (action.getSource() == this.getUpgrade().getOkay()) {
				stage.setScene(this.worldScene);
				stage.setFullScreen(true);
			} else if (action.getSource() == this.getUpgrade().getKiBtn()) {
				try {
					game.getPlayer().upgradeFighter(game.getPlayer().getActiveFighter(), 'K');
				} catch (NotEnoughAbilityPointsException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Invalid Move");
					alert.setHeaderText("Not Enough Ability Points");
					alert.setContentText(e.getMessage());
					alert.initOwner(stage);
					alert.showAndWait();
				}
				this.getUpgrade().getKi().setText("Max Ki: " + game.getPlayer().getActiveFighter().getMaxKi());
			} else if (action.getSource() == this.getUpgrade().getStaminaBtn()) {
				try {
					game.getPlayer().upgradeFighter(game.getPlayer().getActiveFighter(), 'S');
				} catch (NotEnoughAbilityPointsException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Invalid Move");
					alert.setHeaderText("Not Enough Ability Points");
					alert.setContentText(e.getMessage());
					alert.initOwner(stage);
					alert.showAndWait();
				}
				this.getUpgrade().getStamina()
						.setText("Max Stamina: " + game.getPlayer().getActiveFighter().getMaxStamina());
			} else if (action.getSource() == this.getUpgrade().getHealthBtn()) {
				try {
					game.getPlayer().upgradeFighter(game.getPlayer().getActiveFighter(), 'H');
				} catch (NotEnoughAbilityPointsException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Invalid Move");
					alert.setHeaderText("Not Enough Ability Points");
					alert.setContentText(e.getMessage());
					alert.initOwner(stage);
					alert.showAndWait();
				}
				this.getUpgrade().getHealth()
						.setText("Max Health Points: " + game.getPlayer().getActiveFighter().getMaxHealthPoints());
			} else if (action.getSource() == this.getUpgrade().getPhysicalBtn()) {
				try {
					game.getPlayer().upgradeFighter(game.getPlayer().getActiveFighter(), 'P');
				} catch (NotEnoughAbilityPointsException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Invalid Move");
					alert.setHeaderText("Not Enough Ability Points");
					alert.setContentText(e.getMessage());
					alert.initOwner(stage);
					alert.showAndWait();
				}
				this.getUpgrade().getPhysical()
						.setText("Physical Damage: " + game.getPlayer().getActiveFighter().getPhysicalDamage());
			} else if (action.getSource() == this.getUpgrade().getBlastBtn()) {
				try {
					game.getPlayer().upgradeFighter(game.getPlayer().getActiveFighter(), 'B');
				} catch (NotEnoughAbilityPointsException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Invalid Move");
					alert.setHeaderText("Not Enough Ability Points");
					alert.setContentText(e.getMessage());
					alert.initOwner(stage);
					alert.showAndWait();
				}
				this.getUpgrade().getBlast()
						.setText("Blast Damage: " + game.getPlayer().getActiveFighter().getBlastDamage());
			} else if (action.getSource() == this.getFightersMn().getOkay()) {
				String fighterName = this.getFightersMn().getFighterName().getText();
				String race = this.getFightersMn().getChoosenFighter();
				PlayableFighter fighter = null;
				switch (race) {
				case "Earthling":
					fighter = new Earthling(fighterName);
					break;
				case "Saiyan":
					fighter = new Saiyan(fighterName);
					break;
				case "Namekian":
					fighter = new Namekian(fighterName);
					break;
				case "Frieza":
					fighter = new Frieza(fighterName);
					break;
				case "Majin":
					fighter = new Majin(fighterName);
					break;
				default:
					fighter = new Saiyan(fighterName);
					break;
				}
				this.getWorld().getFighterName().setText("Fighter's Name: " + fighterName);
				this.getWorld().getFighterLevel().setText("Fighter's Level: 1");
				this.getWorld().getDragonballs().setText("Dragonballs: 0");
				game.getPlayer().getFighters().add(fighter);
				game.getPlayer().setActiveFighter(fighter);
				stage.setScene(this.worldScene);
				stage.setFullScreen(true);
			} else if (action.getSource() == this.getPlayerMenu().getSwitchFighter()) {
				List<String> choices = new ArrayList<>();
				for (int i = 0; i < game.getPlayer().getFighters().size(); i++) {
					choices.add(game.getPlayer().getFighters().get(i).getName());
				}
				ChoiceDialog<String> dialog = new ChoiceDialog<>(game.getPlayer().getFighters().get(0).getName(),
						choices);
				dialog.setTitle("Switch Fighter");
				dialog.setHeaderText("Choose a Fighter");
				dialog.setContentText("Fighter: ");
				dialog.initOwner(stage);
				// Traditional way to get the response value.
				Optional<String> result = dialog.showAndWait();
				PlayableFighter fighter = game.getPlayer().getActiveFighter();
				if (result.isPresent()) {
					for (int i = 0; i < game.getPlayer().getFighters().size(); i++) {
						if (result.get().equals(game.getPlayer().getFighters().get(i).getName()))
							fighter = game.getPlayer().getFighters().get(i);
					}
				}
				game.getPlayer().setActiveFighter(fighter);
				this.getWorld().getFighterName()
						.setText("Fighter's Name: " + game.getPlayer().getActiveFighter().getName());
				this.getWorld().getFighterLevel()
						.setText("Fighter's Level: " + game.getPlayer().getActiveFighter().getLevel());
				stage.setScene(this.worldScene);
				stage.setFullScreen(true);
			} else if (action.getSource() == this.getDragon().getSenzuBeansBtn()) {
				game.getPlayer()
						.setSenzuBeans(game.getPlayer().getSenzuBeans() + this.dragon.getDragon().getSenzuBeans());
				this.getWorld().getSenzuBeans().setText("Senzu Beans: " + game.getPlayer().getSenzuBeans());
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Notification");
				alert.setHeaderText("Wish granted!");
				alert.setContentText("You got "+this.dragon.getDragon().getSenzuBeans()+" new senzu beans");
				alert.initOwner(stage);
				alert.showAndWait();
				stage.setScene(this.worldScene);
				stage.setFullScreen(true);
			} else if (action.getSource() == this.getDragon().getAbilityPointsBtn()) {
				game.getPlayer().getActiveFighter()
						.setAbilityPoints(game.getPlayer().getActiveFighter().getAbilityPoints()
								+ this.dragon.getDragon().getAbilityPoints());				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Notification");
				alert.setHeaderText("Wish granted!");
				alert.setContentText("You got "+this.dragon.getDragon().getSenzuBeans()+" new ability points");
				alert.initOwner(stage);
				alert.showAndWait();
				stage.setScene(this.worldScene);
				stage.setFullScreen(true);
			} else if (action.getSource() == this.getDragon().getSuperAttacksBtn()) {
				String name="";
				for (int i = 0; i < this.dragon.getDragon().getWishes().length; i++) {
					if (this.dragon.getDragon().getWishes()[i].getType() == DragonWishType.SUPER_ATTACK) {
						game.getPlayer().getSuperAttacks().add(this.dragon.getDragon().getWishes()[i].getSuperAttack());
						name = this.dragon.getDragon().getWishes()[i].getSuperAttack().getName();
					}
				}
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Notification");
				alert.setHeaderText("Wish granted!");
				alert.setContentText("You got "+ name);
				alert.initOwner(stage);
				alert.showAndWait();
				stage.setScene(this.worldScene);
				stage.setFullScreen(true);
			} else if (action.getSource() == this.getDragon().getUltimateAttacksBtn()) {
				String name="";
				
				for (int i = 0; i < this.dragon.getDragon().getWishes().length; i++) {
					if (this.dragon.getDragon().getWishes()[i].getType() == DragonWishType.ULTIMATE_ATTACK) {
						game.getPlayer().getUltimateAttacks()
								.add(this.dragon.getDragon().getWishes()[i].getUltimateAttack());
						name = this.dragon.getDragon().getWishes()[i].getUltimateAttack().getName();

					}
				}
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Notification");
				alert.setHeaderText("Wish granted!");
				alert.setContentText("You got "+ name);
				alert.initOwner(stage);
				alert.showAndWait();
				stage.setScene(this.worldScene);
				stage.setFullScreen(true);
			}

		}

	}

	public UpgradeFighterPane getUpgrade() {
		return upgrade;
	}

	public Scene getUpgradeScene() {
		return upgradeScene;
	}

	public DragonPane getDragon() {
		return dragon;
	}

	public Scene getWorldScene() {
		return worldScene;
	}

	public Scene getStartMenuScene() {
		return startMenuScene;
	}

	public Scene getPlayerMenuScene() {
		return playerMenuScene;
	}

	public Scene getFightersMenuScene() {
		return fightersMenuScene;
	}

	public Scene getDragonScene() {
		return dragonScene;
	}

	public Stage getStage() {
		return stage;
	}

	@Override
	public void onDragonCalled(Dragon dragon) {
		this.dragon.getName().setText(dragon.getName());
		for (int i = 0; i < dragon.getWishes().length; i++) {
			if (dragon.getWishes()[i].getType() == DragonWishType.SENZU_BEANS) {
				this.dragon.getSenzuBeans().setText("Senzu Bean: " + dragon.getWishes()[i].getSenzuBeans());
			} else if (dragon.getWishes()[i].getType() == DragonWishType.ABILITY_POINTS) {
				this.dragon.getAbilityPoints().setText("Ability Points: " + dragon.getWishes()[i].getAbilityPoints());
			} else if (dragon.getWishes()[i].getType() == DragonWishType.SUPER_ATTACK) {
				this.dragon.getSuperAttacks()
						.setText("Super Attack: " + dragon.getWishes()[i].getSuperAttack().getName());
			} else if (dragon.getWishes()[i].getType() == DragonWishType.ULTIMATE_ATTACK) {
				this.dragon.getUltimateAttacks()
						.setText("Ultimate Attack: " + dragon.getWishes()[i].getUltimateAttack().getName());
			}
		}
		this.world.getDragonballs().setText("Dragonballs: " + this.game.getPlayer().getDragonBalls());
		this.dragon.setDragon(dragon);
		stage.setScene(dragonScene);
		stage.setFullScreen(true);
	}

	@Override
	public void onCollectibleFound(Collectible collectible) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Notification");
		alert.setHeaderText("Collectible Found!");
		String s = "You've found " + (collectible == Collectible.SENZU_BEAN ? "1 Senzu Bean" : "1 DragonBall") + "..!";
		if (this.game.getPlayer().getDragonBalls() == 7)
			s += " Congrats! You called the dragon!";
		alert.setContentText(s);
		alert.initOwner(stage);
		alert.show();

		this.getWorld().getDragonballs().setText("Dragonballs: " + game.getPlayer().getDragonBalls());
		this.getWorld().getSenzuBeans().setText("Senzu Beans: " + game.getPlayer().getSenzuBeans());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBattleEvent(BattleEvent e) {
		Battle battle = ((Battle) e.getSource());
		Fighter fighter = (Fighter) battle.getMe();
		Fighter foe = (Fighter) battle.getFoe();
		if (e.getType() == BattleEventType.STARTED) {
			
			this.foeBrain = new FoeBrain(fighter, foe, battle);

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Battle Mode");
			alert.setHeaderText("It's not your lucky day.");
			alert.setContentText("Careful with the next step!");
			alert.initOwner(this.stage);
			alert.showAndWait();
			this.battle = new BattlePane(this);

			this.battle.getNameLvlP().setText("Name: " + fighter.getName() + "\t\tLevel: " + fighter.getLevel());
			this.battle.getStaminaP()
					.setText("Current stamina: " + fighter.getStamina() + "\tMax Stamina: " + fighter.getMaxStamina());
			this.battle.getKiP().setText("Current Ki: " + fighter.getKi() + "\t\tMax Ki: " + fighter.getMaxKi());
			this.battle.getHealthTP()
					.setText("Current HP: " + fighter.getHealthPoints() + "\tMax HP: " + fighter.getMaxHealthPoints());

			this.battle.getNameLvlF().setText("Name: " + foe.getName() + "\t\tLevel: " + foe.getLevel());
			this.battle.getStaminaF()
					.setText("Current stamina: " + foe.getStamina() + "\tMax Stamina: " + foe.getMaxStamina());
			this.battle.getKiF().setText("Current Ki: " + foe.getKi() + "\t\tMax Ki: " + foe.getMaxKi());
			this.battle.getHealthTF()
					.setText("Current HP: " + foe.getHealthPoints() + "\tMax HP: " + foe.getMaxHealthPoints());

			this.battleScene = new Scene(this.battle);
			// this.battleScene.getFocusOwner();
			this.stage.setScene(this.battleScene); //hena ya yaseen
			
			//this.stage.setHeight(200);
			//this.stage.setWidth(300);
			
			this.stage.setFullScreen(true);
		} else if (e.getType() == BattleEventType.ENDED) {
			String superAttacks = "";
			for (int i = 0; i < foe.getSuperAttacks().size(); i++) {
				superAttacks += foe.getSuperAttacks().get(i).getName() + " ";
			}
			String ultimateAttakcs = "";
			for (int i = 0; i < foe.getUltimateAttacks().size(); i++) {
				ultimateAttakcs += foe.getUltimateAttacks().get(i).getName() + " ";
			}
			if (e.getWinner().equals(fighter)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("You Won!");
				alert.setHeaderText("Huh, you were lucky this time!");
				alert.setContentText("Updated xp: " + game.getPlayer().getActiveFighter().getXp()
						+ "\nNew Super attacks: " + superAttacks + "\nNew Ultimate attacks: " + ultimateAttakcs);
				alert.initOwner(stage);
				alert.showAndWait();

				if (game.getPlayer().getActiveFighter().getUpgraded()) {
					Alert alertu = new Alert(AlertType.INFORMATION);
					alertu.setTitle("You've upgraded!");
					alertu.setHeaderText("Your level now is " + game.getPlayer().getActiveFighter().getLevel());
					alertu.setContentText("New target: " + game.getPlayer().getActiveFighter().getTargetXp()
							+ "\nAbility Points: " + game.getPlayer().getActiveFighter().getAbilityPoints());
					alertu.initOwner(stage);
					alertu.showAndWait();
					game.getPlayer().getActiveFighter().setUpgraded(false);
				}
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("You Lost!");
				alert.setHeaderText("Told you  it's not your lucky day");
				if (((NonPlayableFighter) foe).isStrong())
					alert.setContentText("that was the boss!");
				alert.initOwner(stage);
				alert.showAndWait();
			}
			// game.setListener(this);
			int row = game.getWorld().getPlayerRow();
			int column = game.getWorld().getPlayerColumn();
			imageView2 = new ImageView(new Image("player.gif"));
			imageView2.fitWidthProperty().bind(world.getCells()[row][column].widthProperty());
			imageView2.fitHeightProperty().bind(world.getCells()[row][column].heightProperty());
			world.getCells()[row][column].setGraphic(imageView2);
			world.getFighterLevel().setText("Fighter's Level: " + game.getPlayer().getActiveFighter().getLevel());
			world.getSenzuBeans().setText("Senzu Beans: "+game.getPlayer().getSenzuBeans());
			this.stage.setScene(this.worldScene);
			this.stage.setFullScreen(true);

		} else if (e.getType() == BattleEventType.NEW_TURN) {
			if (e.getCurrentOpponent().equals(fighter)) {

				Boolean flag = false;
				do {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("It's your turn");
					alert.setHeaderText("Choose your move wisely");
					alert.setContentText("move: ");
					alert.initOwner(stage);
					ButtonType buttonAttack = new ButtonType("attack");
					ButtonType buttonBlock = new ButtonType("block");
					ButtonType buttonUse = new ButtonType("use");
					ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
					alert.getButtonTypes().setAll(buttonAttack, buttonBlock, buttonUse, buttonTypeCancel);
					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == buttonAttack) {
						do {
							Alert alertA = new Alert(AlertType.CONFIRMATION);
							alertA.setTitle("Attack");
							alertA.setHeaderText("Choose an attack type");
							alertA.setContentText("type: ");
							alertA.initOwner(stage);
							ButtonType buttonPhysical = new ButtonType("Physical Attack");
							ButtonType buttonSuper = new ButtonType("Super Attack");
							ButtonType buttonUltimate = new ButtonType("Ultimate Attack");
							ButtonType buttonTypeCancelAtt = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
							alertA.getButtonTypes().setAll(buttonPhysical, buttonSuper, buttonUltimate,
									buttonTypeCancelAtt);

							Optional<ButtonType> resultA = alertA.showAndWait();
							if (resultA.get() == buttonPhysical) {
								try {
									battle.attack(new PhysicalAttack());
									flag = false;

								} catch (NotEnoughKiException fe) {
									Alert alertE = new Alert(AlertType.ERROR);
									alertE.setTitle("Invalid Move");
									alertE.setHeaderText("Not Enough Ki");
									alertE.setContentText(fe.getMessage());
									alertE.initOwner(stage);
									alertE.showAndWait();
									flag = true;
								}
							} else if (resultA.get() == buttonSuper) {
								if (fighter.getSuperAttacks().size() == 0) {
									Alert alertE = new Alert(AlertType.INFORMATION);
									alertE.setTitle("Invalid Move");
									alertE.setHeaderText("You don't have any super attacks");
									alertE.initOwner(stage);
									alertE.showAndWait();
									flag = true;
									continue;
								}
								List<String> choices = new ArrayList<>();
								for (Attack v : fighter.getSuperAttacks()) {
									choices.add(v.getName());
								}
								ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);
								dialog.setTitle("Super Attack");
								dialog.setHeaderText("Choose a super attack");
								dialog.setContentText("attack: ");
								dialog.initOwner(stage);
								// Traditional way to get the response value.
								Optional<String> resultS = dialog.showAndWait();
								if (resultS.isPresent()) {
									String superAttackName = resultS.get();
									SuperAttack superAttack = null;
									for (int i = 0; i < fighter.getSuperAttacks().size(); i++) {
										if (superAttackName.equals(fighter.getSuperAttacks().get(i).getName()))
											superAttack = fighter.getSuperAttacks().get(i);
									}
									try {
										battle.attack(superAttack);
										flag = false;

									} catch (NotEnoughKiException fe) {
										Alert alertE = new Alert(AlertType.ERROR);
										alertE.setTitle("Invalid Move");
										alertE.setHeaderText("Not Enough Ki");
										alertE.setContentText(fe.getMessage());
										alertE.initOwner(stage);
										alertE.showAndWait();
										flag = true;
									}
								}

							} else if (resultA.get() == buttonUltimate) {
								if (fighter.getUltimateAttacks().size() == 0) {
									Alert alertE = new Alert(AlertType.INFORMATION);
									alertE.setTitle("Invalid Move");
									alertE.setHeaderText("You don't have any ultimate attacks");
									alertE.initOwner(stage);
									alertE.showAndWait();
									flag = true;
									continue;
								}

								List<String> choices = new ArrayList<>();
								for (int i = 0; i < fighter.getUltimateAttacks().size(); i++) {
									choices.add(fighter.getUltimateAttacks().get(i).getName());
								}
								ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);
								dialog.setTitle("Ultimate Attack");
								dialog.setHeaderText("Choose an ultimate attack");
								dialog.setContentText("attack: ");
								dialog.initOwner(stage);
								// Traditional way to get the response value.
								Optional<String> resultU = dialog.showAndWait();
								if (resultU.isPresent()) {
									String ultimateAttackName = resultU.get();
									UltimateAttack ultimateAttack = null;
									for (int i = 0; i < fighter.getUltimateAttacks().size(); i++) {
										if (ultimateAttackName.equals(fighter.getUltimateAttacks().get(i).getName()))
											ultimateAttack = fighter.getUltimateAttacks().get(i);
									}
									try {
										battle.attack(ultimateAttack);
										flag = false;

									} catch (NotEnoughKiException fe) {
										Alert alertE = new Alert(AlertType.ERROR);
										alertE.setTitle("Invalid Move");
										alertE.setHeaderText("Not Enough Ki");
										alertE.setContentText(fe.getMessage());
										alertE.initOwner(stage);
										alertE.showAndWait();
										flag = true;
									}
								}
							} else {
								System.out.println("canceled");
								// ... user chose CANCEL or closed the dialog
								Alert alertC = new Alert(AlertType.CONFIRMATION);
								alertC.setTitle("Exit");
								alertC.setHeaderText("Do you want to leave?");
								alertC.setContentText("I didn't think you're a quitter.");
								alertC.initOwner(stage);
								Optional<ButtonType> resultC = alertC.showAndWait();
								if (resultC.get() == ButtonType.OK) {
									looped_forever = false;
									thread.stop();
									stage.close();
									alertC.close();
								}
							}
						} while (flag&&false);
					} else if (result.get() == buttonBlock) {

						battle.block();

					} else if (result.get() == buttonUse) {
						try {
							battle.use(game.getPlayer(), Collectible.SENZU_BEAN);
							flag = false;
						} catch (NotEnoughSenzuBeansException senzu) {
							Alert alertE = new Alert(AlertType.ERROR);
							alertE.setTitle("Invalid Move");
							alertE.setHeaderText("Not Enough Senzu Beans");
							alertE.setContentText(senzu.getMessage());
							alertE.initOwner(stage);
							alertE.showAndWait();
							flag = true;
							continue;
						}

					} else {
						// ... user chose CANCEL or closed the dialog
						Alert alertC = new Alert(AlertType.CONFIRMATION);
						alertC.setTitle("Exit");
						alertC.setHeaderText("Do you want to leave?");
						alertC.setContentText("I didn't think you're a quitter.");
						alertC.initOwner(stage);
						Optional<ButtonType> resultC = alertC.showAndWait();
						if (resultC.get() == ButtonType.OK) {
							looped_forever = false;
							thread.stop();
							stage.close();
							alertC.close();
						} else {
							// ... user chose CANCEL or closed the dialog
							flag = true;
							continue;
						}
					}

				} while (flag&&false);

			} else {

				try {
					Thread.sleep(500); // 1000 milliseconds is one second.
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				/*Boolean flag = false;
				do {
					try {
						battle.play();
						flag = false;
					} catch (NotEnoughKiException fe) {
						flag = true;
					}
				} while (flag);*/
				
				this.foeBrain.thinkAndAttack();

			}
			
			
			battle.endTurn();
			
			if (fighter instanceof Saiyan && ((Saiyan) fighter).isTransformed()) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Transformed");
				alert.setHeaderText("You transformed to a Super Saiyan");
				alert.setContentText("You're now stronger than ever.");
				alert.initOwner(stage);
				alert.showAndWait();
			}
		} else if (e.getType() == BattleEventType.ATTACK) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle(e.getCurrentOpponent().equals(fighter) ? "Your Turn" : "Foe's Turn");
			alert.setHeaderText((e.getCurrentOpponent().equals(fighter) ? "You're" : "Foe is") + " attacking with: "
					+ e.getAttack().getName());
			alert.setContentText("");
			alert.initOwner(stage);
			alert.showAndWait();
		} else if (e.getType() == BattleEventType.BLOCK) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle(e.getCurrentOpponent().equals(fighter) ? "Your Turn" : "Foe's Turn");
			alert.setHeaderText((e.getCurrentOpponent().equals(fighter) ? "You're" : "Foe is") + " blocking");
			alert.setContentText("");
			alert.initOwner(stage);
			alert.showAndWait();

		}

		else if (e.getType() == BattleEventType.USE) {
			Alert alert2 = new Alert(AlertType.INFORMATION);
			alert2.setTitle("Notification");
			alert2.setHeaderText("You used a senzu bean.");
			alert2.setContentText("Gained full health.");
			alert2.initOwner(stage);
			alert2.showAndWait();
		}

		this.battle.getNameLvlP().setText("Name: " + fighter.getName() + "\t\tLevel: " + fighter.getLevel());
		this.battle.getStaminaP()
				.setText("Current stamina: " + fighter.getStamina() + "\tMax Stamina: " + fighter.getMaxStamina());
		this.battle.getKiP().setText("Current Ki: " + fighter.getKi() + "\t\tMax Ki: " + fighter.getMaxKi());
		this.battle.getHealthTP()
				.setText("Current HP: " + fighter.getHealthPoints() + "\tMax HP: " + fighter.getMaxHealthPoints());

		this.battle.getNameLvlF().setText("Name: " + foe.getName() + "\t\tLevel: " + foe.getLevel());
		this.battle.getStaminaF()
				.setText("Current stamina: " + foe.getStamina() + "\tMax Stamina: " + foe.getMaxStamina());
		this.battle.getKiF().setText("Current Ki: " + foe.getKi() + "\t\tMax Ki: " + foe.getMaxKi());
		this.battle.getHealthTF()
				.setText("Current HP: " + foe.getHealthPoints() + "\tMax HP: " + foe.getMaxHealthPoints());

		double ratio = (100 * foe.getHealthPoints()) / (foe.getMaxHealthPoints());
		if (ratio <= 25) {
			this.battle.gethF().setImage(new Image("healthf25.png"));
		} else if (ratio <= 50) {
			this.battle.gethF().setImage(new Image("healthf50.png"));
		} else if (ratio <= 75) {
			this.battle.gethF().setImage(new Image("healthf75.png"));
		} else {
			this.battle.gethP().setImage(new Image("healthf.png"));
		}

		double ratio2 = (100 * fighter.getHealthPoints()) / (fighter.getMaxHealthPoints());
		if (ratio2 <= 25) {
			this.battle.gethP().setImage(new Image("healthp25.png"));
		} else if (ratio2 <= 50) {
			this.battle.gethP().setImage(new Image("healthp50.png"));
		} else if (ratio2 <= 75) {
			this.battle.gethP().setImage(new Image("healthp75.png"));
		} else {
			this.battle.gethP().setImage(new Image("healthp.png"));
		}
	}

	public Game getGame() {
		return game;
	}

	public WorldPane getWorld() {
		return world;
	}

	public StartMenuPane getStartMenu() {
		return startMenu;
	}

	public FightersMenuPane getFightersMenu() {
		return fightersMenu;
	}

	public PlayerMenuPane getPlayerMenu() {
		return playerMenu;
	}

	public BattlePane getBattle() {
		return battle;
	}

	public Scene getBattleScene() {
		return battleScene;
	}

	final static Runnable play = new Runnable() // This Thread Runnabe is for
												// playing the sound
	{
		public void run() {
			try {
				// Check if the audio file is a .wav file
				if (sound.getName().toLowerCase().contains(".wav")) {
					AudioInputStream stream = AudioSystem.getAudioInputStream(sound);

					AudioFormat format = stream.getFormat();

					if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
						format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(),
								format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2,
								format.getFrameRate(), true);

						stream = AudioSystem.getAudioInputStream(format, stream);
					}

					SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat(),
							(int) (stream.getFrameLength() * format.getFrameSize()));

					SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
					line.open(stream.getFormat());
					line.start();

					// Set Volume
					FloatControl volume_control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
					volume_control.setValue((float) (Math.log(volume / 100.0f) / Math.log(10.0f) * 20.0f));

					// Mute
					BooleanControl mute_control = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
					mute_control.setValue(muted);

					FloatControl pan_control = (FloatControl) line.getControl(FloatControl.Type.PAN);
					pan_control.setValue(pan);

					long last_update = System.currentTimeMillis();
					double since_last_update = (System.currentTimeMillis() - last_update) / 1000.0d;

					// Wait the amount of seconds set before continuing
					while (since_last_update < seconds) {
						since_last_update = (System.currentTimeMillis() - last_update) / 1000.0d;
					}

					// System.out.println("Playing!");

					int num_read = 0;
					byte[] buf = new byte[line.getBufferSize()];

					while ((num_read = stream.read(buf, 0, buf.length)) >= 0) {
						int offset = 0;

						while (offset < num_read) {
							offset += line.write(buf, offset, num_read - offset);
						}
					}

					line.drain();
					line.stop();

					if (looped_forever) {
						thread = new Thread(play);
						thread.start();
					} else if (loops_done < loop_times) {
						loops_done++;
						thread = new Thread(play);
						thread.start();
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};
}
