package main.init.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be placed on attribute's setter methods or fields for autowiring. The annotation will tell to call
 * the setter method and pass the appropriately registered instance of class as an argument or will assign the
 * appropriately registered instance of class to the field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface Setter
{
	/**
	 * Returns the name of registered class instance.
	 */
	String name() default "";
}