import com.lsx.jedis.util.JedisConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * @Author:lsx
 * @Date:2025/10/23
 * @Description:jedis-demo
 */

public class JedisTest {
    private Jedis jedis;

    @BeforeEach
    void setUp() {
        //jedis = new Jedis("192.168.100.128", 6379);
        jedis = JedisConnectionFactory.getJedis();
        jedis.auth("123456");
        jedis.select(0);
    }

    @Test
    void testString() {
        //存入数据
        String result = jedis.set("name", "虎哥");
        System.out.println("result = " + result);
        //获取数据
        String name = jedis.get("name");
        System.out.println("name = " + name);
    }

    @Test
    void testHash() {
        //插入hash数据
        jedis.hset("user:1", "name", "Jack");
        jedis.hset("user:1", "age", "18");

        //获取
        Map<String, String> map = jedis.hgetAll("user:1");
        System.out.println(map);
    }

    @AfterEach
    void tearDown() {
        if (jedis != null) {
            jedis.close();
        }
    }
}
