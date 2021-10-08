package com.baeldung.cassecuredapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CAS相关配置
 *
 * @author luohq
 * @date 2021-10-08 16:41
 */
@ConfigurationProperties(prefix = CasProperties.PREFIX)
public class CasProperties {

    public static final String PREFIX = "cas";

    /**
     * CAS server配置
     */
    private ServerProperties server;
    /**
     * CAS client配置
     */
    private ClientProperties client;

    public ServerProperties getServer() {
        return server;
    }

    public void setServer(ServerProperties server) {
        this.server = server;
    }

    public ClientProperties getClient() {
        return client;
    }

    public void setClient(ClientProperties client) {
        this.client = client;
    }

    /**
     * CAS server配置
     */
    public static class ServerProperties {
        /**
         * CAS server基础URL
         */
        private String baseUrl;
        /**
         * CAS server登录页面URL
         */
        private String loginUrl;
        /**
         * CAS server登出URL
         */
        private String logoutUrl;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getLoginUrl() {
            return loginUrl;
        }

        public void setLoginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
        }

        public String getLogoutUrl() {
            return logoutUrl;
        }

        public void setLogoutUrl(String logoutUrl) {
            this.logoutUrl = logoutUrl;
        }
    }

    /**
     * CAS client配置
     */
    public static class ClientProperties {
        /**
         * CAS client基础URL
         */
        private String baseUrl;
        /**
         * CAS client service参数（需匹配CAS service注册中的serviceId）
         */
        private String service;
        /**
         * CAS client在当前应用中登出cas的urlPath（请求该url则自动重定向到cas.server.logout-url）
         */
        private String logoutCasPath;
        /**
         * CAS client在当前应用中单点登出的回调urlPath
         */
        private String sloCallbackPath;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getLogoutCasPath() {
            return logoutCasPath;
        }

        public void setLogoutCasPath(String logoutCasPath) {
            this.logoutCasPath = logoutCasPath;
        }

        public String getSloCallbackPath() {
            return sloCallbackPath;
        }

        public void setSloCallbackPath(String sloCallbackPath) {
            this.sloCallbackPath = sloCallbackPath;
        }
    }
}
