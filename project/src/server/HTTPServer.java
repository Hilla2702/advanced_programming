package server;

import servlets.Servlet;

public interface HTTPServer extends Runnable {
    // Add a servlet to handle specific HTTP commands and URIs
    public void addServlet(String httpCommand, String uri, Servlet s);

    // Remove a servlet from handling specific HTTP commands and URIs
    public void removeServlet(String httpCommand, String uri);

    // Start the HTTP server
    public void start();

    // Close the HTTP server and release resources
    public void close();
}
