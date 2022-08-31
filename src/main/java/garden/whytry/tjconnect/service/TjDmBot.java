package garden.whytry.tjconnect.service;

import garden.whytry.tjconnect.data.User;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public final class TjDmBot implements DmBot {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final String apiToken;

    public TjDmBot(String apiToken) {
        this.apiToken = apiToken;
    }

    private static String generateId() {
        return String.valueOf(RANDOM.nextLong(1000000000L, 10000000000L));
    }

    private static String getTs() {
        Date date = new Date();
        long millis = date.getTime();
        return "%d.%d".formatted(millis / 1000, millis % 1000);
    }

    @Override
    public void sendVerificationCodeToUser(User user, String code) {
        var userId = user.id();
        Objects.requireNonNull(userId);

        var formData = Map.of(
                "channelId", userId,
                "text", code,
                "idTmp", generateId(),
                "ts", getTs()
        );

        var boundary = "00000000000000000000000000000";
        StringBuilder bodyBuilder = new StringBuilder();

        for (var entry : formData.entrySet()) {
            bodyBuilder.append("-----------------------------").append(boundary)
                    .append("\r\nContent-Disposition: form-data; name=\"").append(entry.getKey()).append("\"").append("\r\n\r\n")
                    .append(entry.getValue()).append("\r\n");
        }
        bodyBuilder.append("-----------------------------").append(boundary).append("-----------------------------\r\n");

        var body = bodyBuilder.toString();
        System.out.println(body);

        try {
            URL url = new URL("https://tjournal.ru/m/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=---------------------------" + boundary);
            conn.setRequestProperty("X-This-Is-Csrf", "THIS IS SPARTA!");
            conn.setRequestProperty("Origin", "https://tjournal.ru");
            conn.setRequestProperty("Cookie", "osnova-remember=" + apiToken);
            conn.setRequestProperty("Sec-Fetch-Mode", "cors");
            conn.setRequestProperty("credentials", "include");
            conn.setUseCaches(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(body);
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
