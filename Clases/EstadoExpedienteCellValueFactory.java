package com.lucas.proyecto.Clases;


import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class EstadoExpedienteCellValueFactory implements Callback<TableColumn.CellDataFeatures<Expediente, String>, TableCell<Expediente, String>> {

	@Override
	public TableCell<Expediente, String> call(TableColumn.CellDataFeatures<Expediente, String> param) {
		return new TableCell<Expediente, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					Expediente expediente = param.getValue();
					if (expediente.isEstado()) {
						setText("Abierto");
					} else {
						setText("Cerrado");
					}
				}
			}
		};
	}
}