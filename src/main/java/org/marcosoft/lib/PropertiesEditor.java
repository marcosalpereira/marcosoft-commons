package org.marcosoft.lib;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class PropertiesEditor extends JDialog {
	private static final long serialVersionUID = 8884030517324029091L;
	private JScrollPane jScrollPane1;
	private JButton btnCancel;
	private JButton btnOk;
	private JPanel jPanel1;
	private JTable tblProperties;
	private final PropertyValidator validator;
	private final ApplicationProperties properties;

	public PropertiesEditor(ApplicationProperties properties, PropertyValidator validator, String... propertiesToEdit) {
		initGUI();
		this.properties = properties;
		this.validator = validator;

		TableModel tblPropertiesModel = new DefaultTableModel(new String[propertiesToEdit.length][2],
				new String[] { "OpÃ§Ã£o", "Valor" });

		this.tblProperties.setModel(tblPropertiesModel);

		for (int row = 0; row < propertiesToEdit.length; row++) {
			String propertyName = propertiesToEdit[row];
			this.tblProperties.setValueAt(propertyName, row, 0);
			this.tblProperties.setValueAt(properties.getProperty(propertyName), row, 1);
		}

		this.tblProperties.getColumnModel().getColumn(0).setMaxWidth(250);
		this.tblProperties.getColumnModel().getColumn(0).setPreferredWidth(200);

		TableColumn col = this.tblProperties.getColumnModel().getColumn(1);
		col.setCellEditor(new CellEditor());

		setAlwaysOnTop(true);
		setModal(true);
		setVisible(true);
	}

	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			setDefaultCloseOperation(2);
			thisLayout.rowWeights = new double[] { 0.1D, 0.0D };
			thisLayout.rowHeights = new int[] { 7, 7 };
			thisLayout.columnWeights = new double[] { 0.1D };
			thisLayout.columnWidths = new int[] { 7 };
			getContentPane().setLayout(thisLayout);
			setTitle("OpÃ§Ãµes");

			this.jScrollPane1 = new JScrollPane();
			getContentPane().add(this.jScrollPane1,
					new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));

			TableModel tblPropertiesModel = new DefaultTableModel(new String[][] { { "", "" }, { "", "" }, { "", "" } },
					new String[] { "OpÃ§Ã£o", "Valor" });

			this.tblProperties = new JTable() {
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column) {
					return column == 1;
				}
			};
			this.jScrollPane1.setViewportView(this.tblProperties);
			this.tblProperties.setModel(tblPropertiesModel);

			this.jPanel1 = new JPanel();
			FlowLayout jPanel1Layout = new FlowLayout();
			jPanel1Layout.setAlignment(2);
			this.jPanel1.setLayout(jPanel1Layout);
			getContentPane().add(this.jPanel1,
					new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));

			this.btnCancel = new JButton();
			this.jPanel1.add(this.btnCancel);
			this.btnCancel.setPreferredSize(new Dimension(110, 22));
			this.btnCancel.setText("Cancelar");
			this.btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					PropertiesEditor.this.btnCancelActionPerformed(evt);
				}

			});
			this.btnOk = new JButton();
			this.jPanel1.add(this.btnOk);
			this.btnOk.setText("ok");
			this.btnOk.setPreferredSize(new Dimension(110, 22));
			this.btnOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					PropertiesEditor.this.btnOkActionPerformed(evt);
				}

			});
			pack();
			setSize(700, 200);
			SwingUtil.center(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void btnCancelActionPerformed(ActionEvent evt) {
		dispose();
	}

	private void btnOkActionPerformed(ActionEvent evt) {
		for (int row = 0; row < this.tblProperties.getRowCount(); row++) {
			this.properties.setProperty((String) this.tblProperties.getValueAt(row, 0),
					(String) this.tblProperties.getValueAt(row, 1));
		}
		dispose();
	}

	public class CellEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = -679122425784443569L;
		final JTextField component;
		String valorAnterior;

		public CellEditor() {
			this.component = new JTextField();
		}

		public boolean stopCellEditing() {
			try {
				String value = this.component.getText();
				if (!value.equals(this.valorAnterior)) {
					String property = (String) PropertiesEditor.this.tblProperties
							.getValueAt(PropertiesEditor.this.tblProperties.getSelectedRow(), 0);
					PropertiesEditor.this.validator.validate(property, value);
				}
			} catch (ValidatorException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				return false;
			}
			super.stopCellEditing();
			return true;
		}

		public Object getCellEditorValue() {
			return this.component.getText();
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			this.component.setText((String) value);
			this.valorAnterior = ((String) value);
			return this.component;
		}
	}
}

/*
 * Location:
 * /home/54706424372/bin/java/alarm-0.11.jar!/org/marcosoft/lib/PropertiesEditor
 * .class Java compiler version: 5 (49.0) JD-Core Version: 0.7.1
 */