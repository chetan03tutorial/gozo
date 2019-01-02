/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *********************************************************************/

package com.lbg.ib.api.sales.common.spring;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextHolder implements ApplicationContextAware {
    private static final Holder HOLDER = new Holder();

    public static void autowire(Object object) {
        HOLDER.autowire(object);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        HOLDER.setApplicationContext(applicationContext);
    }

    public static <T> T getBean(final Class<T> requiredType) {
        return HOLDER.getBean(requiredType);
    }
    
    public static <T> T getBean(final String id, final Class<T> requiredType) {
        return HOLDER.getBean(id,requiredType);
    }

    /**
     * Creates a {@link javax.inject.Provider} object which supplies dependency
     * instance from a Spring context.
     * 
     * This method should be only used for legacy code dependency declared as a
     * static variable.
     * 
     * @param requiredType
     *            dependency class
     * @param <T>
     *            dependency type
     * @return returns a lazy provider for dependency which could be stored in a
     *         static global variable.
     */
    public static <T> Provider<T> autowiredStatic(final Class<T> requiredType) {
        return HOLDER.autowiredStatic(requiredType);
    }

    static class Holder {
        private ApplicationContext                         applicationContext;

        // this stops us from writing to toAutowire from one thread while
        // reading from it in another
        private Lock                                       lock          = new ReentrantLock();

        // this holds any object that have requested autowiring before the
        // context was available
        private List<WeakReference<Object>>                toAutowire    = new LinkedList<WeakReference<Object>>();

        // this holds all caching providers which should be cleaned while
        // assigning another context.
        private final List<WeakReference<LazyProvider<?>>> lazyProviders = new ArrayList<WeakReference<LazyProvider<?>>>();

        public void setApplicationContext(ApplicationContext applicationContext) {
            lock.lock();
            try {
                // setting this static valid has to be done from a static method
                // otherwise
                // checkstyle objects.
                this.applicationContext = applicationContext;

                List<WeakReference> toRemove = new LinkedList<WeakReference>();

                for (WeakReference<Object> ref : toAutowire) {
                    Object object = ref.get();

                    // using weak references so need to null guard
                    if (object != null) {
                        applicationContext.getAutowireCapableBeanFactory().autowireBean(object);
                    }

                    // we've autowired this so add it to the list of things to
                    // remove
                    toRemove.add(ref);
                }

                // remove all autowired entries
                toAutowire.removeAll(toRemove);
            } finally {
                lock.unlock();
            }
        }

        public void autowire(final Object object) {
            if (applicationContext != null) {
                // got an application context so will autowire this
                applicationContext.getAutowireCapableBeanFactory().autowireBean(object);

                return;
            }

            lock.lock();
            try {
                if (applicationContext != null) {
                    // applicationContext was set while we were waiting for the
                    // lock to be released
                    applicationContext.getAutowireCapableBeanFactory().autowireBean(object);
                } else {
                    // we will autowire this later
                    toAutowire.add(new WeakReference<Object>(object));
                }
            } finally {
                lock.unlock();
            }
        }

        public <T> T getBean(final Class<T> requiredType) {
            return context().getBean(requiredType);
        }

        public <T> Provider<T> autowiredStatic(final Class<T> requiredType) {
            final LazyProvider<T> provider = new LazyProvider<T>(requiredType);
            lazyProviders.add(new WeakReference<LazyProvider<?>>(provider));
            return provider;
        }

        private ApplicationContext context() {
            if (applicationContext != null) {
                return applicationContext;
            }
            // Memory barrier (memory synchronization) to read variable if it is
            // initialised
            // by a concurrent thread
            lock.lock();
            try {
                if (applicationContext != null) {
                    return applicationContext;
                }
            } finally {
                lock.unlock();
            }
            throw new FatalBeanException("Application context is not ready yet, initialisation is not complete");
        }

        private class LazyProvider<T> implements Provider<T> {
            private final Class<T> requiredType;

            private T              object;

            public LazyProvider(final Class<T> requiredType) {
                this.requiredType = requiredType;
            }

            public T get() {
                if (object == null) {
                    // we don't need any synchronisation, as all threads will
                    // get the same value
                    // anyway
                    object = getBean(requiredType);
                }
                assert object != null;
                return object;
            }

            public void clean() {
                // We assume it will be only used for tests, never for
                // production code. So, no
                // multi-threading problems.
                object = null;
            }
        }
        
        public <T> T getBean(final String beanName, Class<T> clazz) {
        	return context().getBean(beanName,clazz);
        }
    }

}
