package teamdev.tech.jbank.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    private final IpFilter ipFilter;

    public FilterConfig(IpFilter ipFilter) {
        this.ipFilter = ipFilter;
    }

    @Bean
    public FilterRegistrationBean<IpFilter> filterRegistrationBean() {
        var registration = new FilterRegistrationBean<IpFilter>();

        registration.setFilter(ipFilter);
        registration.setOrder(0);
//        registration.addUrlPatterns("/*");

        return registration;
    }
}
