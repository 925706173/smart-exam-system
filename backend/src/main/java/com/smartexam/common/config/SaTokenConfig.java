package com.smartexam.common.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token鉴权配置
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 登录接口 - 放行
            SaRouter.match("/api/auth/login").stop();

            // Swagger接口 - 放行
            SaRouter.match("/swagger-ui/**", "/v3/api-docs/**").stop();

            // 静态资源 - 放行
            SaRouter.match("/static/**", "/favicon.ico").stop();

            // 学生接口 - 需要STUDENT角色
            SaRouter.match("/api/exam/**", "/api/practice/**", "/api/stats/student/**")
                    .check(r -> StpUtil.checkRole("STUDENT"));

            // 教师专属接口
            SaRouter.match("/api/questions/**", "/api/grading/**", "/api/stats/paper/**")
                    .check(r -> StpUtil.checkRole("TEACHER"));

            // 试卷接口 - 学生和教师都可访问（学生看试卷列表，教师管理试卷）
            SaRouter.match("/api/papers/**")
                    .check(r -> StpUtil.checkRoleOr("STUDENT", "TEACHER"));

            // 管理员接口
            SaRouter.match("/api/users/**").check(r -> StpUtil.checkRole("ADMIN"));

            // 班级接口 - 管理员和教师可访问
            SaRouter.match("/api/classes/**")
                    .check(r -> StpUtil.checkRoleOr("ADMIN", "TEACHER"));

            // 字典接口 - 管理员和教师可访问
            SaRouter.match("/api/dicts/**")
                    .check(r -> StpUtil.checkRoleOr("ADMIN", "TEACHER"));

            // 其他接口 - 需要登录
            SaRouter.match("/**").check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/api/**")
          .excludePathPatterns("/api/auth/login", "/api/auth/register");
    }
}
