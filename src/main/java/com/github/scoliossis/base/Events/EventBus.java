package com.github.scoliossis.base.Events;

import com.github.scoliossis.base.modules.Module;
import com.github.scoliossis.base.utils.C;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventBus {

    public static EventBus INSTANCE = new EventBus();
    private final ConcurrentHashMap<
            Class<? extends Event>,
            ConcurrentHashMap<Priority, ConcurrentLinkedQueue<Listener>>
            > listeners = new ConcurrentHashMap<>();

    public EventBus() {
        // $$$INSERT$$$___LICENSE-CHECK-2___$$$INSERT$$$
    }

    public void register(
            Method method,
            Object instance,
            Class<?> instanceClazz
    ) {
        Class<? extends Event> clazz = method
                .getParameterTypes()[0].asSubclass(Event.class);

        ConcurrentHashMap<Priority, ConcurrentLinkedQueue<Listener>> l;
        if (!listeners.containsKey(clazz)) {
            l = new ConcurrentHashMap<>();
        } else {
            l = listeners.get(clazz);
        }

        Priority p = method.getAnnotation(SubscribeEvent.class).value();
        ConcurrentLinkedQueue<Listener> q;
        if (!l.containsKey(p)) {
            q = new ConcurrentLinkedQueue<>();
        } else {
            q = l.get(p);
        }

        boolean isModule = Module.class.isAssignableFrom(instanceClazz);
        q.add(
                new Listener(method, instance, isModule)
        );
        l.put(p, q);
        listeners.put(clazz, l);
    }

    public synchronized void post(Event ev)
            throws InvocationTargetException, IllegalAccessException {
        Class<? extends Event> clazz = ev.getClass();

        if (listeners.containsKey(clazz)) {
            ConcurrentHashMap<Priority, ConcurrentLinkedQueue<Listener>> l =
                    listeners.get(clazz);
            boolean didMutate = false;
            for (Priority p : Priority.values()) {
                if (l.containsKey(p)) {
                    ConcurrentLinkedQueue<Listener> q = l.get(p);
                    ConcurrentLinkedQueue<Listener> n =
                            new ConcurrentLinkedQueue<>();
                    while (!q.isEmpty()) {
                        Listener ln = q.poll();
                        n.add(ln);

                        ln.call(ev);
                    }
                    l.put(p, n);
                    didMutate = true;
                }
            }
            if (didMutate) listeners.put(clazz, l);
        }
    }

    public enum Priority {
        LOWEST,
        LOW,
        NORMAL,
        HIGH,
        HIGHEST,
    }

    @AllArgsConstructor
    private static class Listener {
        Method method;
        Object instance;
        boolean isModule;

        public void call(Event ev) {
            if (isModule) {
                Module m = (Module) instance;
                if (!m.isEnabled()) return;
            }

            try {
                method.invoke(instance, ev);
            } catch (Throwable ex) {
                C.getLogger().error("Error calling listener");
                ex.printStackTrace();
            }
        }
    }
}