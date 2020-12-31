/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

/**
 * enum de naves para a escolha de naves jogaveis
 * @author joao
 */
public enum SHIP {
    
    BLUE("UI/resources/shipchooser/ship_blue.png"),
    GREEN("UI/resources/shipchooser/ship_green.png"),
    ORANGE("UI/resources/shipchooser/ship_orange.png"),
    RED("UI/resources/shipchooser/ship_red.png");
    
    String urlShip;
  
    /**
     * 
     * @param urlShip url da nave
     */
    private SHIP(String urlShip){
        this.urlShip = urlShip;
        
    }
    /**
     * 
     * @return url da nave
     */
    public String getUrl(){
        return this.urlShip;
    }
    
}
