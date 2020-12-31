/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import UI.InfoLabel;
import UI.MenuButton;
import UI.SHIP;
import UI.ShipPicker;
import UI.SpaceInvadersSubscene;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * ViewManager
 * Classe que controla nossa janela de menu
 * @author joao
 */
public class ViewManager {
    
    private static final int SCREEN_WIDTH = 1080;
    private static final int SCREEN_HEIGHT = 720;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;
    
    private final static int MENU_BUTTONS_START_X = 100;
    private final static int MENU_BUTTONS_START_Y = 135;
    
    private final static String BACKGROUND_PATH = "UI/resources/glassPanel.png";
    private final static String CONTROLS_PATH = "UI/resources/controls.png";
    
    private SpaceInvadersSubscene creditsSubscene;
    private SpaceInvadersSubscene helpSubscene;
    private SpaceInvadersSubscene scoreSubscene;
    private SpaceInvadersSubscene shipChooserSubscene;
    
    private SpaceInvadersSubscene sceneToHide;
    
    ArrayList<MenuButton> menuButtons = new ArrayList();
    
    ArrayList<ShipPicker> shipsList = new ArrayList();
    private SHIP choosenShip;
    
    /**
     * ViewManager()
     * Construtor da classe
     * Responsavel por inicializar nosso Pane, junto da Scene e do Stage
     * chama as funcoes que criam as SubScenes, cria o plano de fundo padrao, a logo do jogo e os botoes
     */
    public ViewManager(){
        try{
            mainPane = new AnchorPane();
            mainScene = new Scene(mainPane, SCREEN_WIDTH, SCREEN_HEIGHT);
            mainStage = new Stage();
            mainStage.setScene(mainScene);
            createSubscenes();
            createButtons();
            createBackground();
            createLogo();
    
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    /**
     * showSubscene()
     * Exibe a SubScene recebida por parametro através dos Listeners dos botoes
     * @param subScene SubScene a ser exibida
     */
    private void showSubscene(SpaceInvadersSubscene subScene){
        if(sceneToHide != null){
            sceneToHide.moveSubscene();
        }
        
        subScene.moveSubscene();
        sceneToHide = subScene;
    }
    
    /**
     * createSubscenes()
     * Instancia nossas Subscenes
     */
    private void createSubscenes(){
        createCreditsSubscene();
        
        createHelpSubscene();
        
        createScoreSubscene();
        
        createShipChooserSubscene();
        
    }
    
    /**
     * Cria SubScene de creditos
     */
    public void createCreditsSubscene(){
        creditsSubscene = new SpaceInvadersSubscene();
        mainPane.getChildren().add(creditsSubscene);
        
        String text = 
                    "\n" +
                    " Projeto Space Invaders - Parte 2\n" +
                    " Programacao Orientada a Objetos\n" +
                    " Prof. Robson L. F. Cordeiro\n" +
                    " 21 de Dezembro de 2020\n" +
                    " Aluno Joao Gabriel Zanao Costa\n" +
                    " NUSP 11234266\n" +
                    " @author joao\n" +
                    " @version 2.0.0\n" +
                    " ";
        
        TextArea creditsArea = new TextArea(text);
 
        creditsArea.setBackground(Background.EMPTY);
        creditsArea.setLayoutX(60);
        creditsArea.setLayoutY(40);
        creditsArea.setEditable(false);
        
        creditsSubscene.getPane().getChildren().add(creditsArea);
    }
    /**
     * cria Subscene de ajuda
     */
    public void createHelpSubscene(){
        helpSubscene = new SpaceInvadersSubscene();
        mainPane.getChildren().add(helpSubscene);
        
        ImageView help = new ImageView(CONTROLS_PATH);
        
        help.setLayoutX(120);
        help.setLayoutY(100);
        help.setFitHeight(204);
        help.setFitWidth(372);
        
        
        helpSubscene.getPane().getChildren().add(help);
        
        
    }
    /**
     * cria subscene da pontuacao
     */
    public void createScoreSubscene(){
        scoreSubscene = new SpaceInvadersSubscene();
        mainPane.getChildren().add(scoreSubscene);
        
        InfoLabel newLabel = new InfoLabel("Not Implemented Yet!");
        
        newLabel.setLayoutX(110);
        newLabel.setLayoutY(250);
        
        scoreSubscene.getPane().getChildren().add(newLabel);
        
    }
    /**
     * createShipChooserSubscene()
     * Esta SubScene eh mais especifica do que as outras, ela e responsavel por carregar e mostrar as opcoes de escolhas de nave e do botao de PLAY
     */
    private void createShipChooserSubscene(){
        shipChooserSubscene = new SpaceInvadersSubscene();
        mainPane.getChildren().add(shipChooserSubscene);
        
        InfoLabel chooseShipLabel = new InfoLabel("CHOOSE YOUR SHIP");
        chooseShipLabel.setLayoutX(110);
        chooseShipLabel.setLayoutY(25);
        shipChooserSubscene.getPane().getChildren().add(chooseShipLabel);
        shipChooserSubscene.getPane().getChildren().add(createShipsToChoose());
        shipChooserSubscene.getPane().getChildren().add(createButtonStart());
           
    }
    
    /**
     * createShipsToChoose()
     * Cria nossa caixa de alinhamento horizontal para carregarmos nossos naves junta da opcao escolhida
     * @return box caixa auto-ajustavel contendo as naves a serem escolhidas
     */
    private HBox createShipsToChoose(){
        HBox box =  new HBox();
        box.setSpacing(25);
        for(SHIP ship :  SHIP.values()){
            ShipPicker shipToPick = new ShipPicker(ship);
            shipsList.add(shipToPick);
            box.getChildren().add(shipToPick);
            shipToPick.setOnMouseClicked((MouseEvent event) -> {
                shipsList.forEach(ship1 -> {
                    ship1.setIsCircleChoosen(false);
                });
                shipToPick.setIsCircleChoosen(true);
                choosenShip = shipToPick.getShip();
            });
        }
        box.setLayoutX(64);
        box.setLayoutY(115);
        return box;
    }
    
    /**
     * createButtonStart()
     * Cria nosso botao que inicializara nosso jogo
     * @return startButton o botao
     */
    private MenuButton createButtonStart(){
        MenuButton startButton = new MenuButton("START");
        startButton.setLayoutX(350);
        startButton.setLayoutY(300);
        
        startButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                if(choosenShip != null){
                    GameViewManager gameManager = new GameViewManager();
                    gameManager.createNewGame(mainStage, choosenShip);
                }
            }
            
        });
        
