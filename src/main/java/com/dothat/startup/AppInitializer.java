package com.dothat.startup;

import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppInitializer implements ServletContextListener {
  
  @Override
  public void contextInitialized(ServletContextEvent event) {
    ObjectifyService.init();
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // Perform Cleanup
  }
}
