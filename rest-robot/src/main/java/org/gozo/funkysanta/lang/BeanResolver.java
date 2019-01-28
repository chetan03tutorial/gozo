    package org.gozo.funkysanta.lang;

    import java.lang.ref.WeakReference;
    import java.util.ArrayList;
    import java.util.LinkedList;
    import java.util.List;
    import java.util.concurrent.locks.Lock;
    import java.util.concurrent.locks.ReentrantLock;

    import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
    import org.springframework.beans.factory.support.BeanDefinitionRegistry;
    import org.springframework.beans.factory.support.GenericBeanDefinition;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

    @Component
    public class BeanResolver implements ApplicationContextAware {
        private static final Holder HOLDER = new Holder();

        public static void autowire(Object object) {
            HOLDER.autowire(object);
        }

        public void setApplicationContext(ApplicationContext applicationContext) {
            
            /*BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
            BeanDefinition bd = new GenericBeanDefinition();*/
            HOLDER.setApplicationContext(applicationContext);
        }

        public <T> void registerBeanDefinition(String beanName, Class<T> beanClass){
            HOLDER.registerBean(beanName,beanClass);
        }

        public <T> T getBean(final Class<T> requiredType) {
            return HOLDER.getBean(requiredType);
        }

        public <T> T getBean(final String id, final Class<T> requiredType) {
            return HOLDER.getBean(id,requiredType);
        }

        public Object getBeanByName(final String id){
            return HOLDER.getBeanByName(id);
        }

        static class Holder {
            private ApplicationContext applicationContext;
            private Lock lock = new ReentrantLock();

            // this holds any object that have requested autowiring before the
            // context was available
            private List<WeakReference<Object>>                toAutowire    = new LinkedList<WeakReference<Object>>();


            public void setApplicationContext(ApplicationContext applicationContext) {
                lock.lock();
                try {
                    this.applicationContext = applicationContext;
                    List<WeakReference> toRemove = new LinkedList<WeakReference>();

                    for (WeakReference<Object> ref : toAutowire) {
                        Object object = ref.get();
                        // using weak references so need to null guard
                        if (object != null) {
                            applicationContext.getAutowireCapableBeanFactory().autowireBean(object);
                        }
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
            
            public <T> void registerBean(String beanName, Class<T> beanClass) {
        	AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        	BeanDefinitionRegistry registry = (BeanDefinitionRegistry)beanFactory;
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        	beanDefinition.setAutowireCandidate(true);
        	beanDefinition.setBeanClass(beanClass);
        	registry.registerBeanDefinition(beanName, beanDefinition);
            }

            public <T> T getBean(final Class<T> requiredType) {
                return context().getBean(requiredType);
            }

            public Object getBeanByName(final String id) {
                return context().getBean(id);
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

            public <T> T getBean(final String beanName, Class<T> clazz) {
                return context().getBean(beanName,clazz);
            }
        }

    }
