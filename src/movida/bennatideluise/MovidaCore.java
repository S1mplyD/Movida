package movida.bennatideluise;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import movida.commons.*;


import java.util.*;

public class MovidaCore implements IMovidaDB, IMovidaSearch, IMovidaConfig, IMovidaCollaborations{
	//oggetto della tabella hash
	public class oggettoHash{
			
			private Movie valore; 
			private int chiave;
			public oggettoHash(Movie valore, int chiave) {
				this.chiave = chiave;
				this.valore = valore;
			}
			//funzione che ritorna il valore della tabella hash
			public Movie getValue() {
				return this.valore;
			}
		}
	//oggetto grafo contenente attore e collaborazioni
	public class GraphObject{
		Person actor;
		ArrayList<Collaboration> collabs;	//array di collaborazioni
		public GraphObject(Person att, ArrayList<Collaboration> col) {
			this.actor = att;
			this.collabs = col;
		}
	}

	static LinkedList<GraphObject> Collab;	//Grafo delle collaborazioni
	public static final int ARR_SIZE = 10;	
	Movie[] filmz = new Movie[ARR_SIZE];	//array di film iniziale
	LinkedList<oggettoHash>[] arrayhash = new LinkedList[ARR_SIZE];	//tabella hash di film
	Movie[] filmz2 = null;	//array di film trimmato
	//false = Bubblesort, true = quicksort
	boolean algo = false;
	//false = arrayOrdinato, true = hashConcatenamento
	boolean map = false;
	
	static MovidaCore core = new MovidaCore();
	//funzione che mette un film nella tabella hash
	public void put_in_hash(Movie value) {
		int index = value.getYear()%ARR_SIZE;	//
		LinkedList<oggettoHash> oggetti = arrayhash[index];	//creo una lista di oggetti hash che contiene l'elemento puntato da index in arrayhash
		if(oggetti == null) {	//la lista di oggetti hash è vuota
            oggetti = new LinkedList<oggettoHash>();	//creo una nuova lista di oggetti hash
            oggettoHash item = new oggettoHash(value, value.getYear());	//creo un nuovo oggetto da inserire nella tabella hash
            oggetti.add(item);	//aggiungo item alla lista 
            arrayhash[index] = oggetti;	//inserisco la lista nella cella di arrayhash puntata da index
            
        }
        else {	//la lista oggetti non è vuota
            for(oggettoHash item : oggetti) { //scorro la lista oggetti
            	boolean FLAG = true;	//
                if(item.chiave == value.getYear() && FLAG == false) {	//controllo se item ha chiave corrispondente a value.getYear() e se FLAG è false
                    //pongo il valore di item uguale al film passato come parametro nella funzione
                	item.valore = new Movie(value.getTitle(), value.getYear(), value.getVotes(), value.getCast(), value.getDirector());
                    FLAG = false;	//
                }
            }
            oggettoHash item = new oggettoHash(value, value.getYear());
            oggetti.add(item);
            
        }
    }
	
	 class Grafo{	//classe che definisce le funzioni di costruzione del grafo
		public void createGraph() {	//funzione che inizializza il grafo
			Collab = new LinkedList<>();
		}
		
		public ArrayList<Movie> checkMoviestogheter(Person A, Person B) {	//funzione che permette di controllare se e in quali film 2 attori
																			//hanno collaborato
			ArrayList<Movie> movies = new ArrayList<>();	//creo un nuovo array di film
			if (map==false) {		//se map è falso uso l'array ordinato
				for (Movie m : filmz2) {	//controllo se nel film m è presente l'attore p
					for (Person p : m.getCast()) {
						if (A.getName().equals(p.getName())) {	//controllo se l'attore A passato come parametro è anche l'attore p
							movies.add(m);	//aggiungo il film m all'array di film
						}
					}
				}
				for (Movie n : movies) {	//controllo se nel film m è presente l'attore p
					for (Person b : n.getCast()) {
						if (!(B.getName().equals(b.getName()))) {	//controllo se l'attore A passato come parametro è anche l'attore p
							movies.remove(n);	//aggiungo il film m all'array di film
						}
					}
				}
			}
			else {
				for(int i = 0; i < arrayhash.length; i++ ){
					if(arrayhash[i] != null) {
						for(int j = 0; j < arrayhash[i].size(); j++) {
							for(int g = 0; g < arrayhash[i].get(j).getValue().getCast().length; g++) {
								if(arrayhash[i].get(j).getValue().getCast()[g].getName().equals(A.getName())) {
									movies.add(arrayhash[i].get(j).getValue());
								}
							}
						}
					}
				}
				for(int i = 0; i < arrayhash.length; i++ ){
					if(arrayhash[i] != null) {
						for(int j = 0; j < arrayhash[i].size(); j++) {
							for(int g = 0; g < arrayhash[i].get(j).getValue().getCast().length; g++) {
								if(!(arrayhash[i].get(j).getValue().getCast()[g].getName().equals(B.getName()))) {
									movies.remove(arrayhash[i].get(j).getValue());
								}
							}
						}
					}
				}
			}
			return movies;
		}
		
		public Collaboration createCollab(Person A, Person B) {
			ArrayList<Movie> moviez = checkMoviestogheter(A, B); 
			Collaboration cll = new Collaboration(A,B);
			for (Movie m : moviez) {
				cll.movies.add(m);
			}
			return cll;
		}
		
