package by.andersen.rz.loader;

import java.io.*;

public class MyClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //Проверяем, существует ли такой файл
        File f = new File(name+".class");
        if(!f.isFile())
            throw new ClassNotFoundException("No class found " + name);
        InputStream ins = null;
        try{
            //С помощью потока считываем файл в массив байт
            ins = new BufferedInputStream(new FileInputStream(f));
            byte[]b = new byte[(int)f.length()];
            ins.read(b);
            //С помощью функции defineClass загружаем класс
            Class c = defineClass("Hello", b, 0, b.length);
            return c;
        }catch (Exception e){
            e.printStackTrace();
            throw new ClassNotFoundException("Byte code troubles");
        }
        finally {
            try {
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
