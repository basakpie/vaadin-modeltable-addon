package com.vaadin.addon.modeltable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import com.vaadin.v7.ui.Table;
import com.vaadin.v7.data.Property;

@SpringUI
@Theme("demo")
@Title("ModelTable Add-on Demo")
@Widgetset("com.vaadin.addon.modeltable.DemoWidgetSet")
@SuppressWarnings("serial")
public class DemoUI extends UI {

	public static Map<Integer, Item> itemFieldMap = new HashMap<>();

	@Override
	protected void init(VaadinRequest request){

		ModelTable<Item> table1 = buildModelTable(0);
		table1.setTitleCaption("Direction.RIGHT [ ColumnSize: " + 3 + "]");
		table1.setItemDirection(ModelTable.Direction.RIGHT, 3);
		table1.addTableGneratedColumn("field01", new Table.ColumnGenerator() {
			@SuppressWarnings("rawtypes")
			@Override
			public Label generateCell(Table source, Object itemId, Object columnId) {
				Random random = new Random();
				Property property = source.getContainerProperty(itemId, columnId);
				String color = random.nextBoolean() == true ? "#2dd085" : "#f54993";
				String iconTag = "<span class=\"v-icon\" style=\"font-family: "
						+ FontAwesome.CIRCLE.getFontFamily() + ";color:" + color
						+ "\">&#x"
						+ Integer.toHexString(FontAwesome.CIRCLE.getCodepoint())
						+ ";</span>";

				String html = iconTag + " "+  table1.formatPropertyValue(columnId, property);

				Label label = new Label(html, ContentMode.HTML);
				label.setSizeUndefined();

				return label;
			}
		});


		table1.addTableGneratedColumn("field07", new Table.ColumnGenerator() {
			@SuppressWarnings("rawtypes")
			@Override
			public Label generateCell(Table source, Object itemId, Object columnId) {
				Property property = source.getContainerProperty(itemId, columnId);
				return new Label(String.valueOf(property.getValue()));
			}
		});


		table1.setTableCellStyleGenerator(new Table.CellStyleGenerator() {
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				String cellId = (String)propertyId;
				if (cellId.equals("field02")) {
					return "red";
				}
				return null;
			}
		});

		ModelTable<Item> table2 = buildModelTable(1);
		table2.setTitleCaption("Direction.BOTTOM [ RowSize: " + 3 + "]");
		table2.setItemDirection(ModelTable.Direction.BOTTOM, 3);

		table2.addTableGneratedColumn("field03", new Table.ColumnGenerator() {
			@SuppressWarnings("rawtypes")
			@Override
			public Label generateCell(Table source, Object itemId, Object columnId) {
				Random random = new Random();

				Property property = source.getContainerProperty(itemId, columnId);
				String color = random.nextBoolean() == true ? "#2dd085" : "#f54993";
				String iconTag = "<span class=\"v-icon\" style=\"font-family: "
						+ FontAwesome.CIRCLE.getFontFamily() + ";color:" + color
						+ "\">&#x"
						+ Integer.toHexString(FontAwesome.CIRCLE.getCodepoint())
						+ ";</span>";

				String html = iconTag + " "+  table1.formatPropertyValue(columnId, property);

				Label label = new Label(html, ContentMode.HTML);
				label.setSizeUndefined();
				return label;
			}
		});

		table2.addTableGneratedColumn("field07", new Table.ColumnGenerator() {
			@SuppressWarnings("rawtypes")
			@Override
			public Label generateCell(Table source, Object itemId, Object columnId) {
				Property property = source.getContainerProperty(itemId, columnId);
				return new Label(String.valueOf(property.getValue()));
			}
		});


		table2.setTableCellStyleGenerator(new Table.CellStyleGenerator() {
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				String cellId = (String)propertyId;
				if (cellId.equals("field01")) {
					return "green";
				}
				return null;
			}
		});

		ModelTable<Item> table3 = buildModelTable(2);
		table3.setTitleCaption("Direction.RIGHT [ ColumnSize: " + 3 + "]");
		table3.setItemDirection(ModelTable.Direction.RIGHT, 3);
		table3.setVisibleColumns("field01", "field02", "field03", "field04", "field05", "field07", "field08", "field14");
		table3.setColumnHeaders("필드01", "필드02", "필드03", "필드04", "필드05", "필드07", "필드08", "필드14");


		ModelTable<Item> table4 = buildModelTable(3);
		table4.setTitleCaption("Direction.BOTTOM [ RowSize: " + 3 + "]");
		table4.setItemDirection(ModelTable.Direction.BOTTOM, 3);
		table4.setVisibleColumns("field01", "field02", "field03", "field04", "field05", "field07", "field08", "field14");
		table4.setColumnHeaders("필드01", "필드02", "필드03", "필드04", "필드05", "필드07", "필드08", "필드14");

		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(table1);
		layout.addComponent(table2);
		layout.addComponent(table3);
		layout.addComponent(table4);

		setContent(layout);
	}

	private ModelTable<Item> buildModelTable(int dataIndex) {
		Item item = itemFieldMap.get(dataIndex);
		ModelTable<Item> modelTable = new ModelTable<>(Item.class);
		modelTable.setItem(item);
		modelTable.addMenuItem("Edit", FontAwesome.EDIT, new MenuBar.Command() {
			@Override
			public void menuSelected(MenuBar.MenuItem menuItem) {
				item.setField01("Edit :" + dataIndex);
				modelTable.setItem(item);
				Notification.show("Edit : " + dataIndex);
			}
		});
		modelTable.addMenuItem("Reload", FontAwesome.CIRCLE_THIN, new MenuBar.Command() {
			@Override
			public void menuSelected(MenuBar.MenuItem menuItem) {
				Notification.show("Refresh[removeItem] : " + dataIndex);
				modelTable.removeItem();
			}
		});
		return modelTable;
	}

}
