import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.TemplateFunction;
import com.github.mustachejava.util.DecoratedCollection;
import com.google.common.base.Function;

/**
 * <p>Generator. </p>
 * 
 * @author anavarro - May 12, 2013
 * 
 */
public final class MapDtoGenerator {

    /**
     * main.
     * 
     * @param args
     */
    public static void main(String[] args) {
        final Class<?> clazz = Player.class;

        final Map<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("class", clazz);
        
        
        final List<Map<String, String>> fields = new ArrayList<Map<String, String>>();

        for (final java.lang.reflect.Method method : clazz.getMethods()) {
            if (method.getName().startsWith("get")) {
                final Map<String, String> fieldsMap = new HashMap<String, String>();
                fieldsMap.put("name", method.getName().substring(3));
                fieldsMap.put("type", method.getReturnType().getSimpleName());
                fields.add(fieldsMap);
            }
        }

        scopes.put("fields", fields);
        final Function<String, String> upperFunction = new Function<String, String>() {

            public String apply(String aInput) {
                return aInput.toUpperCase();
            }

        };
        scopes.put("upper", upperFunction);
        
        final Function<String, String> underscoreFunction = new Function<String, String>() {

            public String apply(String aInput) {
                return aInput.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
            }

        };
        scopes.put("underscore", underscoreFunction);
        
        
        Writer writer = new OutputStreamWriter(System.out);
        DefaultMustacheFactory mf = new DefaultMustacheFactory();

        Mustache mustache = mf.compile("map-dto.mustache");

        mustache.execute(writer, scopes);
        try {
            writer.flush();
        } catch (IOException e) {
            //
        }
    }

}