		public void fillGraph() {
			 Person[] attori= core.getAllActors();
			 if(map == false) {
				 for (Movie m : filmz2) {
					 for(Person p : m.getCast()) {
						 ArrayList<Collaboration> collabbe = new ArrayList<>();
						 for (int i = 0; i < m.getCast().length; i++) {
							 if (!(p.getName().equals(m.getCast()[i].getName()))) {
								 Collaboration coll = createCollab(p, m.getCast()[i]);
								 collabbe.add(coll);
								 
							 }
						 }
						 GraphObject ogg = new GraphObject(p,  collabbe);
						 Collab.add(ogg);
					 }
				 }
				 
			 }
			 else {
				 for(int i = 0; i < arrayhash.length; i++ ){
						if(arrayhash[i] != null) {
							for(int j = 0; j < arrayhash[i].size(); j++) {
								for(int g = 0; g < arrayhash[i].get(j).getValue().getCast().length; g++) {
									ArrayList<Collaboration> collabbe = new ArrayList<>();
									for(int k = 0; k < arrayhash[i].get(j).getValue().getCast().length; k++) {
										if(!arrayhash[i].get(j).getValue().getCast()[g].getName().equals(arrayhash[i].get(j).getValue().getCast()[k].getName())) {
											Collaboration coll = createCollab(arrayhash[i].get(j).getValue().getCast()[g], arrayhash[i].get(j).getValue().getCast()[k]);
											 collabbe.add(coll);
										}
									}
									GraphObject ogg = new GraphObject(arrayhash[i].get(j).getValue().getCast()[g], collabbe);
									 Collab.add(ogg);
								}
							}
						}
					}
			 }
			 removeDoubles(); 
			 
		 }
		
		public void removeDoubles() {
			for(int i = 0 ; i < Collab.size(); i++) {
				if(Collab.get(i) != null) {
					for(int j = 0; j < Collab.get(i).collabs.size(); j++) {
						for(int g = j + 1; g < Collab.get(i).collabs.size(); g++) {
							if(Collab.get(i).collabs.get(j).getActorA().getName() == 
									Collab.get(i).collabs.get(g).getActorA().getName() &&
									Collab.get(i).collabs.get(j).getActorB().getName() == 
									Collab.get(i).collabs.get(g).getActorB().getName()
									|| 
									Collab.get(i).collabs.get(j).getActorA().getName() == 
									Collab.get(i).collabs.get(g).getActorB().getName() &&
									Collab.get(i).collabs.get(j).getActorB().getName() == 
									Collab.get(i).collabs.get(g).getActorA().getName()) {
								Collab.get(i).collabs.remove(g);
							}
						}
					}
				}
			}
		}
		
		
	}
	 public Person[] getCollab(Person act) {	
			Person[] collt = new Person[100];
			for(int i = 0; i < Collab.size(); i++) {
				
				if(Collab.get(i) != null) {
					if(Collab.get(i).actor.getName().equals(act.getName())) {
						for(int g = 0; g < Collab.get(i).collabs.size();g++){
							collt[g] = Collab.get(i).collabs.get(g).getActorB();
						}
					}
				}
			}
			int arr = trimArray(collt);
			Person[] coll = new Person[arr];
			for(int i = 0; i < arr; i ++) {
				coll[i] = collt[i];
			}
			return coll;
		}
	 
	 @Override
		public Person[] getDirectCollaboratorsOf(Person actor) {
		 	Person[] directColl = getCollab(actor);
			return directColl;
		}



		@Override
		public Person[] getTeamOf(Person actor) {
			ArrayList<Person> teamt = new ArrayList<>();
			Person[] directColl = getCollab(actor);
			teamt.add(actor);
			for(int i = 0; i < directColl.length; i++) {
				teamt.add(directColl[i]);
			}
			for(int i = 1; i < directColl.length; i ++) {
				Person[] tmp = getCollab(directColl[i]);
				for(int j = 0; j < tmp.length; j++) {
					teamt.add(tmp[j]);
				}
			}
			for(int i = 0; i < teamt.size(); i++) {
				for(int j = i + 1; j < teamt.size(); j++) {
					if(teamt.get(i).getName().equals(teamt.get(j).getName())){
						teamt.remove(j);
					}
				}
				
			}
			
			Person[] team = new Person[teamt.size()];
			for(int i = 0; i < teamt.size(); i ++) {
				team[i] = teamt.get(i);
			}
			return team;
		}	
		
		@Override
		public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
			Person[] team = getTeamOf(actor);
			Person[] directColl = getDirectCollaboratorsOf(actor);
			Collaboration[] icc = new Collaboration[team.length - directColl.length + 1];
			
			double score = 0;
			
