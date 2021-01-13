package movida.bennatideluise;

import java.io.File;
import movida.commons.*;

public class demo extends MovidaCore{

	public static void main(String[] args) {
		File f = new File("src/movida/bennatideluise/esempio-formato-dati.txt");
		MovidaCore core = new MovidaCore();
		Grafo grafo = core.new Grafo();
		
		//WORKING
		/*
		-- Array Ordinato, BubbleSort --
		core.loadFromFile(f);
		System.out.println("countMovies() => " + core.countMovies());
		System.out.println("countPeople() => " + core.countPeople());
		System.out.println("deleteMovieByTitle() => " + core.deleteMovieByTitle("Cape Fear") + " " + core.getMovieByTitle("Cape Fear").getTitle());
		System.out.println("getMovieByTitle() => " + core.getMovieByTitle("Pulp Fiction").getTitle());
		System.out.println("getPersonByName() => " + core.getPersonByName("Jodie Foster").getName());
		Movie[] all = core.getAllMovies();
		System.out.println("getAllMovies() => ");
		for (Movie i : all) {
			System.out.println(i.getTitle() + "\n");
		}
		Person[] tutti = core.getAllPeople();
		System.out.println("getAllPeople() => ");
		for (Person i : tutti) {
			System.out.println(i.getName() + "\n");
		}
		System.out.println("searchMoviesByTitle() => " + core.searchMoviesByTitle("Pulp")[0].getTitle());
		System.out.println("searchMoviesInYear() => " + core.searchMoviesInYear(1988)[0].getTitle());
		System.out.println("searchMoviesDirectedBy() => " + core.searchMoviesDirectedBy("Brian De Palma")[0].getTitle());
		System.out.println("searchMoviesStarredBy() => " + core.searchMoviesStarredBy("John Travolta")[0].getTitle());
		System.out.println("searchMostVotedMovies() => " + core.searchMostVotedMovies(2)[0].getTitle() + " "
				+ core.searchMostVotedMovies(2)[1].getTitle());
		System.out.println("searchMostRecentMovies() => " + core.searchMostRecentMovies(2)[0].getTitle() + " "
				+ core.searchMostRecentMovies(2)[1].getTitle());
		System.out.println("countActors() => " + core.countActors());
		Person[] all1 = core.getAllActors();
		System.out.println("getAllActors() => ");
		for (Person i : all1) {
			System.out.println(i.getName());
		}
		System.out.println("searchMostActiveActors() => " + core.searchMostActiveActors(2)[0].getName() + " " + 
				core.searchMostActiveActors(2)[1].getName());
		core.saveToFile(f);
		core.clear();
		System.out.println(core.getAllMovies()[0].getTitle()); 
		
		--Array Ordinato, QuickSort--
		core.setSort(SortingAlgorithm.QuickSort);
		core.loadFromFile(f);
		System.out.println("countMovies() => " + core.countMovies());
		System.out.println("countPeople() => " + core.countPeople());
		System.out.println("deleteMovieByTitle() => " + core.deleteMovieByTitle("Cape Fear"));
		System.out.println("getMovieByTitle() => " + core.getMovieByTitle("Taxi Driver").getTitle());
		System.out.println("getPersonByName() => " + core.getPersonByName("Robert De Niro").getName());
		Movie[] all = core.getAllMovies();
		System.out.println("getAllMovies() => ");
		for (Movie i : all) {
			System.out.println(i.getTitle() + "\n");
		}
		Person[] tutti = core.getAllPeople();
		System.out.println("getAllPeople() => ");
		for (Person i : tutti) {
			System.out.println(i.getName() + "\n");
		}
		System.out.println("searchMoviesByTitle() => " + core.searchMoviesByTitle("Taxi")[0].getTitle());
		System.out.println("searchMoviesInYear() => " + core.searchMoviesInYear(1997)[0].getTitle());
		System.out.println("searchMoviesDirectedBy() => " + core.searchMoviesDirectedBy("Robert Zemeckis")[0].getTitle());
		System.out.println("searchMoviesStarredBy() => " + core.searchMoviesStarredBy("Robert De Niro")[0].getTitle());
		System.out.println("searchMostVotedMovies() => " + core.searchMostVotedMovies(2)[0].getTitle() + " "
				+ core.searchMostVotedMovies(2)[1].getTitle());
		System.out.println("searchMostRecentMovies() => " + core.searchMostRecentMovies(2)[0].getTitle() + " "
				+ core.searchMostRecentMovies(2)[1].getTitle());
		System.out.println("countActors() => " + core.countActors());
		Person[] all1 = core.getAllActors();
		System.out.println("getAllActors() => ");
		for (Person i : all1) {
			System.out.println(i.getName());
		}
		System.out.println("searchMostActiveActors() => " + core.searchMostActiveActors(2)[0].getName() + " " + 
				core.searchMostActiveActors(2)[1].getName());
		core.saveToFile(f);
		core.clear();
		System.out.println(core.getAllMovies()[0].getTitle());
		--Hash Concatenamento, BubbleSort--
		core.setMap(MapImplementation.HashConcatenamento);
		core.loadFromFile(f);
		System.out.println("countMovies() => " + core.countMovies());
		System.out.println("countPeople() => " + core.countPeople());
		System.out.println("deleteMovieByTitle() => " + core.deleteMovieByTitle("Cape Fear"));
		System.out.println("getMovieByTitle() => " + core.getMovieByTitle("Taxi Driver").getTitle());
		System.out.println("getPersonByName() => " + core.getPersonByName("Robert De Niro").getName());
		Movie[] all = core.getAllMovies();
		System.out.println("getAllMovies() => ");
		for (Movie i : all) {
			System.out.println(i.getTitle() + "\n");
		}
		Person[] tutti = core.getAllPeople();
		System.out.println("getAllPeople() => ");
		for (Person i : tutti) {
			System.out.println(i.getName() + "\n");
		}
		System.out.println("searchMoviesByTitle() => " + core.searchMoviesByTitle("Taxi")[0].getTitle());
		System.out.println("searchMoviesInYear() => " + core.searchMoviesInYear(1997)[0].getTitle());
		System.out.println("searchMoviesDirectedBy() => " + core.searchMoviesDirectedBy("Robert Zemeckis")[0].getTitle());
		System.out.println("searchMoviesStarredBy() => " + core.searchMoviesStarredBy("Robert De Niro")[0].getTitle());
		System.out.println("searchMostVotedMovies() => " + core.searchMostVotedMovies(2)[0].getTitle() + " "
				+ core.searchMostVotedMovies(2)[1].getTitle());
		System.out.println("searchMostRecentMovies() => " + core.searchMostRecentMovies(2)[0].getTitle() + " "
				+ core.searchMostRecentMovies(2)[1].getTitle());
		System.out.println("countActors() => " + core.countActors());
		Person[] all1 = core.getAllActors();
		System.out.println("getAllActors() => ");
		for (Person i : all1) {
			System.out.println(i.getName());
		}
		System.out.println("searchMostActiveActors() => " + core.searchMostActiveActors(2)[0].getName() + " " + 
				core.searchMostActiveActors(2)[1].getName());
		core.saveToFile(f);
		core.clear();
		System.out.println(core.getAllMovies()[0].getTitle());
		
		--Hash Concatenamento, QuickSort--
		core.setMap(MapImplementation.HashConcatenamento);
		core.setSort(SortingAlgorithm.QuickSort);
		core.loadFromFile(f);
		System.out.println("countMovies() => " + core.countMovies());
		System.out.println("countPeople() => " + core.countPeople());
		System.out.println("deleteMovieByTitle() => " + core.deleteMovieByTitle("Cape Fear") + " " + core.getMovieByTitle("Cape Fear").getTitle());
		System.out.println("getMovieByTitle() => " + core.getMovieByTitle("Taxi Driver").getTitle());
		System.out.println("getPersonByName() => " + core.getPersonByName("Robert De Niro").getName());
		Movie[] all = core.getAllMovies();
		System.out.println("getAllMovies() => ");
		for (Movie i : all) {
			System.out.println(i.getTitle() + "\n");
		}
		Person[] tutti = core.getAllPeople();
		System.out.println("getAllPeople() => ");
		for (Person i : tutti) {
			System.out.println(i.getName() + "\n");
		}
		System.out.println("searchMoviesByTitle() => " + core.searchMoviesByTitle("Taxi")[0].getTitle());
		System.out.println("searchMoviesInYear() => " + core.searchMoviesInYear(1997)[0].getTitle());
		System.out.println("searchMoviesDirectedBy() => " + core.searchMoviesDirectedBy("Robert Zemeckis")[0].getTitle());
		System.out.println("searchMoviesStarredBy() => " + core.searchMoviesStarredBy("Robert De Niro")[0].getTitle());
		System.out.println("searchMostVotedMovies() => ");
		Movie[] arr = core.searchMostVotedMovies(4);
		for(Movie i : arr) {
			System.out.println(i.getTitle());
		}
		System.out.println("searchMostRecentMovies() => ");
		Movie[] arr2 = core.searchMostRecentMovies(4);
		for(Movie i : arr2) {
			System.out.println(i.getTitle());
		}
		
		System.out.println("countActors() => " + core.countActors());
		Person[] all1 = core.getAllActors();
		System.out.println("getAllActors() => ");
		for (Person i : all1) {
			System.out.println(i.getName());
		}
		System.out.println("searchMostActiveActors() => " + core.searchMostActiveActors(2)[0].getName() + " " + 
				core.searchMostActiveActors(2)[1].getName());
		core.clear();
		System.out.println(core.getAllMovies()[0].getTitle());
		--Grafi--
		grafo.createGraph();
		grafo.fillGraph();
		Person p = core.getPersonByName("Robert De Niro");
		System.out.println("getCollab() => ");
		Person[] arr = grafo.getDirectCollaboratorsOf(p);
		for (Person i : arr) {
			System.out.println(i.getName());
		}
		Person[] arr2 = grafo.getTeamOf(p);
		System.out.println("getTeamOf() => ");
		for (Person i : arr2) {
			System.out.println(i.getName());
		}
		Collaboration[] c = grafo.maximizeCollaborationsInTheTeamOf(p);
		System.out.println("maximaziCollaborationsInTheTeamOf() => ");
		for (Collaboration i : c) {
			System.out.println(i.getActorA().getName() + "+" + i.getActorB().getName() + "=" + i.getScore());
		*/
		core.loadFromFile(f);
		System.out.println("countMovies() => " + core.countMovies());
		System.out.println("countPeople() => " + core.countPeople());
		System.out.println("deleteMovieByTitle() => " + core.deleteMovieByTitle("Cape Fear") + " " + core.getMovieByTitle("Cape Fear").getTitle());
		System.out.println("getMovieByTitle() => " + core.getMovieByTitle("Pulp Fiction").getTitle());
		System.out.println("getPersonByName() => " + core.getPersonByName("Jodie Foster").getName());
		Movie[] all = core.getAllMovies();
		System.out.println("getAllMovies() => ");
		for (Movie i : all) {
			System.out.println(i.getTitle() + "\n");
		}
		Person[] tutti = core.getAllPeople();
		System.out.println("getAllPeople() => ");
		for (Person i : tutti) {
			System.out.println(i.getName() + "\n");
		}
		System.out.println("searchMoviesByTitle() => " + core.searchMoviesByTitle("Pulp")[0].getTitle());
		System.out.println("searchMoviesInYear() => " + core.searchMoviesInYear(1988)[0].getTitle());
		System.out.println("searchMoviesDirectedBy() => " + core.searchMoviesDirectedBy("Brian De Palma")[0].getTitle());
		System.out.println("searchMoviesStarredBy() => " + core.searchMoviesStarredBy("John Travolta")[0].getTitle());
		System.out.println("searchMostVotedMovies() => " + core.searchMostVotedMovies(2)[0].getTitle() + " "
				+ core.searchMostVotedMovies(2)[1].getTitle());
		System.out.println("searchMostRecentMovies() => " + core.searchMostRecentMovies(2)[0].getTitle() + " "
				+ core.searchMostRecentMovies(2)[1].getTitle());
		System.out.println("countActors() => " + core.countActors());
		Person[] all1 = core.getAllActors();
		System.out.println("getAllActors() => ");
		for (Person i : all1) {
			System.out.println(i.getName());
		}
		System.out.println("searchMostActiveActors() => " + core.searchMostActiveActors(2)[0].getName() + " " +
				core.searchMostActiveActors(2)[1].getName());
		core.saveToFile(f);
		core.clear();
		System.out.println(core.getAllMovies()[0].getTitle());

	}

}