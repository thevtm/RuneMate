package com.OpenTasks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VTM on 9/6/2016.
 */
public abstract class Task {

  /* FIELDS */

  private Task parent;
  protected List<Task> children;

  public final String name;
  public final int priority;

  private Task lastChildTaskExecuted;

  /* METHODS */

  public Task(String name, int priority) {
    this.name = name;
    this.priority = priority;

    children = new ArrayList<>();
  }

  public abstract boolean validate();

  public void execute() {
    // 1. No child case
    if (children.isEmpty()) return;

    // 2. First execution
    boolean isFirstExec = lastChildTaskExecuted == null;
    if (isFirstExec) {
      lastChildTaskExecuted = children.get(0);
    }

    // 3. First try to run the last task executed
    if (lastChildTaskExecuted.validate()) {
      if (isFirstExec) lastChildTaskExecuted.begin();
      lastChildTaskExecuted.execute();
      return;
    } else if (!isFirstExec){
      lastChildTaskExecuted.end();
    }

    // 4. Secondly try to execute the others tasks in order
    int indexOfLastTaskExecuted = children.indexOf(lastChildTaskExecuted);
    int nChildren = children.size();

    for (int i = (indexOfLastTaskExecuted + 1) % nChildren; i != indexOfLastTaskExecuted; i = (i + 1) % nChildren) {
      Task ti = children.get(i);
      if (ti.validate()) {
        lastChildTaskExecuted = ti;
        lastChildTaskExecuted.begin();
        ti.execute();
        return;
      }
    }
  }

  public void begin() {

  }

  public void end() {

  }

  public void configure(Object config) {
    // Config children
    for (Task child : children) child.configure(config);
  }

  public void addChild(Task child) {
    if (child == this) throw new RuntimeException("A child can't be its own parent.");
    if (children.contains(child)) throw new RuntimeException("This child has already been added.");

    // Add child to the children list
    children.add(child);
    child.setParent(this);

    // Sort children based on priority
    children.sort((childA, childB) -> childA.priority - childB.priority);
  }

  public void removeChild(Task child) {
    if (!children.contains(child)) {
      throw new RuntimeException("I'm not your father Task. (The task you are trying to remove is not a child of this task)");
    }

    if (lastChildTaskExecuted == child) {
      lastChildTaskExecuted = null;
    }

    children.remove(child);
    child.setParent(null);
  }

  public void clearChildren() {
    for (Task child : children) {
      child.setParent(null);
    }

    lastChildTaskExecuted = null;
    children.clear();
  }

  public Task getParent() {
    return parent;
  }

  private void setParent(Task parent) {
    this.parent = parent;
  }

  public List<Task> getChildren() {
    return children;
  }

  public Task getLastChildTaskExecuted() {
    return lastChildTaskExecuted;
  }

}
