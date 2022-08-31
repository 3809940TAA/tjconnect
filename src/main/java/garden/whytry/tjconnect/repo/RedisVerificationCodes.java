package garden.whytry.tjconnect.repo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

public final class RedisVerificationCodes implements VerificationCodes {
    private static final int EXPIRE_TIME = 15 * 60; // in seconds

    private final JedisPool pool;

    public RedisVerificationCodes(JedisPool pool) {
        this.pool = pool;
    }

    @Override
    public void add(String user, String code) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(user, code, SetParams.setParams().ex(EXPIRE_TIME));
        }
    }

    @Override
    public boolean verify(String user, String candidate) {
        try (Jedis jedis = pool.getResource()) {
            String value = jedis.get(user);
            return value != null && value.equals(candidate);
        }
    }

    @Override
    public void delete(String user) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(user);
        }
    }
}
