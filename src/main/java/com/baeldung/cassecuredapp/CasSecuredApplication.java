package com.baeldung.cassecuredapp;

import com.baeldung.cassecuredapp.config.CasProperties;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@SpringBootApplication
@EnableConfigurationProperties({CasProperties.class})
public class CasSecuredApplication {

    private static final Logger logger = LoggerFactory.getLogger(CasSecuredApplication.class);

    public static void main(String... args) {
        SpringApplication.run(CasSecuredApplication.class, args);
    }



    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(
      AuthenticationManager authenticationManager,
      ServiceProperties serviceProperties) throws Exception {
        //定义cas认证过滤器
        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        //设置认证管理器
        filter.setAuthenticationManager(authenticationManager);
        //设置service属性
        filter.setServiceProperties(serviceProperties);
        return filter;
    }

    @Bean
    public ServiceProperties serviceProperties(CasProperties casProperties) {
        logger.info("service properties");
        //注册service属性
        ServiceProperties serviceProperties = new ServiceProperties();
        //此处对应挑战到cas登录页url后拼接的service参数，
        //即在CAS登录成功后会重定向到该url
        //http://localhost:8900/login/cas?ticket=xxx
        serviceProperties.setService(casProperties.getClient().getService());
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

    @Bean
    public TicketValidator ticketValidator(CasProperties casProperties) {
        //定义CAS server ticket验证器
        return new Cas30ServiceTicketValidator(casProperties.getServer().getBaseUrl());
    }

    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(
      TicketValidator ticketValidator,
      ServiceProperties serviceProperties) {
        //定义CAS认证
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        //设置service属性
        provider.setServiceProperties(serviceProperties);
        //设置ticket验证器
        provider.setTicketValidator(ticketValidator);
        //设置用户服务（即根据CAS登录用户名获取用户详细信息），此处可根据业务进行调整
        provider.setUserDetailsService(
          userName -> new User(userName, "***", true, true, true, true,
          AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
        //设置认证器标识KEY
        provider.setKey("CAS_PROVIDER_LOCALHOST_8900");
        return provider;
    }


    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        //定义security登出处理器
        return new SecurityContextLogoutHandler();
    }

    @Bean
    public LogoutFilter logoutFilter(CasProperties casProperties) {
        //定义登出过滤器
        LogoutFilter logoutFilter = new LogoutFilter(
                //在当前应用client登出成功后，需跳转页面（即需跳转到cas登出请求页）
                casProperties.getServer().getLogoutUrl(),
                securityContextLogoutHandler()
        );
        //设置在当前应用client中执行登出CAS的端点，
        //即http://client/logou/cas -> https://casServer/cas/logout
        //即http://localhost:8900/logout/cas -> https://localhost:8443/cas/logout
        logoutFilter.setFilterProcessesUrl(casProperties.getClient().getLogoutCasPath());
        return logoutFilter;
    }

    @Bean
    public SingleSignOutFilter singleSignOutFilter(CasProperties casProperties) {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        //singleSignOutFilter.setCasServerUrlPrefix("https://localhost:8443");
        singleSignOutFilter.setLogoutCallbackPath(casProperties.getClient().getSloCallbackPath());
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

}
