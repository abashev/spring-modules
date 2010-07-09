package org.springmodules.cache.provider.jsr107;

import net.sf.jsr107cache.CacheManager;

import org.springmodules.cache.provider.AbstractCacheManagerFactoryBean;

/**
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 * @version $Id$
 */
public class Jsr107CacheManagerFactoryBean extends AbstractCacheManagerFactoryBean{
    private static final String CACHE_PROVIDER_NAME = "Jsr107Cache";

    private CacheManager cacheManager;
    
    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.AbstractCacheManagerFactoryBean#createCacheManager()
     */
    @Override
    protected void createCacheManager() throws Exception {
        cacheManager = CacheManager.getInstance();
    }

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.AbstractCacheManagerFactoryBean#destroyCacheManager()
     */
    @Override
    protected void destroyCacheManager() throws Exception {
        cacheManager = null;
    }

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.AbstractCacheManagerFactoryBean#getCacheProviderName()
     */
    @Override
    protected String getCacheProviderName() {
        return CACHE_PROVIDER_NAME;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    @Override
    public Object getObject() throws Exception {
        return cacheManager;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    @Override
    public Class<?> getObjectType() {
        return (cacheManager != null) ? cacheManager.getClass() : CacheManager.class;
    }
}