        return startButton;
    }
    
    /**
     * getMainStage()
     * @return retorna o Stage do nosso menu
     */
    public Stage getMainStage(){
        return mainStage;
    }
    
    /**
     * addMenuButton()
     * Adiciona um MenuButton para o AnchorPane do nosso menu
     * @param button botao para ser inserido
     */
    private void addMenuButton(MenuButton button){
        button.setLayoutX(MENU_BUTTONS_START_X);
        if(!menuButtons.isEmpty()) {
            button.setLayoutY(MENU_BUTTONS_START_Y + menuButtons.size() * 110);
        } else {
            button.setLayoutY(MENU_BUTTONS_START_Y);
        }
        menuButtons.add(button);
        mainPane.getChildren().add(button);
    }
    
    /**
     * createButtons()
     * Cria todos os botoes que serao apresentados em nosso Menu e suas SubScenes
     */
    private void createButtons(){
        createStartButton();
        createScoreButton();
        createHelpButton();
        createCreditsButton();
        createExitButton();
    }
    
    /**
     * createStartButton()
     * Cria o botao que nos revelara a SubScene de escolha de naves
     */
    private void createStartButton() {
        MenuButton startButton = new MenuButton("PLAY");
        addMenuButton(startButton);
        
        startButton.setOnAction((ActionEvent event) -> {
            showSubscene(shipChooserSubscene);
        });
    }
    /**
     * createScoreButton()
     * Cria o botao que nos revelera a SubScene de Scores (NAO IMPLEMENTADA)
     */
     private void createScoreButton() {
        MenuButton scoreButton = new MenuButton("SCORES");
        addMenuButton(scoreButton);
        
        scoreButton.setOnAction((ActionEvent event) -> {
            showSubscene(scoreSubscene);
        });
    }
    
     /**
     * createHelpButton()
     * Cria o botao que nos revelera a SubScene de Help (NAO IMPLEMENTADA)
     */
    private void createHelpButton() {
        MenuButton helpButton = new MenuButton("HELP");
        addMenuButton(helpButton);
        
        helpButton.setOnAction((ActionEvent event) -> {
            showSubscene(helpSubscene);
        });
    }
    
    /**
     * createCreditsButton()
     * Cria o botao que nos revelera a SubScene de Credits (NAO IMPLEMENTADA)
     */
    private void createCreditsButton() {
        MenuButton creditsButton = new MenuButton("CREDITS");
        addMenuButton(creditsButton);
        
        creditsButton.setOnAction((ActionEvent event) -> {
            showSubscene(creditsSubscene);
        });
    }
    
    /**
     * createExitButton()
     * Cria o botao que sair da nossa aplicacao
     */
    private void createExitButton() {
        MenuButton exitButton = new MenuButton("EXIT");
        addMenuButton(exitButton);
        
        exitButton.setOnAction((ActionEvent event) -> {
            mainStage.close();
        });
    }
    
    /**
     * createBackground()
     * Cria o plano de fundo da nossa janela de menu e o adiciona no AnchorPane
     */
    private void createBackground(){
        Image backgroundImage = new Image("View/resources/darkPurple.png", 256, 256, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        mainPane.setBackground(new Background(background));
    }
    
    /**
     * createLogo()
     * Cria a logo do nosso jogo e adiciona no AnchorPane
     */
    private void createLogo(){
        ImageView logo = new ImageView("View/resources/game_logo.png");
        logo.setLayoutX(380);
        logo.setLayoutY(20);
        logo.setOnMouseEntered((MouseEvent event) -> {
            logo.setEffect(new DropShadow());
        });
        logo.setOnMouseExited((MouseEvent event) -> {
            logo.setEffect(null);
        });
        
        mainPane.getChildren().add(logo);
    }
    
}
