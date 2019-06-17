/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import systems.reformcloud.config.ConfigLoader;
import systems.reformcloud.config.config.CloudFlareConfig;
import systems.reformcloud.dns.ADNSDefaultRecord;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.result.Result;
import systems.reformcloud.util.RequestMethod;
import systems.reformcloud.utility.StringUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author _Klaro | Pasqual K. / created on 09.05.2019
 */

public final class CloudFlareUtil implements Serializable {

    private static CloudFlareUtil instance;

    public CloudFlareUtil() {
        if (instance != null) {
            return;
        }

        instance = this;
        this.cloudFlareConfig = new ConfigLoader().load();

        if (this.cloudFlareConfig.getCloudFlareZone().isUseOwn()) {
            this.zoneID = this.cloudFlareConfig.getCloudFlareZone().getZoneID();
        } else {
            String currentZoneID = getZoneID();
            if (currentZoneID == null) {
                StringUtil.printError(
                    ReformCloudController.getInstance().getColouredConsoleProvider(),
                    "An error occurred in cloudflare addon",
                    new IllegalStateException(
                        "Could not find zone id for given domain, please recheck")
                );
                return;
            }

            zoneID = currentZoneID;
        }

        this.createClientEntries();
        this.createProxyEntries();
    }

    private List<Result> results = new LinkedList<>();
    private String zoneID;
    private CloudFlareConfig cloudFlareConfig;

