/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Entity.Alien;
import Entity.Barrier;
import Entity.Default;
import Entity.Shot;
import UI.SHIP;
import UI.SmallInfoLabel;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @see game.SpaceInvaders
 * class GameViewManager -  sera nossa engine
 * Instancia um novo AnchorPane, Scene e Stage para que possamos ter algo dedicado ao jogo
 * A dimensao da tela eh 1080x720
 * As verificacoes de colisao, limites, proxima fase e fim de jogo sao todas executadas por aqui
 * @author joao
 */
public class GameViewManager {
    
    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;
    
    private static final int GAME_WIDTH = 1080;
    private static final int GAME_HEIGHT = 720;
    
    private Stage menuStage;
    private Default ship;
    
    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private int angle;
    private AnimationTimer gameTimer;
    
    private GridPane gridPane1;
    private GridPane gridPane2;
    private final static String BACKGROUND_PATH = "UI/resources/background.jpg";
    
    private final static String ALIEN_GREEN_PATH = "UI/resources/green_alien.png";
    private final static String ALIEN_YELLOW_PATH = "UI/resources/yellow_alien.png";
    private final static String ALIEN_PINK_PATH = "UI/resources/pink_alien.png";
    private final static String ALIEN_BEIGI_PATH = "UI/resources/beige_alien.png";
    private final static String SHIP_SHOT_PATH = "UI/resources/fire01.png";
    private final static String ALIEN_SHOT_PATH = "UI/resources/fire17.png";
    private final static String BARRIER_PATH = "UI/resources/barrier.png";
    
    private ArrayList<Alien> greenAliens = new ArrayList<>(11);
    private ArrayList<Alien> pinkAliens = new ArrayList<>(22);
    private ArrayList<Alien> beigeAliens = new ArrayList<>(22);
    
    private Alien yellowAliens;
    
    private ArrayList<Shot> shipShots = new ArrayList<>(0);
    private ArrayList<Shot> alienShots = new ArrayList<>(0);
    
    private ArrayList<Barrier> barriers = new ArrayList<>(0);
    
    Random rng;
    
    private ImageView[] playerLifes;
    private SmallInfoLabel pointsLabel;
    private SmallInfoLabel stageLabel;
    private int playerLife;
    private int points;
    private final static String LIFE_PATH = "UI/resources/life.png";
    
    private final static int ALIEN_RADIUS = 17;
    private final static int SHIP_RADIUS = 30;
    private final static int SHOT_RADIUS = 8;
    private final static int BARRIER_RADIUS = 12;
    
    private double alienRotation = 0.50;
    private double alienCooldown = 300;
    
    private int currentStage = 0;
    private boolean MACH1 = false;
    private boolean MACH2 = false;
    private boolean MACH3 = false;

    
    /**
     * Construtor da Engine
     * Inicializa nosso Stage
     * Cria os Listeners de entrada de teclado para nossa nave
     * Cria nosso plano de fundo movel
     * E inicializa nosso estagio inicial como zero, ou seja, o primeiro
     */
    public GameViewManager(){
        initializaStage();
        createKeyListeners();
        createBackground();
        currentStage = 0;
    }

    /**
     * Inicializa o Stage do nosso jogo
     * gamePane sera onde vamos inserir nossos objetos
     * gameStage eh o topo da nossa aplicacao onde adicionamos nossa Scene
     */
    private void initializaStage() {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
        rng = new Random();
    }

