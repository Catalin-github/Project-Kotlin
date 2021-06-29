package com.project.dia.web_mvc.config_security

//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.reactive.CorsConfigurationSource
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

//import com.project.dia.web_mvc.Component.oauth2.Oauth2AuthenticationSuccessHandler
//import com.project.dia.web_mvc.Component.oauth2.Oauth2AuthenticationSuccessHandler
import com.project.dia.web_mvc.Service.UserService
import com.project.dia.web_mvc.config_security.filters.AuthenticationFilter
import com.project.dia.web_mvc.config_security.filters.AuthorizationFilter
import com.project.dia.web_mvc.constants.SecurityKey
import com.project.dia.web_mvc.repository.UserJpaRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import javax.servlet.http.HttpServletRequest


@Configuration
@EnableWebSecurity
class SecurityConfiguration(var bCryptPasswordEncoder: BCryptPasswordEncoder,  var userDetailsService: UserService, var userJpaRepository: UserJpaRepository) : WebSecurityConfigurerAdapter() {


    override fun configure(http: HttpSecurity) {
        val securityKey: SecurityKey = SecurityKey()

        http
                .csrf().disable()
                 .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/user/accountConfirm")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/account")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/facebook")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/isAuthenticated")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/login")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/google")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/loginlogin")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/logout")
                .permitAll()

                .antMatchers(HttpMethod.POST, "/api/user/request-reset-password")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/register")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/update")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/reset-password")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(getAuthenticationFilter())
                .addFilter(AuthorizationFilter(authenticationManager(), userJpaRepository))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.cors { c ->
            val cs = CorsConfigurationSource { r: HttpServletRequest? ->
                val cc = CorsConfiguration()
                cc.allowCredentials = true
                cc.allowedOrigins = mutableListOf("http://localhost:3000")
                cc.addAllowedMethod("OPTIONS")
                cc.addAllowedMethod("GET")
                cc.addAllowedMethod("PUT")
                cc.addAllowedMethod("POST")
                cc.addAllowedMethod("PATCH")
                cc.addAllowedMethod("*")
                cc.addAllowedHeader("*")
                cc.addAllowedHeader("Origin")
                cc.addAllowedHeader("X-Requested-With")
                cc.addAllowedHeader("Content-Type")
                cc.addAllowedOriginPattern("http://localhost:3000")
                cc.addAllowedHeader("Accept")
                cc

            }
            c.configurationSource(cs)
        }

    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder)

    }

    fun getAuthenticationFilter(): AuthenticationFilter {
        val filter = AuthenticationFilter(authenticationManager())
        filter.setFilterProcessesUrl("/api/user/login")

        return filter
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }


    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = mutableListOf("http://localhost:3000")
        configuration.addAllowedHeader("*")
        configuration.addAllowedHeader("Origin")
        configuration.addAllowedHeader("X-Requested-With")
        configuration.addAllowedHeader("Content-Type")
        configuration.addAllowedHeader("Accept")
        configuration.addAllowedMethod("OPTIONS")
        configuration.addAllowedMethod("GET")
        configuration.addAllowedMethod("PUT")
        configuration.addAllowedMethod("POST")
        configuration.addAllowedMethod("DELETE")
        configuration.addAllowedMethod("PATCH")
        configuration.addAllowedMethod("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }


}