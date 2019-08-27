package com.sombrainc.commons.process;

import com.sombrainc.commons.environment.SystemEnvironment;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

public class ProcessCommandExecutorTest {

    @Test
    public void testJavaVersion() throws InterruptedException, IOException, TimeoutException {
        String commandOutput = ProcessCommandExecutor.of().runCommand("java", "-version");

        if (isBlank(commandOutput)) {
            Assert.fail("A command <java -version> outputs an empty string");
        }

        String javaVersion = SystemEnvironment.gerProperty("java.version").orElse(null);

        String versionMismatchMessage = "Java version mismatch - expected value: " + javaVersion + ", actual value: " + commandOutput;
        Assert.assertTrue(isBlank(javaVersion) || commandOutput.contains(javaVersion), versionMismatchMessage);
    }
}
