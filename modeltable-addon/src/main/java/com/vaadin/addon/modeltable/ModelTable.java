package com.vaadin.addon.modeltable;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.data.util.converter.ConverterUtil;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by basakpie on 2017-05-18.
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

    @SuppressWarnings("unused")
	private final Map<String, Object> generatedColumnMap;

    @SuppressWarnings("unused")
	private Table.CellStyleGenerator cellStyleGenerator;

    private final KeyPairMapper<Object, Object> keyPairMapper;

    public ModelTable(Class<? super T> beanType) {
        this("", beanType);
    }

    public ModelTable(String caption, Class<? super T> beanType) {
        this.keyPairMapper = new KeyPairMapper<>();
        this.generatedColumnMap = new HashMap<>();
        this.title = new Label(caption);
        this.title.addStyleName("modeltable");
        this.columnDirection = Direction.RIGHT;
        this.menubar = new MenuBar();
        this.targetTable = new Table();
        this.sourceTable = new Table();
        this.sourceTable.setContainerDataSource(new BeanItemContainer<T>(beanType));
        VerticalLayout component = buildContent();
        addComponent(component);
    }

    private VerticalLayout buildContent() {
        buildModelTable();
        HorizontalLayout toolbar = createToolbar();

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

    private HorizontalLayout createToolbar() {
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
        sourceTable.getContainerDataSource().addItem(item);
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
                    String headerName = sourceHeaders[index];
                    Object columnId = sourceVisibleColumns[index];

                    if(headerName.isEmpty()) {
                        headerName = String.valueOf(columnId);
                    }

                    Property<Object> property = sourceItem.getItemProperty(columnId);

                    if(property!=null) {
                        Property<Object> propertyKey = newRow.getItemProperty("key_"+j);
                        propertyKey.setValue(headerName);

                        Property<Object> propertyValue = newRow.getItemProperty("value_"+j);
                        propertyValue.setValue(formatPropertyValue(columnId, property));

                        keyPairMapper.put(columnId, newItem+"-value_"+j);
                    }
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

    public void addTableGneratedColumn(Object id, Table.ColumnGenerator generatedColumn) {
        sourceTable.addGeneratedColumn(id, generatedColumn);
    }

    public void removeTableGneratedColumn(Object id) {
        sourceTable.removeGeneratedColumn(id);
    }

    public void setTableCellStyleGenerator(Table.CellStyleGenerator cellStyleGenerator) {
        sourceTable.setCellStyleGenerator(cellStyleGenerator);
    }

    public void getTableCellStyleGenerator() {
        sourceTable.getCellStyleGenerator();
    }

    public void setTableEditable(boolean editable) {
        targetTable.setEditable(true);
    }

	@SuppressWarnings({"unchecked", "rawtypes"})
	public Object formatPropertyValue(Object columnId, Property property) {
        Converter<String, Object> converter = sourceTable.getConverter(columnId);
        if (converter==null) {
            converter = ConverterUtil.getConverter(String.class, property.getType(), getSession());
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

    @SuppressWarnings({"serial", "rawtypes"})
	private void propertyTrait() {
        int columnMaxSize = columnMaxSize();

        for(int id=0; id < columnMaxSize; id++) {
            targetTable.addContainerProperty("key_"+id, Object.class, null);
            targetTable.addContainerProperty("value_"+id, Object.class, null);
            targetTable.removeGeneratedColumn("key_"+id);
            targetTable.removeGeneratedColumn("value_"+id);

            targetTable.addGeneratedColumn("key_"+id, new Table.ColumnGenerator() {
                @Override
                public Label generateCell(Table source, Object itemId, Object columnId) {
                    Property property = source.getItem(itemId).getItemProperty(columnId);
                    if(property==null) {
                        return null;
                    }

                    if(property.getValue()==null) {
                        return null;
                    }

                    String html = "&middot; "+  property.getValue();
                    Label label = new Label(html, ContentMode.HTML);
                    label.setSizeUndefined();

                    return label;
                }
            });

            targetTable.addGeneratedColumn("value_"+id, new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
					Property property = source.getItem(itemId).getItemProperty(columnId);
                    if(property==null) {
                        return null;
                    }
                    Object sourceColumnId = keyPairMapper.getSourceColumnId(itemId+"-"+columnId);
                    if(sourceColumnId!=null) {
                        Table.ColumnGenerator columnGenerator = sourceTable.getColumnGenerator(sourceColumnId);
                        if(columnGenerator!=null) {
                            Object cell = columnGenerator.generateCell(sourceTable, sourceTable.firstItemId(), sourceColumnId);
                            return cell;
                        }
                    }
                    Object value = property.getValue();
                    return value;
                }
            });
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

                Object sourceColumnId = keyPairMapper.getSourceColumnId(itemId+"-"+propertyId);
                if(sourceColumnId!=null) {
                    Table.CellStyleGenerator cellStyleGenerator = sourceTable.getCellStyleGenerator();
                    if(cellStyleGenerator!=null) {
                        return sourceTable.getCellStyleGenerator().getStyle(sourceTable, sourceTable.firstItemId(), sourceColumnId);
                    }
                }
                return null;
            }
        });
    }

    @Override
    public void attach() {
        super.attach();
        refreshRendered();
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
