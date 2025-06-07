package Task6_1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ExplicitWord extends Thread {
    JTextArea area;
    volatile boolean flag;
    public ExplicitWord(JTextArea area) {
        this.area = area;
        flag = true;
    }

    public void run() {
        while (flag) {
            String text = area.getText();
            try {
                int index = text.indexOf("cholera ");
                if (index != -1) {
                    JOptionPane.showMessageDialog(null, "Nieodpowiednie słowo ");
                }
                sleep(1000);
            }catch (Exception e) {}
        }

    }
}


class Zamieniacz extends Thread {
    JTextArea okno;
    volatile boolean zakonczyc;
    public Zamieniacz(JTextArea comp) {
        okno = comp;
        zakonczyc = false;
    }

    public void run() {
        while (! zakonczyc) {
            try {
                String tekst = okno.getText();
                int indeks = tekst.indexOf("{"); //pokazuje miejsce danego symbolu, jesli nie ma -1
                if (indeks >= 0) {
                    okno.replaceRange("begin", indeks, indeks+1);
                    okno.setCaretPosition(tekst.length()+4); //kursor
                }
                else {
                    indeks = tekst.indexOf("}");
                    if (indeks >=0) {
                        okno.replaceRange("end", indeks, indeks+1);
                        okno.setCaretPosition(tekst.length()+2);
                    }
                }
                sleep(2000);
            }
            catch (Exception e) {}
        }
    }
}
 class NewJFrame extends JFrame {
    public NewJFrame() {
        initComponents();
        setSize(350, 250);
        watek = new Zamieniacz(jTextArea1);
        watek.start();
        exWord = new ExplicitWord(jTextArea1);
        exWord.start();
    }
    private void initComponents() {
        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            //Nadpisanie wlasna metoda
            public void windowClosing(WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        jTextArea1.setPreferredSize(new Dimension(300, 200));
        jScrollPane1.setViewportView(jTextArea1);
        getContentPane().add(jScrollPane1, BorderLayout.CENTER);
        pack();
    }
    private void formWindowClosing(WindowEvent evt) {
        watek.zakonczyc = true;
        watek = null;

        exWord.flag = false;
        exWord = null;
    }
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;
    private Zamieniacz watek;
    private ExplicitWord exWord;
}