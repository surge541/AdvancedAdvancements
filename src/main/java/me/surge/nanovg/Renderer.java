package me.surge.nanovg;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author surge
 * @since 02/07/2023
 */
public class Renderer {

    public static long handle;

    private static ByteBuffer font;

    private static List<String> texturePaths = Arrays.asList(
            "task",
            "goal",
            "challenge",
            "flash"
    );

    private static Map<String, Integer> textures = new HashMap<>();

    public static void initialise() {
        handle = nvgCreate(NVG_ANTIALIAS);

        try {
            font = getResourceBytes("/assets/advancedadvancements/font/minecraftia.ttf", 1024);
            nvgCreateFontMem(handle, "minecraftia", font, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        texturePaths.forEach(path -> {
            try {
                textures.put(path, nvgCreateImageMem(handle, NVG_IMAGE_NEAREST, getResourceBytes("/assets/advancedadvancements/texture/" + path + ".png", 512)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void terminate() {
        textures.forEach((path, textureHandle) -> nvgDeleteImage(handle, textureHandle));

        nvgDelete(handle);
    }

    public static void frame(Runnable block) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL_LESS);
        RenderSystem.clear(GL_DEPTH_BUFFER_BIT, false);

        nvgBeginFrame(handle, MinecraftClient.getInstance().getWindow().getWidth(), MinecraftClient.getInstance().getWindow().getHeight(), 1f);

        block.run();

        nvgEndFrame(handle);
    }

    public static void texture(String name, float x, float y, float width, float height) {
        texture(name, x, y, width, height, 1f);
    }

    public static void texture(String name, float x, float y, float width, float height, float alpha) {
        NVGPaint paint = NVGPaint.calloc();

        nvgImagePattern(handle, x, y, width, height, 0, textures.get(name), alpha, paint);

        nvgBeginPath(handle);
        nvgRect(handle, x, y, width, height);
        nvgFillPaint(handle, paint);
        nvgFill(handle);
        nvgClosePath(handle);

        paint.free();
    }

    public static void text(String text, float x, float y, Color colour, float size) {
        NVGColor colourised = convert(colour);

        nvgBeginPath(handle);

        nvgFillColor(handle, colourised);
        nvgFontFace(handle, "minecraftia");
        nvgFontSize(handle, size);
        nvgTextAlign(handle, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
        nvgText(handle, x, y, text);

        nvgClosePath(handle);

        colourised.free();
    }

    public static void translate(float x, float y) {
        nvgTranslate(handle, x, y);
    }

    public static void scale(float factor, float x, float y) {
        translate(x, y);
        nvgScale(handle, factor, factor);
        translate(-x, -y);
    }

    public static void scope(Runnable block) {
        nvgSave(handle);
        block.run();
        nvgRestore(handle);
    }

    private static NVGColor convert(Color colour) {
        return NVGColor.calloc()
            .r(colour.getRed() / 255f)
            .g(colour.getGreen() / 255f)
            .b(colour.getBlue() / 255f)
            .a(colour.getAlpha() / 255f);
    }

    private static ByteBuffer getResourceBytes(String resource, int bufferSize) throws IOException {
        InputStream source = Renderer.class.getResourceAsStream(resource);
        ReadableByteChannel rbc = Channels.newChannel(source);

        ByteBuffer buffer = BufferUtils.createByteBuffer(bufferSize);

        while (true) {
            int bytes = rbc.read(buffer);

            if (bytes == -1) {
                break;
            }

            if (buffer.remaining() == 0) {
                buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2);
            }
        }

        buffer.flip();

        return MemoryUtil.memSlice(buffer);
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);

        buffer.flip();
        newBuffer.put(buffer);

        return newBuffer;
    }

}
