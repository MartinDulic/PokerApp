package hr.dulic.pokerapp.utils.networkUtils;

import hr.dulic.pokerapp.model.ConfigurationKey;
import hr.dulic.pokerapp.model.ConfigurationReader;

public class NetworkConfiguration {
    public static Integer SERVER_PORT = ConfigurationReader.getIntegerValueForKey(ConfigurationKey.SERVER_PORT);
    public static Integer CLIENT_PORT = ConfigurationReader.getIntegerValueForKey(ConfigurationKey.CLIENT_PORT);
    public static final String HOST = ConfigurationReader.getStringValueForKey(ConfigurationKey.HOST);
    public static final String GROUP = ConfigurationReader.getStringValueForKey(ConfigurationKey.GROUP);
    public static final int RANDOM_PORT_HINT = ConfigurationReader.getIntegerValueForKey(ConfigurationKey.RANDOM_PORT_HINT);
    public static final int RMI_PORT = ConfigurationReader.getIntegerValueForKey(ConfigurationKey.RMI_PORT);
}
