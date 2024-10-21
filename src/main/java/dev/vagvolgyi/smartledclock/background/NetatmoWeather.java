package dev.vagvolgyi.smartledclock.background;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.prefs.Preferences;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class NetatmoWeather implements Runnable {
    private static final String DEVICE_ID = "<device_id>";
    private static final String CLIENT_ID = "<client_id>";
    private static final String CLIENT_SECRET = "<client_secret>";
    private static final String INITIAL_REFRESH_TOKEN = "<initial_refresh_token>";

    private static final Preferences modulePreferences = Preferences.userRoot().node("SmartLedClock/modules/NetatmoWeather");

    private Float indoorTemp;
    private Float outdoorTemp;

    @Override
    public void run() {
        try {
            refreshWeatherData();
        }
        catch(IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Failed to refresh weather data", e);
        }

    }

    public Float getIndoorTemp() {
        return indoorTemp;
    }

    public Float getOutdoorTemp() {
        return outdoorTemp;
    }

    private void refreshWeatherData() throws IOException, InterruptedException {
        String accessToken = getAccessToken();

        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create("https://api.netatmo.com/api/getstationsdata?device_id=" + DEVICE_ID + "&get_favorites=false"))
                                         .header("Authorization", "Bearer " + accessToken)
                                         .timeout(Duration.ofSeconds(5))
                                         .build();

        try(HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonObject body = jsonObject.getAsJsonObject("body");
                JsonObject devices = body.getAsJsonArray("devices").get(0).getAsJsonObject();
                JsonObject dashboardData = devices.getAsJsonObject("dashboard_data");
                JsonObject modules = devices.getAsJsonArray("modules").get(0).getAsJsonObject();
                JsonObject moduleData = modules.getAsJsonObject("dashboard_data");

                indoorTemp = dashboardData.get("Temperature").getAsFloat();
                outdoorTemp = moduleData.get("Temperature").getAsFloat();
            }
            else {
                throw new IllegalStateException("Failed to refresh weather data: " + response.body());
            }
        }
    }

    private String getAccessToken() throws IOException, InterruptedException {
        String refreshToken = getRefreshToken();

        String formData = String.format(
                "grant_type=%s&refresh_token=%s&client_id=%s&client_secret=%s",
                URLEncoder.encode("refresh_token", StandardCharsets.UTF_8),
                URLEncoder.encode(refreshToken, StandardCharsets.UTF_8),
                URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8),
                URLEncoder.encode(CLIENT_SECRET, StandardCharsets.UTF_8)
        );

        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create("https://api.netatmo.com/oauth2/token"))
                                         .POST(BodyPublishers.ofString(formData))
                                         .header("Content-Type", "application/x-www-form-urlencoded")
                                         .timeout(Duration.ofSeconds(5))
                                         .build();

        try(HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                storeRefreshToken(jsonObject.get("refresh_token").getAsString());
                return jsonObject.get("access_token").getAsString();
            }
            else {
                throw new IllegalStateException("Failed to get access token: " + response.body());
            }
        }
    }

    private static String getRefreshToken() {
        return modulePreferences.get("refreshToken", INITIAL_REFRESH_TOKEN);
    }

    private static void storeRefreshToken(String refreshToken) {
        if(refreshToken != null && !refreshToken.isEmpty()) {
            modulePreferences.put("refreshToken", refreshToken);
        }
    }
}