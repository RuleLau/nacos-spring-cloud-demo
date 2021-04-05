package com.rule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 请配置这个，以保证在刷新Token时能成功刷新
//    @Autowired
//    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
//        // 配置用户来源于数据库
//        // 配置密码加密方式  BCryptPasswordEncoder，添加用户加密的时候请也用这个加密
//        auth.userDetailsService(userDetailsService()).passwordEncoder(new MyPasswordEncoder());
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()// 禁用跨站攻击
                .authorizeRequests()
                .antMatchers("/oauth/**", "/login/**",  "/login.html",
                        "/success.html", "/fail.html")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
//                .loginPage("/login.html")
                .loginProcessingUrl("/demo-login")
                .failureForwardUrl("/login/fail")
                .permitAll();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

