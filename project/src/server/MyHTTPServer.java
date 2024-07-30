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

    private final int port;
    private final ExecutorService threadPool;
    private final ConcurrentHashMap<String, Servlet> getServlets;
    private final ConcurrentHashMap<String, Servlet> postServlets;
    private final ConcurrentHashMap<String, Servlet> deleteServlets;
    private volatile boolean running;
    private ServerSocket serverSocket;

    public MyHTTPServer(int port, int nThreads) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(nThreads);
        this.getServlets = new ConcurrentHashMap<>();
        this.postServlets = new ConcurrentHashMap<>();
        this.deleteServlets = new ConcurrentHashMap<>();
        this.running = true;
    }

    @Override
    public void addServlet(String httpCommand, String uri, Servlet s) {
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
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.submit(() -> handleClient(clientSocket));
                } catch (IOException e) {
                    if (running) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    public void close() {
        running = false;
        threadPool.shutdown();
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Servlet getServlets : getServlets.values()) {
            try {
                getServlets.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Servlet postServlets : postServlets.values()) {
            try {
                postServlets.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Servlet deleteServlets : deleteServlets.values()) {
            try {
                deleteServlets.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        super.start();
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream outputStream = clientSocket.getOutputStream()) {
            RequestParser.RequestInfo requestInfo = RequestParser.parseRequest(reader);
            String httpCommand = requestInfo.getHttpCommand();
            String uri = requestInfo.getUri();
            Servlet servlet = findBestMatchingServlet(httpCommand, uri);

            if (servlet != null) {
                servlet.handle(requestInfo, outputStream);
            } else {
                sendNotFoundResponse(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Servlet findBestMatchingServlet(String httpCommand, String uri) {
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

        for (String registeredUri : commandMap.keySet()) {
            if (uri.startsWith(registeredUri) && registeredUri.length() > bestMatchLength) {
                bestMatch = commandMap.get(registeredUri);
                bestMatchLength = registeredUri.length();
            }
        }

        return bestMatch;
    }

    private void sendNotFoundResponse(OutputStream outputStream) throws IOException {
        String response = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Length: 0\r\n" +
                "\r\n";
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
    }

}
