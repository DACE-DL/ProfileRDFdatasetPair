package profiling.util;

import java.util.ArrayList;

public class PairOfDatasets {
	
	private String idPair;
	private ArrayList<FileName> filesSource;
	private ArrayList<FileName> filesTarget;

	public PairOfDatasets() {
	}

	public PairOfDatasets(String idPair, ArrayList<FileName> filesSource, ArrayList<FileName> filesTarget) {
		this.idPair = idPair;
		this.filesSource = filesSource;
		this.filesTarget = filesTarget;
	}

	public String getIdPair() {
		return idPair;
	}

	public void setIdPair(String idPair) {
		this.idPair = idPair;
	}

	public ArrayList<FileName> getFilesSource() {
		return filesSource;
	}

	public void setFilesSource(ArrayList<FileName> filesSource) {
		this.filesSource = filesSource;
	}

	public ArrayList<FileName> getFilesTarget() {
		return filesTarget;
	}

	public void setFilesTarget(ArrayList<FileName> filesTarget) {
		this.filesTarget = filesTarget;
	}

	

}
	