package org.springmodules.cache.provider.jsr107;

import org.springmodules.cache.CachingModel;

/**
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 * @version $Id$
 */
public class Jsr107CacheCachingModel implements CachingModel {
    private static final long serialVersionUID = -8291087695037233893L;
    
    private String cacheName;

    /**
     * @param cacheName
     */
    public Jsr107CacheCachingModel(String cacheName) {
        setCacheName(cacheName);
    }

    /**
     * @return the cacheName
     */
    public String getCacheName() {
        return cacheName;
    }

    /**
     * @param cacheName the cacheName to set
     */
    public void setCacheName(String cacheName) {
        this.cacheName = cacheName.trim();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cacheName == null) ? 0 : cacheName.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Jsr107CacheCachingModel other = (Jsr107CacheCachingModel) obj;
        if (cacheName == null) {
            if (other.cacheName != null)
                return false;
        } else if (!cacheName.equals(other.cacheName))
            return false;
        return true;
    }
}