    /**
     * Ativa os KeyListeners da nossa nave
     * Se apertou seta da esquerda, entao seta da esquerda = true
     * Se apertou seta da direita, entao seta da direita = true
     * Se soltou seta da esquerda, entao seta da esquerda = false
     * Se soltou seta da direita, entao seta da direita = false
     * Se soltou barra de espaco, entao gera um tiro na posicao atual da nave
     */
    private void createKeyListeners() {
        
        gameScene.setOnKeyPressed((KeyEvent event) -> {
           if(event.getCode() == KeyCode.LEFT){
               isLeftKeyPressed = true;
               //System.out.println("Apertou esquerda");
               
           } else if(event.getCode() == KeyCode.RIGHT){
               isRightKeyPressed = true;
               //System.out.println("Apertou Direita");
           }
           
        });
        
        gameScene.setOnKeyReleased((KeyEvent event) -> {
            if(event.getCode() == KeyCode.LEFT){
                isLeftKeyPressed = false;
                //System.out.println("Soltou esquerda");
           } else if(event.getCode() == KeyCode.RIGHT){
               isRightKeyPressed = false;
               //System.out.println("Soltou Direita");
           }
           if(event.getCode() == KeyCode.SPACE){
               if(!(shipShots.size() > 3)){
                   Shot fired = new Shot(5, SHIP_SHOT_PATH, -1);
                    fired.setLayoutX(ship.getLayoutX() + 30);
                    fired.setLayoutY(ship.getLayoutY() - 10);
                    shipShots.add(fired);
                    gamePane.getChildren().add(fired);
                }       
            }    
        });
        
    }
    /**
     * CreateNewGame()
     * Esconde a janela anterior de menu, cria nossa nave de acordo com a escolhida, cria os elementos de jogo (na fase 0), cria um Loop para o jogo ( AnimationTimer) e exibe nosso Stage de jogo
     * @param menuStage nossa janela anterior, sera escondido com o .hide()
     * @param choosenShip nave escolhida na SubScene da janela anterior, carrega a URl da nave que queremos
     */
    public void createNewGame(Stage menuStage, SHIP choosenShip){
        this.menuStage = menuStage;
        this.menuStage.hide();
        createShip(choosenShip);
        createGameElements(currentStage);
        createGameLoop();
        gameStage.show();
    }
    
    /**
     * createShip()
     * Esta funcao inicializa nossa nave de acordo com a opcao escolhida
     * Posicao em X = 20% da largura da tela
     * Posicao em Y = 720 - 60 px
     * Tamanho da nave 72x54 px
     * Adiciona nossa nave no Panel do jogo
     * @param choosenShip 
     */
    private void createShip(SHIP choosenShip){
        ship = new Default(3, choosenShip.getUrl());
        ship.setLayoutX(GAME_WIDTH*0.2);
        ship.setLayoutY(GAME_HEIGHT - 60);
        ship.setFitHeight(54);
        ship.setFitWidth(72);
        gamePane.getChildren().add(ship);
    }
    
    /**
     * createGameLoop()
     * O coracao do nosso jogo, aqui diversas funcoes serao chamadas.
     * Todas estas funcoes sao repetidas inumeras vezes para o andamento fluido do jogo
     * Entre elas estao as funcoes de movimento do fundo, da nave e dos elementos automaticos do jogo
     * Ha tambem as funcoes que atraves do Random() atiram com os invasores e criam nosso Alien Especial
     * Verificacao de colisao e fora da tela
     * Funcao para o proximo nivel caso os criteiros tenham sido atingidos
     * 
     */
    private void createGameLoop(){
        gameTimer = new AnimationTimer(){
            @Override
            public void handle(long now) {
                moveBackground();
                moveGameElements();
                tryAlienShoot();
                trySpecialAlien();
                checkIfElementsAreBehindTheShipAndEndgame();
                checkIfElementsCollide();
                checkSideCollision();
                checkIfShotsAreOutsideScreen();
                nextLevel();
                moveShip(); 
            }     
        };
        
        gameTimer.start();
    }
    /**
     * moveShip()
     * Move a nossa nave e gera uma pequena rotacao de acordo com a entrada "escutada" pelos KeyListeners
     */
    private void moveShip(){
        if(isLeftKeyPressed && !isRightKeyPressed){
            if(angle > -10){
                angle -= 2;
            }
            ship.setRotate(angle);
            if(ship.getLayoutX() > 0){
                ship.setLayoutX(ship.getLayoutX() - ship.getSpeed());
            }
            
        }
        if(!isLeftKeyPressed && isRightKeyPressed){
            if(angle < 10){
                angle += 2;
            }
            ship.setRotate(angle);
            if(ship.getLayoutX() < 1010){
                ship.setLayoutX(ship.getLayoutX() + ship.getSpeed());
            }
    
        }
        if(!isLeftKeyPressed && !isRightKeyPressed){
            if(angle < 0){
                angle += 5;
            } else if(angle > 0){
                angle -= 5;
            }
            ship.setRotate(angle);
            
        }
        if(isLeftKeyPressed && isRightKeyPressed){
            if(angle < 0){
                angle += 5;
            } else if(angle > 0){
                angle -= 5;
            }
            ship.setRotate(angle);
            
        }
        
    }
    /**
     * createBackground()
     * Cria nosso plano de fundo, assim como no projeto 1.
     * Duas imagens sao postos uma acima da outra e quando a primeira desaperece da tela, a mesa é colocada ao topo para dar impressao de um fundo sem fim em movimento
     */
    private void createBackground(){
        gridPane1 = new GridPane();
        gridPane2 = new GridPane();
        
        ImageView backgroundImage1 = new ImageView(BACKGROUND_PATH);
        ImageView backgroundImage2 = new ImageView(BACKGROUND_PATH);
        GridPane.setConstraints(backgroundImage1, 0, 0);
        GridPane.setConstraints(backgroundImage2, 0, 0);
        gridPane1.getChildren().add(backgroundImage1);
        gridPane2.getChildren().add(backgroundImage2);
        
        gridPane1.setLayoutY(0);
        gridPane2.setLayoutY(-720);
        gamePane.getChildren().addAll(gridPane1, gridPane2);
    }
    
