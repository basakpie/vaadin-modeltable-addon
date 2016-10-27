package com.vaadin.addon.modeltable;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by gmind on 2016-10-20.
 */
public class ModelTable<T> extends VerticalLayout {

	private static final long serialVersionUID = 1L;

    private static int prevStyleHashCode;

    private int columnSize;
    private int rowSize;

    private Direction columnDirection;

    private Label title;

    private final Table targetTable;
    private final Table sourceTable;

    private final MenuBar menubar;
    private MenuBar.MenuItem menuItem;

    private HorizontalLayout toolbar;

    public ModelTable(Class<? super T> beanType) {
        this("", beanType);
    }

    public ModelTable(String caption, Class<? super T> beanType) {
        setStyles();
        this.title = new Label(caption);
        this.title.addStyleName("modeltable");
        this.columnDirection = Direction.RIGHT;
        this.menubar = new MenuBar();
        this.targetTable = new Table();
        this.sourceTable = new Table();
        this.sourceTable.setContainerDataSource(new BeanItemContainer<T>(beanType));
        Component component = buildContent();
        addComponent(component);
    }

    protected void setStyles() {
        Page page = Page.getCurrent();
        if(page==null) {
            return;
        }
        Page.Styles styles = page.getStyles();
        if(styles==null) {
            return;
        }
        if(prevStyleHashCode==styles.hashCode()) {
            return;
        }
        prevStyleHashCode = styles.hashCode();
        styles.add(".v-label-modeltable {\n" +
                "     border-left: 3px solid #00b4f0;\n" +
                "     padding-left: 10px;\n" +
                "}\n" +
                "\n" +
                ".v-table-modeltable .v-table-cell-content-key {\n" +
                "    background-color: #fafafa;\n" +
                "    background-image: -webkit-linear-gradient(top, #fafafa 2%, #efefef 98%);\n" +
                "    background-image: linear, to bottom, #fafafa 2%, #efefef 98%;\n" +
                "    white-space: nowrap;\n" +
                "    font-size: 14px;\n" +
                "    font-weight: bold;\n" +
                "    text-transform: capitalize;\n" +
                "    text-shadow: 0 1px 0 rgba(255, 255, 255, 0.05);\n" +
                "}");
    }

    private Component buildContent() {
        buildModelTable();
        Component toolbar = createToolbar();

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setWidth(100, Unit.PERCENTAGE);
        wrapper.setHeightUndefined();
        wrapper.addComponents(toolbar, targetTable);
        wrapper.setExpandRatio(targetTable, 1.0f);
        return wrapper;
    }

    private void buildModelTable() {
        targetTable.addStyleName("modeltable");
        targetTable.setWidth(100, Unit.PERCENTAGE);
        targetTable.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        targetTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        targetTable.addStyleName(ValoTheme.TABLE_SMALL);
    }

    private Component createToolbar() {
        title.addStyleName(ValoTheme.LABEL_H4);
        title.addStyleName(ValoTheme.LABEL_COLORED);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        menubar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        menubar.setAutoOpen(true);

        toolbar = new HorizontalLayout();
        toolbar.setWidth(100, Unit.PERCENTAGE);
        toolbar.addComponents(title, menubar);
        toolbar.setExpandRatio(title, 1f);
        toolbar.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        return toolbar;
    }

    public void setToolbarVisible(boolean visible) {
        toolbar.setVisible(visible);
    }

    public void setTitleCaption(String caption) {
        title.setValue(caption);
    }

    public void setTitleLabel(Label label) {
        title = label;
        title.addStyleName("modeltable");
    }

    public Object[] getVisibleColumns() {
        return sourceTable.getVisibleColumns();
    }

    public void setVisibleColumns(Object... visibleColumns) {
        sourceTable.setVisibleColumns(visibleColumns);
    }

    public String[] getColumnHeaders() {
        return sourceTable.getColumnHeaders();
    }

    public void setColumnHeaders(String... columnHeaders) {
        sourceTable.setColumnHeaders(columnHeaders);
    }

    public void setItemDirection(Direction direction, int size) {
        columnDirection = direction;
        if(columnDirection==Direction.RIGHT) {
            columnSize = size;
        } else if(columnDirection==Direction.BOTTOM) {
            rowSize = size;
        }
    }

    @SuppressWarnings("unchecked")
	public T getItem() {
        return (T) sourceTable.firstItemId();
    }

    public void setItem(Object item) {
        sourceTable.removeAllItems();
        sourceTable.addItem(item);
        if(isAttached()) {
            refreshRendered();
        }
    }

