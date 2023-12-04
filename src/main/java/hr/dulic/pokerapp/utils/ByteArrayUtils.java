package hr.dulic.pokerapp.utils;

import java.io.*;

public class ByteArrayUtils {

    public static <T extends Serializable> byte[] serializeObject(T object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            // Write the object to the output stream
            objectOutputStream.writeObject(object);

            // Return the byte array
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends Serializable> T deserializeObject(byte[] serializedData, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {

            // Read the object from the input stream
            Object object = objectInputStream.readObject();

            // Check if the deserialized object is of the expected class
            if (clazz.isInstance(object)) {
                return clazz.cast(object);
            } else {
                throw new ClassCastException("Deserialized object is not of the expected class");
            }

        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }
}
