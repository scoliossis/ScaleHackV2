package com.github.scoliossis.base.commands;

public abstract class Command {
    public String commandName = "";
    public String usage = "";
    public String help = "";

    public abstract boolean handleCommand(String message);

}
