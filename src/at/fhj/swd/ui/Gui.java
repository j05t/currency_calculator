package at.fhj.swd.ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.JSeparator;
import java.awt.Font;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import at.fhj.swd.service.CurService;

public class Gui {

	private JFrame frmWhrungsrechner;
	private JProgressBar progressBar;
	private JComboBox<CurrencyInfo> source_currency;
	private JComboBox<CurrencyInfo> target_currency;
	private JTextField statusText;
	private JTextField input;

	private CurService curService;

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
		curService = new CurService();
	}

	private void loadCurrencies() {
		curService.setApiUrl("http://houston.fh-joanneum.at/sodev2/currencies");
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				progressBar.setIndeterminate(true);
				for (CurrencyInfo curInfo : curService.getCurrencies())
					source_currency.addItem(curInfo);

				progressBar.setIndeterminate(false);
				statusText.setText("Währungen geladen");
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
			}
		});

		JLabel lblTargetCurrency = new JLabel("Zielwährung:");
		panel.add(lblTargetCurrency);

		target_currency = new JComboBox<CurrencyInfo>();
		panel.add(target_currency);
		target_currency.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadExchangeRates((CurrencyInfo) source_currency.getSelectedItem());
			}
		});

		JSeparator separator = new JSeparator();
		panel.add(separator);

		JLabel lblAmount = new JLabel("Betrag:");
		panel.add(lblAmount);

		input = new JTextField();
		panel.add(input);
		input.setColumns(10);

		JSeparator separator_1 = new JSeparator();
		panel.add(separator_1);

		statusText = new JTextField();
		statusText.setFont(new Font("Dialog", Font.PLAIN, 14));
		statusText.setHorizontalAlignment(SwingConstants.CENTER);
		statusText.setText("Lade verfügbare Währungen..");
		statusText.setEditable(false);
		panel.add(statusText);

		progressBar = new JProgressBar();
		panel.add(progressBar);
	}

	private void loadExchangeRates(CurrencyInfo selectedCurrency) {
		String baseCurrency = selectedCurrency.getShortName();
		String targetCurrency = ((CurrencyInfo) target_currency.getSelectedItem()).getShortName();
				
		// http://houston.fh-joanneum.at/sodev2/rates?baseCurrency=EUR
		curService.setApiUrl("http://houston.fh-joanneum.at/sodev2/rates?baseCurrency=" + baseCurrency);
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				progressBar.setIndeterminate(true);
				
				Map<String,Float> map = curService.getRatesForCurrency(baseCurrency);
				
				if (map.containsKey(targetCurrency))
					System.out.println("1 " + baseCurrency + " sind " + map.get(targetCurrency) + " " + targetCurrency);
				
				progressBar.setIndeterminate(false);
				statusText.setText("Wechselkurse geladen");
			}
		});

		t.start();
	}

	private void updateTargetCurrencies(String selectedItem) {
		target_currency.removeAllItems();

		for (int i = 0; i < source_currency.getItemCount(); i++)
			if (!source_currency.getItemAt(i).getFullName().equals(selectedItem))
				target_currency.addItem(source_currency.getItemAt(i));

		//target_currency.invalidate();
	}

}
