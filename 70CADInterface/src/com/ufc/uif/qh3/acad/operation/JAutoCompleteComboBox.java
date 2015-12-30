/**
 *
 */
package com.ufc.uif.qh3.acad.operation;

/**
 * @author Liugz
 *
 */
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.util.Arrays;

/**
 * @author SamZheng �����Զ���鹦�ܵ�CombBox
 */
public class JAutoCompleteComboBox extends JComboBox {
  private AutoCompleter completer;

  public JAutoCompleteComboBox() {
    super();
    addCompleter();
  }

  public JAutoCompleteComboBox(JComboBox combo) {
    super();
    addCompleter(combo);
  }

  /**
   * @author Liugz
   * @create on 2009-8-12 This project for CAD_Concrete
   * @param combo
   */
  private void addCompleter(JComboBox combo) {
    setEditable(true);
    completer = new AutoCompleter(combo);
  }

  public JAutoCompleteComboBox(ComboBoxModel cm) {
    super(cm);
    addCompleter();
  }

  public JAutoCompleteComboBox(Object[] items) {
    super(items);
    addCompleter();
  }

  public JAutoCompleteComboBox(List v) {
    super((Vector) v);
    addCompleter();
  }

  private void addCompleter() {
    setEditable(true);
    completer = new AutoCompleter(this);
  }

  public void autoComplete(String str) {
    this.completer.autoComplete(str, str.length());
  }

  public String getText() {
    return ((JTextField) getEditor().getEditorComponent()).getText();
  }

  public void setText(String text) {
    ((JTextField) getEditor().getEditorComponent()).setText(text);
  }

  public boolean containsItem(String itemString) {
    for (int i = 0; i < this.getModel().getSize(); i++) {
      String _item = "" + this.getModel().getElementAt(i);
      if (_item.equals(itemString)) {
        return true;
      }
    }
    return false;
  }

  /*
   * ���Է���
   */
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    Object[] items = new Object[] { "zzz", "zba", "aab", "abc", "aab",
        "dfg", "aba", "hpp", "pp", "hlp", "����1", "����2", "����������",
        "��������", "ͨ��" };
    // ��������
    // java.util.ArrayList list = new
    // java.util.ArrayList(Arrays.asList(items));
    // Collections.sort(list);
    // JComboBox cmb = new JAutoCompleteComboBox(list.toArray());
    Arrays.sort(items);
    JComboBox cmb = new JAutoCompleteComboBox(items);
    cmb.setSelectedIndex(-1);
    frame.getContentPane().add(cmb);
    frame.setSize(400, 80);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

/**
 * �Զ���������Զ��ҵ���ƥ�����Ŀ���������б����ǰ�档
 *
 * @author SamZheng
 */

class AutoCompleter implements KeyListener, ItemListener {

  private JComboBox owner = null;
  private JTextField editor = null;

  private ComboBoxModel model = null;

  public AutoCompleter(JComboBox comboBox) {
    owner = comboBox;
    editor = (JTextField) comboBox.getEditor().getEditorComponent();
    editor.addKeyListener(this);
    model = comboBox.getModel();
    owner.addItemListener(this);
  }

  public void keyTyped(KeyEvent e) {
    char ch = e.getKeyChar();
    if (ch == KeyEvent.CHAR_UNDEFINED /*|| Character.isISOControl(ch)*/
        || ch == KeyEvent.VK_DELETE) {
      return;
    }

    if (ch == KeyEvent.VK_ENTER) {
      int caretPosition = editor.getCaretPosition();
      String str = editor.getText();
      if (str.length() == 0) {
        return;
      }
      autoComplete(str, caretPosition);
    }
  }

  public void keyPressed(KeyEvent e) {
  }

  public void keyReleased(KeyEvent e) {
  }

  /**
   * �Զ���ɡ�������������ݣ����б����ҵ����Ƶ���Ŀ.
   */
  protected void autoComplete(String strf, int caretPosition) {
    Object[] opts;
    opts = getMatchingOptions(strf.substring(0, caretPosition));
    if (owner != null) {
      model = new DefaultComboBoxModel(opts);
      owner.setModel(model);
    }
    if (opts.length > 0) {
      // String str = opts[0].toString();
      if (caretPosition > editor.getText().length())
        return;
      // editor.setCaretPosition(caretPosition);
      // editor.setText(str/*editor.getText().trim().substring(0,caretPosition)*/);
      if (owner != null) {
        try {
          owner.showPopup();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  /**
   *
   * �ҵ����Ƶ���Ŀ, ���ҽ�֮���е��������ǰ�档
   *
   * @param str
   * @return ����������Ŀ���б�
   */
  protected Object[] getMatchingOptions(String str) {
    List v = new Vector();
    List v1 = new Vector();

    for (int k = 0; k < model.getSize(); k++) {
      Object itemObj = model.getElementAt(k);
      if (itemObj != null) {
        String item = itemObj.toString();
        if (item.contains(str)/* item.startsWith(str.toLowerCase()) */) {
          v.add(model.getElementAt(k));
        } else {
          v1.add(model.getElementAt(k));
        }
      } else {
        v1.add(model.getElementAt(k));
      }
    }
    for (int i = 0; i < v1.size(); i++) {
      v.add(v1.get(i));
    }
    if (v.isEmpty()) {
      v.add(str);
    }
    return v.toArray();
  }

  public void itemStateChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      int caretPosition = editor.getCaretPosition();
      if (caretPosition != -1) {
        try {
          editor.moveCaretPosition(caretPosition);
        } catch (IllegalArgumentException ex) {
          ex.printStackTrace();
        }
      }
    }
  }
}
