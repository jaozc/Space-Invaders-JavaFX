/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

/**
 * Class MenuButton
 * Reponsavel por organizar a forma dos botoes que aparecerem na aplicacao
 * @author joao
 */
public class MenuButton extends Button {
    
    private final String FONT_PATH = "src/UI/resources/kenvector_future.ttf";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/UI/resources/grey_button_pressed.png')";
    private final String BUTTON_RELEASED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/UI/resources/grey_button.png')";
    
    /**
     * 
     * @param text 
     */
    public MenuButton(String text){
        setText(text);
        setButtonFont();
        setPrefWidth(190);
        setPrefHeight(49);
        setStyle(BUTTON_RELEASED_STYLE);
        initializeButtonsListeners();
    }
    /**
     * Seta fonte kenvector_future para o botao
     */
    private void setButtonFont() { 
        try{
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), 23));
        } catch(FileNotFoundException e){
            setFont(Font.font("Verdana",23));
        }  
    }
    
    /**
     * muda estilo do botao
     */
    private void setButtonPressedStyle(){
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(45);
        setLayoutY(getLayoutY() + 4);
    }
    
    /**
     * muda estilo do botao
     */
    private void setButtonReleasedStyle(){
        setStyle(BUTTON_RELEASED_STYLE);
        setPrefHeight(49);
        setLayoutY(getLayoutY() - 4);
    }
    
    /**
     * Inicializa os Listeners para os botoes do MenuButton
     */
    private void initializeButtonsListeners(){
        
        
        setOnMousePressed((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                setButtonPressedStyle();
            }
        });
        
        setOnMouseReleased((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                setButtonReleasedStyle();
            }
        });
        
        setOnMouseEntered((MouseEvent event) -> {
            setEffect(new DropShadow());
        });
        
        setOnMouseExited((MouseEvent event) -> {
            setEffect(null);
        });
        
    }
}
