package GPopEditor;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;

public class Mainbackup {
    static float zoomLevel = 1.0f;
    static float zoomCenter = 0.5f;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("GPop Editor (Version 0.02)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1400, 800);

            JPanel panel = new JPanel() {

                AudioInputStream audioInputStream;
                float[] audioData;

                {
                    try {
                        audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\Users\\david\\OneDrive\\Desktop\\GPop Editor GitHub\\GPopEditor/test.wav"));
                        audioData = new float[(int) audioInputStream.getFrameLength()];
                        byte[] audioBytes = new byte[2 * audioData.length];
                        audioInputStream.read(audioBytes);
                        for (int i = 0; i < audioData.length; i++) {
                            audioData[i] = ((audioBytes[2*i] & 0xFF) | (audioBytes[2*i + 1] << 8)) / 32767.0f;
                        }
                    } catch (UnsupportedAudioFileException | IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (audioData != null) {
                        int w = getWidth();
                        int h = getHeight();
                        int audioLength = audioData.length;
                        float samplesPerPixel = Main.zoomLevel * audioLength / w;
                        int startSample = (int) (Main.zoomCenter * audioLength - samplesPerPixel * w / 2);
                        startSample = Math.max(0, Math.min(startSample, audioLength - (int) samplesPerPixel));
                        for (int i = 0; i < w; i++) {
                            int sampleStart = startSample + (int) (i * samplesPerPixel);
                            int sampleEnd = Math.min(audioData.length, startSample + (int) ((i + 1) * samplesPerPixel));
                            float min = 0.0f;
                            float max = 0.0f;
                            for (int j = sampleStart; j < sampleEnd; j++) {
                                float sample = audioData[j];
                                min = Math.min(min, sample);
                                max = Math.max(max, sample);
                            }
                            int minY = (int) ((0.5 - min) * h);
                            int maxY = (int) ((0.5 - max) * h);
                            g.drawLine(i, minY, i, maxY);
                        }
                    }
                }
            };

            MouseWheelListener mouseWheelListener = new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    int notches = e.getWheelRotation();
                    double zoomFactor = (notches < 0) ? 0.9 : 1.1;
                    float oldZoomLevel = Main.zoomLevel;
                    Main.zoomLevel *= zoomFactor;
                    float mousePositionRatio = (float) e.getX() / ((JPanel)e.getSource()).getWidth();
                    float visibleWidthRatio = Main.zoomLevel / oldZoomLevel;
                    float oldZoomCenterOffset = Main.zoomCenter - mousePositionRatio;
                    Main.zoomCenter = mousePositionRatio + oldZoomCenterOffset * visibleWidthRatio;
                    panel.repaint();
                }
            };

            panel.addMouseWheelListener(mouseWheelListener);

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}