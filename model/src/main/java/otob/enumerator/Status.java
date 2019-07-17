package otob.enumerator;

public enum Status {
    ORD_WAIT("Waiting"),
    ORD_ACCEPT("Accepted"),
    ORD_REJECT("Rejected"),
    LOGIN_TRUE("true"),
    LOGIN_FALSE("false");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
