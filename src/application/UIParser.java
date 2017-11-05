package application;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
/*
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;*/
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UIParser {

	private JFrame frame;
	private JTextField txtUser;
	private JButton btnUploadJson;
	private JFileChooser fileChooser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIParser window = new UIParser();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UIParser() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1366, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
			
		btnUploadJson = new JButton("Upload JSON");
		btnUploadJson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser();
				if(fileChooser.showOpenDialog(btnUploadJson) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						JSONArray jsonArray = (JSONArray) new JSONParser().parse(new FileReader(file));
						JSONObject attributes;
						JSONArray children;
						JSONObject childrenObject;

						JSONObject styles;

						
						for(int i=0; i<jsonArray.size(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArray.get(i);
							if(jsonObject.get("type").toString().contains("Element")) {
								System.out.println(jsonObject);
								
								attributes = (JSONObject) jsonObject.get("attributes");

								styles = (JSONObject) attributes.get("style");

								int top = Integer.parseInt(styles.get("top").toString().substring(0, styles.get("top").toString().length()-2));
								int left = Integer.parseInt(styles.get("left").toString().substring(0, styles.get("left").toString().length()-2));
								int fontSize;
								int height;
								int width;
								if(jsonObject.get("tagName").toString().equals("label")) {
									children = (JSONArray) jsonObject.get("children");
									childrenObject = (JSONObject) children.get(0);
									JLabel newLabel = new JLabel(childrenObject.get("content").toString());	
									if(styles.get("fontSize") != null) {
										fontSize = Integer.parseInt(styles.get("fontSize").toString().substring(0, styles.get("fontSize").toString().length()-2));
										newLabel.setFont(new Font("Times New Roman", Font.PLAIN, fontSize));
									}else
										newLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
									FontMetrics fontMetrics = newLabel.getFontMetrics(newLabel.getFont());
									
									newLabel.setBounds(left, top, fontMetrics.stringWidth(childrenObject.get("content").toString()), fontMetrics.getHeight());
									
									frame.getContentPane().add(newLabel);
								}else if(jsonObject.get("tagName").toString().equals("input")) {

									
									if(attributes.get("type").toString().equals("text")) {
										JTextField newTextField = new JTextField();
										if(attributes.get("placeholder")!=null)
											newTextField.setText(attributes.get("placeholder").toString());
										else
											newTextField.setText("");
										if(styles.get("height")==null)
											height = 21;
										else
											height = (int) Math.round(Double.parseDouble(styles.get("height").toString().substring(0, styles.get("height").toString().length()-2)));
										
										if(styles.get("width")==null)
											width = 173;
										else
											width = (int) Math.round(Double.parseDouble(styles.get("width").toString().substring(0, styles.get("width").toString().length()-2)));

										newTextField.setBounds(left, top, width, height);
										frame.getContentPane().add(newTextField);
									}else {
										JButton btnNewButton;
										if(attributes.get("value")!=null)
											btnNewButton = new JButton(attributes.get("value").toString());
										else
											btnNewButton = new JButton("Submit");

										if(styles.get("height")==null)
											height = 21;
										else
											height = (int) Math.round(Double.parseDouble(styles.get("height").toString().substring(0, styles.get("height").toString().length()-2)));
										
										if(styles.get("width")==null)
											width = 58;
										else
											width = (int) Math.round(Double.parseDouble(styles.get("width").toString().substring(0, styles.get("width").toString().length()-2)));

										btnNewButton.setBounds(left, top, width, height);
										frame.getContentPane().add(btnNewButton);
									}
								}
							}
						}
						
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (org.json.simple.parser.ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
									
				}
				frame.getContentPane().repaint();
				frame.getContentPane().revalidate();

			}
		});
		btnUploadJson.setBounds(10, 11, 105, 23);
		frame.getContentPane().add(btnUploadJson);
		
	}
}
