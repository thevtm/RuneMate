package com.OpenTasks.Tasks.MicroTasks;

import com.OpenTasks.Logger;
import com.OpenTasks.Task;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceWindows;
import com.runemate.game.api.hybrid.local.hud.interfaces.Openable;

/**
 * Created by VTM on 8/7/2016.
 */
public class InterfaceWindowOpen extends Task {

  private Openable interfaceWindow;

  private InterfaceWindowOpen(int priority, Openable interfaceWindow) {
    super("InterfaceWindowOpen", priority);
    this.interfaceWindow = interfaceWindow;
  }

  public static InterfaceWindowOpen Equipament(int priority) {
    return new InterfaceWindowOpen(priority, InterfaceWindows.getEquipment());
  }

  public static InterfaceWindowOpen Inventory(int priority) {
    return new InterfaceWindowOpen(priority, InterfaceWindows.getInventory());
  }

  public static InterfaceWindowOpen Magic(int priority) {
    return new InterfaceWindowOpen(priority, InterfaceWindows.getMagic());
  }

  public static InterfaceWindowOpen Prayer(int priority) {
    return new InterfaceWindowOpen(priority, InterfaceWindows.getPrayer());
  }

  public static InterfaceWindowOpen Skills(int priority) {
    return new InterfaceWindowOpen(priority, InterfaceWindows.getSkills());
  }

  @Override
  public boolean validate() {
    try {
      // 1. Interface Window is closed
      boolean isInterfaceWindowClosed = !interfaceWindow.isOpen();

      Logger.debug("isInterfaceWindowClosed: %b", isInterfaceWindowClosed);

      return isInterfaceWindowClosed;

    } catch (Exception e) {
      Logger.error("Unable to validate", e);
      return false;
    }
  }

  @Override
  public void execute() {
    Logger.debug("Opening Interface Window...");

    interfaceWindow.open();

    super.execute();
  }

}