    /**
     * moveBackground()
     * Move os fundos 2 px por frame e quando 1 dos dois atinge o final da nossa tela, o reinicia para o topo
     */
    private void moveBackground(){
        gridPane1.setLayoutY(gridPane1.getLayoutY() + 2);
        gridPane2.setLayoutY(gridPane2.getLayoutY() + 2);
        
        if(gridPane1.getLayoutY() >= 720) gridPane1.setLayoutY(-720);
        if(gridPane2.getLayoutY() >= 720) gridPane2.setLayoutY(-720);
        
    }
    
    /**
     * createGameElements()
     * Cria nossas barreiras estaticas
     * Cria nossa matriz de Aliens com 3 ArrayList diferentes, um para cada cor
     * Crita também nossos labels onde serao exibidos o estagio atual, a pontucao e a quantidade de vidas
     * @param level level atual do nosso jogo
     */
    private void createGameElements(int level){
        
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 15; j++){
                Barrier tmp = new Barrier(BARRIER_PATH);
                tmp.setLayoutX(80 + (j%15)*30 + (j/3)*120);
                tmp.setLayoutY(540 + i*32);
                barriers.add(tmp);
                gamePane.getChildren().add(tmp);
            }
        }
       
        
        
        for(int i = 0; i < 11; i++){
            Alien tmp = new Alien(1, ALIEN_GREEN_PATH);
            greenAliens.add(tmp);
            greenAliens.get(i).setFitHeight(45);
            greenAliens.get(i).setFitWidth(45);
            greenAliens.get(i).setLayoutX(10 + 50*i);
            greenAliens.get(i).setLayoutY(35 + level*15);
            gamePane.getChildren().add(greenAliens.get(i));
        }
        
        for(int i = 0; i < 22; i++){
            Alien tmp = new Alien(1, ALIEN_PINK_PATH);
            pinkAliens.add(tmp);
            pinkAliens.get(i).setFitHeight(45);
            pinkAliens.get(i).setFitWidth(45);
            pinkAliens.get(i).setLayoutX(10 + 50*(i%11));
            pinkAliens.get(i).setLayoutY(100 + 50*(i/11) + level*15);
            gamePane.getChildren().add(pinkAliens.get(i));
            
        }
        
        for(int i = 0; i < 22; i++){
            Alien tmp = new Alien(1, ALIEN_BEIGI_PATH);
            beigeAliens.add(tmp);
            beigeAliens.get(i).setFitHeight(45);
            beigeAliens.get(i).setFitWidth(45);
            beigeAliens.get(i).setLayoutX(10 + 50*(i%11));
            beigeAliens.get(i).setLayoutY(205 + 50*(i/11) + level*15);
            gamePane.getChildren().add(beigeAliens.get(i));  
        }
        
        
        pointsLabel = new SmallInfoLabel("POINTS: 00");
        pointsLabel.setLayoutX(930);
        pointsLabel.setLayoutY(20);
        gamePane.getChildren().add(pointsLabel);
        
        String stageText = "STAGE: " + currentStage;
        stageLabel = new SmallInfoLabel(stageText);
        stageLabel.setLayoutX(930);
        stageLabel.setLayoutY(650);
        gamePane.getChildren().add(stageLabel);
        
        playerLife = 2;
        playerLifes = new ImageView[3];
        
        for(int i = 0; i < playerLifes.length; i++){
           playerLifes[i] = new ImageView(LIFE_PATH);
           playerLifes[i].setFitWidth(35);
           playerLifes[i].setFitHeight(35);
           playerLifes[i].setLayoutX(930 + (i * 48));
           playerLifes[i].setLayoutY(80);
           gamePane.getChildren().add(playerLifes[i]); 
        }
        
    }
    
    /**
     * moveGameElements()
     * Chama as funcoes de movimento automatico de todos os objetos inseridos em nosso Pane
     */
    private void moveGameElements(){
        
        for(int i = 0; i < shipShots.size ();i++){
            shipShots.get(i).autoMove();
        }
        
        for(int i = 0; i < alienShots.size ();i++){
            alienShots.get(i).autoMove();
        }
        if(yellowAliens != null) yellowAliens.autoMove();
        
        for (Alien pinkAlien : pinkAliens) {
            pinkAlien.autoMove();
            if(pinkAlien.getRotate() == 12){
                alienRotation = -0.50;
                pinkAlien.setRotate(pinkAlien.getRotate() + alienRotation);
            } else if(pinkAlien.getRotate() == -12){
                alienRotation = 0.50;
                pinkAlien.setRotate(pinkAlien.getRotate() + alienRotation);
            } else {
                pinkAlien.setRotate(pinkAlien.getRotate() + alienRotation);
            }
        }
        
        
        
        for (Alien greenAlien : greenAliens) {
            greenAlien.autoMove();
            if(greenAlien.getRotate() == 12){
                alienRotation = -0.50;
                greenAlien.setRotate(greenAlien.getRotate() + alienRotation);
            } else if(greenAlien.getRotate() == -12){
                alienRotation = 0.50;
                greenAlien.setRotate(greenAlien.getRotate() + alienRotation);
            } else {
                greenAlien.setRotate(greenAlien.getRotate() + alienRotation);
            }
            
        }
        
        for (Alien beigeAlien : beigeAliens) {
            beigeAlien.autoMove();
            if(beigeAlien.getRotate() == 12){
                alienRotation = -0.50;
                beigeAlien.setRotate(beigeAlien.getRotate() + alienRotation);
            } else if(beigeAlien.getRotate() == -12){
                alienRotation = 0.50;
                beigeAlien.setRotate(beigeAlien.getRotate() + alienRotation);
            } else {
                beigeAlien.setRotate(beigeAlien.getRotate() + alienRotation);
            }
        }
        
    }
    
    /**
     * checkIfElementsAreBehindTheShipAndEndgame()
     * Verifica se ha algum elemento atras da nossa nave, no caso os aliens. Caso positivo zera as vidas para um fim de jogo forcado
     */
    private void checkIfElementsAreBehindTheShipAndEndgame(){
        
        for (ImageView pinkAlien : pinkAliens) {
            if (pinkAlien.getLayoutY() > 600) {
                removeAllLifes();
            }
        }
        
        for (ImageView greenAlien : greenAliens) {
            if (greenAlien.getLayoutY() > 600) {
                removeAllLifes();
            }
        }
        
        for (ImageView beigeAlien : beigeAliens) {
            if (beigeAlien.getLayoutY() > 600) {
                removeAllLifes();
            }
        }
    }
    
    /**
     * checkIfShotsAreOutsideScreen()
     * Verifica se os tiros da nossa nave ou dos aliens ainda estao presentes na tela.
     * Caso contrario, os remove de seus ArrayLists e do Pane
     */
    public void checkIfShotsAreOutsideScreen(){
        for(int i = 0; i < shipShots.size(); i++){
            if(shipShots.get(i).getLayoutY() < 0){
                gamePane.getChildren().remove(shipShots.get(i));
                shipShots.remove(i);
                i--;
            }
        }
        for(int i = 0; i < alienShots.size(); i++){
            if(alienShots.get(i).getLayoutY() > 1080){
                gamePane.getChildren().remove(alienShots.get(i));
                alienShots.remove(i);
                i--;
            }
        }
    }
    
    
    /**
     * checkSideCollision()
     * Verifica se algum dos aliens tocou as bordas do campo.
     * Caso positivo, inverte a direcao de movimento, realiza mais um movimento automatico e pula uma linha
     */
    private void checkSideCollision(){
        for (Alien pinkAlien : pinkAliens) {
            if(pinkAlien.getLayoutX() >= 1030 || pinkAlien.getLayoutX() <= 0){
                reverseAliensDirection();
                return;
            }
        }
        
        for (Alien greenAlien : greenAliens) {
            if(greenAlien.getLayoutX() >= 1030 || greenAlien.getLayoutX() <= 0){
                reverseAliensDirection();
                return;
            }  
        }
        
        for (Alien beigeAlien : beigeAliens) {
            if(beigeAlien.getLayoutX() >= 1030 || beigeAlien.getLayoutX() <= 0 ){
                reverseAliensDirection();
                return;
            }   
        }
        
        if(yellowAliens != null){
            if((yellowAliens.getLayoutX() >= 1030 || yellowAliens.getLayoutX() <= 0)){
                yellowAliens.reverseDirection();
                yellowAliens.autoMove();
            }   
        }
        
        
    }
    
    
    /**
     * reverseAliensDirection()
     * Inverte o sentido de movimento dos aliens, pula uma linha e realiza mais um movimento para termos a certeza de que nao esta mais na borda
     */
    private void reverseAliensDirection(){
        for (Alien pinkAlien : pinkAliens) {
            pinkAlien.reverseDirection();
            pinkAlien.downLine();
            pinkAlien.autoMove();
        }
  
        for (Alien greenAlien : greenAliens) {
            greenAlien.reverseDirection();
            greenAlien.downLine();
            greenAlien.autoMove();
        }
        
        for (Alien beigeAlien : beigeAliens) {
            beigeAlien.reverseDirection();
            beigeAlien.downLine();
            beigeAlien.autoMove();
        }
        
    }
    
    
    /**
     * trySpecialAlien()
     * Caso ainda nao tenha um alien especial em jogo, tenta criar com o auxilio do Math.Random()
     */
    private void trySpecialAlien(){
        if(rng.nextInt(3000) < 3 && yellowAliens == null){
            yellowAliens = new Alien(6, ALIEN_YELLOW_PATH);
            yellowAliens.setLayoutX(10);
            yellowAliens.setLayoutY(10);
            yellowAliens.setFitHeight(45);
            yellowAliens.setFitWidth(45);
            gamePane.getChildren().add(yellowAliens);
        }
        
    }
    
    /**
     * removeAllLifes()
     * Chama a funcao removeLife() varias vezes ate que vida = -1
     */
    private void removeAllLifes(){
        while(playerLife > -1){
            removeLife();
        }
    }
    
    /**
     * removeLife()
     * Remove um sprite de coracao do nosso Pane
     * Subtrai 1 da contagem de vida
     * Caso esta contagem se torne < 0 entao encerra nosso jogo e exibe o menu inicial
     */
    private void removeLife(){
        gamePane.getChildren().remove(playerLifes[playerLife]);
        playerLife--;
        if(playerLife < 0){
            gameStage.close();
            gameTimer.stop();
            menuStage.show();
        }
    }
    
    /**
     * checkIfElementsCollide()
     * Verifica todos os casos de colisoes dos objetos presentes no Pane
     * Verifica se ambas os tiros chocaram com algo
     * Verifica se as barreiras chocaram com algo
     * Verifica se a nave foi atingida
     */
    private void checkIfElementsCollide(){
        
        if(yellowAliens != null) {
            for(int i = 0; i < shipShots.size(); i++){
            if (ALIEN_RADIUS + SHOT_RADIUS > calculateDistance(shipShots.get(i).getLayoutX() + 15, yellowAliens.getLayoutX() + 35, shipShots.get(i).getLayoutY() + 15, yellowAliens.getLayoutY() + 35)){
                    gamePane.getChildren().remove(shipShots.get(i));
                    shipShots.remove(i);
                    i--;
                    gamePane.getChildren().remove(yellowAliens);
                    yellowAliens = null;
                    addPoints(25);
                    break;
                }
            }     
        }
        
        for(int j = 0; j < shipShots.size(); j++){
            for (int i = 0; i < greenAliens.size(); i++) {
                if (ALIEN_RADIUS + SHOT_RADIUS > calculateDistance(shipShots.get(j).getLayoutX() + 15, greenAliens.get(i).getLayoutX() + 35, shipShots.get(j).getLayoutY() + 15, greenAliens.get(i).getLayoutY() + 35)){
                    
                    gamePane.getChildren().remove(shipShots.get(j));
                    shipShots.remove(j);
                    j--;
                    
                    gamePane.getChildren().remove(greenAliens.get(i));
                    greenAliens.remove(i);
                    i--;
                    
                    addPoints(5);
                    
                    break;

                }
            }
        }
            
        for(int j = 0; j < shipShots.size(); j++){    
            for (int i = 0; i < pinkAliens.size(); i++) {
                if (ALIEN_RADIUS + SHOT_RADIUS > calculateDistance(shipShots.get(j).getLayoutX() + 15, pinkAliens.get(i).getLayoutX() + 35, shipShots.get(j).getLayoutY() + 15, pinkAliens.get(i).getLayoutY() + 35)){
                    gamePane.getChildren().remove(shipShots.get(j));
                    shipShots.remove(j);
                    j--;
                    gamePane.getChildren().remove(pinkAliens.get(i));
                    pinkAliens.remove(i);
                    i--;
                    
                    addPoints(3);
                    
                    break;

                }
            }
        }
            
        for(int j = 0; j < shipShots.size(); j++){   
            for (int i = 0; i < beigeAliens.size(); i++) {
                if (ALIEN_RADIUS + SHOT_RADIUS > calculateDistance(shipShots.get(j).getLayoutX() + 15, beigeAliens.get(i).getLayoutX() + 35, shipShots.get(j).getLayoutY() + 15, beigeAliens.get(i).getLayoutY() + 35)){
                    gamePane.getChildren().remove(shipShots.get(j));
                    shipShots.remove(j);
                    j--;
                    gamePane.getChildren().remove(beigeAliens.get(i));
                    beigeAliens.remove(i);
                    i--;
                    
                    addPoints(1);
                    
                    break;
                }
            } 
        }
        
        for(int j = 0; j < shipShots.size(); j++){
            for (int i = 0; i < barriers.size(); i++) {
                if (SHOT_RADIUS + BARRIER_RADIUS > calculateDistance(shipShots.get(j).getLayoutX() + 15, barriers.get(i).getLayoutX() + 35, shipShots.get(j).getLayoutY() + 15, barriers.get(i).getLayoutY() + 35)){
                    
                    gamePane.getChildren().remove(shipShots.get(j));
                    shipShots.remove(j);
                    j--;
                    
                    gamePane.getChildren().remove(barriers.get(i));
                    barriers.remove(i);
                    i--;
                    
                    break;

                }
            }
        }
            
        for(int j = 0; j < barriers.size(); j++){    
            for (int i = 0; i < pinkAliens.size(); i++) {
                if (ALIEN_RADIUS + BARRIER_RADIUS > calculateDistance(barriers.get(j).getLayoutX() + 15, pinkAliens.get(i).getLayoutX() + 35, barriers.get(j).getLayoutY() + 15, pinkAliens.get(i).getLayoutY() + 35)){
                    gamePane.getChildren().remove(barriers.get(j));
                    barriers.remove(j);
                    j--;
                    
                    break;

                }
            }
        }
            
        for(int j = 0; j < barriers.size(); j++){   
            for (int i = 0; i < beigeAliens.size(); i++) {
                if (ALIEN_RADIUS + BARRIER_RADIUS > calculateDistance(barriers.get(j).getLayoutX() + 15, beigeAliens.get(i).getLayoutX() + 35, barriers.get(j).getLayoutY() + 15, beigeAliens.get(i).getLayoutY() + 35)){
                    gamePane.getChildren().remove(shipShots.get(j));
                    barriers.remove(j);
                    j--;
                    
                    break;
                }
            } 
        }
        
        for(int j = 0; j < greenAliens.size(); j++){   
            for (int i = 0; i < barriers.size(); i++) {
                if (BARRIER_RADIUS + ALIEN_RADIUS > calculateDistance(greenAliens.get(j).getLayoutX() + 15, barriers.get(i).getLayoutX() + 35, greenAliens.get(j).getLayoutY() + 15, barriers.get(i).getLayoutY() + 35)){
                    gamePane.getChildren().remove(barriers.get(i));
                    barriers.remove(i);
                    i--;

                    break;
                }
            } 
        }
        
        
        
        for(int j = 0; j < alienShots.size(); j++){   
            for (int i = 0; i < barriers.size(); i++) {
                if (BARRIER_RADIUS + ALIEN_RADIUS > calculateDistance(alienShots.get(j).getLayoutX() + 15, barriers.get(i).getLayoutX() + 35, alienShots.get(j).getLayoutY() + 15, barriers.get(i).getLayoutY() + 35)){
                    gamePane.getChildren().remove(alienShots.get(j));
                    alienShots.remove(j);
                    j--;
                    gamePane.getChildren().remove(barriers.get(i));
                    barriers.remove(i);
                    i--;
                    
                    break;
                }
            } 
        }
        
        for(int j = 0; j < alienShots.size(); j++){   
            if (SHIP_RADIUS + SHOT_RADIUS > calculateDistance(alienShots.get(j).getLayoutX() + 15, 
                    ship.getLayoutX() + 35, alienShots.get(j).getLayoutY() + 15, ship.getLayoutY() + 35)){
                gamePane.getChildren().remove(alienShots.get(j));
                alienShots.remove(j);
                j--;       
                
                resetShip();
                removeLife();
                break;
            }   
        }
        
    }
    /**
     * resetShip()
     * Quando leva um tiro limpa os tiros e coloca a nave na posicao incial
     */
    public void resetShip(){
        ship.setLayoutX(GAME_WIDTH*0.2);
        
        for(int i = 0; i < alienShots.size(); i++){
            gamePane.getChildren().remove(alienShots.get(i));
            alienShots.remove(i);
            i--;
        }
        
        for(int i = 0; i < shipShots.size(); i++){
            gamePane.getChildren().remove(shipShots.get(i));
            shipShots.remove(i);
            i--;
        }
        
    }
    
    /**
     * calculateDistance()
     * Calcula e retorna a distancia de dois pontos
     * @param x1 coordenada em X do objeto1
     * @param x2 coordenada em X do objeto2
     * @param y1 coordenada em Y do objeto1
     * @param y2 coordenada em Y do objeto2
     * @return 
     */
    private double calculateDistance(double x1, double x2, double y1, double y2){
        return Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1 - y2, 2));
    }
    
    /**
     * tryAlienShoot()
     * Tenta atirar com os aliens da primeira fileira ate a ultima
     * Atira na coluna da nave sempre que a condicao de alienCooldown < 0 for verdadeira
     */
    private void tryAlienShoot(){
        alienCooldown--;
        
        for(int i = 0; i < beigeAliens.size(); i++){            
             if(alienCooldown < 0){
                if(beigeAliens.get(i).getLayoutX() == ship.getLayoutX()){
                    Shot tmp = new Shot(5, ALIEN_SHOT_PATH, 1);
                    tmp.setLayoutX(beigeAliens.get(i).getLayoutX() + ALIEN_RADIUS);
                    tmp.setLayoutY(beigeAliens.get(i).getLayoutY() + 10);
                    alienShots.add(tmp);
                    gamePane.getChildren().add(tmp); 
                    alienCooldown = 300;
                }
                
            }
            
            if(rng.nextInt(10000) < currentStage*4 + 1){
                Shot tmp = new Shot(5, ALIEN_SHOT_PATH, 1);
                tmp.setLayoutX(beigeAliens.get(i).getLayoutX() + ALIEN_RADIUS);
                tmp.setLayoutY(beigeAliens.get(i).getLayoutY() + 10);
                alienShots.add(tmp);
                gamePane.getChildren().add(tmp);
                return;   
            }     
        }
        
        for(int i = 0; i < pinkAliens.size(); i++){            
             if(alienCooldown < 0){
                if(pinkAliens.get(i).getLayoutX() == ship.getLayoutX()){
                    Shot tmp = new Shot(5, ALIEN_SHOT_PATH, 1);
                    tmp.setLayoutX(pinkAliens.get(i).getLayoutX() + ALIEN_RADIUS);
                    tmp.setLayoutY(pinkAliens.get(i).getLayoutY() + 10);
                    alienShots.add(tmp);
                    gamePane.getChildren().add(tmp);  
                    alienCooldown = 300;
                }
                
            }
            
            if(rng.nextInt(10000) < currentStage*2 + 1){
                Shot tmp = new Shot(5, ALIEN_SHOT_PATH, 1);
                tmp.setLayoutX(pinkAliens.get(i).getLayoutX() + ALIEN_RADIUS);
                tmp.setLayoutY(pinkAliens.get(i).getLayoutY() + 10);
                alienShots.add(tmp);
                gamePane.getChildren().add(tmp);
                return;   
            }     
        }
        
        for(int i = 0; i < greenAliens.size(); i++){            
             if(alienCooldown < 0){
                if(greenAliens.get(i).getLayoutX() == ship.getLayoutX()){
                    Shot tmp = new Shot(5, ALIEN_SHOT_PATH, 1);
                    tmp.setLayoutX(greenAliens.get(i).getLayoutX() + ALIEN_RADIUS);
                    tmp.setLayoutY(greenAliens.get(i).getLayoutY() + 10);
                    alienShots.add(tmp);
                    gamePane.getChildren().add(tmp);  
                    alienCooldown = 300;
                }
                
            }
            
            if(rng.nextInt(10000) < currentStage*2 + 1){
                Shot tmp = new Shot(5, ALIEN_SHOT_PATH, 1);
                tmp.setLayoutX(greenAliens.get(i).getLayoutX() + ALIEN_RADIUS);
                tmp.setLayoutY(greenAliens.get(i).getLayoutY() + 10);
                alienShots.add(tmp);
                gamePane.getChildren().add(tmp);
                return;   
            }     
        }
    }
    
    /**
     * addPoint()
     * Adiciona pontos na pontuacao atual e tambem reescreve o label de pontos
     * @param toAdd 
     */
    private void addPoints(int toAdd){
        points += toAdd;
        String text = String.valueOf(points);
        pointsLabel.setText(text);

    }
    
    /**
     * aliensRemains()
     * Retorna a quantidade de aliens remanescentes no jogo
     * @return 
     */
    private int aliensRemains(){
        return (  pinkAliens.size() + greenAliens.size() + beigeAliens.size() );
    }
    
    /**
     * aliensSpeedUp()
     * Chama a funcao raiseSpeed() de todos os aliens em jogo
     */
    private void aliensSpeedUp(){
        for(int i = 0; i < pinkAliens.size(); i++) pinkAliens.get(i).raiseSpeed();
        for(int i = 0; i < greenAliens.size(); i++) greenAliens.get(i).raiseSpeed();
        for(int i = 0; i < beigeAliens.size(); i++) beigeAliens.get(i).raiseSpeed();

    }
    
    /**
     * nextLevel()
     * Inicia um proximo level caso nao haja mais aliens em jogo
     * Responsavel tambem por acelerar os aliens conforme o andamento do jogo
     */
    private void nextLevel(){
        if(pinkAliens.isEmpty() && greenAliens.isEmpty() && beigeAliens.isEmpty()){
            gamePane.getChildren().remove(yellowAliens);
            yellowAliens = null;
            
            for(int i = 0; i < shipShots.size(); i++) gamePane.getChildren().remove(shipShots.get(i));
            shipShots.clear();
            
            for(int i = 0; i < alienShots.size(); i++) gamePane.getChildren().remove(alienShots.get(i));
            alienShots.clear();
            
            for(int i = 0; i < barriers.size(); i++) gamePane.getChildren().remove(barriers.get(i));
            barriers.clear();
            
            gamePane.getChildren().remove(pointsLabel);
            gamePane.getChildren().remove(stageLabel);
            for (ImageView playerLife1 : playerLifes) { 
                gamePane.getChildren().remove(playerLife1);
            }
            MACH1 = false;
            MACH2 = false;
            MACH3 = false;
            createGameElements(++currentStage);
        }
         
        if(aliensRemains() == 25 && !MACH1){
            aliensSpeedUp();
            MACH1 = true;
        }
        if(aliensRemains() == 15 && !MACH2){
            aliensSpeedUp();  
            MACH2 = true;
        }   
        if(aliensRemains() == 5 && !MACH3) {
            aliensSpeedUp();
            MACH3 = true;
        }
        if(aliensRemains() == 1){
            Alien last = null;
            if(!greenAliens.isEmpty()){
                last = greenAliens.get(0);
            } else if(!beigeAliens.isEmpty()){
                last = beigeAliens.get(0);
            } else if(!pinkAliens.isEmpty()){
                last = pinkAliens.get(0);
            }
            if(last.getDirection() == 1){
                last.setSpeed(6);
            } else {
                last.setSpeed(3);
            }
        }
    }
    
}
