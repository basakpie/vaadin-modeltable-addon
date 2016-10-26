package com.vaadin.addon.modeltable;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("demo")
@Title("ModelTable Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI{

	public static Map<Integer, ItemField> itemFieldMap = new HashMap<>();

	@Override
	protected void init(VaadinRequest request){

		ModelTable<ItemField> table1 = buildModelTable(0);
		table1.setTitleCaption("Direction.RIGHT [ ColumnSize: " + 3 + "]");
		table1.setItemDirection(ModelTable.Direction.RIGHT, 3);

		ModelTable<ItemField> table2 = buildModelTable(1);
		table2.setTitleCaption("Direction.BOTTOM [ RowSize: " + 3 + "]");
		table2.setItemDirection(ModelTable.Direction.BOTTOM, 3);

		ModelTable<ItemField> table3 = buildModelTable(2);
		table3.setTitleCaption("Direction.RIGHT [ ColumnSize: " + 3 + "]");
		table3.setItemDirection(ModelTable.Direction.RIGHT, 3);
		table3.setVisibleColumns("field01", "field02", "field03", "field04", "field05", "field07", "field08", "field14");
		table3.setColumnHeaders("필드01", "필드02", "필드03", "필드04", "필드05", "필드07", "필드08", "필드14");

		ModelTable<ItemField> table4 = buildModelTable(3);
		table4.setTitleCaption("Direction.BOTTOM [ RowSize: " + 3 + "]");
		table4.setItemDirection(ModelTable.Direction.BOTTOM, 3);
		table4.setVisibleColumns("field01", "field02", "field03", "field04", "field05", "field07", "field08", "field14");
		table4.setColumnHeaders("필드01", "필드02", "필드03", "필드04", "필드05", "필드07", "필드08", "필드14");

		VerticalLayout layout = new VerticalLayout();
		layout.setWidth(100, Unit.PERCENTAGE);
		layout.setSpacing(true);
		layout.addComponent(table1);
		layout.addComponent(table2);
		layout.addComponent(table3);
		layout.addComponent(table4);
		setContent(layout);
		setHeightUndefined();
	}

	private ModelTable<ItemField> buildModelTable(int dataIndex) {
		ItemField itemField = itemFieldMap.get(dataIndex);
		ModelTable<ItemField> modelTable = new ModelTable<>(ItemField.class);
		modelTable.setItem(itemField);
		modelTable.addMenuItem("Edit", FontAwesome.EDIT, new MenuBar.Command() {
			@Override
			public void menuSelected(MenuBar.MenuItem menuItem) {
				itemField.setField01("Edit :" + dataIndex);
				modelTable.setItem(itemField);
				Notification.show("Edit : " + dataIndex);
			}
		});
		modelTable.addMenuItem("Reload", FontAwesome.CIRCLE_O_NOTCH, new MenuBar.Command() {
			@Override
			public void menuSelected(MenuBar.MenuItem menuItem) {
				Notification.show("Refresh[removeItem] : " + dataIndex);
				modelTable.removeItem();
			}
		});
		return modelTable;
	}
}
