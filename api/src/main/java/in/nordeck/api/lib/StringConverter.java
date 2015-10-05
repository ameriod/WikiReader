package in.nordeck.api.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * {@link Converter} that just returns the raw response as a string.
 * <p/>
 * Created by parker on 10/4/15.
 */
public class StringConverter implements Converter {

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        String text = null;
        try {
            text = fromStream(body.in());
        } catch (IOException e) {
            // no impl
        }

        return text;
    }

    @Override
    public TypedOutput toBody(Object object) {
        return null;
    }

    public String fromStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }
}