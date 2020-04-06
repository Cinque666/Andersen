package by.andersen.rz.cache;

import java.util.Set;

public interface IFrequencyCallObject<KeyType> {

    Set<KeyType> getMostFrequentlyUsedKeys();
    int getFrequencyOfCallingObject(KeyType key);
}
