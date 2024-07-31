package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    // Parses an HTTP request from the given BufferedReader
    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
        // Check if the reader is null
        if (reader == null) {
            throw new IllegalArgumentException("BufferedReader cannot be null");
        }

        // Read the request line (e.g., "GET /path?param=value HTTP/1.1")
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IllegalArgumentException("empty");
        }

        // Split the request line into method, URI, and version
        String[] requestParts = requestLine.split(" ");
        if (requestParts.length != 3) {
            throw new IllegalArgumentException("Invalid request line: " + requestLine);
        }

        // Extract the HTTP method and URI
        String method = requestParts[0];
        String uri = requestParts[1];
        // String version = requestParts[2]; // Not used

        // Validate the HTTP method
        if (!(method.equals("GET") || method.equals("POST") || method.equals("DELETE"))) {
            throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }

        // Initialize URI segments and parameters
        String[] uriSegments;
        Map<String, String> parameters = new HashMap<>();
        int questionMarkIndex = uri.indexOf('?');
        if (questionMarkIndex != -1) {
            // Extract URI segments and parameters
            uriSegments = uri.substring(1, questionMarkIndex).split("/");
            String paramString = uri.substring(questionMarkIndex + 1);
            String[] paramPairs = paramString.split("&");
            for (String pair : paramPairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length != 2) {
                    throw new IllegalArgumentException("Invalid parameter: " + pair);
                }
                try {
                    // Decode parameter key and value
                    String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
                    String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name());
                    parameters.put(key, value);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid URL encoding: " + pair, e);
                }
            }
        } else {
            // No parameters, just URI segments
            uriSegments = uri.substring(1).split("/");
        }

        // Handle headers
        String line;
        Map<String, String> headers = new HashMap<>();
        while (reader.ready() && (line = reader.readLine()) != null && !line.isEmpty()) {
            int colonIndex = line.indexOf(':');
            if (colonIndex != -1) {
                // Extract header name and value
                String headerName = line.substring(0, colonIndex).trim();
                String headerValue = line.substring(colonIndex + 1).trim();
                headers.put(headerName, headerValue);
            }
        }

        // Read optional content length
        int contentLength = 0;
        if (headers.containsKey("Content-Length")) {
            try {
                contentLength = Integer.parseInt(headers.get("Content-Length"));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid Content-Length header: " + headers.get("Content-Length"));
            }
        }

        // Read content if present
        byte[] content = new byte[0];
        if (contentLength > 0) {
            char[] buffer = new char[contentLength];
            int read = reader.read(buffer, 0, contentLength);
            content = new String(buffer, 0, read).getBytes(StandardCharsets.UTF_8);
        }

        // Return the RequestInfo object with parsed data
        return new RequestInfo(method, uri, uriSegments, parameters, content);
    }

    // Inner class to store request information
    public static class RequestInfo {
        private final String httpCommand; // HTTP method (e.g., GET, POST)
        private final String uri; // Request URI
        private final String[] uriSegments; // URI segments
        private final Map<String, String> parameters; // URL parameters
        private final byte[] content; // Request content

        // Constructor to initialize RequestInfo fields
        public RequestInfo(String httpCommand, String uri, String[] uriSegments, Map<String, String> parameters,
                byte[] content) {
            this.httpCommand = httpCommand;
            this.uri = uri;
            this.uriSegments = uriSegments;
            this.parameters = parameters;
            this.content = content;
        }

        // Getters for the RequestInfo fields
        public String getHttpCommand() {
            return httpCommand;
        }

        public String getUri() {
            return uri;
        }

        public String[] getUriSegments() {
            return uriSegments;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public byte[] getContent() {
            return content;
        }
    }
}
