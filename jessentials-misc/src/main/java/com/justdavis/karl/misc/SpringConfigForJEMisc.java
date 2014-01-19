package com.justdavis.karl.misc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * A Spring {@link Configuration} object that defines all of the
 * {@link Component}s, etc. provided by the <code>jessentials-misc</code>
 * project.
 */
@Configuration
@ComponentScan(basePackageClasses = { SpringConfigForJEMisc.class })
public class SpringConfigForJEMisc {
	/*
	 * No explicit bean definitions are currently needed here; everything is
	 * handled by the @ComponentScan annotation.
	 */
}
