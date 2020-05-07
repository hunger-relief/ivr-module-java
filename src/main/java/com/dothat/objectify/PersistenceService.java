package com.dothat.objectify;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

/**
 * Global instance of the ObjectifyService that should be initialized only once.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class PersistenceService {
  static {
    JodaTimeTranslators.add(factory());
  }
  
  public static Objectify service() {
    return ObjectifyService.ofy();
  }

  public static ObjectifyFactory factory() {
    return ObjectifyService.factory();
  }
}
