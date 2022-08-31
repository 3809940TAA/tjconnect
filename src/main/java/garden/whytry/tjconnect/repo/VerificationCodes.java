package garden.whytry.tjconnect.repo;

public interface VerificationCodes {
    void add(String user, String code);

    boolean verify(String user, String candidate);

    void delete(String user);
}