    private String getZoneID() {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(
                "https://api.cloudflare.com/client/v4/zones?name=" + this.cloudFlareConfig
                    .getDomain()
            ).openConnection();
            httpURLConnection.setRequestMethod(RequestMethod.GET.getStringValue());
            httpURLConnection.setRequestProperty("X-Auth-Email", this.cloudFlareConfig.getEmail());
            httpURLConnection.setRequestProperty("X-Auth-Key", this.cloudFlareConfig.getApiToken());
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.connect();

            try (InputStream inputStream = httpURLConnection.getResponseCode() < 400
                ? httpURLConnection.getInputStream()
                : httpURLConnection.getErrorStream()) {
                JsonObject jsonObject = convertInputStreamToJson(inputStream);
                httpURLConnection.disconnect();
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonArray jsonArray = jsonObject.get("result").getAsJsonArray();
                    if (jsonArray.size() == 0) {
                        StringUtil.printError(
                            ReformCloudController.getInstance().getColouredConsoleProvider(),
                            "An error occurred in cloudflare addon",
                            new IllegalStateException(jsonObject.toString())
                        );
                        return null;
                    }

                    return jsonArray.get(0).getAsJsonObject().get("id").getAsString();
                } else {
                    StringUtil.printError(
                        ReformCloudController.getInstance().getColouredConsoleProvider(),
                        "An error occurred in cloudflare addon",
                        new IllegalStateException(
                            "Could not find zone id for given domain, please recheck")
                    );
                    return null;
                }
            }
        } catch (final IOException ex) {
            StringUtil.printError(
                ReformCloudController.getInstance().getColouredConsoleProvider(),
                "Error while opening connection",
                ex
            );
        }

        return null;
    }

    public void shutdown() {
        this.deleteRecords();
    }

    private void createProxyRecord(ADNSDefaultRecord.SRVRecord dnsRecord, ProxyInfo proxyInfo) {
        String json = ReformCloudLibraryService.GSON.toJson(dnsRecord);

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(
                "https://api.cloudflare.com/client/v4/zones/" + zoneID + "/dns_records"
            ).openConnection();
            httpURLConnection.setRequestMethod(RequestMethod.POST.getStringValue());
            httpURLConnection.setRequestProperty("Content-Length", json.getBytes().length + "");
            httpURLConnection.setRequestProperty("X-Auth-Email", this.cloudFlareConfig.getEmail());
            httpURLConnection.setRequestProperty("X-Auth-Key", this.cloudFlareConfig.getApiToken());
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            try (DataOutputStream dataOutputStream = new DataOutputStream(
                httpURLConnection.getOutputStream())) {
                dataOutputStream.writeBytes(json);
                dataOutputStream.flush();
            }

            try (InputStream inputStream = httpURLConnection.getResponseCode() < 400
                ? httpURLConnection.getInputStream()
                : httpURLConnection.getErrorStream()) {
                JsonObject jsonObject = convertInputStreamToJson(inputStream);
                if (jsonObject.get("success").getAsBoolean()) {
                    Result result = new Result(
                        jsonObject.get("result").getAsJsonObject().get("id").getAsString(),
                        this.cloudFlareConfig.getEmail(),
                        this.cloudFlareConfig.getApiToken(),
                        proxyInfo.getCloudProcess().getName()
                    );
                    results.add(result);
                } else {
                    StringUtil.printError(
                        ReformCloudController.getInstance().getColouredConsoleProvider(),
                        "An error occurred in cloudflare addon",
                        new IllegalStateException(jsonObject.toString())
                    );
                    return;
                }
            }

            httpURLConnection.disconnect();
        } catch (IOException ex) {
            StringUtil.printError(
                ReformCloudController.getInstance().getColouredConsoleProvider(),
                "Error while opening connection",
                ex
            );
        }
    }

    private void createClientRecord(ADNSDefaultRecord dnsRecord, Client client) {
        String json = ReformCloudLibraryService.GSON.toJson(dnsRecord);

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(
                "https://api.cloudflare.com/client/v4/zones/" + zoneID + "/dns_records"
            ).openConnection();
            httpURLConnection.setRequestMethod(RequestMethod.POST.getStringValue());
            httpURLConnection.setRequestProperty("Content-Length", json.getBytes().length + "");
            httpURLConnection.setRequestProperty("X-Auth-Email", this.cloudFlareConfig.getEmail());
            httpURLConnection.setRequestProperty("X-Auth-Key", this.cloudFlareConfig.getApiToken());
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            try (DataOutputStream dataOutputStream = new DataOutputStream(
                httpURLConnection.getOutputStream())) {
                dataOutputStream.writeBytes(json);
                dataOutputStream.flush();
            }

            try (InputStream inputStream = httpURLConnection.getResponseCode() < 400
                ? httpURLConnection.getInputStream()
                : httpURLConnection.getErrorStream()) {
                JsonObject jsonObject = convertInputStreamToJson(inputStream);
                if (jsonObject.get("success").getAsBoolean()) {
                    Result result = new Result(
                        jsonObject.get("result").getAsJsonObject().get("id").getAsString(),
                        this.cloudFlareConfig.getEmail(),
                        this.cloudFlareConfig.getApiToken(),
                        client.getName()
                    );
                    results.add(result);
                } else {
                    StringUtil.printError(
                        ReformCloudController.getInstance().getColouredConsoleProvider(),
                        "An error occurred in cloudflare addon",
                        new IllegalStateException(jsonObject.toString())
                    );
                    return;
                }
            }

            httpURLConnection.disconnect();
        } catch (IOException ex) {
            StringUtil.printError(
                ReformCloudController.getInstance().getColouredConsoleProvider(),
                "Error while opening connection",
                ex
            );
        }
    }

    private synchronized void deleteRecords() {
        List<Result> results1 = new ArrayList<>(results);
        results1.forEach(this::deleteRecord);
    }

    private synchronized void deleteRecord(Result result) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(
                "https://api.cloudflare.com/client/v4/zones/" + zoneID + "/dns_records/" + result
                    .getId()
            ).openConnection();
            httpURLConnection.setRequestMethod(RequestMethod.DELETE.getStringValue());
            httpURLConnection.setRequestProperty("X-Auth-Email", result.getEmail());
            httpURLConnection.setRequestProperty("X-Auth-Key", result.getToken());
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.connect();

            try (InputStream inputStream = httpURLConnection.getResponseCode() < 400
                ? httpURLConnection.getInputStream()
                : httpURLConnection.getErrorStream()) {
                JsonObject jsonObject = convertInputStreamToJson(inputStream);
                if (jsonObject.get("success").getAsBoolean()) {
                    results.remove(result);
                } else {
                    StringUtil.printError(
                        ReformCloudController.getInstance().getColouredConsoleProvider(),
                        "An error occurred in cloudflare addon",
                        new IllegalStateException(jsonObject.toString())
                    );
                }
            }
        } catch (final IOException ex) {
            StringUtil.printError(
                ReformCloudController.getInstance().getColouredConsoleProvider(),
                "Error while opening connection",
                ex
            );
        }
    }

    private void createClientEntries() {
        ReformCloudController.getInstance()
            .getInternalCloudNetwork()
            .getClients()
            .values()
            .forEach(this::createClientEntry);
    }

    private void createProxyEntries() {
        ReformCloudController.getInstance()
            .getInternalCloudNetwork()
            .getServerProcessManager()
            .getAllRegisteredProxyProcesses()
            .forEach(this::createProxyEntry);
    }

    public void createClientEntry(Client client) {
        if (client == null) {
            return;
        }

        Result result = this.find(client.getName());
        if (result != null) {
            this.deleteRecord(result);
        }

        ADNSDefaultRecord adnsDefaultRecord = new ADNSDefaultRecord(
            client.getName() + "." + this.cloudFlareConfig.getDomain(),
            client.getIp(),
            new JsonObject()
        );
        this.createClientRecord(adnsDefaultRecord, client);
    }

    public void deleteClientEntry(Client client) {
        if (client == null) {
            return;
        }

        Result result = this.find(client.getName());
        if (result != null) {
            this.deleteRecord(result);
        }
    }

    public void createProxyEntry(ProxyInfo proxyInfo) {
        if (proxyInfo == null) {
            return;
        }

        Result result = this.find(proxyInfo.getCloudProcess().getName());
        if (result != null) {
            this.deleteRecord(result);
        }

        CloudFlareConfig.CloudFlareGroup cloudFlareGroup = this
            .findGroup(proxyInfo.getProxyGroup().getName());
        if (cloudFlareGroup == null) {
            return;
        }

        ADNSDefaultRecord.SRVRecord srvRecord = new ADNSDefaultRecord.SRVRecord(
            "_minecraft._tcp." + this.cloudFlareConfig.getDomain(),
            "SRV 1 1 " + proxyInfo.getPort() + " " + proxyInfo.getCloudProcess().getClient() + "."
                + this.cloudFlareConfig.getDomain(),
            "_minecraft",
            "_tcp",
            cloudFlareGroup.getSubDomain().startsWith("@") ? this.cloudFlareConfig.getDomain()
                : cloudFlareGroup.getSubDomain(),
            1,
            1,
            proxyInfo.getPort(),
            proxyInfo.getCloudProcess().getClient() + "." + this.cloudFlareConfig.getDomain()
        );
        this.createProxyRecord(srvRecord, proxyInfo);
    }

    public void deleteProxyEntry(ProxyInfo proxyInfo) {
        if (proxyInfo == null) {
            return;
        }

        Result result = this.find(proxyInfo.getCloudProcess().getName());
        if (result != null) {
            this.deleteRecord(result);
        }
    }

    private Result find(String name) {
        return this.results
            .stream()
            .filter(e -> e.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    private CloudFlareConfig.CloudFlareGroup findGroup(String name) {
        return this.cloudFlareConfig.getCloudFlareGroups()
            .stream()
            .filter(Objects::nonNull)
            .filter(e -> e.getTargetProxyGroup().equals(name))
            .findFirst()
            .orElse(null);
    }

    private JsonObject convertInputStreamToJson(InputStream inputStream) {
        String input;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        try {
            while ((input = bufferedReader.readLine()) != null) {
                stringBuilder.append(input);
            }

        } catch (final IOException ex) {
            StringUtil.printError(
                ReformCloudController.getInstance().getColouredConsoleProvider(),
                "Error while reading cloudflare response",
                ex
            );
        }

        return ReformCloudLibraryService.PARSER.parse(stringBuilder.substring(0)).getAsJsonObject();
    }

    public static CloudFlareUtil getInstance() {
        return instance;
    }
}
