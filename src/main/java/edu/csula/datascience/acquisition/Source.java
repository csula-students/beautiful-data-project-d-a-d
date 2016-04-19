package edu.csula.datascience.acquisition;

import java.util.Collection;
import java.util.Iterator;

/**
 * Data source interface
 *
 * Primary purpose of this interface is to provide data from the data source
 *
 * In provide method, you are going to implement your own data acquisition and
 * parse them to collection of models in memory
 *
 * After storing these objects in memory, then you can download them using
 * Collector#mungee
 */
public interface Source<T> extends Iterator<Collection<T>> {
}
