package fr.zcraft.zsorter.model.serializer;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Gson factory used to call classes implementing PostProcessable interface.
 * @author Lucas
 *
 */
public class PostProcessAdapterFactory implements TypeAdapterFactory {
	/**
	 * Interface defining a post process method.
	 * @author Lucas
	 *
	 */
	public interface PostProcessable {
        /**
         * Method executed right after the deserialization of the object.<br>
         * This method can be used to compute values and recover the transient fields.
         */
        void postProcess();
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

        return new TypeAdapter<T>() {
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            public T read(JsonReader in) throws IOException {
                T obj = delegate.read(in);
                if (obj instanceof PostProcessable) {
                    ((PostProcessable)obj).postProcess();
                }
                return obj;
            }
        };
    }
}
