package com.dhar.automation.common;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Hibernate;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import static org.springframework.util.ReflectionUtils.handleInvocationTargetException;

/**
 * @author Dharmendra.Singh
 */
public class HibernateUtil {
    public <T> T deProxy(T obj) {
        Set<Object> dejaVu = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
        try {
            deProxy(obj, dejaVu);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return obj;
    }

    private void deProxy(Object obj, Set<Object> dejaVu) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (dejaVu.contains(this)) {
            return;
        } else {
            dejaVu.add(this);

            if (!Hibernate.isInitialized(obj)) {
                Hibernate.initialize(obj);
            }
            PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(obj);
            for (PropertyDescriptor propertyDescriptor : properties) {
                Object origProp = PropertyUtils.getProperty(obj, propertyDescriptor.getName());
                if (origProp != null) {
                    this.deProxy(origProp, dejaVu);
                }
                if (origProp instanceof Collection && origProp != null) {
                    for (Object item : (Collection<?>) origProp) {
                        this.deProxy(item, dejaVu);
                    }
                }
            }
        }
    }
    /**
     * Handle the given reflection exception. Should only be called if no
     * checked exception is expected to be thrown by the target method.
     * <p>Throws the underlying RuntimeException or Error in case of an
     * InvocationTargetException with such a root cause. Throws an
     * IllegalStateException with an appropriate message else.
     * @param ex the reflection exception to handle
     */
    public static void handleReflectionException(Exception ex) {
        if (ex instanceof NoSuchMethodException) {
            throw new IllegalStateException("Method not found: " + ex.getMessage());
        }
        if (ex instanceof IllegalAccessException) {
            throw new IllegalStateException("Could not access method: " + ex.getMessage());
        }
        if (ex instanceof InvocationTargetException) {
            handleInvocationTargetException((InvocationTargetException) ex);
        }
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        }
        throw new UndeclaredThrowableException(ex);
    }
}
