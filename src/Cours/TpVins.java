package Cours;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;



public class TpVins {

	static File lesVins = new File("c:/DossierAI108/TpVins/VINStp.DON");
	static int[] structure = new int[7];
	//pour structure index : 
	//0 = nb lignes
	// 1= val max col 1
	//2 = val max col 2
	//etc

	public static void main(String[] args) {
		//TESTEEEE

		//On commence par aller lire le fichier
		lectureFichier(lesVins);

		File vinsBin = ecritureFichier(lesVins);

		triSelectionRecursif(vinsBin, structure[0]);
		
		//rechercheDichoto(lesVins, "Caslot-Bourdin................................");


	}

	private static void swapLigne(int a, int b, File swapFile) {


		String ligneA = lectureLigne(a, swapFile);
		String ligneB = lectureLigne(b, swapFile);
		
		ecritureLigne(swapFile,ligneA, b);
		ecritureLigne(swapFile,ligneB, a);


	}

	private static void ecritureLigne(File vinsBin, String ligne, int numeroLigne) {

		RandomAccessFile raf = null;
		
		
		
		try {
			raf = new RandomAccessFile(vinsBin, "rw");
			raf.seek(numeroLigne*196);
			raf.writeBytes(ligne);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	private static String lecturecol(int ligne, int colonne, File vinsBin) {

		RandomAccessFile raf2 = null;
		String colonneString = "";
		//accès au dbt de la ligne demandée
		int pointeurRAF = ligne*196;
		//accès au dbt de la colonne demandée
		for (int i = 1; i < colonne; i++) {
			pointeurRAF = structure[i]*2+pointeurRAF;
		}

		try {
			raf2 = new RandomAccessFile(vinsBin, "r");
			raf2.seek(pointeurRAF);
			//je suis bien positionné pour lire la colonne demandée
			//lecture de la colonne
			for (int i = 0; i < structure[colonne]; i++) {
				colonneString = colonneString + Character.toString(raf2.readChar());
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				raf2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return colonneString;

	}

	private static String lectureLigne(int numeroLigne, File vinsBin) {

		RandomAccessFile raf2 = null;
		//accès au dbt de la ligne demandée
		int pointeurRAF = numeroLigne*196;
		String ligne ="";
		byte [] tabBytes = new byte [98];

		try {
			raf2 = new RandomAccessFile(vinsBin, "r");
			raf2.seek(pointeurRAF);
			//je suis bien positionné pour lire la colonne demandée
			//lecture de la colonne
			//for (int i = 0; i < 98; i++) {
				raf2.read(tabBytes);				
				ligne = new String(tabBytes);
				
//				ligne = ligne + Character.toString(raf2.readChar());
			//}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				raf2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ligne;
	}


	private static File ecritureFichier(File lesVins) {

		FileReader in=null;
		BufferedReader br = null;
		RandomAccessFile raf = null;
		String ligne = "";
		File retour = new File(lesVins.getParent()+"/VINSbin.DON");



		if (retour.exists()) {
			return retour ;
		}
		try {
			in = new FileReader(lesVins);
			br = new BufferedReader(in);
			ligne = br.readLine();
			raf = new RandomAccessFile(retour,"rw");
			while(ligne!= null) {

				byte[] tabBytes = ligne.getBytes();



				int indexCol = 1;
				int finLigne = tabBytes.length - 1;
				int nbCaractere=0;


				for (int i = 0; i < tabBytes.length; i++) {

					//si on detecte un caractère on écrit le caractère
					//puis nbCaractere++
					if (tabBytes[i]!='\t') {
						raf.write(tabBytes[i]);
						nbCaractere++;


						// des que l'on detecte un \t on vérifie le nb d'espace manquants
						// on les écrits avec le RAF puis on change de colone
					} else if ((tabBytes[i]=='\t') ) {
						int nbespace= structure[indexCol]-nbCaractere;
						for (int j = 0; j < nbespace; j++) {
							raf.write('-');
						}
						indexCol++;
						nbCaractere=0;


						// une fois arrivé au dernier caractère du tableau
						// on calcul le nombre d'espace manquant
						//puis on les imprime dans le fichier
					} else if (i == finLigne) {
						for (int j = 0; j < (structure[indexCol]-nbCaractere); j++) {
							raf.write('@');
						}
					}

				}
				ligne = br.readLine();
			}	
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				br.close();
				in.close();
				raf.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



		}



		return retour ;
	}

	private static void lectureFichier(File lesVins) {



		FileReader in=null;
		BufferedReader br = null;
		String ligne = "";



		try {
			in = new FileReader(lesVins);
			br = new BufferedReader(in);
			ligne = br.readLine();

			while(ligne!= null) {
				structure[0]++;



				char[] tabChar = ligne.toCharArray();
				largeurCol(tabChar, structure);




				//il était sur l'acolade du dessus donc bug
				ligne = br.readLine();
			}	
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				br.close();
				in.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



		}

	}

	private static void largeurCol(char[] ligne, int[] structure) {


		int indexCol = 1;
		int finLigne = ligne.length - 1;
		int[] structureTemp = new int[structure.length];


		for (int i = 0; i < ligne.length; i++) {

			//si on detecte un caractère on est dans la colonne
			if (ligne[i]!='\t') {

				structureTemp[indexCol]++;

				// des que l'on detecte un \t on verifie si on avait une colonne avant
				// si oui indexcol++ et ce n'est plus une colone
			} else if ((ligne[i]=='\t') ) {
				indexCol++;


				// si le dernier char est une lettre et pas un espace
				// alors nbmot++.
			} else if (i == finLigne) {
				structureTemp[indexCol]++;;

			}
		}
		for (int i = 1; i < structureTemp.length; i++) {
			structure[i]= structureTemp[i]>structure[i] ? structureTemp[i]:structure[i];
		}






	}

	public static void triSelectionRecursif(File file, int nbLignes)
	{
		String a;
		String b;




		if (nbLignes > 1)
		{
			int ligneDuPlusGrand = 0;

			for (int i = 1; i < nbLignes; i++) 
			{
				a = lectureLigne(i, file);
				b = lectureLigne(ligneDuPlusGrand, file);
				if (b.compareTo(a)<0)
				{
					ligneDuPlusGrand = i;
				}
			}

			//permuter(tableau, ligneDuPlusGrand, nbLignes-1);
			swapLigne(ligneDuPlusGrand, nbLignes-1, file);

			triSelectionRecursif(file, nbLignes-1);
		}
	}

	public static int rechercheDichoto(File file, String recherche)
		{

		// initialisation :

		

		int debut = 0;
		int fin = structure[0];	
		boolean trouve = false; // flag 
		boolean rechercheTerminee = false; // flag


		// traitement :
		do
		{
			System.out.print("portion de " + debut + " à " + fin + " : ");
			int milieu = (debut + fin) / 2;

			if (recherche.equalsIgnoreCase(lecturecol(milieu, 1, file)))
			{
				System.out.println("Touvé !!! indice : " + milieu);
				trouve = true;
				return milieu;
			}

			if (lecturecol(milieu, 1, file).compareTo(recherche) < 0)
			{
				System.out.println(" plus grand que " + lecturecol(milieu, 1, file));
				debut = milieu + 1;
			}

			if (lecturecol(milieu, 1, file).compareTo(recherche) > 0)
			{
				System.out.println(" plus petit que " + lecturecol(milieu, 1, file));
				fin = milieu;
			}

			if (fin == -1 || debut == structure[0] || debut == fin)
			{
				rechercheTerminee = true;
			}
		}
		while(!trouve && !rechercheTerminee);

		if (!trouve)
		{
			System.out.println("Elément non trouvé...");
		}


		return -1;

	}
		

}
