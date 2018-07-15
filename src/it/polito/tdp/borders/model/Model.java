package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;
// UPDATED TO 04/06
public class Model {
	
	private Graph<Country, DefaultEdge> graph ;
	private List<Country> countries ;
	private Map<Integer,Country> countriesMap ;
	
	public Model() {
			}
	
	public void creaGrafo(int anno) {
		
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;

		
		BordersDAO dao = new BordersDAO() ;
		
		//vertici
		this.countries = dao.getCountriesFromYear(anno) ;
		this.countriesMap = new HashMap<>() ;
		for(Country c: this.countries) {
			this.countriesMap.put(c.getcCode(), c) ;
		}
		
		Graphs.addAllVertices(graph, this.countries) ;
		
		// archi
		List<CoppiaNoStati> archi = dao.getCoppieAdiacenti(anno) ;
		for( CoppiaNoStati c: archi) {
			
			graph.addEdge(this.countriesMap.get(c.getState1no()), 
					this.countriesMap.get(c.getState2no())) ;
			
		}
		
//		System.out.format("Grafo creato con %d vertici e %d archi\n", 
//				this.graph.vertexSet().size(),
//				this.graph.edgeSet().size()) ;
		
	}
	
	public List<CountryAndNumber> getCountryAndNumber() {
		List<CountryAndNumber> list = new ArrayList<>() ;
		
		for(Country c: graph.vertexSet()) {
			list.add(new CountryAndNumber(c, graph.degreeOf(c))) ;
		}
		Collections.sort(list);
		return list ;
	}

}
