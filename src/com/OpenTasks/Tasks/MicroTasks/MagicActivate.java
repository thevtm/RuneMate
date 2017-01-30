package com.OpenTasks.Tasks.MicroTasks;

import com.OpenTasks.Logger;
import com.OpenTasks.Task;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;

/**
 * Created by VTM on 9/7/2016.
 */
public class MagicActivate<T> extends Task {

  private T magic;
  private int ableToCastTextureID;

  protected MagicActivate(int priority, T magic, int ableToCastTextureID) {
    super("MagicActivate", priority);
    this.magic = magic;
    this.ableToCastTextureID = ableToCastTextureID;
  }

  public static MagicActivate<Magic> fromStandard(int priority, Magic magic, int ableToCastTextureID) {
    return new MagicActivate<>(priority, magic, ableToCastTextureID);
  }

  public static MagicActivate<Magic.Ancient> fromAncient(int priority, Magic.Ancient magic, int ableToCastTextureID) {
    return new MagicActivate<>(priority, magic, ableToCastTextureID);
  }

  public static MagicActivate<Magic.Lunar> fromLunar(int priority, Magic.Lunar magic, int ableToCastTextureID) {
    return new MagicActivate<>(priority, magic, ableToCastTextureID);
  }

  @Override
  public boolean validate() {
    // 1. Is not selected already
    boolean isMagicNotSelected = false;

    if (magic instanceof Magic) {
      isMagicNotSelected = ((Magic) magic).isSelected();
    } else if (magic instanceof Magic.Ancient) {
      isMagicNotSelected = ((Magic.Ancient) magic).isSelected();
    } else if (magic instanceof Magic.Lunar) {
      isMagicNotSelected = ((Magic.Lunar) magic).isSelected();
    }

    // 2. Is able to cast magic
    boolean isAbleToCastMagic = false;

    if (magic instanceof Magic) {
      isAbleToCastMagic = ((Magic) magic).getComponent().getSpriteId() == ableToCastTextureID;
    } else if (magic instanceof Magic.Ancient) {
      isAbleToCastMagic = ((Magic.Ancient) magic).getComponent().getSpriteId() == ableToCastTextureID;
    } else if (magic instanceof Magic.Lunar) {
      isAbleToCastMagic = ((Magic.Lunar) magic).getComponent().getSpriteId() == ableToCastTextureID;
    }

    Logger.debug("isMagicNotSelected: %b, isAbleToCastMagic: %b",
        isMagicNotSelected, isAbleToCastMagic);

    return isMagicNotSelected
      && isAbleToCastMagic;
  }

  @Override
  public void execute() {
    Logger.debug("Activating magic.");

    if (magic instanceof Magic) {
      ((Magic) magic).activate();
    } else if (magic instanceof Magic.Ancient) {
      ((Magic.Ancient) magic).activate();
    } else if (magic instanceof Magic.Lunar) {
      ((Magic.Lunar) magic).activate();
    }

    super.execute();
  }
}
