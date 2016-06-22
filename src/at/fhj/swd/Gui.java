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

public class Gui {

	private JFrame frame;
	private JTextField txtLadenSieZuerst;
	private JProgressBar progressBar;
	private JComboBox<Currency> source_currency;
	private JComboBox<Currency> target_currency;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
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
	public Gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("Währung:");
		panel.add(lblNewLabel);

		source_currency = new JComboBox();
		panel.add(source_currency);

		JLabel lblZielwhrung = new JLabel("Zielwährung:");
		panel.add(lblZielwhrung);

		target_currency = new JComboBox();
		panel.add(target_currency);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Laden");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadCurrencies();
			}
		});

		progressBar = new JProgressBar();
		panel_1.add(progressBar);
		panel_1.add(btnNewButton);

		txtLadenSieZuerst = new JTextField();
		txtLadenSieZuerst.setText("Laden Sie zuerst die aktuellen Umrechnungskurse.");
		frame.getContentPane().add(txtLadenSieZuerst, BorderLayout.CENTER);
		txtLadenSieZuerst.setColumns(10);
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

}
