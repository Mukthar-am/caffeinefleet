package com.caffeine.fleet.utils.general;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Mukthar.M@myntra.com on 17/08/16.
 * <p>
 * General java utilities
 */
public class JsonUtils {
    private static Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * - Modifies JsonNode's value of execution particular field in the JsonTree
     *
     * @param jsonTree  - Entire JsonNode
     * @param fieldName - The field name to be modified.
     * @param newValue  - Value by which, it should be replaced with.
     */
    public static void modifyJsonTreeFieldOccurrences(JsonNode jsonTree, String fieldName, String newValue) {
        if (jsonTree.has(fieldName)) {
            ((ObjectNode) jsonTree).put(fieldName, newValue);
        }

        // Now, recursively invoke this method on all properties
        for (JsonNode child : jsonTree) {
            modifyJsonTreeFieldOccurrences(child, fieldName, newValue);
        }
    }


    /**
     * @param jsonTree       - entire jsonnode tree
     * @param leafValue      - the value to appear at the leaf node
     * @param nodePathSplits - split of the json path Eg: "changeEntryLog.Item.itemInfo.orderId"
     * @param startFromNode  - value is always, 0 when called.
     */
    public static void modifyJsonTree(JsonNode jsonTree,
                                        Object leafValue,
                                        String[] nodePathSplits,
                                        int startFromNode,
                                        boolean dropNode) throws Exception {

        int pathLength = nodePathSplits.length;
        String currentNode = nodePathSplits[startFromNode];

        if (startFromNode == pathLength - 1) {
            if (!dropNode) {
                LOG.debug(" Modifying leaf value of instance-type: " + leafValue.getClass());
                if (leafValue instanceof Long) {
                    Long valueToSet = Long.parseLong(leafValue.toString());
                    LOG.debug("Leaf node: " + currentNode + ", will be modified with the \"Integer\" value: " + valueToSet);
                    ((ObjectNode) jsonTree).put(currentNode, valueToSet);
                }
                else if (leafValue instanceof Integer) {
                    int valueToSet = Integer.parseInt(leafValue.toString());
                    LOG.debug("Leaf node: " + currentNode + ", will be modified with the \"Integer\" value: " + valueToSet);
                    ((ObjectNode) jsonTree).put(currentNode, valueToSet);
                }
                else if (leafValue instanceof String) {
                    LOG.debug("Leaf node: " + currentNode + ", will be modified with the \"String\" value: " + leafValue);
                    ((ObjectNode) jsonTree).put(currentNode, leafValue.toString());
                }
                else if (leafValue instanceof Boolean) {
                    boolean valueBoolean = Boolean.parseBoolean(leafValue.toString());
                    LOG.debug("Leaf node: " + currentNode + ", will be modified with the \"Boolean\" value: " + valueBoolean);
                    ((ObjectNode) jsonTree).put(currentNode, valueBoolean);
                }
                else if (leafValue instanceof Double) {
                    Double valueDouble = Double.parseDouble(leafValue.toString());
                    LOG.debug("Leaf node: " + currentNode + ", will be modified with the \"Double\" value: " + valueDouble);
                    ((ObjectNode) jsonTree).put(currentNode, valueDouble);
                }

            } else {
                LOG.debug("# Dropping leaf node now.");
                ((ObjectNode) jsonTree).remove(currentNode);
            }

            return;
        }

        if (startFromNode < pathLength) {
            if (jsonTree.has(currentNode)) {
                jsonTree = jsonTree.get(currentNode);
                modifyJsonTree(jsonTree, leafValue, nodePathSplits, ++startFromNode, dropNode);
            }
            else {
                throw new Exception("Json tree node \"" + currentNode + "\" is NOT found in the node path sequence.");
            }
        }
    }


