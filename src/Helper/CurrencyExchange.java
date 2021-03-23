package Helper;

import Helper.CurrencyUpdate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyExchange {

    /**
     * Takes a parameter to indicate whether or not to update the database base on the latest exchange rate.
     * @param updateDatabase
     */
    public CurrencyExchange(boolean updateDatabase){
        if (updateDatabase){
            CurrencyUpdate.update();
        }

    }

    public static Float convert(float amount, String fromSymbol, String toSymbol){
        Connection conn = DBConn.connection("currency");
        String command = "SELECT rate FROM rate WHERE from_symbol=? AND to_symbol=?";
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            ps = conn.prepareStatement(command);
            ps.setString(1, fromSymbol);
            ps.setString(2, toSymbol);
            res = ps.executeQuery();
            float finalRes = amount * res.getFloat("rate");//(Float)res.get(fromSymbol).get(toSymbol);
            res.close();
            ps.close();
            conn.close();
            return finalRes;

        } catch (SQLException error) {
            error.printStackTrace();

            return 0.0F;
        }

    }

}
