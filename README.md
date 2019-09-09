# FFmpeg Java API

It's java api wrapper for work with FFmpeg tool. 

After doing some research, we found similar library [ffmpeg-cli-wrapper](https://github.com/bramp/ffmpeg-cli-wrapper) that can be used for the demo. But by using this library we were not able to cover all requirements that can be expected from video editor.
Also the library license doesn't allow source modification without contribution process. So this is not best practice to use such libraries for commercial projects.

Following samples demonstrates our vision how to work with command line tool. 

For example we have implemented several codecs with base options than can be expanded in an easy way. Please see [VideoCodecs](https://github.com/moliyar/video-composer-java/blob/develop/ffmpeg-tool/src/main/java/com/sombrainc/ffmpegtool/media/codec/VideoCodecs.java) and [AudioCodecs](https://github.com/moliyar/video-composer-java/blob/develop/ffmpeg-tool/src/main/java/com/sombrainc/ffmpegtool/media/codec/AudioCodecs.java).
Also please see [FilterComplex](https://github.com/moliyar/video-composer-java/blob/develop/ffmpeg-tool/src/main/java/com/sombrainc/ffmpegtool/media/filter/FilterComplex.java) in order to implement more filters.

Please note that **ffmpeg** should be locally installed on Linux (or other OS) so this usage doesn't seem to violate GPL licensing, because it doesn't need ffmpeg to be packaged inside jar.

Install
-------

Maven:
```xml
<dependency>
    <groupId>com.sombrainc</groupId>
    <artifactId>ffmpeg-tool</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Usage
-----
#### Clip a video file
Following example demonstrates how to cutting a video based on start and end time.

```java
	FFmpeg ffmpeg = FFmpeg.fromPath(pathToFFmpeg);
	FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_MP4_VIDEO, ffmpeg);

	Output output = Output.of(videoInput, buildTmpFilePath("videoClipping.mp4"))
                .clipping(Clipping.betweenSeconds(5, 10));

	FFmpegResult ffmpegResult = ffmpeg.toOutput(output).execute();
```

#### Add watermark to video
Code
```java
	FFmpeg ffmpeg = FFmpeg.fromPath(pathToFFmpeg);
	FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_MP4_VIDEO, ffmpeg);
	FileInput watermarkInput = FileInput.fromPath(Resources.PATH_TO_FFMPEG_LOGO, ffmpeg);

	FilterChainInput overlayVideo = ffmpeg.filterComplex().applyOverlay(videoInput, watermarkInput, OverlayFilter.ofTopRight(10, 10));
	Output output = Output.of(overlayVideo, buildTmpFilePath("videoWithWatermark.mp4"));
	FFmpegResult ffmpegResult = ffmpeg.toOutput(output).execute();
```

#### Compress .avi video
Code
```java
	FFmpeg ffmpeg = FFmpeg.fromPath(pathToFFmpeg);

	MSMPEG4v2Codec videoCodec = VideoCodecs.MSMPEG4v2_CODEC.build()
                .size(320, 240);

	FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_AVI_VIDEO, ffmpeg);
	Output output = Output.of(videoInput, buildTmpFilePath("compressVideo.avi"))
                .videoCodec(videoCodec);

	FFmpegResult ffmpegResult = ffmpeg.toOutput(output).execute();
```

#### Extract Sound From a Video, And Save It in Mp3 Format
Code
```java
	FFmpeg ffmpeg = FFmpeg.fromPath(pathToFFmpeg);

	FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_AVI_VIDEO, ffmpeg);
	MP3Codec mp3Codec = AudioCodecs.MP3_CODEC.build()
                .samplingRate("44100")
                .bitRate("192k");

	Output output = Output.of(videoInput, buildTmpFilePath("extractAudioStream.mp3"))
                .disableVideo()
                .audioCodec(mp3Codec);

	FFmpegResult ffmpegResult = ffmpeg.toOutput(output).execute();