    public static void addToJsonTree(JsonNode jsonTree,
                                      Object leafValue,
                                      String[] nodePathSplits,
                                      int startFromNode,
                                      boolean dropNode) throws Exception {

        int pathLength = nodePathSplits.length;
        String currentNode = nodePathSplits[startFromNode];


        if (startFromNode == pathLength - 1) {
            if (!dropNode) {
                LOG.debug(" Modifying leaf value of instance-type: " + leafValue.getClass());
                if (leafValue instanceof Long) {
                    Long valueToSet = Long.parseLong(leafValue.toString());
                    LOG.debug("Leaf node: " + currentNode + ", will be modified with the \"Integer\" value: " + valueToSet);
                    ((ObjectNode) jsonTree).put(currentNode, valueToSet);
                }
                else if (leafValue instanceof Integer) {
                    int valueToSet = Integer.parseInt(leafValue.toString());
                    LOG.debug("Leaf node: " + currentNode + ", will be modified with the \"Integer\" value: " + valueToSet);
                    ((ObjectNode) jsonTree).put(currentNode, valueToSet);
                }
                else if (leafValue instanceof String) {
                    LOG.debug("Leaf node: " + currentNode + ", will be modified with the \"String\" value: " + leafValue);
                    ((ObjectNode) jsonTree).put(currentNode, leafValue.toString());
                }
                else if (leafValue instanceof Boolean) {
                    boolean valueBoolean = Boolean.parseBoolean(leafValue.toString());
                    LOG.debug("Leaf node: " + currentNode + ", will be modified with the \"Boolean\" value: " + valueBoolean);
                    ((ObjectNode) jsonTree).put(currentNode, valueBoolean);
                }
                else if (leafValue instanceof Double) {
                    Double valueDouble = Double.parseDouble(leafValue.toString());
                    LOG.debug("Leaf node: " + currentNode + ", will be modified with the \"Double\" value: " + valueDouble);
                    ((ObjectNode) jsonTree).put(currentNode, valueDouble);
                }

            } else {
                LOG.debug("# Dropping leaf node now.");
                ((ObjectNode) jsonTree).remove(currentNode);
            }

            return;
        }

        if (startFromNode < pathLength) {
            if (jsonTree.has(currentNode)) {
                jsonTree = jsonTree.get(currentNode);
                addToJsonTree(jsonTree, leafValue, nodePathSplits, ++startFromNode, dropNode);
            }
            else {
                ObjectMapper mapper = new ObjectMapper();
                ((ObjectNode) jsonTree).put(currentNode, mapper.createObjectNode());
                addToJsonTree(jsonTree, leafValue, nodePathSplits, startFromNode, dropNode);
            }
        }
    }


    /**
     * @param jsonTree
     * @param nodePathSplits
     * @return
     */
    public static Object getJsonTreeLeaf(JsonNode jsonTree, String[] nodePathSplits) {
        LOG.debug("# Fetching leaf for the path - " + nodePathSplits[nodePathSplits.length - 1]);

        String field = null;
        JsonNode currentJsonTree = jsonTree;

        int i = 0;
        for (; i < nodePathSplits.length - 1; i++) {
            field = nodePathSplits[i];

            if (currentJsonTree.has(field)) {
                currentJsonTree = currentJsonTree.get(field);
            }
        }

        field = nodePathSplits[i];
        if (currentJsonTree.has(field)) {
            return currentJsonTree.get(field);
        }

        LOG.error("# Field = " + field + ", was NOT found in the tree.");
        return null;
    }


    /**
     * - Converts execution map to execution json String
     *
     * @param map
     * @return
     */
    public static String convert(Map<String, String> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }

    /**
     * Checks if the obtained string is execution valid JSON string or not
     *
     * @param jsonInString
     * @return - true|false
     */
    public static boolean isJsonValid(String jsonInString) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        boolean isValid = true;

