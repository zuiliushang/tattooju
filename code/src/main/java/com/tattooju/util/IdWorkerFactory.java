package com.tattooju.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * twitter snowflake生成唯一id算法
 */
public final class IdWorkerFactory {

    private static Map<String,IdWorker> idWorkerMap = new ConcurrentHashMap<>();

    public static IdWorker getIdWorder(long workerId) {
        synchronized(idWorkerMap){
            IdWorker idWorker = idWorkerMap.get(String.valueOf(workerId));
            if(idWorker==null){
                idWorker = new IdWorker(workerId);
                idWorkerMap.put(String.valueOf(workerId),idWorker);
            }
            return idWorker;
        }

    }

    public static final class IdWorker {


        private final long        workerId;
        private final static long twepoch            = 1490371200000L;//2017-03-25 00:00:00
        private long              sequence           = 0L;
        //机器标识位数
        private final static long workerIdBits       = 10L;
        //机器ID最大值
        private final static long maxWorkerId        = -1L ^ -1L << workerIdBits;
        //毫秒内自增位
        private final static long sequenceBits       = 10L;
        //机器ID偏左移
        private final static long workerIdShift      = sequenceBits;
        //时间毫秒左移
        private final static long timestampLeftShift = sequenceBits + workerIdBits;

        private final static long sequenceMask       = -1L ^ -1L << sequenceBits;

        private long              lastTimestamp      = -1L;

        public IdWorker(final long workerId) {
            super();
            if (workerId > maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException(String.format(
                        "worker Id can't be greater than %d or less than 0", maxWorkerId));
            }
            this.workerId = workerId;
        }



        public synchronized long nextId() {
            long timestamp = this.timeGen();
            if (this.lastTimestamp == timestamp) {
                this.sequence = (this.sequence + 1) & sequenceMask;
                if (this.sequence == 0) {
                    System.out.println("###########" + sequenceMask);
                    timestamp = this.tilNextMillis(this.lastTimestamp);
                }
            } else {
                this.sequence = 0;
            }
            if (timestamp < this.lastTimestamp) {
                try {
                    throw new Exception(String.format(
                            "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                            this.lastTimestamp - timestamp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            this.lastTimestamp = timestamp;
            long _times = (timestamp - twepoch) << timestampLeftShift;
            long _worker = this.workerId << workerIdShift;
            long _sequence = this.sequence;


            long nextId = _times | _worker |_sequence;
//          System.out.println("workerId:" + workerId
//               + ",timestamp:" + timestamp
//               + ",_times:"+_times
//               + ",_worker:"+_worker
//               + ",sequence:" + sequence);
            return nextId;
        }


        public String nextId(String prefix){
            return prefix+this.nextId();
        }

        private long tilNextMillis(final long lastTimestamp) {
            long timestamp = this.timeGen();
            while (timestamp <= lastTimestamp) {
                timestamp = this.timeGen();
            }
            return timestamp;
        }

        private long timeGen() {
            return System.currentTimeMillis();
        }



    }

}
