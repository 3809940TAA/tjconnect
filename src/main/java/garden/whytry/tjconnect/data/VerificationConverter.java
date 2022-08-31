package garden.whytry.tjconnect.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class VerificationConverter {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Verification convertToVerification(String content) throws RuntimeException {
        try {
            return MAPPER.readValue(content, Verification.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
