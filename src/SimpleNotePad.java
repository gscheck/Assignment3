import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import javax.swing.*;

// Revision: AB
// Date: 6/7/2019
// Revision by: Garth Scheck

public class SimpleNotePad  extends JFrame implements ActionListener {
    JMenuBar mb = new JMenuBar();
    JMenu fm = new JMenu("File");
    JMenu em = new JMenu("Edit");
    JTextPane d = new JTextPane();
    JMenuItem nf = new JMenuItem("New File");
    JMenuItem sf = new JMenuItem("Save File");
    JMenuItem opf = new JMenuItem("Open");
    JMenu rf = new JMenu("Recent");
    JMenuItem pf = new JMenuItem("Print File");
    JMenuItem u = new JMenuItem("Replace");
    JMenuItem c = new JMenuItem("Copy");
    JMenuItem p = new JMenuItem("Paste");

    final int TOP = 0;

    NotePadFunctionality func = new NotePadFunctionality();

    // constructor
    public SimpleNotePad() {
        setTitle("A Simple Notepad Tool");

        // add
        fm.add(opf);
        fm.addSeparator();
        fm.add(nf);
        fm.addSeparator();
        fm.add(sf);
        fm.addSeparator();
        fm.add(pf);
        fm.addSeparator();
        fm.add(rf);

        // add items to edit menu
        em.add(u);
        em.add(c);
        em.add(p);

        // add listeners
        opf.addActionListener(this);
        opf.setActionCommand("open");
        rf.addActionListener(this);
        rf.setActionCommand("recent");
        nf.addActionListener(this);
        nf.setActionCommand("new");
        sf.addActionListener(this);
        sf.setActionCommand("save");
        pf.addActionListener(this);
        pf.setActionCommand("print");
        c.addActionListener(this);
        c.setActionCommand("copy");
        p.addActionListener(this);
        p.setActionCommand("paste");
        u.addActionListener(this);
        u.setActionCommand("replace");

        // add file menu to menu bar
        mb.add(fm);
        // add edit menu to menu bar
        mb.add(em);

        setJMenuBar(mb);
        add(new JScrollPane(d));
        setPreferredSize(new Dimension(600,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }

    // main entry point
    public static void main(String[] args) {
        SimpleNotePad app = new SimpleNotePad();
    }

    // get printable item
    private Printable getPrintable()
    {
        return new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {
                if (pageNum>0)
                    return Printable.NO_SUCH_PAGE;
                pg.drawString(d.getText(), 500, 500);
                paint(pg);
                return Printable.PAGE_EXISTS;
            }
        };
    }

    // Add file name to top of recent item menu
    private void recentItem(String fName){
        boolean found = false;
        int idx = 0;

        // create menu item from file name
        JMenuItem item = new JMenuItem(fName);

        // get items in recent item menu
        Object subComp[] = rf.getMenuComponents();

        // iterate through items and compare to one being added
        for (int cmpIdx = 0; cmpIdx < rf.getMenuComponents().length; cmpIdx++) {
            JMenuItem menuItem1 = (JMenuItem) subComp[cmpIdx];

            String itemStr = menuItem1.getText();

            // if same, mark as found
            if(itemStr.equals(fName)) {
                idx = cmpIdx;
                found = true;
            }
        }

        // if found remove from recent items
        if(found)
            rf.remove(idx);

        // add file name to top of recent items
        rf.add(item, TOP);
        item.addActionListener(this);
        item.setActionCommand(fName);
    }

    // event handler for menu
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("new")) {
            d.setText("");
        }else if(e.getActionCommand().equals("save")) {
            func.save(d.getText());
        }else if(e.getActionCommand().equals("print")) {
            func.print(getPrintable());
        }else if(e.getActionCommand().equals("copy")) {
            d.copy();
        }else if(e.getActionCommand().equals("paste")) {
            d.paste();
        }else if(e.getActionCommand().equals("replace")) {
            // display dialog for text entry
            String s = (String)JOptionPane.showInputDialog("Replace or insert with:");

            // replace selected text with text entry
            d.replaceSelection(s);
        }else if(e.getActionCommand().equals("open")) {
            // open file
            String fName = func.open();
            // store as recent item
            recentItem(fName);
            // read file into pane
            d.setText(func.read(fName));
        }else
        {
            // get file name selected
            String fName = e.getActionCommand();
            // store as recent
            recentItem(fName);
            // read file into pane
            d.setText(func.read(e.getActionCommand()));
        }

    }
}
