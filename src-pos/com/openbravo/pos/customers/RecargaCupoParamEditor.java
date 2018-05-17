package com.openbravo.pos.customers;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.UUID;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.LocalRes;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;

public class RecargaCupoParamEditor extends JPanel implements EditorRecord {

	private DataLogicCustomers dlSalesT;
	private RecargaCupoParamPanel _recargaCupoParamPanel;

	public RecargaCupoParamEditor(DirtyManager dirty, DataLogicCustomers dlCustomers,
			RecargaCupoParamPanel recargaCupoParamPanel) {
		dlSalesT = dlCustomers;
		_recargaCupoParamPanel = recargaCupoParamPanel;
		initComponents();
		this.txtValor.addEditorKeys(this.m_jKeys);
		this.txtFecha.getDocument().addDocumentListener(dirty);
		this.cb_Activo.addActionListener(dirty);
		this.txtValor.addPropertyChangeListener("Text", dirty);
		bChActivo = false;
		writeValueEOF();
	}

	public void activate() throws BasicException {
		
		//this.txtValor.reset();
		this.cb_Activo.setSelected(false);
		bChActivo = false;

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				txtFecha.requestFocus();
			}
		});
	}

	@Override
	public Object createValue() throws BasicException {
		Object[] recargacupo = new Object[4];
		
		recargacupo[0] = m_oId == null ? UUID.randomUUID().toString() : m_oId;
		recargacupo[1] = Formats.DATE.parseValue(txtFecha.getText()); 
		recargacupo[2] = Formats.CURRENCY.parseValue(txtValor.getText(), new Double(0.0));
		recargacupo[3] = Boolean.valueOf(cb_Activo.isSelected());

		return recargacupo;
	}

	@Override
	public void writeValueEOF() {
		this.m_oId = null;
		this.txtFecha.setText(null);
		this.txtFecha.setEnabled(false);
		this.txtValor.setValue(0.0);
		this.txtValor.setEnabled(false);
		this.cb_Activo.setSelected(false);

	}

	@Override
	public void writeValueInsert() {
		this.m_oId = null;
		this.txtFecha.setText(null);
		this.txtFecha.setEnabled(true);		
		this.txtValor.setValue(new Double(0.0));
		this.txtValor.setEnabled(true);
		this.cb_Activo.setSelected(false);

	}

	@Override
	public void writeValueEdit(Object value) {
		Object[] recargacupo = (Object[]) value;
		this.m_oId = recargacupo[0];
		this.txtFecha.setText(Formats.DATE.formatValue(recargacupo[1]));
		this.txtFecha.setEnabled(true);
		this.txtValor.setValue(Double.valueOf((recargacupo[2].toString())));
		this.txtValor.setEnabled(true);
		this.cb_Activo.setSelected((Boolean) recargacupo[3]);
		this.cb_Activo.setEnabled(true);
	}

	@Override
	public void writeValueDelete(Object value) throws BasicException {
		Object[] recargacupo = (Object[]) value;
		this.m_oId = recargacupo[0];
		this.txtFecha.setText(Formats.DATE.formatValue(recargacupo[1]));
		this.txtFecha.setEnabled(false);
		this.txtValor.setValue(Double.valueOf((recargacupo[2].toString())));
		this.txtValor.setEnabled(false);
		this.cb_Activo.setSelected((Boolean) recargacupo[3]);
		this.cb_Activo.setEnabled(false);
	}

	@Override
	public Component getComponent() {
		// TODO Auto-generated method stub
		return this;
	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		lblFecha = new javax.swing.JLabel();
		m_jKeys = new com.openbravo.editor.JEditorKeys();
		txtFecha = new javax.swing.JTextField();
		cmdFecha = new javax.swing.JButton();
		lblValor = new javax.swing.JLabel();
		txtValor = new com.openbravo.editor.JEditorCurrency();
		lblActivado = new javax.swing.JLabel();
		cb_Activo = new javax.swing.JCheckBox();
		this.panelGeneral = new javax.swing.JPanel();
		this.title = javax.swing.BorderFactory.createTitledBorder("Por año, mes y día");
		this.panelGeneral.setBorder(this.title);

		this.lblAnio = new javax.swing.JLabel("Año");
		this.cbAnio = new javax.swing.JComboBox<AnioInfo>();
		this.lblMes = new javax.swing.JLabel("Mes");
		this.cbMes = new javax.swing.JComboBox<MesInfo>();
		this.chkMesTodos = new javax.swing.JCheckBox("Todos");
		this.lblDia = new javax.swing.JLabel("Día");
		this.cbDia = new javax.swing.JComboBox<DiaInfo>();
		this.lblValorG = new javax.swing.JLabel("Valor:");
		this.txtValorG = new com.openbravo.editor.JEditorCurrency();
		this.cmdGrabar = new javax.swing.JButton("Grabar");

		setLayout(null);

		lblFecha.setText(AppLocal.getIntString("label.date")); // NOI18N
		add(lblFecha);
		lblFecha.setBounds(20, 20, 80, 15);

		add(txtFecha);
		txtFecha.setBounds(120, 20, 130, 19);

		cmdFecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
		cmdFecha.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdFechaActionPerformed(evt);
			}
		});
		add(cmdFecha);
		cmdFecha.setBounds(290, 20, 40, 28);

		lblValor.setText(AppLocal.getIntString("label.valorrecarga")); // NOI18N
		add(lblValor);
		lblValor.setBounds(20, 50, 80, 15);

		txtValor.setFocusable(false);
		txtValor.setRequestFocusEnabled(false);
		add(txtValor);
		txtValor.setBounds(120, 50, 160, 19);

		m_jKeys.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
			}
		});
		add(m_jKeys);
		m_jKeys.setBounds(360, 50, 200, 200);

		lblActivado.setText(AppLocal.getIntString("label.activo")); // NOI18N
		add(lblActivado);
		lblActivado.setBounds(20, 80, 80, 15);

		add(cb_Activo);
		cb_Activo.setBounds(118, 80, 40, 19);
		cb_Activo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {

				int state = itemEvent.getStateChange();
				if (state == ItemEvent.SELECTED) {
					bChActivo = true;
				} else {
					bChActivo = false;
				}
			}
		});

		add(this.panelGeneral);
		this.panelGeneral.setLayout(null);

		this.panelGeneral.setBounds(20, 140, 330, 200);

		this.panelGeneral.add(this.lblAnio);
		this.lblAnio.setBounds(new java.awt.Rectangle(10, 20, 80, 25));
		this.panelGeneral.add(this.cbAnio);
		this.cbAnio.setBounds(new java.awt.Rectangle(100, 20, 130, 25));
		this.cbAnio.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {

				int state = itemEvent.getStateChange();
				if (state == java.awt.event.ItemEvent.SELECTED) {
					
					
				} else {
					if (state == java.awt.event.ItemEvent.DESELECTED) {
						
						
					}
				}
				
				MesInfo itemMes = (MesInfo) cbMes.getSelectedItem();
				AnioInfo anioInfo = (AnioInfo) cbAnio.getSelectedItem();
				llenaDiaPorMes(itemMes.getMes(), anioInfo.getAnio());
			}
		});
		this.panelGeneral.add(this.lblMes);
		this.lblMes.setBounds(new java.awt.Rectangle(10, 55, 80, 25));
		this.panelGeneral.add(this.cbMes);
		this.cbMes.setBounds(new java.awt.Rectangle(100, 55, 130, 25));
		this.cbMes.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {

				int state = itemEvent.getStateChange();
				if (state == java.awt.event.ItemEvent.SELECTED) {
					
					
				} else {
					if (state == java.awt.event.ItemEvent.DESELECTED) {
						
						
					}
				}
				
				MesInfo itemMes = (MesInfo) cbMes.getSelectedItem();
				AnioInfo anioInfo = (AnioInfo) cbAnio.getSelectedItem();
				llenaDiaPorMes(itemMes.getMes(), anioInfo.getAnio());
			}
		});
		this.panelGeneral.add(this.chkMesTodos);
		this.chkMesTodos.setBounds(new java.awt.Rectangle(250, 55, 70, 25));
		this.chkMesTodos.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {

				int state = itemEvent.getStateChange();
				if (state == java.awt.event.ItemEvent.SELECTED) {
					isAllMes = true;
					
					cbMes.setEnabled(false);
					llenaDiaPorMes();
				} else {
					if (state == java.awt.event.ItemEvent.DESELECTED) {
						isAllMes = false;
						
						cbMes.setEnabled(true);
						MesInfo itemMes = (MesInfo) cbMes.getSelectedItem();
						AnioInfo anioInfo = (AnioInfo) cbAnio.getSelectedItem();
						llenaDiaPorMes(itemMes.getMes(), anioInfo.getAnio());
					}
				}
			}
		});

		this.panelGeneral.add(this.lblDia);
		this.lblDia.setBounds(new java.awt.Rectangle(10, 90, 80, 25));
		this.panelGeneral.add(this.cbDia);
		this.cbDia.setBounds(new java.awt.Rectangle(100, 90, 130, 25));

		this.panelGeneral.add(this.lblValorG);
		this.lblValorG.setBounds(new java.awt.Rectangle(10, 125, 80, 25));
		this.panelGeneral.add(this.txtValorG);
		this.txtValorG.setBounds(new java.awt.Rectangle(100, 125, 130, 25));

		this.cmdGrabar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdGrabarActionPerformed(evt);
			}
		});
		this.panelGeneral.add(this.cmdGrabar);
		this.cmdGrabar.setBounds(new java.awt.Rectangle(100, 160, 130, 25));

		// Crear anios modelo
		int inicio = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
		this.listaAnio = new java.util.ArrayList<AnioInfo>();

		for (int i = inicio; i <= (inicio + 20); i++) {
			this.listaAnio.add(new AnioInfo(i));
		}

		this.cbAnio.removeAllItems();
		this.cbAnio.setModel(new javax.swing.DefaultComboBoxModel(this.listaAnio.toArray()));

		this.listaMes = new java.util.ArrayList<MesInfo>();
		for (int j = 0; j < 12; j++) {
			this.listaMes.add(new MesInfo(j));
		}
		this.cbMes.removeAllItems();
		this.cbMes.setModel(new javax.swing.DefaultComboBoxModel(this.listaMes.toArray()));

		llenaDiaPorMes();

		this.txtValorG.addEditorKeys(this.m_jKeys);
		this.txtValorG.setValue(0.0);

	}
	
	private void llenaDiaPorMes()
	{
		this.listaDia = new java.util.ArrayList<DiaInfo>();
		for (int k = 1; k < 32; k++) {
			this.listaDia.add(new DiaInfo(k));
		}
		this.cbDia.removeAllItems();
		this.cbDia.setModel(new javax.swing.DefaultComboBoxModel(this.listaDia.toArray()));
	}
	
	private void llenaDiaPorMes(int mes, int anio)
	{
		this.listaDia = new java.util.ArrayList<DiaInfo>();
		for (int k = 1; k <= getDays(anio, mes); k++) {
			this.listaDia.add(new DiaInfo(k));
		}
		this.cbDia.removeAllItems();
		this.cbDia.setModel(new javax.swing.DefaultComboBoxModel(this.listaDia.toArray()));
	}

	private void cmdFechaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jbtndateActionPerformed

		Date date;
		try {
			date = (Date) Formats.DATE.parseValue(txtFecha.getText());
		} catch (BasicException e) {
			date = null;
		}
		date = JCalendarDialog.showCalendarTime(this, date);
		if (date != null) {
			txtFecha.setText(Formats.DATE.formatValue(date));
		}
	}// GEN-LAST:event_m_jbtndateActionPerformed

	private void cmdGrabarActionPerformed(java.awt.event.ActionEvent evt) {
		String meses = "";
		this.listaCalendario = new java.util.ArrayList<CalendarioInfo>();
		this.listaMesOut = new java.util.ArrayList<MesInfo>();
		// Realizar la validación
		AnioInfo anioInfo = (AnioInfo) this.cbAnio.getSelectedItem();
		DiaInfo itemDia = ((DiaInfo) this.cbDia.getSelectedItem());

		try {
			if (this.txtValorG.getValue() > 0) {
				if (this.isAllMes) {
					javax.swing.ComboBoxModel model = this.cbMes.getModel();

					for (int m = 0; m < model.getSize(); m++) {

						MesInfo itemMes = (MesInfo) model.getElementAt(m);

						if (this.getDays(anioInfo.getAnio(), itemMes.getMes()) >= itemDia.getdia()) {
							try {
								this.listaCalendario.add(new CalendarioInfo(anioInfo.getAnio(), itemMes.getMes(),
										itemDia.getdia(), this.txtValorG.getValue()));
							} catch (BasicException e) {
								// TODO Auto-generated catch block
								MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nosave"), e);
								msg.show(this);
							}
						} else {
							this.listaMesOut.add(new MesInfo(itemMes.getMes()));
						}

					}

					if (this.listaMesOut.size() > 0) {

						for (MesInfo item : this.listaMesOut) {
							meses = meses.concat(item.getMES() + ", ");
						}
						if (meses.length() > 0)
							meses = meses.substring(0, meses.length() - 2);

					}

					if (meses.length() > 0) {
						if (JOptionPane.showConfirmDialog(this,
								"Los siguientes meses: - " + meses + " - no contienen el día seleccionado.\n\n"
										+ "\t¿Desea guardar solo los meses que contienen el día seleccionado?\n \n",
								AppLocal.getIntString("Menu.RecargaCupoParam"), JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

							this.Grabar(this.listaCalendario);
						}
					} else {
						this.Grabar(this.listaCalendario);
					}

				} else {
					MesInfo itemMes = (MesInfo) this.cbMes.getSelectedItem();

					if (this.getDays(anioInfo.getAnio(), itemMes.getMes()) >= itemDia.getdia()) {
						try {
							this.listaCalendario.add(new CalendarioInfo(anioInfo.getAnio(), itemMes.getMes(),
									itemDia.getdia(), this.txtValorG.getValue()));
							this.Grabar(this.listaCalendario);
						} catch (BasicException e) {
							MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nosave"), e);
							msg.show(this);
						}
					} else {
						JMessageDialog.showMessage(this, 
								new MessageInf(MessageInf.SGN_DANGER, 
										"  Fecha incorrecta:  El mes " + itemMes.getMES() + " no cuenta con el día seleccionado.", "Fecha incorrecta"));
					}				
				}
			}
			else
			{
				JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_IMPORTANT, " El campo - Valor - no puede ser 0.0", null));
			}
		} catch (HeadlessException e) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nosave"), e);
			msg.show(this);
		} catch (BasicException e) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nosave"), e);
			msg.show(this);
		}

		this.txtValorG.setValue(0.0);
		this.chkMesTodos.setSelected(false);
		_recargaCupoParamPanel.ordenar();

	}

	private void Grabar(java.util.List<CalendarioInfo> lista) {
		for (CalendarioInfo itemRecarga : lista) {
			// Proceder a guardar.
			try {
				this.dlSalesT.insertRecargaCupoParam(UUID.randomUUID().toString(), itemRecarga);
			} catch (BasicException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nosave"), e);
				msg.show(this);
			}
		}
	}

	public int getDays(int anio, int mes) {
		java.util.Calendar calendario = java.util.Calendar.getInstance();
		calendario.set(anio, mes, 1);
		return calendario.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

	}

	private javax.swing.JLabel lblFecha;
	private javax.swing.JTextField txtFecha;
	private javax.swing.JButton cmdFecha;
	private javax.swing.JLabel lblValor;
	private com.openbravo.editor.JEditorCurrency txtValor;
	private com.openbravo.editor.JEditorKeys m_jKeys;
	private javax.swing.JLabel lblActivado;
	private javax.swing.JCheckBox cb_Activo;
	private Boolean bChActivo = false;
	private Object m_oId;

	private javax.swing.JPanel panelGeneral;
	private javax.swing.JLabel lblAnio;
	private javax.swing.JComboBox cbAnio;
	private javax.swing.JLabel lblMes;
	private javax.swing.JComboBox cbMes;
	private javax.swing.JLabel lblDia;
	private javax.swing.JComboBox cbDia;
	private javax.swing.JLabel lblValorG;
	private com.openbravo.editor.JEditorCurrency txtValorG;
	private CalendarioInfo modeloAnio;
	private java.util.List<AnioInfo> listaAnio;
	private java.util.List<MesInfo> listaMes;
	private java.util.List<DiaInfo> listaDia;
	private java.util.List<CalendarioInfo> listaCalendario;
	private java.util.List<MesInfo> listaMesOut;
	private javax.swing.JCheckBox chkMesTodos;
	private javax.swing.JButton cmdGrabar;
	private boolean isAllMes = false;
	private javax.swing.border.TitledBorder title;

}
