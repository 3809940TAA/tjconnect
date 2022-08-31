package garden.whytry.tjconnect.data;

import java.net.URI;

public record User(String profileUrl) {
    public String id() {
        var path = URI.create(profileUrl).getPath();
        if (path == null) {
            return null;
        }
        var si = path.lastIndexOf('/');
        if (si == -1) {
            return null;
        }
        var username = path.substring(si + 1);
        var di = username.indexOf('-');
        if (di == -1) {
            return null;
        }
        return username.substring(0, di);
    }
}
