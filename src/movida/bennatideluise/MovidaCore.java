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

import movida.bennatideluise.MovidaCore.Grafo;
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
	public Movie[] filmz = new Movie[ARR_SIZE];	//array di film iniziale
	public LinkedList<oggettoHash>[] arrayhash = new LinkedList[ARR_SIZE];	//tabella hash di film
	public Movie[] filmz2 = null;	//array di film trimmato
	//false = Bubblesort, true = quicksort
	public boolean algo = false;
	//false = arrayOrdinato, true = hashConcatenamento
	public boolean map = false;
	
	static MovidaCore core = new MovidaCore();
	//funzione che mette un film nella tabella hash
	public Movie[] getFilmsList() {
 		return this.filmz2;
	}
	
	public void setFilmList(Movie[] films) {
		this.filmz2 = films;
	}
	
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
			boolean Flag = false;
			ArrayList<Movie> movies = new ArrayList<>();	//creo un nuovo array di film
			if (map==false) {		//se map è falso uso l'array ordinato
				for (Movie m : filmz2) {	//controllo se nel film m è presente l'attore p
					for (Person p : m.getCast()) {
						if (A.getName().equals(p.getName())) {	//controllo se l'attore A passato come parametro è anche l'attore p
							movies.add(m);	//aggiungo il film m all'array di film
						}
					}
				}
				ArrayList<Movie> tmp = movies;	//creo un array temporaneo che contiene movies
				for(int i = 0; i < movies.size(); i++) {
					for(int j = 0; j < movies.get(i).getCast().length; j++) {
						if((B.getName().equals(tmp.get(i).getCast()[j].getName()))) {//se il nome dell'attore B è uguale a quello del cast puntato da j
							Flag = true;
						}						
					}
					if(Flag == false) {	//rimuovo il film se l'attore non è presente
						movies.remove(movies.get(i));
					}
				}
				
			}
			else {	//uso la tabella hash
				for(int i = 0; i < arrayhash.length; i++ ){	//scorro la tebella hash finché non trovo un elemento
					if(arrayhash[i] != null) {
						for(int j = 0; j < arrayhash[i].size(); j++) {	//una volta trovato un elemento scorro la lista di quell'elemento
							for(int g = 0; g < arrayhash[i].get(j).getValue().getCast().length; g++) {//scorro gli elementi del cast
								//se l'attore del cast ha lo stesso nome del primo attore passato come parametro allora aggiungo il film a cui
								//ha partecipato quell'attore nell'array di film
								if(arrayhash[i].get(j).getValue().getCast()[g].getName().equals(A.getName())) {
									movies.add(arrayhash[i].get(j).getValue());
								}
							}
						}
					}
				}
				for(int i = 0; i < arrayhash.length; i++ ){	//scorro la tebella hash finché non trovo un elemento
					if(arrayhash[i] != null) {
						for(int j = 0; j < arrayhash[i].size(); j++) {	//una volta trovato un elemento scorro la lista di quell'elemento
							for(int g = 0; g < arrayhash[i].get(j).getValue().getCast().length; g++) { //scorro gli elementi del cast
								//se l'attore del cast non ha lo stesso nome del secondo attore passato come parametro allora rimuovo il film 
								if(!(arrayhash[i].get(j).getValue().getCast()[g].getName().equals(B.getName()))) {
									//facendo così nell'array movies mi rimarranno solo i film a cui hanno partecipato entrambi
									movies.remove(arrayhash[i].get(j).getValue());
								}
							}
						}
					}
				}
			}
			return movies;
		}
		
		public Collaboration createCollab(Person A, Person B) {	//funzione che permette di creare una collaborazione
			ArrayList<Movie> moviez = checkMoviestogheter(A, B); //controlliamo in che film hanno collaborato gli attori
			Collaboration cll = new Collaboration(A,B);	//creo la collaborazione
			for (Movie m : moviez) {	//aggiungo alla lista di film della collaborazione i film a cui hanno partecipato entrambi gli attori
				cll.movies.add(m);
			}
			return cll;
		}
		
		public void fillGraph() {	//funzione che serve ad inserire tutti gli elementi nel grafo
			 Person[] attori= getAllActors();	//metto in un array tutti gli attori
			 if(map == false) {	//se map == false allora uso l'array ordinato
				 for (Movie m : filmz2) {	//scorro l'array dei film
					 for(Person p : m.getCast()) {	//scorro l'array cast
						 ArrayList<Collaboration> collabbe = new ArrayList<>();	//creo un array di collaborazioni
						 for (int i = 0; i < m.getCast().length; i++) {	//scorro il cast
							//se il nome della persona p è diverso da quello del membro del cast attualmente puntato allora creo una collaborazione
							//tra di essi e poi la aggiungo all'array di collaborazioni						 
							 if (!(p.getName().equals(m.getCast()[i].getName()))) {									 
								 Collaboration coll = createCollab(p, m.getCast()[i]);
								 collabbe.add(coll);								 
							 }
						 }
						 GraphObject ogg = new GraphObject(p,  collabbe);	//creo un oggetto grafo 
						 Collab.add(ogg);	//aggiungo al grafo l'oggetto appena creato
					 }
				 }
				 
			 }
			 else {	//uso la tabella hash
				 for(int i = 0; i < arrayhash.length; i++ ){	//scorro la tabella hash finchè non trovo un elemento
						if(arrayhash[i] != null) {
							for(int j = 0; j < arrayhash[i].size(); j++) {	//scorro la lista dell'elemento puntato da i
								for(int g = 0; g < arrayhash[i].get(j).getValue().getCast().length; g++) {	//scorro il cast
									ArrayList<Collaboration> collabbe = new ArrayList<>();	//creo una nuova collaborazione
									for(int k = 0; k < arrayhash[i].get(j).getValue().getCast().length; k++) {	//scorro il cast
										//controllo che la persona puntata da g sia diversa da k, allora creo la collaborazione
										if(!arrayhash[i].get(j).getValue().getCast()[g].getName().equals(arrayhash[i].get(j).getValue().getCast()[k].getName())) {
											Collaboration coll = createCollab(arrayhash[i].get(j).getValue().getCast()[g], arrayhash[i].get(j).getValue().getCast()[k]);
											 collabbe.add(coll);	//aggiungo la collaborazione all'array
										}
									}
									//creo un nuovo elemento del grafo e lo aggiungo al grafo esistente
									GraphObject ogg = new GraphObject(arrayhash[i].get(j).getValue().getCast()[g], collabbe);
									 Collab.add(ogg);
								}
							}
						}
					}
			 }
			 removeDoubles(); 	//funzione che rimuove le collaborazioni doppie uguali (ad esempio se ho una collaborazione tra A e B, 
			 					//è inutile averne una tra B ed A)
			 
		 }
		
		public void removeDoubles() {	//funzione che rimuove le collaborazioni doppie
			for(int i = 0 ; i < Collab.size(); i++) {	//scorro gli elementi del grafo
				if(Collab.get(i) != null) {	//se l'elemento puntato da i non è nullo allora scorro la lista di collaborazioni 
											//dell'elemento puntato da i
					for(int j = 0; j < Collab.get(i).collabs.size(); j++) {
						for(int g = j + 1; g < Collab.get(i).collabs.size(); g++) {	//partendo dall'elemento j + 1 controllo per ogni collaborazione se 
							//se ne presenta una con gli stessi attori
							if(Collab.get(i).collabs.get(j).getActorA().getName() == 
									Collab.get(i).collabs.get(g).getActorA().getName() &&
									Collab.get(i).collabs.get(j).getActorB().getName() == 
									Collab.get(i).collabs.get(g).getActorB().getName()
									|| 
									Collab.get(i).collabs.get(j).getActorA().getName() == 
									Collab.get(i).collabs.get(g).getActorB().getName() &&
									Collab.get(i).collabs.get(j).getActorB().getName() == 
									Collab.get(i).collabs.get(g).getActorA().getName()) {
								Collab.get(i).collabs.remove(g);// se trovo una collaborazione con gli stessi attori allora la rimuovo 
							}
						}
					}
				}
			}
		}
		
		public Person[] getCollab(Person act) {	//funzione che permette di prendere le collaborazioni dirette di un attore

			Person[] collt = new Person[100];//creo un array temporaneo di persone (e se le persone sono più di 100? da aggiornare con arrayList(?))
			int lastindex = 0;
			for(int i = 0; i < Collab.size(); i++) {	//scorro il grafo
			
				if(Collab.get(i) != null) {	//quando trovo un nodo non nullo controllo che l'attore puntato da i sia lo stesso passato come parametro
					if(Collab.get(i).actor.getName().equals(act.getName())) {
						for(int g = 0; g < Collab.get(i).collabs.size();g++){	//scorro la lista delle collaborazioni
							collt[lastindex] = Collab.get(i).collabs.get(g).getActorB();	//aggiungo le persone che hanno collaborato con l'attore 
																					//passato come parametro all'array							
							lastindex ++;
						}						
					}
				}
			}
			int arr = trimArray(collt);
			Person[] coll = new Person[arr];	//creo l'array finale privo di celle inutilizzate
			for(int i = 0; i < arr; i ++) {
				coll[i] = collt[i];
			}
			return coll;
		}
		
		public Person[] getDirectCollaboratorsOf(Person actor) { //funzione che permette di prendere le collaborazioni dirette di un attore
		 	Person[] directColl = getCollab(actor);
			return directColl;
		}

		public Person[] getTeamOf(Person actor) {	//funzione che permette di ritornare un team di attori
			ArrayList<Person> teamt = new ArrayList<>(); //creo un array temporaneo dove mettere i membri del team
			Person[] directColl = getCollab(actor);	//prendo i collaboratiri diretti dell'attore passato come riferimento
			teamt.add(actor);	//aggiungo al team l'attore passato come riferimento
			for(int i = 0; i < directColl.length; i++) {	//aggiungo gli altri attori che hanno collaborato direttamente 
				teamt.add(directColl[i]);
			}
			for(int i = 1; i < directColl.length; i ++) {	//prendo gli attori che hanno collaborato indirettamente
				Person[] tmp = getCollab(directColl[i]);
				for(int j = 0; j < tmp.length; j++) {
					teamt.add(tmp[j]);
				}
			}
			Person[] tmp = new Person[teamt.size()];
			for(int i = 0; i < teamt.size(); i++) {
				tmp[i] = teamt.get(i);
			}
			ArrayList<Integer> index = new ArrayList<>();		
			//rimuovo gli attori presenti più di una volta dall'array temporaneo
			for(int i = 0; i < tmp.length; i++) {	
				for(int j = i + 1; j < tmp.length; j++) {
					if(tmp[i].getName().equals(tmp[j].getName())){
						index.add(j);
					}
				}
			}
			for(int j = 0; j < index.size(); j++) {
				tmp[index.get(j)] = null;
			}
			int ind = trimArray(tmp);		
			//creo l'array da ritornare, che avrà dimensione esatta dei componenti del team
			Person[] team = new Person[ind];
			int jex = 0;
			for(int i = 0; i < tmp.length; i ++) {
				if(tmp[i] != null) {
					team[jex] = tmp[i];
					jex++;
				}
			}
			return team;
		}	
	 
		public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
			Person[] team = getTeamOf(actor);	//prendo il team dell'attore
			Person[] directColl = getDirectCollaboratorsOf(actor); //prendo i suoi collaboratori diretti
			Collaboration[] icc = new Collaboration[team.length - directColl.length + 1];	//creo l'icc
			
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
	  }	 	 	 
		
			
		
	@Override
	public void loadFromFile(File f) throws MovidaFileException {	//funzione per caricare i dati da file
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
				while(scanfile.hasNextLine()) {	//ciclo finché il non finisce
					String line = scanfile.nextLine();
					if(!line.equals("")) {	//controllo quando si presenta una riga vuota (vuol dire che il film è finito)
						if(line.contains("Title: ")) {	//se la linea contiene "Title" allora rimuovo "Title"
							line = line.replaceAll("Title: ", "");
							title = line;
						}
						if(line.contains("Year: ")) {	//se la linea contiene "Year" allora rimuovo "Tear"
							line = line.replaceAll("Year: ", "");
							year = Integer.parseInt(line);		
						}
						if(line.contains("Director: ")) {	//se la linea contiene "Director" allora rimuovo "Title"
							line = line.replaceAll("Director: ", "");
							director = new Person(line);		
						}
						
						if(line.contains("Cast: ")) {	//se la linea contiene "Cast" allora rimuovo "Cast"
							line = line.replaceAll("Cast: ", "");
							String[] linearr = null;
							linearr = line.split(", "); //pongo nell'array gli attori
							cast = new Person[20];
							for(int i = 0; i < linearr.length; i++) {
								Person actor = new Person(linearr[i]); //creo una Person con il nome contenuto in linearr[i]
								cast[i] = actor; //setto in cast gli attori
							}
							Integer arr = trimArray(cast); //tolgo le caselle null dal cast
							cast2 = new Person[arr];
							int index = 0;
							for(Person i : cast) {
								if(i != null) {
									cast2[index] = i;
									index++;
								}
							}
						}
						if(line.contains("Votes: ")) {//se la linea contiene "Votes" allora rimuovo "Votes"
							line = line.replaceAll("Votes: ", "");
							votes = Integer.parseInt(line);	//trasformo la stringa in intero		
						}
						
					}
					else {	//se ho finito di analizzare il film creo un oggeto film con i parametri appena letti da file
						Movie film = new Movie(title, year,  votes, cast2, director);
						this.filmz[filmIndex] = film;	//pongo il film in un array di film
						filmIndex ++;	
					}
					
				}
				Movie film = new Movie(title, year,  votes, cast2, director);
				this.filmz[filmIndex] = film;
				Integer arr = trimArrayFilmz(filmz);	//creo un array di film senza caselle null
				Movie []tmp = new Movie[arr];
				int index = 0;
				for(Movie i : filmz) {
					if(i != null) {
						tmp[index] = i;
						index++;
					}
				}
				setFilmList(tmp);
			}
			else if (map == true) {	//funzione uguale a quella sopra, ma per la tabella hash
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
	public void saveToFile(File f) throws MovidaFileException {	//funzione per salvare su file
		try {
			if(map == false) {
				FileWriter writer = new FileWriter(f);
				Integer arr = trimArrayFilmz(filmz);	//tolgo le caselle null dall'array di film
				filmz2 = new Movie[arr];
				int index = 0;
				for(Movie i : filmz) {
					if(i != null) {
						filmz2[index] = i;
						index++;
					}
				}
				for(int i = 0; i < filmz2.length ; i++) {
					if(i >= 1) {	//mette una riga vuota dopo ogni film
						writer.write("\n" + "\n");
					}
					//scrive su file gli attribuiti di ogni film
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
			else {	//funzione uguale a quella sopra, questa è per la tabella hash
				FileWriter writer = new FileWriter(f);
				int contFilm = 0;
				int totalFilm = countMovies();
				boolean currentFilm = false;
				for(int i = 0; i < arrayhash.length; i++) {
					
					if(arrayhash[i] != null) {
						if(currentFilm != false && contFilm < totalFilm) {	//mette una riga vuota dopo ogni film
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
	
	public Integer trimArray(Person[] cast) {	//funzione per trimmerare un array di persone
		int count = 0;
		for(Person i : cast) {
			if(i != null) {
				count ++;
			}
		}
		return count;
	}
	
	public Integer trimArrayFilmz(Movie[] filmz) {	//funzione per trimmerare un array di film
		int count = 0;
		for(Movie i : filmz) {
			if(i != null) {
				count ++;
			}
		}
		return count;
	}
	
	@Override
	public void clear() {	//funzione per svuotare l'array di film o la tebella hash contenente i film
		if (map == false){
			Movie[] clear = new Movie[0];
			filmz = clear;
			filmz2 = clear;
		}
		else {
			LinkedList<oggettoHash>[] clear = new LinkedList[0];
			arrayhash = clear;
		}
		
	}
	
	@Override
	public int countMovies() {	//funzione per contare i film
		if(map == false) {
			int cont = 0;
			for(int i = 0; i < filmz2.length; i++) {	//ciclo sui titoli e per ogni titolo incremento un contatore
				if(filmz2[i].getTitle() != null) {
					cont++;
				}
			}
			return cont;	//ritorno il contatore
		}
		else {
			int cont = 0;
			for(int i = 0; i < arrayhash.length; i++) {//ciclo sulla tabella hash e per ogni valore non nullo della tabella hash incremento un contatore
				if(arrayhash[i] != null) {
					for(int j = 0; j < arrayhash[i].size(); j++) {
						cont ++;
					}
				}				
			}
			return cont;	//ritorno il contatore
		}		
	}
	
	@Override
	public int countPeople() {	//funzione per contare le persone (registi + cast)
		if (map == false) {
			Boolean Flag = false;
			List<String> persone = new ArrayList<>();	//lista di persone
			
			for(int i = 0; i < filmz2.length; i++) {	//ciclo sui film
				persone.add(filmz2[i].getDirector().getName());	//aggiungo il regista del film alla lista
				for(int j = 0; j < filmz2[i].getCast().length; j++) {	//ciclo sul cast
					persone.add(filmz2[i].getCast()[j].getName());	//aggiungo gli attori alla lista
				}
			}
			int cont = 0;
			
			for(int i = 0; i < persone.size(); i++) {	//ciclo la lista di persone e non conto i doppioni
				
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
		else {	//funzione medesima a quella sopra, ma per la tabella hash
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
	public boolean deleteMovieByTitle(String title) {	//funzione che elimina un film basandosi sul titolo
		if (map == false) {
			Boolean Flag = false;
			List<Movie> filmzcambiato = new ArrayList<>();
			
			int cont = 0;
			for(int i = 0; i < filmz2.length; i++) {	//inserisco tutti i film in una lista
				filmzcambiato.add(filmz2[i]);
			}
			for(int i = 0; i < filmzcambiato.size(); i++) {	//scorro la lista e cerco il film con titolo uguale al parametro passato
				if(filmzcambiato.get(i).getTitle().equals(title)){
					filmzcambiato.remove(i);
					cont ++;	//incremento il contatore che servirà per ridimensionare l'array di film
					Flag = true;
					}
				}
			filmz2 = new Movie[filmz2.length - cont];	//creo il nuovo array di film
			for(int i = 0; i < filmz2.length; i++) {	//inserisco i film della lista nell'array di film
				filmz2[i] = filmzcambiato.get(i);
			}
			if(Flag == false) {
				return false;
			}
			return true;
		}
		else {	//funzione medesima a quella sopra, ma per la tabella hash
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
	public Movie getMovieByTitle(String title) {	//funzione che ritorna un film basandosi sul titolo passato per parametro
		
		if(map == false) {
			int index = -1;
			for(int i = 0; i < filmz2.length; i++) {	//ciclo l'array di film, se trovo il film che sto cercando pongo index = i
				if(filmz2[i].getTitle().equals(title)) {
					index = i;
				}
			}
			if(index == -1) {	//se alla fine del controllo index è uguale a -1 allora il film non esiste
				System.out.println("film non esistente");
				Movie vuoto = new Movie(null,null,null,null,null);
				return vuoto;
			}
			return filmz2[index];
		}
		else {	//funzione medesima a quella sopra, ma per la tabella hash
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
	public Person getPersonByName(String name) {	//funzione che ritorna una persona in base al nome passato come paramentro
		if(map == false) {
			List<String> persone = new ArrayList<>();
			
			for(int i = 0; i < filmz2.length; i++) {	//ciclo i film e aggiungo ad una lista di persone tutte le persone dei film
				persone.add(filmz2[i].getDirector().getName());
				for(int j = 0; j < filmz2[i].getCast().length; j++) {
					persone.add(filmz2[i].getCast()[j].getName());
				}
			}
			Person persona = null;
			for(int i = 0; i < persone.size(); i++) {	//cerco nella lista la persona che ha come nome il parametro passato alla funzione
				if(persone.get(i).equals(name)) {
					persona = new Person(persone.get(i));
				}
			}
			return persona;
		}
		else {	
			boolean Flag = false;
			int indexarray = -1, indexlist = -1, indexcast = -1;
			for (int i = 0; i < arrayhash.length; i++) {	//ciclo la tabella hash
				if(arrayhash[i] != null) {	//se è presente un elemento ciclo la lista appartenente a quell'elemento
					for(int j = 0; j < arrayhash[i].size(); j++) {
						if(arrayhash[i].get(j).getValue().getDirector().equals(name)) {//se il parametro viene trovato tra i registri mi salvo gli indici
							indexarray = i; 
							indexlist = j;
							Flag = false;
						}
							
						else {//altrimenti cerco la persona tra il cast e mi salvo gli indici
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
			if (Flag == false && indexarray != -1 && indexlist != -1){ //ritorno il regista
				return arrayhash[indexarray].get(indexlist).getValue().getDirector();
			}
			else if(Flag == true && indexarray != -1 && indexlist != -1 && indexcast != -1){//ritorno l'attore
				return arrayhash[indexarray].get(indexlist).getValue().getCast()[indexcast];
			}
			else {//non esiste una persona con quel nome
				System.out.println("nessuna persona con questo nome!");
				Person vuoto = new Person(null);
				return vuoto;
			}
			
		}
		
	}
	
	@Override
	public Movie[] getAllMovies() {	//funzione che ritorna tutti i film
		if(map == false)
			return filmz2;	//ritorno l'array di film
		else {
			int moviecounter = 0;
			Movie[] moviearray = new Movie[countMovies()];
			for(int i = 0; i < arrayhash.length; i++) {	//inserisco tutti i film della tabella hash in un array e lo ritorno
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
	public Person[] getAllPeople() {	//funzione che ritorna tutte le persone
		if(map == false) {
			int index = countPeople();	//conto tutte le persone
			Person[] persone = new Person[index];	//creo un array di persone
			List<String> personeNomi = new ArrayList<>();
			
			for(int i = 0; i < filmz2.length; i++) {	//ciclo tutti i film e aggiungo i nomi delle persone nella liste dei nomi
				personeNomi.add(filmz2[i].getDirector().getName());
				for(int j = 0; j < filmz2[i].getCast().length; j++) {
					personeNomi.add(filmz2[i].getCast()[j].getName());
				}
			}
			
			for(int i = 0; i < personeNomi.size(); i++) {	//elimino i doppioni
				
				for(int j = i + 1; j < personeNomi.size(); j++) {
					if(personeNomi.get(i).equals(personeNomi.get(j))) {
						personeNomi.remove(j);
					}	
				}
			}
			for(int i = 0; i < persone.length; i++) {	//aggiungo le persone all'array di persone
				Person persona = new Person(personeNomi.get(i));
				persone[i] = persona;
			}
			return persone;
		}
		else {//funzione medesima a quella sopra, ma per la tabella hash
			Person[] arraypersone = new Person[countPeople()];
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
	public Movie[] searchMoviesByTitle(String title) {	//funzione che cerca un film che ha il titolo che contiene la stringa passata come parametro
		if(map  == false) {
			List<Movie> filmzsearchati = new ArrayList<>();
			for (Movie i : filmz2) {	//scorro i film e controllo se uno di loro contiene la stringa passata come parametro
				if (i.getTitle().contains(title)) {
					filmzsearchati.add(i);
				}
			}
			int index = 0;
			Movie[] filmzsearchati2 = new Movie[filmzsearchati.size()];	//creo l'array di dimensione di filmzsearchati
			for (Movie a : filmzsearchati) {	//inserisco nell'array i film
					filmzsearchati2[index] = a;
					index++;
			}
			return filmzsearchati2;
		}
		else {//funzione medesima a quella sopra, ma per la tabella hash
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
	public Movie[] searchMoviesInYear(Integer year) {	//funzione che ritorna i film rilasciati in un determinato anno
		if(map == false) {
			List<Movie> filmzsearchati = new ArrayList<>();
			for (Movie i : filmz2) {	//scorro l'array di film
				if (i.getYear().equals(year)) {	//controllo se il film puntato è stato prodotto nell'anno interessato
					filmzsearchati.add(i);	//aggiungo il film ad una lista
				}
			}
			int index = 0;
			Movie[] filmzsearchati2 = new Movie[filmzsearchati.size()];	//creo un array che ha come dimensione la lunghezza della lista
			for (Movie a : filmzsearchati) {	//inserisco i film della lista in un array
					filmzsearchati2[index] = a;
					index++;
				}
			return filmzsearchati2;
		}
		else {//funzione medesima a quella sopra, ma per la tabella hash
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
	public Movie[] searchMoviesDirectedBy(String name) {	//funzione che ritorna i film diretti da un certo regista
		if(map == false) {
			List<Movie> filmzsearchati = new ArrayList<>();	
			for (Movie i : filmz2) {	//scorro la lista dei film e controllo se è stato diretto dal regista interessato
				if (i.getDirector().getName().equals(name)) {
					filmzsearchati.add(i);	//aggiungo i film alla lista
				}
			}
			Movie[] filmzsearchati2 = new Movie[filmzsearchati.size()];	//creo un array con dimensione della lista di film
			int index = 0;
			for (Movie a : filmzsearchati) {	//inserisco nell'array i film presenti nella lista
					filmzsearchati2[index] = a;
					index++;
				}
			return filmzsearchati2;
		}
		else {//funzione medesima a quella sopra, ma per la tabella hash
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
	public Movie[] searchMoviesStarredBy(String name) {	//ritorna i film a cui ha partecipato un certo attore
		if(map == false) {
			List<Movie> filmzsearchati = new ArrayList<>();
			for (Movie i : filmz2) {	//scorro il cast dei vari film e controllo se l'attore interessato è presente.
				for (Person k : i.getCast()) {
				 if (k.getName().equals(name)) {
					 filmzsearchati.add(i);	//aggiungo il film alla lista
				 }
			    }
			}
			Movie[] filmzsearchati2 = new Movie[filmzsearchati.size()];	//creo un array con dimensione della lista di film
			int index = 0;
			for (Movie a : filmzsearchati) {	//inserisco nell'array i film presenti nella lista
					filmzsearchati2[index] = a;
					index++;
				}
			return filmzsearchati2;	
		}
		else {	//funzione medesima a quella sopra, ma per la tabella hash
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
	public Movie[] searchMostVotedMovies(Integer N) {	//funzione che ritorna gli N film con i voti più alti
		if(map == false){
			Movie[] filmz3 = filmz2;	//creo un array di support contenente tutti i film
			if(algo == false) {	//quicksort
				for (int i = 1; i < filmz3.length; i++) {	//scorro i film
					boolean scambi = false;
					for (int j = 1; j <= filmz3.length - i; j++)	//ordino i film utilizzando bubblesort
						if (filmz3[j - 1].getVotes() < filmz3[j].getVotes()) {
							Movie tmp = filmz3[j - 1];
						    filmz3[j - 1] = filmz3[j];
							filmz3[j] = tmp;
							scambi = true;
						}
					if (!scambi) break;
				}
				if (N >= filmz3.length) return filmz3;	//se N è uguale o superiore al numero di film ritorno l'array di film
				else {	//altrimenti ritorno solo gli N film con i voti più alti
					Movie[] searchedMovies = new Movie[N];	//creo un array di dimension N
					for (int k = 0; k< searchedMovies.length; k++) {	//inserisco i film nell'array
						searchedMovies[k]=filmz3[k]; 
					}
					return searchedMovies;
				}
			}
			else {//quicksort
				movieQuickSortScore(filmz3);	//ordino i film utilizzando quicksort
				if (N >= filmz3.length) return filmz3;	//se N è uguale o superiore al numero di film ritorno l'array di film
				else {	//altrimenti ritorno solo gli N film con i voti più alti
					Movie[] searchedMovies = new Movie[N];	//creo un array di dimension N
					for (int k = 0; k< searchedMovies.length; k++) {	//inserisco i film nell'array
						searchedMovies[k]=filmz3[k]; 
					}
					return searchedMovies;
				}
			
			}
		}
		else {	//funzione medesima a quella sopra, ma per la tabella hash
			Movie[] filmz3 = new Movie[countMovies()];
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
				movieQuickSortScore(filmz3);
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
	public Movie[] searchMostRecentMovies(Integer N) {	//funzione che ritorna gli N film più recenti
		if(map == false) {
			Movie[] filmz3 = filmz2;	//creo un array di supporto che contiene tutti i film 
			if(algo == false) { //bubblesort
				for (int i = 1; i < filmz3.length; i++) {	//ordino i film utilizzando il bubblesort in base all'anno
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
				if (N >= filmz3.length) return filmz3;	//se N è maggiore o uguale al numero dei film allora ritorno l'array di film
				else {	//altrimenti ritorno gli N film più recenti
					Movie[] searchedMovies = new Movie[N];
					for (int k = 0; k< searchedMovies.length; k++) {
						searchedMovies[k]=filmz3[k]; 
					}
					return searchedMovies;
				}
			}else{//quicksort
				movieQuickSortYear(filmz3);	//ordino i film utilizzando il quicksort
				if (N >= filmz3.length) return filmz3;	//se N è maggiore o uguale al numero dei film allora ritorno l'array di film
				else {	//altrimenti ritorno gli N film più recenti
					Movie[] searchedMovies = new Movie[N];
					for (int k = 0; k< searchedMovies.length; k++) {
						searchedMovies[k]=filmz3[k]; 
					}
					return searchedMovies;
				}
			}
		}
		else {	//funzione medesima a quella sopra, ma per la tabella hash
			Movie[] filmz3 = new Movie[countMovies()];
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
				movieQuickSortYear(filmz3);
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

    public int countActors() {	//funzione che conta tutti gli attori
		if(map == false) {
			Boolean Flag = false;
			List<String> persone = new ArrayList<>();			
			for(int i = 0; i < filmz2.length; i++) {	//scorro i film
				for(int j = 0; j < filmz2[i].getCast().length; j++) {	//scorro il cast del film puntato
					persone.add(filmz2[i].getCast()[j].getName());	//aggiungo il nome delle persone alla lista persone
				}
			}
			int cont = 0;			
			for(int i = 0; i < persone.size(); i++) {		//conto gli attori, non contando eventuali doppioni		
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
		else {	//funzione medesima a quella sopra, ma per la tabella hash
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
	
	public Person[] getAllActors() {	//funzione che ritorna tutti gli attori
		if(map == false) {	
			int index = countActors();	//ritorno il numero di attori
			Person[] persone = new Person[index];	//creo un array della dimensione del numero di attori
			List<String> personeNomi = new ArrayList<>();
			
			for(int i = 0; i < filmz2.length; i++) {	//metto in una lista i nomi degli attori
				for(int j = 0; j < filmz2[i].getCast().length; j++) {
					personeNomi.add(filmz2[i].getCast()[j].getName());
				}
			}
			
			for(int i = 0; i < personeNomi.size(); i++) {	//tolgo	i doppioni
				
				for(int j = i + 1; j < personeNomi.size(); j++) {
					if(personeNomi.get(i).equals(personeNomi.get(j))) {
						personeNomi.remove(j);
					}	
				}
			}
			for(int i = 0; i < persone.length; i++) {	//aggiungo le persone all'array
				Person persona = new Person(personeNomi.get(i));
				persone[i] = persona;
			}
			return persone;
		}
		else {//funzione medesima a quella sopra, ma per la tabella hash
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
	public Person[] searchMostActiveActors(Integer N) {	//funzione che ritorna gli N attori più attivi
		if(map == false) {
			Person[] attori = getAllActors(); //array che contiene tutti gli attori
			for (Person i : attori) {	//scorro gli attori e controllo quante volte compare il loro nome
				for (Movie j : filmz2) {
					for (Person g : j.getCast()) {
						if (i.getName().equals(g.getName())) i.increaseMovieCount();				
						}
				}
			}
			if(algo == false) {//bubblesort
				for (int index = 1; index <attori.length; index++) {
					boolean scambi = false;
					for (int jdex = 1; jdex <= attori.length - index; jdex++) {	//ordino gli attori utilizzando il bubblesort
						if (attori[jdex-1].getMovieStarred() < attori[jdex].getMovieStarred()) {
							Person tmp = attori[jdex-1];
							attori[jdex-1] = attori[jdex];
							attori[jdex] = tmp;
							scambi = true;
						}
					}
					if (!scambi) break;
				}
				Person[] attori2 = new Person[N];	//creo un array di dimensione N
				for (int kdex = 0; kdex < attori2.length; kdex++) {	//inserisco gli N attori all'interno dell'array
					attori2[kdex]=attori[kdex]; 
				}
				return attori2;
			}
			else {	//quicksort
				actorQuickSort(attori);	//ordino gli attori con il quicksort
				Person[] attori2 = new Person[N]; //creo un array di dimensione N
				for (int kdex = 0; kdex < attori2.length; kdex++) {	//inserisco gli N attori all'interno dell'array
					attori2[kdex]=attori[kdex]; 
				}
				return attori2;
			}
		}
		else {	//funzione medesima a quella sopra, ma per la tabella hash
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
	
	public static void actorQuickSort(Person[] arr) {	//funzione quicksort per attori
		actorQuickSortRec(arr, 0, arr.length - 1);
	}
	
	public static int partitionActor(Person[] arr, int low, int high) 
    { 
        Person pivot = arr[low];  
        int i = (low); //indice dell'elemento più piccolo 
        for (int j=low +1; j<= high; j++) 
        { 
            //Se l'elemento corrente è più piccolo del pivot
            if (arr[j].getMovieStarred() > pivot.getMovieStarred()) 
            { 
                i++; 
  
                //scambio arr[i] e arr[j]
                Person temp = arr[i]; 
                arr[i] = arr[j]; 
                arr[j] = temp; 
            } 
        } 
  
        //scambio arr[i+1] e arr[high]
        Person temp = arr[i]; 
        arr[i] = arr[low]; 
        arr[low] = temp; 
  
        return i; 
    } 
  
  
    public static void actorQuickSortRec(Person[] arr, int low, int high) 
    { 
        if (low < high) 
        { 
        	
            int pi = partitionActor(arr, low, high); 
  
            //Ordino ricorsivamente gli elementi 
            actorQuickSortRec(arr, low, pi); 
            actorQuickSortRec(arr, pi+1, high); 
        } 
    }
    
    public static void movieQuickSortYear(Movie[] arr) {
		movieQuickSortRecYear(arr, 0, arr.length - 1);
	}
	
	public static int partitionMovieYear(Movie[] arr, int low, int high) 
    { 
        Movie pivot = arr[low];  
        int i = (low); 
        for (int j=low +1; j<= high; j++) 
        { 
             
            if (arr[j].getYear() > pivot.getYear()) 
            { 
                i++; 
  
                 
                Movie temp = arr[i]; 
                arr[i] = arr[j]; 
                arr[j] = temp; 
            } 
        } 
  
        
        Movie temp = arr[i]; 
        arr[i] = arr[low]; 
        arr[low] = temp; 
  
        return i; 
    } 
  
    public static void movieQuickSortRecYear(Movie[] arr, int low, int high) 
    { 
        if (low < high) 
        { 
            
            int pi = partitionMovieYear(arr, low, high); 
         
            movieQuickSortRecYear(arr, low, pi); 
            movieQuickSortRecYear(arr, pi+1, high); 
        } 
    }
	
    public static void movieQuickSortScore(Movie[] arr) {
		movieQuickSortRecScore(arr, 0, arr.length - 1);
	}
	
	public static int partitionMovieScore(Movie[] arr, int low, int high) 
    { 
        Movie pivot = arr[low];  
        int i = (low); 
        for (int j=low +1; j<= high; j++) 
        { 
             
            if (arr[j].getVotes() > pivot.getVotes()) 
            { 
                i++; 
  
                 
                Movie temp = arr[i]; 
                arr[i] = arr[j]; 
                arr[j] = temp; 
            } 
        } 
  
        
        Movie temp = arr[i]; 
        arr[i] = arr[low]; 
        arr[low] = temp; 
  
        return i; 
    } 
  
  
   
    public static void movieQuickSortRecScore(Movie[] arr, int low, int high) 
    { 
        if (low < high) 
        { 
            
            int pi = partitionMovieScore(arr, low, high); 
         
            movieQuickSortRecScore(arr, low, pi); 
            movieQuickSortRecScore(arr, pi+1, high); 
        } 
    }
    
    
	@Override
	public boolean setSort(SortingAlgorithm a) {	//funzione per selezionare l'algoritmo di ordinamento
		if(a == SortingAlgorithm.BubbleSort) {	//se il parametro è bubblesort
			if(algo == false)	//se l'algoritmo è già settato a bubblesort ritorno false
				return false;
			else {	//altrimenti setto l'algoritmo a false e ritorno true
				algo = false;
				return true;
			}
				
		} 
		else if (a == SortingAlgorithm.QuickSort){	//se il parametro è quicksort
			if(algo == true)	//se l'algoritmo è già settato a quicksort ritorno false
				return false;
			else {	//altrimenti lo setto a true e ritorno true
				algo = true;
				return true;
			}
		}
		else	//se il parametro non è ne bubblesort ne quicksort allora ritorno false e non cambio l'algoritmo
		{
			return false;
		}
		
	}

	@Override
	public boolean setMap(MapImplementation m) {	//funzione per settare il dizionario
		if(m == MapImplementation.ArrayOrdinato) {	//se il parametro è ArrayOrdinato
			if(map == false)	//se il dizionario è già settato ad arrayordinato ritorno false
				return false;
			else {	//altrimenti setto il dizionario ad arrayordinato e ritorno true
				map = false;
				return true;
			}
				
		} 
		else if (m == MapImplementation.HashConcatenamento){	//se il parametro è HashConcatenamento
			if(map == true)	//se il dizionario è già settato ad HashConcatenamento ritorno false
				return false;
			else {
				map = true;	//altrimenti setto il dizionario ad HashConcatenamento e ritorno true
				return true;
			}
		}
		else
		{
			return false;	//se il parametro non è ne ArrayOrdinato ne HashConcatenamento allora ritorno false e non cambio il dizionario
		
		}
		
	}

}