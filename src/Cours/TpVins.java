package Cours;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;



public class TpVins {

	static File lesVins = new File("c:/DossierAI108/TpVins/VINStp.DON");
	static int[] structure = new int[7];

	public static void main(String[] args) {

		//On commence par aller lire le fichier
		lectureFichier(lesVins);
		System.out.println("bytes");


		File vinsBin = ecritureFichier(lesVins);
		//File vinsBin = writeFileRaf(lesVins);

		triSelectionRecursif(vinsBin, structure[0]);
		
		//si écritureFichier
		String recherche = "Domaine Alain Geoffroy Vautigreau-------------";
		
		//si writeFileRaf
		//String recherche = "Château Rauzan-Gassies                        Saint-Estephe          Bordelais           Lrq121O12C";
		
		rechercheDichoto(vinsBin,recherche);
		
		//rechercheV2(vinsBin, recherche);


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
			raf.seek(numeroLigne*98);
			raf.writeBytes(ligne);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	private static String lecturecol(int ligne, int colonne, File vinsBin) {

		RandomAccessFile raf2 = null;
		String colonneString = "";
		//accï¿½s au dbt de la ligne demandï¿½e
		int pointeurRAF = ligne*98;
		//accï¿½s au dbt de la colonne demandï¿½e
		for (int i = 1; i < colonne; i++) {
			pointeurRAF = structure[i]*2+pointeurRAF;
		}

		try {
			raf2 = new RandomAccessFile(vinsBin, "r");
			raf2.seek(pointeurRAF);
			//je suis bien positionnï¿½ pour lire la colonne demandÃ©e
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
		//accÃ¨s au dbt de la ligne demandÃ©e
		int pointeurRAF = numeroLigne*98;
		String ligne ="";
		byte [] tabBytes = new byte [98];//46 pour que la première colonne et 98 pour la ligne

		try {
			raf2 = new RandomAccessFile(vinsBin, "r");
			raf2.seek(pointeurRAF);
			//je suis bien positionnÃ© pour lire la colonne demandÃ©e
			//lecture de la colonne
			//for (int i = 0; i < 98; i++) {
			raf2.read(tabBytes);				
			ligne = new String(tabBytes);


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

	private static String lecturecolonne1(int numeroLigne, File vinsBin) {

		RandomAccessFile raf2 = null;
		//accÃ¨s au dbt de la ligne demandÃ©e
		int pointeurRAF = numeroLigne*98;
		String ligne ="";
		byte [] tabBytes = new byte [46];//46 pour que la première colonne et 98 pour la ligne

		try {
			raf2 = new RandomAccessFile(vinsBin, "r");
			raf2.seek(pointeurRAF);
			//je suis bien positionnÃ© pour lire la colonne demandÃ©e
			//lecture de la colonne
			//for (int i = 0; i < 98; i++) {
			raf2.read(tabBytes);				
			ligne = new String(tabBytes);


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
	private static byte[] lectureCol1(int numeroLigne, File vinsBin) {

		RandomAccessFile raf2 = null;
		//accÃ¨s au dbt de la ligne demandÃ©e
		int pointeurRAF = numeroLigne*98;

		byte [] tabBytes = new byte [46];

		try {
			raf2 = new RandomAccessFile(vinsBin, "r");
			raf2.seek(pointeurRAF);
			//je suis bien positionnÃ© pour lire la colonne demandÃ©e
			//lecture de la colonne
			//for (int i = 0; i < 98; i++) {
			raf2.read(tabBytes);				



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

		return tabBytes;
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

					//si on detecte un caractÃ¨re on Ã©crit le caractÃ¨re
					//puis nbCaractere++
					if (tabBytes[i]!='\t') {
						raf.write(tabBytes[i]);
						nbCaractere++;


						// des que l'on detecte un \t on vÃ©rifie le nb d'espace manquants
						// on les Ã©crits avec le RAF puis on change de colone
					} else if ((tabBytes[i]=='\t') ) {
						int nbespace= structure[indexCol]-nbCaractere;
						for (int j = 0; j < nbespace; j++) {
							raf.write('-');
						}
						indexCol++;
						nbCaractere=0;


						// une fois arrivÃ© au dernier caractÃ¨re du tableau
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

	private static File writeFileRaf(File lesVins) {

		RandomAccessFile lecture = null;
		RandomAccessFile ecriture = null;
		File retour = new File(lesVins.getParent()+"/binRAF.DON");




		try {
			lecture = new RandomAccessFile(lesVins,"r");
			ecriture = new RandomAccessFile(retour,"rw");

			int indexCol = 1;
			int nbCaractere=0;


			while (lecture.getFilePointer()!=lesVins.length()-1) {

				int octetLu = lecture.readByte();

				switch (octetLu) {
				case 9: //tabulation
					int nbespace= structure[indexCol]-nbCaractere;
					for (int j = 0; j < nbespace; j++) {
						ecriture.write(00100000);//00100000 = espace
					}
					indexCol++;
					nbCaractere=0;
					break;

				case 10:	//fin de ligne
				case 13:	//fin de ligne
					indexCol=1;
					nbCaractere=0;

					break;
				default:
					ecriture.write(octetLu);
					nbCaractere++;
					break;
				}





			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		;


		return retour;
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




				//il Ã©tait sur l'acolade du dessus donc bug
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

			//si on detecte un caractÃ¨re on est dans la colonne
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
			System.out.print("portion de " + debut + " Ã  " + fin + " : ");
			int milieu = (debut + fin) / 2;

			if (recherche.equals(lecturecolonne1(milieu, file)))
			{
				System.out.println("TouvÃ© !!! indice : " + milieu);
				trouve = true;
				System.out.println(lectureLigne(milieu, file));
				return milieu;
			}

			if (lecturecolonne1(milieu, file).compareTo(recherche) < 0)
			{
				System.out.println(" plus grand que " + lecturecolonne1(milieu, file));
				debut = milieu + 1;
			}

			if (lecturecolonne1(milieu, file).compareTo(recherche) > 0)
			{
				System.out.println(" plus petit que " + lecturecolonne1(milieu, file));
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
			System.out.println("Elï¿½ment non trouvï¿½...");
		}


		return -1;

	}	

	public static int rechercheV2(File file, String recherche)
	{

		// initialisation :



		int debut = 0;
		int fin = structure[0];	
		boolean trouve = false; // flag 
		boolean rechercheTerminee = false; // flag


		byte[] searchByte = recherche.getBytes();


		// traitement :
		do
		{

			System.out.println("portion de " + debut + " Ã  " + fin + " : ");
			int milieu = (debut + fin) / 2;
			int i = 0;
			byte[] colonne = lectureCol1(milieu, file);
			for (byte b : colonne) {
				System.out.println("dans colonne = "+b);
			}
			
			while (searchByte[i]==colonne[i]) {
				System.out.println("la recherche = "+searchByte[i]+" et le pointeur = "+colonne[i]);

				i++;
			}
			System.out.println("la recherche = "+searchByte[i]+" et le pointeur = "+colonne[i]);

			if (searchByte[i]==colonne[i])
			{
				System.out.println("TouvÃ© !!! indice : " + milieu);
				trouve = true;
				return milieu;
			}

			if (searchByte[i]<colonne[i])
			{
				debut = milieu + 1;
			}

			if (searchByte[i]>colonne[i])
			{
				System.out.println(" plus petit que " + lectureLigne(milieu, file));
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
			System.out.println("Elment non trouvï¿½...");
		}









		return -1;

	}	


}
