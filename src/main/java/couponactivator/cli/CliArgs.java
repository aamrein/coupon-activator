package couponactivator.cli;

public class CliArgs {

    private String username = "";
    private String password = "";
    private String typeFilter = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTypeFilter() {
        return typeFilter.toUpperCase();
    }

    public void setTypeFilter(String typeFilter) {
        this.typeFilter = typeFilter;
    }

    public boolean hasTypeFilter() {
        return this.typeFilter.length() > 0;
    }
}
