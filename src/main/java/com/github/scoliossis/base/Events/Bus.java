package com.github.scoliossis.base.Events;

import java.lang.reflect.Method;

public class Bus {
    public static void register(Class<?> clazz, Object instance) {
        //System.out.println(clazz.getName());

        for (Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(SubscribeEvent.class)) {
                EventBus.INSTANCE.register(m, instance, clazz);
                //System.out.println("registered: " + clazz.getName() + " with " + Arrays.toString(m.getParameterTypes()));
            }
        }
    }

    public static boolean post(Event event) {
        try {
            EventBus.INSTANCE.post(event);
        } catch (Exception ex) {
            System.out.println("failed to post event");
            ex.printStackTrace();
        }
        return event.isCancelled();
    }
}
