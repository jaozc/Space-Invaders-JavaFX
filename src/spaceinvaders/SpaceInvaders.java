/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import View.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Projeto Space Invaders - Parte 2
 * Programacao Orientada a Objetos
 * Prof. Robson L. F. Cordeiro
 * 21 de Dezembro de 2020
 * Aluno Joao Gabriel Zanao Costa
 * NUSP 11234266
 * @author joao
 * @version 2.0.0
 */
public class SpaceInvaders extends Application {
    /**
     * Inicia a thread
     * @param primaryStage 
     */
    @Override
    public void start(Stage primaryStage) {
        try{
            ViewManager manager = new ViewManager();
            primaryStage = manager.getMainStage();
            primaryStage.show();
        }catch(Exception e){
        }
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
