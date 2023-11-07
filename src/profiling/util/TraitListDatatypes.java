package profiling.util;

import java.util.ArrayList;


public class TraitListDatatypes {
	static Integer commonDatatypes = 0;
	// Recherche des LPT liés aux datatypes
	public static ArrayList<Lpt> makeList(ArrayList<Uri> listDatatypesSource, ArrayList<Uri> listDatatypesTarget) {
		
		ArrayList<Lpt> listLpts = new ArrayList<Lpt>();
		Boolean noDatatypesSource	= false;
		Boolean noDatatypesTarget	= false;
		Integer listDatatypesSourceSize = listDatatypesSource.size(); 
		Integer listDatatypesTargetSize = listDatatypesTarget.size(); 
		Integer minListDatatypesSize = 0;


		if (listDatatypesSourceSize == 0) {
			noDatatypesSource	= true;
		}
		if (listDatatypesTargetSize == 0) {
			noDatatypesTarget	= true;
		}
	
		if (listDatatypesSourceSize > listDatatypesTargetSize) {
			minListDatatypesSize = listDatatypesTargetSize; 
		} else {
			minListDatatypesSize = listDatatypesSourceSize; 
		}

		// Si pas de datatypes pour le jeux source ou target 
		if (noDatatypesSource || noDatatypesTarget) {
			
		}

		// Si au moins un datatypes pour les datasets source et target
		if (!noDatatypesSource & !noDatatypesTarget) {
			// On compare les datatypes des jeux de données 
			listDatatypesSource.forEach((sourceDatatype) -> {
				listDatatypesTarget.forEach((targetDatatype) -> {
					if (sourceDatatype.getUri().toString().equals(targetDatatype.getUri().toString())) {
						commonDatatypes ++;
					}			
				});
			});
			System.out.println("MinlistDatatypesSize : " + minListDatatypesSize);
			System.out.println("CommonDatatypes : " + commonDatatypes);
			if (commonDatatypes == minListDatatypesSize) {

			} else {	
				listLpts.add(new Lpt("LPT1.1.1.2"));
			}
		}
	
		return listLpts;
	}

	
}