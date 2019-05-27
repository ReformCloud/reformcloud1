/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.sun.management.OperatingSystemMXBean;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import systems.reformcloud.api.IAsyncAPI;
import systems.reformcloud.cache.Cache;
import systems.reformcloud.cache.CacheClearer;
import systems.reformcloud.logging.AbstractLoggerProvider;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.channel.ChannelReader;
import systems.reformcloud.network.handler.Decoder;
import systems.reformcloud.network.handler.Encoder;
import systems.reformcloud.network.length.LengthDecoder;
import systems.reformcloud.network.length.LengthEncoder;
import systems.reformcloud.utility.StringUtil;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public final class ReformCloudLibraryService {
    static {
        String threadName = "ReformCloud-Main";
        THREAD_MAIN_NAME = threadName;
        Thread.currentThread().setName(threadName);
        Thread.setDefaultUncaughtExceptionHandler((t, cause) -> AbstractLoggerProvider.defaultLogger().exception(cause));

        new IAsyncAPI();

        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("io.netty.noPreferDirect", "true");
        System.setProperty("io.netty.maxDirectMemory", "0");
        System.setProperty("io.netty.leakDetectionLevel", "DISABLED");
        System.setProperty("io.netty.recycler.maxCapacity", "0");
        System.setProperty("io.netty.recycler.maxCapacity.default", "0");
        System.setProperty("io.netty.noPreferDirect", "true");
        System.setProperty("io.netty.allocator.type", "UNPOOLED");
    }

    /**
     * Get the reformcloud main thread name
     */
    private static final String THREAD_MAIN_NAME;

    /**
     * The cloud created gson instance
     */
    public static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();

    /**
     * The cloud creates json parser instance
     */
    public static final JsonParser PARSER = new JsonParser();

    /**
     * Netty booleans
     */
    private static final boolean EPOLL = Epoll.isAvailable(), KQUEUE = KQueue.isAvailable();

    /**
     * The current thread local random instance
     */
    public static final ThreadLocalRandom THREAD_LOCAL_RANDOM = ThreadLocalRandom.current();

    /**
     * The executor service used by the cloud
     */
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool(
            createThreadFactory(
                    (t, e) -> {
                        if (ReformCloudLibraryServiceProvider.getInstance() != null)
                            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error in thread group", e);
                        else
                            e.printStackTrace(System.err);
                    })
    );

    /**
     * The cache clearer of the cloud system
     */
    public static final CacheClearer CACHE_CLEARER = new CacheClearer();

    /**
     * Creates a new ConcurrentHashMap
     */
    public static <K, V> ConcurrentHashMap<K, V> concurrentHashMap() {
        return new ConcurrentHashMap<>(0);
    }

    /**
     * Sends the cloud header
     */
    public static void sendHeader() {
        System.out.println(" ");
        System.out.println(
                "         ______ _______ _______  _____   ______ _______ _______         _____  _     _ ______ \n" +
                        "        |_____/ |______ |______ |     | |_____/ |  |  | |       |      |     | |     | |     \\\n" +
                        "        |    \\_ |______ |       |_____| |    \\_ |  |  | |_____  |_____ |_____| |_____| |_____/\n" +
                        "                                                                                              \n" +
                        "                                     The official CloudSystem                               \n" +
                        "      __________________________________________________________________________________________ \n\n" +
                        "                            Support Discord: https://discord.gg/uskXdVZ      \n"
        );
    }

    /**
     * Sends the cloud header colored
     *
     * @param loggerProvider The logger provider which should be used to print the header coloured
     */
    public static void sendHeader(final LoggerProvider loggerProvider) {
        if (loggerProvider == null) {
            sendHeader();
            return;
        }

        System.out.println(" ");
        loggerProvider.coloured(
                "§3" +
                        "         ______ _______ _______  _____   ______ _______ _______         _____  _     _ ______ \n" +
                        "        |_____/ |______ |______ |     | |_____/ |  |  | |       |      |     | |     | |     \\\n" +
                        "        |    \\_ |______ |       |_____| |    \\_ |  |  | |_____  |_____ |_____| |_____| |_____/\n" +
                        "                                                                                              \n" +
                        "                                     §rThe official CloudSystem                               \n" +
                        "      __________________________________________________________________________________________ \n\n" +
                        "                            Support Discord: https://discord.gg/uskXdVZ      \n"
        );
    }

    /**
     * Creates a new controller key
     *
     * @return The created controller key as string
     */
    public static String newKey() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            UUID uuid = UUID.randomUUID();
            stringBuilder.append(uuid.toString().replace("-", ""));
        }

        return stringBuilder.substring(0);
    }

    /**
     * Prepares the given Channel with all utilities
     *
     * @param channel        The given channel where all Handlers should be added
     * @param channelHandler The pre-initialized ChannelHandler where all channels are
     *                       registered and handled
     */
    public static Channel prepareChannel(Channel channel, ChannelHandler channelHandler) {
        channel.pipeline().addLast(
                new LengthDecoder(),
                new Decoder(),
                new LengthEncoder(),
                new Encoder(),
                new ChannelReader(channelHandler));

        return channel;
    }

    /**
     * New EventLoopGroup
     *
     * @return new EpollEventLoopGroup if EPOLL is available,
     *              a new KQueueEventLoopGroup if KQUEUE is available or a new NioEventLoopGroup
     */
    public static EventLoopGroup eventLoopGroup() {
        return EPOLL ? new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors(), createThreadFactory())
                : KQUEUE ? new KQueueEventLoopGroup(Runtime.getRuntime().availableProcessors(), createThreadFactory())
                : new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), createThreadFactory());
    }

    /**
     * New EventLoopGroup
     *
     * @return new EpollEventLoopGroup if EPOLL is available,
     *              a new KQueueEventLoopGroup if KQUEUE is available or a new NioEventLoopGroup with the given threads
     */
    public static EventLoopGroup eventLoopGroup(int threads) {
        return EPOLL ? new EpollEventLoopGroup(threads, createThreadFactory())
                : KQUEUE ? new KQueueEventLoopGroup(threads, createThreadFactory())
                : new NioEventLoopGroup(threads, createThreadFactory());
    }

    /**
     * New ServerSocketChannel
     *
     * @return EpollServerSocketChannel-Class if EPOLL is available or a new NioServerSocketChannel-Class
     */
    public static Class<? extends ServerSocketChannel> serverSocketChannel() {
        return EPOLL ? EpollServerSocketChannel.class : KQUEUE ? KQueueServerSocketChannel.class : NioServerSocketChannel.class;
    }

    /**
     * New SocketChannel
     *
     * @return EpollSocketChannel-Class if EPOLL is available or a new NioSocketChannel-Class
     */
    public static Class<? extends SocketChannel> clientSocketChannel() {
        return EPOLL ? EpollSocketChannel.class : KQUEUE ? KQueueSocketChannel.class : NioSocketChannel.class;
    }

    /**
     * Let the main-thread sleep the given time
     *
     * @param time      The time which should be slept
     */
    public static void sleep(long time) {
        sleep(TimeUnit.MILLISECONDS, time);
    }

    /**
     * Let the given thread sleep the given time
     *
     * @param thread        The thread on which the cloud should sleep
     * @param time          The time which should be sleep
     */
    public static void sleep(Thread thread, long time) {
        try {
            Thread.sleep(time);
        } catch (final InterruptedException ignored) {
        }
    }

    /**
     * Let the main-thread sleep the given time
     *
     * @param timeUnit      The TimeUnit in which the thread should sleep
     * @param time          The time which should be sleep
     */
    public static void sleep(TimeUnit timeUnit, long time) {
        try {
            timeUnit.sleep(time);
        } catch (final InterruptedException ignored) {
        }
    }

    /**
     * Check if the given String is an integer
     *
     * @param key       The incoming string which should be checked
     * @return If the string is an int or not
     */
    public static boolean checkIsInteger(String key) {
        try {
            Integer.parseInt(key);
            return true;
        } catch (final Throwable ignored) {
            return false;
        }
    }

    /**
     * Checks if the given string is a boolean or nor
     *
     * @param key       The incoming string which should be checked
     * @return If the string is an boolean or not
     */
    public static boolean checkIsValidBoolean(String key) {
        return key.equalsIgnoreCase("true") || key.equalsIgnoreCase("false");
    }

    /**
     * Returns the cpu usage of the system
     *
     * @return the cpu usage of the system
     */
    public static double cpuUsage() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad() * 100;
    }

    /**
     * Returns the cpu usage of the internal jar
     *
     * @return the cpu usage of the internal jar
     */
    public static double internalCpuUsage() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getProcessCpuLoad() * 100;
    }

    /**
     * Gets the used system memory
     *
     * @return the memory usage of the system
     */
    public static long usedMemorySystem() {
        return maxMemorySystem() - ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreePhysicalMemorySize();
    }

    /**
     * Gets the max memory of the system
     *
     * @return The max memory of the system
     */
    public static long maxMemorySystem() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
    }

    /**
     * Gets the runtime mx bean of the current jvm
     *
     * @return The runtime mx bean of the current jvm
     */
    private static RuntimeMXBean getRuntimeMXBean() {
        return ManagementFactory.getRuntimeMXBean();
    }

    /**
     * Get the current class loading mx bean of the current jvm
     *
     * @return the current class loading mx bean of the current jvm
     */
    private static ClassLoadingMXBean getClassLoadingMXBean() {
        return ManagementFactory.getClassLoadingMXBean();
    }

    /**
     * Gets the start time of the current jvm
     *
     * @return The start time of the jvm
     */
    public static long systemStartTime() {
        return getRuntimeMXBean().getStartTime();
    }

    /**
     * Get the uptime of the current jvm
     *
     * @return The uptime of the jvm
     */
    public static long systemUpTime() {
        return System.currentTimeMillis() - getRuntimeMXBean().getUptime();
    }

    /**
     * Gets the max memory of the runtime
     *
     * @return The max memory of the runtime
     */
    public static long maxMemoryRuntime() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * Converts a long into a byte
     *
     * @param b The incoming long which should be converted
     * @return The converted long in byte
     */
    public static long bytesToMB(final long b) {
        return b / 1024 / 1024;
    }

    /**
     * Checks if the caller thread is the main thread
     *
     * @return If the caller thread is the main thread
     */
    public static boolean isOnMainThread() {
        return Thread.currentThread().getName().equals(THREAD_MAIN_NAME);
    }

    /**
     * Checks if the object equals the parameters of the checkable
     *
     * @param checkable     The checkable with the check parameters
     * @param toCheck       The object which should be checked
     * @return If the parameters of the checkable are true or not
     */
    public static boolean check(Predicate<Object> checkable, final Object toCheck) {
        return toCheck != null && checkable != null && checkable.test(toCheck);
    }

    /**
     * Creates a new cache
     *
     * @param maxSize       The maximum size of the cache
     * @param <K>           The key value of the cache
     * @param <V>           The values of the cache
     * @return The created cache using the given parameters
     */
    public static <K, V> Cache<K, V> newCache(long maxSize) {
        return new Cache<>(maxSize);
    }

    private static ThreadFactory createThreadFactory(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        AtomicLong atomicLong = new AtomicLong(0);
        return runnable -> {
            Thread thread = threadFactory.newThread(runnable);
            thread.setName(String.format(Locale.ROOT, "ReformCloud-PoolThread-%d", atomicLong.getAndIncrement()));
            thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            thread.setDaemon(true);
            return thread;
        };
    }

    private static ThreadFactory createThreadFactory() {
        return new DefaultThreadFactory(MultithreadEventExecutorGroup.class, true, Thread.MIN_PRIORITY);
    }
}
