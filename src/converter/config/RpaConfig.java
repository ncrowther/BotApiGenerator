package converter.config;

public class RpaConfig {
	String id;
	String startId;
	String name;
	String system;
	String baseUrl;
	String tenantId;
	String rpaUser;
	String rpaPwd;	
	String odmHost;	
	String odmPath;	
	String odmPayload;	
	String resUser;
	String resPwd;
	String documentation;	

	
	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getRpaUser() {
		return rpaUser;
	}

	public void setRpaUser(String rpaUser) {
		this.rpaUser = rpaUser;
	}

	public String getRpaPwd() {
		return rpaPwd;
	}

	public void setRpaPwd(String rpaPwd) {
		this.rpaPwd = rpaPwd;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStartId() {
		return startId;
	}

	public void setStartId(String startId) {
		this.startId = startId;
	}
	
    public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}	

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}
	
	public String getOdmPath() {
		return odmPath;
	}

	public void setOdmPath(String odmPath) {
		this.odmPath = odmPath;
	}

	public String getOdmHost() {
		return odmHost;
	}

	public void setOdmHost(String odmHost) {
		this.odmHost = odmHost;
	}

	public String getOdmPayload() {
		return odmPayload;
	}

	public void setOdmPayload(String odmPayload) {
		this.odmPayload = odmPayload;
	}
	
	public String getResUser() {
		return resUser;
	}

	public void setResUser(String resUser) {
		this.resUser = resUser;
	}

	public String getResPwd() {
		return resPwd;
	}

	public void setResPwd(String resPwd) {
		this.resPwd = resPwd;
	}

	@Override
	public String toString() {
		return "RpaConfig [id=" + id + ", startId=" + startId + ", name=" + name + ", system=" + system + ", baseUrl="
				+ baseUrl + ", tenantId=" + tenantId + ", rpaUser=" + rpaUser + ", rpaPwd=" + rpaPwd + ", odmHost="
				+ odmHost + ", odmPath=" + odmPath + ", odmPayload=" + odmPayload + ", resUser=" + resUser + ", resPwd="
				+ resPwd + ", documentation=" + documentation + "]";
	}


}