        try {
            mapper.readTree(jsonInString);

        } catch (JsonProcessingException e) {
            isValid = false;
            LOG.error("", e);

        }
        return isValid;
    }


    /**
     * @param jsonFile
     * @return
     * @throws IOException
     */
    public static boolean isJsonValid(File jsonFile) {
        LOG.debug("Check if the file is valid: " + jsonFile.getAbsolutePath());

        boolean isValid = false;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(jsonFile.getAbsolutePath()));
            String jsonInString = new String(encoded, "UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);

            isValid = true;

            mapper.readTree(jsonInString);

        } catch (JsonProcessingException e) {
            isValid = false;
            LOG.error("JsonProcessingException Cause:", e.getCause());
            LOG.error("JsonProcessingException has occurred:", e);

        } catch (UnsupportedEncodingException e) {
            LOG.error("UnsupportedEncodingException", e);

        } catch (IOException e) {
            LOG.error("IOException", e);
        }

        return isValid;
    }

    /**
     * @param json
     */
    public static void isGsonValid(String json) {
        Gson gson = new Gson();
        gson.fromJson(json, Object.class);
    }

    /**
     * @param jsonFile
     * @return
     * @throws IOException
     */
    public static Object isGsonValid(File jsonFile) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(jsonFile.getAbsolutePath()));
        String jsonString = new String(encoded, "UTF-8");

        Gson gson = new Gson();
        Object gsonObj = gson.fromJson(jsonString, Object.class);

        if (gsonObj == null) {
            LOG.debug("Found GSON null....");
        }
        return gsonObj;
    }

    /**
     * @param value
     * @return
     */
    public static Map<String, Object> convertStringToMap(String value) {
        Map<String, Object> map = new HashMap<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);

            // convert JSON string to Map
            map = mapper.readValue(value, new TypeReference<Map<String, Object>>() {
            });

        } catch (IOException e) {
            LOG.error("Exception", e);
        }

        return map;
    }

    /**
     * @param inputJsoTextFile
     * @return
     * @throws IOException
     */
    public static JsonNode convertToJsonNode(File inputJsoTextFile) throws IOException {
        LOG.debug("# Converting input file to JsonNode.");

        if (!isJsonValid(inputJsoTextFile)) {
            LOG.error("Input file " + inputJsoTextFile.getAbsolutePath() + " is Json Invalid");
            return null;
        }

        JsonNode jsonNode = null;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);

        try {
            byte[] encoded = Files.readAllBytes(Paths.get(inputJsoTextFile.getAbsolutePath()));
            String jsonString = new String(encoded, "UTF-8");
            jsonNode = mapper.readValue(jsonString, JsonNode.class);
        } catch (Exception e) {
            LOG.error("Exception", e);
        }

        return jsonNode;
    }

    public static JsonNode convertToJsonNode(String value) throws IOException {
        JsonNode map = null;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);

        try {
            // convert JSON string to Map
            map = mapper.readValue(value, JsonNode.class);
        } catch (Exception e) {
            LOG.debug("Exception", e);
        }

        return map;
    }


    public static JsonNode convertToJsonNode(Object inputObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode mapAsJsonNode = null;
        try {
            String itemAsString = objectMapper.writeValueAsString(inputObject);
            mapAsJsonNode = JsonUtils.convertToJsonNode(itemAsString);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapAsJsonNode;
    }


    public static boolean hasValue(JsonNode node, String hasValue) {
        Iterator<String> fields = node.fieldNames();
        while (fields.hasNext()) {
            String field = fields.next();
            LOG.debug(node.get(field).textValue());
            if (node.get(field).textValue().equalsIgnoreCase(hasValue)) {
                return true;
            }
        }

        return false;
    }

    public static String beautifyJson(String diffResponseJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Object json = null;
        String indented = null;

        try {
            json = objectMapper.readValue(diffResponseJson, Object.class);
            indented = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(json);
        } catch (IOException e) {
            LOG.error("IOException", e);

        }
        return indented;
    }


    static String str = "\n" +
            "\t\"topic\": \"madlytics_1\",\n" +
            "\t\"event\": \"madlytics\",\n" +
            "\t\"timestamp\": 1473571563074,\n" +
            "\t\"_env\": \"prodHeimdall\",\n" +
            "\t\"payload\": \"{\\\"ev\\\":{\\\"_ev\\\":{\\\"action\\\":\\\"appLaunch\\\",\\\"category\\\":\\\"appLaunch\\\",\\\"custLogin\\\":\\\"\\\",\\\"eventName\\\":\\\"appLaunch\\\",\\\"installationID\\\":\\\"d5f2f591-e51c-3eb3-baff-5c3aea55d8e6\\\",\\\"isLoggedIn\\\":false,\\\"label\\\":\\\"app-launch\\\",\\\"offset\\\":-215,\\\"ruLoginID\\\":\\\"mukthar.am@gmail.com\\\"},\\\"_t\\\":\\\"appLaunch\\\",\\\"ab_tests\\\":{\\\"android.helpshift\\\":\\\"disabled\\\",\\\"cart.juspay\\\":\\\"enabled\\\",\\\"contactus.number\\\":\\\"outside\\\",\\\"d0.newuser\\\":\\\"enabled\\\",\\\"ios.browse\\\":\\\"default\\\",\\\"ios.profile.fab\\\":\\\"default\\\",\\\"lga.forum\\\":\\\"disabled\\\",\\\"lga.shots\\\":\\\"enabled\\\",\\\"lgp.cardloadevent\\\":\\\"enabled\\\",\\\"lgp.feed.variants\\\":\\\"variantB\\\",\\\"lgp.personalization.rollout\\\":\\\"enabled\\\",\\\"lgp.react.feed\\\":\\\"default\\\",\\\"lgp.rollout\\\":\\\"enabled\\\",\\\"lgp.rollout.ios\\\":\\\"enabled\\\",\\\"lgp.stream\\\":\\\"enabled\\\",\\\"lgp.stream.ios\\\":\\\"disabled\\\",\\\"lgp.stream.variant\\\":\\\"variantC\\\",\\\"lgp.timeline.cardloadevent\\\":\\\"enabled\\\",\\\"nav.guided\\\":\\\"enabled\\\",\\\"nav.links\\\":\\\"list\\\",\\\"nav.store\\\":\\\"disabled\\\",\\\"notification.variant\\\":\\\"v5\\\",\\\"pdp.details\\\":\\\"table\\\",\\\"pdp.forum\\\":\\\"enabled\\\",\\\"pdp.video\\\":\\\"enabled\\\",\\\"pps\\\":\\\"enabled\\\",\\\"recommendations.visual\\\":\\\"disabled\\\",\\\"rn.update\\\":\\\"default\\\",\\\"rn.update.ios\\\":\\\"default\\\",\\\"search.additionalInfo\\\":\\\"test\\\",\\\"search.sampling\\\":\\\"enabled\\\",\\\"search.visual\\\":\\\"enabled\\\",\\\"store.variant\\\":\\\"list\\\",\\\"testset\\\":\\\"test1\\\",\\\"wallet\\\":\\\"enabled\\\"},\\\"app\\\":{\\\"build\\\":110137,\\\"name\\\":\\\"MyntraRetailAndroid\\\",\\\"react_bundle_name\\\":\\\"android-jsbundle-3.8.0\\\",\\\"react_bundle_version\\\":\\\"2.5.0-QA-D2\\\",\\\"version\\\":\\\"3.8.0-QA-D2\\\"},\\\"device\\\":{\\\"advertising_id\\\":\\\"\\\",\\\"build_number\\\":\\\"vbox86p\\\",\\\"category\\\":\\\"Mobile\\\",\\\"device_id\\\":\\\"23bf479dc3c6d4f9\\\",\\\"device_year\\\":2013,\\\"device_year_type\\\":2,\\\"height\\\":1184,\\\"imei\\\":\\\"000000000000000\\\",\\\"installation_id\\\":\\\"d5f2f591-e51c-3eb3-baff-5c3aea55d8e6\\\",\\\"manufacturer\\\":\\\"Genymotion\\\",\\\"model_number\\\":\\\"Google Nexus 4 - 4.1.1 - API 16 - 768x1280\\\",\\\"os\\\":\\\"Android\\\",\\\"os_api_version\\\":16,\\\"os_version\\\":\\\"4.1.1\\\",\\\"width\\\":768},\\\"enhanced\\\":{},\\\"event_meta_version\\\":\\\"1.3.0\\\",\\\"event_sequence\\\":0,\\\"geo\\\":{\\\"lat\\\":0,\\\"long\\\":0},\\\"network\\\":{\\\"bandwidth\\\":\\\"15.78083735145929\\\",\\\"bandwidth_bucket\\\":\\\"WORST\\\",\\\"carrier\\\":\\\"Android\\\",\\\"ip\\\":\\\"0.0.0.0\\\",\\\"type\\\":\\\"WIFI\\\"},\\\"session\\\":{\\\"auto_id\\\":302,\\\"server_offset\\\":-215,\\\"session_id\\\":\\\"e3826c5d-9574-4f2c-a6f5-9cba55324515-23bf479dc3c6d4f9\\\",\\\"session_referrer\\\":{},\\\"start_time\\\":1.473479070971e+12},\\\"time_stamp\\\":1.473479070982e+12,\\\"user\\\":{\\\"customer_id\\\":\\\"\\\",\\\"is_logged_in\\\":false,\\\"prev_customer_id\\\":\\\"mukthar.am@gmail.com\\\"}}}\"\n" +
            "}";

    public static void main(String[] args) {

        String input = "\"QUEUED\" : {\n" +
                "    \"2017-05-01\" : 21145,\n" +
                "    \"2017-06-02\" : 22288," +
                "\"2017-05-17\" : 22141\n" +
                "  }";


        try {
            JsonNode node = convertToJsonNode(input);
            LOG.debug(node.toString());
            LOG.debug("Has = " + hasValue(node, "21145") );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
