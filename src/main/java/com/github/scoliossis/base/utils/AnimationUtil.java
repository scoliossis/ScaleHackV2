package com.github.scoliossis.base.utils;

import lombok.AllArgsConstructor;

import java.util.HashMap;

public class AnimationUtil {

    //https://easings.net/#easeInCirc
    public static double easeInCirc(double x) {
        return 1 - Math.sqrt(1 - Math.pow(x, 2));

    }

    //https://easings.net/#easeOutQuad
    public static double easeOutQuad(double x) {
        return 1 - (1 - x) * (1 - x);
    }


    //https://easings.net/#easeInOutCubic
    public static double easeInOutCubic(double x) {
        return x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2;
    }

    static HashMap<String, Animation> animations = new HashMap<>();

    public static void addEasingAnimation(String key) {
        addEasingAnimation(key, new float[]{});
    }

    static long animationTime = 500;

    public static void addEasingAnimation(String key, float[] extra) {
        if (animations.containsKey(key)) {
            Animation animation = animations.get(key);
            long timeOffset = System.currentTimeMillis()-animation.startTime;
            animations.put(key, new Animation(System.currentTimeMillis() - (animationTime-timeOffset), true, extra));

            return;
        }

        animations.put(key, new Animation(System.currentTimeMillis(), true, extra));
    }

    public static void removeEasingAnimation(String key) {
        removeEasingAnimation(key, new float[]{});
    }

    public static void removeEasingAnimation(String key, float[] extra) {
        if (!animations.containsKey(key)) {
            animations.put(key, new Animation(System.currentTimeMillis(), false, extra));
            return;
        }

        Animation animation = animations.get(key);
        animations.remove(key);


        if (System.currentTimeMillis()-animation.startTime > animationTime) {
            animations.put(key, new Animation(System.currentTimeMillis(), false, extra));
            return;
        }

        long timeOffset = System.currentTimeMillis()-animation.startTime;
        animations.put(key, new Animation(System.currentTimeMillis() - (animationTime-timeOffset), false, extra));
    }

    public static Animation getAnimation(String key) {
        return animations.get(key);
    }

    public static double getEasingAnimation(String key) {
        if (!animations.containsKey(key)) return 0;

        Animation animation = animations.get(key);

        // animation finished
        if (System.currentTimeMillis()-animation.startTime > animationTime) {
            animations.remove(key);
            return animation.forward ? 1 : 0;
        }

        // if animation going backwards, return reverse
        if (!animation.forward)
            return easeInOutCubic((double) ((animation.startTime + animationTime) - System.currentTimeMillis()) / animationTime);

        return easeInOutCubic((double) (System.currentTimeMillis() - animation.startTime) / animationTime);
    }

    @AllArgsConstructor
    public static class Animation {
        long startTime;
        boolean forward;
        public float[] extra;
    }
}
