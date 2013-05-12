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
public class Generator {

    public static void main(String[] args) {
        final Map<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("clazz", List.class);

        final Function<String, String> upperFunction = new TemplateFunction() {

            public String apply(String aInput) {
                return aInput.toUpperCase();
            }

        };
        scopes.put("upper", upperFunction);
        //scopes.put("classmethodlist", List.class.getMethods());
        
        
        Class clazz = List.class;
        
        final List<Method> methods = new ArrayList<Generator.Method>();
        for (final java.lang.reflect.Method method : clazz.getMethods())
        {
            final List<String> parameterTypes = new ArrayList<String>();
            for (final Class arg : method.getParameterTypes())
            {
                parameterTypes.add(arg.getName());
            }
            
            methods.add(new Method(method.getName(), method.getReturnType().getName(), parameterTypes));
        }
        
        Clazz clazz1 = new Clazz(clazz.getName(), clazz.getPackage().getName(), methods);
        
        
        
        
        scopes.put("clazz", clazz1);
        scopes.put("methods", new DecoratedCollection(Arrays.asList(clazz.getMethods())));
        
        
        
        scopes.put("methods1", Arrays.asList(clazz.getMethods()));
        
        scopes.put("methods1", Arrays.asList(clazz.getMethods()));
        Writer writer = new OutputStreamWriter(System.out);
        DefaultMustacheFactory mf = new DefaultMustacheFactory();

//        
//        mf.setObjectHandler(new ReflectionObjectHandler() {
//            @Override
//            public Object coerce(Object object) {
//              if (object instanceof Collection) {
//                return  new DecoratedCollection((Collection) object);
//              }
//              
//              return super.coerce(object);
//            }
//          });
        
        
        Mustache mustache = mf.compile("interface-java.mustache");
        
        mustache.execute(writer, scopes);
        try {
            writer.flush();
        } catch (IOException e) {
            //
        }
    }
    
    
    static class Clazz
    {
        String name;
        String packageName;
        List<Method> methods;
        
        public Clazz(String name, String packageName, List<Method> methods) {
            super();
            this.name = name;
            this.packageName = packageName;
            this.methods = methods;
        }

    }
    
    static class Method
    {
        String name;
        String returnType;
        DecoratedCollection<String> parameterTypes;
        public Method(String name, String returnType, Collection<String> parameterTypes) {
            super();
            this.name = name;
            this.returnType = returnType;
            this.parameterTypes = new DecoratedCollection(parameterTypes);
        }
        
    }
    
//    public static void main(String[] args) {
//        
//        final Map<String, Object> data = new HashMap<String, Object>();
//        data.put("class", List.class);
//        final String text = "{{#class}}{{#bold}}{{name}}{{/bold}}\n" +
//        		"{{#methods}}" +
//        		"{{returnType.name}} {{name}}({{#parameterTypes}}{{^-first}}, {{/-first}}{{simpleName}} arg{{-index}}{{/parameterTypes}});\n" +
//        		"{{/methods}}" +
//        		"" +
//        		"" +
//        		"{{/class}}";
//        final Template tmpl = Mustache.compiler().compile(text);
//        
//        
//        Mustache.Lambda bold = new Mustache.Lambda() {
//            public void execute (Template.Fragment frag, Writer out) throws IOException {
//                //out.write("<b>");
//                out.write(frag.execute().toUpperCase());
//                //out.write("</b>");
//            }
//        };
//        data.put("bold", bold);
//        
//        System.out.println(tmpl.execute(data));
//        
//    }
}
