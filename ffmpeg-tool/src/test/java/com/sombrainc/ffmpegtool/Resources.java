package com.sombrainc.ffmpegtool;

import org.testng.Assert;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.ClassLoader.getSystemResource;

public class Resources {

    public static Path PATH_TO_VIDEO1;
    public static Path PATH_TO_FFMPEG_LOGO;

    static {
        try {
            PATH_TO_VIDEO1 = Paths.get(getSystemResource("video/video1.mp4").toURI());
            PATH_TO_FFMPEG_LOGO = Paths.get(getSystemResource("image/ffmpeg.logo.png").toURI());
        } catch (URISyntaxException e) {
            Assert.fail("Failed to load resources", e);
        }
    }
}
