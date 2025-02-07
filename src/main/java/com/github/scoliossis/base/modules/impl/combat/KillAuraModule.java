package com.github.scoliossis.base.modules.impl.combat;


import com.github.scoliossis.base.config.Category;
import com.github.scoliossis.base.config.ConfigParent;
import com.github.scoliossis.base.modules.Module;
import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.RegisterModule;
import com.github.scoliossis.base.modules.Setting;

@RegisterModule(
        name = "Kill Aura",
        description = "Now, I am become Death,\nthe destroyer of worlds.",
        category = ModuleCategory.Combat
)
public class KillAuraModule extends Module {
    @Setting(name = "Range")
    public Category rangeSubcategory = Category.New();

    @ConfigParent(parent = "Range")
    @Setting(name = "Rotation Range", min = 1, max = 8)
    public Float killAuraRotationRange = 5f;

    @ConfigParent(parent = "Range")
    @Setting(name = "Attack Range", min = 1, max = 6)
    public Float killAuraAttackRange = 3.1f;

    @ConfigParent(parent = "Range")
    @Setting(name = "Through Walls Range", min = 0, max = 6)
    public Float killAuraWallsRange = 2f;


    @Setting(name = "Targeting")
    public Category killAuraTargetingSubCategory = Category.New();

    @ConfigParent(parent = "Targeting")
    @Setting(name = "Target Sorting")
    public KillAuraSorting killAuraSorting = KillAuraSorting.Health;
    public enum KillAuraSorting {
        Distance, Hurt_Time, Health
    }

    // maybe add multi idk
    @ConfigParent(parent = "Targeting")
    @Setting(name = "Target Choice")
    public KillAuraTargeting killAuraTarget = KillAuraTargeting.Best;
    public enum KillAuraTargeting {
        Switch, Best, Single
    }

    @Setting(name = "Rotation")
    public Category rotationsSubcategory = Category.New();

    @ConfigParent(parent = "Rotation")
    @Setting(name = "Rotations")
    public KillAuraRotations rotations = KillAuraRotations.Simple;
    public enum KillAuraRotations {
        Simple, Smooth, Snap, None
    }

    @ConfigParent(parent = "Rotations")
    @Setting(name = "Min Rotation", modeParentString = "Simple", min = 0, max = 180)
    public Float minRotation = 0f;

    @ConfigParent(parent = "Rotations")
    @Setting(name = "Max Rotation", modeParentString = "Simple", min = 1, max = 180)
    public Float maxRotation = 100f;

    @ConfigParent(parent = "Rotations")
    @Setting(name = "Rotation Smoothing", modeParentString = "Smooth", min = 1f, max = 5)
    public Float smoothRotationSpeed = 1f;


    @Setting(name = "Attacking")
    public Category attackingSubCat = Category.New();

    @ConfigParent(parent = "Attacking")
    @Setting(name = "Keep Sprint")
    public Boolean auraKeepSprint = true;

    @ConfigParent(parent = "Attacking")
    @Setting(name = "Sword Only")
    public Boolean swordOnlyAura = true;

    @ConfigParent(parent = "Attacking")
    @Setting(name = "Attack Speed Min", min = 0, max = 20)
    public Float killAuraAttackSpeedMin = 5f;
    @ConfigParent(parent = "Attacking")
    @Setting(name = "Attack Speed Max", min = 0, max = 20)
    public Float killAuraAttackSpeedMax = 10f;

    @ConfigParent(parent = "Attacking")
    @Setting(name = "Autoblock", description = "Automatically blocks with your sword to reduce damage taken")
    public Boolean autoblock = true;

    @ConfigParent(parent = "Autoblock")
    @Setting(name = "mode")
    public AutoBlockMode autoblockMode = AutoBlockMode.Hypixel;
    public enum AutoBlockMode {
        Hypixel,
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
