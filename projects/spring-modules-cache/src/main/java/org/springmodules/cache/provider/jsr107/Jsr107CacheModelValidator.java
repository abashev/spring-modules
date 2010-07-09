package org.springmodules.cache.provider.jsr107;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.provider.AbstractCacheModelValidator;
import org.springmodules.cache.provider.InvalidCacheModelException;

/**
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 * @version $Id$
 */
public class Jsr107CacheModelValidator extends AbstractCacheModelValidator {

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.AbstractCacheModelValidator#getCachingModelTargetClass()
     */
    @Override
    protected Class<? extends CachingModel> getCachingModelTargetClass() {
        return Jsr107CacheCachingModel.class;
    }

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.AbstractCacheModelValidator#getFlushingModelTargetClass()
     */
    @Override
    protected Class<? extends FlushingModel> getFlushingModelTargetClass() {
        return Jsr107CacheFlushingModel.class;
    }

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.AbstractCacheModelValidator#validateCachingModelProperties(java.lang.Object)
     */
    @Override
    protected void validateCachingModelProperties(Object cachingModel) throws InvalidCacheModelException {
        Jsr107CacheCachingModel model = (Jsr107CacheCachingModel) cachingModel;
        
        if (!StringUtils.hasText(model.getCacheName())) {
            throw new InvalidCacheModelException("Jsr107 cache name should not be empty");
        }
    }

    /* (non-Javadoc)
     * @see org.springmodules.cache.provider.AbstractCacheModelValidator#validateFlushingModelProperties(java.lang.Object)
     */
    @Override
    protected void validateFlushingModelProperties(Object flushingModel) throws InvalidCacheModelException {
        Jsr107CacheFlushingModel model = (Jsr107CacheFlushingModel) flushingModel;
        String[] caches = model.getCacheNames();

        if (ObjectUtils.isEmpty(caches)) {
            throw new InvalidCacheModelException("There should be at least one Jsr107 cache");
        }
    }
}
