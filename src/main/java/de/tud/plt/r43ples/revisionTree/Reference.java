package de.tud.plt.r43ples.revisionTree;

public abstract class Reference {
	
	private String uri;
	private String name;
	private Commit ref;
	

	public Reference(String uri, String name, Commit ref) {
		this.uri = uri;
		this.name = name;
		this.ref = ref;
	}
	
	public String getUri() {
		return uri;
	}

	public String getName() {
		return name;
	}
	
	public Commit getReference() {
		return ref;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Reference) {
			Reference b = (Reference) obj;
			return this.uri.equals(b.uri);
		}
		else {
			return super.equals(obj);
		}
	}

}
