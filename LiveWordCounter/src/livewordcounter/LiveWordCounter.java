/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package livewordcounter;

import javax.swing.*;

import java.awt.*;              //for layout managers and more
import java.awt.event.*;        //for action events

public class LiveWordCounter extends JPanel implements ActionListener, KeyListener {
    
    boolean firstTime = true;
    String defaultString = "Type here...";
    JTextArea textArea, displayArea;

    public LiveWordCounter() {
        textArea = new JTextArea(defaultString);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(250, 250));
        
        displayArea = new JTextArea();
        updateWC();
        displayArea.setEditable(false);
        JScrollPane displayScrollPane = new JScrollPane(displayArea);
        displayScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        displayScrollPane.setPreferredSize(new Dimension(250, 250));
        
        add(areaScrollPane, BorderLayout.WEST);
        add(displayScrollPane, BorderLayout.EAST);
        textArea.addKeyListener(this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LiveWordCounter");
        frame.setTitle("Live Word Counter");
        //frame.setSize();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container contentPane = frame.getContentPane();
        contentPane.add(new LiveWordCounter());
        
        //frame.add(new LiveWordCounter());
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public void keyPressed(KeyEvent evt) {
        int key = evt.getKeyCode();
        if (firstTime && (key == KeyEvent.VK_ALPHANUMERIC
                       || key == KeyEvent.VK_TAB
                       || key == KeyEvent.VK_ENTER)) {
            textArea.setText("");
            firstTime = false;
        }
        
        updateWC();
    }
    
    public void keyReleased(KeyEvent evt) { updateWC(); }
    
    public void keyTyped(KeyEvent evt) { updateWC(); }
    
    public void actionPerformed(ActionEvent evt) {}
    
    public void updateWC() {
        //System.out.println("===\n" + textArea.getText() + "\n===");
        if (firstTime && textArea.getText().equals(defaultString)) {
            displayArea.setText("Number of words: 0\nNumber of characters: 0"); return;
        }
        displayArea.setText("Number of words: " + countWords(textArea.getText()) + "\nNumber of characters: " + countCharacters(textArea.getText()));
    }
    
    public int countSpaces(String str) {
        int s = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') s++;
        }
        return s;
    }

    public static int countWords(String str) {
        int w = 0; char before, current, after;
        str = str.replaceAll("\n", " ");
        str = str.replaceAll("\t", " ");
        boolean allSpaces = true;
        // if string's length is zero, return 0
        if (str.length() == 0) return w;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ' ') {
                allSpaces = false;
                i = str.length();
            }
        }
        for (int j = 1; j < (str.length() - 1); j++) {
            before = str.charAt(j - 1);
            current = str.charAt(j);
            //after = str.charAt(j + 1);
            if (current == ' ' && before != ' ') {
                int k = j + 1;
              	while (k < str.length() - 1 && str.charAt(k) == ' ') {
                  	str = str.substring(0, k) + str.substring(k + 1);
                }
              	after = str.charAt(k);
                if (after != ' ') w++;
            }
        }
        if (allSpaces) return 0;
        return w + 1;
    }
    
    public static int countCharacters(String str) {
        str = str.replaceAll("\n", "");
        str = str.replaceAll("\t", "");
        return str.length();
    }
}