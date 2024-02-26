package com.smartcollege.smartcollege;

import Encryption.Encryption;
import com.smartcollege.smartcollege.database.Database;
import com.smartcollege.smartcollege.database.Tokens;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.smartcollege.smartcollege.qrGenerator.generator.generateQRCode;

public class Dashboard{
    private Map<String, String> sessions = new HashMap<>();

    Dashboard(Stage stage) {
        if(HelloApplication.loggedin){
            try{
                openDashboard(stage);

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private void openDashboard(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        scene.getStylesheets().add(getClass().getResource("./src/main/resources/css/test.css").toExternalForm());
        stage.setTitle("Admin Dashboard");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        readyDatabaseIfPossible();
        //stage.get

        //start HTTP server
        startHttpserver();

        //start smtp server
        EmailSender.initialize();

    }
    private void startHttpserver(){

        try{
            HttpServer server = HttpServer.create(new InetSocketAddress(3000),0);
            // Create a handler that serves an HTML file
            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    String response;
                    String requestPath = exchange.getRequestURI().getPath();
                    String requestMethod = exchange.getRequestMethod();

                    //get login request
                    if ("/login".equals(requestPath) && "GET".equals(requestMethod)) {
                        File htmlFile = new File("./src/main/resources/htmlFiles/login.html"); // Adjust the path to your HTML file
                        if (htmlFile.exists() && htmlFile.isFile()) {
                            byte[] htmlBytes = Files.readAllBytes(htmlFile.toPath());
                            exchange.sendResponseHeaders(200, htmlBytes.length);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(htmlBytes);
                            }
                            return;
                        }
                    }

                    //login request
                    if("/login".equals(requestPath) && "POST".equals(requestMethod)){
                        // Handle login POST request
                        String requestBody = new String(exchange.getRequestBody().readAllBytes(),
                                StandardCharsets.UTF_8);
                        Map<String, String> formData = parseFormData(requestBody);

                        String username = formData.get("id");
                        String password = formData.get("password");
                        String Epassword = null;
                        try {
                            Epassword = Encryption.encrypt(password);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        //search logins
                        try{
                            String sql = "Select sid, username, password from login where username ='"+username+"' and password = '"+Epassword+"';";
                            PreparedStatement statement = Database.con.prepareStatement(sql);
                            ResultSet result = statement.executeQuery();
                            while(result.next()){
                                if(result.getString("username").equals(username)){
                                    String sessionId = UUID.randomUUID().toString();
                                    sessions.put(sessionId, username);
                                    exchange.getResponseHeaders().add("Set-Cookie", "sessionId=" + sessionId);
                                    exchange.getResponseHeaders().add("Set-Cookie", "sid="+ result.getInt("sid"));
                                    exchange.getResponseHeaders().add("Location", "/");
                                    exchange.sendResponseHeaders(302, -1); // 302 Found (redirect)
                                    return;
                                }
                            }
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                        return;
                    }

                    //request QR CODE handle
                    if("/qrcode".equals(requestPath) && "GET".equals(requestMethod)){
                        String sessionId = getSessionIdFromCookie(exchange);
                        if(sessionId != null && sessions.containsKey(sessionId)){

                            try {
                                String sid = getStdIdFromCookie(exchange);
                                String bid;
                                String sem;
                                String token = Tokens.generateNewToken();
                                String path = "./src/main/resources/images/QR.png";
                                String charset = "UTF-8";
                                //get bid and sem;
                                String sql = "SELECT batch.bid, batch.semester from students inner join batch on students.bid = batch.bid where students.sid = "+sid;
                                PreparedStatement statement = Database.con.prepareStatement(sql);
                                ResultSet result = statement.executeQuery();
                                result.next();

                                bid = result.getString("bid");
                                sem = result.getString("semester");

                                String data = "{\"sid\":"+sid+"\"bid\":"+bid+",\"semester\":\""+sem+"\", \"token\": \""+token+"\"}";
                                Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
                                hashMap.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.L);
                                generateQRCode(data, path, charset, hashMap, 200, 200);

                                //send qrcode image
                                Path imagePath  = Paths.get(path);
                                if(Files.exists(imagePath)){
                                    byte[] readBytes = Files.readAllBytes(imagePath);
                                    exchange.getResponseHeaders().set("Content-Type","image/png");
                                    exchange.sendResponseHeaders(200,readBytes.length);
                                    try(OutputStream os = exchange.getResponseBody()){
                                        os.write(readBytes);
                                    }
                                }else{
                                    System.out.println("Image not found");
                                }
                            } catch (WriterException e) {
                                throw new RuntimeException(e);
                            }catch (SQLException e){
                                e.printStackTrace();
                            }

                        }
                    }
                    //Student dashboard route
                    if (("/".equals(requestPath) || "/home".equals(requestPath)) && "GET".equals(requestMethod)) {
                        String sessionId = getSessionIdFromCookie(exchange);
                        if (sessionId != null && sessions.containsKey(sessionId)) {
                            File htmlFile = new File("./src/main/resources/htmlFiles/index.html"); // Adjust the path to your HTML file
                            if (htmlFile.exists() && htmlFile.isFile()) {
                                byte[] htmlBytes = Files.readAllBytes(htmlFile.toPath());
                                exchange.sendResponseHeaders(200, htmlBytes.length);
                                try (OutputStream os = exchange.getResponseBody()) {
                                    os.write(htmlBytes);
                                }
                                return;
                            }
                        }else{
                            exchange.getResponseHeaders().add("Location", "/login");
                            exchange.sendResponseHeaders(302, -1);
                        }

                    }



                    response = "404 Not Found";
                    exchange.sendResponseHeaders(404, response.length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            });
            server.start();
        }catch (IOException e){
            e.printStackTrace();;
        }
    }
    private String getSessionIdFromCookie(HttpExchange exchange) {
        String sessionId = null;
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split("; ");
            for (String cookie : cookies) {
                String[] keyValue = cookie.split("=");
                if (keyValue.length == 2 && "sessionId".equals(keyValue[0])) {
                    sessionId = keyValue[1];
                    break;
                }
            }
        }
        return sessionId;
    }
    private String getStdIdFromCookie(HttpExchange exchange) {
        String sessionId = null;
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split("; ");
            for (String cookie : cookies) {
                String[] keyValue = cookie.split("=");
                if (keyValue.length == 2 && "sid".equals(keyValue[0])) {
                    sessionId = keyValue[1];
                    break;
                }
            }
        }
        return sessionId;
    }
    private Map<String, String> parseFormData(String formData) {
        Map<String, String> dataMap = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                dataMap.put(key, value);
            }
        }
        return dataMap;
    }
    private void readyDatabaseIfPossible(){
        if(Database.checkDatabaseDetails()){
            JSONParser jsonParser = new JSONParser();
            try(FileReader reader = new FileReader("./src/main/resources/config/config.json")){
                Object obj = jsonParser.parse(reader);
                JSONObject json = (JSONObject) obj;
                String checkConnection = Database.getConnection(json.get("host").toString(),json.get("database").toString(),json.get("username").toString(), Encryption.decrypt(json.get("password").toString()),json.get("port").toString());
                if(Objects.equals(checkConnection, "connected")){
                   System.out.println("Connected to database.");
                }else{

                    System.out.println(checkConnection);
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{

        }
    }
}
