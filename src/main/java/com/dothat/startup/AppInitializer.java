package com.dothat.startup;

import com.google.appengine.api.utils.SystemProperty;
import com.google.cloud.datastore.DatastoreOptions;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppInitializer implements ServletContextListener {

  private static final Logger logger = LoggerFactory.getLogger(AppInitializer.class);
  private static final String LOCAL_APP_ID = "no_app_id";

  @Override
  public void contextInitialized(ServletContextEvent event) {
    String applicationId = SystemProperty.applicationId.get();
    logger.warn("#### Running with Application Id {} ####", applicationId);
    if (LOCAL_APP_ID.equals(applicationId)) {
      initLocal();
    } else {
      ObjectifyService.init();
    }
  }
  
  private void initLocal() {
    // Assumes that you are running a local data store by running the following command
    // gcloud beta emulators datastore start --host-port=localhost:8484
    ObjectifyService.init(new ObjectifyFactory(
        DatastoreOptions.newBuilder()
            .setHost("http://localhost:8484")
            .setProjectId("hunger-relief-dev")
            .build()
            .getService()
    ));
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // Perform Cleanup
  }
}
