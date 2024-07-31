package config;

public interface Config {
    // Method to create the configuration
    void create();

    // Method to get the name of the configuration
    String getName();

    // Method to get the version of the configuration
    int getVersion();

    // Method to close or clean up the configuration
    void close();
}
