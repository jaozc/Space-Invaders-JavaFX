/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.util.Duration;

/**
 * Class SpaceInvadersSubscene
 * Classe que define o tamanho e comportamento das SubScenes presente na janela de menu da nossa aplicacao
 * @author joao
 */
public class SpaceInvadersSubscene extends SubScene{
    
    private final static String FONT_PATH = "src/UI/resources/kenvector_future.ttf";
    private final static String BACKGROUND_PATH = "UI/resources/grey_panel.png";
    
    private boolean isHidden = true;
    /**
     * 
     */
    public SpaceInvadersSubscene() {
        super(new AnchorPane(), 600, 400);
        prefWidth(600);
        prefHeight(400);
        BackgroundImage backgroundImage =  new BackgroundImage(new Image(BACKGROUND_PATH, 600, 400,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(backgroundImage));
        
        setLayoutX(1050);
        setLayoutY(180);
         
    }
    /**
     * Move a SubScene em um efeito de transicao
     */
    public void moveSubscene(){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(this);
        
        if(isHidden){
           transition.setToX(-676); 
           isHidden = false;
        } else {
           transition.setToX(0); 
           isHidden = true;
        }

        transition.play();
    }
    /**
     * 
     * @return 
     */
    public AnchorPane getPane(){
        return (AnchorPane) this.getRoot();
    }
    
    
    
    
    
}
