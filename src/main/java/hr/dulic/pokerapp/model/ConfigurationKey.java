package hr.dulic.pokerapp.model;

public enum ConfigurationKey {
    SERVER_PORT("server.port"), CLIENT_PORT("client.port"),
    HOST("host"), GROUP("group") ,RANDOM_PORT_HINT("random.port.hint"),
    RMI_PORT("rmi.port");

    private String key;
    private ConfigurationKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
