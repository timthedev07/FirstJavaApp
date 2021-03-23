package Helper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CurrencyUpdate {

    private static final String url_str5 = "https://v6.exchangerate-api.com/v6/cacfc8493cb9155628add7c0/latest/";

    public static final String CONSOLE_RED = "\u001B[31m";
    public static final String CONSOLE_GREEN = "\u001B[32m";
    public static final String CONSOLE_YELLOW = "\u001B[33m";
    public static final String CONSOLE_RESET = "\u001B[0m";


    /**
     * <h3>A simple method that make API calls to update the currency database.</h3>
     */
    public static void update(){
        System.out.println(CONSOLE_YELLOW+"Updating database..."+CONSOLE_RESET);
        // First get the currency list and convert the symbols to strings
        Set<Currency> availableCurrencyBuffer = Currency.getAvailableCurrencies();
        int currencySize = availableCurrencyBuffer.size();
        List<String> currStrings = new ArrayList<>(currencySize);
        for(Currency tmp : availableCurrencyBuffer){
            currStrings.add(tmp.toString());
        }
        String[] availableCurrency = new String[currencySize];
        currStrings.toArray(availableCurrency);

        // Connect to the database
        Connection conn = DBConn.connection("currency");
        PreparedStatement ps = null;

        // Reset the table before doing anything else,
        PreparedStatement reset = null;
        String resetCommand = "DELETE FROM rate";
        try {
            reset = conn.prepareStatement(resetCommand);
            reset.execute();
        } catch (SQLException dude) {
            dude.printStackTrace();
        } finally {
            try {
                reset.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        // iterating through all of the currency symbols
        for (String curr : currStrings) {
            // Making Request
            try {
                // Trying to set url
                URL url = null;
                try {
                    url = new URL(url_str5 + curr);
                } catch (MalformedURLException e) {

                }
                // Trying to make request
                HttpURLConnection request = null;
                try {
                    request = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    System.out.println("Failed to make request");
                }
                // Try to connect
                try {
                    request.connect();
                } catch (IOException e) {

                }
                // Convert to JSON
                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                JsonObject jsonobject = root.getAsJsonObject();

                // Accessing the conversion
                JsonObject req_result = jsonobject.get("conversion_rates").getAsJsonObject();
                // Iterate over all of the symbols
                for (String curr_str2 : currStrings){
                    try {

                        String command = "INSERT INTO rate(from_symbol, to_symbol, rate) VALUES (?,?,?)";
                        ps = conn.prepareStatement(command);
                        ps.setString(1, curr);
                        ps.setString(2, curr_str2);
                        ps.setFloat(3, req_result.get(curr_str2).getAsFloat());
                        ps.execute();
                    } catch(Exception ee){
                    }
                }
                ps = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = format.format(new Date());
                String command2 = "UPDATE logs SET date=? WHERE 1=1";
                ps = conn.prepareStatement(command2);
                ps.setString(1, dateString);
                ps.execute();

            } catch (Exception r) {
            }
        }
        try {
            ps.close();
            conn.close();
            System.out.println(CONSOLE_GREEN+"Finish updating currency database"+CONSOLE_RESET);
        } catch(SQLException eee){

        }
        // no return value is needed.
    }
}
