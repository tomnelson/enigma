/*
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package net.tom.j3d.enigma;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */
public class InfoDialog extends Frame
{
   protected TextArea  text;
   protected Label     label;
   protected Button     okButton;

   /**
     * Create an instance. Set the label and the dimensions of
     * the text area.
     */
   public InfoDialog(String title, String labelStr, int rows, int cols)
   {
      super(title);
      setBackground(SystemColor.control);
      setLayout(new BorderLayout());
      
      text = new TextArea(rows,cols);
      label = new Label(labelStr);
      okButton = new Button("Dismiss");
      okButton.addActionListener(new ActionListener() {
	 public void actionPerformed(ActionEvent e) {
	    setVisible(false);
	    dispose();
	 }
      });

      this.setLayout(new BorderLayout(15, 15));
      this.add("North", label);
      Panel controls = new Panel();
      controls.add(okButton);
      this.add("South", controls);
      this.add("Center", text);
      this.pack();
   }

   public InfoDialog(String title, String labelStr)
   {
      this(title, labelStr, 40, 5);
   }
   /** Set the text in the TextArea.
     */
   public void setText(String textStr)
   {
      this.text.setText(textStr);
   }

   /** Append to the text in the TextArea.
     */
   public void appendText(String textStr)
   {
      text.append(textStr);
   }
}
