package com.revature.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Used as the annotation to identify join points for the security aspect.
 * This annotation should be above every controller method that requires
 * JWT authorization.
 */
@Target(ElementType.METHOD)
public @interface Verify {
}