			for(int i = 0; i < directColl.length; i++) {
				for(int j = 0; j < Collab.size(); j++) {
					if(Collab.get(i).actor.getName().equals(actor.getName())) {
						double scoreMax = 0;
						icc[0] = Collab.get(i).collabs.get(0);
						scoreMax = Collab.get(i).collabs.get(0).getScore();
						for(int g = 1 ; g < Collab.get(i).collabs.size(); g++) {
							if(Collab.get(i).collabs.get(g).getScore() > scoreMax) {
								scoreMax = Collab.get(i).collabs.get(g).getScore();
								icc[0] = Collab.get(i).collabs.get(g);
							}
						}							
					}
				}
			}
			for(int g = 1; g < icc.length; g++) {
				for(int i = 0; i < team.length; i++) {
					for(int j = 0; j < Collab.size(); j ++) {
						if((Collab.get(j).actor.getName().equals(team[i].getName())) && 
								(Collab.get(j).actor.getName()!= actor.getName())
								&& team[i].getName()!= actor.getName()) {
								for(int z = 0; z < Collab.get(j).collabs.size(); z++ )
								icc[g] = Collab.get(j).collabs.get(z);
							}
						}
					}
				}
			return icc;
			}
			
		
	@Override
	public void loadFromFile(File f) throws MovidaFileException {
		//Creo il film
		try {
			if (map == false) {
				String title = null;
				Integer year = null;
				Person director = null;
				Person[] cast = null;
				Integer votes = null;
				Person[] cast2 = null;
				
				int filmIndex = 0;
				Scanner scanfile = new Scanner(f);
				while(scanfile.hasNextLine()) {
					String line = scanfile.nextLine();
					if(!line.equals("")) {
						if(line.contains("Title: ")) {
							line = line.replaceAll("Title: ", "");
							title = line;
						}
						if(line.contains("Year: ")) {
							line = line.replaceAll("Year: ", "");
							year = Integer.parseInt(line);		
						}
						if(line.contains("Director: ")) {
							line = line.replaceAll("Director: ", "");
							director = new Person(line);		
						}
						
						if(line.contains("Cast: ")) {
							line = line.replaceAll("Cast: ", "");
							String[] linearr = null;
							linearr = line.split(", ");
							cast = new Person[20];
							for(int i = 0; i < linearr.length; i++) {
								Person actor = new Person(linearr[i]);
								cast[i] = actor; 
							}
							Integer arr = trimArray(cast);
							cast2 = new Person[arr];
							int index = 0;
							for(Person i : cast) {
								if(i != null) {
									cast2[index] = i;
									index++;
								}
							}
						}
						if(line.contains("Votes: ")) {
							line = line.replaceAll("Votes: ", "");
							votes = Integer.parseInt(line);		
						}
						
					}
					else {
						Movie film = new Movie(title, year,  votes, cast2, director);
						filmz[filmIndex] = film;
						filmIndex ++;	
					}
					
				}
				Movie film = new Movie(title, year,  votes, cast2, director);
				filmz[filmIndex] = film;
			}
			else if (map == true) {
				String title = null;
				Integer year = null;
				Person director = null;
				Person[] cast = null;
				Integer votes = null;
				Person[] cast2 = null;
				Scanner scanfile = new Scanner(f);
				for (int i = 0; i < ARR_SIZE; i++) {
					arrayhash[i] = null;
				}
				while(scanfile.hasNextLine()) {
					String line = scanfile.nextLine();
					if(!line.equals("")) {
						if(line.contains("Title: ")) {
							line = line.replaceAll("Title: ", "");
							title = line;
						}
						if(line.contains("Year: ")) {
							line = line.replaceAll("Year: ", "");
							year = Integer.parseInt(line);		
						}
						if(line.contains("Director: ")) {
							line = line.replaceAll("Director: ", "");
							director = new Person(line);		
						}
						
						if(line.contains("Cast: ")) {
							line = line.replaceAll("Cast: ", "");
							String[] linearr = null;
							linearr = line.split(", ");
							cast = new Person[20];
							for(int i = 0; i < linearr.length; i++) {
								Person actor = new Person(linearr[i]);
								cast[i] = actor; 
							}
							Integer arr = trimArray(cast);
							cast2 = new Person[arr];
							int index = 0;
							for(Person i : cast) {
								if(i != null) {
									cast2[index] = i;
									index++;
								}
							}
						}
						if(line.contains("Votes: ")) {
							line = line.replaceAll("Votes: ", "");
							votes = Integer.parseInt(line);		
						}
					}
					else {
						Movie film = new Movie(title, year,  votes, cast2, director);
						put_in_hash(film);
					}
				}
				Movie film = new Movie(title, year,  votes, cast2, director);
				put_in_hash(film);
			}
		}
		catch (Exception e) {
			e.getMessage();
		}
			
		
	}
	
	@Override
	public void saveToFile(File f) throws MovidaFileException {
		// TODO Auto-generated method stub
		try {
			if(map == false) {
				FileWriter writer = new FileWriter(f);
				Integer arr = trimArrayFilmz(filmz);
				filmz2 = new Movie[arr];
				int index = 0;
				for(Movie i : filmz) {
					if(i != null) {
						filmz2[index] = i;
						index++;
					}
				}
				for(int i = 0; i < filmz2.length ; i++) {
					if(i >= 1) {
						writer.write("\n" + "\n");
					}
					writer.write("Title: " + filmz2[i].getTitle().toString() + "\n");
					writer.write("Year: " + filmz2[i].getYear().toString() + "\n");
					writer.write("Director: " + filmz2[i].getDirector().getName().toString() + "\n");
					writer.write("Cast: ");
					for(int j = 0; j < filmz2[i].getCast().length; j++) {
						if(filmz2[i].getCast().length - 1== j) {
							writer.write(filmz2[i].getCast()[j].getName().toString());
						}
						else {
							writer.write(filmz2[i].getCast()[j].getName().toString()  + ", ");
						}
						
					}
					writer.write("\n");
					
					writer.write("Votes: " + filmz2[i].getVotes().toString());
				}
				writer.close();
			}
			else {
				FileWriter writer = new FileWriter(f);
				int contFilm = 0;
				int totalFilm = countMovies();
				boolean currentFilm = false;
				for(int i = 0; i < arrayhash.length; i++) {
					
					if(arrayhash[i] != null) {
						if(currentFilm != false && contFilm < totalFilm) {
							writer.write("\n" + "\n");
							currentFilm = false;
						}
						for(int j = 0; j < arrayhash[i].size(); j++) {
								
								contFilm++;
								currentFilm = true;
								writer.write("Title: " + arrayhash[i].get(j).getValue().getTitle() + "\n");
								writer.write("Year: " + arrayhash[i].get(j).getValue().getYear().toString() + "\n");
								writer.write("Director: " + arrayhash[i].get(j).getValue().getDirector().getName().toString() + "\n");
								writer.write("Cast: ");
								for(int g = 0; g < arrayhash[i].get(j).getValue().getCast().length; g++) {
									if(arrayhash[i].get(j).getValue().getCast().length - 1== g) {
										writer.write(arrayhash[i].get(j).getValue().getCast()[g].getName().toString());
									}
									else {
										writer.write(arrayhash[i].get(j).getValue().getCast()[g].getName().toString()  + ", ");
									}	
								}
								writer.write("\n");	
								writer.write("Votes: " + arrayhash[i].get(j).getValue().getVotes().toString());
								if(arrayhash[i].size() - j > 1) {
									writer.write("\n" + "\n");
								}
							}
						}
					
					}
				writer.close();
				}
		}
		catch (Exception e){
			e.getMessage();
		}
		
	}
	
	public Integer trimArray(Person[] cast) {
		int count = 0;
		for(Person i : cast) {
			if(i != null) {
				count ++;
			}
		}
		return count;
	}
	
	public Integer trimArrayFilmz(Movie[] filmz) {
		int count = 0;
		for(Movie i : filmz) {
			if(i != null) {
				count ++;
			}
		}
		return count;
	}
	
	@Override
	public void clear() {
		if (map == false){
			Movie[] clear = new Movie[0];
			filmz = clear;//new Movie(null, null, null, null, null);
			filmz2 = clear;//new Movie(null, null, null, null, null);
		}
		else {
			LinkedList<oggettoHash>[] clear = new LinkedList[0];
			arrayhash = clear;
		}
		
	}
	
	@Override
	public int countMovies() {
		if(map == false) {
			int cont = 0;
			for(int i = 0; i < filmz2.length; i++) {
				if(filmz2[i].getTitle() != null) {
					cont++;
				}
			}
			return cont;
		}
		else {
			int cont = 0;
			for(int i = 0; i < arrayhash.length; i++) {
				if(arrayhash[i] != null) {
					for(int j = 0; j < arrayhash[i].size(); j++) {
						cont ++;
					}
				}
				
			}
			return cont;
		}
		
	}
	
	@Override
	public int countPeople() {
		if (map == false) {
			Boolean Flag = false;
			List<String> persone = new ArrayList<>();
			
			for(int i = 0; i < filmz2.length; i++) {
				persone.add(filmz2[i].getDirector().getName());
				for(int j = 0; j < filmz2[i].getCast().length; j++) {
					persone.add(filmz2[i].getCast()[j].getName());
				}
			}
			int cont = 0;
			
			for(int i = 0; i < persone.size(); i++) {
				
				for(int j = i + 1; j < persone.size(); j++) {
					if(persone.get(i).equals(persone.get(j))) {
						Flag = true;
						break;
					}
					else {
						Flag = false;
					}
				}
					
				if(Flag == false) {
					cont ++;
				}
				
			}
			return cont;
		}
		else {
			Boolean Flag = false;
			List<String> persone = new ArrayList<>();
			
			for (int i = 0; i<arrayhash.length; i++) {
				if (arrayhash[i] != null) {
					for (int j = 0; j<arrayhash[i].size(); j++) {
						persone.add(arrayhash[i].get(j).getValue().getDirector().getName());
						for (int h = 0; h < arrayhash[i].get(j).getValue().getCast().length; h++)
							persone.add(arrayhash[i].get(j).getValue().getCast()[h].getName());
					}
				}
			}
			int cont = 0;
			for(int i = 0; i < persone.size(); i++) {
				
					for(int j = i + 1; j < persone.size(); j++) {
						if(persone.get(i).equals(persone.get(j))) {
							Flag = true;
							break;
					}
					else {
							Flag = false;
					}
				}
					
				if(Flag == false) {
					cont ++;
				}
				
			}
			return cont;
		}
		
	}
	
	@Override
	public boolean deleteMovieByTitle(String title) {
		if (map == false) {
			Boolean Flag = false;
			List<Movie> filmzcambiato = new ArrayList<>();
			
			int cont = 0;
			for(int i = 0; i < filmz2.length; i++) {
				filmzcambiato.add(filmz2[i]);
			}
			for(int i = 0; i < filmzcambiato.size(); i++) {
				if(filmzcambiato.get(i).getTitle().equals(title)){
					filmzcambiato.remove(i);
					cont ++;
					Flag = true;
					}
				}
			filmz2 = new Movie[filmz2.length - cont];
			for(int i = 0; i < filmz2.length; i++) {
				filmz2[i] = filmzcambiato.get(i);
			}
			if(Flag == false) {
				return false;
			}
			return true;
		}
		else {
			Boolean Flag = false;			
			for (int i = 0; i < arrayhash.length; i++) {
				if (arrayhash[i] != null) {
					for (int j = 0; j< arrayhash[i].size(); j++) {						
						if (arrayhash[i].get(j).getValue().getTitle().equals(title)) {
							arrayhash[i].remove(j);														
							Flag = true;							
						}						
					}
 				}				
			}			
			if(Flag == false) {
				return false;
			}
			return true;
			
		}
		
	}
	
	@Override
	public Movie getMovieByTitle(String title) {
		
		if(map == false) {
			int index = -1;
			for(int i = 0; i < filmz2.length; i++) {
				if(filmz2[i].getTitle().equals(title)) {
					index = i;
				}
			}
			if(index == -1) {
				System.out.println("film non esistente");
				Movie vuoto = new Movie(null,null,null,null,null);
				return vuoto;
			}
			return filmz2[index];
		}
		else {
			int arrayindex = -1, listindex = -1;
			for(int i = 0; i < arrayhash.length; i++) {
				if(arrayhash[i] != null) {
					for(int j = 0; j < arrayhash[i].size(); j++) {
						if(arrayhash[i].get(j).getValue().getTitle().equals(title)) {
							arrayindex = i;
							listindex = j;
						}														
					}
				}
			}
			if(arrayindex == -1 && listindex == -1) {
				System.out.println("film non esistente!");
				Movie vuoto = new Movie(null,null,null,null,null);
				return vuoto;
			}
			return arrayhash[arrayindex].get(listindex).getValue();
		}
		
	}
	
	@Override
	public Person getPersonByName(String name) {
		if(map == false) {
			List<String> persone = new ArrayList<>();
			
			for(int i = 0; i < filmz2.length; i++) {
				persone.add(filmz2[i].getDirector().getName());
				for(int j = 0; j < filmz2[i].getCast().length; j++) {
					persone.add(filmz2[i].getCast()[j].getName());
				}
			}
			Person persona = null;
			for(int i = 0; i < persone.size(); i++) {
				if(persone.get(i).equals(name)) {
					persona = new Person(persone.get(i));
				}
			}
			return persona;
		}
		else {
			boolean Flag = false;
			int indexarray = -1, indexlist = -1, indexcast = -1;
			for (int i = 0; i < arrayhash.length; i++) {
				if(arrayhash[i] != null) {
					for(int j = 0; j < arrayhash[i].size(); j++) {
						if(arrayhash[i].get(j).getValue().getDirector().equals(name)) {
							indexarray = i; 
							indexlist = j;
							Flag = false;
						}
							
						else {
							for(int g = 0; g < arrayhash[i].get(j).getValue().getCast().length; g++) {
								if(arrayhash[i].get(j).getValue().getCast()[g].getName().equals(name)) {
									indexarray = i;
									indexlist = j;
									indexcast = g;
									Flag = true;
								}
									
							}
						}
					}
				}
			}
			if (Flag == false && indexarray != -1 && indexlist != -1){
				return arrayhash[indexarray].get(indexlist).getValue().getDirector();
			}
			else if(Flag == true && indexarray != -1 && indexlist != -1 && indexcast != -1){
				return arrayhash[indexarray].get(indexlist).getValue().getCast()[indexcast];
			}
			else {
				System.out.println("nessuna persona con questo nome!");
				Person vuoto = new Person(null);
				return vuoto;
			}
			
		}
		
	}
	
	@Override
	public Movie[] getAllMovies() {
		if(map == false)
			return filmz2;
		else {
			int moviecounter = 0;
			Movie[] moviearray = new Movie[core.countMovies()];
			for(int i = 0; i < arrayhash.length; i++) {
				if(arrayhash[i]!= null) {
					for(int j = 0; j < arrayhash[i].size(); j++) {
						moviearray[moviecounter] = arrayhash[i].get(j).getValue();
						moviecounter++;
					}
				}
			}
			return moviearray;
		}
	}
	
	@Override
	public Person[] getAllPeople() {
		if(map == false) {
			int index = countPeople();
			Person[] persone = new Person[index];
			List<String> personeNomi = new ArrayList<>();
			
			for(int i = 0; i < filmz2.length; i++) {
				personeNomi.add(filmz2[i].getDirector().getName());
				for(int j = 0; j < filmz2[i].getCast().length; j++) {
					personeNomi.add(filmz2[i].getCast()[j].getName());
				}
			}
			
			for(int i = 0; i < personeNomi.size(); i++) {
				
				for(int j = i + 1; j < personeNomi.size(); j++) {
					if(personeNomi.get(i).equals(personeNomi.get(j))) {
						personeNomi.remove(j);
					}	
				}
			}
			for(int i = 0; i < persone.length; i++) {
				Person persona = new Person(personeNomi.get(i));
				persone[i] = persona;
			}
			return persone;
		}
		else {
			Person[] arraypersone = new Person[core.countPeople()];
			List<String> personeNomi = new ArrayList<>();
			for(int i = 0; i < arrayhash.length; i++) {
				if(arrayhash[i] != null) {
					for(int j = 0; j < arrayhash[i].size(); j++) {
						personeNomi.add(arrayhash[i].get(j).getValue().getDirector().getName());
						for(int g = 0; g < arrayhash[i].get(j).getValue().getCast().length ; g++) {
							personeNomi.add(arrayhash[i].get(j).getValue().getCast()[g].getName());
						}
					}
				}
			}
			for(int i = 0; i < personeNomi.size(); i++) {
				
				for(int j = i + 1; j < personeNomi.size(); j++) {
					if(personeNomi.get(i).equals(personeNomi.get(j))) {
						personeNomi.remove(j);
					}	
				}
			}
			for(int i = 0; i < arraypersone.length; i++) {
				Person persona = new Person(personeNomi.get(i));
				arraypersone[i] = persona;
			}
			return arraypersone;
		}
		
	}

	

	@Override
	public Movie[] searchMoviesByTitle(String title) {
		if(map  == false) {
			List<Movie> filmzsearchati = new ArrayList<>();
			for (Movie i : filmz2) {
				if (i.getTitle().contains(title)) {
					filmzsearchati.add(i);
				}
			}
			int index = 0;
			Movie[] filmzsearchati2 = new Movie[filmzsearchati.size()];
			for (Movie a : filmzsearchati) {
					filmzsearchati2[index] = a;
					index++;
			}
			return filmzsearchati2;
		}
		else {
			List<Movie> filmzsearchati = new ArrayList<>();
			for(int i = 0; i < arrayhash.length; i++) {
				if(arrayhash[i] != null) {
					for(int j = 0; j < arrayhash[i].size(); j++) {
						if(arrayhash[i].get(j).getValue().getTitle().contains(title))
							filmzsearchati.add(arrayhash[i].get(j).getValue());
					}
				}
			}
			int index = 0;
			Movie[] filmzsearchati2 = new Movie[filmzsearchati.size()];
			for (Movie a : filmzsearchati) {
					filmzsearchati2[index] = a;
					index++;
			}
			return filmzsearchati2;
		}		
	}

	@Override
	public Movie[] searchMoviesInYear(Integer year) {
		if(map == false) {
			List<Movie> filmzsearchati = new ArrayList<>();
			for (Movie i : filmz2) {
				if (i.getYear().equals(year)) {
					filmzsearchati.add(i);
				}
			}
			int index = 0;
			Movie[] filmzsearchati2 = new Movie[filmzsearchati.size()];
			for (Movie a : filmzsearchati) {
					filmzsearchati2[index] = a;
					index++;
				}
			return filmzsearchati2;
		}
		else {
			List<Movie> filmzsearchati = new ArrayList<>();
			for(int i = 0; i < arrayhash.length; i++) {
				if(arrayhash[i] != null) {
					for(int j = 0; j < arrayhash[i].size(); j++) {
						if(arrayhash[i].get(j).getValue().getYear().equals(year))
							filmzsearchati.add(arrayhash[i].get(j).getValue());
					}
				}
			}
			int index = 0;
			Movie[] filmzsearchati2 = new Movie[filmzsearchati.size()];
			for (Movie a : filmzsearchati) {
					filmzsearchati2[index] = a;
					index++;
				}
			return filmzsearchati2;
		}	
	}

	@Override
	public Movie[] searchMoviesDirectedBy(String name) {
		if(map == false) {
			List<Movie> filmzsearchati = new ArrayList<>();
			for (Movie i : filmz2) {
				if (i.getDirector().getName().equals(name)) {
					filmzsearchati.add(i);
				}
			}
			Movie[] filmzsearchati2 = new Movie[filmzsearchati.size()];
			int index = 0;
			for (Movie a : filmzsearchati) {
					filmzsearchati2[index] = a;
					index++;
				}
			return filmzsearchati2;
		}
		else {
			List<Movie> filmzsearchati = new ArrayList<>();
			for(int i = 0; i < arrayhash.length; i++) {
				if(arrayhash[i] != null) {
					for(int j = 0; j < arrayhash[i].size(); j++) {
						if(arrayhash[i].get(j).getValue().getDirector().getName().equals(name))
							filmzsearchati.add(arrayhash[i].get(j).getValue());
					}
				}
			}
			Movie[] filmzsearchati2 = new Movie[filmzsearchati.size()];
			int index = 0;
			for (Movie a : filmzsearchati) {
					filmzsearchati2[index] = a;
					index++;
				}
			return filmzsearchati2;
		}
		
	}

	@Override
	public Movie[] searchMoviesStarredBy(String name) {
		if(map == false) {
			List<Movie> filmzsearchati = new ArrayList<>();
			for (Movie i : filmz2) {
				for (Person k : i.getCast()) {
				 if (k.getName().equals(name)) {
					 filmzsearchati.add(i);
				 }
			    }
			}
			Movie[] filmzsearchati2 = new Movie[filmzsearchati.size()];
			int index = 0;
			for (Movie a : filmzsearchati) {
					filmzsearchati2[index] = a;
					index++;
				}
			return filmzsearchati2;	
		}
		else {
			List<Movie> filmzsearchati = new ArrayList<>();
			for(int i = 0; i < arrayhash.length; i++) {
				if(arrayhash[i] != null) {
					for(int j = 0; j < arrayhash[i].size(); j++) {
						for(int g = 0 ; g < arrayhash[i].get(j).getValue().getCast().length; g++) {
							if(arrayhash[i].get(j).getValue().getCast()[g].getName().equals(name))
								filmzsearchati.add(arrayhash[i].get(j).getValue());
						}
						
					}
				}
			}
			Movie[] filmzsearchati2 = new Movie[filmzsearchati.size()];
			int index = 0;
			for (Movie a : filmzsearchati) {
					filmzsearchati2[index] = a;
					index++;
				}
			return filmzsearchati2;	
		}
		
	}

	@Override
	public Movie[] searchMostVotedMovies(Integer N) {
		if(map == false){
			Movie[] filmz3 = filmz2;
			if(algo == false) {	
				for (int i = 1; i < filmz3.length; i++) {
					boolean scambi = false;
					for (int j = 1; j <= filmz3.length - i; j++)
						if (filmz3[j - 1].getVotes() < filmz3[j].getVotes()) {
							Movie tmp = filmz3[j - 1];
						    filmz3[j - 1] = filmz3[j];
							filmz3[j] = tmp;
							scambi = true;
						}
					if (!scambi) break;
				}
				if (N >= filmz3.length) return filmz3;
				else {
					Movie[] searchedMovies = new Movie[N];
					for (int k = 0; k< searchedMovies.length; k++) {
						searchedMovies[k]=filmz3[k]; 
					}
					return searchedMovies;
				}
			}
			else {
				movieQuickSort(filmz3);
				if (N >= filmz3.length) return filmz3;
				else {
					Movie[] searchedMovies = new Movie[N];
					for (int k = 0; k< searchedMovies.length; k++) {
						searchedMovies[k]=filmz3[k]; 
					}
					return searchedMovies;
				}
			
			}
		}
		else {
			Movie[] filmz3 = new Movie[core.countMovies()];
			if(algo == false) {
				int movieindex = 0;
					//inserisco tutti i film nell'array
					for(int i = 0; i < arrayhash.length; i++) {
						if(arrayhash[i] != null) {
							for(int j = 0; j < arrayhash[i].size(); j++) {
								filmz3[movieindex] = arrayhash[i].get(j).getValue();
								movieindex++;
							}
						}
					}
					for (int i = 1; i < filmz3.length; i++) {
						boolean scambi = false;
						for (int j = 1; j <= filmz3.length - i; j++)
							if (filmz3[j - 1].getVotes() < filmz3[j].getVotes()) {
								Movie tmp = filmz3[j - 1];
							    filmz3[j - 1] = filmz3[j];
								filmz3[j] = tmp;
								scambi = true;
							}
						if (!scambi) break;
					}
					if (N >= filmz3.length) return filmz3;
					else {
						Movie[] searchedMovies = new Movie[N];
						for (int k = 0; k< searchedMovies.length; k++) {
							searchedMovies[k]=filmz3[k]; 
						}
						return searchedMovies;
					}
				}
			else {
				int movieindex = 0;
				//inserisco tutti i film nell'array
				for(int i = 0; i < arrayhash.length; i++) {
					if(arrayhash[i] != null) {
						for(int j = 0; j < arrayhash[i].size(); j++) {
							filmz3[movieindex] = arrayhash[i].get(j).getValue();
							movieindex++;
						}
					}
				}
				movieQuickSort(filmz3);
				if (N >= filmz3.length) return filmz3;
				else {
					Movie[] searchedMovies = new Movie[N];
					for (int k = 0; k< searchedMovies.length; k++) {
						searchedMovies[k]=filmz3[k]; 
					}
					return searchedMovies;
				}		
			}	
		}		
	}

	@Override
	public Movie[] searchMostRecentMovies(Integer N) {
		if(map == false) {
			Movie[] filmz3 = filmz2;
			if(algo == false) {
				for (int i = 1; i < filmz3.length; i++) {
					boolean scambi = false;
					for (int j = 1; j <= filmz3.length - i; j++)
						if (filmz3[j - 1].getYear() < filmz3[j].getYear()) {
							Movie tmp = filmz3[j - 1];
						    filmz3[j - 1] = filmz3[j];
							filmz3[j] = tmp;
							scambi = true;
						}
					if (!scambi) break;
				}
				if (N >= filmz3.length) return filmz3;
				else {
					Movie[] searchedMovies = new Movie[N];
					for (int k = 0; k< searchedMovies.length; k++) {
						searchedMovies[k]=filmz3[k]; 
					}
					return searchedMovies;
				}
			}else{
				movieQuickSort(filmz3);
				if (N >= filmz3.length) return filmz3;
				else {
					Movie[] searchedMovies = new Movie[N];
					for (int k = 0; k< searchedMovies.length; k++) {
						searchedMovies[k]=filmz3[k]; 
					}
					return searchedMovies;
				}
			}
		}
		else {
			Movie[] filmz3 = new Movie[core.countMovies()];
			if(algo == false) {
				int movieindex = 0;
					//inserisco tutti i film nell'array
					for(int i = 0; i < arrayhash.length; i++) {
						if(arrayhash[i] != null) {
							for(int j = 0; j < arrayhash[i].size(); j++) {
								filmz3[movieindex] = arrayhash[i].get(j).getValue();
								movieindex++;
							}
						}
					}
					for (int i = 1; i < filmz3.length; i++) {
						boolean scambi = false;
						for (int j = 1; j <= filmz3.length - i; j++)
							if (filmz3[j - 1].getYear() < filmz3[j].getYear()) {
								Movie tmp = filmz3[j - 1];
							    filmz3[j - 1] = filmz3[j];
								filmz3[j] = tmp;
								scambi = true;
							}
						if (!scambi) break;
					}
					if (N >= filmz3.length) return filmz3;
					else {
						Movie[] searchedMovies = new Movie[N];
						for (int k = 0; k< searchedMovies.length; k++) {
							searchedMovies[k]=filmz3[k]; 
						}
						return searchedMovies;
					}
				}
			else {
				int movieindex = 0;
				//inserisco tutti i film nell'array
				for(int i = 0; i < arrayhash.length; i++) {
					if(arrayhash[i] != null) {
						for(int j = 0; j < arrayhash[i].size(); j++) {
							filmz3[movieindex] = arrayhash[i].get(j).getValue();
							movieindex++;
						}
					}
				}
				movieQuickSort(filmz3);
				if (N >= filmz3.length) return filmz3;
				else {
					Movie[] searchedMovies = new Movie[N];
					for (int k = 0; k< searchedMovies.length; k++) {
						searchedMovies[k]=filmz3[k]; 
					}
					return searchedMovies;
				}		
			}
		}
		
		
		
	}

    public int countActors() {
		if(map == false) {
			Boolean Flag = false;
			List<String> persone = new ArrayList<>();			
			for(int i = 0; i < filmz2.length; i++) {
				for(int j = 0; j < filmz2[i].getCast().length; j++) {
					persone.add(filmz2[i].getCast()[j].getName());
				}
			}
			int cont = 0;			
			for(int i = 0; i < persone.size(); i++) {				
				for(int j = i + 1; j < persone.size(); j++) {
					if(persone.get(i).equals(persone.get(j))) {
						Flag = true;
						break;
					}
					else {
						Flag = false;
					}
				}					
				if(Flag == false) {
					cont ++;
				}	
			}
			return cont;
		}
		else {
			Boolean Flag = false;
			List<String> persone = new ArrayList<>();
			for(int i = 0; i < arrayhash.length;i++) {
				if(arrayhash[i]!= null) {
					for(int j = 0; j< arrayhash[i].size();j++) {
						for(int g = 0; g < arrayhash[i].get(j).getValue().getCast().length; g++) {
							persone.add(arrayhash[i].get(j).getValue().getCast()[g].getName());
						}
					}
				}
			}
			int cont = 0;			
			for(int i = 0; i < persone.size(); i++) {
				for(int j = i + 1; j < persone.size(); j++) {
					if(persone.get(i).equals(persone.get(j))) {
						Flag = true;
						break;
					}
					else {
						Flag = false;
					}
				}	
				if(Flag == false) {
					cont ++;
				}
			}
			return cont;
		}
	}
	
	public Person[] getAllActors() {
		if(map == false) {
			int index = countActors();
			Person[] persone = new Person[index];
			List<String> personeNomi = new ArrayList<>();
			
			for(int i = 0; i < filmz2.length; i++) {
				for(int j = 0; j < filmz2[i].getCast().length; j++) {
					personeNomi.add(filmz2[i].getCast()[j].getName());
				}
			}
			
			for(int i = 0; i < personeNomi.size(); i++) {
				
				for(int j = i + 1; j < personeNomi.size(); j++) {
					if(personeNomi.get(i).equals(personeNomi.get(j))) {
						personeNomi.remove(j);
					}	
				}
			}
			for(int i = 0; i < persone.length; i++) {
				Person persona = new Person(personeNomi.get(i));
				persone[i] = persona;
			}
			return persone;
		}
		else {
			int index = countActors();
			Person[] persone = new Person[index];
			List<String> personeNomi = new ArrayList<>();
			for(int i = 0; i < arrayhash.length;i++) {
				if(arrayhash[i]!= null) {
					for(int j = 0; j< arrayhash[i].size();j++) {
						for(int g = 0; g < arrayhash[i].get(j).getValue().getCast().length; g++) {
							personeNomi.add(arrayhash[i].get(j).getValue().getCast()[g].getName());
						}
					}
				}
			}
			for(int i = 0; i < personeNomi.size(); i++) {
				
				for(int j = i + 1; j < personeNomi.size(); j++) {
					if(personeNomi.get(i).equals(personeNomi.get(j))) {
						personeNomi.remove(j);
					}	
				}
			}
			for(int i = 0; i < persone.length; i++) {
				Person persona = new Person(personeNomi.get(i));
				persone[i] = persona;
			}
			return persone;
		}
		
	}
	
	@Override
	public Person[] searchMostActiveActors(Integer N) {
		if(map == false) {
			Person[] attori = getAllActors(); 
			for (Person i : attori) {
				for (Movie j : filmz2) {
					for (Person g : j.getCast()) {
						if (i.getName().equals(g.getName())) i.increaseMovieCount();				
						}
				}
			}
			if(algo == false) {
				for (int index = 1; index <attori.length; index++) {
					boolean scambi = false;
					for (int jdex = 1; jdex <= attori.length - index; jdex++) {
						if (attori[jdex-1].getMovieStarred() < attori[jdex].getMovieStarred()) {
							Person tmp = attori[jdex-1];
							attori[jdex-1] = attori[jdex];
							attori[jdex] = tmp;
							scambi = true;
						}
					}
					if (!scambi) break;
				}
				Person[] attori2 = new Person[N];
				for (int kdex = 0; kdex < attori2.length; kdex++) {
					attori2[kdex]=attori[kdex]; 
				}
				return attori2;
			}
			else {
				actorQuickSort(attori);
				Person[] attori2 = new Person[N];
				for (int kdex = 0; kdex < attori2.length; kdex++) {
					attori2[kdex]=attori[kdex]; 
				}
				return attori2;
			}
		}
		else {
			Person[] attori = getAllActors();
			for(Person g : attori) {
				for(int i = 0; i < arrayhash.length; i++) {
					if(arrayhash[i] != null) {
						for(int j = 0; j < arrayhash[i].size(); j++) {
							for(int h = 0; h < arrayhash[i].get(j).getValue().getCast().length; h++) {
								if(g.getName().equals(arrayhash[i].get(j).getValue().getCast()[h].getName()))
									g.increaseMovieCount();
							}
						}
					}
				}
			}
			if(algo == false) {
				for (int index = 1; index <attori.length; index++) {
					boolean scambi = false;
					for (int jdex = 1; jdex <= attori.length - index; jdex++) {
						if (attori[jdex-1].getMovieStarred() < attori[jdex].getMovieStarred()) {
							Person tmp = attori[jdex-1];
							attori[jdex-1] = attori[jdex];
							attori[jdex] = tmp;
							scambi = true;
						}
					}
					if (!scambi) break;
				}
				Person[] attori2 = new Person[N];
				for (int kdex = 0; kdex < attori2.length; kdex++) {
					attori2[kdex]=attori[kdex]; 
				}
				return attori2;
			}
			else {
				actorQuickSort(attori);
				Person[] attori2 = new Person[N];
				for (int kdex = 0; kdex < attori2.length; kdex++) {
					attori2[kdex]=attori[kdex]; 
				}
				return attori2;
			}
			
		}						
	}
	
	public static void actorQuickSort(Person[] arr) {
		actorQuickSortRec(arr, 0, arr.length - 1);
	}
	
	public static int partitionActor(Person[] arr, int low, int high) 
    { 
        Person pivot = arr[low];  
        int i = (low); // index of smaller element 
        for (int j=low +1; j<= high; j++) 
        { 
            // If current element is smaller than the pivot 
            if (arr[j].getMovieStarred() > pivot.getMovieStarred()) 
            { 
                i++; 
  
                // swap arr[i] and arr[j] 
                Person temp = arr[i]; 
                arr[i] = arr[j]; 
                arr[j] = temp; 
            } 
        } 
  
        // swap arr[i+1] and arr[high] (or pivot) 
        Person temp = arr[i]; 
        arr[i] = arr[low]; 
        arr[low] = temp; 
  
        return i; 
    } 
  
  
    /* The main function that implements QuickSort() 
      arr[] --> Array to be sorted, 
      low  --> Starting index, 
      high  --> Ending index */
    public static void actorQuickSortRec(Person[] arr, int low, int high) 
    { 
        if (low < high) 
        { 
            /* pi is partitioning index, arr[pi] is  
              now at right place */
            int pi = partitionActor(arr, low, high); 
  
            // Recursively sort elements before 
            // partition and after partition 
            actorQuickSortRec(arr, low, pi); 
            actorQuickSortRec(arr, pi+1, high); 
        } 
    }
    
    public static void movieQuickSort(Movie[] arr) {
		movieQuickSortRec(arr, 0, arr.length - 1);
	}
	
	public static int partitionMovie(Movie[] arr, int low, int high) 
    { 
        Movie pivot = arr[low];  
        int i = (low); // index of smaller element 
        for (int j=low +1; j<= high; j++) 
        { 
            // If current element is smaller than the pivot 
            if (arr[j].getYear() > pivot.getYear()) 
            { 
                i++; 
  
                // swap arr[i] and arr[j] 
                Movie temp = arr[i]; 
                arr[i] = arr[j]; 
                arr[j] = temp; 
            } 
        } 
  
        // swap arr[i+1] and arr[high] (or pivot) 
        Movie temp = arr[i]; 
        arr[i] = arr[low]; 
        arr[low] = temp; 
  
        return i; 
    } 
  
  
    /* The main function that implements QuickSort() 
      arr[] --> Array to be sorted, 
      low  --> Starting index, 
      high  --> Ending index */
    public static void movieQuickSortRec(Movie[] arr, int low, int high) 
    { 
        if (low < high) 
        { 
            /* pi is partitioning index, arr[pi] is  
              now at right place */
            int pi = partitionMovie(arr, low, high); 
  
            // Recursively sort elements before 
            // partition and after partition 
            movieQuickSortRec(arr, low, pi); 
            movieQuickSortRec(arr, pi+1, high); 
        } 
    }
	
	@Override
	public boolean setSort(SortingAlgorithm a) {
		if(a == SortingAlgorithm.BubbleSort) {
			if(algo == false)
				return false;
			else {
				algo = false;
				return true;
			}
				
		} 
		else if (a == SortingAlgorithm.QuickSort){
			if(algo == true)
				return false;
			else {
				algo = true;
				return true;
			}
		}
		else
		{
			return false;
		}
		
	}

	@Override
	public boolean setMap(MapImplementation m) {
		if(m == MapImplementation.ArrayOrdinato) {
			if(map == false)
				return false;
			else {
				map = false;
				return true;
			}
				
		} 
		else if (m == MapImplementation.HashConcatenamento){
			if(map == true)
				return false;
			else {
				map = true;
				return true;
			}
		}
		else
		{
			return false;
		
	}
}

}
