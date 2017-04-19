package org.onehippo.ckeditor;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class for manipulating JSON.
 */
class Json {

    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES,true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
    }

    public static ObjectNode object() throws IOException {
        return mapper.createObjectNode();
    }

    public static ObjectNode object(final String json) throws IOException {
        final String toParse = StringUtils.isBlank(json) ? "{}" : json;
        final JsonNode result = mapper.readTree(toParse);
        if (result.isObject()) {
            return (ObjectNode) result;
        }
        throw new IOException("Not a JSON object: " + toParse);
    }

    public static ArrayNode array() {
        return mapper.createArrayNode();
    }

    public static ArrayNode array(final String json) throws IOException {
        final String toParse = StringUtils.isBlank(json) ? "[]" : json;
        final JsonNode result = mapper.readTree(toParse);
        if (result.isArray()) {
            return (ArrayNode) result;
        }
        throw new IOException("Not a JSON array: " + toParse);
    }

    /**
     * Overlays a JSON object on top of another one. New values will be copied to the existing object.
     * Existing primitive values and arrays will replace existing ones. Existing objects will be merged
     * recursively.
     *
     * For example, if the existing object is:
     * {
     *     a: 'aaa',
     *     b: true
     *     c: {
     *         d: 'ddd',
     *         e: {
     *             f: 'fff'
     *         },
     *         g: {
     *             h: 'hhh'
     *         }
     *     }
     * }
     *
     * And the overlayed values are:
     * {
     *     a: 'AAA',
     *     c: {
     *         d: 'DDD',
     *         e: {
     *             y: 'yyy'
     *         },
     *         g: false
     *     },
     *     x: false
     * }
     *
     * The resulting object is:
     * {
     *     a: 'AAA',
     *     b: true,
     *     c: {
     *         d: 'DDD',
     *         e: {
     *             f: 'fff',
     *             y: 'yyy'
     *         },
     *         g: false
     *     },
     *     x: false
     * }
     *
     * @param object the object to add new values to and replace existing value in
     * @param values the object with the values to add or replace existing ones with
     */
    public static void overlay(final ObjectNode object, final JsonNode values) {
        if (values == null) {
            return;
        }
        for (Iterator<String> fields = values.fieldNames(); fields.hasNext(); ) {
            final String field = fields.next();
            final JsonNode oldValue = object.get(field);
            final JsonNode newValue = values.get(field);

            if (oldValue == null || oldValue.isNull()) {
                object.set(field, newValue);
            } else if (oldValue.isObject() && newValue.isObject()) {
                overlay((ObjectNode)oldValue, newValue);
            } else {
                object.replace(field, newValue);
            }
        }
    }

    /**
     * Appends a JSON object to an existing one. New values will be copied to the existing object.
     * Strings will be appended to existing strings using a comma as separator. Arrays will be joined with existing
     * arrays, otherwise existing arrays will get the new values appended. Existing objects will be appended recursively.
     *
     * For example, if the existing object is:
     * {
     *     a: 'a1',
     *     b: {
     *         c: [ 'c1', 'c2' ]
     *         d: [ 'd1', 'd2' ]
     *     }
     * }
     *
     * And the appended object is:
     * {
     *     a: 'a2',
     *     b: {
     *         c: 'c3',
     *         d: [ 'd3', 'd4' ],
     *         e: false
     *     }
     * }
     *
     * The resulting object will be:
     * {
     *     a: 'a1,a2',
     *     b: {
     *         c: [ 'c1', 'c2', 'c3' ],
     *         d: [ 'd1', 'd2', 'd3', 'd4' ],
     *         e: false
     *     }
     * }
     *
     * @param object the object to append values to
     * @param values the object with the values to append
     */
    public static void append(final ObjectNode object, final JsonNode values) {
        if (values == null) {
            return;
        }
        for (Iterator<String> fields = values.fieldNames(); fields.hasNext(); ) {
            final String field = fields.next();
            final JsonNode oldValue = object.get(field);
            final JsonNode newValue = values.get(field);

            if (oldValue == null || oldValue.isNull()) {
                object.set(field, newValue);
            } else {
                if (oldValue.isObject() && newValue.isObject()) {
                    append( (ObjectNode) oldValue, newValue);
                } else if (oldValue.isTextual() && newValue.isTextual()) {
                    appendToCommaSeparatedString(object, field, newValue.asText());
                } else if (oldValue.isArray()) {
                    if (newValue.isArray()) {
                        ArrayNode concatenated = concatArrays(oldValue, newValue);
                        object.set(field, concatenated);
                    } else {
                        ((ArrayNode)oldValue).add(newValue);
                    }
                }
            }
        }
    }

    public static void appendToCommaSeparatedString(final ObjectNode object, final String key, final String value) {
        if (object.has(key)) {
            final String commaSeparated = object.get(key).asText();
            if (StringUtils.isBlank(commaSeparated)) {
                object.put(key, value);
            } else if (StringUtils.trim(commaSeparated).endsWith(",")) {
                object.put(key, commaSeparated + value);
            } else {
                object.put(key, commaSeparated + ',' + value);
            }
        } else {
            object.put(key, value);
        }
    }

    private static ArrayNode concatArrays(final JsonNode... arrays) {
        ArrayNode result = array();
        for (JsonNode array : arrays) {
            for (int i = 0; i < array.size(); i++) {
                result.add(array.get(i));
            }
        }
        return result;
    }
}