    public void removeItem() {
        sourceTable.removeAllItems();
        targetTable.removeAllItems();
        targetTable.setPageLength(0);
    }

    public void addMenuItem(String caption, MenuBar.Command command) {
        addMenuItem(caption, null, command);
    }

    public void addMenuItem(String caption, Resource icon, MenuBar.Command command) {
        if(caption == null) {
            throw new IllegalArgumentException("title cannot be null");
        } else {
            if(menuItem==null) {
                menuItem = menubar.addItem("", FontAwesome.COG, null);
            }
            if(menuItem.getSize()>1) {
                menuItem.addSeparator();
            }
            menuItem.addItem(caption, icon, command);
        }
    }

    protected void refreshRendered() {
        if(sourceTable.firstItemId()==null) {
            return;
        }

        if(rowSize==0 && columnSize==0) {
            throw new IllegalArgumentException("rowsize/columnsize must not be less than zero!");
        }

        propertyTrait();
        itemTrait();
    }

    @SuppressWarnings("unchecked")
    private void itemTrait() {
        Object sourceItemId = (T) sourceTable.firstItemId();
        Item sourceItem = sourceTable.getItem(sourceItemId);

        int pageLength = 0;

        String[] sourceHeaders = getColumnHeaders();
        Object[] sourceVisibleColumns = getVisibleColumns();

        targetTable.removeAllItems();

        int rowMaxSize = rowMaxSize();
        int columnMaxSize = columnMaxSize();

        for(int i=0; i < rowMaxSize; ) {
            Object newItem = targetTable.addItem();
            Item newRow = targetTable.getItem(newItem);

            for(int j=0, index=0; j < columnMaxSize; j++) {

                if(j==0 || columnDirection==Direction.RIGHT) {
                    index = i+j;
                } else if(columnDirection==Direction.BOTTOM) {
                    index = index + rowMaxSize;
                }

                if(index < sourceVisibleColumns.length) {
                    String header = sourceHeaders[index];
                    Object column = sourceVisibleColumns[index];

                    if(header.isEmpty()) {
                        header = String.valueOf(column);
                    }

                    Property<Object> property = sourceItem.getItemProperty(column);
                    newRow.getItemProperty("key_"+j).setValue(header);
                    newRow.getItemProperty("value_"+j).setValue(formatPropertyValue(column, property));
                }
            }

            if(columnDirection==Direction.RIGHT) {
                i = i+columnSize;
            } else if(columnDirection==Direction.BOTTOM) {
                i++;
            }

            pageLength++;
        }
        targetTable.setPageLength(pageLength);
    }

    public void setTableConverter(Object propertyId, Converter<String, ?> converter) {
        sourceTable.setConverter(propertyId, converter);
    }

    private String formatPropertyValue(Object colId, Property<Object> property) {
        Converter<String, Object> converter = sourceTable.getConverter(colId);
        if (converter==null) {
            converter = (Converter) ConverterUtil.getConverter(String.class, property.getType(), getSession());
        }
        Object value = property.getValue();
        if (converter != null) {
            return converter.convertToPresentation(value, String.class, getLocale());
        }
        return (null != value) ? value.toString() : "";
    }

    protected int rowMaxSize() {
        if(columnDirection==Direction.BOTTOM) {
            return rowSize;
        }
        return getVisibleColumns().length;
    }

    protected int columnMaxSize() {
        if(columnDirection==Direction.RIGHT) {
            return columnSize;
        }
        int size = (int) Math.ceil((double)getVisibleColumns().length / (double) rowSize);
        return size;
    }

    private void propertyTrait() {
        int columnMaxSize = columnMaxSize();

        targetTable.setContainerDataSource(null);

        for(int id=0; id < columnMaxSize; id++) {
            targetTable.addContainerProperty("key_"+id, Object.class, null);
            targetTable.addContainerProperty("value_"+id, Object.class, null);
            targetTable.setColumnAlignment("value_"+id, Table.Align.RIGHT);
        }

        targetTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
            
			private static final long serialVersionUID = 1L;

			@Override
            public String getStyle(Table table, Object itemId, Object propertyId) {
                if(propertyId==null) {
                    return null;
                }
                String columnName = (String)propertyId;
                if (columnName.startsWith("key_")) {
                    return "key";
                }
                return null;
            }
        });
    }

    @Override
    public void attach() {
        refreshRendered();
        super.attach();
    }

    public enum Direction {
        RIGHT,
        BOTTOM
    }

    protected Table sourceTable() {
        return this.sourceTable;
    }

    protected Table targetTable() {
        return this.targetTable;
    }


}
