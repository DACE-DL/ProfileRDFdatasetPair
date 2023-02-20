package webService;

import java.util.ArrayList;

public class Plot {
	private String id;
	private String gps;
    private ArrayList<String> prefLabels;

    public Plot() { }

    public Plot(String id, String gps, ArrayList<String> prefLabels) {
        this.id = id;
        this.gps = gps;
        this.prefLabels = new ArrayList<String>(prefLabels);
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<String> getPrefLabels() {
		return prefLabels;
	}

	public void setPrefLabels(ArrayList<String> prefLabels) {
		this.prefLabels = new ArrayList<String>(prefLabels);
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}
}
