package garden.whytry.tjconnect.service;

import garden.whytry.tjconnect.data.User;

public interface DmBot {
    void sendVerificationCodeToUser(User user, String code);
}
