
package com.mycompany.texteditor;

import java.awt.Font;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JSeparator;
import javax.swing.JColorChooser;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.UIManager;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledEditorKit.BoldAction;
import javax.swing.text.StyledEditorKit.ItalicAction;
import javax.swing.text.StyledEditorKit.FontFamilyAction;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.undo.UndoManager;
import javax.swing.event.UndoableEditListener;
import javax.swing.event.UndoableEditEvent;
import java.util.List;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import javax.swing.KeyStroke;

public class TextEditor{

    private static void setAccelerator(KeyStroke keyStroke) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
	private JFrame frame__;
	private JTextPane editor__;
	private JComboBox<String> textAlignComboBox__;
	private JComboBox<String> fontFamilyComboBox__;
	private UndoManager undoMgr__;
	private File file__;

	enum BulletActionType {INSERT, REMOVE};
	enum NumbersActionType {INSERT, REMOVE};
	enum UndoActionType {UNDO, REDO};
	private boolean startPosPlusBullet__;
	private boolean startPosPlusNum__;
	
	private static final String MAIN_TITLE = "My Editor - ";
	private static final String DEFAULT_FONT_FAMILY = "SansSerif";
	private static final int DEFAULT_FONT_SIZE = 18;
	private static final List<String> FONT_LIST = Arrays.asList(new String [] {"Arial", "Calibri", "Cambria", "Courier New", "Comic Sans MS", "Dialog", "Georgia", "Helevetica", "Lucida Sans", "Monospaced", "Tahoma", "Times New Roman", "Verdana"});
	
	private static final char BULLET_CHAR = '\u2022';
	private static final String NUMBERS_ATTR = "NUMBERS";
	
	public static void main(String [] args)
			throws Exception {

		UIManager.put("TextPane.font", new Font(DEFAULT_FONT_FAMILY, Font.PLAIN, DEFAULT_FONT_SIZE));
		UIManager.setLookAndFeel(new NimbusLookAndFeel());
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
			
				new TextEditor().createAndShowGUI();
			}
		});
	}

	private void createAndShowGUI() {
	
		frame__ = new JFrame();
		setFrameTitleWithExtn("New file");
		editor__ = new JTextPane();
		JScrollPane editorScrollPane = new JScrollPane(editor__);

		editor__.setDocument(getNewDocument());
		
		

		undoMgr__ = new UndoManager();
		EditButtonActionListener editButtonActionListener =new EditButtonActionListener();

		JButton boldButton = new JButton(new BoldAction());
		boldButton.setHideActionText(true);
		boldButton.setText("Bold");
		boldButton.addActionListener(editButtonActionListener);
		JButton italicButton = new JButton(new ItalicAction());
		italicButton.setHideActionText(true);
		italicButton.setText("Italic");
		italicButton.addActionListener(editButtonActionListener);
		JButton colorButton = new JButton("Set Color");
		colorButton.addActionListener(new ColorActionListener());
		Vector<String> editorFonts = getEditorFonts();
		editorFonts.add(0, "Font Family");
		fontFamilyComboBox__ = new JComboBox<String>(editorFonts);
		fontFamilyComboBox__.setEditable(false);
		fontFamilyComboBox__.addItemListener(new FontFamilyItemListener());
	
		JButton undoButton = new JButton("Undo");
		undoButton.addActionListener(new UndoActionListener(UndoActionType.UNDO));
		JButton redoButton = new JButton("Redo");
		redoButton.addActionListener(new UndoActionListener(UndoActionType.REDO));
	        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel1.add(new JSeparator(SwingConstants.VERTICAL));
		panel1.add(boldButton);
		panel1.add(italicButton);
		panel1.add(new JSeparator(SwingConstants.VERTICAL));	
		panel1.add(colorButton);
		panel1.add(new JSeparator(SwingConstants.VERTICAL));
		panel1.add(fontFamilyComboBox__);		
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel2.add(new JSeparator(SwingConstants.VERTICAL));
		panel2.add(new JSeparator(SwingConstants.VERTICAL));
		panel2.add(new JSeparator(SwingConstants.VERTICAL));
		panel2.add(undoButton);
		panel2.add(redoButton);
		JPanel toolBarPanel = new JPanel();
		toolBarPanel.setLayout(new BoxLayout(toolBarPanel, BoxLayout.PAGE_AXIS));
		toolBarPanel.add(panel1);
		toolBarPanel.add(panel2);
		frame__.add(toolBarPanel, BorderLayout.NORTH);
		frame__.add(editorScrollPane, BorderLayout.CENTER);
		JMenuBar menuBar = new JMenuBar();
		frame__.setSize(900, 500);
		frame__.setLocation(150, 80);
		frame__.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame__.setVisible(true);
		editor__.requestFocusInWindow();
	}
	private void setFrameTitleWithExtn(String titleExtn) {
		frame__.setTitle(MAIN_TITLE + titleExtn);
	}
	private StyledDocument getNewDocument() {
		StyledDocument doc = new DefaultStyledDocument();
		doc.addUndoableEditListener(new UndoEditListener());
		return doc;
	}

	private Vector<String> getEditorFonts() {
		String [] availableFonts =
			GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		Vector<String> returnList = new Vector<>();
		for (String font : availableFonts) {
			if (FONT_LIST.contains(font)) {
				returnList.add(font);
			}
		}
		return returnList;
	}
	private class EditButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			editor__.requestFocusInWindow();
		}
	}
	private class ColorActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Color newColor =
				JColorChooser.showDialog(frame__, "Choose a color", Color.BLACK);
			if (newColor == null) {
				editor__.requestFocusInWindow();
				return;
			}
			SimpleAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setForeground(attr, newColor);
			editor__.setCharacterAttributes(attr, false);
			editor__.requestFocusInWindow();
		}
	}

	private class FontFamilyItemListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if ((e.getStateChange() != ItemEvent.SELECTED) ||
				(fontFamilyComboBox__.getSelectedIndex() == 0)) {	
				return;
			}
			String fontFamily = (String) e.getItem();
			fontFamilyComboBox__.setAction(new FontFamilyAction(fontFamily, fontFamily));	
			fontFamilyComboBox__.setSelectedIndex(0); 
			editor__.requestFocusInWindow();
		}
	} 
	private class UndoEditListener implements UndoableEditListener {
		@Override
		public void undoableEditHappened(UndoableEditEvent e) {
			undoMgr__.addEdit(e.getEdit());
		}
	}
        
	private class UndoActionListener implements ActionListener {
		private UndoActionType undoActionType;
		public UndoActionListener(UndoActionType type) {
			undoActionType = type;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (undoActionType) {
				case UNDO:
					if (! undoMgr__.canUndo()) {
						editor__.requestFocusInWindow();
						return; 
					}
					undoMgr__.undo();
					break;
				case REDO:
					if (! undoMgr__.canRedo()) {
						editor__.requestFocusInWindow();
						return;
					}
					undoMgr__.redo();
			}
			editor__.requestFocusInWindow();
		}
	} 
}
