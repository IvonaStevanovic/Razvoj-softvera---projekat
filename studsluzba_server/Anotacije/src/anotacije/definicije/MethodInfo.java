package anotacije.definicije;

import java.lang.annotation.*;

/*
 * Anotacija je dostupna u runtime-u
 */
@Retention(RetentionPolicy.RUNTIME)
/*
 * Samo metode se mogu anotirati ovom anotacijom.
 */
@Target(ElementType.METHOD)
public @interface MethodInfo {
	/**
	 * Atribut anotacije moze imati podrazumevanu vrednost
	 */
	boolean deprecated() default true;

	String version();
}
