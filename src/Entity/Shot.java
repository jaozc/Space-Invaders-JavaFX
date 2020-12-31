/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Interfaces.AutoMoveInterface;

/**
 *  Class Shot
 *  Classe responsavel por guardar as caracterias das balas dos aliados e inimigos
 * @author joao
 */
public class Shot extends Default implements AutoMoveInterface {
    
    int direction;
    /**
     * Construtor
     * @param pSpeed velocidade de movimento
     * @param spritePath URl para imagem do tiro
     * @param isEnemy 1: Inimigo -1:Aliado
     */
    public Shot(int pSpeed, String spritePath, int isEnemy) {
        super(pSpeed, spritePath);
        direction = isEnemy;
    }
    
    /**
     * Movimento automatico
     */
    @Override
    public void autoMove(){
        setLayoutY(getLayoutY() + direction*speed);
    }
    
}
