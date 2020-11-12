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

		//On commence par aller lire le fichier









		lectureFichier(lesVins);



		File vinsBin = ecritureFichier(lesVins);

		int[] structureOctet = new int[structure.length-1];
		for (int i = 0; i < structureOctet.length; i++) {
			structureOctet[i]=structure[i+1]*2;
		}

		

		System.out.println("lecture ligne 0");
		lecturecol (0, 1, vinsBin) ;
		System.out.println();
		lecturecol (0, 2, vinsBin) ;
		System.out.println();
		System.out.println("lecture ligne 1");
		lecturecol (1, 1, vinsBin) ;
		System.out.println();
		lecturecol (1, 2, vinsBin) ;
		System.out.println();

		swapLigne(0,1, vinsBin);

		System.out.println("apres swap");

		System.out.println("lecture ligne 0");
		lecturecol (0, 1, vinsBin) ;
		System.out.println();
		lecturecol (0, 2, vinsBin) ;
		System.out.println();
		System.out.println("lecture ligne 1");
		lecturecol (1, 1, vinsBin) ;
		System.out.println();
		lecturecol (1, 2, vinsBin) ;
		System.out.println();








	}

	private static void swapLigne(int a, int b, File vinsBin) {

		RandomAccessFile rafB = null;
		RandomAccessFile rafA = null;
		int pointeurRAF = a*196;
		char[] temp = new char[98];



		try {
			rafA = new RandomAccessFile(vinsBin, "rw");
			rafB = new RandomAccessFile(vinsBin, "rw");



			rafA.seek(a*196);
			for (int i = 0; i < 98; i++) {
				temp[i]=rafA.readChar();

			}

			rafA.seek(a*196);
			rafB.seek(b*196);
			for (int i = 0; i < 98; i++) {
				rafA.writeChar(rafB.readChar());
			}
			rafB.seek(b*196);
			for (char c : temp) {
				rafB.writeChar(c);
			}











		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				rafB.close();
				rafA.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

	private static void lecturecol(int ligne, int colonne, File vinsBin) {

		RandomAccessFile raf2 = null;
		int pointeurRAF = ligne*196;
		for (int i = 1; i < colonne; i++) {
			pointeurRAF = structure[i]*2+pointeurRAF;
		}

		try {
			raf2 = new RandomAccessFile(vinsBin, "r");
			raf2.seek(pointeurRAF);
						//je suis bien positinné pour lire le 4e int
			for (int i = 0; i < structure[colonne]; i++) {
				System.out.print(raf2.readChar());


			}
			System.out.println();



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

				char[] tabChar = ligne.toCharArray();



				int indexCol = 1;
				int finLigne = tabChar.length - 1;
				int nbCaractere=0;


				for (int i = 0; i < tabChar.length; i++) {

					//si on detecte un caractère on écrit le caractère
					//puis nbCaractere++
					if (tabChar[i]!='\t') {
						raf.writeChar(tabChar[i]);
						nbCaractere++;


						// des que l'on detecte un \t on vérifie le nb d'espace manquants
						// on les écrits avec le RAF puis on change de colone
					} else if ((tabChar[i]=='\t') ) {
						int nbespace= structure[indexCol]-nbCaractere;
						for (int j = 0; j < nbespace; j++) {
							raf.writeChar('.');
						}
						indexCol++;
						nbCaractere=0;


						// une fois arrivé au dernier caractère du tableau
						// on calcul le nombre d'espace manquant
						//puis on les imprime dans le fichier
					} else if (i == finLigne) {
						for (int j = 0; j < (structure[indexCol]-nbCaractere); j++) {
							raf.writeChar('@');
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

	static  void largeurCol(char[] ligne, int[] structure) {


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
}
