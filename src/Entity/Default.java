/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import javafx.scene.image.ImageView;

/**
 *  Class Default
 * Nossa classe padrao para a criacao de entidades em nosso jogo
 *
 * @author joao
 */
public class Default extends ImageView{
    
    protected int speed;
    /**
     * Construtor
     * 
     * @param pSpeed velocidade de movimento
     * @param spritePath URl para a sprite da entidade
     */
    public Default(int pSpeed, String spritePath){
        super(spritePath);
        speed = pSpeed;
        
    }
    /**
     * Seta uma nova velocidade para a entidade
     * @param pSpeed nova velocidade
     */
    public void setSpeed(int pSpeed){
        speed = pSpeed;
    }
    
    /**
     * 
     * @return velocidade da entidade 
     */
    public int getSpeed(){
        return speed;
    }
    
    
    
}
