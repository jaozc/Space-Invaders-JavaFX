/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Class SmallInfoLabel
 * Labels que aparecem no nosso jogo
 * @author joao
 */
public class SmallInfoLabel extends Label {

    private final static String FONT_PATH = "src/UI/resources/kenvector_future.ttf";
    private final static String LABEL_PATH = "UI/resources/glassPanel.png";
    /**
     * Construtor
     * @param text 
     */
    public SmallInfoLabel(String text){
        setPrefWidth(130);
        setPrefHeight(50);
        BackgroundImage backgroundImage =  new BackgroundImage(new Image(LABEL_PATH, 130, 50, false, true), 
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(backgroundImage));
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10,10,10,10));
        setTextFill(Color.web("#ffffff"));
        setText(text);
        setLabelFont();
    }
    /**
     * Tenta setar a font do label como kenvector_future
     */
    private void setLabelFont(){
        try{
            setFont(Font.loadFont(new FileInputStream(new File(FONT_PATH)), 15));
        } catch(FileNotFoundException e){
            setFont(Font.font("Verdana",15));
        }   
    }
    
    
}
