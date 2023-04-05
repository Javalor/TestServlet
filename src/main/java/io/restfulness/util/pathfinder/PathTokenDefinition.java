package io.restfulness.util.pathfinder;

import io.javalor.lexer.SimpleLexer.SegmentExtractor;
import io.javalor.lexer.SimpleLexer.SegmentExtractorGenerator;
import io.javalor.lexer.SimpleLexer.TokenDefinition;

import java.util.*;

import static io.javalor.lexer.SimpleLexer.SegmentExtractor.*;

public class PathTokenDefinition extends TokenDefinition {

    private static final List<TokenDefinition> PATH_TOKEN_DEFINITION_LIST = new LinkedList<>();

    static {

        PATH_TOKEN_DEFINITION_LIST.addAll(
            Arrays.asList(
                    new PathTokenDefinition("SEGMENT_SEPARATOR", 300, 47, Q("/"),
                            extractMatchingPattern("\\/+")),

                    new PathTokenDefinition("ONE_CHAR", 500, 63, Q("?"),
                            extractMatchingPattern(CHAR)),
                    new PathTokenDefinition("SEQ_CHAR", 300, 42, Q("*"),
                            extractMatchingPattern(CHAR+"*")),

                    new PathTokenDefinition("SEQ_SEGMENT_SEPARATOR", 200, 2042, Q("/**"),
                            extractMatchingPattern("[\\/\\w\\.\\-\n ]*")),

                    new PathTokenDefinition("REGEX_VARIABLE", 200, 3001,
                            "\\{"+VARIABLE_NAME+":.+\\}", extractVariable()),

                    new PathTokenDefinition("VARIABLE", 300, 3002,
                        "\\{"+VARIABLE_NAME+"\\}", extractVariable()),
                    new PathTokenDefinition("VARIABLE_CAPTURE_ALL",300, 3003,
                            "\\{*"+VARIABLE_NAME+"\\}",extractVariable()),

                    new PathTokenDefinition("WORD", 9000, WORD ),


                    new PathTokenDefinition("ESCAPE_OPEN_CURL_BRACKET", 100, 9101, Q("\\{")),
                    new PathTokenDefinition("ESCAPE_CLOSE_CURL_BRACKET", 100, 9102, Q("\\}")),

                    new PathTokenDefinition("NO_DELIMITER_DOUBLE_VARIABLE", 300, 20001, "\\{" + ".*" + "}\\{"),
                    new PathTokenDefinition("ILLEGAL_IDENTIFIER_VARIABLE", 350, 20001, "\\{" + ".*" + "}"),
                    new PathTokenDefinition("UNTERMINATED_VARIABLE", 400, 20002, "\\{" + "[^\\/]*")
            )
        );
    }

    protected PathTokenDefinition(String name, int priority, int id, String regex) {
        super(name, priority, id, regex);
    }

    protected PathTokenDefinition(String name, int id, String regex) {
        super(name, id, regex);
    }

    protected PathTokenDefinition(String name, int id, String regex, Object metaData) {
        super(name, id, regex, metaData);
    }

    protected PathTokenDefinition(String name, int priority, int id, String regex, SegmentExtractorGenerator segmentExtractorGenerator) {
        super(name, priority, id, regex, segmentExtractorGenerator);
    }

    protected PathTokenDefinition(String name, int id, String regex, SegmentExtractorGenerator segmentExtractorGenerator) {
        super(name, id, regex, segmentExtractorGenerator);
    }

    protected PathTokenDefinition(String name, int id, String regex, SegmentExtractorGenerator segmentExtractorGenerator, Object metaData) {
        super(name, id, regex, segmentExtractorGenerator, metaData);
    }


    public static List<TokenDefinition> getList(Comparator<? super TokenDefinition> sortComparator) {
        List<TokenDefinition> cloned = new LinkedList<>(PATH_TOKEN_DEFINITION_LIST);
        if (sortComparator != null) {
            cloned.sort(sortComparator);
        }
        return Collections.unmodifiableList(cloned);
    }

    public static SegmentExtractorGenerator extractVariable() {
        return PathTokenDefinition::extractVariable;
    }
    public static SegmentExtractor extractVariable(final int segmentIndex,
                                                   final TokenDefinition tokenDefinition,
                                                   final String tokenData) {
        String variableName;
        String extractPattern;

        switch (tokenDefinition.getName()) {
            case "VARIABLE": //{...}
                variableName = tokenData.substring(1, tokenData.length() - 1);
                extractPattern = "[A-Za-z0-9 \\_\\-\\.\\{\\}\\:\n]+";
                break;
            case "VARIABLE_CAPTURE_ALL": //{*...}
                variableName = tokenData.substring(2, tokenData.length() - 1);
                extractPattern = "[A-Za-z0-9 \\_\\-\\.\\{\\}\\:\\\n/]+";
                break;
            case "REGEX_VARIABLE": //{...:...}
                int idxColon = tokenData.indexOf(':');
                variableName = tokenData.substring(1, idxColon);
                extractPattern = tokenData.substring(idxColon+1,tokenData.length()-1);
                break;
            default:
                return extractAsIs(segmentIndex, tokenDefinition, tokenData);
        }

        return generateExtractor(segmentIndex, tokenDefinition, tokenData, variableName, extractPattern);
    }

}
