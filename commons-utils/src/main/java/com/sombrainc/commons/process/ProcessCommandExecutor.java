package com.sombrainc.commons.process;

import org.zeroturnaround.exec.ProcessExecutor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessCommandExecutor {

    public String runCommand(Path toolPath, String... args) throws InterruptedException, TimeoutException, IOException {
        String commandAbsolutePath = toolPath.toAbsolutePath().toString();
        return runCommand(commandAbsolutePath, args);
    }

    public String runCommand(String toolName, String... args) throws InterruptedException, TimeoutException, IOException {
        String[] commandLine = Stream.concat(Stream.of(toolName), Stream.of(args)).flatMap(Stream::of).toArray(String[]::new);
        String collect = Arrays.stream(commandLine).collect(Collectors.joining(" "));

        ProcessExecutor command = new ProcessExecutor().command(commandLine);

        return command.readOutput(true).execute()
                .outputUTF8();
    }

    public static ProcessCommandExecutor of() {
        return new ProcessCommandExecutor();
    }

}
