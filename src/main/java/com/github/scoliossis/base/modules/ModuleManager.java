package com.github.scoliossis.base.modules;

import com.github.scoliossis.base.Events.Bus;
import com.github.scoliossis.base.annotations.RegisterListeners;
import com.github.scoliossis.base.commands.CommandMain;
import com.github.scoliossis.base.config.Category;
import com.github.scoliossis.base.config.ConfigOption;
import com.github.scoliossis.base.config.ConfigParent;
import com.github.scoliossis.base.config.types.ConfigOptionBoolean;
import com.github.scoliossis.base.config.types.ConfigOptionCategory;
import com.github.scoliossis.base.config.types.ConfigOptionEnum;
import com.github.scoliossis.base.config.types.ConfigOptionNumber;
import com.github.scoliossis.base.utils.C;
import lombok.AllArgsConstructor;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ModuleManager {
    public static HashMap<Class<? extends Module>, ModuleWithInfo> modules = new HashMap<>();

    public static void init() {
        registerEvents();
        CommandMain.loadCommands();
        getModules();
    }

    public static void registerEvents() {
        for (Class<?> clazz : C.reflections.getTypesAnnotatedWith(RegisterListeners.class)) {
            try {
                Bus.register(clazz, clazz.newInstance());
                System.out.printf("registered: %s", clazz.getName());
            } catch (Exception e) {
                System.out.printf("failed to load %s", clazz.getName());
                e.printStackTrace();
            }

        }
    }

    private static void getModules() {
        for (Class<?> clazz : C.reflections.getTypesAnnotatedWith(RegisterModule.class)) {
            try {
                RegisterModule an = clazz.getAnnotation(RegisterModule.class);

                Class<? extends Module> classModule = (Class<? extends Module>) clazz;
                Module module = classModule.newInstance();
                ModuleWithInfo moduleWithInfo = new ModuleWithInfo(module, clazz, an);
                module.setEnabled(an.enabledByDefault());

                modules.put(classModule, moduleWithInfo);

                loadModule(moduleWithInfo);

                Bus.register(clazz, module);
                //KeybindManager.loadBind(m);
            } catch (Exception e) {
                System.out.printf("ERROR ERROR !!!!\nfailed to load %s", clazz);
                e.printStackTrace();
            }
        }
    }

    public static <T extends Module> T getModule(Class<T> clazz) {
        ModuleWithInfo module = modules.get(clazz);
        return module == null ? null : (T) module.module;
    }

    public static <T extends Module> void toggle(Class<T> clazz) {
        Module module = ModuleManager.getModule(clazz);
        if (module == null) return;

        module.toggle();
    }



    public static Module getModulesByFuzzySearch(String input) {
        List<String> mods = modules.values().stream().map(mod -> mod.annotation.name()).collect(Collectors.toList());

        Optional<ExtractedResult> best = FuzzySearch.extractAll(input, mods, 40).stream().max(Comparator.comparingInt(ExtractedResult::getScore));
        return best.map(extractedResult -> ((ModuleWithInfo) modules.values().toArray()[extractedResult.getIndex()]).module).orElse(null);
    }

    public static List<Module> getModulesByCategory(ModuleCategory category) {
        return modules
                .values()
                .stream()
                .filter(m -> m.annotation.category().equals(category))
                //.sorted(Comparator.comparingDouble(setting -> -FontUtil.getStringWidth(ClickGUIScreenMain.formatSettingName(setting.annotation.name()), 1)))
                .map(m -> m.module)
                .collect(Collectors.toList());
    }

    private static void loadModule(ModuleWithInfo moduleWithInfo) {
        moduleWithInfo.module.module = moduleWithInfo.annotation;
        gatherChildren(moduleWithInfo.module);
    }


    private static ConfigOption<?> getConfigOption(Module module, Field field, Class<?> type, Setting setting) {
        if (type == Boolean.class)
            return new ConfigOptionBoolean(field, module, setting);

        if (type == Double.class || type == Float.class || type == Integer.class || type == Long.class)
            return new ConfigOptionNumber<>(field, module, setting);

        if (type.isEnum())
            return new ConfigOptionEnum<>(field, module, setting);

        if (type == Category.class)
            return new ConfigOptionCategory(field, module, setting);

        System.out.println("invalid type!!! error error!");
        return null;
    }



    public static void gatherChildren(Module module) {
        ArrayList<ConfigOption<?>> settingWithParents = new ArrayList<>();

        // adds all settings to this.children, combs through these later to check if they should be children
        addAllSettingsAsChildren(module, settingWithParents);

        // loops through child settings with parents
        filterSubChildren(module, settingWithParents);
    }

    private static void addAllSettingsAsChildren(Module module, ArrayList<ConfigOption<?>> settingWithParents) {
        for (Field field : module.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Setting.class)) {
                Setting setting = field.getAnnotation(Setting.class);

                ConfigOption<?> subSetting = getConfigOption(module, field, field.getType(), setting);

                // add children with parents to a seperate array
                if (field.isAnnotationPresent(ConfigParent.class)) settingWithParents.add(subSetting);

                // add all children to this array, the ones we don't need are removed later.
                // my impl meant that parents would need to be above children in the file, this was a fix
                module.children.add(subSetting);
            }
        }
    }

    private static void filterSubChildren(Module module, ArrayList<ConfigOption<?>> settingWithParents) {
        for (ConfigOption<?> settingWithParent : settingWithParents) {
            String parentToSearchFor = settingWithParent.getField().getAnnotation(ConfigParent.class).parent();

            // loops through all child settings
            for (ConfigOption<?> subSetting : module.children) {

                // if child settings name is the same as the parent name we are looking for
                if (subSetting.getAnnotation().name().equals(parentToSearchFor)) {
                    // move the setting to be with the correct parent
                    subSetting.children.add(settingWithParent);
                    settingWithParent.parent = subSetting;
                    break;
                }
            }
        }

        // removes the setting from being in the modules children, as it is now a child of a submodule
        module.children.removeIf(configOption -> configOption.getField().getAnnotation(ConfigParent.class) != null);
    }


    public static boolean isEnabled(Class clazz) {
        Module mod = ModuleManager.getModule(clazz);
        return mod != null && mod.enabled;
    }

    @AllArgsConstructor
    public static class ModuleWithInfo {
        public Module module;
        public Class<?> clazz;
        public RegisterModule annotation;
    }


    public static void save(String name) {
        // todo
    }
}
