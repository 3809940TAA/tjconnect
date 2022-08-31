package garden.whytry.tjconnect;

import garden.whytry.tjconnect.data.User;
import garden.whytry.tjconnect.data.UserConverter;
import garden.whytry.tjconnect.data.Verification;
import garden.whytry.tjconnect.data.VerificationConverter;
import garden.whytry.tjconnect.repo.RedisVerificationCodes;
import garden.whytry.tjconnect.service.TjDmBot;
import garden.whytry.tjconnect.util.VerificationCodeGenerator;
import io.javalin.Javalin;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class App {
    public static void main(String[] args) {
        var verificationCodes = new RedisVerificationCodes(new JedisPool(new JedisPoolConfig(), "localhost", 6379));
        var userConverter = new UserConverter();
        var verificationConverter = new VerificationConverter();
        var botVerifier = new TjDmBot("YOUR_API_TOKEN");

        Javalin app = Javalin.create().start(7070);
        app.post("/send_code", context -> {
            String body = context.body();
            User user = userConverter.convertToUser(body);
            if (user == null) {
                context.status(400);
                return;
            }
            var userId = user.id();
            if (userId == null) {
                context.status(400);
                return;
            }
            var code = VerificationCodeGenerator.new4DigitsCode();
            // send the code to the user and save it in Redis
            botVerifier.sendVerificationCodeToUser(user, code);
            verificationCodes.add(user.profileUrl(), code);
            context.status(200);
        });
        app.get("/verify_code", context -> {
            Verification verification = verificationConverter.convertToVerification(context.body());
            if (verification == null) {
                context.status(400);
                return;
            }
            boolean verified = verificationCodes.verify(verification.profileUrl(), verification.code());
            if (verified) {
                context.status(200);
                verificationCodes.delete(verification.profileUrl());
            } else {
                context.status(401);
            }
        });
    }
}
