package com.sombrainc.ffmpegtool;

import org.testng.Assert;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.ClassLoader.getSystemResource;

public class Resources {

    public static Path PATH_TO_MP4_VIDEO;
    public static Path PATH_TO_AVI_VIDEO;
    public static Path PATH_TO_FFMPEG_LOGO;

    static {
        try {
            PATH_TO_MP4_VIDEO = Paths.get(getSystemResource("video/video1.mp4").toURI());
            PATH_TO_AVI_VIDEO = Paths.get(getSystemResource("video/video2.avi").toURI());
            PATH_TO_FFMPEG_LOGO = Paths.get(getSystemResource("image/ffmpeg.logo.png").toURI());
        } catch (URISyntaxException e) {
            Assert.fail("Failed to load resources", e);
        }
    }
}
