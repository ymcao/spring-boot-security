package com.mobile2016.common.config;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;

/**
 * redis 的操作开放接口
 * 
 * @author houge
 * 
 */
public interface IRedisDao {

	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	Boolean del(String key);

	/**
	 * 通过keys删除
	 * 
	 * @param key
	 */
	void del(List<String> keys);

	/**
	 * 给key设置生存时间
	 * 
	 * @param key
	 * @param timeOut
	 *            单位 秒
	 * @return
	 */
	boolean expire(String key, long timeOut);

	/**
	 * 获取还剩多少存货时间
	 * 
	 * @param key
	 * @return
	 */
	long ttl(String key);
	
	/**
	 * 
	 * 	将 key 中储存的数字值增一。

		如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。

		如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
		
	 * @param key
	 * @return
	 */
	Long incr(String key);

	/**
	 * 添加key value 并且设置存活时间
	 * 
	 * @param key
	 * @param value
	 * @param timeOut
	 *            单位秒
	 */
	void set(String key, String value, long timeOut);

	/**
	 * 添加key value
	 * 
	 * @param key
	 * @param value
	 */
	void set(String key, String value);

	/**
	 * 获取redis value (String)
	 * 
	 * @param key
	 * @return
	 */
	String get(String key);

	/**
	 * 通过正则匹配keys
	 * 
	 * @param pattern
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Set keys(String pattern);

	/**
	 * 检查key是否已经存在
	 * 
	 * @param key
	 * @return
	 */
	boolean exists(String key);

	/**
	 * 检查集合是否存在数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	boolean exists(String key, String value);

	/**
	 * 清空redis 所有数据
	 * 
	 * @return
	 */
	String flushDB();

	/**
	 * 查看redis里有多少数据
	 */
	long dbSize();

	/**
	 * 检查是否连接成功
	 * 
	 * @return
	 */
	String ping();

	/**
	 * set add
	 * 
	 * @param key
	 * @param value
	 * @return
	 */

	Long sadd(String key, String value);

	/**
	 * zset
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	boolean zadd(String key, String value, double score);
	
	/**
	 * zset
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	Long zremrangebyscore(String key, double min, double max);
	
	Long zrem(String key, String... member);
	
	Set<Tuple> zrange(String key, long start, long stop);
	
	Set<Tuple> zrangebyscore(String key, long minScore, long maxScore);
	
	Long zcount(String key, double minScore, double maxScore);
	
	/**
	 * list
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	long lpush(String key, String value);

	/**
	 * list
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	long rpush(String key, String value);

	/**
	 * map
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	String hget(String key, String field);

	/**
	 * map
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	boolean hset(String key, String field, String value);

	/**
	 * map
	 * 
	 * @param key
	 * @param map
	 */
	boolean hmset(String key, Map<String, String> map);

	/**
	 * map
	 * 
	 * @param key
	 * @param fields
	 * @return
	 */
	List<String> hmget(String key, List<String> fields);

	/**
	 * map
	 * 
	 * @param key
	 * @return
	 */
	Map<String, String> hgetAll(String key);

	/**
	 * map
	 * 
	 * @param key
	 * @return
	 */
	List<String> sort(String key);

	/**
	 * map
	 * 
	 * @param key
	 * @return
	 */
	long size(String key);

	/**
	 * 获取集合元素某个,适应zset,list
	 * 
	 * @param key
	 * @param begin
	 * @param end
	 * @return
	 */
	List<String> list(String key, long begin, long end);

	/**
	 * 获取集合元素某个,适应zset,list
	 * 
	 * @param key
	 * @param begin
	 * @param end
	 * @return
	 */
	Set<String> set(String key, long begin, long end);

	/**
	 * 获取集合,结果list，适应于list,set,zset
	 * 
	 * @param key
	 * @return
	 */
	List<String> list(String key);

	/**
	 * 获取集合,结果set,可去重，适应于list,set,zset
	 * 
	 * @param key
	 * @return
	 */
	Set<String> set(String key);

	/**
	 * 删除集合元素
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	Long del(String key, String value);

}
