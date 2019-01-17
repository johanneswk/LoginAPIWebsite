package nl.johanneswk.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonParser;


@WebServlet(name = "ServletSample", urlPatterns = {"/flight-info"})

public class ServletLogin extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // Leest het html form uit, en zet gegevens in strings
        String ticketNummer = request.getParameter("ticket-nummer");
        String email = request.getParameter("email");
        String wachtwoord = request.getParameter("wachtwoord");
        String vluchtnummer = request.getParameter("vlucht-nummer");

        // Zorgt dat je html code in de 'out' variabele zet om er een pagina van te maken
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<!DOCTYPE html>");
            out.println("<html><head>");
            out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=windows-1252\">");
            out.println("<link rel=\"stylesheet\" href=\"css/styles.css\"></head>");
            out.println("<body>");
            out.println("<div class=\"topnav\" style=\"text-align: center;\">");
            out.println("<a href=\"index.jsp\" style=\"color: #040504;\">Home<img src=\"images/Nederlandsevlag.png\"" +
                    " style=\"width: 27px; height: 19px; margin-left: 125px;\"></a>");
            out.println("<a href=\"Informatie_contact.jsp\" style=\"color: #040504;\">Service &amp; Contact</a>");
            out.println("<a href=\"#\" style=\"margin-left: 141px; width: 131px; " +
                    "color: #040504;\">Veilig boeken</a>");
            out.println("<a href=\"#\" style=\"color: #040504;\">Gratis annuleren</a>");
            out.println("<a href=\"#\" style=\"color: #040504;\">Verantwoord reizen</a>");
            out.println("<a href=\"Mijn-Vlucht.jsp\" style=\"color: #040504;\">Mijn-Vlucht</a>");
            out.println("</div>");
            out.println("<div style=\"text-align: center;\"> <a href=\"index.jsp\">" +
                    "<img src=\"images/test.png\" style=\"width: 224px; height: 224px;\"></a></div>");

            // Checkt of de gebruiker het juiste ticketnummer en wachtwoord invult
            if (ticketNummer.equals("00001") && wachtwoord.equals("test")) {
                out.println("<h1>Welkom " + email + " op Mijn Vlucht</h1>");  // Toont welkom + email

                out.println("<h2>Vlucht informatie:</h2>");
                out.println("<p>" + vluchtnummer + "</p>"); // Toont het vluchtnummer

                /* Maakt http verbinding met schiphol API doormiddel van URL waarin een id en
                wachtwoord van de Schiphol API verwerkt zit. Vluchtnummer moet je op de juiste plaats
                mee geven. Deze verzend je samen met de Header waarin je aangeeft versie 3 te gebruiken
                */

                String id = "";
                String key = "";
                HttpClient httpClient = HttpClients.createDefault();
                HttpGet requestJson = new HttpGet("https://api.schiphol.nl/public-flights/flights?app_id=" +
                        id + "&app_key=" + key + "&flightname=" + vluchtnummer);
                requestJson.addHeader("ResourceVersion", "v3");

                // Je krijgt Json terug en stopt deze in een string en print hem
                HttpResponse responseJson = httpClient.execute(requestJson);
                String responseBody = EntityUtils.toString(responseJson.getEntity(), "UTF-8");
                System.out.println(responseBody);

                // De string stop je in een JsonObject
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = (JsonObject) parser.parse(responseBody);

                // Zet JsonObject om naar Array om in te zoomen op flights, en weer om naar Object om '{}' weg te halen
                JsonArray vluchtInfoArray = (JsonArray) jsonObject.get("flights");
                JsonObject vluchtInfoObject = vluchtInfoArray.get(0).getAsJsonObject();

                // Maakt variabelen
                String gate = "";
                String terminal = "";
                String flightDirection = "";
                String landingTime = "";
                String hourLanding = "";
                String minLanding = "";
                String secLanding = "";
                String gateClosing ="";
                String hourGate = "";
                String minGate = "";
                String secGate ="";


                // Test gate
                boolean testGate = vluchtInfoObject.get("gate").isJsonNull();
                if (testGate) {
                    // Geeft error als gate info niet beschikbaar is
                    out.println("<p style=\"color:red;\">Error getting Gate</p>");
                }
                else {
                    gate = vluchtInfoObject.get("gate").getAsString(); // Laat gate zien
                }

                // Test terminal
                boolean testTerminal = vluchtInfoObject.get("terminal").isJsonNull();
                if (testTerminal) {
                    // Geeft error als terminal info niet beschikbaar is
                    out.println("<p style=\"color:red;\">Error getting Terminal</p>");
                }
                else {
                    terminal = vluchtInfoObject.get("terminal").getAsString(); // Laat terminal zien
                }

                // Test flightdirection
                boolean testFlightDirection = vluchtInfoObject.get("flightDirection").isJsonNull();
                if (testFlightDirection) {
                    // Geeft error als richting info niet beschikbaar is
                    out.println("<p style=\"color:red;\">Error getting flight direction</p>");
                }
                else {
                    flightDirection = vluchtInfoObject.get("flightDirection").getAsString(); // Laat richting zien
                }

                // Test landingtime
                boolean testLandingTimeLong = vluchtInfoObject.get("estimatedLandingTime").isJsonNull();
                if (testLandingTimeLong) {
                    if (flightDirection.equals("A")) {
                        // Geeft error als landings info niet beschikbaar is
                        out.println("<p style=\"color:red;\">Error getting estimated landing time</p>");
                    }
                    else if (flightDirection.equals("D")) {
                        // Staat landingstijd over bij vertrek
                        out.println("");
                    }
                    else {
                        // Geeft error als gate info niet beschikbaar is
                        out.println("<p style=\"color:red;\">Error getting estimated landing time</p>");
                    }
                }

                else {
                    // Geeft verwachte landingstijd
                    String landingTimeLong = vluchtInfoObject.get("estimatedLandingTime").getAsString();
                    landingTime = landingTimeLong.substring(landingTimeLong.indexOf("T"), landingTimeLong.indexOf("."));
                    hourLanding = landingTime.substring(1, 3);
                    minLanding = landingTime.substring(4, 6);
                    secLanding = landingTime.substring(7, 9);
                }

                // Test Gate closing time
                boolean testGateClosing = vluchtInfoObject.get("expectedTimeGateClosing").isJsonNull();
                if (testGateClosing) {
                    if (flightDirection.equals("D")) {
                        // Geeft error als gate sluitings info niet beschikbaar is
                        out.println("<p style=\"color:red;\">Error getting estimated Gate closing time</p>");
                    }
                    else if (flightDirection.equals("A")) {
                        // Slaat gate sluitingstijd over als het een arival is.
                        out.println("");
                    }
                    else {
                        // Geeft error als gate sluitingstijd info niet beschikbaar is
                        out.println("<p style=\"color:red;\">Error getting estimated Gate closing time</p>");

                    }
                }
                else {
                    // Geeft verwachte gate sluitingstijd
                    String gateClosingLong = vluchtInfoObject.get("expectedTimeGateClosing").getAsString();
                    gateClosing = gateClosingLong.substring(gateClosingLong.indexOf("T"), gateClosingLong.indexOf("."));
                    hourGate = gateClosing.substring(1, 3);
                    minGate = gateClosing.substring(4, 6);
                    secGate = gateClosing.substring(7, 9);
                }

                // Gaat alle net gecheckte informatie printen bij de juiste vlucht
                out.println("<p>");
                if (flightDirection.equals("A")) {
                    out.println("Verwachte aankomst Schiphol: ");
                    out.println(hourLanding + ":" + minLanding + ":" + secLanding);
                } else if (flightDirection.equals("D")) {
                    out.println("Verwachte sluitingstijd Gate: ");
                    out.println(hourGate + ":" + minGate + ":" + secGate);
                } else {
                    out.println("Error");
                }
                out.println("<br>Terminal: " + terminal + "<br>Gate: " + gate + "<br></p>");



            } else {
                try {
                    // Laat Java 2 sec wachten
                    Thread.sleep(2000);
                } catch (Exception e) {
                    out.println("Error: " + e.toString());
                }
                // Redirect opnieuw naar inlogpagina en geeft error
                request.setAttribute("error", "Onjuiste gegevens ingevoerd!");
                request.getRequestDispatcher("Mijn-Vlucht.jsp").forward(request, response);
            }
            // Sluit HTML pagina af
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

}





