package com.bigademy.pigademy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pig.tools.pigscript.parser.TokenMgrError;
import org.apache.tools.ant.types.Commandline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.pig.pigunit.PigTest;

import com.bigademy.entities.InputDataset;
import com.bigademy.entities.OutputDataset;
import com.bigademy.utils.Pair;

import com.bigademy.utils.Response;

/**
 * Executes pig script
 * @author tmehta
 *
 */
public class PigScriptExecutor {

    private static final String INPUT_DELIMITER = "\n";

    private static final Logger logger = LoggerFactory.getLogger(PigScriptExecutor.class);
    private static final String[] ALIAS_UNOVERRIDE = { "STORE", "DUMP" };
    private static final String COMMAND_ALLOCATE_MEMORY_TO_SORT_PROPERTY = "set io.sort.mb 10;";
    private static final int RANDOMIZE_SIZE = 4;
    private final String randomizeSuffix = RandomStringUtils.randomAlphanumeric(RANDOMIZE_SIZE);
    private final String pigDirectoryName = StringUtils.join(new String[] { "/tmp/bigademy-", randomizeSuffix });

    /**
     * Execute pig script for given input set and compare with expected output set
     * @param inputDatasets input data set
     * @param outputDatasets expected output data set
     * @param pigScript pig script to be run
     * @return result from running pig script
     */
    public Response executeAndAssertOutput(Set<InputDataset> inputDatasets, Set<OutputDataset> outputDatasets, String pigScript) {

        Response response = new Response();

        List<Pair<String,String>> inputAliasToFileName = new ArrayList<Pair<String,String>>();

        // For each input data set, copy the contents into a file and create a pair of input alias and file name
        for (InputDataset inputDataset: inputDatasets) {
            try {
                logger.info("PIGEXECUTOR: Storing contents of input dataset into file.");
                String randomString = RandomStringUtils.randomAlphanumeric(RANDOMIZE_SIZE);
                String fileName = StringUtils.join(new String[] {pigDirectoryName, "/input/input-", randomString, ".txt"});
                Pair<String,String> aliasToFileName = new Pair<String, String>(inputDataset.getAlias(), fileName);
                inputAliasToFileName.add(aliasToFileName);
                // Assume that each tuple is delimited by a newline
                writeInputIntoFile(inputDataset.getDataset().split(INPUT_DELIMITER), fileName);
            } catch (IOException e) {
                logger.error("PIGEXECUTOR: Error creating file for input data set", e);
                response.setSuccess(false);
                response.setErrorMessage(Response.INTERNAL_ERROR);
                deleteDirectory(pigDirectoryName);
            }
        }

        // Set Pig unit arguments
        String[] pigArgs = new String[inputAliasToFileName.size()];
        int i = 0;
        for(Pair<String,String> inputPair: inputAliasToFileName) {
            pigArgs[i] = StringUtils.join(new String[] {inputPair.getKey(), "=", inputPair.getValue()});
            i++;
        }

        // Test set up - pig script and unoverrides
        // Allocate less memory to io.sort.mb property to avoid out of memory error
        String[] pigScriptArray = new String[] { COMMAND_ALLOCATE_MEMORY_TO_SORT_PROPERTY, pigScript };
        PigTest test = new PigTest(pigScriptArray, pigArgs);
        for (String alias : ALIAS_UNOVERRIDE) {
            test.unoverride(alias);
        }

        // Run test and catch exceptions
        try {
            logger.info("PIGEXECUTOR: Running pig scipt.");
            test.runScript();
        } catch (TokenMgrError e) {
            logger.error("PIGEXECUTOR: Error running pig script.", e);
            response.setSuccess(false);
            response.setErrorMessage(e.getMessage());
            deleteDirectory(pigDirectoryName);
            return response;
        } catch (Exception e) {
            logger.error("PIGEXECUTOR: Error running pig script.", e);
            response.setSuccess(false);
            response.setErrorMessage(e.getMessage());
            deleteDirectory(pigDirectoryName);
            return response;
        }

        response = assertPigOutput(test, outputDatasets);

        deleteDirectory(pigDirectoryName);
        return response;
    }

    /**
     * Assert pig output with the expected output and return the result.
     * @param test pig unit test
     * @param outputDatasets expected output datasets
     * @return response with appropriate error message if applicable
     */
    private Response assertPigOutput(PigTest test, Set<OutputDataset> outputDatasets) {

        Response response = new Response();
        // Set default as true, for exercises with multiple output datasets, the response is true only if ALL actual outputs match their respective expected output
        response.setSuccess(true);
        // Assert the output of the pig script execution
        for (OutputDataset outputDataset : outputDatasets) {
            String actualOutput = new String();
            try {
                actualOutput = StringUtils.join(test.getAlias(outputDataset.getAlias()), "\n");
            } catch (Exception e) {
                logger.error("PIGEXECUTOR: Error creating output dataset for comparison.", e);
                response.setSuccess(false);
                response.setErrorMessage(e.getMessage());
                return response;
            }
            String expectedOutput = outputDataset.getDataset();

            if (actualOutput.isEmpty()) {
                logger.info("PIGEXECUTOR: Actual output is empty.");
                response.setSuccess(false);
                response.setErrorMessage("The expected alias " + outputDataset.getAlias() + " does not have any stored output. Please recheck your pig script.");
                break;
            } else if (!expectedOutput.equals(actualOutput)) {
                logger.info("PIGEXECUTOR: Expected output does not match the actual.");
                response.setSuccess(false);
                response.setErrorMessage("The actual output for alias " + outputDataset.getAlias() + " did not match the expected output.Expected is: " + expectedOutput + " but actual output was: " + actualOutput);
                break;
            } else if (response.isSuccess() && expectedOutput.equals(actualOutput)) {
                logger.info("PIGEXECUTOR: Expected output matches the actual.");
                response.setSuccess(true);
            } else {
                logger.info("PIGEXECUTOR: Unknown error during running pig script.");
                response.setSuccess(false);
                response.setErrorMessage("Unknown error. Please check your script and rerun.");
                break;
            }
        }

        return response;
    }

    /**
     * Delete directory with given name
     * @param directoryName directory to be deleted
     * @throws IOException
     */
    private void deleteDirectory(String directoryName) {
        logger.info("PIGEXECUTOR: Deleting data set directory: " + directoryName);
        FileUtils.deleteQuietly(new File(pigDirectoryName));
    }

    /**
     * Write input data set into file to facilitate args for pig script
     * @param inputDataSet input data set
     * @param fileName file name
     * @throws IOException
     */
    private void writeInputIntoFile(String[] inputDataSet, String fileName) throws IOException {
        logger.info("PIGEXECUTOR: Creating file for input data set with input file name: " + fileName + " with input data set" + inputDataSet);
        FileUtils.writeLines(new File(fileName), Arrays.asList(inputDataSet));
    }
}