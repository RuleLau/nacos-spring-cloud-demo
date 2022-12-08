package com.rule.nio;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundStreamOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class EventLoop implements Runnable, Callback<Integer, String>, RedisOperations<String, String> {

    private Selector selector;
    private Thread thread;
    private Queue<Runnable> taksQueue = new LinkedBlockingQueue<>(32);

    public EventLoop() throws IOException {
        this.selector = SelectorProvider.provider().openSelector();
        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * 把Channel注册到事件查询器上
     */
    public void register(SocketChannel channel, int keyOps) {
        //把注册的逻辑封装成一个任务
        taksQueue.add(() -> {
            try {
                MyChannel myChannel = new MyChannel(channel, this);
                SelectionKey selectionKey = channel.register(selector, keyOps);
                selectionKey.attach(myChannel);
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        });

        //唤起selector上阻塞的线程
        selector.wakeup();

    }
    private static RedisTemplate<String, String> redisTemplate;

    private static RedisTemplate redisTemplate2;

    private static RedisTemplate<Object, Object> redisTemplate3;

    public static void main(String[] args) throws IOException {
        Boolean f1 =  redisTemplate.execute((RedisCallback<Boolean>) connection -> null);
        Boolean f2 = (Boolean) redisTemplate2.execute((RedisCallback<Boolean>) connection -> null);
        Boolean f3 = redisTemplate3.execute((RedisCallback<Boolean>) connection -> null);
    }


    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                System.out.println(thread + "开始查询IO事件...");
                int eventNum = selector.select();
                System.out.println("系统发生IO事件 数量->" + eventNum);

                if (eventNum > 0) {
                    Set<SelectionKey> keySet = selector.selectedKeys();
                    Iterator<SelectionKey> iterable = keySet.iterator();

                    while (iterable.hasNext()) {
                        SelectionKey key = iterable.next();
                        iterable.remove();

                        MyChannel myChannel = (MyChannel) key.attachment();

                        //可读事件
                        if (key.isReadable()) {
                            myChannel.read(key);
                        }

                        //可写事件
                        if (key.isWritable()) {
                            myChannel.write(key);
                        }
                    }
                }

                Runnable task;
                while ((task = taksQueue.poll()) != null) {
                    task.run();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public <T> T check(RedisCallback<T> action) {
        return null;
    }

    @Override
    public <T> T execute(RedisCallback<T> action) {
        return null;
    }

    @Override
    public <T> T execute(SessionCallback<T> session) {
        return null;
    }

    @Override
    public List<Object> executePipelined(RedisCallback<?> action) {
        return null;
    }

    @Override
    public List<Object> executePipelined(RedisCallback<?> action, RedisSerializer<?> resultSerializer) {
        return null;
    }

    @Override
    public List<Object> executePipelined(SessionCallback<?> session) {
        return null;
    }

    @Override
    public List<Object> executePipelined(SessionCallback<?> session, RedisSerializer<?> resultSerializer) {
        return null;
    }

    @Override
    public <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
        return null;
    }

    @Override
    public <T> T execute(RedisScript<T> script, RedisSerializer<?> argsSerializer, RedisSerializer<T> resultSerializer, List<String> keys, Object... args) {
        return null;
    }

    @Override
    public <T extends Closeable> T executeWithStickyConnection(RedisCallback<T> callback) {
        return null;
    }

    @Override
    public Boolean hasKey(String key) {
        return null;
    }

    @Override
    public Long countExistingKeys(Collection<String> keys) {
        return null;
    }

    @Override
    public Boolean delete(String key) {
        return null;
    }

    @Override
    public Long delete(Collection<String> keys) {
        return null;
    }

    @Override
    public Boolean unlink(String key) {
        return null;
    }

    @Override
    public Long unlink(Collection<String> keys) {
        return null;
    }

    @Override
    public DataType type(String key) {
        return null;
    }

    @Override
    public Set<String> keys(String pattern) {
        return null;
    }

    @Override
    public String randomKey() {
        return null;
    }

    @Override
    public void rename(String oldKey, String newKey) {

    }

    @Override
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return null;
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return null;
    }

    @Override
    public Boolean expireAt(String key, Date date) {
        return null;
    }

    @Override
    public Boolean persist(String key) {
        return null;
    }

    @Override
    public Boolean move(String key, int dbIndex) {
        return null;
    }

    @Override
    public byte[] dump(String key) {
        return new byte[0];
    }

    @Override
    public void restore(String key, byte[] value, long timeToLive, TimeUnit unit, boolean replace) {

    }

    @Override
    public Long getExpire(String key) {
        return null;
    }

    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public List<String> sort(SortQuery<String> query) {
        return null;
    }

    @Override
    public <T> List<T> sort(SortQuery<String> query, RedisSerializer<T> resultSerializer) {
        return null;
    }

    @Override
    public <T> List<T> sort(SortQuery<String> query, BulkMapper<T, String> bulkMapper) {
        return null;
    }

    @Override
    public <T, S> List<T> sort(SortQuery<String> query, BulkMapper<T, S> bulkMapper, RedisSerializer<S> resultSerializer) {
        return null;
    }

    @Override
    public Long sort(SortQuery<String> query, String storeKey) {
        return null;
    }

    @Override
    public void watch(String key) {

    }

    @Override
    public void watch(Collection<String> keys) {

    }

    @Override
    public void unwatch() {

    }

    @Override
    public void multi() {

    }

    @Override
    public void discard() {

    }

    @Override
    public List<Object> exec() {
        return null;
    }

    @Override
    public List<Object> exec(RedisSerializer<?> valueSerializer) {
        return null;
    }

    @Override
    public List<RedisClientInfo> getClientList() {
        return null;
    }

    @Override
    public void killClient(String host, int port) {

    }

    @Override
    public void slaveOf(String host, int port) {

    }

    @Override
    public void slaveOfNoOne() {

    }

    @Override
    public void convertAndSend(String destination, Object message) {

    }

    @Override
    public ClusterOperations<String, String> opsForCluster() {
        return null;
    }

    @Override
    public GeoOperations<String, String> opsForGeo() {
        return null;
    }

    @Override
    public BoundGeoOperations<String, String> boundGeoOps(String key) {
        return null;
    }

    @Override
    public <HK, HV> HashOperations<String, HK, HV> opsForHash() {
        return null;
    }

    @Override
    public <HK, HV> BoundHashOperations<String, HK, HV> boundHashOps(String key) {
        return null;
    }

    @Override
    public HyperLogLogOperations<String, String> opsForHyperLogLog() {
        return null;
    }

    @Override
    public ListOperations<String, String> opsForList() {
        return null;
    }

    @Override
    public BoundListOperations<String, String> boundListOps(String key) {
        return null;
    }

    @Override
    public SetOperations<String, String> opsForSet() {
        return null;
    }

    @Override
    public BoundSetOperations<String, String> boundSetOps(String key) {
        return null;
    }

    @Override
    public <HK, HV> StreamOperations<String, HK, HV> opsForStream() {
        return null;
    }

    @Override
    public <HK, HV> StreamOperations<String, HK, HV> opsForStream(HashMapper<? super String, ? super HK, ? super HV> hashMapper) {
        return null;
    }

    @Override
    public <HK, HV> BoundStreamOperations<String, HK, HV> boundStreamOps(String key) {
        return null;
    }

    @Override
    public ValueOperations<String, String> opsForValue() {
        return null;
    }

    @Override
    public BoundValueOperations<String, String> boundValueOps(String key) {
        return null;
    }

    @Override
    public ZSetOperations<String, String> opsForZSet() {
        return null;
    }

    @Override
    public BoundZSetOperations<String, String> boundZSetOps(String key) {
        return null;
    }

    @Override
    public RedisSerializer<?> getKeySerializer() {
        return null;
    }

    @Override
    public RedisSerializer<?> getValueSerializer() {
        return null;
    }

    @Override
    public RedisSerializer<?> getHashKeySerializer() {
        return null;
    }

    @Override
    public RedisSerializer<?> getHashValueSerializer() {
        return null;
    }
}
