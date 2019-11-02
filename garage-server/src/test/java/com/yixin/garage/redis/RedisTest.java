package com.yixin.garage.redis;

import com.yixin.garage.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 
使用时注意事项：
　　　当你的redis数据库里面本来存的是字符串数据或者你要存取的数据就是字符串类型数据的时候，那么你就使用StringRedisTemplate即可。
　　　但是如果你的数据是复杂的对象类型，而取出的时候又不想做任何的数据转换，直接从Redis里面取出一个对象，那么使用RedisTemplate是更好的选择
 *
 */
public class RedisTest extends BaseTest {
	/**
	 * StringRedisTemplate使用的是StringRedisSerializer
	 */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;//操作key-value都是字符串
    /**
     *RedisTemplate使用的是JdkSerializationRedisSerializer ,
	 * 存入数据会将数据先序列化成字节数组然后在存入Redis数据库。 
     */
    @Autowired
    private RedisTemplate redisTemplate;//操作key-value都是对象

    /**
     *  Redis常见的五大数据类型：
     *  stringRedisTemplate.opsForValue();[String(字符串)]
     *  stringRedisTemplate.opsForList();[List(列表)]
     *  stringRedisTemplate.opsForSet();[Set(集合)]
     *  stringRedisTemplate.opsForHash();[Hash(散列)]
     *  stringRedisTemplate.opsForZSet();[ZSet(有序集合)]
     */
    @Test
    public void test(){
    	
        stringRedisTemplate.opsForValue().append("msg","hello");//在原有的值基础上新增字符串到末尾。
        String userNum = stringRedisTemplate.opsForValue().get("userNum");
        if(StringUtils.isBlank(userNum)){
            stringRedisTemplate.opsForValue().set("userNum", "userNum001");
        }
        stringRedisTemplate.opsForValue().set("test", "100",60*10,TimeUnit.SECONDS);//向redis里存入数据和设置缓存时间  
        stringRedisTemplate.opsForValue().getAndSet("test", "test1");// 获取原来key键对应的值并重新赋新值
        stringRedisTemplate.boundValueOps("test").increment(-1);//val做-1操作

        stringRedisTemplate.opsForValue().get("test");//根据key获取缓存中的val

        stringRedisTemplate.boundValueOps("test").increment(1);//val +1

        stringRedisTemplate.getExpire("test");//根据key获取过期时间

        stringRedisTemplate.getExpire("test",TimeUnit.SECONDS);//根据key获取过期时间并换算成指定单位 

        stringRedisTemplate.delete("test");//根据key删除缓存

        stringRedisTemplate.hasKey("546545");//检查key是否存在，返回boolean值 

        stringRedisTemplate.opsForSet().add("red_123", "1","2","3");//向指定key中存放set集合

        stringRedisTemplate.expire("red_123",1000 , TimeUnit.MILLISECONDS);//设置过期时间

        stringRedisTemplate.opsForSet().isMember("red_123", "1");//根据key查看集合中是否存在指定数据

        stringRedisTemplate.opsForSet().members("red_123");//根据key获取set集合
    }
}
