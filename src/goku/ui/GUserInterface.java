package goku.ui;

import goku.GOKU;
import goku.ObservableTaskList;
import goku.Result;
import goku.action.Action;
import goku.action.DisplayAction;
import goku.autocomplete.WordAutocomplete;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.BadLocationException;

/**
 * @author ZhiAn
 * 
 */
public class GUserInterface extends JFrame implements UserInterface,
    DocumentListener, ListDataListener {
  private GOKU goku;
  private JLabel jLabel1;
  private JScrollPane jScrollPane1;
  private JTextArea textArea;
  private JList listArea;
  private ObservableTaskList list;
  private WordAutocomplete autoComplete;

  private static final String COMMIT_ACTION = "commit";

  private static enum Mode {
    INSERT, COMPLETION
  };

  private List<String> words;
  private Mode mode = Mode.INSERT;
  private InputParser pparser = new InputParser(goku);

  public GUserInterface(GOKU goku) {
    super("G.O.K.U.");
    this.goku = goku;
    goku.setTaskList(new ObservableTaskList());
    list = (ObservableTaskList) goku.getTaskList();
    list.addListDataListener(this);

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    Container contentPane = this.getContentPane();
    setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

    setLabel();

    setInputArea();

    setListArea();

    this.pack();
    this.setVisible(true);

    setCompletionWords();
  }

  private void setCompletionWords() {
    autoComplete = new WordAutocomplete();
    words = new ArrayList<String>(5);
    words.add("spark");
    words.add("special");
    words.add("spectacles");
    words.add("spectacular");
    words.add("swing");
  }

  private void setListArea() {
    // String[] data = { "hi", "bye" };
    ArrayList<String> data = new ArrayList<String>();
    data.add("HI");
    data.add("BYE");
    listArea = new JList(); // data has type Object[]
    listArea.setModel(list);
    listArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listArea.setLayoutOrientation(JList.VERTICAL);
    listArea.setVisibleRowCount(-1);

    JScrollPane listScroller = new JScrollPane(listArea);
    listScroller.setPreferredSize(new Dimension(250, 80));
    getContentPane().add(listScroller);
  }

  private void setInputArea() {
    textArea = new JTextArea();
    textArea.setColumns(20);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    getContentPane().add(textArea);

    textArea.getDocument().addDocumentListener(this);

    InputMap im = textArea.getInputMap();
    ActionMap am = textArea.getActionMap();
    im.put(KeyStroke.getKeyStroke("ENTER"), COMMIT_ACTION);
    am.put(COMMIT_ACTION, new CommitAction());
  }

  private void setLabel() {
    jLabel1 = new JLabel("Preim implementation of GUI for G.O.K.U.");
    getContentPane().add(jLabel1);
  }

  @Override
  public void feedBack(Result result) {
    // TODO Auto-generated method stub
  }

  @Override
  public void changedUpdate(DocumentEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void insertUpdate(DocumentEvent ev) {
    if (ev.getLength() != 1) {
      return;
    }

    int pos = ev.getOffset();
    String content = null;
    try {
      content = textArea.getText(0, pos + 1);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }

    // Find where the word starts
    int w;
    for (w = pos; w >= 0; w--) {
      if (!Character.isLetter(content.charAt(w))) {
        break;
      }
    }
    // if (pos - w < 2) {
    // // Too few chars
    // return;
    // }

    String prefix = content.substring(w + 1).toLowerCase();

    List<String> completions = autoComplete.complete(prefix);
    if (completions.size() == 0) {
      mode = Mode.INSERT;
    } else {
      String match = completions.get(0);
      String completion = match.substring(pos - w);
      SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
    }

  }

  @Override
  public void removeUpdate(DocumentEvent arg0) {
    // TODO Auto-generated method stub

  }

  private class CompletionTask implements Runnable {
    String completion;
    int position;

    CompletionTask(String completion, int position) {
      this.completion = completion;
      this.position = position;
    }

    @Override
    public void run() {
      textArea.insert(completion, position);
      textArea.setCaretPosition(position + completion.length());
      textArea.moveCaretPosition(position);
      mode = Mode.COMPLETION;
    }
  }

  private class CommitAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent ev) {
      if (mode == Mode.COMPLETION) {
        int pos = textArea.getSelectionEnd();
        textArea.insert(" ", pos);
        textArea.setCaretPosition(pos + 1);
        mode = Mode.INSERT;
      } else {
        String input = textArea.getText();
        // Command c = makeCommand(input);
        Action action = null;
        try {
          action = pparser.parse(input);
        } catch (MakeActionException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        if (action instanceof DisplayAction) {
          System.out.println("DISPLAY");
        } else {
          Result result = action.doIt();
          feedBack(result);
        }
        // System.out.println(c.toString());
        // Result result = goku.executeCommand(c);
        // feedBack(result);
        textArea.setText("");
        // textArea.replaceSelection("\n");
      }
    }
  }

  public static void main(String args[]) {
    GOKU goku = new GOKU();
    goku.setTaskList(new ObservableTaskList());
    GUserInterface gui = new GUserInterface(goku);
    gui.run();
  }

  @Override
  public String getUserInput() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
  }

  @Override
  public void run() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        setVisible(true);
      }
    });

  }
}
