package CocktailOfTheDay.project.Intercepter;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

public class intercepterConfig implements WebMvcConfigurer{

    /*

        jwt토큰을 처리작업을 하기위한 두번째과정
        writer : mintcho95
        date : 2023.07.16

     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> urls = new ArrayList<>();
        urls.add("/api/upload"); // 인터셉터 whitelist

        registry.addInterceptor(new intercepter()).addPathPatterns(urls); // whitelist url add
    }
}
