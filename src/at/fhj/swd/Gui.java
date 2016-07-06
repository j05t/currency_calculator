package at.fhj.swd;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import jdk.nashorn.api.scripting.URLReader;

import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.DefaultComboBoxModel;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Window.Type;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JSeparator;
import java.awt.Font;

public class Gui {

	private JFrame frmWhrungsrechner;
	private JProgressBar progressBar;
	private JComboBox<Currency> source_currency;
	private JComboBox<Currency> target_currency;
	private JTextField text;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frmWhrungsrechner.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Gui() {
		initialize();
		loadCurrencies();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWhrungsrechner = new JFrame();
		frmWhrungsrechner.setTitle("Währungsrechner 0.1");
		frmWhrungsrechner.setBounds(100, 100, 450, 300);
		frmWhrungsrechner.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frmWhrungsrechner.getContentPane().add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblNewLabel = new JLabel("Währung:");
		panel.add(lblNewLabel);

		source_currency = new JComboBox();
		panel.add(source_currency);
		source_currency.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTargetCurrencies(((Currency) source_currency.getSelectedItem()).getShortName());	
			}
		});
		
		JLabel lblZielwhrung = new JLabel("Zielwährung:");
		panel.add(lblZielwhrung);

		target_currency = new JComboBox();
		panel.add(target_currency);
		target_currency.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				loadExchangeRates((Currency) source_currency.getSelectedItem());				
			}
		});

		JSeparator separator = new JSeparator();
		panel.add(separator);

		text = new JTextField();
		text.setFont(new Font("Dialog", Font.PLAIN, 14));
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setText("Laden Sie zuerst die aktuellen Umrechnungskurse.");
		text.setEditable(false);
		panel.add(text);

		JSeparator separator_1 = new JSeparator();
		panel.add(separator_1);

		JButton btnNewButton = new JButton("Umrechnen");
		panel.add(btnNewButton);

		progressBar = new JProgressBar();
		panel.add(progressBar);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO: Umrechnung
			}
		});
	}

	private void loadCurrencies() {

		// http://docs.oracle.com/javase/6/docs/api/javax/swing/SwingWorker.html
		SwingWorker<List<Currency>, Void> worker = new SwingWorker<List<Currency>, Void>() {
			@Override
			protected List<Currency> doInBackground() throws Exception {
				progressBar.setIndeterminate(true);

				List<Currency> list = new ArrayList<Currency>();
				String line = "";

				// https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
				try (BufferedReader br = new BufferedReader(new InputStreamReader(
						new URL("http://houston.fh-joanneum.at/sodev2/currencies").openStream()))) {
					while ((line = br.readLine()) != null) {
						String[] split = line.split(";");
						list.add(new Currency(split[0], split[1]));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return list;
			}

			@Override
			public void done() {
				try {
					for (Currency item : get())
						source_currency.addItem(item);
				} catch (Exception e) {
					e.printStackTrace();
				}
				progressBar.setIndeterminate(false);
			}

		};

		worker.execute();
	}
	
	private void loadExchangeRates(Currency selectedCurrency) {
		// http://houston.fh-joanneum.at/sodev2/rates?baseCurrency=EUR
		String baseCurrency = selectedCurrency.getShortName();

		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Ein " + baseCurrency + " sind ");
			}
		});
		
		t.start();
	}
	
	private void updateTargetCurrencies(String selectedItem) {
		target_currency.removeAllItems();
		
		for (int i=0; i < source_currency.getItemCount(); i++)
			if (!source_currency.getItemAt(i).getShortName().equals(selectedItem))
				target_currency.addItem(source_currency.getItemAt(i));
		
		target_currency.invalidate();
	}

}
