import io.javalor.lexer.SimpleLexer.*;
import io.restfulness.server.http.HttpServer;
import io.restfulness.server.http.HttpServerFactory;
import io.restfulness.server.http.HttpServerProtocol;
import io.restfulness.util.pathfinder.PathTokenDefinition;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//https://github.com/spring-projects/spring-boot/blob/47516b50c39bd6ea924a1f6720ce6d4a71088651/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/web/embedded/tomcat/TomcatReactiveWebServerFactory.java
public class Main {

    public static void main(String[] args) throws IOException {

        Class<?> cls = Main.class;

        cls.
        ClassLoader classLoader = Main.class.getClassLoader();
        Enumeration<URL> roots = classLoader.getResources("");

        while(roots.hasMoreElements()){
            URL url = roots.nextElement();
            String path = url.getPath();
            System.out.println(path);

            File root = new File(path);
            File[] fileList = root.listFiles();
            if (fileList == null)
                continue;
            for (File file : fileList) {
                if (file.isDirectory()) {
                    String name = file.getName();
                    System.out.println(name);
                } else {
                    String name = file.getName();
                    System.out.println(name);

                }
            }
        }
        HttpServerFactory httpServerFactory = new HttpServerFactory();
        HttpServer httpServer = httpServerFactory.getServer(
                                    ServletLifeCycleExample.class.getCanonicalName(),
                                    HttpServerProtocol.HTTP11_NIO2, HttpServer.DEFAULT_PORT);
        httpServer.start();
        //System.out.println("Http Server '"+ServletLifeCycleExample.class.getCanonicalName()+"' Running on Port "+httpServer.getPort() );

        List<TokenDefinition> patternTokenDefinitions =
                PathTokenDefinition.getList(PathTokenDefinition.SORT_BY_PRIORITY);
        for (TokenDefinition patternTokenDefinition : patternTokenDefinitions) {
            //System.out.println("TOKEN "+ patternTokenDefinition.getPriority()+" "+ patternTokenDefinition.getName());
        }

        Tokenizer pathTokenizer = new Tokenizer(PathTokenDefinition.getList(PathTokenDefinition.SORT_BY_PRIORITY));
        //System.out.println("done Definition Token");
        List<Token> tokenList = pathTokenizer.tokenize("/hello/**/{varabc}/{vardef}");
        //System.out.println("done Path Token");
        Extractor extractor = Extractor.fromTokenList(tokenList);
        Pattern capturePattern = extractor.getCapturePattern();


        //System.out.println("Capture: "+capturePattern);



      String input = "";
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print("> ");
            input = scanner.nextLine();

            Matcher matcher = extractor.matcher(input);

            if (matcher.find()) {
                //System.out.println("Matched");
                System.out.println(matcher.group());
                Map<SegmentExtractor, String> entryList = extractor.extract(matcher.group());

                entryList.forEach((segmentExtractor, value) -> System.out.printf("%s = %s\n",segmentExtractor.getKeyName(),value));

            }
            else {
                //System.out.println("Not matched");
            }
            System.out.println("---");

//            Tokenizer pathTokenizer = new Tokenizer();
//
//            for (PatternToken patternToken :  pathTokenizer.tokenizePattern(input)) {
//                System.out.printf("%s %s(%d):%s \n",
//                        patternToken.data, patternToken.info.getName(), patternToken.info.getId(), patternToken.info.getRegex());
//            }
        } while(!("".equals(input)));

        System.out.println("**");
        httpServer.await();
    }


}
