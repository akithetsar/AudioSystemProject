package client;

import DTOs.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class JavaClient {

    private static final String BASE_URL = "http://localhost:8080/REST-API/api";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.print("Izaberite opciju: ");
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Greska: Unesite broj.");
                continue;
            }

            if (choice == 0) {
                System.out.println("Izlaz...");
                break;
            }

            handleChoice(choice, scanner);

            System.out.println("\n--- Pritisnite Enter za nastavak ---");
            scanner.nextLine();
        }
        scanner.close();
    }
    
    private static String parseJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return null;

        int valueStartIndex = keyIndex + searchKey.length();
        int valueEndIndex = json.indexOf(",", valueStartIndex);
        if (valueEndIndex == -1) { // It's the last key-value pair
            valueEndIndex = json.indexOf("}", valueStartIndex);
        }

        String value = json.substring(valueStartIndex, valueEndIndex).trim();
        if (value.startsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static String makeRequest(String urlString, String method, String jsonBody, int[] outStatusCode) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Accept", "application/json");

            if (jsonBody != null && !jsonBody.isEmpty()) {
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonBody.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            int responseCode = conn.getResponseCode();
            if (outStatusCode != null) outStatusCode[0] = responseCode;
            System.out.println("Status: " + responseCode);

            InputStream is = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();
            if (is == null) return "No response body.";

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            conn.disconnect();

        } catch (Exception e) {
            System.err.println("An error occurred during the request: " + e.getMessage());
            e.printStackTrace();
        }
        return response.toString();
    }


    private static void printMenu() {
        System.out.println("\n--- Meni ---");
        System.out.println("1. Kreiranje grada");
        System.out.println("2. Kreiranje korisnika");
        System.out.println("3. Promena email adrese za korisnika");
        System.out.println("4. Promena mesta za korisnika");
        System.out.println("5. Kreiranje kategorije");
        System.out.println("6. Kreiranje audio snimka");
        System.out.println("7. Promena naziva audio snimka");
        System.out.println("8. Dodavanje kategorije audio snimku");
        System.out.println("9. Kreiranje paketa");
        System.out.println("10. Promena mesečne cene za paket");
        System.out.println("11. Kreiranje pretplate korisnika na paket");
        System.out.println("12. Kreiranje slušanja audio snimka od strane korisnika");
        System.out.println("13. Dodavanje audio snimka u omiljene od strane korisnika");
        System.out.println("14. Kreiranje ocene korisnika za audio snimak");
        System.out.println("15. Menjanje ocene korisnika za audio snimak");
        System.out.println("16. Brisanje ocene korisnika za audio snimak");
        System.out.println("17. Brisanje audio snimka od strane korisnika koji ga je kreirao");
        System.out.println("18. Dohvatanje svih mesta");
        System.out.println("19. Dohvatanje svih korisnika");
        System.out.println("20. Dohvatanje svih kategorija");
        System.out.println("21. Dohvatanje svih audio snimaka");
        System.out.println("22. Dohvatanje kategorija za određeni audio snimak");
        System.out.println("23. Dohvatanje svih paketa");
        System.out.println("24. Dohvatanje svih pretplata za korisnika");
        System.out.println("25. Dohvatanje svih slušanja za audio snimak");
        System.out.println("26. Dohvatanje svih ocena za audio snimak");
        System.out.println("27. Dohvatanje liste omiljenih audio snimaka za korisnika");
        System.out.println("0. Izlaz");
    }

    private static void handleChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1: createCity(scanner); break;
            case 2: createUser(scanner); break;
            case 3: updateUserEmail(scanner); break;
            case 4: updateUserCity(scanner); break;
            case 5: createCategory(scanner); break;
            case 6: createTrack(scanner); break;
            case 7: updateTrackName(scanner); break;
            case 8: addCategoryToTrack(scanner); break;
            case 9: createPackage(scanner); break;
            case 10: updatePackagePrice(scanner); break;
            case 11: createSubscription(scanner); break;
            case 12: createListening(scanner); break;
            case 13: addTrackToFavorites(scanner); break;
            case 14: createRating(scanner); break;
            case 15: updateRating(scanner); break;
            case 16: deleteRating(scanner); break;
            case 17: deleteTrack(scanner); break;
            case 18: getCities(); break;
            case 19: getUsers(); break;
            case 20: getCategories(); break;
            case 21: getTracks(); break;
            case 22: getTrackCategories(scanner); break;
            case 23: getPackages(); break;
            case 24: getUserSubscriptions(scanner); break;
            case 25: getTrackListenings(scanner); break;
            case 26: getTrackRatings(scanner); break;
            case 27: getUserFavorites(scanner); break;
            default: System.out.println("Nepostojeca opcija.");
        }
    }

    // Operation 1
    private static void createCity(Scanner s) {
        System.out.println("--- Kreiranje grada ---");
        System.out.print("Unesite ime grada: ");
        String name = s.nextLine();
        String jsonBody = "{\"name\":\"" + name + "\"}";
        System.out.println("Body: " + makeRequest(BASE_URL + "/cities", "POST", jsonBody, null));
    }

    // Operation 2
    private static void createUser(Scanner s) {
        System.out.println("--- Kreiranje korisnika ---");
        UserDTO user = new UserDTO();
        System.out.print("Unesite ime: "); user.setName(s.nextLine());
        System.out.print("Unesite email: "); user.setEmail(s.nextLine());
        System.out.print("Unesite godinu rodjenja: "); user.setBirthYear(Integer.parseInt(s.nextLine()));
        System.out.print("Unesite pol (Male/Female): "); user.setGender(s.nextLine());
        System.out.print("Unesite ime grada: "); user.setCityName(s.nextLine());
        
        String jsonBody = String.format("{\"name\":\"%s\",\"email\":\"%s\",\"birthYear\":%d,\"gender\":\"%s\",\"cityName\":\"%s\"}",
                user.getName(), user.getEmail(), user.getBirthYear(), user.getGender(), user.getCityName());
        System.out.println("Body: " + makeRequest(BASE_URL + "/users", "POST", jsonBody, null));
    }

    // Operation 3 & 4
    private static void updateUserEmail(Scanner s) {
        System.out.println("--- Promena email adrese za korisnika ---");
        System.out.print("Unesite ID korisnika: "); String userId = s.nextLine();
        System.out.print("Unesite novi email: "); String email = s.nextLine();
        try {
            String url = BASE_URL + "/users/" + userId + "?email=" + URLEncoder.encode(email, "UTF-8");
            System.out.println("Body: " + makeRequest(url, "PUT", null, null));
        } catch (Exception e) { e.printStackTrace(); }
    }
    private static void updateUserCity(Scanner s) {
        System.out.println("--- Promena mesta za korisnika ---");
        System.out.print("Unesite ID korisnika: "); String userId = s.nextLine();
        System.out.print("Unesite novi grad: "); String city = s.nextLine();
        try {
            String url = BASE_URL + "/users/" + userId + "?city=" + URLEncoder.encode(city, "UTF-8");
            System.out.println("Body: " + makeRequest(url, "PUT", null, null));
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Operation 5
    private static void createCategory(Scanner s) {
        System.out.println("--- Kreiranje kategorije ---");
        System.out.print("Unesite naziv kategorije: ");
        String name = s.nextLine();
        String jsonBody = "{\"name\":\"" + name + "\"}";
        System.out.println("Body: " + makeRequest(BASE_URL + "/categories", "POST", jsonBody, null));
    }

    // Operation 18
    private static void getCities() {
        System.out.println("--- Dohvatanje svih mesta ---");
        String response = makeRequest(BASE_URL + "/cities", "GET", null, null);
        System.out.println(response);
        if (response == null || !response.trim().startsWith("[")) {
            System.out.println("Body: " + response);
            return;
        }

    }

    // Operation 19
    private static void getUsers() {
        System.out.println("--- Dohvatanje svih korisnika ---");
        String response = makeRequest(BASE_URL + "/users", "GET", null, null);
        if (response == null || !response.trim().startsWith("[")) {
            System.out.println("Body: " + response);
            return;
        }

        String[] userObjects = response.substring(1, response.length() - 1).split("\\},\\{");
        List<UserDTO> users = new ArrayList<>();
        for (String userStr : userObjects) {
            UserDTO user = new UserDTO();
            user.setName(parseJsonValue(userStr, "name"));
            user.setEmail(parseJsonValue(userStr, "email"));
            user.setBirthYear(Integer.parseInt(parseJsonValue(userStr, "birthYear")));
            user.setGender(parseJsonValue(userStr, "gender"));
            user.setCityName(parseJsonValue(userStr, "cityName"));
            users.add(user);
        }
        
        System.out.println("Parsed Body:");
        for(UserDTO user : users) {
            System.out.println(user);
        }
    }
    
    private static void createTrack(Scanner s) {
        System.out.println("--- Kreiranje audio snimka ---");
        System.out.print("Unesite naziv snimka: "); String name = s.nextLine();
        System.out.print("Unesite trajanje u sekundama: "); int duration = Integer.parseInt(s.nextLine());
        System.out.print("Unesite email vlasnika: "); String ownerEmail = s.nextLine();
        String uploadTime = dateFormat.format(new Date());

        String jsonBody = String.format("{\"name\":\"%s\",\"duration\":%d,\"uploadTime\":\"%s\",\"ownerName\":\"%s\",\"ownerEmail\":\"%s\"}",
                name, duration, uploadTime, "", ownerEmail);
        System.out.println("Body: " + makeRequest(BASE_URL + "/tracks", "POST", jsonBody, null));
    }

    private static void updateTrackName(Scanner s) {
        System.out.println("--- Promena naziva audio snimka ---");
        System.out.print("Unesite ID snimka: "); String trackId = s.nextLine();
        System.out.print("Unesite novi naziv: "); String name = s.nextLine();
        try {
            String url = BASE_URL + "/tracks/" + trackId + "?name=" + URLEncoder.encode(name, "UTF-8");
            System.out.println("Body: " + makeRequest(url, "PUT", null, null));
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private static void addCategoryToTrack(Scanner s) {
        System.out.println("--- Dodavanje kategorije audio snimku ---");
        System.out.print("Unesite ID snimka: "); String trackId = s.nextLine();
        System.out.print("Unesite naziv kategorije: "); String catName = s.nextLine();
        String jsonBody = "{\"name\":\"" + catName + "\"}";
        System.out.println("Body: " + makeRequest(BASE_URL + "/tracks/" + trackId + "/category", "POST", jsonBody, null));
    }

    private static void createPackage(Scanner s) {
        System.out.println("--- Kreiranje paketa ---");
        System.out.print("Unesite naziv paketa: "); String name = s.nextLine();
        System.out.print("Unesite mesecnu cenu: "); BigDecimal price = new BigDecimal(s.nextLine());
        String jsonBody = String.format("{\"name\":\"%s\",\"monthlyPrice\":%s}", name, price.toString());
        System.out.println("Body: " + makeRequest(BASE_URL + "/packages", "POST", jsonBody, null));
    }

    private static void updatePackagePrice(Scanner s) {
        System.out.println("--- Promena mesečne cene za paket ---");
        System.out.print("Unesite ID paketa: "); String packageId = s.nextLine();
        System.out.print("Unesite novu cenu: "); String price = s.nextLine();
        try {
            String url = BASE_URL + "/packages/" + packageId + "?price=" + URLEncoder.encode(price, "UTF-8");
            System.out.println("Body: " + makeRequest(url, "PUT", null, null));
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private static void createSubscription(Scanner s) {
        System.out.println("--- Kreiranje pretplate korisnika na paket ---");
        System.out.print("Unesite ID korisnika: "); String userId = s.nextLine();
        System.out.print("Unesite ID paketa: "); String packageId = s.nextLine();
        String jsonBody = String.format("{\"startDate\":\"%s\",\"pricePaid\":9.99}", dateFormat.format(new Date()));
        System.out.println("Body: " + makeRequest(BASE_URL + "/users/" + userId + "/subscriptions/" + packageId, "POST", jsonBody, null));
    }
    
    private static void createListening(Scanner s) {
        System.out.println("--- Kreiranje slušanja audio snimka od strane korisnika ---");
        System.out.print("Unesite ID korisnika: "); String userId = s.nextLine();
        System.out.print("Unesite ID snimka: "); String trackId = s.nextLine();
        String jsonBody = String.format("{\"startTime\":\"%s\",\"startSecond\":0,\"duration\":120}", dateFormat.format(new Date()));
        System.out.println("Body: " + makeRequest(BASE_URL + "/users/" + userId + "/listenings/" + trackId, "POST", jsonBody, null));
    }
    
    private static void addTrackToFavorites(Scanner s) {
        System.out.println("--- Dodavanje audio snimka u omiljene od strane korisnika ---");
        System.out.print("Unesite ID korisnika: "); String userId = s.nextLine();
        System.out.print("Unesite ID snimka: "); String trackId = s.nextLine();
        System.out.println("Body: " + makeRequest(BASE_URL + "/users/" + userId + "/favorites/" + trackId, "POST", null, null));
    }

    private static void createRating(Scanner s) {
        System.out.println("--- Kreiranje ocene korisnika za audio snimak ---");
        System.out.print("Unesite ID korisnika: "); String userId = s.nextLine();
        System.out.print("Unesite ID snimka: "); String trackId = s.nextLine();
        System.out.print("Unesite ocenu (1-5): "); char ratingVal = s.nextLine().charAt(0);
        String jsonBody = String.format("{\"rating\":\"%c\",\"timestamp\":\"%s\"}", ratingVal, dateFormat.format(new Date()));
        System.out.println("Body: " + makeRequest(BASE_URL + "/tracks/" + trackId + "/ratings/" + userId, "POST", jsonBody, null));
    }

    private static void updateRating(Scanner s) {
        System.out.println("--- Menjanje ocene korisnika za audio snimak ---");
        System.out.print("Unesite ID korisnika: "); String userId = s.nextLine();
        System.out.print("Unesite ID snimka: "); String trackId = s.nextLine();
        System.out.print("Unesite novu ocenu (1-5): "); char ratingVal = s.nextLine().charAt(0);
        String jsonBody = String.format("{\"rating\":\"%c\"}", ratingVal);
        System.out.println("Body: " + makeRequest(BASE_URL + "/tracks/" + trackId + "/ratings/" + userId, "PUT", jsonBody, null));
    }
    
    private static void deleteRating(Scanner s) {
        System.out.println("--- Brisanje ocene korisnika za audio snimak ---");
        System.out.print("Unesite ID korisnika: "); String userId = s.nextLine();
        System.out.print("Unesite ID snimka: "); String trackId = s.nextLine();
        System.out.println("Body: " + makeRequest(BASE_URL + "/tracks/" + trackId + "/ratings/" + userId, "DELETE", null, null));
    }
    
    private static void deleteTrack(Scanner s) {
        System.out.println("--- Brisanje audio snimka ---");
        System.out.print("Unesite ID snimka za brisanje: "); String trackId = s.nextLine();
        System.out.println("Body: " + makeRequest(BASE_URL + "/tracks/" + trackId, "DELETE", null, null));
    }
    
    private static void getCategories() {
        System.out.println("--- Dohvatanje svih kategorija ---");
        System.out.println("Body: " + makeRequest(BASE_URL + "/categories", "GET", null, null));
    }

    private static void getTracks() {
        System.out.println("--- Dohvatanje svih audio snimaka ---");
        System.out.println("Body: " + makeRequest(BASE_URL + "/tracks", "GET", null, null));
    }

    private static void getTrackCategories(Scanner s) {
        System.out.println("--- Dohvatanje kategorija za određeni audio snimak ---");
        System.out.print("Unesite ID snimka: "); String trackId = s.nextLine();
        System.out.println("Body: " + makeRequest(BASE_URL + "/tracks/" + trackId + "/categories", "GET", null, null));
    }

    private static void getPackages() {
        System.out.println("--- Dohvatanje svih paketa ---");
        System.out.println("Body: " + makeRequest(BASE_URL + "/packages", "GET", null, null));
    }

    private static void getUserSubscriptions(Scanner s) {
        System.out.println("--- Dohvatanje svih pretplata za korisnika ---");
        System.out.print("Unesite ID korisnika: "); String userId = s.nextLine();
        System.out.println("Body: " + makeRequest(BASE_URL + "/users/" + userId + "/subscriptions", "GET", null, null));
    }

    private static void getTrackListenings(Scanner s) {
        System.out.println("--- Dohvatanje svih slušanja za audio snimak ---");
        System.out.print("Unesite ID snimka: "); String trackId = s.nextLine();
        System.out.println("Body: " + makeRequest(BASE_URL + "/tracks/" + trackId + "/listenings", "GET", null, null));
    }

    private static void getTrackRatings(Scanner s) {
        System.out.println("--- Dohvatanje svih ocena za audio snimak ---");
        System.out.print("Unesite ID snimka: "); String trackId = s.nextLine();
        System.out.println("Body: " + makeRequest(BASE_URL + "/tracks/" + trackId + "/ratings", "GET", null, null));
    }

    private static void getUserFavorites(Scanner s) {
        System.out.println("--- Dohvatanje liste omiljenih audio snimaka za korisnika ---");
        System.out.print("Unesite ID korisnika: "); String userId = s.nextLine();
        System.out.println("Body: " + makeRequest(BASE_URL + "/users/" + userId + "/favorites", "GET", null, null));
    }
}