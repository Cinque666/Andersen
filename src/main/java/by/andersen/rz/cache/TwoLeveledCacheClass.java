package by.andersen.rz.cache;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class TwoLeveledCacheClass<KeyType, ValueType extends Serializable> implements ILeveledCache<KeyType, ValueType> {

    private RamCacheClass<KeyType, ValueType> ramCache;
    private MemoryCacheClass<KeyType, ValueType> memoryCache;
    private int maxRamCacheCapacity;
    private int numberOfRequests;
    private int numberOfRequestsForRecache;
    private static final Logger LOGGER = LogManager.getLogger(TwoLeveledCacheClass.class);

    public TwoLeveledCacheClass(int maxRamCacheCapacity, int numberOfRequestsForRecache){
        this.maxRamCacheCapacity = maxRamCacheCapacity;
        this.numberOfRequestsForRecache = numberOfRequestsForRecache;
        ramCache = new RamCacheClass<KeyType, ValueType>();
        memoryCache = new MemoryCacheClass<KeyType, ValueType>();
        numberOfRequests = 0;
    }

    public void recache() throws IOException, ClassNotFoundException {
        TreeSet<KeyType> ramKeySet = new TreeSet<KeyType>(ramCache.getMostFrequentlyUsedKeys());
        int boundFrequency = 0;

        for(KeyType key: ramKeySet){
            boundFrequency += ramCache.getFrequencyOfCallingObject(key);
        }
        boundFrequency /= ramKeySet.size();

        for(KeyType key: ramKeySet){
            if(ramCache.getFrequencyOfCallingObject(key) <= boundFrequency){
                memoryCache.cache(key, ramCache.removeObject(key));
            }
        }

        TreeSet<KeyType> memoryKeySet = new TreeSet<KeyType>(memoryCache.getMostFrequentlyUsedKeys());
        for(KeyType key: memoryKeySet){
            try{
                if(memoryCache.getFrequencyOfCallingObject(key) > boundFrequency);{
                    ramCache.cache(key, memoryCache.removeObject(key));
                }
            } catch(IOException e){
                memoryCache.deleteObject(key);
                LOGGER.error("IOException. " + e);
                continue;
            } catch(ClassNotFoundException e){
                LOGGER.error("ClassNotFoundException. " + e);
                continue;
            }
        }
    }

    public void cache(KeyType key, ValueType value) throws IOException, ClassNotFoundException {
        ramCache.cache(key, value);
    }

    public ValueType getObject(KeyType key) throws IOException, ClassNotFoundException {
        if(ramCache.containsKey(key)){
            numberOfRequests++;
            if(numberOfRequests > numberOfRequestsForRecache){
                this.recache();
                numberOfRequests = 0;
            }
            return ramCache.getObject(key);
        }
        if(memoryCache.containsKey(key)){
            numberOfRequests++;
            if(numberOfRequests > numberOfRequestsForRecache){
                this.recache();
                numberOfRequests = 0;
            }
            return memoryCache.getObject(key);
        }
        return null;
    }

    public void deleteObject(KeyType key) {
        if (ramCache.containsKey(key)) {
            ramCache.deleteObject(key);
        }
        if(memoryCache.containsKey(key)){
            memoryCache.deleteObject(key);
        }
    }

    public void clearCache() {
        memoryCache.clearCache();
        ramCache.clearCache();
    }

    public ValueType removeObject(KeyType key) throws IOException, ClassNotFoundException {
        if(ramCache.containsKey(key)){
            return ramCache.removeObject(key);
        }
        if(memoryCache.containsKey(key)){
            return memoryCache.removeObject(key);
        }
        return null;
    }

    public boolean containsKey(KeyType key) {
        if(ramCache.containsKey(key)){
            return true;
        }
        return memoryCache.containsKey(key);
    }

    public int size() {
        return ramCache.size() + memoryCache.size();
    }

    public Set<KeyType> getMostFrequentlyUsedKeys() {
        TreeSet<KeyType> set = new TreeSet<KeyType>(ramCache.getMostFrequentlyUsedKeys());
        set.addAll(memoryCache.getMostFrequentlyUsedKeys());
        return set;
    }

    public int getFrequencyOfCallingObject(KeyType key) {
        if(ramCache.containsKey(key)){
            return ramCache.getFrequencyOfCallingObject(key);
        }
        if(memoryCache.containsKey(key)){
            return memoryCache.getFrequencyOfCallingObject(key);
        }
        return 0;
    }
}
