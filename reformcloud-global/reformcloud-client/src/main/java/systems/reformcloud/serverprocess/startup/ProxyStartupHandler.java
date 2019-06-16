/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.serverprocess.startup;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.imageio.ImageIO;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.CloudProcess;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.proxy.versions.ProxyVersions;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.meta.startup.stages.ProcessStartupStage;
import systems.reformcloud.network.packets.out.PacketOutAddProcess;
import systems.reformcloud.network.packets.out.PacketOutGetControllerTemplate;
import systems.reformcloud.network.packets.out.PacketOutIconSizeIncorrect;
import systems.reformcloud.network.packets.out.PacketOutRemoveProcess;
import systems.reformcloud.network.packets.out.PacketOutSendControllerConsoleMessage;
import systems.reformcloud.network.packets.out.PacketOutUpdateControllerTemplate;
import systems.reformcloud.serverprocess.screen.ScreenHandler;
import systems.reformcloud.template.TemplatePreparer;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.files.ZoneInformationProtocolUtility;
import systems.reformcloud.utility.startup.ServiceAble;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public final class ProxyStartupHandler implements Serializable, ServiceAble {

    private ProxyStartupInfo proxyStartupInfo;

    private Path path;

    private Process process;

    private int port;

    private boolean toShutdown = false;

    private ScreenHandler screenHandler;

    private ProcessStartupStage processStartupStage;

    private Template template;

    private final Lock runtimeLock = new ReentrantLock();
    private long startupTime;
    private long finishedTime;

    /**
     * Creates a instance of a ProxyStartupHandler
     */
    public ProxyStartupHandler(final ProxyStartupInfo proxyStartupInfo) {
        this.startupTime = System.currentTimeMillis();
        this.processStartupStage = ProcessStartupStage.WAITING;
        this.proxyStartupInfo = proxyStartupInfo;

        if (this.proxyStartupInfo.getProxyGroup().isStatic()) {
            this.path = Paths.get("reformcloud/static/proxies/" + proxyStartupInfo.getName());
            FileUtils.createDirectory(path);
        } else {
            this.path = Paths.get(
                "reformcloud/temp/proxies/" + proxyStartupInfo.getName() + "-" + proxyStartupInfo
                    .getUid());
            FileUtils.deleteFullDirectory(path);
            FileUtils.createDirectory(path);
        }
    }

    @Override
    public boolean bootstrap() {
        try {
            runtimeLock.lock();
            bootstrap0();
        } finally {
            runtimeLock.unlock();
        }

        return true;
    }

    /**
     * Starts the BungeeCord
     */
    public void bootstrap0() {
        if (!this.proxyStartupInfo.getProxyGroup().isStatic()) {
            FileUtils.deleteFullDirectory(path);
        }

        if (this.proxyStartupInfo.getTemplate() != null) {
            template = this.proxyStartupInfo.getProxyGroup()
                .getTemplate(this.proxyStartupInfo.getTemplate());
        } else {
            template = this.proxyStartupInfo.getProxyGroup().randomTemplate();
        }

        this.processStartupStage = ProcessStartupStage.COPY;
        if (!this.proxyStartupInfo.getProxyGroup().isStatic()) {
            this.sendMessage(ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded()
                .getClient_copies_template()
                .replace("%path%", this.path + ""));
            if (template.getTemplateBackend().equals(TemplateBackend.URL)
                && template.getTemplateUrl() != null) {
                new TemplatePreparer(path + "/template.zip")
                    .loadTemplate(template.getTemplateUrl());
                try {
                    ZoneInformationProtocolUtility
                        .unZip(new File(path + "/template.zip"), path + "");
                } catch (final Exception ex) {
                    ex.printStackTrace();
                    return;
                }
            } else if (template.getTemplateBackend().equals(TemplateBackend.CLIENT)) {
                if (this.proxyStartupInfo.getProxyGroup().getTemplateOrElseNull("every") != null) {
                    if (!Files.exists(Paths.get(
                        "reformcloud/templates/proxies/" + this.proxyStartupInfo.getProxyGroup()
                            .getName() + "/every"))) {
                        FileUtils.createDirectory(Paths.get(
                            "reformcloud/templates/proxies/" + this.proxyStartupInfo.getProxyGroup()
                                .getName() + "/every"));
                    } else {
                        FileUtils.copyAllFiles(
                            Paths.get("reformcloud/templates/proxies/" + this.proxyStartupInfo
                                .getProxyGroup().getName() + "/every"),
                            this.path.toString()
                        );
                    }
                }

                FileUtils.copyAllFiles(Paths.get(
                    "reformcloud/templates/proxies/" + proxyStartupInfo.getProxyGroup().getName()
                        + "/" + template.getName()), path + StringUtil.EMPTY);
            } else if (template.getTemplateBackend().equals(TemplateBackend.CONTROLLER)) {
                ReformCloudClient.getInstance().getChannelHandler()
                    .sendPacketSynchronized("ReformCloudController",
                        new PacketOutGetControllerTemplate("proxy",
                            this.proxyStartupInfo.getProxyGroup().getName(),
                            this.template.getName(),
                            this.proxyStartupInfo.getUid(),
                            this.proxyStartupInfo.getName())
                    );
                ReformCloudLibraryService.sleep(50);
            } else {
                return;
            }
        }

        FileUtils.copyAllFiles(Paths.get("libraries"), path + "/libraries");

        if (!Files.exists(Paths.get(path + "/plugins"))) {
            FileUtils.createDirectory(Paths.get(path + "/plugins"));
        }

        FileUtils.createDirectory(Paths.get(path + "/reformcloud"));

        FileUtils.copyAllFiles(Paths.get("reformcloud/default/proxies"), path + StringUtil.EMPTY);

        this.processStartupStage = ProcessStartupStage.PREPARING;
        this.port = ReformCloudClient.getInstance().getInternalCloudNetwork()
            .getServerProcessManager()
            .nextFreePort(proxyStartupInfo.getProxyGroup().getStartPort());
        while (!ReformCloudClient.getInstance().isPortUseable(port)) {
            port++;
            ReformCloudLibraryService.sleep(20);
        }

        if (!Files.exists(Paths.get(path + "/server-icon.png"))) {
            FileUtils.copyCompiledFile("reformcloud/bungeecord/icon/server-icon.png",
                path + "/server-icon.png");
        }

        try {
            BufferedImage bufferedImage = ImageIO.read(new File(path + "/server-icon.png"));
            if (bufferedImage.getWidth() != 64 || bufferedImage.getHeight() != 64) {
                ReformCloudClient.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                        new PacketOutIconSizeIncorrect(this.proxyStartupInfo.getName()));
                FileUtils.deleteFileIfExists(Paths.get(path + "/server-icon.png"));
                FileUtils.copyCompiledFile("reformcloud/bungeecord/icon/server-icon.png",
                    path + "/server-icon.png");
            }
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudClient.getInstance().getLoggerProvider(),
                "Error while reading image", ex);
            return;
        }

        if (!proxyStartupInfo.getProxyGroup().getProxyVersions().equals(ProxyVersions.VELOCITY)) {
            if (!Files.exists(Paths.get(path + "/config.yml"))) {
                FileUtils.copyCompiledFile("reformcloud/bungeecord/config/config.yml",
                    path + "/config.yml");
            }

            if (!Files.exists(Paths.get(path + "/BungeeCord.jar"))) {
                if (!Files.exists(Paths.get("reformcloud/jars/" + ProxyVersions
                    .getAsJarFileName(this.proxyStartupInfo.getProxyGroup().getProxyVersions())))) {
                    DownloadManager.downloadAndDisconnect(
                        this.proxyStartupInfo.getProxyGroup().getProxyVersions().getName(),
                        this.proxyStartupInfo.getProxyGroup().getProxyVersions().getUrl(),
                        "reformcloud/jars/" + ProxyVersions.getAsJarFileName(
                            this.proxyStartupInfo.getProxyGroup().getProxyVersions())
                    );
                }

                FileUtils.copyFile("reformcloud/jars/" + ProxyVersions
                        .getAsJarFileName(this.proxyStartupInfo.getProxyGroup().getProxyVersions()),
                    path + "/BungeeCord.jar");
            }

            if (!Files.exists(Paths.get(
                "reformcloud/apis/ReformAPIBungee-" + StringUtil.BUNGEE_API_DOWNLOAD + ".jar"))) {
                DownloadManager.downloadSilentAndDisconnect(
                    "https://dl.reformcloud.systems/apis/ReformAPIBungee-"
                        + StringUtil.BUNGEE_API_DOWNLOAD + ".jar",
                    "reformcloud/apis/ReformAPIBungee-" + StringUtil.BUNGEE_API_DOWNLOAD + ".jar"
                );

                final File dir = new File("reformcloud/apis");
                if (dir.listFiles() != null) {
                    Arrays.stream(dir.listFiles()).forEach(file -> {
                        if (file.getName().startsWith("ReformAPIBungee")
                            && file.getName().endsWith(".jar")
                            && !file.getName().contains(StringUtil.BUNGEE_API_DOWNLOAD)) {
                            file.delete();
                        }
                    });
                }
            }

            FileUtils.deleteFileIfExists(Paths.get(path + "/plugins/ReformAPIBungee.jar"));
            FileUtils.copyFile(
                "reformcloud/apis/ReformAPIBungee-" + StringUtil.BUNGEE_API_DOWNLOAD + ".jar",
                this.path + "/plugins/ReformAPIBungee.jar");

            try {
                this.prepareConfiguration(new File(path + "/config.yml"), "\"" +
                    ReformCloudClient.getInstance().getCloudConfiguration().getStartIP() + ":"
                    + port + "\"");
            } catch (final Throwable throwable) {
                StringUtil
                    .printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(),
                        "Error while preparing proxy configuration, break", throwable);
                return;
            }
        } else {
            if (!Files.exists(Paths.get(path + "/velocity.toml"))) {
                FileUtils.copyCompiledFile("reformcloud/bungeecord/config/velocity.toml",
                    path + "/velocity.toml");
            }

            if (!Files.exists(Paths.get(path + "/BungeeCord.jar"))) {
                if (!Files.exists(Paths.get("reformcloud/jars/" + ProxyVersions
                    .getAsJarFileName(this.proxyStartupInfo.getProxyGroup().getProxyVersions())))) {
                    DownloadManager.downloadAndDisconnect(
                        this.proxyStartupInfo.getProxyGroup().getProxyVersions().getName(),
                        this.proxyStartupInfo.getProxyGroup().getProxyVersions().getUrl(),
                        "reformcloud/jars/" + ProxyVersions.getAsJarFileName(
                            this.proxyStartupInfo.getProxyGroup().getProxyVersions())
                    );
                }

                FileUtils.copyFile("reformcloud/jars/" + ProxyVersions
                        .getAsJarFileName(this.proxyStartupInfo.getProxyGroup().getProxyVersions()),
                    path + "/BungeeCord.jar");
            }

            try {
                this.prepareConfiguration(new File(path + "/velocity.toml"),
                    ReformCloudClient.getInstance().getCloudConfiguration().getStartIP() + ":"
                        + port);
            } catch (Throwable throwable) {
                StringUtil
                    .printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(),
                        "Error while starting proxy process", throwable);
                return;
            }

            if (!Files.exists(Paths.get(
                "reformcloud/apis/ReformAPIVelocity-" + StringUtil.VELOCITY_API_DOWNLOAD
                    + ".jar"))) {
                DownloadManager.downloadSilentAndDisconnect(
                    "https://dl.reformcloud.systems/apis/ReformAPIVelocity-"
                        + StringUtil.VELOCITY_API_DOWNLOAD + ".jar",
                    "reformcloud/apis/ReformAPIVelocity-" + StringUtil.VELOCITY_API_DOWNLOAD
                        + ".jar"
                );

                final File dir = new File("reformcloud/apis");
                if (dir.listFiles() != null) {
                    Arrays.stream(dir.listFiles()).forEach(file -> {
                        if (file.getName().startsWith("ReformAPIVelocity")
                            && file.getName().endsWith(".jar")
                            && !file.getName().contains(StringUtil.VELOCITY_API_DOWNLOAD)) {
                            file.delete();
                        }
                    });
                }
            }

            FileUtils.deleteFileIfExists(Paths.get(path + "/plugins/ReformAPIVelocity.jar"));
            FileUtils.copyFile(
                "reformcloud/apis/ReformAPIVelocity-" + StringUtil.VELOCITY_API_DOWNLOAD + ".jar",
                this.path + "/plugins/ReformAPIVelocity.jar");
        }

        ProxyInfo proxyInfo = new ProxyInfo(
            new CloudProcess(proxyStartupInfo.getName(), proxyStartupInfo.getUid(),
                ReformCloudClient.getInstance().getCloudConfiguration().getClientName(),
                proxyStartupInfo.getProxyGroup().getName(), proxyStartupInfo.getConfiguration(),
                template, proxyStartupInfo.getId()),
            proxyStartupInfo.getProxyGroup(),
            ReformCloudClient.getInstance().getCloudConfiguration().getStartIP(),
            this.port, 0, proxyStartupInfo.getProxyGroup().getMemory(), false, new ArrayList<>()
        );

        new Configuration()
            .addValue("info", proxyInfo)
            .addValue("address",
                ReformCloudClient.getInstance().getCloudConfiguration().getEthernetAddress())
            .addStringValue("controllerKey",
                ReformCloudClient.getInstance().getCloudConfiguration().getControllerKey())
            .addBooleanValue("ssl", ReformCloudClient.getInstance().isSsl())
            .addBooleanValue("debug", ReformCloudClient.getInstance().getLoggerProvider().isDebug())
            .addValue("startupInfo", proxyStartupInfo)

            .write(Paths.get(path + "/reformcloud/config.json"));

        this.screenHandler = new ScreenHandler(proxyInfo.getCloudProcess().getName());

        this.processStartupStage = ProcessStartupStage.START;
        final String[] cmd = new String[]
            {
                "-XX:+UseG1GC",
                "-XX:MaxGCPauseMillis=50",
                "-XX:-UseAdaptiveSizePolicy",
                "-XX:CompileThreshold=100",
                "-Djline.terminal=jline.UnsupportedTerminal",
                "-DIReallyKnowWhatIAmDoingISwear=true",
                "-Xmx" + this.proxyStartupInfo.getProxyGroup().getMemory() + "M",
            };

        final String[] after = new String[]
            {
                StringUtil.JAVA_JAR,
                "loader.jar",
                "--file=BungeeCord.jar",
                "--version=" + StringUtil.REFORM_VERSION
            };

        String command = ReformCloudClient.getInstance().getParameterManager()
            .buildJavaCommand(proxyInfo.getCloudProcess().getGroup(), cmd, after)
            .replace("%port%", Integer.toString(port))
            .replace("%host%", ReformCloudClient.getInstance().getCloudConfiguration().getStartIP())
            .replace("%name%", proxyStartupInfo.getName())
            .replace("%group%", proxyStartupInfo.getProxyGroup().getName())
            .replace("%template%", this.template.getName());

        FileUtils.copyFile("reformcloud/files/ReformCloudProcess.jar", this.path + "/loader.jar");

        try {
            this.process = Runtime.getRuntime().exec(command, null, new File(path.toString()));
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(),
                    "Error while starting proxy process", ex);
            return;
        }

        ReformCloudClient.getInstance().getInternalCloudNetwork().getServerProcessManager()
            .registerProxyProcess(
                proxyStartupInfo.getUid(), proxyStartupInfo.getName(), proxyInfo, port
            );
        ReformCloudClient.getInstance().getChannelHandler()
            .sendPacketSynchronized("ReformCloudController",
                new PacketOutAddProcess(proxyInfo));

        ReformCloudClient.getInstance().getCloudProcessScreenService()
            .registerProxyProcess(proxyStartupInfo.getName(), this);
        ReformCloudClient.getInstance().getClientInfo().getStartedProxies()
            .add(proxyInfo.getCloudProcess().getName());

        this.finishedTime = System.currentTimeMillis();

        this.processStartupStage = ProcessStartupStage.DONE;
    }

    /**
     * Checks if the BungeeCordProcess is alive
     *
     * @return {@code true} if the BungeeCordProcess is alive or {@code false} if the
     * BungeeCordProcess isn't alive
     * @see Process#isAlive()
     * @see Process#getInputStream()
     */
    @Override
    public boolean isAlive() {
        try {
            return process != null && process.getInputStream().available() != -1 && process
                .isAlive();
        } catch (final Throwable throwable) {
            return false;
        }
    }

    @Override
    public void shutdown(boolean update) {
        try {
            runtimeLock.lock();
            shutdown0(null, update);
        } finally {
            runtimeLock.unlock();
        }
    }

    public void shutdown(String message, boolean update) {
        try {
            runtimeLock.lock();
            shutdown0(message, update);
        } finally {
            runtimeLock.unlock();
        }
    }

    @Override
    public void shutdown() {
        shutdown(true);
    }

    /**
     * Stops the Process
     *
     * @see ProxyStartupHandler#isAlive()
     */
    private void shutdown0(final String message, final boolean update) {
        if (toShutdown) {
            return;
        }

        toShutdown = true;
        ReformCloudClient.getInstance().getClientInfo().getStartedProxies()
            .remove(this.proxyStartupInfo.getName());
        final ProxyInfo proxyInfo = ReformCloudClient.getInstance().getInternalCloudNetwork()
            .getServerProcessManager().getRegisteredProxyByUID(this.proxyStartupInfo.getUid());

        if (update) {
            ReformCloudClient.getInstance().getChannelHandler()
                .sendDirectPacket("ReformCloudController",
                    new PacketOutRemoveProcess(proxyInfo));
        }

        ReformCloudClient.getInstance().getCloudProcessScreenService()
            .unregisterProxyProcess(this.proxyStartupInfo.getName());
        ReformCloudClient.getInstance().getInternalCloudNetwork().getServerProcessManager()
            .unregisterProxyProcess(
                this.proxyStartupInfo.getUid(), this.proxyStartupInfo.getName(), this.port
            );

        if (update) {
            ReformCloudLibraryService.sleep(300);
        }

        if (message == null) {
            this.executeCommand("end ReformCloud restarting...");
        } else {
            this.executeCommand(message.startsWith(" ") ? "end" + message : "end " + message);
        }

        try {
            if (this.isAlive()) {
                this.process.destroy();
            }
        } catch (final Throwable throwable) {
            StringUtil.printError(ReformCloudClient.getInstance().getLoggerProvider(),
                "Error on Proxy shutdown", throwable);
        }

        this.screenHandler.disableScreen();

        if (this.proxyStartupInfo.getProxyGroup().isSaveLogs()) {
            if (this.proxyStartupInfo.getProxyGroup().getProxyVersions()
                .equals(ProxyVersions.BUNGEECORD)
                || this.proxyStartupInfo.getProxyGroup().getProxyVersions()
                .equals(ProxyVersions.HEXACORD)) {
                FileUtils.copyFile(this.path + "/proxy.log.0",
                    "reformcloud/saves/proxies/logs/server_log_" +
                        this.proxyStartupInfo.getUid() + "-" + this.proxyStartupInfo.getName()
                        + ".log");
            } else if (this.proxyStartupInfo.getProxyGroup().getProxyVersions()
                .equals(ProxyVersions.TRAVERTINE)
                || this.proxyStartupInfo.getProxyGroup().getProxyVersions()
                .equals(ProxyVersions.WATERFALL)) {
                FileUtils.copyFile(this.path + "/logs/latest.log",
                    "reformcloud/saves/proxies/logs/server_log_" +
                        this.proxyStartupInfo.getUid() + "-" + this.proxyStartupInfo.getName()
                        + ".log");
            }
        }

        if (this.template.getTemplateBackend().equals(TemplateBackend.CONTROLLER)
            && !proxyStartupInfo.getProxyGroup().isStatic()) {
            byte[] template = ZoneInformationProtocolUtility.zipDirectoryToBytes(this.path);
            ReformCloudClient.getInstance().getChannelHandler().sendPacketSynchronized(
                "ReformCloudController", new PacketOutUpdateControllerTemplate(
                    "proxy", this.proxyStartupInfo.getProxyGroup().getName(),
                    this.template.getName(), template
                )
            );
        }

        if (!this.proxyStartupInfo.getProxyGroup().isStatic()) {
            FileUtils.deleteFullDirectory(path);
        } else {
            FileUtils.deleteFileIfExists(
                proxyStartupInfo.getProxyGroup().getProxyVersions().equals(ProxyVersions.VELOCITY)
                    ? path + "/plugins/ReformAPIVelocity.jar"
                    : path + "/plugins/ReformAPIBungee.jar");
        }

        try {
            this.finalize();
        } catch (final Throwable ignored) {
        }

    }

    /**
     * Executes a command on the BungeeCordProcess
     *
     * @see Process#getOutputStream()
     * @see OutputStream#write(byte[])
     */
    @Override
    public void executeCommand(String command) {
        if (!this.isAlive()) {
            return;
        }

        try {
            process.getOutputStream().write((command + "\n").getBytes());
            process.getOutputStream().flush();
        } catch (final IOException ignored) {
        }
    }

    /**
     * Prepares the configuration for the BungeeCord startup
     */
    private void prepareConfiguration(final File file, final String hostAndPort) throws Throwable {
        String context = org.apache.commons.io.FileUtils
            .readFileToString(file, StandardCharsets.UTF_8);
        context = context.replace("0.0.0.0:25577", hostAndPort);
        org.apache.commons.io.FileUtils.write(file, context, StandardCharsets.UTF_8);
    }

    /**
     * Sends a message to ReformCloudController and to Client Console
     *
     * @see PacketOutSendControllerConsoleMessage
     */
    private void sendMessage(final String message) {
        ReformCloudClient.getInstance().getLoggerProvider().info(message);
        ReformCloudClient.getInstance().getChannelHandler()
            .sendPacketSynchronized("ReformCloudController",
                new PacketOutSendControllerConsoleMessage(message));
    }

    @Override
    public String uploadLog() throws IOException {
        if (!this.isAlive()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        if (this.proxyStartupInfo.getProxyGroup().getProxyVersions()
            .equals(ProxyVersions.BUNGEECORD)
            || this.proxyStartupInfo.getProxyGroup().getProxyVersions()
            .equals(ProxyVersions.HEXACORD)) {
            Files.readAllLines(Paths.get(this.path + "/proxy.log.0"), StandardCharsets.UTF_8)
                .forEach(e -> stringBuilder.append(e).append("\n"));
        } else if (this.proxyStartupInfo.getProxyGroup().getProxyVersions()
            .equals(ProxyVersions.TRAVERTINE)
            || this.proxyStartupInfo.getProxyGroup().getProxyVersions()
            .equals(ProxyVersions.WATERFALL)
            || this.proxyStartupInfo.getProxyGroup().getProxyVersions()
            .equals(ProxyVersions.VELOCITY)) {
            Files.readAllLines(Paths.get(this.path + "/logs/latest.log"), StandardCharsets.UTF_8)
                .forEach(e -> stringBuilder.append(e).append("\n"));
        }

        return ReformCloudClient.getInstance().getLoggerProvider()
            .uploadLog(stringBuilder.substring(0));
    }

    public ProxyStartupInfo getProxyStartupInfo() {
        return this.proxyStartupInfo;
    }

    public Path getPath() {
        return this.path;
    }

    public Process getProcess() {
        return this.process;
    }

    public int getPort() {
        return this.port;
    }

    public boolean isToShutdown() {
        return this.toShutdown;
    }

    public ScreenHandler getScreenHandler() {
        return this.screenHandler;
    }

    public ProcessStartupStage getProcessStartupStage() {
        return this.processStartupStage;
    }

    public Template getTemplate() {
        return this.template;
    }

    public long getStartupTime() {
        return this.startupTime;
    }

    public long getFinishedTime() {
        return this.finishedTime;
    }
}
