package cn.langpy.kotime.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * zhangchang
 * an annotation to listen kotime's results after invoking a method.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface KoListener {
    String value() default "";
}
