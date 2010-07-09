package org.springmodules.cache.provider.jsr107;

import java.util.Arrays;

import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.AbstractFlushingModel;

/**
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 * @version $Id$
 */
public class Jsr107CacheFlushingModel extends AbstractFlushingModel {
    private static final long serialVersionUID = 776214744791329560L;
    
    /**
     * Names of the caches to flush.
     */
    private String[] cacheNames;

    /**
     * Constructor.
     *
     * @param csvCacheNames a comma-separated list containing the names of the EHCache caches
     *                      to flush separated by commas
     */
    public Jsr107CacheFlushingModel(String csvCacheNames) {
        setCacheNames(csvCacheNames);
    }

    /**
     * Constructor.
     *
     * @param newCacheNames the names of the EHCache caches to flush
     */
    public Jsr107CacheFlushingModel(String[] newCacheNames) {
        setCacheNames(newCacheNames);
    }

    /**
     * @return the names of the EHCache caches to flush
     */
    public String[] getCacheNames() {
        return cacheNames;
    }

    /**
     * Sets the names of the caches to flush.
     *
     * @param csvCacheNames a comma-separated list of Strings containing the names of the
     *                      caches to flush.
     */
    public void setCacheNames(String csvCacheNames) {
        String[] newCacheNames = null;
        
        if (csvCacheNames != null) {
            newCacheNames = StringUtils.commaDelimitedListToStringArray(csvCacheNames);
        }
        
        setCacheNames(newCacheNames);
    }

    /**
     * Sets the names of the EHCache caches to flush. It also removes any
     * duplicated cache names.
     *
     * @param newCacheNames the names of the caches
     */
    public void setCacheNames(String[] newCacheNames) {
        final String[] trimmedStrings = new String[newCacheNames.length];
        
        for (int i = 0; i < newCacheNames.length; i++) {
            trimmedStrings[i] = newCacheNames[i].trim();
        }
        
        cacheNames = trimmedStrings;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(cacheNames);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Jsr107CacheFlushingModel other = (Jsr107CacheFlushingModel) obj;
        if (!Arrays.equals(cacheNames, other.cacheNames))
            return false;
        return true;
    }
}
