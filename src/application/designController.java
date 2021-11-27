package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class designController{

    @FXML
    private JFXButton home,encrypt,decrypt,shutt;
    @FXML
    private AnchorPane homePane,encryPane,decryPane,shuttPane;
    @FXML
    private Pane desPane;
    @FXML
    private JFXTextArea decryArea,encryArea;
    @FXML
    private JFXTextField encryKey,encryShiftKey;
    
    private double x=0,y=0;
    private int count = 1,rules ;
    private ArrayList<Integer> shift = new ArrayList<>();;
    String alphaU = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	String alphaL = "abcdefghijklmnopqrstuvwxyz";

    @FXML
    private void close(ActionEvent event) {
    	Platform.exit();
    	System.exit(0);
    }
    
    @FXML
    private void decryptionButton(ActionEvent event) {
    	
    	encryArea.setText("");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("C:\\Users\\Mazin\\Desktop"));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT Files","*.txt"));
		File selected = fileChooser.showOpenDialog(null);
		
		String plaintext = "";
		String ciphertext = readFromFile(selected);
		for(int i=0; i<ciphertext.length(); i++) {
			char textget = ciphertext.charAt(i);
			if(Character.isUpperCase(textget)) {
				int textIndex = alphaU.indexOf(textget);
				int total = (textIndex - shift.get(i%rules)) % 26;
				total = (total<0)?total + 26:total;
				plaintext += alphaU.charAt(total);
			}
			else if(Character.isLowerCase(textget)) {
				int textIndex = alphaL.indexOf(textget);
				int total = (textIndex - shift.get(i%rules)) % 26;
				total = (total<0)?total + 26:total;
				plaintext += alphaL.charAt(total);				
			}
		}
		decryArea.setText(plaintext);
		writeInToFile(plaintext, selected);
    }
    @FXML
    private void encryptionButton(ActionEvent event) {
    	decryArea.setText("");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("C:\\Users\\Mazin\\Desktop"));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT Files","*.txt"));
		File selected = fileChooser.showOpenDialog(null);
		
		String plaintext = readFromFile(selected);
		String ciphertext = "";
		for(int i=0; i<plaintext.length(); i++) {
			char textget = plaintext.charAt(i);
			if(Character.isUpperCase(textget)) {
				int textIndex = alphaU.indexOf(textget);
				int total = (textIndex + shift.get(i%rules)) % 26;
				ciphertext += alphaU.charAt(total);
			}
			else if(Character.isLowerCase(textget)) {
				int textIndex = alphaL.indexOf(textget);
				int total = (textIndex + shift.get(i%rules)) % 26;
				ciphertext += alphaL.charAt(total);				
			}
		}
		encryArea.setText(ciphertext);
    	writeInToFile(ciphertext, selected);
    }

	@FXML
    private  void menuHandler(ActionEvent event) {
    	if(event.getSource() == home) {
    		visibility(homePane,encryPane,decryPane,shuttPane);
    	}
    	if(event.getSource() == encrypt) {
    		visibility(encryPane,homePane,decryPane,shuttPane);
    	}
    	if(event.getSource() == decrypt) {
    		visibility(decryPane,homePane,encryPane,shuttPane);
    	}
    	if(event.getSource() == shutt) {
    		visibility(shuttPane,decryPane,homePane,encryPane);
    	}
    }
	private void visibility(AnchorPane needed, AnchorPane not1, AnchorPane not2, AnchorPane not3) {
		needed.setVisible(true);
		not1.setVisible(false);
		not2.setVisible(false);
		not3.setVisible(false);
	}
    @FXML
    private void minimize(ActionEvent event) {
    	Stage stage = (Stage) desPane.getScene().getWindow();
		stage.setIconified(true);
    }
    
    @FXML
    public void numberOfRuleAction(ActionEvent event) {
    	encryShiftKey.setDisable(false);
    	encryShiftKey.setPromptText("Enter The "+count+" Shifting value");
    	rules = Integer.parseInt(encryKey.getText());
    	encryKey.setDisable(true);
    }
    @FXML
    public void ShiftingValueAction(ActionEvent event) {
    	if(count<=rules) {
    		encryShiftKey.setPromptText("Enter The "+(++count)+" Shifting value");
    		shift.add(Integer.parseInt(encryShiftKey.getText()));
    	}
    	encryShiftKey.clear();
    	encryKey.clear();
    	encryKey.setDisable(false);
    	System.out.println(shift);
    }
    @FXML
    private void dragged(MouseEvent event) {
    	Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		stage.setX(event.getScreenX()-x);
		stage.setY(event.getScreenY()-y);
    }
    @FXML
    private void pressed(MouseEvent event) {
    	x = event.getSceneX();
		y = event.getSceneY();
    }
    
    public String readFromFile(File name){
		String readed = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(name));
			String line = null;
			StringBuilder strb = new StringBuilder();
			while((line = reader.readLine()) != null){
				strb.append(line);
			}
			readed = strb.toString();
			reader.close();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "The file "+name.getName()+" could not be found","Open File",JOptionPane.ERROR_MESSAGE);
		}
		return readed;
	}
	public void writeInToFile(String text,File name){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(name));
			writer.write(text);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
