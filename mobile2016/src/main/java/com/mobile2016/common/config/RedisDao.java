package com.mobile2016.common.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class RedisDao implements IRedisDao {

	@Autowired
	private RedisTemplate<String, ?> redisTemplate;

	private static final String redisCode = "utf-8";

	private final byte[] rawKey(String key) {
		return key.getBytes(Charset.forName(redisCode));
	}

	private final byte[][] rawKeys(Collection<String> keys) {
		final byte[][] rawKeys = new byte[keys.size()][];

		int i = 0;
		for (String key : keys) {
			rawKeys[i++] = rawKey(key);
		}
		return rawKeys;
	}

	private final String stringOf(byte[] value) {
		if (value == null) {
			return "";
		}
		try {
			return new String(value, redisCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}


	/**
	 * @param key
	 */
	@Override
	public Boolean del(final String key) {
		Boolean result = Boolean.TRUE;
		try {
			redisTemplate.delete(key);
		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
			e.printStackTrace();
			result = Boolean.FALSE;
		}
		return result;
	}

	/**
	 * @param key
	 */
	@Override
	public void del(final List<String> keys) {
		redisTemplate.delete(keys);
	}

	@Override
	public boolean expire(final String key, final long timeOut) {
		return redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
	}

	@Override
	public long ttl(final String key) {
		return (Long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) {
				return connection.ttl(rawKey(key));
			}
		});
	}

	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	private void set(final byte[] key, final byte[] value, final long timeOut) {
		redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) {
				connection.set(key, value);
				if (timeOut > 0) {
					connection.expire(key, timeOut);
				}
				return timeOut;
			}
		});
	}

	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	public void set(String key, String value, long timeOut) {
		this.set(rawKey(key), rawKey(value), timeOut);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		this.set(key, value, 0L);
	}

	/**
	 * @param key
	 * @return
	 */
	public String get(final String key) {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) {
				return stringOf(connection.get(rawKey(key)));
			}
		});
	}

	/**
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return (Boolean) redisTemplate.execute(new RedisCallback() {
			public Boolean doInRedis(RedisConnection connection) {
				return connection.exists(rawKey(key));
			}
		});
	}

	@Override
	public boolean exists(final String key, final String value) {
		return (Boolean) redisTemplate.execute(new RedisCallback() {
			public Boolean doInRedis(RedisConnection connection) {

				DataType type = connection.type(rawKey(key));

				if (type == DataType.HASH) {
					return connection.hExists(rawKey(key), rawKey(value));
				} else if (type == DataType.ZSET) {
					return connection.zScore(rawKey(key), rawKey(value)) == null ? false : true;
				} else if (type == DataType.SET) {
					return connection.sIsMember(rawKey(key), rawKey(value));
				}
				return false;
			}
		});
	}

	/**
	 * @return
	 */
	public String flushDB() {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) {
				connection.flushDb();
				return "ok";
			}
		});
	}

	/**
	 * @return
	 */
	public long dbSize() {
		return (Long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) {
				return connection.dbSize();
			}
		});
	}

	/**
	 * @return
	 */
	@Override
	public String ping() {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) {
				return connection.ping();
			}
		});
	}

	@Override
	public Set keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	@Override
	public Long sadd(final String key, final String value) {
		return (Long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) {
				return connection.sAdd(rawKey(key), rawKey(value));
			}
		});
	}

	@Override
	public boolean zadd(final String key, final String value, final double score) {
		return (Boolean) redisTemplate.execute(new RedisCallback() {
			public Boolean doInRedis(RedisConnection connection) {
				return connection.zAdd(rawKey(key), score, rawKey(value));
			}
		});
	}

	@Override
	public long lpush(final String key, final String value) {
		return (Long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) {
				return connection.lPush(rawKey(key), rawKey(value));
			}
		});
	}

	@Override
	public long rpush(final String key, final String value) {
		return (Long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) {
				return connection.rPush(rawKey(key), rawKey(value));
			}
		});
	}

	@Override
	public String hget(final String key, final String field) {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) {
				return stringOf(connection.hGet(rawKey(key), rawKey(field)));
			}
		});
	}

	@Override
	public boolean hset(final String key, final String field, final String value) {
		return (Boolean) redisTemplate.execute(new RedisCallback() {
			public Boolean doInRedis(RedisConnection connection) {
				try {
					connection.hSet(rawKey(key), rawKey(field), rawKey(value));
					return Boolean.TRUE;
				} catch (Exception e) {
//					logger.error(e.getMessage(), e);
					e.printStackTrace();
					return Boolean.FALSE;
				}
			}
		});
	}

	@Override
	public boolean hmset(final String key, final Map<String, String> map) {

		return (Boolean) redisTemplate.execute(new RedisCallback() {
			
			public Boolean doInRedis(RedisConnection connection) {
				
				try {
					Map<byte[], byte[]> value = new HashMap<byte[], byte[]>();
					
					for (Map.Entry<String, String> entry : map.entrySet()) {
						if (entry.getValue() == null) {
							value.put(rawKey(entry.getKey()), rawKey(""));
						} else {
							value.put(rawKey(entry.getKey()), rawKey(String.valueOf(entry.getValue())));							
						}
					}
					connection.hMSet(rawKey(key), value);
					
					return true;
					
				} catch (Exception e) {
//					logger.error(e.getMessage(), e);
					e.printStackTrace();
					return false;
				}
			}
		});
	}

	@Override
	public List<String> hmget(final String key, final List<String> fields) {
		return (List<String>) redisTemplate.execute(new RedisCallback() {
			public List<String> doInRedis(RedisConnection connection) {

				List<String> list = new ArrayList<String>();

				List<byte[]> value = connection.hMGet(rawKey(key), rawKeys(fields));

				for (byte[] v : value) {
					list.add(stringOf(v));
				}
				return list;
			}
		});
	}

	@Override
	public Map<String, String> hgetAll(final String key) {
		return (Map<String, String>) redisTemplate.execute(new RedisCallback() {
			public Map<String, String> doInRedis(RedisConnection connection) {

				Map<String, String> map = new HashMap<String, String>();

				Map<byte[], byte[]> value = connection.hGetAll(rawKey(key));

				if (value == null || value.isEmpty()) {
					return map;
				}

				for (Map.Entry<byte[], byte[]> entry : value.entrySet()) {
					map.put(stringOf(entry.getKey()), stringOf(entry.getValue()));
				}

				return map;
			}
		});
	}

	@Override
	public List<String> sort(final String key) {
		return (List<String>) redisTemplate.execute(new RedisCallback() {
			public List<String> doInRedis(RedisConnection connection) {

				List<String> list = new ArrayList<String>();

				DataType type = connection.type(rawKey(key));

				if (type == DataType.LIST || type == DataType.SET || type == DataType.ZSET) {
					List<byte[]> values = connection.sort(rawKey(key), null);

					if (values == null || values.isEmpty()) {
						return list;
					}
					for (byte[] value : values) {
						list.add(stringOf(value));
					}
				}
				return list;
			}
		});
	}

	@Override
	public long size(final String key) {
		return (Long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) {

				DataType type = connection.type(rawKey(key));
				if (type == DataType.LIST) {
					return connection.lLen(rawKey(key));
				} else if (type == DataType.HASH) {
					return connection.hLen(rawKey(key));
				} else if (type == DataType.ZSET) {
					return connection.zCard(rawKey(key));
				} else if (type == DataType.SET) {
					return connection.sCard(rawKey(key));
				}
				return 0L;
			}
		});
	}

	@Override
	public List<String> list(final String key, final long begin, final long end) {
		return (List<String>) redisTemplate.execute(new RedisCallback() {
			public List<String> doInRedis(RedisConnection connection) {
				List<String> list = new ArrayList<String>();

				DataType type = connection.type(rawKey(key));

				Collection<byte[]> col = null;
				if (type == DataType.LIST) {
					col = connection.lRange(rawKey(key), begin, end);
				} else if (type == DataType.ZSET) {
					col = connection.zRange(rawKey(key), begin, end);
				}
				if (col != null && col.size() > 0) {
					for (byte[] v : col) {
						list.add(stringOf(v));
					}
				}
				return list;
			}
		});
	}

	@Override
	public Set<String> set(final String key, final long begin, final long end) {
		return (Set<String>) redisTemplate.execute(new RedisCallback() {
			public Set<String> doInRedis(RedisConnection connection) {
				Set<String> set = new HashSet<String>();

				DataType type = connection.type(rawKey(key));

				Collection<byte[]> col = null;
				if (type == DataType.LIST) {
					col = connection.lRange(rawKey(key), begin, end);
				} else if (type == DataType.ZSET) {
					col = connection.zRange(rawKey(key), begin, end);
				}
				if (col != null && col.size() > 0) {
					for (byte[] v : col) {
						set.add(stringOf(v));
					}
				}
				return set;
			}
		});
	}

	@Override
	public List<String> list(final String key) {
		return (List<String>) redisTemplate.execute(new RedisCallback() {
			public List<String> doInRedis(RedisConnection connection) {
				List<String> list = new ArrayList<String>();
				DataType type = connection.type(rawKey(key));

				Collection<byte[]> col = null;
				if (type == DataType.LIST) {
					col = connection.lRange(rawKey(key), 0, -1);
				} else if (type == DataType.ZSET) {
					col = connection.zRange(rawKey(key), 0, -1);
				} else if (type == DataType.SET) {
					col = connection.sMembers(rawKey(key));
				}
				if (col != null && col.size() > 0) {
					for (byte[] v : col) {
						list.add(stringOf(v));
					}
				}
				return list;
			}
		});
	}

	@Override
	public Set<String> set(final String key) {
		return (Set<String>) redisTemplate.execute(new RedisCallback() {
			public Set<String> doInRedis(RedisConnection connection) {
				Set<String> set = new HashSet<String>();
				DataType type = connection.type(rawKey(key));

				Collection<byte[]> col = null;
				if (type == DataType.LIST) {
					col = connection.lRange(rawKey(key), 0, -1);
				} else if (type == DataType.ZSET) {
					col = connection.zRange(rawKey(key), 0, -1);
				} else if (type == DataType.SET) {
					col = connection.sMembers(rawKey(key));
				}

				if (col != null && col.size() > 0) {
					for (byte[] v : col) {
						set.add(stringOf(v));
					}
				}
				return set;
			}
		});
	}

	@Override
	public Long del(final String key, final String value) {
		return (Long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) {

				DataType type = connection.type(rawKey(key));
				if (type == DataType.LIST) {
					// list全部删除
					return connection.lRem(rawKey(key), 0, rawKey(value)) ;
				} else if (type == DataType.HASH) {
					return connection.hDel(rawKey(key), rawKey(value));
				} else if (type == DataType.ZSET) {
					return connection.zRem(rawKey(key), rawKey(value));
				} else if (type == DataType.SET) {
					return connection.sRem(rawKey(key), rawKey(value));
				}
				return 0L;
			}
		});
	}

	@Override
	public Long zremrangebyscore(final String key, final double min, final double max) {
		return (Long) redisTemplate.execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.zRemRangeByScore(rawKey(key), min, max);
			}
			
		});
	}

	@Override
	public Long zrem(final String key, final String... members) {
		return (Long) redisTemplate.execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[][] bytes = rawKeys(Arrays.asList(members));
				return connection.zRem(rawKey(key), bytes);
			}
			
		});
	}

	@Override
	public Set<Tuple> zrange(final String key, final long start, final long stop) {
		return (Set<Tuple>) redisTemplate.execute(new RedisCallback<Set<Tuple>>() {

			@Override
			public Set<Tuple> doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.zRangeWithScores(rawKey(key), start, stop);
			}
			
		});
	}

	
	@Override
	public Long zcount(final String key, final double minScore, final double maxScore) {
		return (Long) redisTemplate.execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.zCount(rawKey(key), minScore, maxScore);
			}
			
		});
	}

	@Override
	public Set<Tuple> zrangebyscore(final String key, final long minScore, final long maxScore) {
		return (Set<Tuple>) redisTemplate.execute(new RedisCallback<Set<Tuple>>() {

			@Override
			public Set<Tuple> doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.zRangeByScoreWithScores(rawKey(key), minScore, maxScore);
			}
			
		});
	}

	@Override
	public Long incr(final String key) {
		return (Long) redisTemplate.execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.incr(rawKey(key));
			}
			
		});
	}
	
}
