package service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

public class LocalPDFServer {

    private static HttpServer server;

    private static final int PORT = 8080;

    /**
     * Starts the local PDF server.
     */
    public static void startServer() {

        if (server != null) {
            return;
        }

        try {

            server = HttpServer.create(
                    new InetSocketAddress(PORT),
                    0
            );

            server.createContext(
                    "/",
                    LocalPDFServer::handleRequest
            );

            server.setExecutor(null);

            server.start();

            System.out.println(
                    "Local PDF server started at "
                    + "http://localhost:" + PORT
            );

        } catch (IOException e) {

            System.err.println(
                    "Could not start local PDF server: "
                    + e.getMessage()
            );
        }
    }

    /**
     * Handles PDF requests.
     */
    private static void handleRequest(
            HttpExchange exchange) throws IOException {

        String requestedFile =
                exchange.getRequestURI().getPath();

        if (requestedFile.equals("/")
                || requestedFile.isEmpty()) {

            String message =
                    "Sunrise Dental Clinic PDF Server";

            byte[] response =
                    message.getBytes("UTF-8");

            exchange.getResponseHeaders().set(
                    "Content-Type",
                    "text/plain; charset=UTF-8"
            );

            exchange.sendResponseHeaders(
                    200,
                    response.length
            );

            try (OutputStream os =
                    exchange.getResponseBody()) {

                os.write(response);
            }

            return;
        }

        // Remove /
        String fileName =
                requestedFile.substring(1);

        // Only PDF files are allowed
        if (!fileName
                .toLowerCase()
                .endsWith(".pdf")) {

            sendError(
                    exchange,
                    403,
                    "Only PDF files are allowed."
            );

            return;
        }

        File pdfFile =
                new File(
                        System.getProperty("user.dir"),
                        fileName
                );

        if (!pdfFile.exists()
                || !pdfFile.isFile()) {

            sendError(
                    exchange,
                    404,
                    "PDF file not found."
            );

            return;
        }

        byte[] pdfData =
                Files.readAllBytes(
                        pdfFile.toPath()
                );

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/pdf"
        );

        exchange.getResponseHeaders().set(
                "Content-Disposition",
                "inline; filename=\""
                + pdfFile.getName()
                + "\""
        );

        exchange.sendResponseHeaders(
                200,
                pdfData.length
        );

        try (OutputStream os =
                exchange.getResponseBody()) {

            os.write(pdfData);
        }
    }

    /**
     * Sends an error response.
     */
    private static void sendError(
            HttpExchange exchange,
            int status,
            String message)
            throws IOException {

        byte[] response =
                message.getBytes("UTF-8");

        exchange.getResponseHeaders().set(
                "Content-Type",
                "text/plain; charset=UTF-8"
        );

        exchange.sendResponseHeaders(
                status,
                response.length
        );

        try (OutputStream os =
                exchange.getResponseBody()) {

            os.write(response);
        }
    }

    /**
     * Returns the localhost URL for a PDF.
     */
    public static String getPDFUrl(
            String fileName) {

        return "http://localhost:"
                + PORT
                + "/"
                + fileName;
    }
}