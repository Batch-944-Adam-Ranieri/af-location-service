package com.revature.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;


@Aspect
@Component
public class LoggingAspect {
//    private static Logger logger = Logger.getLogger(LoggingAspect.class);

    private static Logger logger = Logger.getLogger(LoggingAspect.class);
    /** Log incoming request */
    @Before("logAllMethods()")
    public void logRequest(JoinPoint joinPoint) {
        logger.info("Attempting incoming request: " + joinPoint.toString());
    }

    /** Log completed requests */
    @After("logAllMethods()")
    public void logAfterRequest(JoinPoint joinPoint) {
        logger.info("Completed request: " + joinPoint.toString());
    }

    /** Log errors/exceptions */
    @AfterThrowing("logAllMethods()")
    public void logAfterExceptionRequest(JoinPoint joinPoint) {
        logger.error("Exception thrown in method: " + joinPoint.toString());
    }

    /** Do the above on pointcuts within the application */
    @Pointcut("within(com.revature..*)")
    private void logAllMethods() {
        // Pointcut to log at every method call
    }
}