```

#### Add watermark, clipping, video codec and audio codec in one call
Multiple actions could be added via customizing ```Output``` class instance.

```java
     FFmpeg ffmpeg = FFmpeg.fromPath(ffmpegPath);

        FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_MP4_VIDEO, ffmpeg);
        FileInput watermarkInput = FileInput.fromPath(Resources.PATH_TO_FFMPEG_LOGO, ffmpeg);

        FilterChainInput overlayVideo = ffmpeg.filterComplex().applyOverlay(videoInput, watermarkInput, OverlayFilter.ofTopRight(10, 10));

        MSMPEG4v2Codec videoCodec = VideoCodecs.MPEG4_CODEC.build()
                .size(320, 240);

        MP3Codec mp3Codec = AudioCodecs.MP3_CODEC.build()
                .samplingRate("44100")
                .bitRate("192k");

        Output output = Output.of(overlayVideo, buildTmpFilePath("videoWithWatermark.mp4"))
                .audioCodec(mp3Codec)
                .videoCodec(videoCodec)
		.clipping(Clipping.betweenSeconds(1, 3));

        FFmpegResult ffmpegResult = ffmpeg.toOutput(output).execute();
```

#### Process multiple files in parallel
Based on [CountDownLatch](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/CountDownLatch.html) class has been implemented set of tools for building and executing parallel threads. When all threads are finished you will be able to get result for each thread. 
If some thread failed you can get the appropriate exception from the result 
```java
	ParallelProcessPoolBuilder<ProcessRunner, FFmpegResult> builder = ParallelProcessPool.withResulting(ProcessRunner.class, FFmpegResult.class);

	ProcessRunner videoWithWatermarkScenario = videoWithWatermarkScenario();
	builder.putProcess(videoWithWatermarkScenario, ProcessRunner::execute);

	ProcessRunner clippingVideoScenario = clippingVideoScenario();
	builder.putProcess(clippingVideoScenario, ProcessRunner::execute);

	Map<ProcessRunner, ProcessResult<FFmpegResult>> results = builder.build().submitAndWaitResults();
    
	// you can get results after finish of all processes
	ProcessResult<FFmpegResult> videoWithWatermarkResult = results.get(videoWithWatermarkScenario);
	ProcessResult<FFmpegResult> clippingVideoResult = results.get(clippingVideoScenario);
    	
...

	private ProcessRunner videoWithWatermarkScenario() throws IOException {
		FFmpeg ffmpeg = FFmpeg.fromPath(pathToFFmpeg);

		FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_MP4_VIDEO, ffmpeg);
		FileInput watermarkInput = FileInput.fromPath(Resources.PATH_TO_FFMPEG_LOGO, ffmpeg);

		FilterChainInput overlayVideo = ffmpeg.filterComplex().applyOverlay(videoInput, watermarkInput, OverlayFilter.ofTopRight(10, 10));
		Output output = Output.of(overlayVideo, buildTmpFilePath("videoWithWatermark.mp4"));

		return ffmpeg.toOutput(output);
	}

	private ProcessRunner clippingVideoScenario() throws IOException {
		FFmpeg ffmpeg = FFmpeg.fromPath(pathToFFmpeg);

		FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_MP4_VIDEO, ffmpeg);
		Output output = Output.of(videoInput, buildTmpFilePath("clippingVideo.mp4"));
		output.clipping(Clipping.betweenSeconds(5, 10));

		return ffmpeg.toOutput(output);
	}
```

#### Get progress while encoding
Code
```java
	FFmpeg ffmpeg = FFmpeg.fromPath(pathToFFmpeg);

	MSMPEG4v2Codec videoCodec = VideoCodecs.MSMPEG4v2_CODEC.build()
                .size(320, 240);

	FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_AVI_VIDEO, ffmpeg);
	Output output = Output.of(videoInput, buildTmpFilePath("compressVideo.avi"))
                .videoCodec(videoCodec);
	ffmpeg.setProgressListener(new ProgressListener() {
			public void progress(ProgressItem progress) {
					// Print out interesting information about the progress
					System.out.println(
							MoreObjects.toStringHelper(this)
									.add("frame", progress.frame)
									.add("fps", progress.fps)
									.add("bitrate", progress.bitrate)
									.add("total_size", progress.totalSize)
									.add("out_time_ns", progress.outTimeNS)
									.add("dup_frames", progress.dupFrames)
									.add("drop_frames", progress.dropFrames)
									.add("speed", progress.speed)
									.add("status", progress.status)
									 .toString()
					);
			 }
	});

	FFmpegResult ffmpegResult = ffmpeg.toOutput(output).execute();
```
