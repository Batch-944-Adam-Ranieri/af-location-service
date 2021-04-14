
package com.revature.aspects;

import com.revature.dtos.UserDto;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Before any controller method is called, the verifyJWT is called to
 * make sure the jwt is valid. If valid, the user info is placed into
 * the first arg of the controller. If not valid, throws error.
 */
@Component
@Aspect
public class SecurityAspect {
    private Logger logger = Logger.getLogger(SecurityAspect.class);

    /**
     * WebClient injection for WebClientBuilder
     */
    @Autowired
    private WebClient.Builder webClientBuilder;
    public static String getEnv(String key) {
        return System.getenv(key);
    }

    /**
     * Called when pointcut method is implemented. Used for verifying incoming JWT at join point.
     *
     * @param pjp       Join Point
     * @return          If JWT is valid, sets the userDTO for the method at that join point.
     *                  If JWT is not valid, throws exception and returns not authorized request.
     * @throws Throwable when JWT cannot be verified.
     */
    @Around("controllerMethodsPointCut()")
    public Object verifyJwt(ProceedingJoinPoint pjp) throws Throwable {
        // PRODUCTION CODE
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String auth = request.getHeader("Authorization");

        try{
            logger.info("TEST TEST");
            UserDto userDTO = webClientBuilder.build()
                    .post().uri(System.getenv("AUTH_SERVER"))
                    .body(Mono.just(auth), String.class)
                    .retrieve()
                    .onStatus(httpStatus -> HttpStatus.UNAUTHORIZED.equals(httpStatus),
                            clientResponse -> {
                                Mono.empty();
                                return null;
                            })
                    .bodyToMono(UserDto.class).block();
            logger.info(userDTO);
            if (userDTO.getId() != 0){
                logger.info("JWT verified: " + userDTO);
                Object[] args = pjp.getArgs();
                args[0] = userDTO;
                return pjp.proceed(args);
            }
        }catch(Exception e){
            logger.error("Unable to verify JWT: " + e.getMessage());
            response.sendError(401, "Unable to verify JWT");
        }
        return null;

    }

    /**
     * Point cut method to run when @Verify is used at join point
     */
    @Pointcut("@annotation(com.revature.aspects.Verify)")
    private void controllerMethodsPointCut(){
        // needed for pointcut expression
    }}