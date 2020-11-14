package movida.bennatideluise;

import movida.bennatideluise.MovidaCore;
import movida.bennatideluise.MovidaCore.Grafo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

//classe di test delle funzioni di MovidaCore

public class Demo {

	public static void main(String[] args) {
		MovidaCore core = new MovidaCore();
		
		File f = new File("src/movida/bennatideluise/fileprova2.txt");
		core.loadFromFile(f);
		core.createGraph();
		core.fillGraph();
	}

}
