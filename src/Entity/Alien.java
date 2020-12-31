/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Interfaces.AutoMoveInterface;
import Interfaces.DownLineInterface;
import javafx.scene.image.ImageView;

/**
 * Class Alien
 * Nossos inimigos neste jogo, esta classe define seus comportamentos de movimentavao
 * @author joao
 */
public class Alien extends Default implements AutoMoveInterface, DownLineInterface {
    
    private int direction;
    
    /**
     * Construtor
     * @param pSpeed velocidade
     * @param spriteUrl caminho para a imagem do alien
     */
    public Alien(int pSpeed, String spriteUrl){
        super(pSpeed, spriteUrl);
        direction = 1;
    }
    
    /**
    * Inverte o sentido de movimento
    */ 
    public void reverseDirection(){
        direction *= -1;
    }
    /**
     * Retorna o sentido de movimento
     * @return 
     */
    public int getDirection(){
        return direction;
    }
    
    /**
     * Movimento automatico
     */
    @Override
    public void autoMove(){
        setLayoutX(getLayoutX() + direction*speed);
    }
    /**
     * Desce uma linha
     */
    @Override
    public void downLine(){
        setLayoutY(getLayoutY() + 20);
    }
    /**
     * Aumenta velocidade em 1
     */
    public void raiseSpeed(){
        speed += 1;
    }
            
}
