/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Class ShipPicker
 * Caixa de ajusto vertical para o auxilio na disposicao de informacoes na SubScene de escolha de nave
 * @author joao
 */
public class ShipPicker extends VBox {
  
    private ImageView circleImage;
    private ImageView shipImage;
    
    private String circleNotChoosen = "UI/resources/shipchooser/grey_circle.png";
    private String circleChoosen = "UI/resources/shipchooser/grey_boxTick.png";
    
    private SHIP ship;
    
    private boolean isCircleChoosen;
    /**
     * 
     * @param ship 
     */
    public ShipPicker(SHIP ship){
        circleImage = new ImageView(circleNotChoosen);
        shipImage = new ImageView(ship.getUrl());
        this.ship = ship;
        isCircleChoosen = false;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.getChildren().add(circleImage);
        this.getChildren().add(shipImage);
    }
    
    /**
     * 
     * @return returna nave
     */
    public SHIP getShip(){
        return ship;
    }
    
    /**
     * 
     * @return retorna se a opcao foi marcada ou nao
     */
    public boolean getIsCircleChoonse(){
        return isCircleChoosen;
    }
    
    /**
     * 
     * @param isCircleChoosen flag para opcao maracada
     */
    public void setIsCircleChoosen(boolean isCircleChoosen){
        this.isCircleChoosen = isCircleChoosen;
        String imageToSet = this.isCircleChoosen ? circleChoosen : circleNotChoosen;
        circleImage.setImage(new Image(imageToSet));
    }
    
    
    
    
}
