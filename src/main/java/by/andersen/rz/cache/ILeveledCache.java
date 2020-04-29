package by.andersen.rz.cache;


import java.io.IOException;

public interface ILeveledCache<KeyType, ValueType> extends ICache<KeyType, ValueType>, IFrequencyCallObject<KeyType> {

    void recache() throws IOException, ClassNotFoundException;
}
