package io.restfulness.util.pathfinder;

import io.restfulness.log.Log;
import io.javalor.lexer.SimpleLexer.Extractor;
import io.javalor.lexer.SimpleLexer.Token;
import io.javalor.lexer.SimpleLexer.Tokenizer;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PathFinder {


    private static final Tokenizer patternTokenizer;
    private static final Logger logger = Log.getLogger();

    static {
        patternTokenizer = new Tokenizer(PathTokenDefinition.getList(PathTokenDefinition.SORT_BY_PRIORITY));
    }

    public boolean match(String pattern, CharSequence path) {
        if ((pattern == null)||(path == null)) return false;
        logger.info("Matching pattern \\"+pattern+"\\ => \""+path+"\"");
       Matcher matcher = getCapturePattern(pattern).matcher(path);

       return matcher.find();
    }

    protected Pattern getCapturePattern(String pattern) {
        List<Token> tokenList = patternTokenizer.tokenize(pattern);
        logger.info("Pattern Token: "+tokenList.stream().map(t -> String.format("%s(\"%s\")", t.getDefinition().getName(), t.getData())).collect(Collectors.joining(", ")));
        Pattern capturePattern = Extractor.fromTokenList(tokenList).getCapturePattern();
        logger.info("Capture Pattern: "+capturePattern);
        return capturePattern;
    }
}
