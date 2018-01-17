package main.java.init.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be placed on methods for creation of class instance.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Instance
{
	/**
	 * Returns the name of registered class instance.
	 */
	String name() default "";
}