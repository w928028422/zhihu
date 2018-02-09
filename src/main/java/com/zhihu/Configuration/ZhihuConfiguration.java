package com.zhihu.Configuration;

import com.zhihu.Interceptor.LoginRequiredInteceptor;
import com.zhihu.Interceptor.PassPortInteceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class ZhihuConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    private PassPortInteceptor passPortInteceptor;

    @Autowired
    private LoginRequiredInteceptor loginRequiredInteceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passPortInteceptor);
        registry.addInterceptor(loginRequiredInteceptor).addPathPatterns("/user/*", "/test");
        super.addInterceptors(registry);
    }
}
