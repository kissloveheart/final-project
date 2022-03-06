package com.hiep.finalproject.config;


import com.hiep.finalproject.oauth2.CustomAuthenticationSuccessHandler;
import com.hiep.finalproject.oauth2.CustomOAuth2UserService;
import com.hiep.finalproject.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;
    @Autowired
    private PersistentTokenRepository persistentTokenRepository;
    @Autowired
    ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;





/*    @Bean
    public ConcurrentSessionFilter concurrentSessionFilter(){
        SimpleRedirectSessionInformationExpiredStrategy expiredSessionStrategy =
                new SimpleRedirectSessionInformationExpiredStrategy("/login?expire=true");
        return new ConcurrentSessionFilter(sessionRegistry(),expiredSessionStrategy);
    }*/

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Sét đặt dịch vụ để tìm kiếm User trong Database.
        // Và sét đặt PasswordEncoder.
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        // Các trang không yêu cầu login
        http.authorizeRequests().antMatchers("/", "/login/**", "/logout","forget_password/**"
                        ,"/regis/**","/reset_password/**","/campaign/**").permitAll();

        // Trang /userInfo yêu cầu phải login với vai trò ROLE_USER hoặc ROLE_ADMIN.
        // Nếu chưa login, nó sẽ redirect tới trang /login.
        http.authorizeRequests().antMatchers("/user_info","/edit_profile","/change_password","/donation")
                .hasAnyAuthority("USER", "ADMIN");

        // Trang chỉ dành cho ADMIN
        http.authorizeRequests().antMatchers("/management/**").hasAuthority("ADMIN");

        // Khi người dùng đã login, với vai trò XX.
        // Nhưng truy cập vào trang yêu cầu vai trò YY,
        // Ngoại lệ AccessDeniedException sẽ ném ra.
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");


        // Cấu hình cho Login Form.
        http.authorizeRequests().and().formLogin()//
                // Submit URL của trang login
                .loginProcessingUrl("/j_spring_security_check") // Submit URL
                .loginPage("/login")//
                .defaultSuccessUrl("/user_info")//
                .failureUrl("/login?error=true")//
                .usernameParameter("email")//
                .passwordParameter("password")
                .failureHandler(authenticationFailureHandler)
                .and().oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint().userService(new CustomOAuth2UserService())
                .and().successHandler(successHandler)
                // Cấu hình cho Logout Page.
                .and().logout()
                .logoutSuccessHandler(oidcLogoutSuccessHandler())
                //.logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID").clearAuthentication(true)
                .invalidateHttpSession(true)
                .and().sessionManagement().maximumSessions(5)
                .expiredUrl("/login?expire=true");


        //addFilterBefore(concurrentSessionFilter(), ConcurrentSessionFilter.class);

        // Cấu hình Remember Me.
        http.authorizeRequests().and().rememberMe().rememberMeCookieName("Login").tokenRepository(persistentTokenRepository)
                .tokenValiditySeconds(24*60*60); // 24h

    }

    private OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("http://localhost:8080/");
        return successHandler;
    }



//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
//        db.setDataSource(dataSource);
//        db.setCreateTableOnStartup(true);
//        return db;
//    }
}
