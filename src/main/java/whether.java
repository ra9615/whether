import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class whether {
    private static final int CONNECTION_TIMEOUT = 30000;

    public static void main(String[] args) throws IOException {
        final URL url = new URL("https://api.weather.yandex.ru/v2/forecast?lat=55.75&lon=37.62&limit=7");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("X-Yandex-Weather-Key", "2fe68ccc-cf08-4dc2-a3e9-cf847e39fede");
        con.setConnectTimeout(CONNECTION_TIMEOUT);
        con.setReadTimeout(CONNECTION_TIMEOUT);

        String result = getWhether(con);
        JSONObject jsonObject = new JSONObject(result);
        int temp = jsonObject.getJSONObject("fact").getInt("temp");
        JSONArray forecast = jsonObject.getJSONArray("forecasts");
        int sumTemp = 0;
        for (int i = 0; i < forecast.length(); i++) {
            sumTemp += forecast.getJSONObject(i).getJSONObject("parts").getJSONObject("day_short").getInt("temp");
        }
        int avgTemp = sumTemp / (forecast.length());

        System.out.println(result);
        System.out.println("Температура: " + temp);
        System.out.println("Средняя температура за 7 дней: " + avgTemp);
    }

    public static String getWhether(HttpURLConnection con) {
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
