package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;
import org.jgrapht.*;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Model {

	private ExtFlightDelaysDAO dao;
    private Graph<Airport, DefaultWeightedEdge> grafo;
    Map<Integer, Airport> airportIdMap;
    
    
	public Model() {
		super();
		this.dao = new ExtFlightDelaysDAO();
		this.grafo = new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.airportIdMap = new HashMap<Integer, Airport>();
	}

	public void popolateAirportIdMap() {
		for (Airport airport : this.dao.loadAllAirports()) {
			airportIdMap.put(airport.getId(), airport);
		}
	}
	
//	public List<ReducedFlight> loadReducedFlights(Double minimaDistanza){
//		return dao.loadReducedFlights(minimaDistanza);
//	}
	
	public void creaGrafo(Double minimaDistanza) {
				
		this.popolateAirportIdMap();

		Graphs.addAllVertices(this.grafo, this.airportIdMap.values());
		
		List<ReducedFlight> reducedFlights = dao.loadReducedFlights();
		
		for (ReducedFlight rf : reducedFlights) {
			
			boolean addable = false;
			Integer idOne = rf.getOriginAirportId();
			Integer idTwo = rf.getDestinationAirportId();
			Double avgDistance = rf.getAvgDistance();
			
			if (rf.getAvgDistance() > minimaDistanza) { //Verifico che l'arco sia valido nel verso A --> B
				addable = true;
			}

			for (ReducedFlight rfl : reducedFlights) {
				if (rfl.getOriginAirportId() == idTwo && rfl.getDestinationAirportId() == idOne) {
					if (rf.getAvgDistance() < minimaDistanza) {
						addable = false; //Verifico che l'arco sia valido nel verso inverso B --> A
					}
					else {
						avgDistance = (avgDistance + rfl.getAvgDistance())/2;
						//Modifico la distanza nel media nel caso in cui i valori di distanza nei due versi siano
						//entrambi sopra la soglia richiesta, ma diversi fra loro.
					}
				}
			} 
			
			if (addable) {
				Airport firstVertex =this.airportIdMap.get(idOne);
				Airport secondVertex = this.airportIdMap.get(idTwo);
				this.grafo.addEdge(firstVertex, secondVertex);
				grafo.setEdgeWeight(firstVertex, secondVertex, avgDistance);
			}
			
		}
		
		System.out.println("Graph successfully created.");
		System.out.println("Number of vertexes: " + this.grafo.vertexSet().size());
		System.out.println("Number of edges: " + this.grafo.edgeSet().size());
		System.out.println("All edges:");
		for (DefaultWeightedEdge dfe : this.grafo.edgeSet()) {
			System.out.println(grafo.getEdgeSource(dfe) + " | " + grafo.getEdgeTarget(dfe) + " | " + grafo.getEdgeWeight(dfe));
		}

	}

	public Graph<Airport, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	
}
