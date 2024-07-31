package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import servlets.Servlet;

public class MyHTTPServer extends Thread implements HTTPServer {

    private final int port; // Port for the HTTP server
    private final ExecutorService threadPool; // Thread pool for handling client requests
    private final ConcurrentHashMap<String, Servlet> getServlets; // Mapping of GET requests to servlets
    private final ConcurrentHashMap<String, Servlet> postServlets; // Mapping of POST requests to servlets
    private final ConcurrentHashMap<String, Servlet> deleteServlets; // Mapping of DELETE requests to servlets
    private volatile boolean running; // Flag to indicate if the server is running
    private ServerSocket serverSocket; // Server socket for accepting client connections

    // Constructor
    public MyHTTPServer(int port, int nThreads) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(nThreads); // Initialize thread pool
        this.getServlets = new ConcurrentHashMap<>();
        this.postServlets = new ConcurrentHashMap<>();
        this.deleteServlets = new ConcurrentHashMap<>();
        this.running = true;
    }

    @Override
    public void addServlet(String httpCommand, String uri, Servlet s) {
        // Add a servlet based on the HTTP command and URI
        switch (httpCommand) {
            case "GET":
                getServlets.put(uri, s);
                break;
            case "POST":
                postServlets.put(uri, s);
                break;
            case "DELETE":
                deleteServlets.put(uri, s);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP command: " + httpCommand);
        }
    }

    @Override
    public void removeServlet(String httpCommand, String uri) {
        // Remove a servlet based on the HTTP command and URI
        switch (httpCommand) {
            case "GET":
                getServlets.remove(uri);
                break;
            case "POST":
                postServlets.remove(uri);
                break;
            case "DELETE":
                deleteServlets.remove(uri);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP command: " + httpCommand);
        }
    }

    @Override
    public void run() {
        // Main server loop to accept and handle client connections
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            this.serverSocket = serverSocket; // Store server socket for later use
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.submit(() -> handleClient(clientSocket)); // Handle client connection in a separate
                                                                         // thread
                } catch (IOException e) {
                    if (running) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown(); // Shut down the thread pool
        }
    }

    @Override
    public void close() {
        // Close the server and release resources
        running = false;
        threadPool.shutdown();
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close(); // Close server socket
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Close all registered servlets
        getServlets.values().forEach(this::closeServlet);
        postServlets.values().forEach(this::closeServlet);
        deleteServlets.values().forEach(this::closeServlet);
    }

    @Override
    public void start() {
        super.start(); // Start the server thread
    }

    private void handleClient(Socket clientSocket) {
        // Handle client requests
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream outputStream = clientSocket.getOutputStream()) {
            RequestParser.RequestInfo requestInfo = RequestParser.parseRequest(reader);
            String httpCommand = requestInfo.getHttpCommand();
            String uri = requestInfo.getUri();
            Servlet servlet = findBestMatchingServlet(httpCommand, uri);

            if (servlet != null) {
                servlet.handle(requestInfo, outputStream); // Handle request with the best matching servlet
            } else {
                sendNotFoundResponse(outputStream); // Send 404 Not Found response
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close(); // Close client socket
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Servlet findBestMatchingServlet(String httpCommand, String uri) {
        // Find the best matching servlet for a given HTTP command and URI
        ConcurrentHashMap<String, Servlet> commandMap;
        switch (httpCommand.toUpperCase()) {
            case "GET":
                commandMap = getServlets;
                break;
            case "POST":
                commandMap = postServlets;
                break;
            case "DELETE":
                commandMap = deleteServlets;
                break;
            default:
                return null;
        }

        Servlet bestMatch = null;
        int bestMatchLength = -1;

        // Find the longest matching URI
        for (String registeredUri : commandMap.keySet()) {
            if (uri.startsWith(registeredUri) && registeredUri.length() > bestMatchLength) {
                bestMatch = commandMap.get(registeredUri);
                bestMatchLength = registeredUri.length();
            }
        }

        return bestMatch;
    }

    private void sendNotFoundResponse(OutputStream outputStream) throws IOException {
        // Send a 404 Not Found response
        String response = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Length: 0\r\n" +
                "\r\n";
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
    }

    private void closeServlet(Servlet servlet) {
        try {
            servlet.close(); // Close the servlet
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
