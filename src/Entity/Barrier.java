/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import javafx.scene.image.ImageView;

/**
 * Class Barrier
 * Esta classe define nossas barreiras
 * @author joao
 */
public class Barrier extends ImageView {

    /**
     * Construtor
     * @param BARRIER_PATH caminho para a imagem da barreira 
     */
    public Barrier(String BARRIER_PATH) {
        super(BARRIER_PATH);
    }
    
}
