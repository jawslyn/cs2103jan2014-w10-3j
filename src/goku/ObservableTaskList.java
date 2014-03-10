package goku;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ObservableTaskList extends TaskList implements ListModel<Task> {

  private List<ListDataListener> listeners = new ArrayList<ListDataListener>();

  @Override
  public int addTask(Task task) {
    // TODO Auto-generated method stub
    int newId = super.addTask(task);
    notifyChange();
    return newId;
  }

  private void notifyChange() {
    for (ListDataListener listener : listeners) {
      listener.contentsChanged(new ListDataEvent(this,
          ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
    }
  }

  @Override
  public void addListDataListener(ListDataListener l) {
    listeners.add(l);
  }

  @Override
  public Task deleteTaskById(int id) {
    Task task = super.deleteTaskById(id);
    if (task != null) {
      notifyChange();
    }
    return task;
  }

  @Override
  public Task getElementAt(int index) {
    return getTaskByIndex(index);
  }

  @Override
  public int getSize() {
    return size();
  }

  @Override
  public void removeListDataListener(ListDataListener l) {
    listeners.remove(l);
  }

}
