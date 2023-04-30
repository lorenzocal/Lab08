/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="distanzaMinima"
    private TextField distanzaMinima; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	try {
    		Double distanzaMinima = Double.parseDouble(this.distanzaMinima.getText());
    		this.model.creaGrafo(distanzaMinima);
    		this.txtResult.appendText("Graph Succesfully created\n");
    		this.txtResult.appendText("Number of vertexes: " + this.model.getGrafo().vertexSet().size() + "\n");
    		this.txtResult.appendText("Number of edges: " + this.model.getGrafo().edgeSet().size() + "\n");
    		System.out.println("All edges:\n");
    		for (DefaultWeightedEdge dfe : this.model.getGrafo().edgeSet()) {
    			this.txtResult.appendText(this.model.getGrafo().getEdgeSource(dfe) + " | " + this.model.getGrafo().getEdgeTarget(dfe) + " | " + this.model.getGrafo().getEdgeWeight(dfe) + "\n");
    		}
    	} catch (NullPointerException npe) {
    		this.txtResult.appendText("Inserire un valore nel campo 'Distanza minima'\n");
    	}
    	catch (NumberFormatException nfe) {
    		this.txtResult.appendText("Inserire un valore valido nel campo 'Distanza minima'\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
