package by.andersen.rz.cache;

import by.andersen.rz.cache.comparator.ComparatorClass;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

public class MemoryCacheClass<KeyType, ValueType> implements ICache<KeyType, ValueType>, IFrequencyCallObject<KeyType>{

    private HashMap<KeyType, String> hashMap;
    private TreeMap<KeyType, Integer> frequencyMap;
    private static final Logger LOGGER = LogManager.getLogger(MemoryCacheClass.class);

    public MemoryCacheClass(){
        hashMap = new HashMap<KeyType, String>();
        frequencyMap = new TreeMap<KeyType, Integer>();

        File tempFolder = new File("temp\\");
        if(!tempFolder.exists()){
            tempFolder.mkdirs();
        }
    }

    public void cache(KeyType key, ValueType value) throws IOException, ClassNotFoundException {
        String pathToObject;
        pathToObject = "temp\\" + UUID.randomUUID().toString() + ".temp";

        frequencyMap.put(key, 1);
        hashMap.put(key, pathToObject);

        FileOutputStream fileOutputStream = new FileOutputStream(pathToObject);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(value);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public ValueType getObject(KeyType key) throws IOException, ClassNotFoundException {
        if(hashMap.containsKey(key)){
            String pathToObject = hashMap.get(key);
            FileInputStream fileInputStream = new FileInputStream(pathToObject);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            try{
                ValueType deserializedObject = (ValueType)objectInputStream.readObject();

                int frequency = frequencyMap.remove(key);
                frequencyMap.put(key, ++frequency);

                return deserializedObject;
            } catch(IOException e){
                LOGGER.error("IOException while getting Object. " + e);
            } catch(ClassNotFoundException e){
                LOGGER.error("ClassNotFoundException. " + e);
            } finally {
                fileInputStream.close();
                objectInputStream.close();
            }
        }
        return null;
    }

    public void deleteObject(KeyType key) {
        if(hashMap.containsKey(key)){
            File deletingFile = new File(hashMap.remove(key));
            frequencyMap.remove(key);
            deletingFile.delete();
        }
    }

    public void clearCache() {
        for(KeyType key: hashMap.keySet()){
            File deletingFile = new File(hashMap.get(key));
            deletingFile.delete();
        }

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
