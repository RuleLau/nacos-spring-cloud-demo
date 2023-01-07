package com.rule.test;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * 视频中提取音乐
 */
public class VideoToMusicUtil {


    public static void toMusic(String videoPath) {

        System.out.println("提取音频文件");
        File file = new File(videoPath);
        if (file.exists()) {
            String name = file.getName();
            String[] videoArr = name.split("\\.");
            //抓取资源
            FFmpegFrameGrabber frameGrabber1 = new FFmpegFrameGrabber(videoPath);
            Frame frame;
            FFmpegFrameRecorder recorder;
            try {
                frameGrabber1.start();
                File parentFile = file.getParentFile();
                String[] fileNameList = parentFile.list();
                String mp3Name = parentFile.getAbsolutePath() + "\\" + videoArr[0] + ".mp3";
                int seq = 0;
                for (String fileName : fileNameList) {
                    // xxx.mp3
                    // xxx_1.mp3
                    if (fileName.startsWith(videoArr[0]) && fileName.endsWith("mp3")) {
                        // xxx // .mp3
                        String[] split = fileName.split(videoArr[0]);
                        // "" mp3
                        String[] split1 = split[1].split("\\.");
                        int tmp = StringUtils.isEmpty(split1[0]) ? 0 : Integer.parseInt(split1[0].substring(1));
                        if (tmp >= seq) {
                            seq = tmp + 1;
                            mp3Name = parentFile.getAbsolutePath() + "\\" + videoArr[0] + "_" + seq + ".mp3";
                        }
                    }
                }
                System.out.println("--转换后 mp3 文件名-->>" + mp3Name);
                recorder = new FFmpegFrameRecorder(mp3Name, frameGrabber1.getAudioChannels());
                recorder.setFormat("mp3");
                recorder.setSampleRate(frameGrabber1.getSampleRate());
                recorder.setTimestamp(frameGrabber1.getTimestamp());
                recorder.setAudioQuality(0);

                recorder.start();
                int index = 0;
                while (true) {
                    frame = frameGrabber1.grab();
                    if (frame == null) {
                        System.out.println("视频处理完成");
                        break;
                    }
                    if (frame.samples != null) {
                        recorder.recordSamples(frame.sampleRate, frame.audioChannels, frame.samples);
                    }
//                    System.out.println("帧值=" + index);
                    index++;
                }
                recorder.stop();
                recorder.release();
                frameGrabber1.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("文件找不到，请检查！");
        }
    }

    public static void main(String[] args) {
        toMusic("E:\\Movies\\Dear Jane -  銀河修理員 Galactic Repairman (Official Music Video).mp4");
//        File file = new File("E:\\Movies\\iiilab_video.mp4");


    }

}
