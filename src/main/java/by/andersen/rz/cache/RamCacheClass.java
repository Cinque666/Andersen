package by.andersen.rz.cache;

import by.andersen.rz.cache.comparator.ComparatorClass;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

public class RamCacheClass<KeyType, ValueType> implements ICache<KeyType, ValueType>, IFrequencyCallObject<KeyType> {

    private HashMap<KeyType, ValueType> hashMap;
    private TreeMap<KeyType, Integer> frequencyMap;

    public RamCacheClass(){
        hashMap = new HashMap<KeyType, ValueType>();
        frequencyMap = new TreeMap<KeyType, Integer>();
    }

    public void cache(KeyType key, ValueType value) throws IOException, ClassNotFoundException {
        frequencyMap.put(key, 1);
        hashMap.put(key, value);
    }

    public ValueType getObject(KeyType key) throws IOException, ClassNotFoundException {
        if(hashMap.containsKey(key)){
            int frequency = frequencyMap.get(key);
            frequencyMap.put(key, ++frequency);
            return hashMap.get(key);
        } else {
            return null;
        }
    }

    public void deleteObject(KeyType key) {
        if(hashMap.containsKey(key)){
            hashMap.remove(key);
            frequencyMap.remove(key);
        }
    }

    public void clearCache() {
        hashMap.clear();
        frequencyMap.clear();
    }

    public ValueType removeObject(KeyType key) throws IOException, ClassNotFoundException {
        if(hashMap.containsKey(key)){
            ValueType result = this.getObject(key);
            this.deleteObject(key);
            return result;
        }
        return null;
    }

    public boolean containsKey(KeyType key) {
        return hashMap.containsKey(key);
    }

    public int size() {
        return hashMap.size();
    }

    public Set<KeyType> getMostFrequentlyUsedKeys() {
        Comparator comparator = new ComparatorClass(frequencyMap);
        TreeMap<KeyType, Integer> sorted = new TreeMap(comparator);
        sorted.putAll(frequencyMap);
        return sorted.keySet();
    }

    public int getFrequencyOfCallingObject(KeyType key) {
        if(hashMap.containsKey(key)){
            return frequencyMap.get(key);
        }
        return 0;
    }
}
