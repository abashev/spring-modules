package org.springmodules.cache.provider.jsr107;

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.util.ObjectUtils;
import org.springmodules.cache.CacheException;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.provider.AbstractCacheProviderFacade;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.ReflectionCacheModelEditor;

/**
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 * @version $Id$
 */
public class Jsr107CacheFacade extends AbstractCacheProviderFacade{
    private static final Lock createCacheLock = new ReentrantLock();

    private final Log log = LogFactory.getLog(Jsr107CacheFacade.class);
    
    private CacheManager cacheManager;
    private CacheFactory cacheFactory;
    
    private final Jsr107CacheModelValidator modelValidator = new Jsr107CacheModelValidator();
    
    private Map<String, Map<String, String>> cacheProperties;
    
    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.AbstractCacheProviderFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.CachingModel)
     */
    @Override
    protected Object onGetFromCache(Serializable key, CachingModel model) throws CacheException {
        Object cachedObject = null;

        try {
            Cache cache = getCache(model);

            cachedObject = cache.get(key);

            if (log.isDebugEnabled()) {
                if (cachedObject == null) {
                    log.debug("Missed value in cache with [key=" + key + "]");
                } else {
                    log.debug("Found value in cache with [key=" + key + "]");
                }
            }
        } catch (Exception exception) {
            throw new CacheAccessException(exception);
        }

        return cachedObject;
    }

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.AbstractCacheProviderFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.CachingModel, java.lang.Object)
     */
    @Override
    protected void onPutInCache(Serializable key, CachingModel model, Object obj) throws CacheException {
        try {
            Cache cache = getCache(model);
            
            cache.put(key, obj);
            
            if (log.isDebugEnabled()) {
                log.debug("Put value in cache with [key=" + key + "]");
            }
        } catch (Exception exception) {
            throw new CacheAccessException(exception);
        }
    }

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.AbstractCacheProviderFacade#onRemoveFromCache(java.io.Serializable, org.springmodules.cache.CachingModel)
     */
    @Override
    protected void onRemoveFromCache(Serializable key, CachingModel model) throws CacheException {
        try {
            Cache cache = getCache(model);

            cache.remove(key);

            if (log.isDebugEnabled()) {
                log.debug("Value with [key=" + key + "] successfully removed");
            }
        } catch (Exception exception) {
            throw new CacheAccessException(exception);
        }
    }

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.AbstractCacheProviderFacade#onFlushCache(org.springmodules.cache.FlushingModel)
     */
    @Override
    protected void onFlushCache(FlushingModel model) throws CacheException {
        Jsr107CacheFlushingModel flushingModel = (Jsr107CacheFlushingModel) model;
        String[] cacheNames = flushingModel.getCacheNames();

        if (!ObjectUtils.isEmpty(cacheNames)) {
            CacheException cacheException = null;

            try {
                for (String cacheName : cacheNames) {
                    Cache cache = getCache(cacheName);

                    cache.clear();
                    
                    if (log.isDebugEnabled()) {
                        log.debug("Did full cache flush for " + cacheName);
                    }
                }
            } catch (CacheException exception) {
                cacheException = exception;
            } catch (Exception exception) {
                cacheException = new CacheAccessException(exception);
            }

            if (cacheException != null) {
                throw cacheException;
            }
        }
    }

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.CacheProviderFacade#getCachingModelEditor()
     */
    @Override
    public PropertyEditor getCachingModelEditor() {
        ReflectionCacheModelEditor editor = new ReflectionCacheModelEditor();
        
        editor.setCacheModelClass(Jsr107CacheCachingModel.class);
        
        return editor;
    }

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.CacheProviderFacade#getFlushingModelEditor()
     */
    @Override
    public PropertyEditor getFlushingModelEditor() {
        Map<String, Object> propertyEditors = new HashMap<String, Object>();

        propertyEditors.put("cacheNames", new StringArrayPropertyEditor());

        ReflectionCacheModelEditor editor = new ReflectionCacheModelEditor();
        
        editor.setCacheModelClass(Jsr107CacheFlushingModel.class);
        editor.setCacheModelPropertyEditors(propertyEditors);
        
        return editor;
    }

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.CacheProviderFacade#modelValidator()
     */
    @Override
    public CacheModelValidator modelValidator() {
        return modelValidator;
    }

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.AbstractCacheProviderFacade#validateCacheManager()
     */
    @Override
    protected void validateCacheManager() throws FatalCacheException {
        assertCacheManagerIsNotNull(cacheManager);
    }

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.AbstractCacheProviderFacade#isSerializableCacheElementRequired()
     */
    @Override
    protected boolean isSerializableCacheElementRequired() {
        return true;
    }

    /**
     * This property is used for setting custom cache properties, like namespace for GAE memcache 
     * @return the cacheProperties
     */
    public Map<String, Map<String, String>> getCacheProperties() {
        return cacheProperties;
    }

    /**
     * @param cacheProperties the cacheProperties to set
     */
    public void setCacheProperties(Map<String, Map<String, String>> cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    /**
     * @param cacheManager the cacheManager to set
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.cacheFactory = null;
    }
    
    private CacheFactory getCacheFactory() throws net.sf.jsr107cache.CacheException {
        if (cacheFactory == null) {
            this.cacheFactory = cacheManager.getCacheFactory();
        }
        
        return cacheFactory;
    }
    
    private Cache getCache(CachingModel model) throws net.sf.jsr107cache.CacheException {
        Jsr107CacheCachingModel cachingModel = (Jsr107CacheCachingModel) model;
        
        return getCache(cachingModel.getCacheName());
    }
    
    private Cache getCache(String cacheName) throws net.sf.jsr107cache.CacheException {
        Cache cache = cacheManager.getCache(cacheName);
        
        if (cache == null) {
            createCacheLock.lock();
            
            try {
                // Maybe somebody already create cache?
                cache = cacheManager.getCache(cacheName);
                
                if (cache != null) {
                    return cache;
                }
                
                // Nope
                Map<String, String> props = Collections.emptyMap();
                
                if ((cacheProperties != null) && (cacheProperties.containsKey(cacheName))) {
                    props = cacheProperties.get(cacheName);
                }
                
                cache = getCacheFactory().createCache(props);
                
                cacheManager.registerCache(cacheName, cache);
                
                if (log.isInfoEnabled()) {
                    log.info("Create new cache [" + cacheName + "] with properties " + props.toString());
                }
            } finally {
                createCacheLock.unlock();
            }
        }
        
        return cache;
    }
}
