package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private SimpleWeightedGraph<Match,DefaultWeightedEdge>grafo;
	private PremierLeagueDAO dao;
	private Map<Integer,Match>idMap;
	
	public Model() {
		dao= new PremierLeagueDAO();
		idMap=new HashMap<Integer,Match>();
		dao.listAllMatches(idMap);
	}
	
	public void creaGrafo(int mese,int minuti) {
		grafo =new SimpleWeightedGraph<Match,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//vertici
		Graphs.addAllVertices(grafo, dao.getVertici(mese,idMap));
		//archi
		for(Scontro s:dao.scontri(mese,idMap,minuti)) {
			if(grafo.containsVertex(s.getM1())&&grafo.containsVertex(s.getM2())) {
				DefaultWeightedEdge e=grafo.getEdge(s.getM1(),s.getM2());
				if(e==null) {
					Graphs.addEdgeWithVertices(grafo, s.getM1(), s.getM2(), s.getPeso());
				}
			}
		}
	}
	public int getNVertici() {
		if(grafo != null)
		return grafo.vertexSet().size();
		return 0;}
					
		public int getNArchi() {
		if(grafo != null)
		return grafo.edgeSet().size();
		return 0;}



//verifico se esiste un grafo

        public SimpleWeightedGraph<Match,DefaultWeightedEdge>getGrafo(){
         return this.grafo;}

        
        public List<Scontro> maxScontro(){
        	List<Scontro>stampa=new ArrayList<Scontro>();
        	List<Scontro>archi=new ArrayList<Scontro>();
        	for(DefaultWeightedEdge e:grafo.edgeSet())
        		archi.add(new Scontro(grafo.getEdgeSource(e),grafo.getEdgeTarget(e),grafo.getEdgeWeight(e)));
        	Collections.sort(archi);
        	Scontro max=archi.get(0);
        	stampa.add(max);
        	for(Scontro s:archi) {
        		if(s.getPeso()==max.getPeso()&&(s.getM1()!=max.getM1()||s.getM2()!=max.getM2()))
        			stampa.add(s);
        	}
        	return stampa;
        }
}
