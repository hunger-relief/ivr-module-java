package com.dothat.location;

import com.dothat.location.data.State;

/**
 * Interface for all classes that resolve the state.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface StateResolver {
  State resolve(String code);
}
