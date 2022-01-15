package com.example.weblogin.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity // 해당 파일로 시큐리티 활성화, springSecurityFilterChain가 자동으로 포함
@Configuration // IoC 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encoder() {
        // DB 패스워드 암호화
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http); // 이 코드 삭제하면 기존 시큐리티가 가진 모든 기능 비활성화
        http.csrf().disable(); // csrf 토큰 비활성화 코드

        http.authorizeRequests()
                .antMatchers("/", "/main/**").authenticated() // 로그인을 요구함
                .antMatchers("/admin/**").hasRole("ADMIN")  // 해당 url patterns(/admin/**)은 ADMIN에게만 허용
                .anyRequest().permitAll() // 그게 아닌 모든 주소는 인증 필요 없음
                .and()
                //.mvcMatchers("/", "/member/**", "/item/**").permitAll()
                //.mvcMatchers("/admin/**").hasRole("ADMIN")
                //.anyRequest().authenticated();



                // 로그인하는 경우에 대해 설정함
                .formLogin()
                .loginPage("/member/signin") // 인증필요한 주소로 접속하면 이 주소로 이동시킴
                .loginProcessingUrl("/member/signin") // 스프링 시큐리티가 로그인 자동 진행 POST방식으로 로그인 진행
                .defaultSuccessUrl("/") // 로그인이 정상적이면 "/" 로 이동
                .and()
                .logout();



        // http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }

    // static 디렉터리의 하위 파일은 인증을 무시하도록 설정
    //@Override
    //public void configure(WebSecurity web) throws Exception {
        //web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    //}


    @Override public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}
