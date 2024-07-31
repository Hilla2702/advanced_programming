package servlets;

import java.io.IOException;
import java.io.OutputStream;

import server.RequestParser.RequestInfo;

public interface Servlet {

    // Handles an HTTP request and sends a response to the client
    void handle(RequestInfo ri, OutputStream toClient) throws IOException;

    // Closes any resources used by the servlet
    void close() throws IOException;
}
