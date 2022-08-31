package garden.whytry.tjconnect.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class UserConverter {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public User convertToUser(String content) throws RuntimeException {
        try {
            return MAPPER.readValue(content, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
