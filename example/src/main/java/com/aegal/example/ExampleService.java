package com.aegal.example;

import com.aegal.example.resources.ExampleResource;
import com.aegal.framework.core.MicroserviceBundle;
import com.aegal.framework.core.MicroserviceConfig;
import com.aegal.framework.core.tenacity.InitializeTenacity;

import com.yammer.tenacity.core.bundle.BaseTenacityBundleConfigurationFactory;
import com.yammer.tenacity.core.bundle.TenacityBundleBuilder;
import com.yammer.tenacity.core.bundle.TenacityBundleConfigurationFactory;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;
import com.yammer.tenacity.core.properties.TenacityPropertyKeyFactory;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * User: A.Egal
 * Date: 9/6/14
 * Time: 10:25 PM
 */
public class ExampleService extends Application<MicroserviceConfig> {

    public static void main(String[] args) throws Exception {
        new ExampleService().run(args);
    }

    public enum DependencyKeys implements TenacityPropertyKey {
        Action;

        public static TenacityPropertyKeyFactory getTenacityPropertyKeyFactory() {
            return new TenacityPropertyKeyFactory(){
                public TenacityPropertyKey from(String value) {
                    return DependencyKeys.valueOf(value.toUpperCase());
                }
            };
        }
    }

    @Override
    public void initialize(Bootstrap<MicroserviceConfig> bootstrap) {
    	 TenacityBundleConfigurationFactory<Configuration> configurationFactory = new BaseTenacityBundleConfigurationFactory<Configuration>() {
 			
 			@Override
 			public TenacityPropertyKeyFactory getTenacityPropertyKeyFactory(Configuration applicationConfiguration) {
 				// TODO Auto-generated method stub
 				return DependencyKeys.getTenacityPropertyKeyFactory();
 			}
 			
 			
 		};
 		bootstrap.addBundle( TenacityBundleBuilder.newBuilder().configurationFactory(configurationFactory).build());
    }

    @Override
    public void run(MicroserviceConfig exampleConfiguration, Environment environment) throws Exception {
        environment.jersey().register(ExampleResource.class);

        InitializeTenacity.initialize(DependencyKeys.values());
    }
}
