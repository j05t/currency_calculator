package at.fhj.swd.ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.GridLayout;
import javax.swing.JSeparator;
import java.awt.Font;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import at.fhj.swd.service.CSVCurrencyService;
import at.fhj.swd.service.CurrencyService;

public class Gui {

	private JFrame frmWhrungsrechner;
	private JProgressBar progressBar;
	private JComboBox<CurrencyInfo> source_currency;
	private JComboBox<CurrencyInfo> target_currency;
	private JTextField statusText;
	private JTextField input;
	private Map<String, Float> exchangeRates;

	private CurrencyService curService;
	private Float amount;
	private boolean validInput = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frmWhrungsrechner.setVisible(true);
					window.loadCurrencies();
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
		curService = new CSVCurrencyService();
	}

	private void updateProgress(boolean inProgress, String status) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressBar.setIndeterminate(inProgress);
				statusText.setText(status);
			}
		});
	}

	private void loadCurrencies() {
		curService.setApiUrl("http://houston.fh-joanneum.at/sodev2/currencies");
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				updateProgress(true, "Lade Währungen");
				for (CurrencyInfo curInfo : curService.getCurrencies())
					source_currency.addItem(curInfo);

				updateProgress(false, "Währungen geladen, Quellwährung wählen");
			}
		});

		t.start();
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
		panel.setBorder(new CompoundBorder(null, new EmptyBorder(6, 10, 10, 10)));
		frmWhrungsrechner.getContentPane().add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblCurrency = new JLabel("Währung:");
		panel.add(lblCurrency);

		source_currency = new JComboBox<CurrencyInfo>();
		panel.add(source_currency);
		source_currency.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateTargetCurrencies(((CurrencyInfo) source_currency.getSelectedItem()).getFullName());
				loadExchangeRates();
			}

		});

		JLabel lblTargetCurrency = new JLabel("Zielwährung:");
		panel.add(lblTargetCurrency);

		target_currency = new JComboBox<CurrencyInfo>();
		panel.add(target_currency);
		target_currency.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				outputExchangeRate();
			}
		});

		JSeparator separator = new JSeparator();
		panel.add(separator);

		JLabel lblAmount = new JLabel("Betrag:");
		panel.add(lblAmount);

		input = new JTextField();
		panel.add(input);

		input.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					amount = Float.parseFloat(input.getText());
					validInput = true;
					outputExchangeRate();
				} catch (Exception e2) {
					statusText.setText("Ungültige Eingabe!");
					validInput = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

		JSeparator separator_1 = new JSeparator();
		panel.add(separator_1);

		statusText = new JTextField();
		statusText.setFont(new Font("Dialog", Font.PLAIN, 14));
		statusText.setHorizontalAlignment(SwingConstants.CENTER);
		statusText.setEditable(false);
		panel.add(statusText);

		progressBar = new JProgressBar();
		panel.add(progressBar);
	}

	private void loadExchangeRates() {
		if (source_currency.getSelectedItem() == null)
			return;

		String baseCurrency = ((CurrencyInfo) source_currency.getSelectedItem()).getShortName();

		// http://houston.fh-joanneum.at/sodev2/rates?baseCurrency=EUR
		curService.setApiUrl("http://houston.fh-joanneum.at/sodev2/rates?baseCurrency=" + baseCurrency);

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				updateProgress(true, "Lade Wechselkurse");
				exchangeRates = curService.getRatesForCurrency(baseCurrency);
				updateProgress(false, "Wechselkurse geladen, Zielwährung wählen");
			}
		});

		t.start();
	}

	private void updateTargetCurrencies(String selectedItem) {
		target_currency.removeAllItems();

		for (int i = 0; i < source_currency.getItemCount(); i++)
			if (!source_currency.getItemAt(i).getFullName().equals(selectedItem))
				target_currency.addItem(source_currency.getItemAt(i));
	}

	private void outputExchangeRate() {
		if (!validInput || exchangeRates == null || target_currency == null
				|| ((CurrencyInfo) target_currency.getSelectedItem()) == null)
			return;

		String baseCurrency = ((CurrencyInfo) source_currency.getSelectedItem()).getShortName();
		String targetCurrency = ((CurrencyInfo) target_currency.getSelectedItem()).getShortName();

		if (exchangeRates.containsKey(targetCurrency))
			statusText.setText(amount + " " + baseCurrency + " sind " + exchangeRates.get(targetCurrency) * amount + " "
					+ targetCurrency);
	}

}
