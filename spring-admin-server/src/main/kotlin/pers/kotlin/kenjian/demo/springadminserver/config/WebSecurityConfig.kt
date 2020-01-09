package pers.kotlin.kenjian.demo.springadminserver.config

import de.codecentric.boot.admin.server.config.AdminServerProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.util.*

@Profile("secure")
@Configuration(proxyBeanMethods = false)
class WebSecurityConfig : WebSecurityConfigurerAdapter {

    private val adminServer: AdminServerProperties

    constructor(adminServer: AdminServerProperties) {
        this.adminServer = adminServer
    }

    override fun configure(http: HttpSecurity?) {

        val successHandler = SavedRequestAwareAuthenticationSuccessHandler()
        successHandler.setTargetUrlParameter("redirectTo")
        successHandler.setDefaultTargetUrl(this.adminServer.path("/"))

        http?.authorizeRequests()
                ?.antMatchers(this.adminServer.path("/assets/**"))?.permitAll()
                ?.antMatchers(this.adminServer.path("/login"))?.permitAll()
                ?.anyRequest()?.authenticated()
                ?.and()
                ?.formLogin()
                ?.loginPage(this.adminServer.path("/login"))
                ?.successHandler(successHandler)
                ?.and()
                ?.logout()?.logoutUrl(this.adminServer.path("/logout"))
                ?.and()
                ?.httpBasic()
                ?.and()
                ?.csrf()
                ?.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                ?.ignoringRequestMatchers(
                        AntPathRequestMatcher(this.adminServer.path("/instances"), HttpMethod.POST.toString()),
                        AntPathRequestMatcher(this.adminServer.path("/instances/*"), HttpMethod.DELETE.toString()),
                        AntPathRequestMatcher(this.adminServer.path("/actuator/**")))
                ?.and()
                ?.rememberMe()?.key(UUID.randomUUID().toString())?.tokenValiditySeconds(1209600)
    }
}